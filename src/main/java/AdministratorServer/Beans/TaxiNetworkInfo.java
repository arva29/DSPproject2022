package AdministratorServer.Beans;

import com.example.grpc.TaxiNetworkServiceOuterClass.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains all the taxi's information useful for communication, such as id, ip address and port number
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxiNetworkInfo {
    private int id;
    private String ipAddress;
    private int portNumber;

    public TaxiNetworkInfo() {}

    public TaxiNetworkInfo(int id, String ipAddress, int portNumber) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public TaxiNetworkInfo(TaxiInformation taxiInformation){
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
