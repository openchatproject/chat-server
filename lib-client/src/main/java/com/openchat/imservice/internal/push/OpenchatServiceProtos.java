package com.openchat.imservice.internal.push;

public final class OpenchatServiceProtos {
  private OpenchatServiceProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface EnvelopeOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasType();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type getType();

    
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

    
    boolean hasLegacyMessage();
    
    com.google.protobuf.ByteString getLegacyMessage();

    
    boolean hasContent();
    
    com.google.protobuf.ByteString getContent();
  }
  
  public static final class Envelope extends
      com.google.protobuf.GeneratedMessage
      implements EnvelopeOrBuilder {
    private Envelope(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Envelope(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Envelope defaultInstance;
    public static Envelope getDefaultInstance() {
      return defaultInstance;
    }

    public Envelope getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Envelope(
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
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type value = com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type.valueOf(rawValue);
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
              legacyMessage_ = input.readBytes();
              break;
            }
            case 56: {
              bitField0_ |= 0x00000004;
              sourceDevice_ = input.readUInt32();
              break;
            }
            case 66: {
              bitField0_ |= 0x00000040;
              content_ = input.readBytes();
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
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Envelope_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Envelope_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Builder.class);
    }

    public static com.google.protobuf.Parser<Envelope> PARSER =
        new com.google.protobuf.AbstractParser<Envelope>() {
      public Envelope parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Envelope(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Envelope> getParserForType() {
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.getDescriptor().getEnumTypes().get(0);
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
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type type_;
    
    public boolean hasType() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type getType() {
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

    public static final int LEGACYMESSAGE_FIELD_NUMBER = 6;
    private com.google.protobuf.ByteString legacyMessage_;
    
    public boolean hasLegacyMessage() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    
    public com.google.protobuf.ByteString getLegacyMessage() {
      return legacyMessage_;
    }

    public static final int CONTENT_FIELD_NUMBER = 8;
    private com.google.protobuf.ByteString content_;
    
    public boolean hasContent() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    
    public com.google.protobuf.ByteString getContent() {
      return content_;
    }

    private void initFields() {
      type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type.UNKNOWN;
      source_ = "";
      sourceDevice_ = 0;
      relay_ = "";
      timestamp_ = 0L;
      legacyMessage_ = com.google.protobuf.ByteString.EMPTY;
      content_ = com.google.protobuf.ByteString.EMPTY;
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
        output.writeBytes(6, legacyMessage_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeUInt32(7, sourceDevice_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBytes(8, content_);
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
          .computeBytesSize(6, legacyMessage_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(7, sourceDevice_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(8, content_);
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.EnvelopeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Envelope_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Envelope_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Builder.class);
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
        type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type.UNKNOWN;
        bitField0_ = (bitField0_ & ~0x00000001);
        source_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        sourceDevice_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        relay_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        timestamp_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000010);
        legacyMessage_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000020);
        content_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Envelope_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope(this);
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
        result.legacyMessage_ = legacyMessage_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.content_ = content_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.getDefaultInstance()) return this;
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
        if (other.hasLegacyMessage()) {
          setLegacyMessage(other.getLegacyMessage());
        }
        if (other.hasContent()) {
          setContent(other.getContent());
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type.UNKNOWN;
      
      public boolean hasType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type getType() {
        return type_;
      }
      
      public Builder setType(com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type value) {
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
        type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type.UNKNOWN;
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

      private com.google.protobuf.ByteString legacyMessage_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasLegacyMessage() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      
      public com.google.protobuf.ByteString getLegacyMessage() {
        return legacyMessage_;
      }
      
      public Builder setLegacyMessage(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        legacyMessage_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearLegacyMessage() {
        bitField0_ = (bitField0_ & ~0x00000020);
        legacyMessage_ = getDefaultInstance().getLegacyMessage();
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString content_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasContent() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      
      public com.google.protobuf.ByteString getContent() {
        return content_;
      }
      
      public Builder setContent(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        content_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearContent() {
        bitField0_ = (bitField0_ & ~0x00000040);
        content_ = getDefaultInstance().getContent();
        onChanged();
        return this;
      }

    }

    static {
      defaultInstance = new Envelope(true);
      defaultInstance.initFields();
    }

  }

  public interface ContentOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasDataMessage();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage getDataMessage();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder getDataMessageOrBuilder();

    
    boolean hasSyncMessage();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage getSyncMessage();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessageOrBuilder getSyncMessageOrBuilder();

    
    boolean hasCallMessage();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage getCallMessage();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessageOrBuilder getCallMessageOrBuilder();

    
    boolean hasNullMessage();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage getNullMessage();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessageOrBuilder getNullMessageOrBuilder();

    
    boolean hasReceiptMessage();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage getReceiptMessage();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessageOrBuilder getReceiptMessageOrBuilder();
  }
  
  public static final class Content extends
      com.google.protobuf.GeneratedMessage
      implements ContentOrBuilder {
    private Content(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Content(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Content defaultInstance;
    public static Content getDefaultInstance() {
      return defaultInstance;
    }

    public Content getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Content(
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
              com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = dataMessage_.toBuilder();
              }
              dataMessage_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(dataMessage_);
                dataMessage_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 18: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = syncMessage_.toBuilder();
              }
              syncMessage_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(syncMessage_);
                syncMessage_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
            case 26: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = callMessage_.toBuilder();
              }
              callMessage_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(callMessage_);
                callMessage_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 34: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = nullMessage_.toBuilder();
              }
              nullMessage_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(nullMessage_);
                nullMessage_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
            case 42: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Builder subBuilder = null;
              if (((bitField0_ & 0x00000010) == 0x00000010)) {
                subBuilder = receiptMessage_.toBuilder();
              }
              receiptMessage_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(receiptMessage_);
                receiptMessage_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000010;
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
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Content_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Content_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Content.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.Content.Builder.class);
    }

    public static com.google.protobuf.Parser<Content> PARSER =
        new com.google.protobuf.AbstractParser<Content>() {
      public Content parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Content(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Content> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int DATAMESSAGE_FIELD_NUMBER = 1;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage dataMessage_;
    
    public boolean hasDataMessage() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage getDataMessage() {
      return dataMessage_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder getDataMessageOrBuilder() {
      return dataMessage_;
    }

    public static final int SYNCMESSAGE_FIELD_NUMBER = 2;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage syncMessage_;
    
    public boolean hasSyncMessage() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage getSyncMessage() {
      return syncMessage_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessageOrBuilder getSyncMessageOrBuilder() {
      return syncMessage_;
    }

    public static final int CALLMESSAGE_FIELD_NUMBER = 3;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage callMessage_;
    
    public boolean hasCallMessage() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage getCallMessage() {
      return callMessage_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessageOrBuilder getCallMessageOrBuilder() {
      return callMessage_;
    }

    public static final int NULLMESSAGE_FIELD_NUMBER = 4;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage nullMessage_;
    
    public boolean hasNullMessage() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage getNullMessage() {
      return nullMessage_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessageOrBuilder getNullMessageOrBuilder() {
      return nullMessage_;
    }

    public static final int RECEIPTMESSAGE_FIELD_NUMBER = 5;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage receiptMessage_;
    
    public boolean hasReceiptMessage() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage getReceiptMessage() {
      return receiptMessage_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessageOrBuilder getReceiptMessageOrBuilder() {
      return receiptMessage_;
    }

    private void initFields() {
      dataMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance();
      syncMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.getDefaultInstance();
      callMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.getDefaultInstance();
      nullMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.getDefaultInstance();
      receiptMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.getDefaultInstance();
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
        output.writeMessage(1, dataMessage_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, syncMessage_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(3, callMessage_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(4, nullMessage_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeMessage(5, receiptMessage_);
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
          .computeMessageSize(1, dataMessage_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, syncMessage_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, callMessage_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, nullMessage_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, receiptMessage_);
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.Content prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.ContentOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Content_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Content_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.Content.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.Content.Builder.class);
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
          getDataMessageFieldBuilder();
          getSyncMessageFieldBuilder();
          getCallMessageFieldBuilder();
          getNullMessageFieldBuilder();
          getReceiptMessageFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (dataMessageBuilder_ == null) {
          dataMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance();
        } else {
          dataMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (syncMessageBuilder_ == null) {
          syncMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.getDefaultInstance();
        } else {
          syncMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        if (callMessageBuilder_ == null) {
          callMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.getDefaultInstance();
        } else {
          callMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        if (nullMessageBuilder_ == null) {
          nullMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.getDefaultInstance();
        } else {
          nullMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        if (receiptMessageBuilder_ == null) {
          receiptMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.getDefaultInstance();
        } else {
          receiptMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Content_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Content getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.Content.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Content build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.Content result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Content buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.Content result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.Content(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (dataMessageBuilder_ == null) {
          result.dataMessage_ = dataMessage_;
        } else {
          result.dataMessage_ = dataMessageBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (syncMessageBuilder_ == null) {
          result.syncMessage_ = syncMessage_;
        } else {
          result.syncMessage_ = syncMessageBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (callMessageBuilder_ == null) {
          result.callMessage_ = callMessage_;
        } else {
          result.callMessage_ = callMessageBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (nullMessageBuilder_ == null) {
          result.nullMessage_ = nullMessage_;
        } else {
          result.nullMessage_ = nullMessageBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        if (receiptMessageBuilder_ == null) {
          result.receiptMessage_ = receiptMessage_;
        } else {
          result.receiptMessage_ = receiptMessageBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.Content) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.Content)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.Content other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.Content.getDefaultInstance()) return this;
        if (other.hasDataMessage()) {
          mergeDataMessage(other.getDataMessage());
        }
        if (other.hasSyncMessage()) {
          mergeSyncMessage(other.getSyncMessage());
        }
        if (other.hasCallMessage()) {
          mergeCallMessage(other.getCallMessage());
        }
        if (other.hasNullMessage()) {
          mergeNullMessage(other.getNullMessage());
        }
        if (other.hasReceiptMessage()) {
          mergeReceiptMessage(other.getReceiptMessage());
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.Content parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.Content) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage dataMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder> dataMessageBuilder_;
      
      public boolean hasDataMessage() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage getDataMessage() {
        if (dataMessageBuilder_ == null) {
          return dataMessage_;
        } else {
          return dataMessageBuilder_.getMessage();
        }
      }
      
      public Builder setDataMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage value) {
        if (dataMessageBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          dataMessage_ = value;
          onChanged();
        } else {
          dataMessageBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      
      public Builder setDataMessage(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder builderForValue) {
        if (dataMessageBuilder_ == null) {
          dataMessage_ = builderForValue.build();
          onChanged();
        } else {
          dataMessageBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      
      public Builder mergeDataMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage value) {
        if (dataMessageBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              dataMessage_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance()) {
            dataMessage_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.newBuilder(dataMessage_).mergeFrom(value).buildPartial();
          } else {
            dataMessage_ = value;
          }
          onChanged();
        } else {
          dataMessageBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      
      public Builder clearDataMessage() {
        if (dataMessageBuilder_ == null) {
          dataMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance();
          onChanged();
        } else {
          dataMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder getDataMessageBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getDataMessageFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder getDataMessageOrBuilder() {
        if (dataMessageBuilder_ != null) {
          return dataMessageBuilder_.getMessageOrBuilder();
        } else {
          return dataMessage_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder> 
          getDataMessageFieldBuilder() {
        if (dataMessageBuilder_ == null) {
          dataMessageBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder>(
                  dataMessage_,
                  getParentForChildren(),
                  isClean());
          dataMessage_ = null;
        }
        return dataMessageBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage syncMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessageOrBuilder> syncMessageBuilder_;
      
      public boolean hasSyncMessage() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage getSyncMessage() {
        if (syncMessageBuilder_ == null) {
          return syncMessage_;
        } else {
          return syncMessageBuilder_.getMessage();
        }
      }
      
      public Builder setSyncMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage value) {
        if (syncMessageBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          syncMessage_ = value;
          onChanged();
        } else {
          syncMessageBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      
      public Builder setSyncMessage(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Builder builderForValue) {
        if (syncMessageBuilder_ == null) {
          syncMessage_ = builderForValue.build();
          onChanged();
        } else {
          syncMessageBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      
      public Builder mergeSyncMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage value) {
        if (syncMessageBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              syncMessage_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.getDefaultInstance()) {
            syncMessage_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.newBuilder(syncMessage_).mergeFrom(value).buildPartial();
          } else {
            syncMessage_ = value;
          }
          onChanged();
        } else {
          syncMessageBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      
      public Builder clearSyncMessage() {
        if (syncMessageBuilder_ == null) {
          syncMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.getDefaultInstance();
          onChanged();
        } else {
          syncMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Builder getSyncMessageBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getSyncMessageFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessageOrBuilder getSyncMessageOrBuilder() {
        if (syncMessageBuilder_ != null) {
          return syncMessageBuilder_.getMessageOrBuilder();
        } else {
          return syncMessage_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessageOrBuilder> 
          getSyncMessageFieldBuilder() {
        if (syncMessageBuilder_ == null) {
          syncMessageBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessageOrBuilder>(
                  syncMessage_,
                  getParentForChildren(),
                  isClean());
          syncMessage_ = null;
        }
        return syncMessageBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage callMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessageOrBuilder> callMessageBuilder_;
      
      public boolean hasCallMessage() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage getCallMessage() {
        if (callMessageBuilder_ == null) {
          return callMessage_;
        } else {
          return callMessageBuilder_.getMessage();
        }
      }
      
      public Builder setCallMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage value) {
        if (callMessageBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          callMessage_ = value;
          onChanged();
        } else {
          callMessageBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder setCallMessage(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Builder builderForValue) {
        if (callMessageBuilder_ == null) {
          callMessage_ = builderForValue.build();
          onChanged();
        } else {
          callMessageBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder mergeCallMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage value) {
        if (callMessageBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              callMessage_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.getDefaultInstance()) {
            callMessage_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.newBuilder(callMessage_).mergeFrom(value).buildPartial();
          } else {
            callMessage_ = value;
          }
          onChanged();
        } else {
          callMessageBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder clearCallMessage() {
        if (callMessageBuilder_ == null) {
          callMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.getDefaultInstance();
          onChanged();
        } else {
          callMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Builder getCallMessageBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getCallMessageFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessageOrBuilder getCallMessageOrBuilder() {
        if (callMessageBuilder_ != null) {
          return callMessageBuilder_.getMessageOrBuilder();
        } else {
          return callMessage_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessageOrBuilder> 
          getCallMessageFieldBuilder() {
        if (callMessageBuilder_ == null) {
          callMessageBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessageOrBuilder>(
                  callMessage_,
                  getParentForChildren(),
                  isClean());
          callMessage_ = null;
        }
        return callMessageBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage nullMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessageOrBuilder> nullMessageBuilder_;
      
      public boolean hasNullMessage() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage getNullMessage() {
        if (nullMessageBuilder_ == null) {
          return nullMessage_;
        } else {
          return nullMessageBuilder_.getMessage();
        }
      }
      
      public Builder setNullMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage value) {
        if (nullMessageBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          nullMessage_ = value;
          onChanged();
        } else {
          nullMessageBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder setNullMessage(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.Builder builderForValue) {
        if (nullMessageBuilder_ == null) {
          nullMessage_ = builderForValue.build();
          onChanged();
        } else {
          nullMessageBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder mergeNullMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage value) {
        if (nullMessageBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              nullMessage_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.getDefaultInstance()) {
            nullMessage_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.newBuilder(nullMessage_).mergeFrom(value).buildPartial();
          } else {
            nullMessage_ = value;
          }
          onChanged();
        } else {
          nullMessageBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder clearNullMessage() {
        if (nullMessageBuilder_ == null) {
          nullMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.getDefaultInstance();
          onChanged();
        } else {
          nullMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.Builder getNullMessageBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getNullMessageFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessageOrBuilder getNullMessageOrBuilder() {
        if (nullMessageBuilder_ != null) {
          return nullMessageBuilder_.getMessageOrBuilder();
        } else {
          return nullMessage_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessageOrBuilder> 
          getNullMessageFieldBuilder() {
        if (nullMessageBuilder_ == null) {
          nullMessageBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessageOrBuilder>(
                  nullMessage_,
                  getParentForChildren(),
                  isClean());
          nullMessage_ = null;
        }
        return nullMessageBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage receiptMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessageOrBuilder> receiptMessageBuilder_;
      
      public boolean hasReceiptMessage() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage getReceiptMessage() {
        if (receiptMessageBuilder_ == null) {
          return receiptMessage_;
        } else {
          return receiptMessageBuilder_.getMessage();
        }
      }
      
      public Builder setReceiptMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage value) {
        if (receiptMessageBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          receiptMessage_ = value;
          onChanged();
        } else {
          receiptMessageBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder setReceiptMessage(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Builder builderForValue) {
        if (receiptMessageBuilder_ == null) {
          receiptMessage_ = builderForValue.build();
          onChanged();
        } else {
          receiptMessageBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder mergeReceiptMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage value) {
        if (receiptMessageBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              receiptMessage_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.getDefaultInstance()) {
            receiptMessage_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.newBuilder(receiptMessage_).mergeFrom(value).buildPartial();
          } else {
            receiptMessage_ = value;
          }
          onChanged();
        } else {
          receiptMessageBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder clearReceiptMessage() {
        if (receiptMessageBuilder_ == null) {
          receiptMessage_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.getDefaultInstance();
          onChanged();
        } else {
          receiptMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Builder getReceiptMessageBuilder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getReceiptMessageFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessageOrBuilder getReceiptMessageOrBuilder() {
        if (receiptMessageBuilder_ != null) {
          return receiptMessageBuilder_.getMessageOrBuilder();
        } else {
          return receiptMessage_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessageOrBuilder> 
          getReceiptMessageFieldBuilder() {
        if (receiptMessageBuilder_ == null) {
          receiptMessageBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessageOrBuilder>(
                  receiptMessage_,
                  getParentForChildren(),
                  isClean());
          receiptMessage_ = null;
        }
        return receiptMessageBuilder_;
      }

    }

    static {
      defaultInstance = new Content(true);
      defaultInstance.initFields();
    }

  }

  public interface CallMessageOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasOffer();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer getOffer();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.OfferOrBuilder getOfferOrBuilder();

    
    boolean hasAnswer();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer getAnswer();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.AnswerOrBuilder getAnswerOrBuilder();

    
    java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate> 
        getIceUpdateList();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate getIceUpdate(int index);
    
    int getIceUpdateCount();
    
    java.util.List<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdateOrBuilder> 
        getIceUpdateOrBuilderList();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdateOrBuilder getIceUpdateOrBuilder(
        int index);

    
    boolean hasHangup();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup getHangup();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.HangupOrBuilder getHangupOrBuilder();

    
    boolean hasBusy();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy getBusy();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.BusyOrBuilder getBusyOrBuilder();
  }
  
  public static final class CallMessage extends
      com.google.protobuf.GeneratedMessage
      implements CallMessageOrBuilder {
    private CallMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private CallMessage(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final CallMessage defaultInstance;
    public static CallMessage getDefaultInstance() {
      return defaultInstance;
    }

    public CallMessage getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private CallMessage(
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
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = offer_.toBuilder();
              }
              offer_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(offer_);
                offer_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 18: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = answer_.toBuilder();
              }
              answer_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(answer_);
                answer_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
            case 26: {
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
                iceUpdate_ = new java.util.ArrayList<com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate>();
                mutable_bitField0_ |= 0x00000004;
              }
              iceUpdate_.add(input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.PARSER, extensionRegistry));
              break;
            }
            case 34: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = hangup_.toBuilder();
              }
              hangup_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(hangup_);
                hangup_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 42: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = busy_.toBuilder();
              }
              busy_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(busy_);
                busy_ = subBuilder.buildPartial();
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
        if (((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
          iceUpdate_ = java.util.Collections.unmodifiableList(iceUpdate_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Builder.class);
    }

    public static com.google.protobuf.Parser<CallMessage> PARSER =
        new com.google.protobuf.AbstractParser<CallMessage>() {
      public CallMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new CallMessage(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<CallMessage> getParserForType() {
      return PARSER;
    }

    public interface OfferOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasId();
      
      long getId();

      
      boolean hasDescription();
      
      java.lang.String getDescription();
      
      com.google.protobuf.ByteString
          getDescriptionBytes();
    }
    
    public static final class Offer extends
        com.google.protobuf.GeneratedMessage
        implements OfferOrBuilder {
      private Offer(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Offer(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Offer defaultInstance;
      public static Offer getDefaultInstance() {
        return defaultInstance;
      }

      public Offer getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Offer(
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
                bitField0_ |= 0x00000001;
                id_ = input.readUInt64();
                break;
              }
              case 18: {
                bitField0_ |= 0x00000002;
                description_ = input.readBytes();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Offer_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Offer_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.Builder.class);
      }

      public static com.google.protobuf.Parser<Offer> PARSER =
          new com.google.protobuf.AbstractParser<Offer>() {
        public Offer parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Offer(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Offer> getParserForType() {
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

      public static final int DESCRIPTION_FIELD_NUMBER = 2;
      private java.lang.Object description_;
      
      public boolean hasDescription() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public java.lang.String getDescription() {
        java.lang.Object ref = description_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            description_ = s;
          }
          return s;
        }
      }
      
      public com.google.protobuf.ByteString
          getDescriptionBytes() {
        java.lang.Object ref = description_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          description_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      private void initFields() {
        id_ = 0L;
        description_ = "";
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
          output.writeUInt64(1, id_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeBytes(2, getDescriptionBytes());
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
            .computeUInt64Size(1, id_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(2, getDescriptionBytes());
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.OfferOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Offer_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Offer_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.Builder.class);
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
          description_ = "";
          bitField0_ = (bitField0_ & ~0x00000002);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Offer_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.id_ = id_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.description_ = description_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.getDefaultInstance()) return this;
          if (other.hasId()) {
            setId(other.getId());
          }
          if (other.hasDescription()) {
            bitField0_ |= 0x00000002;
            description_ = other.description_;
            onChanged();
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer) e.getUnfinishedMessage();
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

        private java.lang.Object description_ = "";
        
        public boolean hasDescription() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        
        public java.lang.String getDescription() {
          java.lang.Object ref = description_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            description_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }
        
        public com.google.protobuf.ByteString
            getDescriptionBytes() {
          java.lang.Object ref = description_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            description_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }
        
        public Builder setDescription(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
          description_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearDescription() {
          bitField0_ = (bitField0_ & ~0x00000002);
          description_ = getDefaultInstance().getDescription();
          onChanged();
          return this;
        }
        
        public Builder setDescriptionBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
          description_ = value;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new Offer(true);
        defaultInstance.initFields();
      }

    }

    public interface AnswerOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasId();
      
      long getId();

      
      boolean hasDescription();
      
      java.lang.String getDescription();
      
      com.google.protobuf.ByteString
          getDescriptionBytes();
    }
    
    public static final class Answer extends
        com.google.protobuf.GeneratedMessage
        implements AnswerOrBuilder {
      private Answer(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Answer(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Answer defaultInstance;
      public static Answer getDefaultInstance() {
        return defaultInstance;
      }

      public Answer getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Answer(
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
                bitField0_ |= 0x00000001;
                id_ = input.readUInt64();
                break;
              }
              case 18: {
                bitField0_ |= 0x00000002;
                description_ = input.readBytes();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Answer_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Answer_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.Builder.class);
      }

      public static com.google.protobuf.Parser<Answer> PARSER =
          new com.google.protobuf.AbstractParser<Answer>() {
        public Answer parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Answer(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Answer> getParserForType() {
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

      public static final int DESCRIPTION_FIELD_NUMBER = 2;
      private java.lang.Object description_;
      
      public boolean hasDescription() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public java.lang.String getDescription() {
        java.lang.Object ref = description_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            description_ = s;
          }
          return s;
        }
      }
      
      public com.google.protobuf.ByteString
          getDescriptionBytes() {
        java.lang.Object ref = description_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          description_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      private void initFields() {
        id_ = 0L;
        description_ = "";
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
          output.writeUInt64(1, id_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeBytes(2, getDescriptionBytes());
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
            .computeUInt64Size(1, id_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(2, getDescriptionBytes());
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.AnswerOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Answer_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Answer_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.Builder.class);
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
          description_ = "";
          bitField0_ = (bitField0_ & ~0x00000002);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Answer_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.id_ = id_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.description_ = description_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.getDefaultInstance()) return this;
          if (other.hasId()) {
            setId(other.getId());
          }
          if (other.hasDescription()) {
            bitField0_ |= 0x00000002;
            description_ = other.description_;
            onChanged();
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer) e.getUnfinishedMessage();
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

        private java.lang.Object description_ = "";
        
        public boolean hasDescription() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        
        public java.lang.String getDescription() {
          java.lang.Object ref = description_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            description_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }
        
        public com.google.protobuf.ByteString
            getDescriptionBytes() {
          java.lang.Object ref = description_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            description_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }
        
        public Builder setDescription(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
          description_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearDescription() {
          bitField0_ = (bitField0_ & ~0x00000002);
          description_ = getDefaultInstance().getDescription();
          onChanged();
          return this;
        }
        
        public Builder setDescriptionBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
          description_ = value;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new Answer(true);
        defaultInstance.initFields();
      }

    }

    public interface IceUpdateOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasId();
      
      long getId();

      
      boolean hasSdpMid();
      
      java.lang.String getSdpMid();
      
      com.google.protobuf.ByteString
          getSdpMidBytes();

      
      boolean hasSdpMLineIndex();
      
      int getSdpMLineIndex();

      
      boolean hasSdp();
      
      java.lang.String getSdp();
      
      com.google.protobuf.ByteString
          getSdpBytes();
    }
    
    public static final class IceUpdate extends
        com.google.protobuf.GeneratedMessage
        implements IceUpdateOrBuilder {
      private IceUpdate(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private IceUpdate(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final IceUpdate defaultInstance;
      public static IceUpdate getDefaultInstance() {
        return defaultInstance;
      }

      public IceUpdate getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private IceUpdate(
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
                bitField0_ |= 0x00000001;
                id_ = input.readUInt64();
                break;
              }
              case 18: {
                bitField0_ |= 0x00000002;
                sdpMid_ = input.readBytes();
                break;
              }
              case 24: {
                bitField0_ |= 0x00000004;
                sdpMLineIndex_ = input.readUInt32();
                break;
              }
              case 34: {
                bitField0_ |= 0x00000008;
                sdp_ = input.readBytes();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_IceUpdate_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_IceUpdate_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder.class);
      }

      public static com.google.protobuf.Parser<IceUpdate> PARSER =
          new com.google.protobuf.AbstractParser<IceUpdate>() {
        public IceUpdate parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new IceUpdate(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<IceUpdate> getParserForType() {
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

      public static final int SDPMID_FIELD_NUMBER = 2;
      private java.lang.Object sdpMid_;
      
      public boolean hasSdpMid() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public java.lang.String getSdpMid() {
        java.lang.Object ref = sdpMid_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            sdpMid_ = s;
          }
          return s;
        }
      }
      
      public com.google.protobuf.ByteString
          getSdpMidBytes() {
        java.lang.Object ref = sdpMid_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          sdpMid_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public static final int SDPMLINEINDEX_FIELD_NUMBER = 3;
      private int sdpMLineIndex_;
      
      public boolean hasSdpMLineIndex() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public int getSdpMLineIndex() {
        return sdpMLineIndex_;
      }

      public static final int SDP_FIELD_NUMBER = 4;
      private java.lang.Object sdp_;
      
      public boolean hasSdp() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public java.lang.String getSdp() {
        java.lang.Object ref = sdp_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            sdp_ = s;
          }
          return s;
        }
      }
      
      public com.google.protobuf.ByteString
          getSdpBytes() {
        java.lang.Object ref = sdp_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          sdp_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      private void initFields() {
        id_ = 0L;
        sdpMid_ = "";
        sdpMLineIndex_ = 0;
        sdp_ = "";
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
          output.writeUInt64(1, id_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeBytes(2, getSdpMidBytes());
        }
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          output.writeUInt32(3, sdpMLineIndex_);
        }
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          output.writeBytes(4, getSdpBytes());
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
            .computeUInt64Size(1, id_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(2, getSdpMidBytes());
        }
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          size += com.google.protobuf.CodedOutputStream
            .computeUInt32Size(3, sdpMLineIndex_);
        }
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(4, getSdpBytes());
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdateOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_IceUpdate_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_IceUpdate_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder.class);
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
          sdpMid_ = "";
          bitField0_ = (bitField0_ & ~0x00000002);
          sdpMLineIndex_ = 0;
          bitField0_ = (bitField0_ & ~0x00000004);
          sdp_ = "";
          bitField0_ = (bitField0_ & ~0x00000008);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_IceUpdate_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.id_ = id_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.sdpMid_ = sdpMid_;
          if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
            to_bitField0_ |= 0x00000004;
          }
          result.sdpMLineIndex_ = sdpMLineIndex_;
          if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
            to_bitField0_ |= 0x00000008;
          }
          result.sdp_ = sdp_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.getDefaultInstance()) return this;
          if (other.hasId()) {
            setId(other.getId());
          }
          if (other.hasSdpMid()) {
            bitField0_ |= 0x00000002;
            sdpMid_ = other.sdpMid_;
            onChanged();
          }
          if (other.hasSdpMLineIndex()) {
            setSdpMLineIndex(other.getSdpMLineIndex());
          }
          if (other.hasSdp()) {
            bitField0_ |= 0x00000008;
            sdp_ = other.sdp_;
            onChanged();
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate) e.getUnfinishedMessage();
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

        private java.lang.Object sdpMid_ = "";
        
        public boolean hasSdpMid() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        
        public java.lang.String getSdpMid() {
          java.lang.Object ref = sdpMid_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            sdpMid_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }
        
        public com.google.protobuf.ByteString
            getSdpMidBytes() {
          java.lang.Object ref = sdpMid_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            sdpMid_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }
        
        public Builder setSdpMid(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
          sdpMid_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearSdpMid() {
          bitField0_ = (bitField0_ & ~0x00000002);
          sdpMid_ = getDefaultInstance().getSdpMid();
          onChanged();
          return this;
        }
        
        public Builder setSdpMidBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
          sdpMid_ = value;
          onChanged();
          return this;
        }

        private int sdpMLineIndex_ ;
        
        public boolean hasSdpMLineIndex() {
          return ((bitField0_ & 0x00000004) == 0x00000004);
        }
        
        public int getSdpMLineIndex() {
          return sdpMLineIndex_;
        }
        
        public Builder setSdpMLineIndex(int value) {
          bitField0_ |= 0x00000004;
          sdpMLineIndex_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearSdpMLineIndex() {
          bitField0_ = (bitField0_ & ~0x00000004);
          sdpMLineIndex_ = 0;
          onChanged();
          return this;
        }

        private java.lang.Object sdp_ = "";
        
        public boolean hasSdp() {
          return ((bitField0_ & 0x00000008) == 0x00000008);
        }
        
        public java.lang.String getSdp() {
          java.lang.Object ref = sdp_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            sdp_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }
        
        public com.google.protobuf.ByteString
            getSdpBytes() {
          java.lang.Object ref = sdp_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            sdp_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }
        
        public Builder setSdp(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
          sdp_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearSdp() {
          bitField0_ = (bitField0_ & ~0x00000008);
          sdp_ = getDefaultInstance().getSdp();
          onChanged();
          return this;
        }
        
        public Builder setSdpBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
          sdp_ = value;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new IceUpdate(true);
        defaultInstance.initFields();
      }

    }

    public interface BusyOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasId();
      
      long getId();
    }
    
    public static final class Busy extends
        com.google.protobuf.GeneratedMessage
        implements BusyOrBuilder {
      private Busy(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Busy(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Busy defaultInstance;
      public static Busy getDefaultInstance() {
        return defaultInstance;
      }

      public Busy getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Busy(
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
                bitField0_ |= 0x00000001;
                id_ = input.readUInt64();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Busy_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Busy_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.Builder.class);
      }

      public static com.google.protobuf.Parser<Busy> PARSER =
          new com.google.protobuf.AbstractParser<Busy>() {
        public Busy parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Busy(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Busy> getParserForType() {
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

      private void initFields() {
        id_ = 0L;
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
          output.writeUInt64(1, id_);
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
            .computeUInt64Size(1, id_);
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.BusyOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Busy_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Busy_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.Builder.class);
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
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Busy_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.id_ = id_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.getDefaultInstance()) return this;
          if (other.hasId()) {
            setId(other.getId());
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy) e.getUnfinishedMessage();
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

      }

      static {
        defaultInstance = new Busy(true);
        defaultInstance.initFields();
      }

    }

    public interface HangupOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasId();
      
      long getId();
    }
    
    public static final class Hangup extends
        com.google.protobuf.GeneratedMessage
        implements HangupOrBuilder {
      private Hangup(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Hangup(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Hangup defaultInstance;
      public static Hangup getDefaultInstance() {
        return defaultInstance;
      }

      public Hangup getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Hangup(
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
                bitField0_ |= 0x00000001;
                id_ = input.readUInt64();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Hangup_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Hangup_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.Builder.class);
      }

      public static com.google.protobuf.Parser<Hangup> PARSER =
          new com.google.protobuf.AbstractParser<Hangup>() {
        public Hangup parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Hangup(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Hangup> getParserForType() {
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

      private void initFields() {
        id_ = 0L;
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
          output.writeUInt64(1, id_);
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
            .computeUInt64Size(1, id_);
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.HangupOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Hangup_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Hangup_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.Builder.class);
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
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_Hangup_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.id_ = id_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.getDefaultInstance()) return this;
          if (other.hasId()) {
            setId(other.getId());
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup) e.getUnfinishedMessage();
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

      }

      static {
        defaultInstance = new Hangup(true);
        defaultInstance.initFields();
      }

    }

    private int bitField0_;
    public static final int OFFER_FIELD_NUMBER = 1;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer offer_;
    
    public boolean hasOffer() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer getOffer() {
      return offer_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.OfferOrBuilder getOfferOrBuilder() {
      return offer_;
    }

    public static final int ANSWER_FIELD_NUMBER = 2;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer answer_;
    
    public boolean hasAnswer() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer getAnswer() {
      return answer_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.AnswerOrBuilder getAnswerOrBuilder() {
      return answer_;
    }

    public static final int ICEUPDATE_FIELD_NUMBER = 3;
    private java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate> iceUpdate_;
    
    public java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate> getIceUpdateList() {
      return iceUpdate_;
    }
    
    public java.util.List<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdateOrBuilder> 
        getIceUpdateOrBuilderList() {
      return iceUpdate_;
    }
    
    public int getIceUpdateCount() {
      return iceUpdate_.size();
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate getIceUpdate(int index) {
      return iceUpdate_.get(index);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdateOrBuilder getIceUpdateOrBuilder(
        int index) {
      return iceUpdate_.get(index);
    }

    public static final int HANGUP_FIELD_NUMBER = 4;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup hangup_;
    
    public boolean hasHangup() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup getHangup() {
      return hangup_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.HangupOrBuilder getHangupOrBuilder() {
      return hangup_;
    }

    public static final int BUSY_FIELD_NUMBER = 5;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy busy_;
    
    public boolean hasBusy() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy getBusy() {
      return busy_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.BusyOrBuilder getBusyOrBuilder() {
      return busy_;
    }

    private void initFields() {
      offer_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.getDefaultInstance();
      answer_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.getDefaultInstance();
      iceUpdate_ = java.util.Collections.emptyList();
      hangup_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.getDefaultInstance();
      busy_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.getDefaultInstance();
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
        output.writeMessage(1, offer_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, answer_);
      }
      for (int i = 0; i < iceUpdate_.size(); i++) {
        output.writeMessage(3, iceUpdate_.get(i));
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(4, hangup_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(5, busy_);
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
          .computeMessageSize(1, offer_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, answer_);
      }
      for (int i = 0; i < iceUpdate_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, iceUpdate_.get(i));
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, hangup_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, busy_);
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Builder.class);
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
          getOfferFieldBuilder();
          getAnswerFieldBuilder();
          getIceUpdateFieldBuilder();
          getHangupFieldBuilder();
          getBusyFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (offerBuilder_ == null) {
          offer_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.getDefaultInstance();
        } else {
          offerBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (answerBuilder_ == null) {
          answer_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.getDefaultInstance();
        } else {
          answerBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        if (iceUpdateBuilder_ == null) {
          iceUpdate_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
        } else {
          iceUpdateBuilder_.clear();
        }
        if (hangupBuilder_ == null) {
          hangup_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.getDefaultInstance();
        } else {
          hangupBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        if (busyBuilder_ == null) {
          busy_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.getDefaultInstance();
        } else {
          busyBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_CallMessage_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (offerBuilder_ == null) {
          result.offer_ = offer_;
        } else {
          result.offer_ = offerBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (answerBuilder_ == null) {
          result.answer_ = answer_;
        } else {
          result.answer_ = answerBuilder_.build();
        }
        if (iceUpdateBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004)) {
            iceUpdate_ = java.util.Collections.unmodifiableList(iceUpdate_);
            bitField0_ = (bitField0_ & ~0x00000004);
          }
          result.iceUpdate_ = iceUpdate_;
        } else {
          result.iceUpdate_ = iceUpdateBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000004;
        }
        if (hangupBuilder_ == null) {
          result.hangup_ = hangup_;
        } else {
          result.hangup_ = hangupBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000008;
        }
        if (busyBuilder_ == null) {
          result.busy_ = busy_;
        } else {
          result.busy_ = busyBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.getDefaultInstance()) return this;
        if (other.hasOffer()) {
          mergeOffer(other.getOffer());
        }
        if (other.hasAnswer()) {
          mergeAnswer(other.getAnswer());
        }
        if (iceUpdateBuilder_ == null) {
          if (!other.iceUpdate_.isEmpty()) {
            if (iceUpdate_.isEmpty()) {
              iceUpdate_ = other.iceUpdate_;
              bitField0_ = (bitField0_ & ~0x00000004);
            } else {
              ensureIceUpdateIsMutable();
              iceUpdate_.addAll(other.iceUpdate_);
            }
            onChanged();
          }
        } else {
          if (!other.iceUpdate_.isEmpty()) {
            if (iceUpdateBuilder_.isEmpty()) {
              iceUpdateBuilder_.dispose();
              iceUpdateBuilder_ = null;
              iceUpdate_ = other.iceUpdate_;
              bitField0_ = (bitField0_ & ~0x00000004);
              iceUpdateBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getIceUpdateFieldBuilder() : null;
            } else {
              iceUpdateBuilder_.addAllMessages(other.iceUpdate_);
            }
          }
        }
        if (other.hasHangup()) {
          mergeHangup(other.getHangup());
        }
        if (other.hasBusy()) {
          mergeBusy(other.getBusy());
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer offer_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.OfferOrBuilder> offerBuilder_;
      
      public boolean hasOffer() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer getOffer() {
        if (offerBuilder_ == null) {
          return offer_;
        } else {
          return offerBuilder_.getMessage();
        }
      }
      
      public Builder setOffer(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer value) {
        if (offerBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          offer_ = value;
          onChanged();
        } else {
          offerBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      
      public Builder setOffer(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.Builder builderForValue) {
        if (offerBuilder_ == null) {
          offer_ = builderForValue.build();
          onChanged();
        } else {
          offerBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      
      public Builder mergeOffer(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer value) {
        if (offerBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              offer_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.getDefaultInstance()) {
            offer_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.newBuilder(offer_).mergeFrom(value).buildPartial();
          } else {
            offer_ = value;
          }
          onChanged();
        } else {
          offerBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      
      public Builder clearOffer() {
        if (offerBuilder_ == null) {
          offer_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.getDefaultInstance();
          onChanged();
        } else {
          offerBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.Builder getOfferBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getOfferFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.OfferOrBuilder getOfferOrBuilder() {
        if (offerBuilder_ != null) {
          return offerBuilder_.getMessageOrBuilder();
        } else {
          return offer_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.OfferOrBuilder> 
          getOfferFieldBuilder() {
        if (offerBuilder_ == null) {
          offerBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Offer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.OfferOrBuilder>(
                  offer_,
                  getParentForChildren(),
                  isClean());
          offer_ = null;
        }
        return offerBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer answer_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.AnswerOrBuilder> answerBuilder_;
      
      public boolean hasAnswer() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer getAnswer() {
        if (answerBuilder_ == null) {
          return answer_;
        } else {
          return answerBuilder_.getMessage();
        }
      }
      
      public Builder setAnswer(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer value) {
        if (answerBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          answer_ = value;
          onChanged();
        } else {
          answerBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      
      public Builder setAnswer(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.Builder builderForValue) {
        if (answerBuilder_ == null) {
          answer_ = builderForValue.build();
          onChanged();
        } else {
          answerBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      
      public Builder mergeAnswer(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer value) {
        if (answerBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              answer_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.getDefaultInstance()) {
            answer_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.newBuilder(answer_).mergeFrom(value).buildPartial();
          } else {
            answer_ = value;
          }
          onChanged();
        } else {
          answerBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      
      public Builder clearAnswer() {
        if (answerBuilder_ == null) {
          answer_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.getDefaultInstance();
          onChanged();
        } else {
          answerBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.Builder getAnswerBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getAnswerFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.AnswerOrBuilder getAnswerOrBuilder() {
        if (answerBuilder_ != null) {
          return answerBuilder_.getMessageOrBuilder();
        } else {
          return answer_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.AnswerOrBuilder> 
          getAnswerFieldBuilder() {
        if (answerBuilder_ == null) {
          answerBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Answer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.AnswerOrBuilder>(
                  answer_,
                  getParentForChildren(),
                  isClean());
          answer_ = null;
        }
        return answerBuilder_;
      }

      private java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate> iceUpdate_ =
        java.util.Collections.emptyList();
      private void ensureIceUpdateIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          iceUpdate_ = new java.util.ArrayList<com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate>(iceUpdate_);
          bitField0_ |= 0x00000004;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdateOrBuilder> iceUpdateBuilder_;

      
      public java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate> getIceUpdateList() {
        if (iceUpdateBuilder_ == null) {
          return java.util.Collections.unmodifiableList(iceUpdate_);
        } else {
          return iceUpdateBuilder_.getMessageList();
        }
      }
      
      public int getIceUpdateCount() {
        if (iceUpdateBuilder_ == null) {
          return iceUpdate_.size();
        } else {
          return iceUpdateBuilder_.getCount();
        }
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate getIceUpdate(int index) {
        if (iceUpdateBuilder_ == null) {
          return iceUpdate_.get(index);
        } else {
          return iceUpdateBuilder_.getMessage(index);
        }
      }
      
      public Builder setIceUpdate(
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate value) {
        if (iceUpdateBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureIceUpdateIsMutable();
          iceUpdate_.set(index, value);
          onChanged();
        } else {
          iceUpdateBuilder_.setMessage(index, value);
        }
        return this;
      }
      
      public Builder setIceUpdate(
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder builderForValue) {
        if (iceUpdateBuilder_ == null) {
          ensureIceUpdateIsMutable();
          iceUpdate_.set(index, builderForValue.build());
          onChanged();
        } else {
          iceUpdateBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      
      public Builder addIceUpdate(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate value) {
        if (iceUpdateBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureIceUpdateIsMutable();
          iceUpdate_.add(value);
          onChanged();
        } else {
          iceUpdateBuilder_.addMessage(value);
        }
        return this;
      }
      
      public Builder addIceUpdate(
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate value) {
        if (iceUpdateBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureIceUpdateIsMutable();
          iceUpdate_.add(index, value);
          onChanged();
        } else {
          iceUpdateBuilder_.addMessage(index, value);
        }
        return this;
      }
      
      public Builder addIceUpdate(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder builderForValue) {
        if (iceUpdateBuilder_ == null) {
          ensureIceUpdateIsMutable();
          iceUpdate_.add(builderForValue.build());
          onChanged();
        } else {
          iceUpdateBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      
      public Builder addIceUpdate(
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder builderForValue) {
        if (iceUpdateBuilder_ == null) {
          ensureIceUpdateIsMutable();
          iceUpdate_.add(index, builderForValue.build());
          onChanged();
        } else {
          iceUpdateBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      
      public Builder addAllIceUpdate(
          java.lang.Iterable<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate> values) {
        if (iceUpdateBuilder_ == null) {
          ensureIceUpdateIsMutable();
          super.addAll(values, iceUpdate_);
          onChanged();
        } else {
          iceUpdateBuilder_.addAllMessages(values);
        }
        return this;
      }
      
      public Builder clearIceUpdate() {
        if (iceUpdateBuilder_ == null) {
          iceUpdate_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
          onChanged();
        } else {
          iceUpdateBuilder_.clear();
        }
        return this;
      }
      
      public Builder removeIceUpdate(int index) {
        if (iceUpdateBuilder_ == null) {
          ensureIceUpdateIsMutable();
          iceUpdate_.remove(index);
          onChanged();
        } else {
          iceUpdateBuilder_.remove(index);
        }
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder getIceUpdateBuilder(
          int index) {
        return getIceUpdateFieldBuilder().getBuilder(index);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdateOrBuilder getIceUpdateOrBuilder(
          int index) {
        if (iceUpdateBuilder_ == null) {
          return iceUpdate_.get(index);  } else {
          return iceUpdateBuilder_.getMessageOrBuilder(index);
        }
      }
      
      public java.util.List<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdateOrBuilder> 
           getIceUpdateOrBuilderList() {
        if (iceUpdateBuilder_ != null) {
          return iceUpdateBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(iceUpdate_);
        }
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder addIceUpdateBuilder() {
        return getIceUpdateFieldBuilder().addBuilder(
            com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.getDefaultInstance());
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder addIceUpdateBuilder(
          int index) {
        return getIceUpdateFieldBuilder().addBuilder(
            index, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.getDefaultInstance());
      }
      
      public java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder> 
           getIceUpdateBuilderList() {
        return getIceUpdateFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdateOrBuilder> 
          getIceUpdateFieldBuilder() {
        if (iceUpdateBuilder_ == null) {
          iceUpdateBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdate.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.IceUpdateOrBuilder>(
                  iceUpdate_,
                  ((bitField0_ & 0x00000004) == 0x00000004),
                  getParentForChildren(),
                  isClean());
          iceUpdate_ = null;
        }
        return iceUpdateBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup hangup_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.HangupOrBuilder> hangupBuilder_;
      
      public boolean hasHangup() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup getHangup() {
        if (hangupBuilder_ == null) {
          return hangup_;
        } else {
          return hangupBuilder_.getMessage();
        }
      }
      
      public Builder setHangup(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup value) {
        if (hangupBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          hangup_ = value;
          onChanged();
        } else {
          hangupBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder setHangup(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.Builder builderForValue) {
        if (hangupBuilder_ == null) {
          hangup_ = builderForValue.build();
          onChanged();
        } else {
          hangupBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder mergeHangup(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup value) {
        if (hangupBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              hangup_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.getDefaultInstance()) {
            hangup_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.newBuilder(hangup_).mergeFrom(value).buildPartial();
          } else {
            hangup_ = value;
          }
          onChanged();
        } else {
          hangupBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder clearHangup() {
        if (hangupBuilder_ == null) {
          hangup_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.getDefaultInstance();
          onChanged();
        } else {
          hangupBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.Builder getHangupBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getHangupFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.HangupOrBuilder getHangupOrBuilder() {
        if (hangupBuilder_ != null) {
          return hangupBuilder_.getMessageOrBuilder();
        } else {
          return hangup_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.HangupOrBuilder> 
          getHangupFieldBuilder() {
        if (hangupBuilder_ == null) {
          hangupBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Hangup.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.HangupOrBuilder>(
                  hangup_,
                  getParentForChildren(),
                  isClean());
          hangup_ = null;
        }
        return hangupBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy busy_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.BusyOrBuilder> busyBuilder_;
      
      public boolean hasBusy() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy getBusy() {
        if (busyBuilder_ == null) {
          return busy_;
        } else {
          return busyBuilder_.getMessage();
        }
      }
      
      public Builder setBusy(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy value) {
        if (busyBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          busy_ = value;
          onChanged();
        } else {
          busyBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder setBusy(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.Builder builderForValue) {
        if (busyBuilder_ == null) {
          busy_ = builderForValue.build();
          onChanged();
        } else {
          busyBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder mergeBusy(com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy value) {
        if (busyBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              busy_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.getDefaultInstance()) {
            busy_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.newBuilder(busy_).mergeFrom(value).buildPartial();
          } else {
            busy_ = value;
          }
          onChanged();
        } else {
          busyBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder clearBusy() {
        if (busyBuilder_ == null) {
          busy_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.getDefaultInstance();
          onChanged();
        } else {
          busyBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.Builder getBusyBuilder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getBusyFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.BusyOrBuilder getBusyOrBuilder() {
        if (busyBuilder_ != null) {
          return busyBuilder_.getMessageOrBuilder();
        } else {
          return busy_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.BusyOrBuilder> 
          getBusyFieldBuilder() {
        if (busyBuilder_ == null) {
          busyBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.Busy.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage.BusyOrBuilder>(
                  busy_,
                  getParentForChildren(),
                  isClean());
          busy_ = null;
        }
        return busyBuilder_;
      }

    }

    static {
      defaultInstance = new CallMessage(true);
      defaultInstance.initFields();
    }

  }

  public interface DataMessageOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasBody();
    
    java.lang.String getBody();
    
    com.google.protobuf.ByteString
        getBodyBytes();

    
    java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer> 
        getAttachmentsList();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getAttachments(int index);
    
    int getAttachmentsCount();
    
    java.util.List<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> 
        getAttachmentsOrBuilderList();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getAttachmentsOrBuilder(
        int index);

    
    boolean hasGroup();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext getGroup();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContextOrBuilder getGroupOrBuilder();

    
    boolean hasFlags();
    
    int getFlags();

    
    boolean hasExpireTimer();
    
    int getExpireTimer();

    
    boolean hasProfileKey();
    
    com.google.protobuf.ByteString getProfileKey();

    
    boolean hasTimestamp();
    
    long getTimestamp();
  }
  
  public static final class DataMessage extends
      com.google.protobuf.GeneratedMessage
      implements DataMessageOrBuilder {
    private DataMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private DataMessage(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final DataMessage defaultInstance;
    public static DataMessage getDefaultInstance() {
      return defaultInstance;
    }

    public DataMessage getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private DataMessage(
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
                attachments_ = new java.util.ArrayList<com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer>();
                mutable_bitField0_ |= 0x00000002;
              }
              attachments_.add(input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.PARSER, extensionRegistry));
              break;
            }
            case 26: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = group_.toBuilder();
              }
              group_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.PARSER, extensionRegistry);
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
            case 40: {
              bitField0_ |= 0x00000008;
              expireTimer_ = input.readUInt32();
              break;
            }
            case 50: {
              bitField0_ |= 0x00000010;
              profileKey_ = input.readBytes();
              break;
            }
            case 56: {
              bitField0_ |= 0x00000020;
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
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          attachments_ = java.util.Collections.unmodifiableList(attachments_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_DataMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_DataMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder.class);
    }

    public static com.google.protobuf.Parser<DataMessage> PARSER =
        new com.google.protobuf.AbstractParser<DataMessage>() {
      public DataMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new DataMessage(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<DataMessage> getParserForType() {
      return PARSER;
    }

    
    public enum Flags
        implements com.google.protobuf.ProtocolMessageEnum {
      
      END_SESSION(0, 1),
      
      EXPIRATION_TIMER_UPDATE(1, 2),
      
      PROFILE_KEY_UPDATE(2, 4),
      ;

      
      public static final int END_SESSION_VALUE = 1;
      
      public static final int EXPIRATION_TIMER_UPDATE_VALUE = 2;
      
      public static final int PROFILE_KEY_UPDATE_VALUE = 4;

      public final int getNumber() { return value; }

      public static Flags valueOf(int value) {
        switch (value) {
          case 1: return END_SESSION;
          case 2: return EXPIRATION_TIMER_UPDATE;
          case 4: return PROFILE_KEY_UPDATE;
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDescriptor().getEnumTypes().get(0);
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
    private java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer> attachments_;
    
    public java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer> getAttachmentsList() {
      return attachments_;
    }
    
    public java.util.List<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> 
        getAttachmentsOrBuilderList() {
      return attachments_;
    }
    
    public int getAttachmentsCount() {
      return attachments_.size();
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getAttachments(int index) {
      return attachments_.get(index);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getAttachmentsOrBuilder(
        int index) {
      return attachments_.get(index);
    }

    public static final int GROUP_FIELD_NUMBER = 3;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext group_;
    
    public boolean hasGroup() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext getGroup() {
      return group_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContextOrBuilder getGroupOrBuilder() {
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

    public static final int EXPIRETIMER_FIELD_NUMBER = 5;
    private int expireTimer_;
    
    public boolean hasExpireTimer() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public int getExpireTimer() {
      return expireTimer_;
    }

    public static final int PROFILEKEY_FIELD_NUMBER = 6;
    private com.google.protobuf.ByteString profileKey_;
    
    public boolean hasProfileKey() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    
    public com.google.protobuf.ByteString getProfileKey() {
      return profileKey_;
    }

    public static final int TIMESTAMP_FIELD_NUMBER = 7;
    private long timestamp_;
    
    public boolean hasTimestamp() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    
    public long getTimestamp() {
      return timestamp_;
    }

    private void initFields() {
      body_ = "";
      attachments_ = java.util.Collections.emptyList();
      group_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.getDefaultInstance();
      flags_ = 0;
      expireTimer_ = 0;
      profileKey_ = com.google.protobuf.ByteString.EMPTY;
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
        output.writeUInt32(5, expireTimer_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(6, profileKey_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeUInt64(7, timestamp_);
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
          .computeUInt32Size(5, expireTimer_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, profileKey_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(7, timestamp_);
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_DataMessage_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_DataMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder.class);
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
          group_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.getDefaultInstance();
        } else {
          groupBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        flags_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        expireTimer_ = 0;
        bitField0_ = (bitField0_ & ~0x00000010);
        profileKey_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000020);
        timestamp_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_DataMessage_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage(this);
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
        result.expireTimer_ = expireTimer_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000010;
        }
        result.profileKey_ = profileKey_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000020;
        }
        result.timestamp_ = timestamp_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance()) return this;
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
        if (other.hasExpireTimer()) {
          setExpireTimer(other.getExpireTimer());
        }
        if (other.hasProfileKey()) {
          setProfileKey(other.getProfileKey());
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage) e.getUnfinishedMessage();
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

      private java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer> attachments_ =
        java.util.Collections.emptyList();
      private void ensureAttachmentsIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          attachments_ = new java.util.ArrayList<com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer>(attachments_);
          bitField0_ |= 0x00000002;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> attachmentsBuilder_;

      
      public java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer> getAttachmentsList() {
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
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getAttachments(int index) {
        if (attachmentsBuilder_ == null) {
          return attachments_.get(index);
        } else {
          return attachmentsBuilder_.getMessage(index);
        }
      }
      
      public Builder setAttachments(
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer value) {
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
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder builderForValue) {
        if (attachmentsBuilder_ == null) {
          ensureAttachmentsIsMutable();
          attachments_.set(index, builderForValue.build());
          onChanged();
        } else {
          attachmentsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      
      public Builder addAttachments(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer value) {
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
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer value) {
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder builderForValue) {
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
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder builderForValue) {
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
          java.lang.Iterable<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer> values) {
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
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder getAttachmentsBuilder(
          int index) {
        return getAttachmentsFieldBuilder().getBuilder(index);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getAttachmentsOrBuilder(
          int index) {
        if (attachmentsBuilder_ == null) {
          return attachments_.get(index);  } else {
          return attachmentsBuilder_.getMessageOrBuilder(index);
        }
      }
      
      public java.util.List<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> 
           getAttachmentsOrBuilderList() {
        if (attachmentsBuilder_ != null) {
          return attachmentsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(attachments_);
        }
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder addAttachmentsBuilder() {
        return getAttachmentsFieldBuilder().addBuilder(
            com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance());
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder addAttachmentsBuilder(
          int index) {
        return getAttachmentsFieldBuilder().addBuilder(
            index, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance());
      }
      
      public java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder> 
           getAttachmentsBuilderList() {
        return getAttachmentsFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> 
          getAttachmentsFieldBuilder() {
        if (attachmentsBuilder_ == null) {
          attachmentsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder>(
                  attachments_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          attachments_ = null;
        }
        return attachmentsBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext group_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContextOrBuilder> groupBuilder_;
      
      public boolean hasGroup() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext getGroup() {
        if (groupBuilder_ == null) {
          return group_;
        } else {
          return groupBuilder_.getMessage();
        }
      }
      
      public Builder setGroup(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext value) {
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Builder builderForValue) {
        if (groupBuilder_ == null) {
          group_ = builderForValue.build();
          onChanged();
        } else {
          groupBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder mergeGroup(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext value) {
        if (groupBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              group_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.getDefaultInstance()) {
            group_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.newBuilder(group_).mergeFrom(value).buildPartial();
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
          group_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.getDefaultInstance();
          onChanged();
        } else {
          groupBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Builder getGroupBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getGroupFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContextOrBuilder getGroupOrBuilder() {
        if (groupBuilder_ != null) {
          return groupBuilder_.getMessageOrBuilder();
        } else {
          return group_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContextOrBuilder> 
          getGroupFieldBuilder() {
        if (groupBuilder_ == null) {
          groupBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContextOrBuilder>(
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

      private int expireTimer_ ;
      
      public boolean hasExpireTimer() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      
      public int getExpireTimer() {
        return expireTimer_;
      }
      
      public Builder setExpireTimer(int value) {
        bitField0_ |= 0x00000010;
        expireTimer_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearExpireTimer() {
        bitField0_ = (bitField0_ & ~0x00000010);
        expireTimer_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString profileKey_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasProfileKey() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      
      public com.google.protobuf.ByteString getProfileKey() {
        return profileKey_;
      }
      
      public Builder setProfileKey(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        profileKey_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearProfileKey() {
        bitField0_ = (bitField0_ & ~0x00000020);
        profileKey_ = getDefaultInstance().getProfileKey();
        onChanged();
        return this;
      }

      private long timestamp_ ;
      
      public boolean hasTimestamp() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      
      public long getTimestamp() {
        return timestamp_;
      }
      
      public Builder setTimestamp(long value) {
        bitField0_ |= 0x00000040;
        timestamp_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearTimestamp() {
        bitField0_ = (bitField0_ & ~0x00000040);
        timestamp_ = 0L;
        onChanged();
        return this;
      }

    }

    static {
      defaultInstance = new DataMessage(true);
      defaultInstance.initFields();
    }

  }

  public interface NullMessageOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasPadding();
    
    com.google.protobuf.ByteString getPadding();
  }
  
  public static final class NullMessage extends
      com.google.protobuf.GeneratedMessage
      implements NullMessageOrBuilder {
    private NullMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private NullMessage(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final NullMessage defaultInstance;
    public static NullMessage getDefaultInstance() {
      return defaultInstance;
    }

    public NullMessage getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private NullMessage(
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
              padding_ = input.readBytes();
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
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_NullMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_NullMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.Builder.class);
    }

    public static com.google.protobuf.Parser<NullMessage> PARSER =
        new com.google.protobuf.AbstractParser<NullMessage>() {
      public NullMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new NullMessage(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<NullMessage> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int PADDING_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString padding_;
    
    public boolean hasPadding() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    
    public com.google.protobuf.ByteString getPadding() {
      return padding_;
    }

    private void initFields() {
      padding_ = com.google.protobuf.ByteString.EMPTY;
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
        output.writeBytes(1, padding_);
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
          .computeBytesSize(1, padding_);
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_NullMessage_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_NullMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.Builder.class);
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
        padding_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_NullMessage_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.padding_ = padding_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage.getDefaultInstance()) return this;
        if (other.hasPadding()) {
          setPadding(other.getPadding());
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.ByteString padding_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasPadding() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.google.protobuf.ByteString getPadding() {
        return padding_;
      }
      
      public Builder setPadding(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        padding_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearPadding() {
        bitField0_ = (bitField0_ & ~0x00000001);
        padding_ = getDefaultInstance().getPadding();
        onChanged();
        return this;
      }

    }

    static {
      defaultInstance = new NullMessage(true);
      defaultInstance.initFields();
    }

  }

  public interface ReceiptMessageOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasType();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type getType();

    
    java.util.List<java.lang.Long> getTimestampList();
    
    int getTimestampCount();
    
    long getTimestamp(int index);
  }
  
  public static final class ReceiptMessage extends
      com.google.protobuf.GeneratedMessage
      implements ReceiptMessageOrBuilder {
    private ReceiptMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private ReceiptMessage(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final ReceiptMessage defaultInstance;
    public static ReceiptMessage getDefaultInstance() {
      return defaultInstance;
    }

    public ReceiptMessage getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private ReceiptMessage(
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
              com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type value = com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                type_ = value;
              }
              break;
            }
            case 16: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                timestamp_ = new java.util.ArrayList<java.lang.Long>();
                mutable_bitField0_ |= 0x00000002;
              }
              timestamp_.add(input.readUInt64());
              break;
            }
            case 18: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002) && input.getBytesUntilLimit() > 0) {
                timestamp_ = new java.util.ArrayList<java.lang.Long>();
                mutable_bitField0_ |= 0x00000002;
              }
              while (input.getBytesUntilLimit() > 0) {
                timestamp_.add(input.readUInt64());
              }
              input.popLimit(limit);
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
          timestamp_ = java.util.Collections.unmodifiableList(timestamp_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ReceiptMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ReceiptMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Builder.class);
    }

    public static com.google.protobuf.Parser<ReceiptMessage> PARSER =
        new com.google.protobuf.AbstractParser<ReceiptMessage>() {
      public ReceiptMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ReceiptMessage(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<ReceiptMessage> getParserForType() {
      return PARSER;
    }

    
    public enum Type
        implements com.google.protobuf.ProtocolMessageEnum {
      
      DELIVERY(0, 0),
      
      READ(1, 1),
      ;

      
      public static final int DELIVERY_VALUE = 0;
      
      public static final int READ_VALUE = 1;

      public final int getNumber() { return value; }

      public static Type valueOf(int value) {
        switch (value) {
          case 0: return DELIVERY;
          case 1: return READ;
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.getDescriptor().getEnumTypes().get(0);
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
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type type_;
    
    public boolean hasType() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type getType() {
      return type_;
    }

    public static final int TIMESTAMP_FIELD_NUMBER = 2;
    private java.util.List<java.lang.Long> timestamp_;
    
    public java.util.List<java.lang.Long>
        getTimestampList() {
      return timestamp_;
    }
    
    public int getTimestampCount() {
      return timestamp_.size();
    }
    
    public long getTimestamp(int index) {
      return timestamp_.get(index);
    }

    private void initFields() {
      type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type.DELIVERY;
      timestamp_ = java.util.Collections.emptyList();
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
      for (int i = 0; i < timestamp_.size(); i++) {
        output.writeUInt64(2, timestamp_.get(i));
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
      {
        int dataSize = 0;
        for (int i = 0; i < timestamp_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeUInt64SizeNoTag(timestamp_.get(i));
        }
        size += dataSize;
        size += 1 * getTimestampList().size();
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ReceiptMessage_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ReceiptMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Builder.class);
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
        type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type.DELIVERY;
        bitField0_ = (bitField0_ & ~0x00000001);
        timestamp_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ReceiptMessage_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.type_ = type_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          timestamp_ = java.util.Collections.unmodifiableList(timestamp_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.timestamp_ = timestamp_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.getDefaultInstance()) return this;
        if (other.hasType()) {
          setType(other.getType());
        }
        if (!other.timestamp_.isEmpty()) {
          if (timestamp_.isEmpty()) {
            timestamp_ = other.timestamp_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureTimestampIsMutable();
            timestamp_.addAll(other.timestamp_);
          }
          onChanged();
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type.DELIVERY;
      
      public boolean hasType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type getType() {
        return type_;
      }
      
      public Builder setType(com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type value) {
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
        type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage.Type.DELIVERY;
        onChanged();
        return this;
      }

      private java.util.List<java.lang.Long> timestamp_ = java.util.Collections.emptyList();
      private void ensureTimestampIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          timestamp_ = new java.util.ArrayList<java.lang.Long>(timestamp_);
          bitField0_ |= 0x00000002;
         }
      }
      
      public java.util.List<java.lang.Long>
          getTimestampList() {
        return java.util.Collections.unmodifiableList(timestamp_);
      }
      
      public int getTimestampCount() {
        return timestamp_.size();
      }
      
      public long getTimestamp(int index) {
        return timestamp_.get(index);
      }
      
      public Builder setTimestamp(
          int index, long value) {
        ensureTimestampIsMutable();
        timestamp_.set(index, value);
        onChanged();
        return this;
      }
      
      public Builder addTimestamp(long value) {
        ensureTimestampIsMutable();
        timestamp_.add(value);
        onChanged();
        return this;
      }
      
      public Builder addAllTimestamp(
          java.lang.Iterable<? extends java.lang.Long> values) {
        ensureTimestampIsMutable();
        super.addAll(values, timestamp_);
        onChanged();
        return this;
      }
      
      public Builder clearTimestamp() {
        timestamp_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }

    }

    static {
      defaultInstance = new ReceiptMessage(true);
      defaultInstance.initFields();
    }

  }

  public interface VerifiedOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasDestination();
    
    java.lang.String getDestination();
    
    com.google.protobuf.ByteString
        getDestinationBytes();

    
    boolean hasIdentityKey();
    
    com.google.protobuf.ByteString getIdentityKey();

    
    boolean hasState();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State getState();

    
    boolean hasNullMessage();
    
    com.google.protobuf.ByteString getNullMessage();
  }
  
  public static final class Verified extends
      com.google.protobuf.GeneratedMessage
      implements VerifiedOrBuilder {
    private Verified(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Verified(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Verified defaultInstance;
    public static Verified getDefaultInstance() {
      return defaultInstance;
    }

    public Verified getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Verified(
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
            case 18: {
              bitField0_ |= 0x00000002;
              identityKey_ = input.readBytes();
              break;
            }
            case 24: {
              int rawValue = input.readEnum();
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State value = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(3, rawValue);
              } else {
                bitField0_ |= 0x00000004;
                state_ = value;
              }
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              nullMessage_ = input.readBytes();
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
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Verified_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Verified_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder.class);
    }

    public static com.google.protobuf.Parser<Verified> PARSER =
        new com.google.protobuf.AbstractParser<Verified>() {
      public Verified parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Verified(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Verified> getParserForType() {
      return PARSER;
    }

    
    public enum State
        implements com.google.protobuf.ProtocolMessageEnum {
      
      DEFAULT(0, 0),
      
      VERIFIED(1, 1),
      
      UNVERIFIED(2, 2),
      ;

      
      public static final int DEFAULT_VALUE = 0;
      
      public static final int VERIFIED_VALUE = 1;
      
      public static final int UNVERIFIED_VALUE = 2;

      public final int getNumber() { return value; }

      public static State valueOf(int value) {
        switch (value) {
          case 0: return DEFAULT;
          case 1: return VERIFIED;
          case 2: return UNVERIFIED;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<State>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<State>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<State>() {
              public State findValueByNumber(int number) {
                return State.valueOf(number);
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDescriptor().getEnumTypes().get(0);
      }

      private static final State[] VALUES = values();

      public static State valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int index;
      private final int value;

      private State(int index, int value) {
        this.index = index;
        this.value = value;
      }

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

    public static final int IDENTITYKEY_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString identityKey_;
    
    public boolean hasIdentityKey() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    
    public com.google.protobuf.ByteString getIdentityKey() {
      return identityKey_;
    }

    public static final int STATE_FIELD_NUMBER = 3;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State state_;
    
    public boolean hasState() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State getState() {
      return state_;
    }

    public static final int NULLMESSAGE_FIELD_NUMBER = 4;
    private com.google.protobuf.ByteString nullMessage_;
    
    public boolean hasNullMessage() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public com.google.protobuf.ByteString getNullMessage() {
      return nullMessage_;
    }

    private void initFields() {
      destination_ = "";
      identityKey_ = com.google.protobuf.ByteString.EMPTY;
      state_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State.DEFAULT;
      nullMessage_ = com.google.protobuf.ByteString.EMPTY;
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
        output.writeBytes(2, identityKey_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeEnum(3, state_.getNumber());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, nullMessage_);
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
          .computeBytesSize(2, identityKey_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(3, state_.getNumber());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, nullMessage_);
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Verified_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Verified_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder.class);
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
        identityKey_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        state_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State.DEFAULT;
        bitField0_ = (bitField0_ & ~0x00000004);
        nullMessage_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_Verified_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.destination_ = destination_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.identityKey_ = identityKey_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.state_ = state_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.nullMessage_ = nullMessage_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance()) return this;
        if (other.hasDestination()) {
          bitField0_ |= 0x00000001;
          destination_ = other.destination_;
          onChanged();
        }
        if (other.hasIdentityKey()) {
          setIdentityKey(other.getIdentityKey());
        }
        if (other.hasState()) {
          setState(other.getState());
        }
        if (other.hasNullMessage()) {
          setNullMessage(other.getNullMessage());
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified) e.getUnfinishedMessage();
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

      private com.google.protobuf.ByteString identityKey_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasIdentityKey() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public com.google.protobuf.ByteString getIdentityKey() {
        return identityKey_;
      }
      
      public Builder setIdentityKey(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        identityKey_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearIdentityKey() {
        bitField0_ = (bitField0_ & ~0x00000002);
        identityKey_ = getDefaultInstance().getIdentityKey();
        onChanged();
        return this;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State state_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State.DEFAULT;
      
      public boolean hasState() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State getState() {
        return state_;
      }
      
      public Builder setState(com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000004;
        state_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearState() {
        bitField0_ = (bitField0_ & ~0x00000004);
        state_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.State.DEFAULT;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString nullMessage_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasNullMessage() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public com.google.protobuf.ByteString getNullMessage() {
        return nullMessage_;
      }
      
      public Builder setNullMessage(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        nullMessage_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearNullMessage() {
        bitField0_ = (bitField0_ & ~0x00000008);
        nullMessage_ = getDefaultInstance().getNullMessage();
        onChanged();
        return this;
      }

    }

    static {
      defaultInstance = new Verified(true);
      defaultInstance.initFields();
    }

  }

  public interface SyncMessageOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasSent();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent getSent();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.SentOrBuilder getSentOrBuilder();

    
    boolean hasContacts();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts getContacts();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ContactsOrBuilder getContactsOrBuilder();

    
    boolean hasGroups();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups getGroups();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.GroupsOrBuilder getGroupsOrBuilder();

    
    boolean hasRequest();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request getRequest();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.RequestOrBuilder getRequestOrBuilder();

    
    java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read> 
        getReadList();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read getRead(int index);
    
    int getReadCount();
    
    java.util.List<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ReadOrBuilder> 
        getReadOrBuilderList();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ReadOrBuilder getReadOrBuilder(
        int index);

    
    boolean hasBlocked();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked getBlocked();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.BlockedOrBuilder getBlockedOrBuilder();

    
    boolean hasVerified();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified getVerified();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder getVerifiedOrBuilder();

    
    boolean hasConfiguration();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration getConfiguration();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ConfigurationOrBuilder getConfigurationOrBuilder();

    
    boolean hasPadding();
    
    com.google.protobuf.ByteString getPadding();
  }
  
  public static final class SyncMessage extends
      com.google.protobuf.GeneratedMessage
      implements SyncMessageOrBuilder {
    private SyncMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private SyncMessage(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final SyncMessage defaultInstance;
    public static SyncMessage getDefaultInstance() {
      return defaultInstance;
    }

    public SyncMessage getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private SyncMessage(
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
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = sent_.toBuilder();
              }
              sent_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(sent_);
                sent_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 18: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = contacts_.toBuilder();
              }
              contacts_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(contacts_);
                contacts_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
            case 26: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = groups_.toBuilder();
              }
              groups_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(groups_);
                groups_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 34: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = request_.toBuilder();
              }
              request_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(request_);
                request_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
            case 42: {
              if (!((mutable_bitField0_ & 0x00000010) == 0x00000010)) {
                read_ = new java.util.ArrayList<com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read>();
                mutable_bitField0_ |= 0x00000010;
              }
              read_.add(input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.PARSER, extensionRegistry));
              break;
            }
            case 50: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.Builder subBuilder = null;
              if (((bitField0_ & 0x00000010) == 0x00000010)) {
                subBuilder = blocked_.toBuilder();
              }
              blocked_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(blocked_);
                blocked_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000010;
              break;
            }
            case 58: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder subBuilder = null;
              if (((bitField0_ & 0x00000020) == 0x00000020)) {
                subBuilder = verified_.toBuilder();
              }
              verified_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(verified_);
                verified_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000020;
              break;
            }
            case 66: {
              bitField0_ |= 0x00000080;
              padding_ = input.readBytes();
              break;
            }
            case 74: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.Builder subBuilder = null;
              if (((bitField0_ & 0x00000040) == 0x00000040)) {
                subBuilder = configuration_.toBuilder();
              }
              configuration_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(configuration_);
                configuration_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000040;
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
        if (((mutable_bitField0_ & 0x00000010) == 0x00000010)) {
          read_ = java.util.Collections.unmodifiableList(read_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Builder.class);
    }

    public static com.google.protobuf.Parser<SyncMessage> PARSER =
        new com.google.protobuf.AbstractParser<SyncMessage>() {
      public SyncMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new SyncMessage(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<SyncMessage> getParserForType() {
      return PARSER;
    }

    public interface SentOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasDestination();
      
      java.lang.String getDestination();
      
      com.google.protobuf.ByteString
          getDestinationBytes();

      
      boolean hasTimestamp();
      
      long getTimestamp();

      
      boolean hasMessage();
      
      com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage getMessage();
      
      com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder getMessageOrBuilder();

      
      boolean hasExpirationStartTimestamp();
      
      long getExpirationStartTimestamp();
    }
    
    public static final class Sent extends
        com.google.protobuf.GeneratedMessage
        implements SentOrBuilder {
      private Sent(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Sent(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Sent defaultInstance;
      public static Sent getDefaultInstance() {
        return defaultInstance;
      }

      public Sent getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Sent(
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
              case 26: {
                com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder subBuilder = null;
                if (((bitField0_ & 0x00000004) == 0x00000004)) {
                  subBuilder = message_.toBuilder();
                }
                message_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.PARSER, extensionRegistry);
                if (subBuilder != null) {
                  subBuilder.mergeFrom(message_);
                  message_ = subBuilder.buildPartial();
                }
                bitField0_ |= 0x00000004;
                break;
              }
              case 32: {
                bitField0_ |= 0x00000008;
                expirationStartTimestamp_ = input.readUInt64();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Sent_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Sent_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.Builder.class);
      }

      public static com.google.protobuf.Parser<Sent> PARSER =
          new com.google.protobuf.AbstractParser<Sent>() {
        public Sent parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Sent(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Sent> getParserForType() {
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

      public static final int MESSAGE_FIELD_NUMBER = 3;
      private com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage message_;
      
      public boolean hasMessage() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage getMessage() {
        return message_;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder getMessageOrBuilder() {
        return message_;
      }

      public static final int EXPIRATIONSTARTTIMESTAMP_FIELD_NUMBER = 4;
      private long expirationStartTimestamp_;
      
      public boolean hasExpirationStartTimestamp() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public long getExpirationStartTimestamp() {
        return expirationStartTimestamp_;
      }

      private void initFields() {
        destination_ = "";
        timestamp_ = 0L;
        message_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance();
        expirationStartTimestamp_ = 0L;
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
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          output.writeMessage(3, message_);
        }
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          output.writeUInt64(4, expirationStartTimestamp_);
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
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          size += com.google.protobuf.CodedOutputStream
            .computeMessageSize(3, message_);
        }
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          size += com.google.protobuf.CodedOutputStream
            .computeUInt64Size(4, expirationStartTimestamp_);
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.SentOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Sent_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Sent_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.Builder.class);
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
            getMessageFieldBuilder();
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
          if (messageBuilder_ == null) {
            message_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance();
          } else {
            messageBuilder_.clear();
          }
          bitField0_ = (bitField0_ & ~0x00000004);
          expirationStartTimestamp_ = 0L;
          bitField0_ = (bitField0_ & ~0x00000008);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Sent_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent(this);
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
          if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
            to_bitField0_ |= 0x00000004;
          }
          if (messageBuilder_ == null) {
            result.message_ = message_;
          } else {
            result.message_ = messageBuilder_.build();
          }
          if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
            to_bitField0_ |= 0x00000008;
          }
          result.expirationStartTimestamp_ = expirationStartTimestamp_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.getDefaultInstance()) return this;
          if (other.hasDestination()) {
            bitField0_ |= 0x00000001;
            destination_ = other.destination_;
            onChanged();
          }
          if (other.hasTimestamp()) {
            setTimestamp(other.getTimestamp());
          }
          if (other.hasMessage()) {
            mergeMessage(other.getMessage());
          }
          if (other.hasExpirationStartTimestamp()) {
            setExpirationStartTimestamp(other.getExpirationStartTimestamp());
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent) e.getUnfinishedMessage();
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

        private com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage message_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance();
        private com.google.protobuf.SingleFieldBuilder<
            com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder> messageBuilder_;
        
        public boolean hasMessage() {
          return ((bitField0_ & 0x00000004) == 0x00000004);
        }
        
        public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage getMessage() {
          if (messageBuilder_ == null) {
            return message_;
          } else {
            return messageBuilder_.getMessage();
          }
        }
        
        public Builder setMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage value) {
          if (messageBuilder_ == null) {
            if (value == null) {
              throw new NullPointerException();
            }
            message_ = value;
            onChanged();
          } else {
            messageBuilder_.setMessage(value);
          }
          bitField0_ |= 0x00000004;
          return this;
        }
        
        public Builder setMessage(
            com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder builderForValue) {
          if (messageBuilder_ == null) {
            message_ = builderForValue.build();
            onChanged();
          } else {
            messageBuilder_.setMessage(builderForValue.build());
          }
          bitField0_ |= 0x00000004;
          return this;
        }
        
        public Builder mergeMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage value) {
          if (messageBuilder_ == null) {
            if (((bitField0_ & 0x00000004) == 0x00000004) &&
                message_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance()) {
              message_ =
                com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.newBuilder(message_).mergeFrom(value).buildPartial();
            } else {
              message_ = value;
            }
            onChanged();
          } else {
            messageBuilder_.mergeFrom(value);
          }
          bitField0_ |= 0x00000004;
          return this;
        }
        
        public Builder clearMessage() {
          if (messageBuilder_ == null) {
            message_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.getDefaultInstance();
            onChanged();
          } else {
            messageBuilder_.clear();
          }
          bitField0_ = (bitField0_ & ~0x00000004);
          return this;
        }
        
        public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder getMessageBuilder() {
          bitField0_ |= 0x00000004;
          onChanged();
          return getMessageFieldBuilder().getBuilder();
        }
        
        public com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder getMessageOrBuilder() {
          if (messageBuilder_ != null) {
            return messageBuilder_.getMessageOrBuilder();
          } else {
            return message_;
          }
        }
        
        private com.google.protobuf.SingleFieldBuilder<
            com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder> 
            getMessageFieldBuilder() {
          if (messageBuilder_ == null) {
            messageBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessageOrBuilder>(
                    message_,
                    getParentForChildren(),
                    isClean());
            message_ = null;
          }
          return messageBuilder_;
        }

        private long expirationStartTimestamp_ ;
        
        public boolean hasExpirationStartTimestamp() {
          return ((bitField0_ & 0x00000008) == 0x00000008);
        }
        
        public long getExpirationStartTimestamp() {
          return expirationStartTimestamp_;
        }
        
        public Builder setExpirationStartTimestamp(long value) {
          bitField0_ |= 0x00000008;
          expirationStartTimestamp_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearExpirationStartTimestamp() {
          bitField0_ = (bitField0_ & ~0x00000008);
          expirationStartTimestamp_ = 0L;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new Sent(true);
        defaultInstance.initFields();
      }

    }

    public interface ContactsOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasBlob();
      
      com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getBlob();
      
      com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getBlobOrBuilder();

      
      boolean hasComplete();
      
      boolean getComplete();
    }
    
    public static final class Contacts extends
        com.google.protobuf.GeneratedMessage
        implements ContactsOrBuilder {
      private Contacts(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Contacts(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Contacts defaultInstance;
      public static Contacts getDefaultInstance() {
        return defaultInstance;
      }

      public Contacts getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Contacts(
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
                com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder subBuilder = null;
                if (((bitField0_ & 0x00000001) == 0x00000001)) {
                  subBuilder = blob_.toBuilder();
                }
                blob_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.PARSER, extensionRegistry);
                if (subBuilder != null) {
                  subBuilder.mergeFrom(blob_);
                  blob_ = subBuilder.buildPartial();
                }
                bitField0_ |= 0x00000001;
                break;
              }
              case 16: {
                bitField0_ |= 0x00000002;
                complete_ = input.readBool();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Contacts_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Contacts_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.Builder.class);
      }

      public static com.google.protobuf.Parser<Contacts> PARSER =
          new com.google.protobuf.AbstractParser<Contacts>() {
        public Contacts parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Contacts(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Contacts> getParserForType() {
        return PARSER;
      }

      private int bitField0_;
      public static final int BLOB_FIELD_NUMBER = 1;
      private com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer blob_;
      
      public boolean hasBlob() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getBlob() {
        return blob_;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getBlobOrBuilder() {
        return blob_;
      }

      public static final int COMPLETE_FIELD_NUMBER = 2;
      private boolean complete_;
      
      public boolean hasComplete() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public boolean getComplete() {
        return complete_;
      }

      private void initFields() {
        blob_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
        complete_ = false;
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
          output.writeMessage(1, blob_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeBool(2, complete_);
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
            .computeMessageSize(1, blob_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBoolSize(2, complete_);
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ContactsOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Contacts_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Contacts_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.Builder.class);
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
            getBlobFieldBuilder();
          }
        }
        private static Builder create() {
          return new Builder();
        }

        public Builder clear() {
          super.clear();
          if (blobBuilder_ == null) {
            blob_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
          } else {
            blobBuilder_.clear();
          }
          bitField0_ = (bitField0_ & ~0x00000001);
          complete_ = false;
          bitField0_ = (bitField0_ & ~0x00000002);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Contacts_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          if (blobBuilder_ == null) {
            result.blob_ = blob_;
          } else {
            result.blob_ = blobBuilder_.build();
          }
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.complete_ = complete_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.getDefaultInstance()) return this;
          if (other.hasBlob()) {
            mergeBlob(other.getBlob());
          }
          if (other.hasComplete()) {
            setComplete(other.getComplete());
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer blob_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
        private com.google.protobuf.SingleFieldBuilder<
            com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> blobBuilder_;
        
        public boolean hasBlob() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }
        
        public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getBlob() {
          if (blobBuilder_ == null) {
            return blob_;
          } else {
            return blobBuilder_.getMessage();
          }
        }
        
        public Builder setBlob(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer value) {
          if (blobBuilder_ == null) {
            if (value == null) {
              throw new NullPointerException();
            }
            blob_ = value;
            onChanged();
          } else {
            blobBuilder_.setMessage(value);
          }
          bitField0_ |= 0x00000001;
          return this;
        }
        
        public Builder setBlob(
            com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder builderForValue) {
          if (blobBuilder_ == null) {
            blob_ = builderForValue.build();
            onChanged();
          } else {
            blobBuilder_.setMessage(builderForValue.build());
          }
          bitField0_ |= 0x00000001;
          return this;
        }
        
        public Builder mergeBlob(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer value) {
          if (blobBuilder_ == null) {
            if (((bitField0_ & 0x00000001) == 0x00000001) &&
                blob_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance()) {
              blob_ =
                com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.newBuilder(blob_).mergeFrom(value).buildPartial();
            } else {
              blob_ = value;
            }
            onChanged();
          } else {
            blobBuilder_.mergeFrom(value);
          }
          bitField0_ |= 0x00000001;
          return this;
        }
        
        public Builder clearBlob() {
          if (blobBuilder_ == null) {
            blob_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
            onChanged();
          } else {
            blobBuilder_.clear();
          }
          bitField0_ = (bitField0_ & ~0x00000001);
          return this;
        }
        
        public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder getBlobBuilder() {
          bitField0_ |= 0x00000001;
          onChanged();
          return getBlobFieldBuilder().getBuilder();
        }
        
        public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getBlobOrBuilder() {
          if (blobBuilder_ != null) {
            return blobBuilder_.getMessageOrBuilder();
          } else {
            return blob_;
          }
        }
        
        private com.google.protobuf.SingleFieldBuilder<
            com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> 
            getBlobFieldBuilder() {
          if (blobBuilder_ == null) {
            blobBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder>(
                    blob_,
                    getParentForChildren(),
                    isClean());
            blob_ = null;
          }
          return blobBuilder_;
        }

        private boolean complete_ ;
        
        public boolean hasComplete() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        
        public boolean getComplete() {
          return complete_;
        }
        
        public Builder setComplete(boolean value) {
          bitField0_ |= 0x00000002;
          complete_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearComplete() {
          bitField0_ = (bitField0_ & ~0x00000002);
          complete_ = false;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new Contacts(true);
        defaultInstance.initFields();
      }

    }

    public interface GroupsOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasBlob();
      
      com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getBlob();
      
      com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getBlobOrBuilder();
    }
    
    public static final class Groups extends
        com.google.protobuf.GeneratedMessage
        implements GroupsOrBuilder {
      private Groups(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Groups(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Groups defaultInstance;
      public static Groups getDefaultInstance() {
        return defaultInstance;
      }

      public Groups getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Groups(
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
                com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder subBuilder = null;
                if (((bitField0_ & 0x00000001) == 0x00000001)) {
                  subBuilder = blob_.toBuilder();
                }
                blob_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.PARSER, extensionRegistry);
                if (subBuilder != null) {
                  subBuilder.mergeFrom(blob_);
                  blob_ = subBuilder.buildPartial();
                }
                bitField0_ |= 0x00000001;
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Groups_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Groups_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.Builder.class);
      }

      public static com.google.protobuf.Parser<Groups> PARSER =
          new com.google.protobuf.AbstractParser<Groups>() {
        public Groups parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Groups(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Groups> getParserForType() {
        return PARSER;
      }

      private int bitField0_;
      public static final int BLOB_FIELD_NUMBER = 1;
      private com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer blob_;
      
      public boolean hasBlob() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getBlob() {
        return blob_;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getBlobOrBuilder() {
        return blob_;
      }

      private void initFields() {
        blob_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
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
          output.writeMessage(1, blob_);
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
            .computeMessageSize(1, blob_);
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.GroupsOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Groups_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Groups_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.Builder.class);
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
            getBlobFieldBuilder();
          }
        }
        private static Builder create() {
          return new Builder();
        }

        public Builder clear() {
          super.clear();
          if (blobBuilder_ == null) {
            blob_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
          } else {
            blobBuilder_.clear();
          }
          bitField0_ = (bitField0_ & ~0x00000001);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Groups_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          if (blobBuilder_ == null) {
            result.blob_ = blob_;
          } else {
            result.blob_ = blobBuilder_.build();
          }
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.getDefaultInstance()) return this;
          if (other.hasBlob()) {
            mergeBlob(other.getBlob());
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer blob_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
        private com.google.protobuf.SingleFieldBuilder<
            com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> blobBuilder_;
        
        public boolean hasBlob() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }
        
        public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getBlob() {
          if (blobBuilder_ == null) {
            return blob_;
          } else {
            return blobBuilder_.getMessage();
          }
        }
        
        public Builder setBlob(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer value) {
          if (blobBuilder_ == null) {
            if (value == null) {
              throw new NullPointerException();
            }
            blob_ = value;
            onChanged();
          } else {
            blobBuilder_.setMessage(value);
          }
          bitField0_ |= 0x00000001;
          return this;
        }
        
        public Builder setBlob(
            com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder builderForValue) {
          if (blobBuilder_ == null) {
            blob_ = builderForValue.build();
            onChanged();
          } else {
            blobBuilder_.setMessage(builderForValue.build());
          }
          bitField0_ |= 0x00000001;
          return this;
        }
        
        public Builder mergeBlob(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer value) {
          if (blobBuilder_ == null) {
            if (((bitField0_ & 0x00000001) == 0x00000001) &&
                blob_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance()) {
              blob_ =
                com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.newBuilder(blob_).mergeFrom(value).buildPartial();
            } else {
              blob_ = value;
            }
            onChanged();
          } else {
            blobBuilder_.mergeFrom(value);
          }
          bitField0_ |= 0x00000001;
          return this;
        }
        
        public Builder clearBlob() {
          if (blobBuilder_ == null) {
            blob_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
            onChanged();
          } else {
            blobBuilder_.clear();
          }
          bitField0_ = (bitField0_ & ~0x00000001);
          return this;
        }
        
        public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder getBlobBuilder() {
          bitField0_ |= 0x00000001;
          onChanged();
          return getBlobFieldBuilder().getBuilder();
        }
        
        public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getBlobOrBuilder() {
          if (blobBuilder_ != null) {
            return blobBuilder_.getMessageOrBuilder();
          } else {
            return blob_;
          }
        }
        
        private com.google.protobuf.SingleFieldBuilder<
            com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> 
            getBlobFieldBuilder() {
          if (blobBuilder_ == null) {
            blobBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder>(
                    blob_,
                    getParentForChildren(),
                    isClean());
            blob_ = null;
          }
          return blobBuilder_;
        }

      }

      static {
        defaultInstance = new Groups(true);
        defaultInstance.initFields();
      }

    }

    public interface BlockedOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      java.util.List<java.lang.String>
      getNumbersList();
      
      int getNumbersCount();
      
      java.lang.String getNumbers(int index);
      
      com.google.protobuf.ByteString
          getNumbersBytes(int index);
    }
    
    public static final class Blocked extends
        com.google.protobuf.GeneratedMessage
        implements BlockedOrBuilder {
      private Blocked(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Blocked(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Blocked defaultInstance;
      public static Blocked getDefaultInstance() {
        return defaultInstance;
      }

      public Blocked getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Blocked(
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
                if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                  numbers_ = new com.google.protobuf.LazyStringArrayList();
                  mutable_bitField0_ |= 0x00000001;
                }
                numbers_.add(input.readBytes());
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
          if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
            numbers_ = new com.google.protobuf.UnmodifiableLazyStringList(numbers_);
          }
          this.unknownFields = unknownFields.build();
          makeExtensionsImmutable();
        }
      }
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Blocked_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Blocked_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.Builder.class);
      }

      public static com.google.protobuf.Parser<Blocked> PARSER =
          new com.google.protobuf.AbstractParser<Blocked>() {
        public Blocked parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Blocked(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Blocked> getParserForType() {
        return PARSER;
      }

      public static final int NUMBERS_FIELD_NUMBER = 1;
      private com.google.protobuf.LazyStringList numbers_;
      
      public java.util.List<java.lang.String>
          getNumbersList() {
        return numbers_;
      }
      
      public int getNumbersCount() {
        return numbers_.size();
      }
      
      public java.lang.String getNumbers(int index) {
        return numbers_.get(index);
      }
      
      public com.google.protobuf.ByteString
          getNumbersBytes(int index) {
        return numbers_.getByteString(index);
      }

      private void initFields() {
        numbers_ = com.google.protobuf.LazyStringArrayList.EMPTY;
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
        for (int i = 0; i < numbers_.size(); i++) {
          output.writeBytes(1, numbers_.getByteString(i));
        }
        getUnknownFields().writeTo(output);
      }

      private int memoizedSerializedSize = -1;
      public int getSerializedSize() {
        int size = memoizedSerializedSize;
        if (size != -1) return size;

        size = 0;
        {
          int dataSize = 0;
          for (int i = 0; i < numbers_.size(); i++) {
            dataSize += com.google.protobuf.CodedOutputStream
              .computeBytesSizeNoTag(numbers_.getByteString(i));
          }
          size += dataSize;
          size += 1 * getNumbersList().size();
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.BlockedOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Blocked_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Blocked_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.Builder.class);
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
          numbers_ = com.google.protobuf.LazyStringArrayList.EMPTY;
          bitField0_ = (bitField0_ & ~0x00000001);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Blocked_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked(this);
          int from_bitField0_ = bitField0_;
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            numbers_ = new com.google.protobuf.UnmodifiableLazyStringList(
                numbers_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.numbers_ = numbers_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.getDefaultInstance()) return this;
          if (!other.numbers_.isEmpty()) {
            if (numbers_.isEmpty()) {
              numbers_ = other.numbers_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureNumbersIsMutable();
              numbers_.addAll(other.numbers_);
            }
            onChanged();
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private com.google.protobuf.LazyStringList numbers_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        private void ensureNumbersIsMutable() {
          if (!((bitField0_ & 0x00000001) == 0x00000001)) {
            numbers_ = new com.google.protobuf.LazyStringArrayList(numbers_);
            bitField0_ |= 0x00000001;
           }
        }
        
        public java.util.List<java.lang.String>
            getNumbersList() {
          return java.util.Collections.unmodifiableList(numbers_);
        }
        
        public int getNumbersCount() {
          return numbers_.size();
        }
        
        public java.lang.String getNumbers(int index) {
          return numbers_.get(index);
        }
        
        public com.google.protobuf.ByteString
            getNumbersBytes(int index) {
          return numbers_.getByteString(index);
        }
        
        public Builder setNumbers(
            int index, java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  ensureNumbersIsMutable();
          numbers_.set(index, value);
          onChanged();
          return this;
        }
        
        public Builder addNumbers(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  ensureNumbersIsMutable();
          numbers_.add(value);
          onChanged();
          return this;
        }
        
        public Builder addAllNumbers(
            java.lang.Iterable<java.lang.String> values) {
          ensureNumbersIsMutable();
          super.addAll(values, numbers_);
          onChanged();
          return this;
        }
        
        public Builder clearNumbers() {
          numbers_ = com.google.protobuf.LazyStringArrayList.EMPTY;
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
          return this;
        }
        
        public Builder addNumbersBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  ensureNumbersIsMutable();
          numbers_.add(value);
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new Blocked(true);
        defaultInstance.initFields();
      }

    }

    public interface RequestOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasType();
      
      com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type getType();
    }
    
    public static final class Request extends
        com.google.protobuf.GeneratedMessage
        implements RequestOrBuilder {
      private Request(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Request(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Request defaultInstance;
      public static Request getDefaultInstance() {
        return defaultInstance;
      }

      public Request getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Request(
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
                com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type value = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type.valueOf(rawValue);
                if (value == null) {
                  unknownFields.mergeVarintField(1, rawValue);
                } else {
                  bitField0_ |= 0x00000001;
                  type_ = value;
                }
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Request_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Request_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Builder.class);
      }

      public static com.google.protobuf.Parser<Request> PARSER =
          new com.google.protobuf.AbstractParser<Request>() {
        public Request parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Request(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Request> getParserForType() {
        return PARSER;
      }

      
      public enum Type
          implements com.google.protobuf.ProtocolMessageEnum {
        
        UNKNOWN(0, 0),
        
        CONTACTS(1, 1),
        
        GROUPS(2, 2),
        
        BLOCKED(3, 3),
        
        CONFIGURATION(4, 4),
        ;

        
        public static final int UNKNOWN_VALUE = 0;
        
        public static final int CONTACTS_VALUE = 1;
        
        public static final int GROUPS_VALUE = 2;
        
        public static final int BLOCKED_VALUE = 3;
        
        public static final int CONFIGURATION_VALUE = 4;

        public final int getNumber() { return value; }

        public static Type valueOf(int value) {
          switch (value) {
            case 0: return UNKNOWN;
            case 1: return CONTACTS;
            case 2: return GROUPS;
            case 3: return BLOCKED;
            case 4: return CONFIGURATION;
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
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.getDescriptor().getEnumTypes().get(0);
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
      private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type type_;
      
      public boolean hasType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type getType() {
        return type_;
      }

      private void initFields() {
        type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type.UNKNOWN;
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.RequestOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Request_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Request_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Builder.class);
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
          type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type.UNKNOWN;
          bitField0_ = (bitField0_ & ~0x00000001);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Request_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.type_ = type_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.getDefaultInstance()) return this;
          if (other.hasType()) {
            setType(other.getType());
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type.UNKNOWN;
        
        public boolean hasType() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }
        
        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type getType() {
          return type_;
        }
        
        public Builder setType(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type value) {
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
          type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Type.UNKNOWN;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new Request(true);
        defaultInstance.initFields();
      }

    }

    public interface ReadOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasSender();
      
      java.lang.String getSender();
      
      com.google.protobuf.ByteString
          getSenderBytes();

      
      boolean hasTimestamp();
      
      long getTimestamp();
    }
    
    public static final class Read extends
        com.google.protobuf.GeneratedMessage
        implements ReadOrBuilder {
      private Read(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Read(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Read defaultInstance;
      public static Read getDefaultInstance() {
        return defaultInstance;
      }

      public Read getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Read(
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
                sender_ = input.readBytes();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Read_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Read_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder.class);
      }

      public static com.google.protobuf.Parser<Read> PARSER =
          new com.google.protobuf.AbstractParser<Read>() {
        public Read parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Read(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Read> getParserForType() {
        return PARSER;
      }

      private int bitField0_;
      public static final int SENDER_FIELD_NUMBER = 1;
      private java.lang.Object sender_;
      
      public boolean hasSender() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public java.lang.String getSender() {
        java.lang.Object ref = sender_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            sender_ = s;
          }
          return s;
        }
      }
      
      public com.google.protobuf.ByteString
          getSenderBytes() {
        java.lang.Object ref = sender_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          sender_ = b;
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
        sender_ = "";
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
          output.writeBytes(1, getSenderBytes());
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
            .computeBytesSize(1, getSenderBytes());
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ReadOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Read_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Read_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder.class);
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
          sender_ = "";
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
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Read_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.sender_ = sender_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.timestamp_ = timestamp_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.getDefaultInstance()) return this;
          if (other.hasSender()) {
            bitField0_ |= 0x00000001;
            sender_ = other.sender_;
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private java.lang.Object sender_ = "";
        
        public boolean hasSender() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }
        
        public java.lang.String getSender() {
          java.lang.Object ref = sender_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            sender_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }
        
        public com.google.protobuf.ByteString
            getSenderBytes() {
          java.lang.Object ref = sender_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            sender_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }
        
        public Builder setSender(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          sender_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearSender() {
          bitField0_ = (bitField0_ & ~0x00000001);
          sender_ = getDefaultInstance().getSender();
          onChanged();
          return this;
        }
        
        public Builder setSenderBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          sender_ = value;
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
        defaultInstance = new Read(true);
        defaultInstance.initFields();
      }

    }

    public interface ConfigurationOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasReadReceipts();
      
      boolean getReadReceipts();
    }
    
    public static final class Configuration extends
        com.google.protobuf.GeneratedMessage
        implements ConfigurationOrBuilder {
      private Configuration(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Configuration(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Configuration defaultInstance;
      public static Configuration getDefaultInstance() {
        return defaultInstance;
      }

      public Configuration getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Configuration(
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
                bitField0_ |= 0x00000001;
                readReceipts_ = input.readBool();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Configuration_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Configuration_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.Builder.class);
      }

      public static com.google.protobuf.Parser<Configuration> PARSER =
          new com.google.protobuf.AbstractParser<Configuration>() {
        public Configuration parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Configuration(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Configuration> getParserForType() {
        return PARSER;
      }

      private int bitField0_;
      public static final int READRECEIPTS_FIELD_NUMBER = 1;
      private boolean readReceipts_;
      
      public boolean hasReadReceipts() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public boolean getReadReceipts() {
        return readReceipts_;
      }

      private void initFields() {
        readReceipts_ = false;
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
          output.writeBool(1, readReceipts_);
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
            .computeBoolSize(1, readReceipts_);
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ConfigurationOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Configuration_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Configuration_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.Builder.class);
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
          readReceipts_ = false;
          bitField0_ = (bitField0_ & ~0x00000001);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_Configuration_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.readReceipts_ = readReceipts_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.getDefaultInstance()) return this;
          if (other.hasReadReceipts()) {
            setReadReceipts(other.getReadReceipts());
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private boolean readReceipts_ ;
        
        public boolean hasReadReceipts() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }
        
        public boolean getReadReceipts() {
          return readReceipts_;
        }
        
        public Builder setReadReceipts(boolean value) {
          bitField0_ |= 0x00000001;
          readReceipts_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearReadReceipts() {
          bitField0_ = (bitField0_ & ~0x00000001);
          readReceipts_ = false;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new Configuration(true);
        defaultInstance.initFields();
      }

    }

    private int bitField0_;
    public static final int SENT_FIELD_NUMBER = 1;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent sent_;
    
    public boolean hasSent() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent getSent() {
      return sent_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.SentOrBuilder getSentOrBuilder() {
      return sent_;
    }

    public static final int CONTACTS_FIELD_NUMBER = 2;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts contacts_;
    
    public boolean hasContacts() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts getContacts() {
      return contacts_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ContactsOrBuilder getContactsOrBuilder() {
      return contacts_;
    }

    public static final int GROUPS_FIELD_NUMBER = 3;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups groups_;
    
    public boolean hasGroups() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups getGroups() {
      return groups_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.GroupsOrBuilder getGroupsOrBuilder() {
      return groups_;
    }

    public static final int REQUEST_FIELD_NUMBER = 4;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request request_;
    
    public boolean hasRequest() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request getRequest() {
      return request_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.RequestOrBuilder getRequestOrBuilder() {
      return request_;
    }

    public static final int READ_FIELD_NUMBER = 5;
    private java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read> read_;
    
    public java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read> getReadList() {
      return read_;
    }
    
    public java.util.List<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ReadOrBuilder> 
        getReadOrBuilderList() {
      return read_;
    }
    
    public int getReadCount() {
      return read_.size();
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read getRead(int index) {
      return read_.get(index);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ReadOrBuilder getReadOrBuilder(
        int index) {
      return read_.get(index);
    }

    public static final int BLOCKED_FIELD_NUMBER = 6;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked blocked_;
    
    public boolean hasBlocked() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked getBlocked() {
      return blocked_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.BlockedOrBuilder getBlockedOrBuilder() {
      return blocked_;
    }

    public static final int VERIFIED_FIELD_NUMBER = 7;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified verified_;
    
    public boolean hasVerified() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified getVerified() {
      return verified_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder getVerifiedOrBuilder() {
      return verified_;
    }

    public static final int CONFIGURATION_FIELD_NUMBER = 9;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration configuration_;
    
    public boolean hasConfiguration() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration getConfiguration() {
      return configuration_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ConfigurationOrBuilder getConfigurationOrBuilder() {
      return configuration_;
    }

    public static final int PADDING_FIELD_NUMBER = 8;
    private com.google.protobuf.ByteString padding_;
    
    public boolean hasPadding() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    
    public com.google.protobuf.ByteString getPadding() {
      return padding_;
    }

    private void initFields() {
      sent_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.getDefaultInstance();
      contacts_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.getDefaultInstance();
      groups_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.getDefaultInstance();
      request_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.getDefaultInstance();
      read_ = java.util.Collections.emptyList();
      blocked_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.getDefaultInstance();
      verified_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance();
      configuration_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.getDefaultInstance();
      padding_ = com.google.protobuf.ByteString.EMPTY;
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
        output.writeMessage(1, sent_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, contacts_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(3, groups_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(4, request_);
      }
      for (int i = 0; i < read_.size(); i++) {
        output.writeMessage(5, read_.get(i));
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeMessage(6, blocked_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeMessage(7, verified_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeBytes(8, padding_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeMessage(9, configuration_);
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
          .computeMessageSize(1, sent_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, contacts_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, groups_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, request_);
      }
      for (int i = 0; i < read_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, read_.get(i));
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, blocked_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(7, verified_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(8, padding_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(9, configuration_);
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Builder.class);
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
          getSentFieldBuilder();
          getContactsFieldBuilder();
          getGroupsFieldBuilder();
          getRequestFieldBuilder();
          getReadFieldBuilder();
          getBlockedFieldBuilder();
          getVerifiedFieldBuilder();
          getConfigurationFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (sentBuilder_ == null) {
          sent_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.getDefaultInstance();
        } else {
          sentBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (contactsBuilder_ == null) {
          contacts_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.getDefaultInstance();
        } else {
          contactsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        if (groupsBuilder_ == null) {
          groups_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.getDefaultInstance();
        } else {
          groupsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        if (requestBuilder_ == null) {
          request_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.getDefaultInstance();
        } else {
          requestBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        if (readBuilder_ == null) {
          read_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000010);
        } else {
          readBuilder_.clear();
        }
        if (blockedBuilder_ == null) {
          blocked_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.getDefaultInstance();
        } else {
          blockedBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000020);
        if (verifiedBuilder_ == null) {
          verified_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance();
        } else {
          verifiedBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000040);
        if (configurationBuilder_ == null) {
          configuration_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.getDefaultInstance();
        } else {
          configurationBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000080);
        padding_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000100);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_SyncMessage_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (sentBuilder_ == null) {
          result.sent_ = sent_;
        } else {
          result.sent_ = sentBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (contactsBuilder_ == null) {
          result.contacts_ = contacts_;
        } else {
          result.contacts_ = contactsBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (groupsBuilder_ == null) {
          result.groups_ = groups_;
        } else {
          result.groups_ = groupsBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (requestBuilder_ == null) {
          result.request_ = request_;
        } else {
          result.request_ = requestBuilder_.build();
        }
        if (readBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010)) {
            read_ = java.util.Collections.unmodifiableList(read_);
            bitField0_ = (bitField0_ & ~0x00000010);
          }
          result.read_ = read_;
        } else {
          result.read_ = readBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000010;
        }
        if (blockedBuilder_ == null) {
          result.blocked_ = blocked_;
        } else {
          result.blocked_ = blockedBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000020;
        }
        if (verifiedBuilder_ == null) {
          result.verified_ = verified_;
        } else {
          result.verified_ = verifiedBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000040;
        }
        if (configurationBuilder_ == null) {
          result.configuration_ = configuration_;
        } else {
          result.configuration_ = configurationBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000080;
        }
        result.padding_ = padding_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.getDefaultInstance()) return this;
        if (other.hasSent()) {
          mergeSent(other.getSent());
        }
        if (other.hasContacts()) {
          mergeContacts(other.getContacts());
        }
        if (other.hasGroups()) {
          mergeGroups(other.getGroups());
        }
        if (other.hasRequest()) {
          mergeRequest(other.getRequest());
        }
        if (readBuilder_ == null) {
          if (!other.read_.isEmpty()) {
            if (read_.isEmpty()) {
              read_ = other.read_;
              bitField0_ = (bitField0_ & ~0x00000010);
            } else {
              ensureReadIsMutable();
              read_.addAll(other.read_);
            }
            onChanged();
          }
        } else {
          if (!other.read_.isEmpty()) {
            if (readBuilder_.isEmpty()) {
              readBuilder_.dispose();
              readBuilder_ = null;
              read_ = other.read_;
              bitField0_ = (bitField0_ & ~0x00000010);
              readBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getReadFieldBuilder() : null;
            } else {
              readBuilder_.addAllMessages(other.read_);
            }
          }
        }
        if (other.hasBlocked()) {
          mergeBlocked(other.getBlocked());
        }
        if (other.hasVerified()) {
          mergeVerified(other.getVerified());
        }
        if (other.hasConfiguration()) {
          mergeConfiguration(other.getConfiguration());
        }
        if (other.hasPadding()) {
          setPadding(other.getPadding());
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent sent_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.SentOrBuilder> sentBuilder_;
      
      public boolean hasSent() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent getSent() {
        if (sentBuilder_ == null) {
          return sent_;
        } else {
          return sentBuilder_.getMessage();
        }
      }
      
      public Builder setSent(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent value) {
        if (sentBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          sent_ = value;
          onChanged();
        } else {
          sentBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      
      public Builder setSent(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.Builder builderForValue) {
        if (sentBuilder_ == null) {
          sent_ = builderForValue.build();
          onChanged();
        } else {
          sentBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      
      public Builder mergeSent(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent value) {
        if (sentBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              sent_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.getDefaultInstance()) {
            sent_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.newBuilder(sent_).mergeFrom(value).buildPartial();
          } else {
            sent_ = value;
          }
          onChanged();
        } else {
          sentBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      
      public Builder clearSent() {
        if (sentBuilder_ == null) {
          sent_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.getDefaultInstance();
          onChanged();
        } else {
          sentBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.Builder getSentBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getSentFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.SentOrBuilder getSentOrBuilder() {
        if (sentBuilder_ != null) {
          return sentBuilder_.getMessageOrBuilder();
        } else {
          return sent_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.SentOrBuilder> 
          getSentFieldBuilder() {
        if (sentBuilder_ == null) {
          sentBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Sent.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.SentOrBuilder>(
                  sent_,
                  getParentForChildren(),
                  isClean());
          sent_ = null;
        }
        return sentBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts contacts_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ContactsOrBuilder> contactsBuilder_;
      
      public boolean hasContacts() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts getContacts() {
        if (contactsBuilder_ == null) {
          return contacts_;
        } else {
          return contactsBuilder_.getMessage();
        }
      }
      
      public Builder setContacts(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts value) {
        if (contactsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          contacts_ = value;
          onChanged();
        } else {
          contactsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      
      public Builder setContacts(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.Builder builderForValue) {
        if (contactsBuilder_ == null) {
          contacts_ = builderForValue.build();
          onChanged();
        } else {
          contactsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      
      public Builder mergeContacts(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts value) {
        if (contactsBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              contacts_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.getDefaultInstance()) {
            contacts_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.newBuilder(contacts_).mergeFrom(value).buildPartial();
          } else {
            contacts_ = value;
          }
          onChanged();
        } else {
          contactsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      
      public Builder clearContacts() {
        if (contactsBuilder_ == null) {
          contacts_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.getDefaultInstance();
          onChanged();
        } else {
          contactsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.Builder getContactsBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getContactsFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ContactsOrBuilder getContactsOrBuilder() {
        if (contactsBuilder_ != null) {
          return contactsBuilder_.getMessageOrBuilder();
        } else {
          return contacts_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ContactsOrBuilder> 
          getContactsFieldBuilder() {
        if (contactsBuilder_ == null) {
          contactsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Contacts.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ContactsOrBuilder>(
                  contacts_,
                  getParentForChildren(),
                  isClean());
          contacts_ = null;
        }
        return contactsBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups groups_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.GroupsOrBuilder> groupsBuilder_;
      
      public boolean hasGroups() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups getGroups() {
        if (groupsBuilder_ == null) {
          return groups_;
        } else {
          return groupsBuilder_.getMessage();
        }
      }
      
      public Builder setGroups(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups value) {
        if (groupsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          groups_ = value;
          onChanged();
        } else {
          groupsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder setGroups(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.Builder builderForValue) {
        if (groupsBuilder_ == null) {
          groups_ = builderForValue.build();
          onChanged();
        } else {
          groupsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder mergeGroups(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups value) {
        if (groupsBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              groups_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.getDefaultInstance()) {
            groups_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.newBuilder(groups_).mergeFrom(value).buildPartial();
          } else {
            groups_ = value;
          }
          onChanged();
        } else {
          groupsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder clearGroups() {
        if (groupsBuilder_ == null) {
          groups_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.getDefaultInstance();
          onChanged();
        } else {
          groupsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.Builder getGroupsBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getGroupsFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.GroupsOrBuilder getGroupsOrBuilder() {
        if (groupsBuilder_ != null) {
          return groupsBuilder_.getMessageOrBuilder();
        } else {
          return groups_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.GroupsOrBuilder> 
          getGroupsFieldBuilder() {
        if (groupsBuilder_ == null) {
          groupsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Groups.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.GroupsOrBuilder>(
                  groups_,
                  getParentForChildren(),
                  isClean());
          groups_ = null;
        }
        return groupsBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request request_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.RequestOrBuilder> requestBuilder_;
      
      public boolean hasRequest() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request getRequest() {
        if (requestBuilder_ == null) {
          return request_;
        } else {
          return requestBuilder_.getMessage();
        }
      }
      
      public Builder setRequest(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request value) {
        if (requestBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          request_ = value;
          onChanged();
        } else {
          requestBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder setRequest(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Builder builderForValue) {
        if (requestBuilder_ == null) {
          request_ = builderForValue.build();
          onChanged();
        } else {
          requestBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder mergeRequest(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request value) {
        if (requestBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              request_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.getDefaultInstance()) {
            request_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.newBuilder(request_).mergeFrom(value).buildPartial();
          } else {
            request_ = value;
          }
          onChanged();
        } else {
          requestBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder clearRequest() {
        if (requestBuilder_ == null) {
          request_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.getDefaultInstance();
          onChanged();
        } else {
          requestBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Builder getRequestBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getRequestFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.RequestOrBuilder getRequestOrBuilder() {
        if (requestBuilder_ != null) {
          return requestBuilder_.getMessageOrBuilder();
        } else {
          return request_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.RequestOrBuilder> 
          getRequestFieldBuilder() {
        if (requestBuilder_ == null) {
          requestBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.RequestOrBuilder>(
                  request_,
                  getParentForChildren(),
                  isClean());
          request_ = null;
        }
        return requestBuilder_;
      }

      private java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read> read_ =
        java.util.Collections.emptyList();
      private void ensureReadIsMutable() {
        if (!((bitField0_ & 0x00000010) == 0x00000010)) {
          read_ = new java.util.ArrayList<com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read>(read_);
          bitField0_ |= 0x00000010;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ReadOrBuilder> readBuilder_;

      
      public java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read> getReadList() {
        if (readBuilder_ == null) {
          return java.util.Collections.unmodifiableList(read_);
        } else {
          return readBuilder_.getMessageList();
        }
      }
      
      public int getReadCount() {
        if (readBuilder_ == null) {
          return read_.size();
        } else {
          return readBuilder_.getCount();
        }
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read getRead(int index) {
        if (readBuilder_ == null) {
          return read_.get(index);
        } else {
          return readBuilder_.getMessage(index);
        }
      }
      
      public Builder setRead(
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read value) {
        if (readBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureReadIsMutable();
          read_.set(index, value);
          onChanged();
        } else {
          readBuilder_.setMessage(index, value);
        }
        return this;
      }
      
      public Builder setRead(
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder builderForValue) {
        if (readBuilder_ == null) {
          ensureReadIsMutable();
          read_.set(index, builderForValue.build());
          onChanged();
        } else {
          readBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      
      public Builder addRead(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read value) {
        if (readBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureReadIsMutable();
          read_.add(value);
          onChanged();
        } else {
          readBuilder_.addMessage(value);
        }
        return this;
      }
      
      public Builder addRead(
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read value) {
        if (readBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureReadIsMutable();
          read_.add(index, value);
          onChanged();
        } else {
          readBuilder_.addMessage(index, value);
        }
        return this;
      }
      
      public Builder addRead(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder builderForValue) {
        if (readBuilder_ == null) {
          ensureReadIsMutable();
          read_.add(builderForValue.build());
          onChanged();
        } else {
          readBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      
      public Builder addRead(
          int index, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder builderForValue) {
        if (readBuilder_ == null) {
          ensureReadIsMutable();
          read_.add(index, builderForValue.build());
          onChanged();
        } else {
          readBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      
      public Builder addAllRead(
          java.lang.Iterable<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read> values) {
        if (readBuilder_ == null) {
          ensureReadIsMutable();
          super.addAll(values, read_);
          onChanged();
        } else {
          readBuilder_.addAllMessages(values);
        }
        return this;
      }
      
      public Builder clearRead() {
        if (readBuilder_ == null) {
          read_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000010);
          onChanged();
        } else {
          readBuilder_.clear();
        }
        return this;
      }
      
      public Builder removeRead(int index) {
        if (readBuilder_ == null) {
          ensureReadIsMutable();
          read_.remove(index);
          onChanged();
        } else {
          readBuilder_.remove(index);
        }
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder getReadBuilder(
          int index) {
        return getReadFieldBuilder().getBuilder(index);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ReadOrBuilder getReadOrBuilder(
          int index) {
        if (readBuilder_ == null) {
          return read_.get(index);  } else {
          return readBuilder_.getMessageOrBuilder(index);
        }
      }
      
      public java.util.List<? extends com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ReadOrBuilder> 
           getReadOrBuilderList() {
        if (readBuilder_ != null) {
          return readBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(read_);
        }
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder addReadBuilder() {
        return getReadFieldBuilder().addBuilder(
            com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.getDefaultInstance());
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder addReadBuilder(
          int index) {
        return getReadFieldBuilder().addBuilder(
            index, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.getDefaultInstance());
      }
      
      public java.util.List<com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder> 
           getReadBuilderList() {
        return getReadFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ReadOrBuilder> 
          getReadFieldBuilder() {
        if (readBuilder_ == null) {
          readBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Read.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ReadOrBuilder>(
                  read_,
                  ((bitField0_ & 0x00000010) == 0x00000010),
                  getParentForChildren(),
                  isClean());
          read_ = null;
        }
        return readBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked blocked_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.BlockedOrBuilder> blockedBuilder_;
      
      public boolean hasBlocked() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked getBlocked() {
        if (blockedBuilder_ == null) {
          return blocked_;
        } else {
          return blockedBuilder_.getMessage();
        }
      }
      
      public Builder setBlocked(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked value) {
        if (blockedBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          blocked_ = value;
          onChanged();
        } else {
          blockedBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      
      public Builder setBlocked(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.Builder builderForValue) {
        if (blockedBuilder_ == null) {
          blocked_ = builderForValue.build();
          onChanged();
        } else {
          blockedBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      
      public Builder mergeBlocked(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked value) {
        if (blockedBuilder_ == null) {
          if (((bitField0_ & 0x00000020) == 0x00000020) &&
              blocked_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.getDefaultInstance()) {
            blocked_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.newBuilder(blocked_).mergeFrom(value).buildPartial();
          } else {
            blocked_ = value;
          }
          onChanged();
        } else {
          blockedBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      
      public Builder clearBlocked() {
        if (blockedBuilder_ == null) {
          blocked_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.getDefaultInstance();
          onChanged();
        } else {
          blockedBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.Builder getBlockedBuilder() {
        bitField0_ |= 0x00000020;
        onChanged();
        return getBlockedFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.BlockedOrBuilder getBlockedOrBuilder() {
        if (blockedBuilder_ != null) {
          return blockedBuilder_.getMessageOrBuilder();
        } else {
          return blocked_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.BlockedOrBuilder> 
          getBlockedFieldBuilder() {
        if (blockedBuilder_ == null) {
          blockedBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Blocked.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.BlockedOrBuilder>(
                  blocked_,
                  getParentForChildren(),
                  isClean());
          blocked_ = null;
        }
        return blockedBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified verified_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified, com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder> verifiedBuilder_;
      
      public boolean hasVerified() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified getVerified() {
        if (verifiedBuilder_ == null) {
          return verified_;
        } else {
          return verifiedBuilder_.getMessage();
        }
      }
      
      public Builder setVerified(com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified value) {
        if (verifiedBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          verified_ = value;
          onChanged();
        } else {
          verifiedBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000040;
        return this;
      }
      
      public Builder setVerified(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder builderForValue) {
        if (verifiedBuilder_ == null) {
          verified_ = builderForValue.build();
          onChanged();
        } else {
          verifiedBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000040;
        return this;
      }
      
      public Builder mergeVerified(com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified value) {
        if (verifiedBuilder_ == null) {
          if (((bitField0_ & 0x00000040) == 0x00000040) &&
              verified_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance()) {
            verified_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.newBuilder(verified_).mergeFrom(value).buildPartial();
          } else {
            verified_ = value;
          }
          onChanged();
        } else {
          verifiedBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000040;
        return this;
      }
      
      public Builder clearVerified() {
        if (verifiedBuilder_ == null) {
          verified_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance();
          onChanged();
        } else {
          verifiedBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder getVerifiedBuilder() {
        bitField0_ |= 0x00000040;
        onChanged();
        return getVerifiedFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder getVerifiedOrBuilder() {
        if (verifiedBuilder_ != null) {
          return verifiedBuilder_.getMessageOrBuilder();
        } else {
          return verified_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified, com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder> 
          getVerifiedFieldBuilder() {
        if (verifiedBuilder_ == null) {
          verifiedBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified, com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder>(
                  verified_,
                  getParentForChildren(),
                  isClean());
          verified_ = null;
        }
        return verifiedBuilder_;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration configuration_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ConfigurationOrBuilder> configurationBuilder_;
      
      public boolean hasConfiguration() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration getConfiguration() {
        if (configurationBuilder_ == null) {
          return configuration_;
        } else {
          return configurationBuilder_.getMessage();
        }
      }
      
      public Builder setConfiguration(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration value) {
        if (configurationBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          configuration_ = value;
          onChanged();
        } else {
          configurationBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000080;
        return this;
      }
      
      public Builder setConfiguration(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.Builder builderForValue) {
        if (configurationBuilder_ == null) {
          configuration_ = builderForValue.build();
          onChanged();
        } else {
          configurationBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000080;
        return this;
      }
      
      public Builder mergeConfiguration(com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration value) {
        if (configurationBuilder_ == null) {
          if (((bitField0_ & 0x00000080) == 0x00000080) &&
              configuration_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.getDefaultInstance()) {
            configuration_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.newBuilder(configuration_).mergeFrom(value).buildPartial();
          } else {
            configuration_ = value;
          }
          onChanged();
        } else {
          configurationBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000080;
        return this;
      }
      
      public Builder clearConfiguration() {
        if (configurationBuilder_ == null) {
          configuration_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.getDefaultInstance();
          onChanged();
        } else {
          configurationBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000080);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.Builder getConfigurationBuilder() {
        bitField0_ |= 0x00000080;
        onChanged();
        return getConfigurationFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ConfigurationOrBuilder getConfigurationOrBuilder() {
        if (configurationBuilder_ != null) {
          return configurationBuilder_.getMessageOrBuilder();
        } else {
          return configuration_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ConfigurationOrBuilder> 
          getConfigurationFieldBuilder() {
        if (configurationBuilder_ == null) {
          configurationBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Configuration.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.ConfigurationOrBuilder>(
                  configuration_,
                  getParentForChildren(),
                  isClean());
          configuration_ = null;
        }
        return configurationBuilder_;
      }

      private com.google.protobuf.ByteString padding_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasPadding() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }
      
      public com.google.protobuf.ByteString getPadding() {
        return padding_;
      }
      
      public Builder setPadding(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000100;
        padding_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearPadding() {
        bitField0_ = (bitField0_ & ~0x00000100);
        padding_ = getDefaultInstance().getPadding();
        onChanged();
        return this;
      }

    }

    static {
      defaultInstance = new SyncMessage(true);
      defaultInstance.initFields();
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

    
    boolean hasSize();
    
    int getSize();

    
    boolean hasThumbnail();
    
    com.google.protobuf.ByteString getThumbnail();

    
    boolean hasDigest();
    
    com.google.protobuf.ByteString getDigest();

    
    boolean hasFileName();
    
    java.lang.String getFileName();
    
    com.google.protobuf.ByteString
        getFileNameBytes();

    
    boolean hasFlags();
    
    int getFlags();
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
            case 32: {
              bitField0_ |= 0x00000008;
              size_ = input.readUInt32();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              thumbnail_ = input.readBytes();
              break;
            }
            case 50: {
              bitField0_ |= 0x00000020;
              digest_ = input.readBytes();
              break;
            }
            case 58: {
              bitField0_ |= 0x00000040;
              fileName_ = input.readBytes();
              break;
            }
            case 64: {
              bitField0_ |= 0x00000080;
              flags_ = input.readUInt32();
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
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_AttachmentPointer_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_AttachmentPointer_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder.class);
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

    
    public enum Flags
        implements com.google.protobuf.ProtocolMessageEnum {
      
      VOICE_MESSAGE(0, 1),
      ;

      
      public static final int VOICE_MESSAGE_VALUE = 1;

      public final int getNumber() { return value; }

      public static Flags valueOf(int value) {
        switch (value) {
          case 1: return VOICE_MESSAGE;
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDescriptor().getEnumTypes().get(0);
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

    public static final int SIZE_FIELD_NUMBER = 4;
    private int size_;
    
    public boolean hasSize() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public int getSize() {
      return size_;
    }

    public static final int THUMBNAIL_FIELD_NUMBER = 5;
    private com.google.protobuf.ByteString thumbnail_;
    
    public boolean hasThumbnail() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    
    public com.google.protobuf.ByteString getThumbnail() {
      return thumbnail_;
    }

    public static final int DIGEST_FIELD_NUMBER = 6;
    private com.google.protobuf.ByteString digest_;
    
    public boolean hasDigest() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    
    public com.google.protobuf.ByteString getDigest() {
      return digest_;
    }

    public static final int FILENAME_FIELD_NUMBER = 7;
    private java.lang.Object fileName_;
    
    public boolean hasFileName() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    
    public java.lang.String getFileName() {
      java.lang.Object ref = fileName_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          fileName_ = s;
        }
        return s;
      }
    }
    
    public com.google.protobuf.ByteString
        getFileNameBytes() {
      java.lang.Object ref = fileName_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        fileName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int FLAGS_FIELD_NUMBER = 8;
    private int flags_;
    
    public boolean hasFlags() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    
    public int getFlags() {
      return flags_;
    }

    private void initFields() {
      id_ = 0L;
      contentType_ = "";
      key_ = com.google.protobuf.ByteString.EMPTY;
      size_ = 0;
      thumbnail_ = com.google.protobuf.ByteString.EMPTY;
      digest_ = com.google.protobuf.ByteString.EMPTY;
      fileName_ = "";
      flags_ = 0;
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
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeUInt32(4, size_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, thumbnail_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(6, digest_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBytes(7, getFileNameBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeUInt32(8, flags_);
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
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(4, size_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, thumbnail_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, digest_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, getFileNameBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(8, flags_);
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_AttachmentPointer_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_AttachmentPointer_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder.class);
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
        size_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        thumbnail_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000010);
        digest_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000020);
        fileName_ = "";
        bitField0_ = (bitField0_ & ~0x00000040);
        flags_ = 0;
        bitField0_ = (bitField0_ & ~0x00000080);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_AttachmentPointer_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer(this);
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
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.size_ = size_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.thumbnail_ = thumbnail_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.digest_ = digest_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.fileName_ = fileName_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.flags_ = flags_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance()) return this;
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
        if (other.hasSize()) {
          setSize(other.getSize());
        }
        if (other.hasThumbnail()) {
          setThumbnail(other.getThumbnail());
        }
        if (other.hasDigest()) {
          setDigest(other.getDigest());
        }
        if (other.hasFileName()) {
          bitField0_ |= 0x00000040;
          fileName_ = other.fileName_;
          onChanged();
        }
        if (other.hasFlags()) {
          setFlags(other.getFlags());
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer) e.getUnfinishedMessage();
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

      private int size_ ;
      
      public boolean hasSize() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public int getSize() {
        return size_;
      }
      
      public Builder setSize(int value) {
        bitField0_ |= 0x00000008;
        size_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearSize() {
        bitField0_ = (bitField0_ & ~0x00000008);
        size_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString thumbnail_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasThumbnail() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      
      public com.google.protobuf.ByteString getThumbnail() {
        return thumbnail_;
      }
      
      public Builder setThumbnail(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        thumbnail_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearThumbnail() {
        bitField0_ = (bitField0_ & ~0x00000010);
        thumbnail_ = getDefaultInstance().getThumbnail();
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString digest_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasDigest() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      
      public com.google.protobuf.ByteString getDigest() {
        return digest_;
      }
      
      public Builder setDigest(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        digest_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearDigest() {
        bitField0_ = (bitField0_ & ~0x00000020);
        digest_ = getDefaultInstance().getDigest();
        onChanged();
        return this;
      }

      private java.lang.Object fileName_ = "";
      
      public boolean hasFileName() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      
      public java.lang.String getFileName() {
        java.lang.Object ref = fileName_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          fileName_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      
      public com.google.protobuf.ByteString
          getFileNameBytes() {
        java.lang.Object ref = fileName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          fileName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      
      public Builder setFileName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        fileName_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearFileName() {
        bitField0_ = (bitField0_ & ~0x00000040);
        fileName_ = getDefaultInstance().getFileName();
        onChanged();
        return this;
      }
      
      public Builder setFileNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        fileName_ = value;
        onChanged();
        return this;
      }

      private int flags_ ;
      
      public boolean hasFlags() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      
      public int getFlags() {
        return flags_;
      }
      
      public Builder setFlags(int value) {
        bitField0_ |= 0x00000080;
        flags_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearFlags() {
        bitField0_ = (bitField0_ & ~0x00000080);
        flags_ = 0;
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
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type getType();

    
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
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getAvatar();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getAvatarOrBuilder();
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
              com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type value = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type.valueOf(rawValue);
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
              com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = avatar_.toBuilder();
              }
              avatar_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.PARSER, extensionRegistry);
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
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupContext_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupContext_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Builder.class);
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
      
      REQUEST_INFO(4, 4),
      ;

      
      public static final int UNKNOWN_VALUE = 0;
      
      public static final int UPDATE_VALUE = 1;
      
      public static final int DELIVER_VALUE = 2;
      
      public static final int QUIT_VALUE = 3;
      
      public static final int REQUEST_INFO_VALUE = 4;

      public final int getNumber() { return value; }

      public static Type valueOf(int value) {
        switch (value) {
          case 0: return UNKNOWN;
          case 1: return UPDATE;
          case 2: return DELIVER;
          case 3: return QUIT;
          case 4: return REQUEST_INFO;
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.getDescriptor().getEnumTypes().get(0);
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
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type type_;
    
    public boolean hasType() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type getType() {
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
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer avatar_;
    
    public boolean hasAvatar() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getAvatar() {
      return avatar_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getAvatarOrBuilder() {
      return avatar_;
    }

    private void initFields() {
      id_ = com.google.protobuf.ByteString.EMPTY;
      type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type.UNKNOWN;
      name_ = "";
      members_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContextOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupContext_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupContext_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Builder.class);
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
        type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type.UNKNOWN;
        bitField0_ = (bitField0_ & ~0x00000002);
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        members_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000008);
        if (avatarBuilder_ == null) {
          avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupContext_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext(this);
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
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.getDefaultInstance()) return this;
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext) e.getUnfinishedMessage();
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

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type.UNKNOWN;
      
      public boolean hasType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type getType() {
        return type_;
      }
      
      public Builder setType(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type value) {
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
        type_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type.UNKNOWN;
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

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> avatarBuilder_;
      
      public boolean hasAvatar() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer getAvatar() {
        if (avatarBuilder_ == null) {
          return avatar_;
        } else {
          return avatarBuilder_.getMessage();
        }
      }
      
      public Builder setAvatar(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer value) {
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder builderForValue) {
        if (avatarBuilder_ == null) {
          avatar_ = builderForValue.build();
          onChanged();
        } else {
          avatarBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder mergeAvatar(com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer value) {
        if (avatarBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              avatar_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance()) {
            avatar_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.newBuilder(avatar_).mergeFrom(value).buildPartial();
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
          avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.getDefaultInstance();
          onChanged();
        } else {
          avatarBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder getAvatarBuilder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getAvatarFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder getAvatarOrBuilder() {
        if (avatarBuilder_ != null) {
          return avatarBuilder_.getMessageOrBuilder();
        } else {
          return avatar_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder> 
          getAvatarFieldBuilder() {
        if (avatarBuilder_ == null) {
          avatarBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointerOrBuilder>(
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

  public interface ContactDetailsOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasNumber();
    
    java.lang.String getNumber();
    
    com.google.protobuf.ByteString
        getNumberBytes();

    
    boolean hasName();
    
    java.lang.String getName();
    
    com.google.protobuf.ByteString
        getNameBytes();

    
    boolean hasAvatar();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar getAvatar();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.AvatarOrBuilder getAvatarOrBuilder();

    
    boolean hasColor();
    
    java.lang.String getColor();
    
    com.google.protobuf.ByteString
        getColorBytes();

    
    boolean hasVerified();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified getVerified();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder getVerifiedOrBuilder();

    
    boolean hasProfileKey();
    
    com.google.protobuf.ByteString getProfileKey();
  }
  
  public static final class ContactDetails extends
      com.google.protobuf.GeneratedMessage
      implements ContactDetailsOrBuilder {
    private ContactDetails(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private ContactDetails(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final ContactDetails defaultInstance;
    public static ContactDetails getDefaultInstance() {
      return defaultInstance;
    }

    public ContactDetails getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private ContactDetails(
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
              number_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              name_ = input.readBytes();
              break;
            }
            case 26: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = avatar_.toBuilder();
              }
              avatar_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(avatar_);
                avatar_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              color_ = input.readBytes();
              break;
            }
            case 42: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder subBuilder = null;
              if (((bitField0_ & 0x00000010) == 0x00000010)) {
                subBuilder = verified_.toBuilder();
              }
              verified_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(verified_);
                verified_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000010;
              break;
            }
            case 50: {
              bitField0_ |= 0x00000020;
              profileKey_ = input.readBytes();
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
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ContactDetails_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ContactDetails_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Builder.class);
    }

    public static com.google.protobuf.Parser<ContactDetails> PARSER =
        new com.google.protobuf.AbstractParser<ContactDetails>() {
      public ContactDetails parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ContactDetails(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<ContactDetails> getParserForType() {
      return PARSER;
    }

    public interface AvatarOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasContentType();
      
      java.lang.String getContentType();
      
      com.google.protobuf.ByteString
          getContentTypeBytes();

      
      boolean hasLength();
      
      int getLength();
    }
    
    public static final class Avatar extends
        com.google.protobuf.GeneratedMessage
        implements AvatarOrBuilder {
      private Avatar(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Avatar(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Avatar defaultInstance;
      public static Avatar getDefaultInstance() {
        return defaultInstance;
      }

      public Avatar getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Avatar(
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
                contentType_ = input.readBytes();
                break;
              }
              case 16: {
                bitField0_ |= 0x00000002;
                length_ = input.readUInt32();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ContactDetails_Avatar_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ContactDetails_Avatar_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.Builder.class);
      }

      public static com.google.protobuf.Parser<Avatar> PARSER =
          new com.google.protobuf.AbstractParser<Avatar>() {
        public Avatar parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Avatar(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Avatar> getParserForType() {
        return PARSER;
      }

      private int bitField0_;
      public static final int CONTENTTYPE_FIELD_NUMBER = 1;
      private java.lang.Object contentType_;
      
      public boolean hasContentType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
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

      public static final int LENGTH_FIELD_NUMBER = 2;
      private int length_;
      
      public boolean hasLength() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public int getLength() {
        return length_;
      }

      private void initFields() {
        contentType_ = "";
        length_ = 0;
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
          output.writeBytes(1, getContentTypeBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeUInt32(2, length_);
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
            .computeBytesSize(1, getContentTypeBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeUInt32Size(2, length_);
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.AvatarOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ContactDetails_Avatar_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ContactDetails_Avatar_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.Builder.class);
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
          contentType_ = "";
          bitField0_ = (bitField0_ & ~0x00000001);
          length_ = 0;
          bitField0_ = (bitField0_ & ~0x00000002);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ContactDetails_Avatar_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.contentType_ = contentType_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.length_ = length_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.getDefaultInstance()) return this;
          if (other.hasContentType()) {
            bitField0_ |= 0x00000001;
            contentType_ = other.contentType_;
            onChanged();
          }
          if (other.hasLength()) {
            setLength(other.getLength());
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private java.lang.Object contentType_ = "";
        
        public boolean hasContentType() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
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
  bitField0_ |= 0x00000001;
          contentType_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearContentType() {
          bitField0_ = (bitField0_ & ~0x00000001);
          contentType_ = getDefaultInstance().getContentType();
          onChanged();
          return this;
        }
        
        public Builder setContentTypeBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          contentType_ = value;
          onChanged();
          return this;
        }

        private int length_ ;
        
        public boolean hasLength() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        
        public int getLength() {
          return length_;
        }
        
        public Builder setLength(int value) {
          bitField0_ |= 0x00000002;
          length_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearLength() {
          bitField0_ = (bitField0_ & ~0x00000002);
          length_ = 0;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new Avatar(true);
        defaultInstance.initFields();
      }

    }

    private int bitField0_;
    public static final int NUMBER_FIELD_NUMBER = 1;
    private java.lang.Object number_;
    
    public boolean hasNumber() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    
    public java.lang.String getNumber() {
      java.lang.Object ref = number_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          number_ = s;
        }
        return s;
      }
    }
    
    public com.google.protobuf.ByteString
        getNumberBytes() {
      java.lang.Object ref = number_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        number_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int NAME_FIELD_NUMBER = 2;
    private java.lang.Object name_;
    
    public boolean hasName() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
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

    public static final int AVATAR_FIELD_NUMBER = 3;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar avatar_;
    
    public boolean hasAvatar() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar getAvatar() {
      return avatar_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.AvatarOrBuilder getAvatarOrBuilder() {
      return avatar_;
    }

    public static final int COLOR_FIELD_NUMBER = 4;
    private java.lang.Object color_;
    
    public boolean hasColor() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public java.lang.String getColor() {
      java.lang.Object ref = color_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          color_ = s;
        }
        return s;
      }
    }
    
    public com.google.protobuf.ByteString
        getColorBytes() {
      java.lang.Object ref = color_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        color_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int VERIFIED_FIELD_NUMBER = 5;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified verified_;
    
    public boolean hasVerified() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified getVerified() {
      return verified_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder getVerifiedOrBuilder() {
      return verified_;
    }

    public static final int PROFILEKEY_FIELD_NUMBER = 6;
    private com.google.protobuf.ByteString profileKey_;
    
    public boolean hasProfileKey() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    
    public com.google.protobuf.ByteString getProfileKey() {
      return profileKey_;
    }

    private void initFields() {
      number_ = "";
      name_ = "";
      avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.getDefaultInstance();
      color_ = "";
      verified_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance();
      profileKey_ = com.google.protobuf.ByteString.EMPTY;
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
        output.writeBytes(1, getNumberBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getNameBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(3, avatar_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, getColorBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeMessage(5, verified_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(6, profileKey_);
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
          .computeBytesSize(1, getNumberBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getNameBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, avatar_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getColorBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, verified_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, profileKey_);
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetailsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ContactDetails_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ContactDetails_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Builder.class);
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
          getVerifiedFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        number_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        if (avatarBuilder_ == null) {
          avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.getDefaultInstance();
        } else {
          avatarBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        color_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        if (verifiedBuilder_ == null) {
          verified_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance();
        } else {
          verifiedBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        profileKey_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_ContactDetails_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.number_ = number_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (avatarBuilder_ == null) {
          result.avatar_ = avatar_;
        } else {
          result.avatar_ = avatarBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.color_ = color_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        if (verifiedBuilder_ == null) {
          result.verified_ = verified_;
        } else {
          result.verified_ = verifiedBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.profileKey_ = profileKey_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.getDefaultInstance()) return this;
        if (other.hasNumber()) {
          bitField0_ |= 0x00000001;
          number_ = other.number_;
          onChanged();
        }
        if (other.hasName()) {
          bitField0_ |= 0x00000002;
          name_ = other.name_;
          onChanged();
        }
        if (other.hasAvatar()) {
          mergeAvatar(other.getAvatar());
        }
        if (other.hasColor()) {
          bitField0_ |= 0x00000008;
          color_ = other.color_;
          onChanged();
        }
        if (other.hasVerified()) {
          mergeVerified(other.getVerified());
        }
        if (other.hasProfileKey()) {
          setProfileKey(other.getProfileKey());
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object number_ = "";
      
      public boolean hasNumber() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      
      public java.lang.String getNumber() {
        java.lang.Object ref = number_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          number_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      
      public com.google.protobuf.ByteString
          getNumberBytes() {
        java.lang.Object ref = number_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          number_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      
      public Builder setNumber(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        number_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearNumber() {
        bitField0_ = (bitField0_ & ~0x00000001);
        number_ = getDefaultInstance().getNumber();
        onChanged();
        return this;
      }
      
      public Builder setNumberBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        number_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object name_ = "";
      
      public boolean hasName() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
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
  bitField0_ |= 0x00000002;
        name_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000002);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        name_ = value;
        onChanged();
        return this;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar, com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.AvatarOrBuilder> avatarBuilder_;
      
      public boolean hasAvatar() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar getAvatar() {
        if (avatarBuilder_ == null) {
          return avatar_;
        } else {
          return avatarBuilder_.getMessage();
        }
      }
      
      public Builder setAvatar(com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar value) {
        if (avatarBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          avatar_ = value;
          onChanged();
        } else {
          avatarBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder setAvatar(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.Builder builderForValue) {
        if (avatarBuilder_ == null) {
          avatar_ = builderForValue.build();
          onChanged();
        } else {
          avatarBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder mergeAvatar(com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar value) {
        if (avatarBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              avatar_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.getDefaultInstance()) {
            avatar_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.newBuilder(avatar_).mergeFrom(value).buildPartial();
          } else {
            avatar_ = value;
          }
          onChanged();
        } else {
          avatarBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      
      public Builder clearAvatar() {
        if (avatarBuilder_ == null) {
          avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.getDefaultInstance();
          onChanged();
        } else {
          avatarBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.Builder getAvatarBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getAvatarFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.AvatarOrBuilder getAvatarOrBuilder() {
        if (avatarBuilder_ != null) {
          return avatarBuilder_.getMessageOrBuilder();
        } else {
          return avatar_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar, com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.AvatarOrBuilder> 
          getAvatarFieldBuilder() {
        if (avatarBuilder_ == null) {
          avatarBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar, com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.Avatar.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.ContactDetails.AvatarOrBuilder>(
                  avatar_,
                  getParentForChildren(),
                  isClean());
          avatar_ = null;
        }
        return avatarBuilder_;
      }

      private java.lang.Object color_ = "";
      
      public boolean hasColor() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public java.lang.String getColor() {
        java.lang.Object ref = color_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          color_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      
      public com.google.protobuf.ByteString
          getColorBytes() {
        java.lang.Object ref = color_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          color_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      
      public Builder setColor(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        color_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearColor() {
        bitField0_ = (bitField0_ & ~0x00000008);
        color_ = getDefaultInstance().getColor();
        onChanged();
        return this;
      }
      
      public Builder setColorBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        color_ = value;
        onChanged();
        return this;
      }

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified verified_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified, com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder> verifiedBuilder_;
      
      public boolean hasVerified() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified getVerified() {
        if (verifiedBuilder_ == null) {
          return verified_;
        } else {
          return verifiedBuilder_.getMessage();
        }
      }
      
      public Builder setVerified(com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified value) {
        if (verifiedBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          verified_ = value;
          onChanged();
        } else {
          verifiedBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder setVerified(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder builderForValue) {
        if (verifiedBuilder_ == null) {
          verified_ = builderForValue.build();
          onChanged();
        } else {
          verifiedBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder mergeVerified(com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified value) {
        if (verifiedBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              verified_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance()) {
            verified_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.newBuilder(verified_).mergeFrom(value).buildPartial();
          } else {
            verified_ = value;
          }
          onChanged();
        } else {
          verifiedBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      
      public Builder clearVerified() {
        if (verifiedBuilder_ == null) {
          verified_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.getDefaultInstance();
          onChanged();
        } else {
          verifiedBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder getVerifiedBuilder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getVerifiedFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder getVerifiedOrBuilder() {
        if (verifiedBuilder_ != null) {
          return verifiedBuilder_.getMessageOrBuilder();
        } else {
          return verified_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified, com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder> 
          getVerifiedFieldBuilder() {
        if (verifiedBuilder_ == null) {
          verifiedBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified, com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.VerifiedOrBuilder>(
                  verified_,
                  getParentForChildren(),
                  isClean());
          verified_ = null;
        }
        return verifiedBuilder_;
      }

      private com.google.protobuf.ByteString profileKey_ = com.google.protobuf.ByteString.EMPTY;
      
      public boolean hasProfileKey() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      
      public com.google.protobuf.ByteString getProfileKey() {
        return profileKey_;
      }
      
      public Builder setProfileKey(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        profileKey_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearProfileKey() {
        bitField0_ = (bitField0_ & ~0x00000020);
        profileKey_ = getDefaultInstance().getProfileKey();
        onChanged();
        return this;
      }

    }

    static {
      defaultInstance = new ContactDetails(true);
      defaultInstance.initFields();
    }

  }

  public interface GroupDetailsOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    
    boolean hasId();
    
    com.google.protobuf.ByteString getId();

    
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
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar getAvatar();
    
    com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.AvatarOrBuilder getAvatarOrBuilder();

    
    boolean hasActive();
    
    boolean getActive();
  }
  
  public static final class GroupDetails extends
      com.google.protobuf.GeneratedMessage
      implements GroupDetailsOrBuilder {
    private GroupDetails(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private GroupDetails(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final GroupDetails defaultInstance;
    public static GroupDetails getDefaultInstance() {
      return defaultInstance;
    }

    public GroupDetails getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private GroupDetails(
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
            case 18: {
              bitField0_ |= 0x00000002;
              name_ = input.readBytes();
              break;
            }
            case 26: {
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
                members_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000004;
              }
              members_.add(input.readBytes());
              break;
            }
            case 34: {
              com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = avatar_.toBuilder();
              }
              avatar_ = input.readMessage(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(avatar_);
                avatar_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 40: {
              bitField0_ |= 0x00000008;
              active_ = input.readBool();
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
        if (((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
          members_ = new com.google.protobuf.UnmodifiableLazyStringList(members_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupDetails_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupDetails_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Builder.class);
    }

    public static com.google.protobuf.Parser<GroupDetails> PARSER =
        new com.google.protobuf.AbstractParser<GroupDetails>() {
      public GroupDetails parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new GroupDetails(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<GroupDetails> getParserForType() {
      return PARSER;
    }

    public interface AvatarOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      
      boolean hasContentType();
      
      java.lang.String getContentType();
      
      com.google.protobuf.ByteString
          getContentTypeBytes();

      
      boolean hasLength();
      
      int getLength();
    }
    
    public static final class Avatar extends
        com.google.protobuf.GeneratedMessage
        implements AvatarOrBuilder {
      private Avatar(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Avatar(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Avatar defaultInstance;
      public static Avatar getDefaultInstance() {
        return defaultInstance;
      }

      public Avatar getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Avatar(
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
                contentType_ = input.readBytes();
                break;
              }
              case 16: {
                bitField0_ |= 0x00000002;
                length_ = input.readUInt32();
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
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupDetails_Avatar_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupDetails_Avatar_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.Builder.class);
      }

      public static com.google.protobuf.Parser<Avatar> PARSER =
          new com.google.protobuf.AbstractParser<Avatar>() {
        public Avatar parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Avatar(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Avatar> getParserForType() {
        return PARSER;
      }

      private int bitField0_;
      public static final int CONTENTTYPE_FIELD_NUMBER = 1;
      private java.lang.Object contentType_;
      
      public boolean hasContentType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
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

      public static final int LENGTH_FIELD_NUMBER = 2;
      private int length_;
      
      public boolean hasLength() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      
      public int getLength() {
        return length_;
      }

      private void initFields() {
        contentType_ = "";
        length_ = 0;
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
          output.writeBytes(1, getContentTypeBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeUInt32(2, length_);
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
            .computeBytesSize(1, getContentTypeBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeUInt32Size(2, length_);
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

      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar prototype) {
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
         implements com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.AvatarOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupDetails_Avatar_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupDetails_Avatar_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.Builder.class);
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
          contentType_ = "";
          bitField0_ = (bitField0_ & ~0x00000001);
          length_ = 0;
          bitField0_ = (bitField0_ & ~0x00000002);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupDetails_Avatar_descriptor;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar getDefaultInstanceForType() {
          return com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.getDefaultInstance();
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar build() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar buildPartial() {
          com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.contentType_ = contentType_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.length_ = length_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar) {
            return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar other) {
          if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.getDefaultInstance()) return this;
          if (other.hasContentType()) {
            bitField0_ |= 0x00000001;
            contentType_ = other.contentType_;
            onChanged();
          }
          if (other.hasLength()) {
            setLength(other.getLength());
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
          com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private java.lang.Object contentType_ = "";
        
        public boolean hasContentType() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
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
  bitField0_ |= 0x00000001;
          contentType_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearContentType() {
          bitField0_ = (bitField0_ & ~0x00000001);
          contentType_ = getDefaultInstance().getContentType();
          onChanged();
          return this;
        }
        
        public Builder setContentTypeBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          contentType_ = value;
          onChanged();
          return this;
        }

        private int length_ ;
        
        public boolean hasLength() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        
        public int getLength() {
          return length_;
        }
        
        public Builder setLength(int value) {
          bitField0_ |= 0x00000002;
          length_ = value;
          onChanged();
          return this;
        }
        
        public Builder clearLength() {
          bitField0_ = (bitField0_ & ~0x00000002);
          length_ = 0;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new Avatar(true);
        defaultInstance.initFields();
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

    public static final int NAME_FIELD_NUMBER = 2;
    private java.lang.Object name_;
    
    public boolean hasName() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
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

    public static final int MEMBERS_FIELD_NUMBER = 3;
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

    public static final int AVATAR_FIELD_NUMBER = 4;
    private com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar avatar_;
    
    public boolean hasAvatar() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar getAvatar() {
      return avatar_;
    }
    
    public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.AvatarOrBuilder getAvatarOrBuilder() {
      return avatar_;
    }

    public static final int ACTIVE_FIELD_NUMBER = 5;
    private boolean active_;
    
    public boolean hasActive() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    
    public boolean getActive() {
      return active_;
    }

    private void initFields() {
      id_ = com.google.protobuf.ByteString.EMPTY;
      name_ = "";
      members_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.getDefaultInstance();
      active_ = true;
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
        output.writeBytes(2, getNameBytes());
      }
      for (int i = 0; i < members_.size(); i++) {
        output.writeBytes(3, members_.getByteString(i));
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(4, avatar_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBool(5, active_);
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
          .computeBytesSize(2, getNameBytes());
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
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, avatar_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(5, active_);
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

    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails prototype) {
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
       implements com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetailsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupDetails_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupDetails_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.class, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Builder.class);
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
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        members_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        if (avatarBuilder_ == null) {
          avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.getDefaultInstance();
        } else {
          avatarBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        active_ = true;
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.internal_static_openchatservice_GroupDetails_descriptor;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails getDefaultInstanceForType() {
        return com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.getDefaultInstance();
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails build() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails buildPartial() {
        com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails result = new com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.id_ = id_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.name_ = name_;
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          members_ = new com.google.protobuf.UnmodifiableLazyStringList(
              members_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.members_ = members_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000004;
        }
        if (avatarBuilder_ == null) {
          result.avatar_ = avatar_;
        } else {
          result.avatar_ = avatarBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000008;
        }
        result.active_ = active_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails) {
          return mergeFrom((com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails other) {
        if (other == com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasName()) {
          bitField0_ |= 0x00000002;
          name_ = other.name_;
          onChanged();
        }
        if (!other.members_.isEmpty()) {
          if (members_.isEmpty()) {
            members_ = other.members_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensureMembersIsMutable();
            members_.addAll(other.members_);
          }
          onChanged();
        }
        if (other.hasAvatar()) {
          mergeAvatar(other.getAvatar());
        }
        if (other.hasActive()) {
          setActive(other.getActive());
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
        com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails) e.getUnfinishedMessage();
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

      private java.lang.Object name_ = "";
      
      public boolean hasName() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
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
  bitField0_ |= 0x00000002;
        name_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000002);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        name_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.LazyStringList members_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureMembersIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          members_ = new com.google.protobuf.LazyStringArrayList(members_);
          bitField0_ |= 0x00000004;
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
        bitField0_ = (bitField0_ & ~0x00000004);
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

      private com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.AvatarOrBuilder> avatarBuilder_;
      
      public boolean hasAvatar() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar getAvatar() {
        if (avatarBuilder_ == null) {
          return avatar_;
        } else {
          return avatarBuilder_.getMessage();
        }
      }
      
      public Builder setAvatar(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar value) {
        if (avatarBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          avatar_ = value;
          onChanged();
        } else {
          avatarBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder setAvatar(
          com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.Builder builderForValue) {
        if (avatarBuilder_ == null) {
          avatar_ = builderForValue.build();
          onChanged();
        } else {
          avatarBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder mergeAvatar(com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar value) {
        if (avatarBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              avatar_ != com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.getDefaultInstance()) {
            avatar_ =
              com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.newBuilder(avatar_).mergeFrom(value).buildPartial();
          } else {
            avatar_ = value;
          }
          onChanged();
        } else {
          avatarBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      
      public Builder clearAvatar() {
        if (avatarBuilder_ == null) {
          avatar_ = com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.getDefaultInstance();
          onChanged();
        } else {
          avatarBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.Builder getAvatarBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getAvatarFieldBuilder().getBuilder();
      }
      
      public com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.AvatarOrBuilder getAvatarOrBuilder() {
        if (avatarBuilder_ != null) {
          return avatarBuilder_.getMessageOrBuilder();
        } else {
          return avatar_;
        }
      }
      
      private com.google.protobuf.SingleFieldBuilder<
          com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.AvatarOrBuilder> 
          getAvatarFieldBuilder() {
        if (avatarBuilder_ == null) {
          avatarBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.Avatar.Builder, com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupDetails.AvatarOrBuilder>(
                  avatar_,
                  getParentForChildren(),
                  isClean());
          avatar_ = null;
        }
        return avatarBuilder_;
      }

      private boolean active_ = true;
      
      public boolean hasActive() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      
      public boolean getActive() {
        return active_;
      }
      
      public Builder setActive(boolean value) {
        bitField0_ |= 0x00000010;
        active_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearActive() {
        bitField0_ = (bitField0_ & ~0x00000010);
        active_ = true;
        onChanged();
        return this;
      }

    }

    static {
      defaultInstance = new GroupDetails(true);
      defaultInstance.initFields();
    }

  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_Envelope_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_Envelope_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_Content_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_Content_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_CallMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_CallMessage_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_CallMessage_Offer_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_CallMessage_Offer_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_CallMessage_Answer_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_CallMessage_Answer_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_CallMessage_IceUpdate_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_CallMessage_IceUpdate_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_CallMessage_Busy_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_CallMessage_Busy_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_CallMessage_Hangup_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_CallMessage_Hangup_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_DataMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_DataMessage_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_NullMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_NullMessage_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_ReceiptMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_ReceiptMessage_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_Verified_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_Verified_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_SyncMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_SyncMessage_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_SyncMessage_Sent_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_SyncMessage_Sent_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_SyncMessage_Contacts_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_SyncMessage_Contacts_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_SyncMessage_Groups_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_SyncMessage_Groups_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_SyncMessage_Blocked_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_SyncMessage_Blocked_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_SyncMessage_Request_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_SyncMessage_Request_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_SyncMessage_Read_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_SyncMessage_Read_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_SyncMessage_Configuration_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_SyncMessage_Configuration_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_AttachmentPointer_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_AttachmentPointer_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_GroupContext_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_GroupContext_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_ContactDetails_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_ContactDetails_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_ContactDetails_Avatar_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_ContactDetails_Avatar_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_GroupDetails_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_GroupDetails_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_openchatservice_GroupDetails_Avatar_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_openchatservice_GroupDetails_Avatar_fieldAccessorTable;

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
          internal_static_openchatservice_Envelope_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_openchatservice_Envelope_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_Envelope_descriptor,
              new java.lang.String[] { "Type", "Source", "SourceDevice", "Relay", "Timestamp", "LegacyMessage", "Content", });
          internal_static_openchatservice_Content_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_openchatservice_Content_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_Content_descriptor,
              new java.lang.String[] { "DataMessage", "SyncMessage", "CallMessage", "NullMessage", "ReceiptMessage", });
          internal_static_openchatservice_CallMessage_descriptor =
            getDescriptor().getMessageTypes().get(2);
          internal_static_openchatservice_CallMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_CallMessage_descriptor,
              new java.lang.String[] { "Offer", "Answer", "IceUpdate", "Hangup", "Busy", });
          internal_static_openchatservice_CallMessage_Offer_descriptor =
            internal_static_openchatservice_CallMessage_descriptor.getNestedTypes().get(0);
          internal_static_openchatservice_CallMessage_Offer_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_CallMessage_Offer_descriptor,
              new java.lang.String[] { "Id", "Description", });
          internal_static_openchatservice_CallMessage_Answer_descriptor =
            internal_static_openchatservice_CallMessage_descriptor.getNestedTypes().get(1);
          internal_static_openchatservice_CallMessage_Answer_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_CallMessage_Answer_descriptor,
              new java.lang.String[] { "Id", "Description", });
          internal_static_openchatservice_CallMessage_IceUpdate_descriptor =
            internal_static_openchatservice_CallMessage_descriptor.getNestedTypes().get(2);
          internal_static_openchatservice_CallMessage_IceUpdate_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_CallMessage_IceUpdate_descriptor,
              new java.lang.String[] { "Id", "SdpMid", "SdpMLineIndex", "Sdp", });
          internal_static_openchatservice_CallMessage_Busy_descriptor =
            internal_static_openchatservice_CallMessage_descriptor.getNestedTypes().get(3);
          internal_static_openchatservice_CallMessage_Busy_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_CallMessage_Busy_descriptor,
              new java.lang.String[] { "Id", });
          internal_static_openchatservice_CallMessage_Hangup_descriptor =
            internal_static_openchatservice_CallMessage_descriptor.getNestedTypes().get(4);
          internal_static_openchatservice_CallMessage_Hangup_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_CallMessage_Hangup_descriptor,
              new java.lang.String[] { "Id", });
          internal_static_openchatservice_DataMessage_descriptor =
            getDescriptor().getMessageTypes().get(3);
          internal_static_openchatservice_DataMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_DataMessage_descriptor,
              new java.lang.String[] { "Body", "Attachments", "Group", "Flags", "ExpireTimer", "ProfileKey", "Timestamp", });
          internal_static_openchatservice_NullMessage_descriptor =
            getDescriptor().getMessageTypes().get(4);
          internal_static_openchatservice_NullMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_NullMessage_descriptor,
              new java.lang.String[] { "Padding", });
          internal_static_openchatservice_ReceiptMessage_descriptor =
            getDescriptor().getMessageTypes().get(5);
          internal_static_openchatservice_ReceiptMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_ReceiptMessage_descriptor,
              new java.lang.String[] { "Type", "Timestamp", });
          internal_static_openchatservice_Verified_descriptor =
            getDescriptor().getMessageTypes().get(6);
          internal_static_openchatservice_Verified_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_Verified_descriptor,
              new java.lang.String[] { "Destination", "IdentityKey", "State", "NullMessage", });
          internal_static_openchatservice_SyncMessage_descriptor =
            getDescriptor().getMessageTypes().get(7);
          internal_static_openchatservice_SyncMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_SyncMessage_descriptor,
              new java.lang.String[] { "Sent", "Contacts", "Groups", "Request", "Read", "Blocked", "Verified", "Configuration", "Padding", });
          internal_static_openchatservice_SyncMessage_Sent_descriptor =
            internal_static_openchatservice_SyncMessage_descriptor.getNestedTypes().get(0);
          internal_static_openchatservice_SyncMessage_Sent_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_SyncMessage_Sent_descriptor,
              new java.lang.String[] { "Destination", "Timestamp", "Message", "ExpirationStartTimestamp", });
          internal_static_openchatservice_SyncMessage_Contacts_descriptor =
            internal_static_openchatservice_SyncMessage_descriptor.getNestedTypes().get(1);
          internal_static_openchatservice_SyncMessage_Contacts_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_SyncMessage_Contacts_descriptor,
              new java.lang.String[] { "Blob", "Complete", });
          internal_static_openchatservice_SyncMessage_Groups_descriptor =
            internal_static_openchatservice_SyncMessage_descriptor.getNestedTypes().get(2);
          internal_static_openchatservice_SyncMessage_Groups_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_SyncMessage_Groups_descriptor,
              new java.lang.String[] { "Blob", });
          internal_static_openchatservice_SyncMessage_Blocked_descriptor =
            internal_static_openchatservice_SyncMessage_descriptor.getNestedTypes().get(3);
          internal_static_openchatservice_SyncMessage_Blocked_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_SyncMessage_Blocked_descriptor,
              new java.lang.String[] { "Numbers", });
          internal_static_openchatservice_SyncMessage_Request_descriptor =
            internal_static_openchatservice_SyncMessage_descriptor.getNestedTypes().get(4);
          internal_static_openchatservice_SyncMessage_Request_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_SyncMessage_Request_descriptor,
              new java.lang.String[] { "Type", });
          internal_static_openchatservice_SyncMessage_Read_descriptor =
            internal_static_openchatservice_SyncMessage_descriptor.getNestedTypes().get(5);
          internal_static_openchatservice_SyncMessage_Read_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_SyncMessage_Read_descriptor,
              new java.lang.String[] { "Sender", "Timestamp", });
          internal_static_openchatservice_SyncMessage_Configuration_descriptor =
            internal_static_openchatservice_SyncMessage_descriptor.getNestedTypes().get(6);
          internal_static_openchatservice_SyncMessage_Configuration_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_SyncMessage_Configuration_descriptor,
              new java.lang.String[] { "ReadReceipts", });
          internal_static_openchatservice_AttachmentPointer_descriptor =
            getDescriptor().getMessageTypes().get(8);
          internal_static_openchatservice_AttachmentPointer_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_AttachmentPointer_descriptor,
              new java.lang.String[] { "Id", "ContentType", "Key", "Size", "Thumbnail", "Digest", "FileName", "Flags", });
          internal_static_openchatservice_GroupContext_descriptor =
            getDescriptor().getMessageTypes().get(9);
          internal_static_openchatservice_GroupContext_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_GroupContext_descriptor,
              new java.lang.String[] { "Id", "Type", "Name", "Members", "Avatar", });
          internal_static_openchatservice_ContactDetails_descriptor =
            getDescriptor().getMessageTypes().get(10);
          internal_static_openchatservice_ContactDetails_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_ContactDetails_descriptor,
              new java.lang.String[] { "Number", "Name", "Avatar", "Color", "Verified", "ProfileKey", });
          internal_static_openchatservice_ContactDetails_Avatar_descriptor =
            internal_static_openchatservice_ContactDetails_descriptor.getNestedTypes().get(0);
          internal_static_openchatservice_ContactDetails_Avatar_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_ContactDetails_Avatar_descriptor,
              new java.lang.String[] { "ContentType", "Length", });
          internal_static_openchatservice_GroupDetails_descriptor =
            getDescriptor().getMessageTypes().get(11);
          internal_static_openchatservice_GroupDetails_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_GroupDetails_descriptor,
              new java.lang.String[] { "Id", "Name", "Members", "Avatar", "Active", });
          internal_static_openchatservice_GroupDetails_Avatar_descriptor =
            internal_static_openchatservice_GroupDetails_descriptor.getNestedTypes().get(0);
          internal_static_openchatservice_GroupDetails_Avatar_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_openchatservice_GroupDetails_Avatar_descriptor,
              new java.lang.String[] { "ContentType", "Length", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

}
