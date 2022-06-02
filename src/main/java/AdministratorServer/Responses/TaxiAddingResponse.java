package AdministratorServer.Responses;

import AdministratorServer.Beans.TaxiBean;
import SETA.Data.Position;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxiAddingResponse {

    private final Position position;
    private final List<TaxiBean> taxiList;

    public TaxiAddingResponse(Position position, List<TaxiBean> taxiList) {
        this.position = position;
        this.taxiList = taxiList;
    }

    public Position getPosition() {
        return position;
    }

    public List<TaxiBean> getTaxiList() {
        return taxiList;
    }

}
