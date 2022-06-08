package AdministratorServer.Responses;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.Position;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxiAddingResponse {

    private Position position;
    private List<TaxiNetworkInfo> taxiList;

    public TaxiAddingResponse(Position position, List<TaxiNetworkInfo> taxiList) {
        this.position = position;
        this.taxiList = taxiList;
    }

    public TaxiAddingResponse() {}

    public Position getPosition() {
        return position;
    }

    public List<TaxiNetworkInfo> getTaxiList() {
        return taxiList;
    }

}
