package AdministratorServer.Responses;

import AdministratorServer.Beans.TaxiInfo;
import SETA.Data.Position;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxiAddingResponse {

    private Position position;
    private List<TaxiInfo> taxiList;

    public TaxiAddingResponse(Position position, List<TaxiInfo> taxiList) {
        this.position = position;
        this.taxiList = taxiList;
    }

    public TaxiAddingResponse() {}

    public Position getPosition() {
        return position;
    }

    public List<TaxiInfo> getTaxiList() {
        return taxiList;
    }

}
