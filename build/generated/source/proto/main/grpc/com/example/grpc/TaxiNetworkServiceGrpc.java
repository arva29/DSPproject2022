package com.example.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: TaxiNetworkService.proto")
public final class TaxiNetworkServiceGrpc {

  private TaxiNetworkServiceGrpc() {}

  public static final String SERVICE_NAME = "com.example.grpc.TaxiNetworkService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation,
      com.example.grpc.TaxiNetworkServiceOuterClass.Empty> getNotifyNewTaxiPresenceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "notifyNewTaxiPresence",
      requestType = com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation.class,
      responseType = com.example.grpc.TaxiNetworkServiceOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation,
      com.example.grpc.TaxiNetworkServiceOuterClass.Empty> getNotifyNewTaxiPresenceMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation, com.example.grpc.TaxiNetworkServiceOuterClass.Empty> getNotifyNewTaxiPresenceMethod;
    if ((getNotifyNewTaxiPresenceMethod = TaxiNetworkServiceGrpc.getNotifyNewTaxiPresenceMethod) == null) {
      synchronized (TaxiNetworkServiceGrpc.class) {
        if ((getNotifyNewTaxiPresenceMethod = TaxiNetworkServiceGrpc.getNotifyNewTaxiPresenceMethod) == null) {
          TaxiNetworkServiceGrpc.getNotifyNewTaxiPresenceMethod = getNotifyNewTaxiPresenceMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation, com.example.grpc.TaxiNetworkServiceOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "notifyNewTaxiPresence"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.TaxiNetworkServiceOuterClass.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new TaxiNetworkServiceMethodDescriptorSupplier("notifyNewTaxiPresence"))
              .build();
        }
      }
    }
    return getNotifyNewTaxiPresenceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TaxiNetworkServiceStub newStub(io.grpc.Channel channel) {
    return new TaxiNetworkServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TaxiNetworkServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TaxiNetworkServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TaxiNetworkServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TaxiNetworkServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TaxiNetworkServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void notifyNewTaxiPresence(com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation request,
        io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyNewTaxiPresenceMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getNotifyNewTaxiPresenceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation,
                com.example.grpc.TaxiNetworkServiceOuterClass.Empty>(
                  this, METHODID_NOTIFY_NEW_TAXI_PRESENCE)))
          .build();
    }
  }

  /**
   */
  public static final class TaxiNetworkServiceStub extends io.grpc.stub.AbstractStub<TaxiNetworkServiceStub> {
    private TaxiNetworkServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaxiNetworkServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaxiNetworkServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaxiNetworkServiceStub(channel, callOptions);
    }

    /**
     */
    public void notifyNewTaxiPresence(com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation request,
        io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNotifyNewTaxiPresenceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TaxiNetworkServiceBlockingStub extends io.grpc.stub.AbstractStub<TaxiNetworkServiceBlockingStub> {
    private TaxiNetworkServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaxiNetworkServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaxiNetworkServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaxiNetworkServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.grpc.TaxiNetworkServiceOuterClass.Empty notifyNewTaxiPresence(com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation request) {
      return blockingUnaryCall(
          getChannel(), getNotifyNewTaxiPresenceMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TaxiNetworkServiceFutureStub extends io.grpc.stub.AbstractStub<TaxiNetworkServiceFutureStub> {
    private TaxiNetworkServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaxiNetworkServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaxiNetworkServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaxiNetworkServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.TaxiNetworkServiceOuterClass.Empty> notifyNewTaxiPresence(
        com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation request) {
      return futureUnaryCall(
          getChannel().newCall(getNotifyNewTaxiPresenceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_NOTIFY_NEW_TAXI_PRESENCE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TaxiNetworkServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TaxiNetworkServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_NOTIFY_NEW_TAXI_PRESENCE:
          serviceImpl.notifyNewTaxiPresence((com.example.grpc.TaxiNetworkServiceOuterClass.TaxiInformation) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class TaxiNetworkServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TaxiNetworkServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.grpc.TaxiNetworkServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TaxiNetworkService");
    }
  }

  private static final class TaxiNetworkServiceFileDescriptorSupplier
      extends TaxiNetworkServiceBaseDescriptorSupplier {
    TaxiNetworkServiceFileDescriptorSupplier() {}
  }

  private static final class TaxiNetworkServiceMethodDescriptorSupplier
      extends TaxiNetworkServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TaxiNetworkServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TaxiNetworkServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TaxiNetworkServiceFileDescriptorSupplier())
              .addMethod(getNotifyNewTaxiPresenceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
