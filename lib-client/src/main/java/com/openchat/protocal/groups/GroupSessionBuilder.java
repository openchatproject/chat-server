package com.openchat.protocal.groups;

import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidKeyIdException;
import com.openchat.protocal.groups.state.SenderKeyRecord;
import com.openchat.protocal.groups.state.SenderKeyState;
import com.openchat.protocal.groups.state.SenderKeyStore;
import com.openchat.protocal.protocol.SenderKeyDistributionMessage;
import com.openchat.protocal.util.KeyHelper;



public class GroupSessionBuilder {

  private final SenderKeyStore senderKeyStore;

  public GroupSessionBuilder(SenderKeyStore senderKeyStore) {
    this.senderKeyStore = senderKeyStore;
  }

  
  public void process(SenderKeyName senderKeyName, SenderKeyDistributionMessage senderKeyDistributionMessage) {
    synchronized (GroupCipher.LOCK) {
      SenderKeyRecord senderKeyRecord = senderKeyStore.loadSenderKey(senderKeyName);
      senderKeyRecord.addSenderKeyState(senderKeyDistributionMessage.getId(),
                                        senderKeyDistributionMessage.getIteration(),
                                        senderKeyDistributionMessage.getChainKey(),
                                        senderKeyDistributionMessage.getSignatureKey());
      senderKeyStore.storeSenderKey(senderKeyName, senderKeyRecord);
    }
  }

  
  public SenderKeyDistributionMessage create(SenderKeyName senderKeyName) {
    synchronized (GroupCipher.LOCK) {
      try {
        SenderKeyRecord senderKeyRecord = senderKeyStore.loadSenderKey(senderKeyName);

        if (senderKeyRecord.isEmpty()) {
          senderKeyRecord.setSenderKeyState(KeyHelper.generateSenderKeyId(),
                                            0,
                                            KeyHelper.generateSenderKey(),
                                            KeyHelper.generateSenderSigningKey());
          senderKeyStore.storeSenderKey(senderKeyName, senderKeyRecord);
        }

        SenderKeyState state = senderKeyRecord.getSenderKeyState();

        return new SenderKeyDistributionMessage(state.getKeyId(),
                                                state.getSenderChainKey().getIteration(),
                                                state.getSenderChainKey().getSeed(),
                                                state.getSigningKeyPublic());

      } catch (InvalidKeyIdException | InvalidKeyException e) {
        throw new AssertionError(e);
      }
    }
  }
}
