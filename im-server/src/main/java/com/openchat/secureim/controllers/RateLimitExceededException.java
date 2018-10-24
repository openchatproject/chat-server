package com.openchat.secureim.controllers;

public class RateLimitExceededException extends Exception {
  public RateLimitExceededException(String number) {
    super(number);
  }
}
