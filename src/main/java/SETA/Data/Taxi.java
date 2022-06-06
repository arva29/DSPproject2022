package SETA.Data;

import AdministratorServer.Beans.TaxiInfo;

import java.util.List;

public class Taxi {
    private final TaxiInfo taxiInfo;
    private Position position;
    private List<TaxiInfo> taxiNetwork;

    public Taxi(TaxiInfo taxiInfo, Position position, List<TaxiInfo> taxiNetwork) {
        this.taxiInfo = taxiInfo;
        this.position = position;
        this.taxiNetwork = taxiNetwork;
    }

    public void addTaxiToNetwork(TaxiInfo newTaxi){
        taxiNetwork.add(newTaxi);
    }

    public TaxiInfo getTaxiInfo() {
        return taxiInfo;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<TaxiInfo> getTaxiNetwork() {
        return taxiNetwork;
    }
}
