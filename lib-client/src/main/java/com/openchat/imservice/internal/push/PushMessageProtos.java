package com.openchat.imservice.internal.push;

public final class PushMessageProtos {
  private PushMessageProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface IncomingPushMessageOpenchatOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasType();
    
    com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type getType();

    
    boolean hasSource();
    
    java.lang.String getSource();
    
    com.google.protobuf.ByteString
        getSourceBytes();

    
    boolean hasSourceDevice();
    
    int getSourceDevice();

    
    boolean hasRelay();
    
    java.lang.String getRelay();
    
    com.google.protobuf.ByteString
        getRelayBytes();

    
    boolean hasTimestamp();
    
    long getTimestamp();

    
    boolean hasMessage();
    
    com.google.protobuf.ByteString getMessage();
  }
  
  public static final class IncomingPushMessageOpenchat extends
      com.google.protobuf.GeneratedMessage
      implements IncomingPushMessageOpenchatOrBuilder {
    private IncomingPushMessageOpenchat(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private IncomingPushMessageOpenchat(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final IncomingPushMessageOpenchat defaultInstance;
    public static IncomingPushMessageOpenchat getDefaultInstance() {
      return defaultInstance;
    }

    public IncomingPushMessageOpenchat getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private IncomingPushMessageOpenchat(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              int rawValue = input.readEnum();
              com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type value = com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                type_ = value;
              }
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              source_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000008;
              relay_ = input.readBytes();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              timestamp_ = input.readUInt64();
              break;
            }
            case 50: {
              bitField0_ |= 0x00000020;
              message_ = input.readBytes();
              break;
            }
            case 56: {
              bitField0_ |= 0x00000004;
              sourceDevice_ = input.readUInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_IncomingPushMessageOpenchat_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_IncomingPushMessageOpenchat_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.class, com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Builder.class);
    }

    public static com.google.protobuf.Parser<IncomingPushMessageOpenchat> PARSER =
        new com.google.protobuf.AbstractParser<IncomingPushMessageOpenchat>() {
      public IncomingPushMessageOpenchat parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new IncomingPushMessageOpenchat(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<IncomingPushMessageOpenchat> getParserForType() {
      return PARSER;
    }

    
    public enum Type
        implements com.google.protobuf.ProtocolMessageEnum {
      
      UNKNOWN(0, 0),
      
      CIPHERTEXT(1, 1),
      
      KEY_EXCHANGE(2, 2),
      
      PREKEY_BUNDLE(3, 3),
      
      RECEIPT(4, 5),
      ;

      
      public static final int UNKNOWN_VALUE = 0;
      
      public static final int CIPHERTEXT_VALUE = 1;
      
      public static final int KEY_EXCHANGE_VALUE = 2;
      
      public static final int PREKEY_BUNDLE_VALUE = 3;
      
      public static final int RECEIPT_VALUE = 5;

      public final int getNumber() { return value; }

      public static Type valueOf(int value) {
        switch (value) {
          case 0: return UNKNOWN;
          case 1: return CIPHERTEXT;
          case 2: return KEY_EXCHANGE;
          case 3: return PREKEY_BUNDLE;
          case 5: return RECEIPT;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<Type>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<Type>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<Type>() {
              public Type findValueByNumber(int number) {
                return Type.valueOf(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(index);
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.getDescriptor().getEnumTypes().get(0);
      }

      private static final Type[] VALUES = values();

      public static Type valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int index;
      private final int value;

      private Type(int index, int value) {
        this.index = index;
        this.value = value;
      }

    }

    private int bitField0_;
    public static final int TYPE_FIELD_NUMBER = 1;
    private com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type type_;
    
    public boolean hasType() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    
    public com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type getType() {
      return type_;
    }

    public static final int SOURCE_FIELD_NUMBER = 2;
    private java.lang.Object source_;
    
    public boolean hasSource() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    
    public java.lang.String getSource() {
      java.lang.Object ref = source_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          source_ = s;
        }
        return s;
      }
    }
    
    public com.google.protobuf.ByteString
        getSourceBytes() {
      java.lang.Object ref = source_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        source_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int SOURCEDEVICE_FIELD_NUMBER = 7;
    private int sourceDevice_;
    
    public boolean hasSourceDevice() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    
    public int getSourceDevice() {
      return sourceDevice_;
    }

    public static final int RELAY_FIELD_NUMBER = 3;
    private java.lang.Object relay_;
    
    public boolean hasRelay() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public java.lang.String getRelay() {
      java.lang.Object ref = relay_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          relay_ = s;
        }
        return s;
      }
    }
    
    public com.google.protobuf.ByteString
        getRelayBytes() {
      java.lang.Object ref = relay_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        relay_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TIMESTAMP_FIELD_NUMBER = 5;
    private long timestamp_;
    
    public boolean hasTimestamp() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    
    public long getTimestamp() {
      return timestamp_;
    }

    public static final int MESSAGE_FIELD_NUMBER = 6;
    private com.google.protobuf.ByteString message_;
    
    public boolean hasMessage() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    
    public com.google.protobuf.ByteString getMessage() {
      return message_;
    }

    private void initFields() {
      type_ = com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type.UNKNOWN;
      source_ = "";
      sourceDevice_ = 0;
      relay_ = "";
      timestamp_ = 0L;
      message_ = com.google.protobuf.ByteString.EMPTY;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeEnum(1, type_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getSourceBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(3, getRelayBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeUInt64(5, timestamp_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(6, message_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeUInt32(7, sourceDevice_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(1, type_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getSourceBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getRelayBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(5, timestamp_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, message_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(7, sourceDevice_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchatOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_IncomingPushMessageOpenchat_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_IncomingPushMessageOpenchat_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.class, com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        type_ = com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type.UNKNOWN;
        bitField0_ = (bitField0_ & ~0x00000001);
        source_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        sourceDevice_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        relay_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        timestamp_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000010);
        message_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_IncomingPushMessageOpenchat_descriptor;
      }

      public com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat build() {
        com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat buildPartial() {
        com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat result = new com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.type_ = type_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.source_ = source_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.sourceDevice_ = sourceDevice_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.relay_ = relay_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.timestamp_ = timestamp_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.message_ = message_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat) {
          return mergeFrom((com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat other) {
        if (other == com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.getDefaultInstance()) return this;
        if (other.hasType()) {
          setType(other.getType());
        }
        if (other.hasSource()) {
          bitField0_ |= 0x00000002;
          source_ = other.source_;
          onChanged();
        }
        if (other.hasSourceDevice()) {
          setSourceDevice(other.getSourceDevice());
        }
        if (other.hasRelay()) {
          bitField0_ |= 0x00000008;
          relay_ = other.relay_;
          onChanged();
        }
        if (other.hasTimestamp()) {
          setTimestamp(other.getTimestamp());
        }
        if (other.hasMessage()) {
          setMessage(other.getMessage());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type type_ = com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type.UNKNOWN;
      
      public boolean hasType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type getType() {
        return type_;
      }
      
      public Builder setType(com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        type_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearType() {
        bitField0_ = (bitField0_ & ~0x00000001);
        type_ = com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type.UNKNOWN;
        onChanged();
        return this;
      }

      private java.lang.Object source_ = "";
      
      public boolean hasSource() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public java.lang.String getSource() {
        java.lang.Object ref = source_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          source_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      
      public com.google.protobuf.ByteString
          getSourceBytes() {
        java.lang.Object ref = source_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          source_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      
      public Builder setSource(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        source_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearSource() {
        bitField0_ = (bitField0_ & ~0x00000002);
        source_ = getDefaultInstance().getSource();
        onChanged();
        return this;
      }
      
      public Builder setSourceBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        source_ = value;
        onChanged();
        return this;
      }

      private int sourceDevice_ ;
      
      public boolean hasSourceDevice() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public int getSourceDevice() {
        return sourceDevice_;
      }
      
      public Builder setSourceDevice(int value) {
        bitField0_ |= 0x00000004;
        sourceDevice_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearSourceDevice() {
        bitField0_ = (bitField0_ & ~0x00000004);
        sourceDevice_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object relay_ = "";
      
      public boolean hasRelay() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public java.lang.String getRelay() {
        java.lang.Object ref = relay_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          relay_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      
      public com.google.protobuf.ByteString
          getRelayBytes() {
        java.lang.Object ref = relay_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          relay_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      
      public Builder setRelay(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        relay_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearRelay() {
        bitField0_ = (bitField0_ & ~0x00000008);
        relay_ = getDefaultInstance().getRelay();
        onChanged();
        return this;
      }
      
      public Builder setRelayBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        relay_ = value;
        onChanged();
        return this;
      }

      private long timestamp_ ;
      
      public boolean hasTimestamp() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      
      public long getTimestamp() {
        return timestamp_;
      }
      
      public Builder setTimestamp(long value) {
        bitField0_ |= 0x00000010;
        timestamp_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearTimestamp() {
        bitField0_ = (bitField0_ & ~0x00000010);
        timestamp_ = 0L;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString message_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasMessage() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      
      public com.google.protobuf.ByteString getMessage() {
        return message_;
      }
      
      public Builder setMessage(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        message_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearMessage() {
        bitField0_ = (bitField0_ & ~0x00000020);
        message_ = getDefaultInstance().getMessage();
        onChanged();
        return this;
      }

    }

    static {
      defaultInstance = new IncomingPushMessageOpenchat(true);
      defaultInstance.initFields();
    }

  }

  public interface PushMessageContentOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasBody();
    
    java.lang.String getBody();
    
    com.google.protobuf.ByteString
        getBodyBytes();

    
    java.util.List<com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer> 
        getAttachmentsList();
    
    com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer getAttachments(int index);
    
    int getAttachmentsCount();
    
    java.util.List<? extends com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder> 
        getAttachmentsOrBuilderList();
    
    com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder getAttachmentsOrBuilder(
        int index);

    
    boolean hasGroup();
    
    com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext getGroup();
    
    com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContextOrBuilder getGroupOrBuilder();

    
    boolean hasFlags();
    
    int getFlags();

    
    boolean hasSync();
    
    com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext getSync();
    
    com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContextOrBuilder getSyncOrBuilder();
  }
  
  public static final class PushMessageContent extends
      com.google.protobuf.GeneratedMessage
      implements PushMessageContentOrBuilder {
    private PushMessageContent(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private PushMessageContent(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final PushMessageContent defaultInstance;
    public static PushMessageContent getDefaultInstance() {
      return defaultInstance;
    }

    public PushMessageContent getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private PushMessageContent(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              body_ = input.readBytes();
              break;
            }
            case 18: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                attachments_ = new java.util.ArrayList<com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer>();
                mutable_bitField0_ |= 0x00000002;
              }
              attachments_.add(input.readMessage(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.PARSER, extensionRegistry));
              break;
            }
            case 26: {
              com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = group_.toBuilder();
              }
              group_ = input.readMessage(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(group_);
                group_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
            case 32: {
              bitField0_ |= 0x00000004;
              flags_ = input.readUInt32();
              break;
            }
            case 42: {
              com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = sync_.toBuilder();
              }
              sync_ = input.readMessage(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(sync_);
                sync_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          attachments_ = java.util.Collections.unmodifiableList(attachments_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.class, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.Builder.class);
    }

    public static com.google.protobuf.Parser<PushMessageContent> PARSER =
        new com.google.protobuf.AbstractParser<PushMessageContent>() {
      public PushMessageContent parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new PushMessageContent(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<PushMessageContent> getParserForType() {
      return PARSER;
    }

    
    public enum Flags
        implements com.google.protobuf.ProtocolMessageEnum {
      
      END_SESSION(0, 1),
      ;

      
      public static final int END_SESSION_VALUE = 1;

      public final int getNumber() { return value; }

      public static Flags valueOf(int value) {
        switch (value) {
          case 1: return END_SESSION;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<Flags>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<Flags>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<Flags>() {
              public Flags findValueByNumber(int number) {
                return Flags.valueOf(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(index);
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.getDescriptor().getEnumTypes().get(0);
      }

      private static final Flags[] VALUES = values();

      public static Flags valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int index;
      private final int value;

      private Flags(int index, int value) {
        this.index = index;
        this.value = value;
      }

    }

    public interface AttachmentPointerOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasId();
      
      long getId();

      
      boolean hasContentType();
      
      java.lang.String getContentType();
      
      com.google.protobuf.ByteString
          getContentTypeBytes();

      
      boolean hasKey();
      
      com.google.protobuf.ByteString getKey();
    }
    
    public static final class AttachmentPointer extends
        com.google.protobuf.GeneratedMessage
        implements AttachmentPointerOrBuilder {
      private AttachmentPointer(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private AttachmentPointer(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final AttachmentPointer defaultInstance;
      public static AttachmentPointer getDefaultInstance() {
        return defaultInstance;
      }

      public AttachmentPointer getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private AttachmentPointer(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        initFields();
        int mutable_bitField0_ = 0;
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
            com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              default: {
                if (!parseUnknownField(input, unknownFields,
                                       extensionRegistry, tag)) {
                  done = true;
                }
                break;
              }
              case 9: {
                bitField0_ |= 0x00000001;
                id_ = input.readFixed64();
                break;
              }
              case 18: {
                bitField0_ |= 0x00000002;
                contentType_ = input.readBytes();
                break;
              }
              case 26: {
                bitField0_ |= 0x00000004;
                key_ = input.readBytes();
                break;
              }
            }
          }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(
              e.getMessage()).setUnfinishedMessage(this);
        } finally {
          this.unknownFields = unknownFields.build();
          makeExtensionsImmutable();
        }
      }
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_AttachmentPointer_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_AttachmentPointer_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.class, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder.class);
      }

      public static com.google.protobuf.Parser<AttachmentPointer> PARSER =
          new com.google.protobuf.AbstractParser<AttachmentPointer>() {
        public AttachmentPointer parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new AttachmentPointer(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<AttachmentPointer> getParserForType() {
        return PARSER;
      }

      private int bitField0_;
      public static final int ID_FIELD_NUMBER = 1;
      private long id_;
      
      public boolean hasId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public long getId() {
        return id_;
      }

      public static final int CONTENTTYPE_FIELD_NUMBER = 2;
      private java.lang.Object contentType_;
      
      public boolean hasContentType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public java.lang.String getContentType() {
        java.lang.Object ref = contentType_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            contentType_ = s;
          }
          return s;
        }
      }
      
      public com.google.protobuf.ByteString
          getContentTypeBytes() {
        java.lang.Object ref = contentType_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          contentType_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public static final int KEY_FIELD_NUMBER = 3;
      private com.google.protobuf.ByteString key_;
      
      public boolean hasKey() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public com.google.protobuf.ByteString getKey() {
        return key_;
      }

      private void initFields() {
        id_ = 0L;
        contentType_ = "";
        key_ = com.google.protobuf.ByteString.EMPTY;
      }
      private byte memoizedIsInitialized = -1;
      public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized != -1) return isInitialized == 1;

        memoizedIsInitialized = 1;
        return true;
      }

      public void writeTo(com.google.protobuf.CodedOutputStream output)
                          throws java.io.IOException {
        getSerializedSize();
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          output.writeFixed64(1, id_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeBytes(2, getContentTypeBytes());
        }
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          output.writeBytes(3, key_);
        }
        getUnknownFields().writeTo(output);
      }

      private int memoizedSerializedSize = -1;
      public int getSerializedSize() {
        int size = memoizedSerializedSize;
        if (size != -1) return size;

        size = 0;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          size += com.google.protobuf.CodedOutputStream
            .computeFixed64Size(1, id_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(2, getContentTypeBytes());
        }
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(3, key_);
        }
        size += getUnknownFields().getSerializedSize();
        memoizedSerializedSize = size;
        return size;
      }

      private static final long serialVersionUID = 0L;
      @java.lang.Override
      protected java.lang.Object writeReplace()
          throws java.io.ObjectStreamException {
        return super.writeReplace();
      }

      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer prototype) {
        return newBuilder().mergeFrom(prototype);
      }
      public Builder toBuilder() { return newBuilder(this); }

      @java.lang.Override
      protected Builder newBuilderForType(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
      }
      
      public static final class Builder extends
          com.google.protobuf.GeneratedMessage.Builder<Builder>
         implements com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_AttachmentPointer_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_AttachmentPointer_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.class, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder.class);
        }

        private Builder() {
          maybeForceBuilderInitialization();
        }

        private Builder(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
          super(parent);
          maybeForceBuilderInitialization();
        }
        private void maybeForceBuilderInitialization() {
          if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          }
        }
        private static Builder create() {
          return new Builder();
        }

        public Builder clear() {
          super.clear();
          id_ = 0L;
          bitField0_ = (bitField0_ & ~0x00000001);
          contentType_ = "";
          bitField0_ = (bitField0_ & ~0x00000002);
          key_ = com.google.protobuf.ByteString.EMPTY;
          bitField0_ = (bitField0_ & ~0x00000004);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_AttachmentPointer_descriptor;
        }

        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer build() {
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer buildPartial() {
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer result = new com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.id_ = id_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.contentType_ = contentType_;
          if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
            to_bitField0_ |= 0x00000004;
          }
          result.key_ = key_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer) {
            return mergeFrom((com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer other) {
          if (other == com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.getDefaultInstance()) return this;
          if (other.hasId()) {
            setId(other.getId());
          }
          if (other.hasContentType()) {
            bitField0_ |= 0x00000002;
            contentType_ = other.contentType_;
            onChanged();
          }
          if (other.hasKey()) {
            setKey(other.getKey());
          }
          this.mergeUnknownFields(other.getUnknownFields());
          return this;
        }

        public final boolean isInitialized() {
          return true;
        }

        public Builder mergeFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private long id_ ;
        
        public boolean hasId() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }
        
        public long getId() {
          return id_;
        }
        
        public Builder setId(long value) {
          bitField0_ |= 0x00000001;
          id_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearId() {
          bitField0_ = (bitField0_ & ~0x00000001);
          id_ = 0L;
          onChanged();
          return this;
        }

        private java.lang.Object contentType_ = "";
        
        public boolean hasContentType() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        
        public java.lang.String getContentType() {
          java.lang.Object ref = contentType_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            contentType_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }
        
        public com.google.protobuf.ByteString
            getContentTypeBytes() {
          java.lang.Object ref = contentType_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            contentType_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }
        
        public Builder setContentType(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
          contentType_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearContentType() {
          bitField0_ = (bitField0_ & ~0x00000002);
          contentType_ = getDefaultInstance().getContentType();
          onChanged();
          return this;
        }
        
        public Builder setContentTypeBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
          contentType_ = value;
          onChanged();
          return this;
        }

        private com.google.protobuf.ByteString key_ = com.google.protobuf.ByteString.EMPTY;
        
        public boolean hasKey() {
          return ((bitField0_ & 0x00000004) == 0x00000004);
        }
        
        public com.google.protobuf.ByteString getKey() {
          return key_;
        }
        
        public Builder setKey(com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
          key_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearKey() {
          bitField0_ = (bitField0_ & ~0x00000004);
          key_ = getDefaultInstance().getKey();
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new AttachmentPointer(true);
        defaultInstance.initFields();
      }

    }

    public interface GroupContextOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasId();
      
      com.google.protobuf.ByteString getId();

      
      boolean hasType();
      
      com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type getType();

      
      boolean hasName();
      
      java.lang.String getName();
      
      com.google.protobuf.ByteString
          getNameBytes();

      
      java.util.List<java.lang.String>
      getMembersList();
      
      int getMembersCount();
      
      java.lang.String getMembers(int index);
      
      com.google.protobuf.ByteString
          getMembersBytes(int index);

      
      boolean hasAvatar();
      
      com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer getAvatar();
      
      com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder getAvatarOrBuilder();
    }
    
    public static final class GroupContext extends
        com.google.protobuf.GeneratedMessage
        implements GroupContextOrBuilder {
      private GroupContext(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private GroupContext(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final GroupContext defaultInstance;
      public static GroupContext getDefaultInstance() {
        return defaultInstance;
      }

      public GroupContext getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private GroupContext(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        initFields();
        int mutable_bitField0_ = 0;
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
            com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              default: {
                if (!parseUnknownField(input, unknownFields,
                                       extensionRegistry, tag)) {
                  done = true;
                }
                break;
              }
              case 10: {
                bitField0_ |= 0x00000001;
                id_ = input.readBytes();
                break;
              }
              case 16: {
                int rawValue = input.readEnum();
                com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type value = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type.valueOf(rawValue);
                if (value == null) {
                  unknownFields.mergeVarintField(2, rawValue);
                } else {
                  bitField0_ |= 0x00000002;
                  type_ = value;
                }
                break;
              }
              case 26: {
                bitField0_ |= 0x00000004;
                name_ = input.readBytes();
                break;
              }
              case 34: {
                if (!((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
                  members_ = new com.google.protobuf.LazyStringArrayList();
                  mutable_bitField0_ |= 0x00000008;
                }
                members_.add(input.readBytes());
                break;
              }
              case 42: {
                com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder subBuilder = null;
                if (((bitField0_ & 0x00000008) == 0x00000008)) {
                  subBuilder = avatar_.toBuilder();
                }
                avatar_ = input.readMessage(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.PARSER, extensionRegistry);
                if (subBuilder != null) {
                  subBuilder.mergeFrom(avatar_);
                  avatar_ = subBuilder.buildPartial();
                }
                bitField0_ |= 0x00000008;
                break;
              }
            }
          }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(
              e.getMessage()).setUnfinishedMessage(this);
        } finally {
          if (((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
            members_ = new com.google.protobuf.UnmodifiableLazyStringList(members_);
          }
          this.unknownFields = unknownFields.build();
          makeExtensionsImmutable();
        }
      }
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_GroupContext_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_GroupContext_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.class, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Builder.class);
      }

      public static com.google.protobuf.Parser<GroupContext> PARSER =
          new com.google.protobuf.AbstractParser<GroupContext>() {
        public GroupContext parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new GroupContext(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<GroupContext> getParserForType() {
        return PARSER;
      }

      
      public enum Type
          implements com.google.protobuf.ProtocolMessageEnum {
        
        UNKNOWN(0, 0),
        
        UPDATE(1, 1),
        
        DELIVER(2, 2),
        
        QUIT(3, 3),
        ;

        
        public static final int UNKNOWN_VALUE = 0;
        
        public static final int UPDATE_VALUE = 1;
        
        public static final int DELIVER_VALUE = 2;
        
        public static final int QUIT_VALUE = 3;

        public final int getNumber() { return value; }

        public static Type valueOf(int value) {
          switch (value) {
            case 0: return UNKNOWN;
            case 1: return UPDATE;
            case 2: return DELIVER;
            case 3: return QUIT;
            default: return null;
          }
        }

        public static com.google.protobuf.Internal.EnumLiteMap<Type>
            internalGetValueMap() {
          return internalValueMap;
        }
        private static com.google.protobuf.Internal.EnumLiteMap<Type>
            internalValueMap =
              new com.google.protobuf.Internal.EnumLiteMap<Type>() {
                public Type findValueByNumber(int number) {
                  return Type.valueOf(number);
                }
              };

        public final com.google.protobuf.Descriptors.EnumValueDescriptor
            getValueDescriptor() {
          return getDescriptor().getValues().get(index);
        }
        public final com.google.protobuf.Descriptors.EnumDescriptor
            getDescriptorForType() {
          return getDescriptor();
        }
        public static final com.google.protobuf.Descriptors.EnumDescriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.getDescriptor().getEnumTypes().get(0);
        }

        private static final Type[] VALUES = values();

        public static Type valueOf(
            com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
          if (desc.getType() != getDescriptor()) {
            throw new java.lang.IllegalArgumentException(
              "EnumValueDescriptor is not for this type.");
          }
          return VALUES[desc.getIndex()];
        }

        private final int index;
        private final int value;

        private Type(int index, int value) {
          this.index = index;
          this.value = value;
        }

      }

      private int bitField0_;
      public static final int ID_FIELD_NUMBER = 1;
      private com.google.protobuf.ByteString id_;
      
      public boolean hasId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.google.protobuf.ByteString getId() {
        return id_;
      }

      public static final int TYPE_FIELD_NUMBER = 2;
      private com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type type_;
      
      public boolean hasType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type getType() {
        return type_;
      }

      public static final int NAME_FIELD_NUMBER = 3;
      private java.lang.Object name_;
      
      public boolean hasName() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            name_ = s;
          }
          return s;
        }
      }
      
      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public static final int MEMBERS_FIELD_NUMBER = 4;
      private com.google.protobuf.LazyStringList members_;
      
      public java.util.List<java.lang.String>
          getMembersList() {
        return members_;
      }
      
      public int getMembersCount() {
        return members_.size();
      }
      
      public java.lang.String getMembers(int index) {
        return members_.get(index);
      }
      
      public com.google.protobuf.ByteString
          getMembersBytes(int index) {
        return members_.getByteString(index);
      }

      public static final int AVATAR_FIELD_NUMBER = 5;
      private com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer avatar_;
      
      public boolean hasAvatar() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer getAvatar() {
        return avatar_;
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder getAvatarOrBuilder() {
        return avatar_;
      }

      private void initFields() {
        id_ = com.google.protobuf.ByteString.EMPTY;
        type_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type.UNKNOWN;
        name_ = "";
        members_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        avatar_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.getDefaultInstance();
      }
      private byte memoizedIsInitialized = -1;
      public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized != -1) return isInitialized == 1;

        memoizedIsInitialized = 1;
        return true;
      }

      public void writeTo(com.google.protobuf.CodedOutputStream output)
                          throws java.io.IOException {
        getSerializedSize();
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          output.writeBytes(1, id_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeEnum(2, type_.getNumber());
        }
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          output.writeBytes(3, getNameBytes());
        }
        for (int i = 0; i < members_.size(); i++) {
          output.writeBytes(4, members_.getByteString(i));
        }
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          output.writeMessage(5, avatar_);
        }
        getUnknownFields().writeTo(output);
      }

      private int memoizedSerializedSize = -1;
      public int getSerializedSize() {
        int size = memoizedSerializedSize;
        if (size != -1) return size;

        size = 0;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(1, id_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeEnumSize(2, type_.getNumber());
        }
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(3, getNameBytes());
        }
        {
          int dataSize = 0;
          for (int i = 0; i < members_.size(); i++) {
            dataSize += com.google.protobuf.CodedOutputStream
              .computeBytesSizeNoTag(members_.getByteString(i));
          }
          size += dataSize;
          size += 1 * getMembersList().size();
        }
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          size += com.google.protobuf.CodedOutputStream
            .computeMessageSize(5, avatar_);
        }
        size += getUnknownFields().getSerializedSize();
        memoizedSerializedSize = size;
        return size;
      }

      private static final long serialVersionUID = 0L;
      @java.lang.Override
      protected java.lang.Object writeReplace()
          throws java.io.ObjectStreamException {
        return super.writeReplace();
      }

      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext prototype) {
        return newBuilder().mergeFrom(prototype);
      }
      public Builder toBuilder() { return newBuilder(this); }

      @java.lang.Override
      protected Builder newBuilderForType(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
      }
      
      public static final class Builder extends
          com.google.protobuf.GeneratedMessage.Builder<Builder>
         implements com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContextOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_GroupContext_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_GroupContext_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.class, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Builder.class);
        }

        private Builder() {
          maybeForceBuilderInitialization();
        }

        private Builder(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
          super(parent);
          maybeForceBuilderInitialization();
        }
        private void maybeForceBuilderInitialization() {
          if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
            getAvatarFieldBuilder();
          }
        }
        private static Builder create() {
          return new Builder();
        }

        public Builder clear() {
          super.clear();
          id_ = com.google.protobuf.ByteString.EMPTY;
          bitField0_ = (bitField0_ & ~0x00000001);
          type_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type.UNKNOWN;
          bitField0_ = (bitField0_ & ~0x00000002);
          name_ = "";
          bitField0_ = (bitField0_ & ~0x00000004);
          members_ = com.google.protobuf.LazyStringArrayList.EMPTY;
          bitField0_ = (bitField0_ & ~0x00000008);
          if (avatarBuilder_ == null) {
            avatar_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.getDefaultInstance();
          } else {
            avatarBuilder_.clear();
          }
          bitField0_ = (bitField0_ & ~0x00000010);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_GroupContext_descriptor;
        }

        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext build() {
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext buildPartial() {
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext result = new com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.id_ = id_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.type_ = type_;
          if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
            to_bitField0_ |= 0x00000004;
          }
          result.name_ = name_;
          if (((bitField0_ & 0x00000008) == 0x00000008)) {
            members_ = new com.google.protobuf.UnmodifiableLazyStringList(
                members_);
            bitField0_ = (bitField0_ & ~0x00000008);
          }
          result.members_ = members_;
          if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
            to_bitField0_ |= 0x00000008;
          }
          if (avatarBuilder_ == null) {
            result.avatar_ = avatar_;
          } else {
            result.avatar_ = avatarBuilder_.build();
          }
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext) {
            return mergeFrom((com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext other) {
          if (other == com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.getDefaultInstance()) return this;
          if (other.hasId()) {
            setId(other.getId());
          }
          if (other.hasType()) {
            setType(other.getType());
          }
          if (other.hasName()) {
            bitField0_ |= 0x00000004;
            name_ = other.name_;
            onChanged();
          }
          if (!other.members_.isEmpty()) {
            if (members_.isEmpty()) {
              members_ = other.members_;
              bitField0_ = (bitField0_ & ~0x00000008);
            } else {
              ensureMembersIsMutable();
              members_.addAll(other.members_);
            }
            onChanged();
          }
          if (other.hasAvatar()) {
            mergeAvatar(other.getAvatar());
          }
          this.mergeUnknownFields(other.getUnknownFields());
          return this;
        }

        public final boolean isInitialized() {
          return true;
        }

        public Builder mergeFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private com.google.protobuf.ByteString id_ = com.google.protobuf.ByteString.EMPTY;
        
        public boolean hasId() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }
        
        public com.google.protobuf.ByteString getId() {
          return id_;
        }
        
        public Builder setId(com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          id_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearId() {
          bitField0_ = (bitField0_ & ~0x00000001);
          id_ = getDefaultInstance().getId();
          onChanged();
          return this;
        }

        private com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type type_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type.UNKNOWN;
        
        public boolean hasType() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        
        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type getType() {
          return type_;
        }
        
        public Builder setType(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type value) {
          if (value == null) {
            throw new NullPointerException();
          }
          bitField0_ |= 0x00000002;
          type_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearType() {
          bitField0_ = (bitField0_ & ~0x00000002);
          type_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type.UNKNOWN;
          onChanged();
          return this;
        }

        private java.lang.Object name_ = "";
        
        public boolean hasName() {
          return ((bitField0_ & 0x00000004) == 0x00000004);
        }
        
        public java.lang.String getName() {
          java.lang.Object ref = name_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            name_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }
        
        public com.google.protobuf.ByteString
            getNameBytes() {
          java.lang.Object ref = name_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            name_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }
        
        public Builder setName(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
          name_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearName() {
          bitField0_ = (bitField0_ & ~0x00000004);
          name_ = getDefaultInstance().getName();
          onChanged();
          return this;
        }
        
        public Builder setNameBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
          name_ = value;
          onChanged();
          return this;
        }

        private com.google.protobuf.LazyStringList members_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        private void ensureMembersIsMutable() {
          if (!((bitField0_ & 0x00000008) == 0x00000008)) {
            members_ = new com.google.protobuf.LazyStringArrayList(members_);
            bitField0_ |= 0x00000008;
           }
        }
        
        public java.util.List<java.lang.String>
            getMembersList() {
          return java.util.Collections.unmodifiableList(members_);
        }
        
        public int getMembersCount() {
          return members_.size();
        }
        
        public java.lang.String getMembers(int index) {
          return members_.get(index);
        }
        
        public com.google.protobuf.ByteString
            getMembersBytes(int index) {
          return members_.getByteString(index);
        }
        
        public Builder setMembers(
            int index, java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  ensureMembersIsMutable();
          members_.set(index, value);
          onChanged();
          return this;
        }
        
        public Builder addMembers(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  ensureMembersIsMutable();
          members_.add(value);
          onChanged();
          return this;
        }
        
        public Builder addAllMembers(
            java.lang.Iterable<java.lang.String> values) {
          ensureMembersIsMutable();
          super.addAll(values, members_);
          onChanged();
          return this;
        }
        
        public Builder clearMembers() {
          members_ = com.google.protobuf.LazyStringArrayList.EMPTY;
          bitField0_ = (bitField0_ & ~0x00000008);
          onChanged();
          return this;
        }
        
        public Builder addMembersBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  ensureMembersIsMutable();
          members_.add(value);
          onChanged();
          return this;
        }

        private com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer avatar_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.getDefaultInstance();
        private com.google.protobuf.SingleFieldBuilder<
            com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder> avatarBuilder_;
        
        public boolean hasAvatar() {
          return ((bitField0_ & 0x00000010) == 0x00000010);
        }
        
        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer getAvatar() {
          if (avatarBuilder_ == null) {
            return avatar_;
          } else {
            return avatarBuilder_.getMessage();
          }
        }
        
        public Builder setAvatar(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer value) {
          if (avatarBuilder_ == null) {
            if (value == null) {
              throw new NullPointerException();
            }
            avatar_ = value;
            onChanged();
          } else {
            avatarBuilder_.setMessage(value);
          }
          bitField0_ |= 0x00000010;
          return this;
        }
        
        public Builder setAvatar(
            com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder builderForValue) {
          if (avatarBuilder_ == null) {
            avatar_ = builderForValue.build();
            onChanged();
          } else {
            avatarBuilder_.setMessage(builderForValue.build());
          }
          bitField0_ |= 0x00000010;
          return this;
        }
        
        public Builder mergeAvatar(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer value) {
          if (avatarBuilder_ == null) {
            if (((bitField0_ & 0x00000010) == 0x00000010) &&
                avatar_ != com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.getDefaultInstance()) {
              avatar_ =
                com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.newBuilder(avatar_).mergeFrom(value).buildPartial();
            } else {
              avatar_ = value;
            }
            onChanged();
          } else {
            avatarBuilder_.mergeFrom(value);
          }
          bitField0_ |= 0x00000010;
          return this;
        }
        
        public Builder clearAvatar() {
          if (avatarBuilder_ == null) {
            avatar_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.getDefaultInstance();
            onChanged();
          } else {
            avatarBuilder_.clear();
          }
          bitField0_ = (bitField0_ & ~0x00000010);
          return this;
        }
        
        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder getAvatarBuilder() {
          bitField0_ |= 0x00000010;
          onChanged();
          return getAvatarFieldBuilder().getBuilder();
        }
        
        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder getAvatarOrBuilder() {
          if (avatarBuilder_ != null) {
            return avatarBuilder_.getMessageOrBuilder();
          } else {
            return avatar_;
          }
        }
        
        private com.google.protobuf.SingleFieldBuilder<
            com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder> 
            getAvatarFieldBuilder() {
          if (avatarBuilder_ == null) {
            avatarBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder>(
                    avatar_,
                    getParentForChildren(),
                    isClean());
            avatar_ = null;
          }
          return avatarBuilder_;
        }

      }

      static {
        defaultInstance = new GroupContext(true);
        defaultInstance.initFields();
      }

    }

    public interface SyncMessageContextOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasDestination();
      
      java.lang.String getDestination();
      
      com.google.protobuf.ByteString
          getDestinationBytes();

      
      boolean hasTimestamp();
      
      long getTimestamp();
    }
    
    public static final class SyncMessageContext extends
        com.google.protobuf.GeneratedMessage
        implements SyncMessageContextOrBuilder {
      private SyncMessageContext(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private SyncMessageContext(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final SyncMessageContext defaultInstance;
      public static SyncMessageContext getDefaultInstance() {
        return defaultInstance;
      }

      public SyncMessageContext getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private SyncMessageContext(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        initFields();
        int mutable_bitField0_ = 0;
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
            com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              default: {
                if (!parseUnknownField(input, unknownFields,
                                       extensionRegistry, tag)) {
                  done = true;
                }
                break;
              }
              case 10: {
                bitField0_ |= 0x00000001;
                destination_ = input.readBytes();
                break;
              }
              case 16: {
                bitField0_ |= 0x00000002;
                timestamp_ = input.readUInt64();
                break;
              }
            }
          }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(
              e.getMessage()).setUnfinishedMessage(this);
        } finally {
          this.unknownFields = unknownFields.build();
          makeExtensionsImmutable();
        }
      }
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_SyncMessageContext_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_SyncMessageContext_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.class, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.Builder.class);
      }

      public static com.google.protobuf.Parser<SyncMessageContext> PARSER =
          new com.google.protobuf.AbstractParser<SyncMessageContext>() {
        public SyncMessageContext parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new SyncMessageContext(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<SyncMessageContext> getParserForType() {
        return PARSER;
      }

      private int bitField0_;
      public static final int DESTINATION_FIELD_NUMBER = 1;
      private java.lang.Object destination_;
      
      public boolean hasDestination() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public java.lang.String getDestination() {
        java.lang.Object ref = destination_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            destination_ = s;
          }
          return s;
        }
      }
      
      public com.google.protobuf.ByteString
          getDestinationBytes() {
        java.lang.Object ref = destination_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          destination_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public static final int TIMESTAMP_FIELD_NUMBER = 2;
      private long timestamp_;
      
      public boolean hasTimestamp() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public long getTimestamp() {
        return timestamp_;
      }

      private void initFields() {
        destination_ = "";
        timestamp_ = 0L;
      }
      private byte memoizedIsInitialized = -1;
      public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized != -1) return isInitialized == 1;

        memoizedIsInitialized = 1;
        return true;
      }

      public void writeTo(com.google.protobuf.CodedOutputStream output)
                          throws java.io.IOException {
        getSerializedSize();
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          output.writeBytes(1, getDestinationBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeUInt64(2, timestamp_);
        }
        getUnknownFields().writeTo(output);
      }

      private int memoizedSerializedSize = -1;
      public int getSerializedSize() {
        int size = memoizedSerializedSize;
        if (size != -1) return size;

        size = 0;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(1, getDestinationBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeUInt64Size(2, timestamp_);
        }
        size += getUnknownFields().getSerializedSize();
        memoizedSerializedSize = size;
        return size;
      }

      private static final long serialVersionUID = 0L;
      @java.lang.Override
      protected java.lang.Object writeReplace()
          throws java.io.ObjectStreamException {
        return super.writeReplace();
      }

      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext prototype) {
        return newBuilder().mergeFrom(prototype);
      }
      public Builder toBuilder() { return newBuilder(this); }

      @java.lang.Override
      protected Builder newBuilderForType(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
      }
      
      public static final class Builder extends
          com.google.protobuf.GeneratedMessage.Builder<Builder>
         implements com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContextOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_SyncMessageContext_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_SyncMessageContext_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.class, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.Builder.class);
        }

        private Builder() {
          maybeForceBuilderInitialization();
        }

        private Builder(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
          super(parent);
          maybeForceBuilderInitialization();
        }
        private void maybeForceBuilderInitialization() {
          if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          }
        }
        private static Builder create() {
          return new Builder();
        }

        public Builder clear() {
          super.clear();
          destination_ = "";
          bitField0_ = (bitField0_ & ~0x00000001);
          timestamp_ = 0L;
          bitField0_ = (bitField0_ & ~0x00000002);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_SyncMessageContext_descriptor;
        }

        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext build() {
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext buildPartial() {
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext result = new com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.destination_ = destination_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.timestamp_ = timestamp_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext) {
            return mergeFrom((com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext other) {
          if (other == com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.getDefaultInstance()) return this;
          if (other.hasDestination()) {
            bitField0_ |= 0x00000001;
            destination_ = other.destination_;
            onChanged();
          }
          if (other.hasTimestamp()) {
            setTimestamp(other.getTimestamp());
          }
          this.mergeUnknownFields(other.getUnknownFields());
          return this;
        }

        public final boolean isInitialized() {
          return true;
        }

        public Builder mergeFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private java.lang.Object destination_ = "";
        
        public boolean hasDestination() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }
        
        public java.lang.String getDestination() {
          java.lang.Object ref = destination_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            destination_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }
        
        public com.google.protobuf.ByteString
            getDestinationBytes() {
          java.lang.Object ref = destination_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            destination_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }
        
        public Builder setDestination(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          destination_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearDestination() {
          bitField0_ = (bitField0_ & ~0x00000001);
          destination_ = getDefaultInstance().getDestination();
          onChanged();
          return this;
        }
        
        public Builder setDestinationBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          destination_ = value;
          onChanged();
          return this;
        }

        private long timestamp_ ;
        
        public boolean hasTimestamp() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        
        public long getTimestamp() {
          return timestamp_;
        }
        
        public Builder setTimestamp(long value) {
          bitField0_ |= 0x00000002;
          timestamp_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearTimestamp() {
          bitField0_ = (bitField0_ & ~0x00000002);
          timestamp_ = 0L;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new SyncMessageContext(true);
        defaultInstance.initFields();
      }

    }

    private int bitField0_;
    public static final int BODY_FIELD_NUMBER = 1;
    private java.lang.Object body_;
    
    public boolean hasBody() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    
    public java.lang.String getBody() {
      java.lang.Object ref = body_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          body_ = s;
        }
        return s;
      }
    }
    
    public com.google.protobuf.ByteString
        getBodyBytes() {
      java.lang.Object ref = body_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        body_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int ATTACHMENTS_FIELD_NUMBER = 2;
    private java.util.List<com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer> attachments_;
    
    public java.util.List<com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer> getAttachmentsList() {
      return attachments_;
    }
    
    public java.util.List<? extends com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder> 
        getAttachmentsOrBuilderList() {
      return attachments_;
    }
    
    public int getAttachmentsCount() {
      return attachments_.size();
    }
    
    public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer getAttachments(int index) {
      return attachments_.get(index);
    }
    
    public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder getAttachmentsOrBuilder(
        int index) {
      return attachments_.get(index);
    }

    public static final int GROUP_FIELD_NUMBER = 3;
    private com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext group_;
    
    public boolean hasGroup() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    
    public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext getGroup() {
      return group_;
    }
    
    public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContextOrBuilder getGroupOrBuilder() {
      return group_;
    }

    public static final int FLAGS_FIELD_NUMBER = 4;
    private int flags_;
    
    public boolean hasFlags() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    
    public int getFlags() {
      return flags_;
    }

    public static final int SYNC_FIELD_NUMBER = 5;
    private com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext sync_;
    
    public boolean hasSync() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext getSync() {
      return sync_;
    }
    
    public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContextOrBuilder getSyncOrBuilder() {
      return sync_;
    }

    private void initFields() {
      body_ = "";
      attachments_ = java.util.Collections.emptyList();
      group_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.getDefaultInstance();
      flags_ = 0;
      sync_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getBodyBytes());
      }
      for (int i = 0; i < attachments_.size(); i++) {
        output.writeMessage(2, attachments_.get(i));
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(3, group_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeUInt32(4, flags_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(5, sync_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getBodyBytes());
      }
      for (int i = 0; i < attachments_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, attachments_.get(i));
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, group_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(4, flags_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, sync_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContentOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.class, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getAttachmentsFieldBuilder();
          getGroupFieldBuilder();
          getSyncFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        body_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        if (attachmentsBuilder_ == null) {
          attachments_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          attachmentsBuilder_.clear();
        }
        if (groupBuilder_ == null) {
          group_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.getDefaultInstance();
        } else {
          groupBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        flags_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        if (syncBuilder_ == null) {
          sync_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.getDefaultInstance();
        } else {
          syncBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.PushMessageProtos.internal_static_openchatservice_PushMessageContent_descriptor;
      }

      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent build() {
        com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent buildPartial() {
        com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent result = new com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.body_ = body_;
        if (attachmentsBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            attachments_ = java.util.Collections.unmodifiableList(attachments_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.attachments_ = attachments_;
        } else {
          result.attachments_ = attachmentsBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000002;
        }
        if (groupBuilder_ == null) {
          result.group_ = group_;
        } else {
          result.group_ = groupBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000004;
        }
        result.flags_ = flags_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000008;
        }
        if (syncBuilder_ == null) {
          result.sync_ = sync_;
        } else {
          result.sync_ = syncBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent) {
          return mergeFrom((com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent other) {
        if (other == com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.getDefaultInstance()) return this;
        if (other.hasBody()) {
          bitField0_ |= 0x00000001;
          body_ = other.body_;
          onChanged();
        }
        if (attachmentsBuilder_ == null) {
          if (!other.attachments_.isEmpty()) {
            if (attachments_.isEmpty()) {
              attachments_ = other.attachments_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensureAttachmentsIsMutable();
              attachments_.addAll(other.attachments_);
            }
            onChanged();
          }
        } else {
          if (!other.attachments_.isEmpty()) {
            if (attachmentsBuilder_.isEmpty()) {
              attachmentsBuilder_.dispose();
              attachmentsBuilder_ = null;
              attachments_ = other.attachments_;
              bitField0_ = (bitField0_ & ~0x00000002);
              attachmentsBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getAttachmentsFieldBuilder() : null;
            } else {
              attachmentsBuilder_.addAllMessages(other.attachments_);
            }
          }
        }
        if (other.hasGroup()) {
          mergeGroup(other.getGroup());
        }
        if (other.hasFlags()) {
          setFlags(other.getFlags());
        }
        if (other.hasSync()) {
          mergeSync(other.getSync());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object body_ = "";
      
      public boolean hasBody() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public java.lang.String getBody() {
        java.lang.Object ref = body_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          body_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      
      public com.google.protobuf.ByteString
          getBodyBytes() {
        java.lang.Object ref = body_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          body_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      
      public Builder setBody(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        body_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearBody() {
        bitField0_ = (bitField0_ & ~0x00000001);
        body_ = getDefaultInstance().getBody();
        onChanged();
        return this;
      }
      
      public Builder setBodyBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        body_ = value;
        onChanged();
        return this;
      }

      private java.util.List<com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer> attachments_ =
        java.util.Collections.emptyList();
      private void ensureAttachmentsIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          attachments_ = new java.util.ArrayList<com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer>(attachments_);
          bitField0_ |= 0x00000002;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder> attachmentsBuilder_;

      
      public java.util.List<com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer> getAttachmentsList() {
        if (attachmentsBuilder_ == null) {
          return java.util.Collections.unmodifiableList(attachments_);
        } else {
          return attachmentsBuilder_.getMessageList();
        }
      }
      
      public int getAttachmentsCount() {
        if (attachmentsBuilder_ == null) {
          return attachments_.size();
        } else {
          return attachmentsBuilder_.getCount();
        }
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer getAttachments(int index) {
        if (attachmentsBuilder_ == null) {
          return attachments_.get(index);
        } else {
          return attachmentsBuilder_.getMessage(index);
        }
      }
      
      public Builder setAttachments(
          int index, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer value) {
        if (attachmentsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAttachmentsIsMutable();
          attachments_.set(index, value);
          onChanged();
        } else {
          attachmentsBuilder_.setMessage(index, value);
        }
        return this;
      }
      
      public Builder setAttachments(
          int index, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder builderForValue) {
        if (attachmentsBuilder_ == null) {
          ensureAttachmentsIsMutable();
          attachments_.set(index, builderForValue.build());
          onChanged();
        } else {
          attachmentsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      
      public Builder addAttachments(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer value) {
        if (attachmentsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAttachmentsIsMutable();
          attachments_.add(value);
          onChanged();
        } else {
          attachmentsBuilder_.addMessage(value);
        }
        return this;
      }
      
      public Builder addAttachments(
          int index, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer value) {
        if (attachmentsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAttachmentsIsMutable();
          attachments_.add(index, value);
          onChanged();
        } else {
          attachmentsBuilder_.addMessage(index, value);
        }
        return this;
      }
      
      public Builder addAttachments(
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder builderForValue) {
        if (attachmentsBuilder_ == null) {
          ensureAttachmentsIsMutable();
          attachments_.add(builderForValue.build());
          onChanged();
        } else {
          attachmentsBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      
      public Builder addAttachments(
          int index, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder builderForValue) {
        if (attachmentsBuilder_ == null) {
          ensureAttachmentsIsMutable();
          attachments_.add(index, builderForValue.build());
          onChanged();
        } else {
          attachmentsBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      
      public Builder addAllAttachments(
          java.lang.Iterable<? extends com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer> values) {
        if (attachmentsBuilder_ == null) {
          ensureAttachmentsIsMutable();
          super.addAll(values, attachments_);
          onChanged();
        } else {
          attachmentsBuilder_.addAllMessages(values);
        }
        return this;
      }
      
      public Builder clearAttachments() {
        if (attachmentsBuilder_ == null) {
          attachments_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
        } else {
          attachmentsBuilder_.clear();
        }
        return this;
      }
      
      public Builder removeAttachments(int index) {
        if (attachmentsBuilder_ == null) {
          ensureAttachmentsIsMutable();
          attachments_.remove(index);
          onChanged();
        } else {
          attachmentsBuilder_.remove(index);
        }
        return this;
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder getAttachmentsBuilder(
          int index) {
        return getAttachmentsFieldBuilder().getBuilder(index);
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder getAttachmentsOrBuilder(
          int index) {
        if (attachmentsBuilder_ == null) {
          return attachments_.get(index);  } else {
          return attachmentsBuilder_.getMessageOrBuilder(index);
        }
      }
      
      public java.util.List<? extends com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder> 
           getAttachmentsOrBuilderList() {
        if (attachmentsBuilder_ != null) {
          return attachmentsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(attachments_);
        }
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder addAttachmentsBuilder() {
        return getAttachmentsFieldBuilder().addBuilder(
            com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.getDefaultInstance());
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder addAttachmentsBuilder(
          int index) {
        return getAttachmentsFieldBuilder().addBuilder(
            index, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.getDefaultInstance());
      }
      
      public java.util.List<com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder> 
           getAttachmentsBuilderList() {
        return getAttachmentsFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder> 
          getAttachmentsFieldBuilder() {
        if (attachmentsBuilder_ == null) {
          attachmentsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointerOrBuilder>(
                  attachments_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          attachments_ = null;
        }
        return attachmentsBuilder_;
      }

      private com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext group_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContextOrBuilder> groupBuilder_;
      
      public boolean hasGroup() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext getGroup() {
        if (groupBuilder_ == null) {
          return group_;
        } else {
          return groupBuilder_.getMessage();
        }
      }
      
      public Builder setGroup(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext value) {
        if (groupBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          group_ = value;
          onChanged();
        } else {
          groupBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder setGroup(
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Builder builderForValue) {
        if (groupBuilder_ == null) {
          group_ = builderForValue.build();
          onChanged();
        } else {
          groupBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder mergeGroup(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext value) {
        if (groupBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              group_ != com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.getDefaultInstance()) {
            group_ =
              com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.newBuilder(group_).mergeFrom(value).buildPartial();
          } else {
            group_ = value;
          }
          onChanged();
        } else {
          groupBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder clearGroup() {
        if (groupBuilder_ == null) {
          group_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.getDefaultInstance();
          onChanged();
        } else {
          groupBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Builder getGroupBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getGroupFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContextOrBuilder getGroupOrBuilder() {
        if (groupBuilder_ != null) {
          return groupBuilder_.getMessageOrBuilder();
        } else {
          return group_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContextOrBuilder> 
          getGroupFieldBuilder() {
        if (groupBuilder_ == null) {
          groupBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContextOrBuilder>(
                  group_,
                  getParentForChildren(),
                  isClean());
          group_ = null;
        }
        return groupBuilder_;
      }

      private int flags_ ;
      
      public boolean hasFlags() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public int getFlags() {
        return flags_;
      }
      
      public Builder setFlags(int value) {
        bitField0_ |= 0x00000008;
        flags_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearFlags() {
        bitField0_ = (bitField0_ & ~0x00000008);
        flags_ = 0;
        onChanged();
        return this;
      }

      private com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext sync_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContextOrBuilder> syncBuilder_;
      
      public boolean hasSync() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext getSync() {
        if (syncBuilder_ == null) {
          return sync_;
        } else {
          return syncBuilder_.getMessage();
        }
      }
      
      public Builder setSync(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext value) {
        if (syncBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          sync_ = value;
          onChanged();
        } else {
          syncBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder setSync(
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.Builder builderForValue) {
        if (syncBuilder_ == null) {
          sync_ = builderForValue.build();
          onChanged();
        } else {
          syncBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder mergeSync(com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext value) {
        if (syncBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              sync_ != com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.getDefaultInstance()) {
            sync_ =
              com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.newBuilder(sync_).mergeFrom(value).buildPartial();
          } else {
            sync_ = value;
          }
          onChanged();
        } else {
          syncBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder clearSync() {
        if (syncBuilder_ == null) {
          sync_ = com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.getDefaultInstance();
          onChanged();
        } else {
          syncBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.Builder getSyncBuilder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getSyncFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContextOrBuilder getSyncOrBuilder() {
        if (syncBuilder_ != null) {
          return syncBuilder_.getMessageOrBuilder();
        } else {
          return sync_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContextOrBuilder> 
          getSyncFieldBuilder() {
        if (syncBuilder_ == null) {
          syncBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContext.Builder, com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.SyncMessageContextOrBuilder>(
                  sync_,
                  getParentForChildren(),
                  isClean());
          sync_ = null;
        }
        return syncBuilder_;
      }

    }

    static {
      defaultInstance = new PushMessageContent(true);
      defaultInstance.initFields();
    }

  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_IncomingPushMessageOpenchat_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_IncomingPushMessageOpenchat_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_PushMessageContent_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_PushMessageContent_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_PushMessageContent_AttachmentPointer_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_PushMessageContent_AttachmentPointer_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_PushMessageContent_GroupContext_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_PushMessageContent_GroupContext_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_PushMessageContent_SyncMessageContext_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_PushMessageContent_SyncMessageContext_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {""};
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_openchatservice_IncomingPushMessageOpenchat_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_openchatservice_IncomingPushMessageOpenchat_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_IncomingPushMessageOpenchat_descriptor,
              new java.lang.String[] { "Type", "Source", "SourceDevice", "Relay", "Timestamp", "Message", });
          internal_static_openchatservice_PushMessageContent_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_openchatservice_PushMessageContent_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_PushMessageContent_descriptor,
              new java.lang.String[] { "Body", "Attachments", "Group", "Flags", "Sync", });
          internal_static_openchatservice_PushMessageContent_AttachmentPointer_descriptor =
            internal_static_openchatservice_PushMessageContent_descriptor.getNestedTypes().get(0);
          internal_static_openchatservice_PushMessageContent_AttachmentPointer_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_PushMessageContent_AttachmentPointer_descriptor,
              new java.lang.String[] { "Id", "ContentType", "Key", });
          internal_static_openchatservice_PushMessageContent_GroupContext_descriptor =
            internal_static_openchatservice_PushMessageContent_descriptor.getNestedTypes().get(1);
          internal_static_openchatservice_PushMessageContent_GroupContext_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_PushMessageContent_GroupContext_descriptor,
              new java.lang.String[] { "Id", "Type", "Name", "Members", "Avatar", });
          internal_static_openchatservice_PushMessageContent_SyncMessageContext_descriptor =
            internal_static_openchatservice_PushMessageContent_descriptor.getNestedTypes().get(2);
          internal_static_openchatservice_PushMessageContent_SyncMessageContext_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_PushMessageContent_SyncMessageContext_descriptor,
              new java.lang.String[] { "Destination", "Timestamp", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

}
