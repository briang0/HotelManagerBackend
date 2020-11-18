package domain;

import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * API for interacting with the employee's work time
 *
 * @author Collin
 */
public class TimeTable {
    private static final Gson GSON = new Gson();
    private long employeeID;
    private long date;
    private long checkIn;
    private long checkOut;

    /**
     * Denotes status of employee at this time
     */
    private enum ClockStatus {
        CLOCK_IN,
        CLOCK_OUT
    }

    /**
     * Initialize employee time table entry
     * @param employeeID
     *  ID of the employee
     * @param date
     *  Date employee is working (starting at midnight)
     * @param checkIn
     *  Unix timestamp employee checked in
     * @param checkOut
     *  Unix timestamp employee checked out
     */
    public TimeTable(long employeeID, long date, long checkIn, long checkOut) {
        this.employeeID = employeeID;
        this.date = date;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public long getEmployeeID() {
        return employeeID;
    }

    /**
     * Get date as unix timestamp of time entry
     * @return
     *  Unix timestamp of time entry, midnight of that day
     */
    public long getDate() {
        return date;
    }

    public long getCheckIn() {
        return checkIn;
    }

    public long getCheckOut() {
        return checkOut;
    }

    /**
     * Check an employee in or out
     * @param employeeID
     *  The employee to log
     * @param status
     *  The clock status of the employee
     * @return
     *  If the clock event was successful
     */
    private static boolean updateClockStatus(long employeeID, ClockStatus status) {
        String clockOp = (status == ClockStatus.CLOCK_IN) ? "checkIn" : "checkOut";
        long timestamp = Instant.now().getEpochSecond();

        RestTemplate request = new RestTemplate();
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/time/" + clockOp.toLowerCase())
                .queryParam("employeeID", employeeID)
                .queryParam(clockOp, timestamp)
                .build();

        String resp = request.getForEntity(uri.toUri(), String.class).getBody();
        return resp.equals("ok");
    }

    /**
     * Clock in an employee
     * @param employeeID
     *  The employee to check in
     * @return
     *  Success of the operation
     */
    public static boolean checkIn(long employeeID) {
        return updateClockStatus(employeeID, ClockStatus.CLOCK_IN);
    }

    /**
     * Clock out an employee
     * @param employeeID
     *  The employee to clock out
     * @return
     *  The success of the operation
     */
    public static boolean checkOut(long employeeID) {
        return updateClockStatus(employeeID, ClockStatus.CLOCK_OUT);
    }

    /**
     * Take a unix timestamp, and strip all extra seconds such that the timestamp
     * represents midnight of that day.
     * @param timestamp
     *  The timestamp to modify
     * @return
     *  A timestamp representing midnight of the provided timestamp's day
     */
    public static long dateFromTimestamp(long timestamp) {
        // A day is 86400 seconds. timestamp % 86400 gives us the amount of seconds past midnight of the current
        // day; subtracting them gives us midnight of today.
        return timestamp - (timestamp % 86400);
    }

    /**
     * Obtain all clock entries for a given date range
     * @param fromDate
     *  The start time, inclusive
     * @param toDate
     *  The end time, inclusive
     * @return
     *  A list of clock entries within the range, if any
     */
    private static LinkedList<TimeTable> getClockEntries(long fromDate, long toDate) {
        RestTemplate request = new RestTemplate();
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/time/entry_range")
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate)
                .build();

        String resp = request.getForEntity(uri.toUri(), String.class).getBody();
        TimeTable[] entries = GSON.fromJson(resp, TimeTable[].class);

        return new LinkedList<>(Arrays.asList(entries));
    }

    /**
     * Get the time worked
     * @param fromDate
     * @param toDate
     * @return
     */
    public static long timeWorked(long fromDate, long toDate) {
        LinkedList<TimeTable> entries = getClockEntries(fromDate, toDate);

        return entries.stream()
                .map(x -> x.getCheckOut() - x.getCheckIn())
                .reduce(Long::sum)
                .orElse(0L);
    }
}
