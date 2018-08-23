package com.openchat.secureim.controllers;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.auth.AuthenticationCredentials;
import com.openchat.secureim.auth.AuthorizationHeader;
import com.openchat.secureim.auth.InvalidAuthorizationHeaderException;
import com.openchat.secureim.entities.AccountAttributes;
import com.openchat.secureim.entities.ApnRegistrationId;
import com.openchat.secureim.entities.GcmRegistrationId;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.sms.SenderFactory;
import com.openchat.secureim.sms.TwilioSmsSender;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.PendingAccountsManager;
import com.openchat.secureim.util.Util;
import com.openchat.secureim.util.VerificationCode;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Path("/v1/accounts")
public class AccountController {

  private final Logger logger = LoggerFactory.getLogger(AccountController.class);

  private final PendingAccountsManager pendingAccounts;
  private final AccountsManager        accounts;
  private final RateLimiters           rateLimiters;
  private final SenderFactory          senderFactory;

  public AccountController(PendingAccountsManager pendingAccounts,
                           AccountsManager accounts,
                           RateLimiters rateLimiters,
                           SenderFactory smsSenderFactory)
  {
    this.pendingAccounts = pendingAccounts;
    this.accounts        = accounts;
    this.rateLimiters    = rateLimiters;
    this.senderFactory   = smsSenderFactory;
  }

  @Timed
  @GET
  @Path("/{transport}/code/{number}")
  public Response createAccount(@PathParam("transport") String transport,
                                @PathParam("number")    String number)
      throws IOException, RateLimitExceededException
  {
    if (!Util.isValidNumber(number)) {
      logger.debug("Invalid number: " + number);
      throw new WebApplicationException(Response.status(400).build());
    }

    switch (transport) {
      case "sms":
        rateLimiters.getSmsDestinationLimiter().validate(number);
        break;
      case "voice":
        rateLimiters.getVoiceDestinationLimiter().validate(number);
        break;
      default:
        throw new WebApplicationException(Response.status(415).build());
    }

    VerificationCode verificationCode = generateVerificationCode();
    pendingAccounts.store(number, verificationCode.getVerificationCode());

    if (transport.equals("sms")) {
      senderFactory.getSmsSender(number).deliverSmsVerification(number, verificationCode.getVerificationCodeDisplay());
    } else if (transport.equals("voice")) {
      senderFactory.getVoxSender(number).deliverVoxVerification(number, verificationCode.getVerificationCodeSpeech());
    }

    return Response.ok().build();
  }

  @Timed
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/code/{verification_code}")
  public void verifyAccount(@PathParam("verification_code") String verificationCode,
                            @HeaderParam("Authorization")   String authorizationHeader,
                            @Valid                          AccountAttributes accountAttributes)
      throws RateLimitExceededException
  {
    try {
      AuthorizationHeader header = new AuthorizationHeader(authorizationHeader);
      String number              = header.getUserName();
      String password            = header.getPassword();

      rateLimiters.getVerifyLimiter().validate(number);

      Optional<String> storedVerificationCode = pendingAccounts.getCodeForNumber(number);

      if (!storedVerificationCode.isPresent() ||
          !verificationCode.equals(storedVerificationCode.get()))
      {
        throw new WebApplicationException(Response.status(403).build());
      }

      Account account = new Account();
      account.setNumber(number);
      account.setAuthenticationCredentials(new AuthenticationCredentials(password));
      account.setSignalingKey(accountAttributes.getSignalingKey());
      account.setSupportsSms(accountAttributes.getSupportsSms());

      accounts.create(account);
      logger.debug("Stored account...");

    } catch (InvalidAuthorizationHeaderException e) {
      logger.info("Bad Authorization Header", e);
      throw new WebApplicationException(Response.status(401).build());
    }
  }

  @Timed
  @PUT
  @Path("/gcm/")
  @Consumes(MediaType.APPLICATION_JSON)
  public void setGcmRegistrationId(@Auth Account account, @Valid GcmRegistrationId registrationId)  {
    account.setApnRegistrationId(null);
    account.setGcmRegistrationId(registrationId.getGcmRegistrationId());
    accounts.update(account);
  }

  @Timed
  @DELETE
  @Path("/gcm/")
  public void deleteGcmRegistrationId(@Auth Account account) {
    account.setGcmRegistrationId(null);
    accounts.update(account);
  }

  @Timed
  @PUT
  @Path("/apn/")
  @Consumes(MediaType.APPLICATION_JSON)
  public void setApnRegistrationId(@Auth Account account, @Valid ApnRegistrationId registrationId) {
    account.setApnRegistrationId(registrationId.getApnRegistrationId());
    account.setGcmRegistrationId(null);
    accounts.update(account);
  }

  @Timed
  @DELETE
  @Path("/apn/")
  public void deleteApnRegistrationId(@Auth Account account) {
    account.setApnRegistrationId(null);
    accounts.update(account);
  }

  @Timed
  @POST
  @Path("/voice/twiml/{code}")
  @Produces(MediaType.APPLICATION_XML)
  public Response getTwiml(@PathParam("code") String encodedVerificationText) {
    return Response.ok().entity(String.format(TwilioSmsSender.SAY_TWIML,
                                              SenderFactory.VoxSender.VERIFICATION_TEXT +
                                                  encodedVerificationText)).build();
  }

  private VerificationCode generateVerificationCode() {
    try {
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      int randomInt       = 100000 + random.nextInt(900000);
      return new VerificationCode(randomInt);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

}
