package domain;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;

public class TimeTable {
    private long employeeID;
    private long date;
    private long checkIn;
    private long checkOut;

    private enum ClockStatus {
        CLOCK_IN,
        CLOCK_OUT
    };

    public TimeTable(long employeeID, long date, long checkIn, long checkOut) {
        this.employeeID = employeeID;
        this.date = date;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public long getEmployeeID() {
        return employeeID;
    }

    public long getDate() {
        return date;
    }

    public long getCheckIn() {
        return checkIn;
    }

    public long getCheckOut() {
        return checkOut;
    }

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

    public static boolean checkIn(long employeeID) {
        return updateClockStatus(employeeID, ClockStatus.CLOCK_IN);
    }

    public static boolean checkOut(long employeeID) {
        return updateClockStatus(employeeID, ClockStatus.CLOCK_OUT);
    }

    public static long dateFromTimestamp(long timestamp) {
        // A day is 86400 seconds. timestamp % 86400 gives us the amount of seconds past midnight of the current
        // day; subtracting them gives us midnight of today.
        return timestamp - (timestamp % 86400);
    }

    public float hoursWorked(long fromDate, long toDate) {
        return 0f;
    }
}
