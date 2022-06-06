package SETA;

import AdministratorServer.Beans.TaxiInfo;
import SETA.Data.Taxi;
import com.example.grpc.TaxiNetworkServiceGrpc.*;
import com.example.grpc.TaxiNetworkServiceOuterClass.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.grpc.TaxiNetworkServiceGrpc.newStub;

public class NetworkCommunicationModule extends Thread{
    private int port;
    private Taxi taxi;

    public NetworkCommunicationModule(int port, Taxi taxi){
        this.port = port;
        this.taxi = taxi;
    }

    @Override
    public void run() {
        try {
            io.grpc.Server server = ServerBuilder.forPort(port).addService(new TaxiNetworkServiceImpl(taxi)).build();
            server.start();
            System.out.println("Server started!\n");
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifyPresenceToNetwork(List<TaxiInfo> taxiList, TaxiInfo taxiToNotify) throws InterruptedException {
        for(TaxiInfo taxi: taxiList){
            ManagedChannel channel = ManagedChannelBuilder.forTarget(taxi.getIpAddress()+":"+taxi.getPortNumber()).usePlaintext().build();
            TaxiNetworkServiceStub stub = newStub(channel);

            TaxiInformation request = TaxiInformation.newBuilder().setId(taxiToNotify.getId()).setIpAddress(taxiToNotify.getIpAddress()).setPortNumber(taxiToNotify.getPortNumber()).build();

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.notifyNewTaxiPresence(request, new StreamObserver<Empty>() {

                //this hanlder takes care of each item received in the stream
                public void onNext(Empty response) {
                }

                //if there are some errors, this method will be called
                public void onError(Throwable throwable) {
                    System.out.println("Error! "+throwable.getMessage());
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
}
