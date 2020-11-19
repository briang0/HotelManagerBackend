package domain;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * This class is used to represent a timestamp with a number of people recorded.
 * It is meant to be used for foot traffic analytics
 * @author Brian Guidarini
 */
public class TimeStamp {

    private Timestamp timestamp;
    private int numberOfPeople;

    /**
     * Constructor
     * @param timestamp
     * The timestamp of the reading
     * @param numberOfPeople
     * The number of people read
     */
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