package SETA;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.Position;
import com.example.grpc.TaxiNetworkServiceGrpc.*;
import com.example.grpc.TaxiNetworkServiceOuterClass.*;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

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

        ElectionReply reply;

        if(Taxi.getAvailableForRide().get()) { //NOT INVOLVED IN AN OTHER RIDE OR RECHARGE PROCESS
            if (new Position(request.getStartPosition()).getDistrict() == Taxi.getPosition().getDistrict()) { //CHECK IF REQUEST IS FROM SAME DISTRICT
                if (request.getDistance() > Taxi.getPosition().getDistance(new Position(request.getStartPosition()))) { //DISTANCE COMPARISON
                    reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                } else if (request.getDistance() < Taxi.getPosition().getDistance(new Position(request.getStartPosition()))) {
                    reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                } else {
                    if (request.getBatteryLvl() > Taxi.getBatteryLvl()){    //BATTERY LEVEL COMPARISON
                        reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                    } else if (request.getBatteryLvl() < Taxi.getBatteryLvl()){
                        reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                    } else {    //ID COMPARISON
                        //System.out.println("ID REQ: " + request.getTaxiId() + " MY ID: " + Taxi.getTaxiNetworkInfo().getId());
                        if(request.getTaxiId() > Taxi.getTaxiNetworkInfo().getId()){
                            reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                        } else {
                            reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.STOP).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
                        }
                    }
                }
            }  else {
                reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
            }
        } else {
            reply = ElectionReply.newBuilder().setRideRequestId(request.getRideRequestId()).setMessage(ReplyMessage.OK).setTaxiId(Taxi.getTaxiNetworkInfo().getId()).build();
        }

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void rechargeMessage(RechargeMessage request, StreamObserver<RechargeReply> responseObserver) {
        RechargeReply reply;

        if(Taxi.getAvailableForRide().get()){ //If free for ride not asking for recharge
            reply = RechargeReply.newBuilder().setMessage(ReplyMessage.OK).build();
        } else {
            if(request.getDistrict() == Taxi.getPosition().getDistrict()){ //Not free but other district
                if(Taxi.isRecharging()){ //Not free and recharging
                    Taxi.addToRechargeQueue(new TaxiNetworkInfo(request.getTaxiInfo()));
                    reply = RechargeReply.newBuilder().setMessage(ReplyMessage.STOP).build();
                } else { //Not free but occupied in a ride
                    reply = RechargeReply.newBuilder().setMessage(ReplyMessage.OK).build();
                }
            } else {
                reply = RechargeReply.newBuilder().setMessage(ReplyMessage.OK).build();
            }
        }

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void notifyPermissionToRecharge(RechargePermission request, StreamObserver<Empty> responseObserver) {
        if(request.getRechargeQueueList().size() > 0) {
            Taxi.addAllToRechargeQueue(request.getRechargeQueueList());
        }
        try {
            networkCommunicationModule.getRideManagementModule().recharge(true);
        } catch (InterruptedException | MqttException e) {
            e.printStackTrace();
        }
        responseObserver.onCompleted();
    }
}
