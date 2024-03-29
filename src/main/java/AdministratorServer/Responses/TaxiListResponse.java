package AdministratorServer.Responses;

import AdministratorServer.Beans.TaxiNetworkInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * REST server response to the client for the request of the list of taxi in the city
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxiListResponse {
    private List<TaxiNetworkInfo> taxiList;

    public TaxiListResponse(List<TaxiNetworkInfo> taxiList) {
        this.taxiList = taxiList;
    }

    public TaxiListResponse() {}

    public List<TaxiNetworkInfo> getTaxiList() {
        return taxiList;
    }

    public void addTaxi(TaxiNetworkInfo taxi){
        this.taxiList.add(taxi);
    }
}
