package AdministratorServer.Responses;

import AdministratorServer.Beans.TaxiInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxiListResponse {
    private List<TaxiInfo> taxiList;

    public TaxiListResponse(List<TaxiInfo> taxiList) {
        this.taxiList = taxiList;
    }

    public TaxiListResponse() {}

    public List<TaxiInfo> getTaxiList() {
        return taxiList;
    }

    public void addTaxi(TaxiInfo taxi){
        this.taxiList.add(taxi);
    }
}
