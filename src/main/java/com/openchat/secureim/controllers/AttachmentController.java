package com.openchat.secureim.controllers;

import com.amazonaws.HttpMethod;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.AttachmentDescriptor;
import com.openchat.secureim.entities.AttachmentUri;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.federation.NoSuchPeerException;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.util.Conversions;
import com.openchat.secureim.util.UrlSigner;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


@Path("/v1/attachments")
public class AttachmentController {

  private final Logger logger = LoggerFactory.getLogger(AttachmentController.class);

  private final RateLimiters           rateLimiters;
  private final FederatedClientManager federatedClientManager;
  private final UrlSigner              urlSigner;

  public AttachmentController(RateLimiters rateLimiters,
                              FederatedClientManager federatedClientManager,
                              UrlSigner urlSigner)
  {
    this.rateLimiters           = rateLimiters;
    this.federatedClientManager = federatedClientManager;
    this.urlSigner              = urlSigner;
  }

  @Timed
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response allocateAttachment(@Auth Account account) throws RateLimitExceededException {
    rateLimiters.getAttachmentLimiter().validate(account.getNumber());

    long                 attachmentId = generateAttachmentId();
    URL                  url          = urlSigner.getPreSignedUrl(attachmentId, HttpMethod.PUT);
    AttachmentDescriptor descriptor   = new AttachmentDescriptor(attachmentId, url.toExternalForm());

    return Response.ok().entity(descriptor).build();
  }

  @Timed
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{attachmentId}")
  public Response redirectToAttachment(@Auth                      Account account,
                                       @PathParam("attachmentId") long attachmentId,
                                       @QueryParam("relay")       String relay)
  {
    try {
      URL url;

      if (relay == null) url = urlSigner.getPreSignedUrl(attachmentId, HttpMethod.GET);
      else               url = federatedClientManager.getClient(relay).getSignedAttachmentUri(attachmentId);

      return Response.ok().entity(new AttachmentUri(url)).build();
    } catch (IOException e) {
      logger.warn("No conectivity", e);
      return Response.status(500).build();
    } catch (NoSuchPeerException e) {
      logger.info("No such peer: " + relay);
      return Response.status(404).build();
    }
  }

  private long generateAttachmentId() {
    try {
      byte[] attachmentBytes = new byte[8];
      SecureRandom.getInstance("SHA1PRNG").nextBytes(attachmentBytes);

      attachmentBytes[0] = (byte)(attachmentBytes[0] & 0x7F);
      return Conversions.byteArrayToLong(attachmentBytes);
    } catch (NoSuchAlgorithmException nsae) {
      throw new AssertionError(nsae);
    }
  }
}
