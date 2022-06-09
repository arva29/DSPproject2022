package SETA;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.RideRequest;
import com.example.grpc.TaxiNetworkServiceGrpc.*;
import com.example.grpc.TaxiNetworkServiceOuterClass.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.example.grpc.TaxiNetworkServiceGrpc.newStub;

public class NetworkCommunicationModule extends Thread{
    private Server server;
    private RideManagementModule rideManagementModule;
    int rechargeReplyCounter = 0;

    @Override
    public void run() {
        try {
            server = ServerBuilder.forPort(Taxi.getTaxiNetworkInfo().getPortNumber()).addService(new TaxiNetworkServiceImpl(this)).build();
            server.start();
            System.out.println("Server started!\n");
            server.awaitTermination();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifyPresenceToNetwork(List<TaxiNetworkInfo> taxiList, TaxiNetworkInfo taxiToNotify) throws InterruptedException {

        for(TaxiNetworkInfo taxi: taxiList){
            ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(taxi.getIpAddress()+":"+taxi.getPortNumber())
                    .usePlaintext()
                    .build();
            TaxiNetworkServiceStub stub = newStub(channel);

            TaxiInformation request = TaxiInformation.newBuilder().setId(taxiToNotify.getId())
                    .setIpAddress(taxiToNotify.getIpAddress())
                    .setPortNumber(taxiToNotify.getPortNumber())
                    .build();

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.notifyNewTaxiPresence(request, new StreamObserver<Empty>() {

                //this hanlder takes care of each item received in the stream
                public void onNext(Empty response) {}

                //if there are some errors, this method will be called
                public void onError(Throwable throwable) {
                    System.out.println("NOTIFY TO NETWORK ERROR: "+ throwable.getMessage());
                }

                //when the stream is completed (the server called "onCompleted") just close the channel
                public void onCompleted() {
                    channel.shutdownNow();
                }

            });

            //you need this. otherwise the method will terminate before that answers from the server are received
            channel.awaitTermination(10, TimeUnit.SECONDS);
        }
    }

    public void startElection(RideRequest rideRequest, RideManagementModule rideMngModule) throws InterruptedException, MqttException {
        if(Taxi.getTaxiNetwork().size() != 0) { //If only 1 taxi, it directly takes the ride
            ExecutorService executor = Executors.newFixedThreadPool(Taxi.getTaxiNetwork().size());

            for (TaxiNetworkInfo taxi : Taxi.getTaxiNetwork()) {
                Runnable electionTask = new ElectionTask(taxi, rideRequest);

                executor.execute(electionTask);
            }

            try {
                executor.awaitTermination(10, TimeUnit.SECONDS);
                executor.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(Taxi.getAvailableForRide().get()){
            Taxi.getAvailableForRide().set(false);
            rideMngModule.accomplishRide(rideRequest);
        } else {
            Taxi.setAvailableForRide(true);
        }
    }

    public void notifyPendingTaxi(TaxiNetworkInfo taxi, List<TaxiNetworkInfo> rechargeQueue) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(taxi.getIpAddress()+":"+taxi.getPortNumber())
                .usePlaintext()
                .build();
        TaxiNetworkServiceStub stub = newStub(channel);

        RechargePermission.Builder requestBuilder = RechargePermission.newBuilder();
        for(TaxiNetworkInfo i: rechargeQueue){
            TaxiInformation info = TaxiInformation.newBuilder().setId(i.getId())
                    .setIpAddress(i.getIpAddress())
                    .setPortNumber(i.getPortNumber())
                    .build();

            requestBuilder.addRechargeQueue(info);
        }
        RechargePermission request = requestBuilder.build();

        //calling the RPC method. since it is asynchronous, we need to define handlers
        stub.notifyPermissionToRecharge(request, new StreamObserver<Empty>() {

            public void onNext(Empty response) {}

            public void onError(Throwable throwable) {
                System.out.println("NOTIFY RECHARGING ERROR: "+ throwable.getMessage());
            }

            public void onCompleted() {
                channel.shutdownNow();
            }

        });

        channel.awaitTermination(10, TimeUnit.SECONDS);
    }

    public boolean askingForRecharge(){
        if(Taxi.getTaxiNetwork().size() != 0) { //If only 1 taxi, it directly takes the ride
            ExecutorService executor = Executors.newFixedThreadPool(Taxi.getTaxiNetwork().size());

            for (TaxiNetworkInfo taxi : Taxi.getTaxiNetwork()) {
                Runnable rechargeTask = new RechargeTask(taxi);

                executor.execute(rechargeTask);
            }

            try {
                executor.awaitTermination(10, TimeUnit.SECONDS);
                executor.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(rechargeReplyCounter == Taxi.getTaxiNetwork().size()){
            return true;
        } else {
            return false;
        }
    }

    private class ElectionTask implements Runnable{
        private final TaxiNetworkInfo taxi;
        private final RideRequest rideRequest;

        public ElectionTask(TaxiNetworkInfo taxi, RideRequest rideRequest) {
            this.taxi = taxi;
            this.rideRequest = rideRequest;
        }

        @Override
        public void run() {

            ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(taxi.getIpAddress() + ":" + taxi.getPortNumber())
                    .usePlaintext()
                    .build();
            TaxiNetworkServiceStub stub = newStub(channel);

            ElectionMessage message = ElectionMessage.newBuilder()
                    .setTaxiId(Taxi.getTaxiNetworkInfo().getId())
                    .setBatteryLvl(Taxi.getBatteryLvl())
                    .setRideRequestId(rideRequest.getId())
                    .setDistance(Taxi.getPosition().getDistance(rideRequest.getStartPosition()))
                    .setStartPosition(rideRequest.getStartPosition().toProtoPosition())
                    .build();

            stub.electionMessage(message, new StreamObserver<ElectionReply>() {
                @Override
                public void onNext(ElectionReply reply) {
                    System.out.println(" - REPLY - \nFROM: " + reply.getTaxiId() + "\nMESSAGE: " + reply.getMessage() + "\n");
                    if (reply.getMessage().equals(ReplyMessage.STOP)) {
                        Taxi.setAvailableForRide(false);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("ELECTION MESSAGE ERROR: " + t.getMessage());
                }

                @Override
                public void onCompleted() {

                }
            });

        }
    }

    private class RechargeTask implements Runnable{
        private final TaxiNetworkInfo taxi;

        public RechargeTask(TaxiNetworkInfo taxi) {
            this.taxi = taxi;
        }

        @Override
        public void run() {

            ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(taxi.getIpAddress() + ":" + taxi.getPortNumber())
                    .usePlaintext()
                    .build();
            TaxiNetworkServiceStub stub = newStub(channel);

            TaxiInformation taxiInfo = TaxiInformation.newBuilder().setId(taxi.getId())
                    .setIpAddress(taxi.getIpAddress())
                    .setPortNumber(taxi.getPortNumber())
                    .build();
            RechargeMessage message = RechargeMessage.newBuilder()
                    .setTaxiInfo(taxiInfo)
                    .setDistrict(Taxi.getPosition().getDistrict())
                    .build();

            stub.rechargeMessage(message, new StreamObserver<RechargeReply>() {
                @Override
                public void onNext(RechargeReply reply) {
                    if(reply.getMessage().equals(ReplyMessage.OK)){
                        incrementRechargeReplyCounter();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("RECHARGE MESSAGE ERROR: " + t.getMessage());
                }

                @Override
                public void onCompleted() {

                }
            });

        }
    }

    public void disconnect(){
        server.shutdown();
        System.out.println(" -- GRPC SERVER DISCONNETTED -- ");
    }

    public RideManagementModule getRideManagementModule() {
        return rideManagementModule;
    }

    public void setRideManagementModule(RideManagementModule rideManagementModule) {
        this.rideManagementModule = rideManagementModule;
    }

    private synchronized void incrementRechargeReplyCounter(){
        rechargeReplyCounter++;
    }
}
