package SETA.Data;

import java.sql.Timestamp;

/**
 * Flag to understand if the taxi is asking for recharging or not, with the timestamp of the request associated
 */
public class RechargingTrigger {
    private Boolean value;
    private Timestamp timestamp;

    public RechargingTrigger(Boolean value) {
        this.value = value;
        this.timestamp = null;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
