package com.openchat.push.controllers;

import com.codahale.metrics.annotation.Timed;

import com.openchat.push.auth.Server;
import com.openchat.push.entities.UnregisteredEventList;
import com.openchat.push.senders.UnregisteredQueue;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.auth.Auth;

@Path("/api/v1/feedback")
public class FeedbackController {

  private final UnregisteredQueue gcmQueue;
  private final UnregisteredQueue apnQueue;

  public FeedbackController(UnregisteredQueue gcmQueue, UnregisteredQueue apnQueue) {
    this.gcmQueue = gcmQueue;
    this.apnQueue = apnQueue;
  }

  @Timed
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/gcm/")
  public UnregisteredEventList getUnregisteredGcmDevices(@Auth Server server) {
    return new UnregisteredEventList(gcmQueue.get(server.getName()));
  }

  @Timed
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/apn/")
  public UnregisteredEventList getUnregisteredApnDevices(@Auth Server server) {
    return new UnregisteredEventList(apnQueue.get(server.getName()));
  }

}
