package AdministratorServer.Beans;

import com.example.grpc.TaxiNetworkServiceOuterClass.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxiInfo {
    private int id;
    private String ipAddress;
    private int portNumber;

    public TaxiInfo() {}

    public TaxiInfo(int id, String ipAddress, int portNumber) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public TaxiInfo(TaxiInformation taxiInformation){
        this.id = taxiInformation.getId();
        this.ipAddress = taxiInformation.getIpAddress();
        this.portNumber = taxiInformation.getPortNumber();
    }

    public int getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
}
