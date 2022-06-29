package SETA;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.Position;
import com.example.grpc.TaxiNetworkServiceGrpc.*;
import com.example.grpc.TaxiNetworkServiceOuterClass.*;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.Timestamp;

/**
 * Class to manage responses of the proto messages coming from gRPC communication with other taxis
 */
public class TaxiNetworkServiceImpl extends TaxiNetworkServiceImplBase {
    private final NetworkCommunicationModule networkCommunicationModule;

    public TaxiNetworkServiceImpl(NetworkCommunicationModule networkCommunicationModule) {
        this.networkCommunicationModule = networkCommunicationModule;
    }

    /**
     * Adds the taxi to the list of taxi in the network and responds with an empty message
     */
    @Override
    public void notifyNewTaxiPresence(TaxiInformation request, StreamObserver<Empty> responseObserver) {
        Taxi.addTaxiToNetwork(new TaxiNetworkInfo(request));

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }

    /**
     * If the taxi from which the request arrives doesn't match all the requisites to take charge of the ride, it responds
     * with STOP, otherwise it responds with OK
     */
    @Override
    public void electionMessage(ElectionMessage request, StreamObserver<ElectionReply> responseObserver) {

        /*ElectionReply reply;

        if(Taxi.isFree()) { //NOT INVOLVED IN AN OTHER RIDE OR RECHARGE PROCESS
            if(!Taxi.isAskingForRecharging()) {
                if (request.getRideRequestId() == Taxi.getCurrentElection() || Taxi.getCurrentElection() == -1) {
                    if (new Position(request.getStartPosition()).getDistrict() == Taxi.getPosition().getDistrict()) { //CHECK IF REQUEST IS FROM SAME DISTRICT
                        if (!Taxi.rideAlreadyAccomplished(request.getRideRequestId())) { //REQUEST FOR RIDE THAT THIS TAXI HAS ALREADY TAKEN
                            if (request.getDistance() > Taxi.getPosition().getDistance(new Position(request.getStartPosition()))) { //DISTANCE COMPARISON
                                reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                            } else if (request.getDistance() < Taxi.getPosition().getDistance(new Position(request.getStartPosition()))) {
                                reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                            } else {
                                if (request.getBatteryLvl() > Taxi.getBatteryLvl()) {    //BATTERY LEVEL COMPARISON
                                    reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                                } else if (request.getBatteryLvl() < Taxi.getBatteryLvl()) {
                                    reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                                } else {    //ID COMPARISON
                                    //System.out.println("ID REQ: " + request.getTaxiId() + " MY ID: " + Taxi.getTaxiNetworkInfo().getId());
                                    if (request.getTaxiId() > Taxi.getTaxiNetworkInfo().getId()) {
                                        reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                                    } else {
                                        reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                                    }
                                }
                            }
                        } else {
                            reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                        }
                    } else {
                        reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                    }
                } else {
                    reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                }
            } else {
                reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
            }
        } else {
            //if(Taxi.rideAlreadyAccomplished(request.getRideRequestId())) {
            //    reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
            //} else {
            reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
            //}
        }*/

        ElectionReply reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();

        //System.out.println("\nTaxi.isFree - " + Taxi.isFree() + "\n!Taxi.isAskingForRecharging - " + !Taxi.isAskingForRecharging() +
        //        "\nReqID - CurrElect - " + request.getRideRequestId() + " - " +  Taxi.getCurrentElection() + "\n!Taxi.rideAlreadyAccomplished - " +
        //        !Taxi.rideAlreadyAccomplished(request.getRideRequestId()) + "\n");

        if(Taxi.isFree() && !Taxi.isAskingForRecharging() && (request.getRideRequestId() == Taxi.getCurrentElection() || Taxi.getCurrentElection() == -1)) {
            if (new Position(request.getStartPosition()).getDistrict() == Taxi.getPosition().getDistrict()) { //CHECK IF REQUEST IS FROM SAME DISTRICT
                if (!Taxi.rideAlreadyAccomplished(request.getRideRequestId())) { //REQUEST FOR RIDE THAT THIS TAXI HAS ALREADY TAKEN
                    if (request.getDistance() > Taxi.getPosition().getDistance(new Position(request.getStartPosition()))) { //DISTANCE COMPARISON
                        reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                    } else if (request.getDistance() == Taxi.getPosition().getDistance(new Position(request.getStartPosition()))) {
                        if (request.getBatteryLvl() < Taxi.getBatteryLvl()) {
                            reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                        } else if (request.getBatteryLvl() == Taxi.getBatteryLvl()) {    //ID COMPARISON
                            //System.out.println("ID REQ: " + request.getTaxiId() + " MY ID: " + Taxi.getTaxiNetworkInfo().getId());
                            if (request.getTaxiId() < Taxi.getTaxiNetworkInfo().getId()) {
                                reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                            }
                        }
                    }
                } else {
                    reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                }
            }
        } else {
            if (Taxi.getCurrentElection() == request.getRideRequestId()){
                reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
            }
        }

        //if(reply.getMessage().equals(ReplyMessage.OK) && !Taxi.rideAlreadyAccomplished(request.getRideRequestId())){
        //    Taxi.addToAccomplishedRide(request.getRideRequestId());
        //}

        responseObserver.onNext(reply);
        responseObserver.onCompleted();

        //System.out.println("------ Replied to " + request.getTaxiId() + " - Request " + request.getRideRequestId());
    }

    /**
     * If the taxi is already asking for recharging, or it is already in a recharging process it replies with STOP and add the taxi
     * from which the request arrives to the recharging queue.
     */
    @Override
    public void rechargeMessage(RechargeMessage request, StreamObserver<RechargeReply> responseObserver) {

        RechargeReply reply = RechargeReply.newBuilder().setMessage(ReplyMessage.OK).build();

        if(request.getDistrict() == Taxi.getPosition().getDistrict()){ //Other district
            if((Taxi.isAskingForRecharging() && Taxi.getAskingForRecharging().getTimestamp().before(Timestamp.valueOf(request.getTimestamp()))) || Taxi.isRecharging()){
                Taxi.addToRechargeQueue(new TaxiNetworkInfo(request.getTaxiInfo()));
                reply = RechargeReply.newBuilder().setMessage(ReplyMessage.STOP).build();
            }
        }

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    /**
     * The taxi could enter the recharging process without starting an election
     */
    @Override
    public void notifyPermissionToRecharge(RechargePermission request, StreamObserver<Empty> responseObserver) {

        try {
            networkCommunicationModule.getRideManagementModule().recharge(true);
        } catch (InterruptedException | MqttException e) {
            e.printStackTrace();
        }
        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }

    /**
     * The taxi remove the taxi from which the message arrives from the list of the taxi in the network
     */
    @Override
    public void notifyTaxiLeavingNetwork(TaxiLeaving request, StreamObserver<Empty> responseObserver) {
        Taxi.removeTaxiFromNetwork(request.getId());

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }
}
