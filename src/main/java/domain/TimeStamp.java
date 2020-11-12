package domain;

import java.sql.Date;
import java.sql.Timestamp;

public class TimeStamp {

    private Timestamp timestamp;
    private int numberOfPeople;

    public TimeStamp(Timestamp timestamp, int numberOfPeople) {
        this.timestamp = timestamp;
        this.numberOfPeople = numberOfPeople;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

}