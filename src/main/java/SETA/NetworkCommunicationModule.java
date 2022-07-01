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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.grpc.TaxiNetworkServiceGrpc.newStub;

/**
 * Class to manage the gRPC communication between taxis.
 */
public class NetworkCommunicationModule extends Thread{
    private Server server;
    private RideManagementModule rideManagementModule;
    private int rechargeReplyCounter = 0;
    private int replies = 0;
    private static final Object lock = new Object();

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

    /**
     * Method to notify the entrance of the taxi into the network. It sends a message to all the other taxis with its information
     */
    public void notifyPresenceToNetwork() throws InterruptedException {

        for(TaxiNetworkInfo taxi: Taxi.getTaxiNetwork()){
            ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(taxi.getIpAddress()+":"+taxi.getPortNumber())
                    .usePlaintext()
                    .build();
            TaxiNetworkServiceStub stub = newStub(channel);

            TaxiInformation request = TaxiInformation.newBuilder().setId(Taxi.getTaxiNetworkInfo().getId())
                    .setIpAddress(Taxi.getTaxiNetworkInfo().getIpAddress())
                    .setPortNumber(Taxi.getTaxiNetworkInfo().getPortNumber())
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

    /**
     * Sends a message to all the taxi in the network to notify that it is leaving the network
     */
    public void notifyLeavingNetwork() throws InterruptedException {

        for(TaxiNetworkInfo taxi: Taxi.getTaxiNetwork()){
            ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(taxi.getIpAddress()+":"+taxi.getPortNumber())
                    .usePlaintext()
                    .build();
            TaxiNetworkServiceStub stub = newStub(channel);

            TaxiLeaving message = TaxiLeaving.newBuilder().setId(Taxi.getTaxiNetworkInfo().getId()).build();

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.notifyTaxiLeavingNetwork(message, new StreamObserver<Empty>() {

                //this hanlder takes care of each item received in the stream
                public void onNext(Empty response) {}

                //if there are some errors, this method will be called
                public void onError(Throwable throwable) {
                    System.out.println("NOTIFY TO LEAVING ERROR: "+ throwable.getMessage());
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

    /**
     * Starts the election. It launches a thread for each other taxis in the network and after all threads have closed, if the
     * Taxi field eligible is still true, the taxi take charge of the ride otherwise it will be free for another ride.
     * @param rideRequest ride request for the election
     * @param rideMngModule module that handle mqtt communication
     */
    public void startElection(RideRequest rideRequest, RideManagementModule rideMngModule) throws InterruptedException, MqttException {

        Taxi.setCurrentElection(rideRequest.getId());
        Taxi.setInElection(true);

        setReplies(Taxi.getTaxiNetwork().size());

        if(Taxi.getTaxiNetwork().size() != 0){ //If only 1 taxi, it directly takes the ride
            CustomThreadPool pool = new CustomThreadPool();

            for (TaxiNetworkInfo taxi : Taxi.getTaxiNetwork()) {
                pool.addThread(new ElectionTask(taxi, rideRequest));
            }

            pool.run();
        }

        //Waiting for all responses from threads
        waitForAllResponses();

        Taxi.setInElection(false);

        if(Taxi.isEligible()){ //If eligible at the end of the election the Taxi take the ride
            Taxi.setFree(false);
            Taxi.addToAccomplishedRide(rideRequest.getId());
            try {
                rideMngModule.accomplishRide(rideRequest);
            } catch (InterruptedException | MqttException e) {
                e.printStackTrace();
            }
        } else {
            Taxi.setEligible(true);
            Taxi.setCurrentElection(-1);
            Taxi.notifyEligibility();
        }

    }

    /**
     * Notify the first taxi in the recharging queue that now it has access to the recharging station
     * @param taxi Taxi to notify
     */
    public void notifyPendingTaxi(TaxiNetworkInfo taxi) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(taxi.getIpAddress()+":"+taxi.getPortNumber())
                .usePlaintext()
                .build();
        TaxiNetworkServiceStub stub = newStub(channel);

        RechargePermission request = RechargePermission.newBuilder().build();

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

    /**
     * Launches a thread for each taxi in the network to ask them if it can access the recharging station of its district
     * @return true if the taxi can access the recharging station
     */
    public boolean askingForRecharge() throws InterruptedException {
        Taxi.setAskingForRecharging(true);
        List<TaxiNetworkInfo> networkSnapshot = Taxi.getTaxiNetwork();

        setReplies(networkSnapshot.size());

        if(networkSnapshot.size() != 0) { //If only 1 taxi, it directly takes the ride
            CustomThreadPool pool = new CustomThreadPool();

            for (TaxiNetworkInfo taxi : networkSnapshot) {
                RechargeTask rechargeTask = new RechargeTask(taxi);

                pool.addThread(rechargeTask);
            }

            pool.run();
        }

        waitForAllResponses();

        boolean value = (rechargeReplyCounter == networkSnapshot.size());

        rechargeReplyCounter = 0;
        return value;
    }

    /**
     * Thread to send election messages to other taxi in the network via gRPC. The thread creates the message and sends it to
     * the taxi specified in the constructor, then it waits for the response. If the response is OK nothing happen, otherwise
     * if the response is STOP the taxi will set itself as not eligible anymore (It will not take charge of the ride).
     */
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

                    System.out.println(" - REPLY " + reply.getRideRequestId() + " - \nFROM: " + reply.getTaxiId() + "\nMESSAGE: " + reply.getMessage() + "\n");

                    if (reply.getMessage().equals(ReplyMessage.STOP)) {
                       Taxi.setEligible(false); //Replace true value with false one
                    }

                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("ELECTION MESSAGE ERROR: " + t.getMessage() + " - " + Arrays.toString(t.getStackTrace()));
                }

                @Override
                public void onCompleted() {
                    addReplies();
                    if(getReplies() == 0) notifyResponses();
                }
            });
        }
    }

    /**
     * Thread to send message asking for permission to recharge from other taxi in the network via gRPC. The thread creates the
     * message and sends it to the taxi specified in the constructor, then it waits for the response. If the response is OK it
     * increment a counter and if at the end the counter will be equal to the number of taxi in the network, it means that
     * everyone has responded with OK and the taxi could enter the recharging station
     */
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

            TaxiInformation taxiInfo = TaxiInformation.newBuilder().setId(Taxi.getTaxiNetworkInfo().getId())
                    .setIpAddress(Taxi.getTaxiNetworkInfo().getIpAddress())
                    .setPortNumber(Taxi.getTaxiNetworkInfo().getPortNumber())
                    .build();
            RechargeMessage message = RechargeMessage.newBuilder()
                    .setTaxiInfo(taxiInfo)
                    .setDistrict(Taxi.getPosition().getDistrict())
                    .setTimestamp(new Timestamp(System.currentTimeMillis()).toString())
                    .build();

            stub.rechargeMessage(message, new StreamObserver<RechargeReply>() {
                @Override
                public void onNext(RechargeReply reply) {
                    System.out.println(" - RECHARGE REPLY - \nMESSAGE: " + reply.getMessage() + "\n");
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
                    addReplies();
                    if(getReplies() == 0) notifyResponses();
                }
            });

        }
    }

    /**
     * It shuts down its gRPC server
     */
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

    /**
     * Increment the counter of the OK responses from recharging tasks
     */
    private synchronized void incrementRechargeReplyCounter(){
        rechargeReplyCounter++;
    }

    /**
     * Set the number of replies needed to end the election process
     * @param n number of replies needed
     */
    public synchronized void setReplies(int n){
        replies = n;
    }

    public synchronized int getReplies() {
        return replies;
    }

    public synchronized void addReplies(){
        replies--;
    }

    /**
     * Wait for all the response of an election or mutual exclusion algorithm to arrive
     */
    public static void waitForAllResponses() throws InterruptedException {
        synchronized (lock){
            //System.out.println("... waiting the taxi to be free!");
            lock.wait();
        }
    }

    /**
     * Notify the process waiting for all the responses from other taxis
     */
    public static void notifyResponses() {
        synchronized (lock){
            //System.out.println("Taxi is finally free!");
            lock.notify();
        }
    }
}
