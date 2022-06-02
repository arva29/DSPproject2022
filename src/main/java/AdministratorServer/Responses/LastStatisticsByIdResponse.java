package AdministratorServer.Responses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LastStatisticsByIdResponse {
    private int id;
    private AverageStatisticsResponse statistics;

    public LastStatisticsByIdResponse(int id, AverageStatisticsResponse statistics) {
        this.id = id;
        this.statistics = statistics;
    }

    public LastStatisticsByIdResponse() {}

    public int getId() {
        return id;
    }

    public AverageStatisticsResponse getStatistics() {
        return statistics;
    }
}
