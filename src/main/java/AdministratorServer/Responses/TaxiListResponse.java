package AdministratorServer.Responses;

import AdministratorServer.Beans.TaxiBean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxiListResponse {
    private List<TaxiBean> taxiList;

    public TaxiListResponse(List<TaxiBean> taxiList) {
        this.taxiList = taxiList;
    }

    public TaxiListResponse() {}

    public List<TaxiBean> getTaxiList() {
        return taxiList;
    }

    public void addTaxi(TaxiBean taxi){
        this.taxiList.add(taxi);
    }
}
