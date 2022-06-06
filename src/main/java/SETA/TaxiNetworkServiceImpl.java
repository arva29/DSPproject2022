package SETA;

import AdministratorServer.Beans.TaxiInfo;
import SETA.Data.Taxi;
import com.example.grpc.TaxiNetworkServiceGrpc.*;
import com.example.grpc.TaxiNetworkServiceOuterClass.*;
import io.grpc.stub.StreamObserver;

public class TaxiNetworkServiceImpl extends TaxiNetworkServiceImplBase {
    Taxi taxi;

    public TaxiNetworkServiceImpl(Taxi taxi) {
        this.taxi = taxi;
    }

    @Override
    public void notifyNewTaxiPresence(TaxiInformation request, StreamObserver<Empty> responseObserver) {
        taxi.addTaxiToNetwork(new TaxiInfo(request));

        responseObserver.onCompleted();
    }
}
