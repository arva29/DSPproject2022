package SETA.Data;

import java.sql.Timestamp;

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
