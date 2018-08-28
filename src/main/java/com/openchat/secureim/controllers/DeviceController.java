package com.openchat.secureim.controllers;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.auth.AuthenticationCredentials;
import com.openchat.secureim.auth.AuthorizationHeader;
import com.openchat.secureim.auth.InvalidAuthorizationHeaderException;
import com.openchat.secureim.entities.AccountAttributes;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.PendingDevicesManager;
import com.openchat.secureim.util.VerificationCode;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Path("/v1/devices")
public class DeviceController {

  private final Logger logger = LoggerFactory.getLogger(DeviceController.class);

  private final PendingDevicesManager pendingDevices;
  private final AccountsManager       accounts;
  private final RateLimiters          rateLimiters;

  public DeviceController(PendingDevicesManager pendingDevices,
                          AccountsManager accounts,
                          RateLimiters rateLimiters)
  {
    this.pendingDevices  = pendingDevices;
    this.accounts        = accounts;
    this.rateLimiters    = rateLimiters;
  }

  @Timed
  @GET
  @Path("/provisioning_code")
  @Produces(MediaType.APPLICATION_JSON)
  public VerificationCode createDeviceToken(@Auth Device device)
      throws RateLimitExceededException
  {
    rateLimiters.getVerifyLimiter().validate(device.getNumber()); //TODO: New limiter?

    VerificationCode verificationCode = generateVerificationCode();
    pendingDevices.store(device.getNumber(), verificationCode.getVerificationCode());

    return verificationCode;
  }

  @Timed
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/{verification_code}")
  public long verifyDeviceToken(@PathParam("verification_code") String verificationCode,
                                @HeaderParam("Authorization")   String authorizationHeader,
                                @Valid                          AccountAttributes accountAttributes)
      throws RateLimitExceededException
  {
    Device device;
    try {
      AuthorizationHeader header = AuthorizationHeader.fromFullHeader(authorizationHeader);
      String number              = header.getNumber();
      String password            = header.getPassword();

      rateLimiters.getVerifyLimiter().validate(number); //TODO: New limiter?

      Optional<String> storedVerificationCode = pendingDevices.getCodeForNumber(number);

      if (!storedVerificationCode.isPresent() ||
          !verificationCode.equals(storedVerificationCode.get()))
      {
        throw new WebApplicationException(Response.status(403).build());
      }

      device = new Device();
      device.setNumber(number);
      device.setAuthenticationCredentials(new AuthenticationCredentials(password));
      device.setSignalingKey(accountAttributes.getSignalingKey());
      device.setSupportsSms(accountAttributes.getSupportsSms());
      device.setFetchesMessages(accountAttributes.getFetchesMessages());

      accounts.createAccountOnExistingNumber(device);

      pendingDevices.remove(number);

      logger.debug("Stored new device device...");
    } catch (InvalidAuthorizationHeaderException e) {
      logger.info("Bad Authorization Header", e);
      throw new WebApplicationException(Response.status(401).build());
    }

    return device.getDeviceId();
  }

  @VisibleForTesting protected VerificationCode generateVerificationCode() {
    try {
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      int randomInt       = 100000 + random.nextInt(900000);
      return new VerificationCode(randomInt);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }
}
