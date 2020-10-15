package domain;

import java.sql.Statement;

public class ConciergeEntry {
    /*
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_IN_PROGRESS = 1;
    public static final int STATUS_CANCELLED = 2;
    public static final int STATUS_DONE = 3;
     */

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
