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

  private static volatile io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving,
      com.example.grpc.TaxiNetworkServiceOuterClass.Empty> getNotifyTaxiLeavingNetworkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "notifyTaxiLeavingNetwork",
      requestType = com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving.class,
      responseType = com.example.grpc.TaxiNetworkServiceOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving,
      com.example.grpc.TaxiNetworkServiceOuterClass.Empty> getNotifyTaxiLeavingNetworkMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving, com.example.grpc.TaxiNetworkServiceOuterClass.Empty> getNotifyTaxiLeavingNetworkMethod;
    if ((getNotifyTaxiLeavingNetworkMethod = TaxiNetworkServiceGrpc.getNotifyTaxiLeavingNetworkMethod) == null) {
      synchronized (TaxiNetworkServiceGrpc.class) {
        if ((getNotifyTaxiLeavingNetworkMethod = TaxiNetworkServiceGrpc.getNotifyTaxiLeavingNetworkMethod) == null) {
          TaxiNetworkServiceGrpc.getNotifyTaxiLeavingNetworkMethod = getNotifyTaxiLeavingNetworkMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving, com.example.grpc.TaxiNetworkServiceOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "notifyTaxiLeavingNetwork"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.TaxiNetworkServiceOuterClass.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new TaxiNetworkServiceMethodDescriptorSupplier("notifyTaxiLeavingNetwork"))
              .build();
        }
      }
    }
    return getNotifyTaxiLeavingNetworkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage,
      com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply> getElectionMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "electionMessage",
      requestType = com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage.class,
      responseType = com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage,
      com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply> getElectionMessageMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage, com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply> getElectionMessageMethod;
    if ((getElectionMessageMethod = TaxiNetworkServiceGrpc.getElectionMessageMethod) == null) {
      synchronized (TaxiNetworkServiceGrpc.class) {
        if ((getElectionMessageMethod = TaxiNetworkServiceGrpc.getElectionMessageMethod) == null) {
          TaxiNetworkServiceGrpc.getElectionMessageMethod = getElectionMessageMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage, com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "electionMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply.getDefaultInstance()))
              .setSchemaDescriptor(new TaxiNetworkServiceMethodDescriptorSupplier("electionMessage"))
              .build();
        }
      }
    }
    return getElectionMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage,
      com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply> getRechargeMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "rechargeMessage",
      requestType = com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage.class,
      responseType = com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage,
      com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply> getRechargeMessageMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage, com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply> getRechargeMessageMethod;
    if ((getRechargeMessageMethod = TaxiNetworkServiceGrpc.getRechargeMessageMethod) == null) {
      synchronized (TaxiNetworkServiceGrpc.class) {
        if ((getRechargeMessageMethod = TaxiNetworkServiceGrpc.getRechargeMessageMethod) == null) {
          TaxiNetworkServiceGrpc.getRechargeMessageMethod = getRechargeMessageMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage, com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "rechargeMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply.getDefaultInstance()))
              .setSchemaDescriptor(new TaxiNetworkServiceMethodDescriptorSupplier("rechargeMessage"))
              .build();
        }
      }
    }
    return getRechargeMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission,
      com.example.grpc.TaxiNetworkServiceOuterClass.Empty> getNotifyPermissionToRechargeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "notifyPermissionToRecharge",
      requestType = com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission.class,
      responseType = com.example.grpc.TaxiNetworkServiceOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission,
      com.example.grpc.TaxiNetworkServiceOuterClass.Empty> getNotifyPermissionToRechargeMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission, com.example.grpc.TaxiNetworkServiceOuterClass.Empty> getNotifyPermissionToRechargeMethod;
    if ((getNotifyPermissionToRechargeMethod = TaxiNetworkServiceGrpc.getNotifyPermissionToRechargeMethod) == null) {
      synchronized (TaxiNetworkServiceGrpc.class) {
        if ((getNotifyPermissionToRechargeMethod = TaxiNetworkServiceGrpc.getNotifyPermissionToRechargeMethod) == null) {
          TaxiNetworkServiceGrpc.getNotifyPermissionToRechargeMethod = getNotifyPermissionToRechargeMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission, com.example.grpc.TaxiNetworkServiceOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "notifyPermissionToRecharge"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.TaxiNetworkServiceOuterClass.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new TaxiNetworkServiceMethodDescriptorSupplier("notifyPermissionToRecharge"))
              .build();
        }
      }
    }
    return getNotifyPermissionToRechargeMethod;
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

    /**
     */
    public void notifyTaxiLeavingNetwork(com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving request,
        io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyTaxiLeavingNetworkMethod(), responseObserver);
    }

    /**
     */
    public void electionMessage(com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage request,
        io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply> responseObserver) {
      asyncUnimplementedUnaryCall(getElectionMessageMethod(), responseObserver);
    }

    /**
     */
    public void rechargeMessage(com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage request,
        io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply> responseObserver) {
      asyncUnimplementedUnaryCall(getRechargeMessageMethod(), responseObserver);
    }

    /**
     */
    public void notifyPermissionToRecharge(com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission request,
        io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyPermissionToRechargeMethod(), responseObserver);
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
          .addMethod(
            getNotifyTaxiLeavingNetworkMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving,
                com.example.grpc.TaxiNetworkServiceOuterClass.Empty>(
                  this, METHODID_NOTIFY_TAXI_LEAVING_NETWORK)))
          .addMethod(
            getElectionMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage,
                com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply>(
                  this, METHODID_ELECTION_MESSAGE)))
          .addMethod(
            getRechargeMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage,
                com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply>(
                  this, METHODID_RECHARGE_MESSAGE)))
          .addMethod(
            getNotifyPermissionToRechargeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission,
                com.example.grpc.TaxiNetworkServiceOuterClass.Empty>(
                  this, METHODID_NOTIFY_PERMISSION_TO_RECHARGE)))
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

    /**
     */
    public void notifyTaxiLeavingNetwork(com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving request,
        io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNotifyTaxiLeavingNetworkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void electionMessage(com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage request,
        io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElectionMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void rechargeMessage(com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage request,
        io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRechargeMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void notifyPermissionToRecharge(com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission request,
        io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNotifyPermissionToRechargeMethod(), getCallOptions()), request, responseObserver);
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

    /**
     */
    public com.example.grpc.TaxiNetworkServiceOuterClass.Empty notifyTaxiLeavingNetwork(com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving request) {
      return blockingUnaryCall(
          getChannel(), getNotifyTaxiLeavingNetworkMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply electionMessage(com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage request) {
      return blockingUnaryCall(
          getChannel(), getElectionMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply rechargeMessage(com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage request) {
      return blockingUnaryCall(
          getChannel(), getRechargeMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.grpc.TaxiNetworkServiceOuterClass.Empty notifyPermissionToRecharge(com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission request) {
      return blockingUnaryCall(
          getChannel(), getNotifyPermissionToRechargeMethod(), getCallOptions(), request);
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

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.TaxiNetworkServiceOuterClass.Empty> notifyTaxiLeavingNetwork(
        com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving request) {
      return futureUnaryCall(
          getChannel().newCall(getNotifyTaxiLeavingNetworkMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply> electionMessage(
        com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getElectionMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply> rechargeMessage(
        com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getRechargeMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.TaxiNetworkServiceOuterClass.Empty> notifyPermissionToRecharge(
        com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission request) {
      return futureUnaryCall(
          getChannel().newCall(getNotifyPermissionToRechargeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_NOTIFY_NEW_TAXI_PRESENCE = 0;
  private static final int METHODID_NOTIFY_TAXI_LEAVING_NETWORK = 1;
  private static final int METHODID_ELECTION_MESSAGE = 2;
  private static final int METHODID_RECHARGE_MESSAGE = 3;
  private static final int METHODID_NOTIFY_PERMISSION_TO_RECHARGE = 4;

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
        case METHODID_NOTIFY_TAXI_LEAVING_NETWORK:
          serviceImpl.notifyTaxiLeavingNetwork((com.example.grpc.TaxiNetworkServiceOuterClass.TaxiLeaving) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.Empty>) responseObserver);
          break;
        case METHODID_ELECTION_MESSAGE:
          serviceImpl.electionMessage((com.example.grpc.TaxiNetworkServiceOuterClass.ElectionMessage) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.ElectionReply>) responseObserver);
          break;
        case METHODID_RECHARGE_MESSAGE:
          serviceImpl.rechargeMessage((com.example.grpc.TaxiNetworkServiceOuterClass.RechargeMessage) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.TaxiNetworkServiceOuterClass.RechargeReply>) responseObserver);
          break;
        case METHODID_NOTIFY_PERMISSION_TO_RECHARGE:
          serviceImpl.notifyPermissionToRecharge((com.example.grpc.TaxiNetworkServiceOuterClass.RechargePermission) request,
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
              .addMethod(getNotifyTaxiLeavingNetworkMethod())
              .addMethod(getElectionMessageMethod())
              .addMethod(getRechargeMessageMethod())
              .addMethod(getNotifyPermissionToRechargeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
