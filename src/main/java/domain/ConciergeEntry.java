package domain;

import java.sql.Statement;

/**
 * A POJO for entries in the Concierge table
 *
 * @author Collin
 */
public class ConciergeEntry {
    private int index;
    private String status;
    private float charge;
    private String description;

    public ConciergeEntry(int index, String status, float charge, String description) {
        this.index = index;
        this.status = status;
        this.charge = charge;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public String getStatus() {
        return status;
    }

    public float getCharge() {
        return charge;
    }

    public String getDescription() {
        return description;
    }
}
