package SETA;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.Position;
import com.example.grpc.TaxiNetworkServiceGrpc.*;
import com.example.grpc.TaxiNetworkServiceOuterClass.*;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.Timestamp;

public class TaxiNetworkServiceImpl extends TaxiNetworkServiceImplBase {
    private final NetworkCommunicationModule networkCommunicationModule;

    public TaxiNetworkServiceImpl(NetworkCommunicationModule networkCommunicationModule) {
        this.networkCommunicationModule = networkCommunicationModule;
    }

    @Override
    public void notifyNewTaxiPresence(TaxiInformation request, StreamObserver<Empty> responseObserver) {
        Taxi.addTaxiToNetwork(new TaxiNetworkInfo(request));

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }

    @Override
    public void electionMessage(ElectionMessage request, StreamObserver<ElectionReply> responseObserver) {

        System.out.println("------ Replied to " + request.getTaxiId() + " - Request " + request.getRideRequestId());
        ElectionReply reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();


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
        }

        if(reply.getMessage().equals(ReplyMessage.OK) && !Taxi.rideAlreadyAccomplished(request.getRideRequestId())){
            Taxi.addToAccomplishedRide(request.getRideRequestId());
        }

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

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

    @Override
    public void notifyTaxiLeavingNetwork(TaxiLeaving request, StreamObserver<Empty> responseObserver) {
        Taxi.removeTaxiFromNetwork(request.getId());

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }
}
