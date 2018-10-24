package com.openchat.messaging.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.nurkiewicz.asyncretry.AsyncRetryExecutor;
import com.nurkiewicz.asyncretry.RetryContext;
import com.nurkiewicz.asyncretry.RetryExecutor;
import com.nurkiewicz.asyncretry.function.RetryCallable;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import com.openchat.messaging.server.internal.GcmResponseEntity;
import com.openchat.messaging.server.internal.GcmResponseListEntity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;


public class Sender {

  private static final String PRODUCTION_URL = "https://android.googleapis.com/gcm/send";

  private final CloseableHttpAsyncClient client;
  private final String                   authorizationHeader;
  private final RetryExecutor            executor;
  private final String                   url;

  
  public Sender(String apiKey) {
    this(apiKey, 10);
  }

  
  public Sender(String apiKey, int retryCount) {
    this(apiKey, retryCount, PRODUCTION_URL);
  }

  @VisibleForTesting
  public Sender(String apiKey, int retryCount, String url) {
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    this.url                 = url;
    this.authorizationHeader = String.format("key=%s", apiKey);

    this.client = HttpAsyncClients.custom()
                                  .setMaxConnTotal(100)
                                  .setMaxConnPerRoute(10)
                                  .build();

    this.executor = new AsyncRetryExecutor(scheduler).retryOn(ServerFailedException.class)
                                                     .retryOn(TimeoutException.class)
                                                     .retryOn(IOException.class)
                                                     .withExponentialBackoff(100, 2.0)
                                                     .withUniformJitter()
                                                     .withMaxDelay(4000)
                                                     .withMaxRetries(retryCount);

    this.client.start();
  }

  
  public ListenableFuture<Result> send(Message message) {
    return send(message, null);
  }

  
  public ListenableFuture<Result> send(final Message message, final Object requestContext) {
    return executor.getFutureWithRetry(new RetryCallable<ListenableFuture<Result>>() {
      @Override
      public ListenableFuture<Result> call(RetryContext context) throws Exception {
        SettableFuture<Result> future  = SettableFuture.create();
        HttpPost               request = new HttpPost(url);

        request.setHeader("Authorization", authorizationHeader);
        request.setEntity(new StringEntity(message.serialize(),
                                           ContentType.parse("application/json")));

        client.execute(request, new ResponseHandler(future, requestContext));

        return future;
      }
    });
  }

  
  public void stop() throws IOException {
    this.client.close();
  }

  private static final class ResponseHandler implements FutureCallback<HttpResponse> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final SettableFuture<Result> future;
    private final Object                 requestContext;

    public ResponseHandler(SettableFuture<Result> future, Object requestContext) {
      this.future         = future;
      this.requestContext = requestContext;
    }

    @Override
    public void completed(HttpResponse result) {
      try {
        String responseBody = EntityUtils.toString(result.getEntity());

        switch (result.getStatusLine().getStatusCode()) {
          case 400: future.setException(new InvalidRequestException());       break;
          case 401: future.setException(new AuthenticationFailedException()); break;
          case 204:
          case 200: future.set(parseResult(responseBody));                    break;
          default:  future.setException(new ServerFailedException("Bad status: " + result.getStatusLine().getStatusCode()));
        }
      } catch (IOException e) {
        future.setException(e);
      }
    }

    @Override
    public void failed(Exception ex) {
      future.setException(ex);
    }

    @Override
    public void cancelled() {
      future.setException(new ServerFailedException("Canceled!"));
    }

    private Result parseResult(String body) throws IOException {
      List<GcmResponseEntity> responseList = objectMapper.readValue(body, GcmResponseListEntity.class)
                                                         .getResults();

      if (responseList == null || responseList.size() == 0) {
        throw new IOException("Empty response list!");
      }

      GcmResponseEntity responseEntity = responseList.get(0);

      return new Result(this.requestContext,
                        responseEntity.getCanonicalRegistrationId(),
                        responseEntity.getMessageId(),
                        responseEntity.getError());
    }
  }
}
