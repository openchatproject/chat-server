package com.openchat.secureim.controllers;

import com.openchat.secureim.storage.AccountsManager;

public class FederationController {

  protected final AccountsManager      accounts;
  protected final AttachmentController attachmentController;
  protected final MessageController    messageController;

  public FederationController(AccountsManager accounts,
                              AttachmentController attachmentController,
                              MessageController messageController)
  {
    this.accounts             = accounts;
    this.attachmentController = attachmentController;
    this.messageController    = messageController;
  }
}
