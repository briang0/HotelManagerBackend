package service;

import com.google.gson.Gson;
import db.Connector;
import domain.TimeTable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Controller for handling time table requests
 * @author Collin
 */
@RestController
public class TimeTableController {
    private Connection db;
    private static final Gson GSON = new Gson();

    public TimeTableController() {
        db = Connector.getConnection("brian", "YuckyP@ssw0rd");
    }

    // Date is not necessary to pass, since it can be obtained via checkIn timestamp
    @RequestMapping("/time/checkin")
    public String checkIn(@RequestParam  long employeeID, @RequestParam long checkIn) {
        long date = TimeTable.dateFromTimestamp(checkIn);
        // If we have a check in without a corresponding check out, then fail.
        // (The catch all for this is to simply see if there are any entries where checkOut = 0 for a given employee)
        try (Statement stmt = db.createStatement()) {
            TimeTable timeEntry = getOpenCheckIn(employeeID);
            if (timeEntry != null) {
                System.out.println(timeEntry.getEmployeeID());
                return "error: employee is still clocked in";
            }
            stmt.executeUpdate(String.format("insert into time_table (employee_id, entry_date, check_in, check_out) " +
                    String.format("values (%d, %d, %d, %d)", employeeID, date, checkIn, 0)));

            return "ok";
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return "error";
        }
    }

    @RequestMapping("/time/checkout")
    public String checkOut(@RequestParam long employeeID, @RequestParam long checkOut) {
        try (Statement stmt = db.createStatement()) {
            // Need to use this time entry alongside 'checkOut' to determine if we need to insert new days --
            // for example, if our checkout is say, two days later.. (a typical mistake)
            // Just take a flat division, floor((checkOut - timeEntry.getCheckIn) / 86400)
            // Insert by iterating over this
            // Secondary case is if we checkout, and the dates are different (checked out >= midnight)
            // Set checkout of prior entry to 11:59:59, create new check in at midnight, & place check out there.
            TimeTable timeEntry = getOpenCheckIn(employeeID);
            if (timeEntry == null) {
                return "error: failed to find a corresponding clock-in";
            }

            // We have a checkin / checkout that span potentially multiple days
            // TODO: Make sure these have a 'where' clause (also needs tested!)
            long checkOutDate = TimeTable.dateFromTimestamp(checkOut);
            if (timeEntry.getDate() != checkOutDate) {
                long entryDaySpan = Math.floorDiv(checkOutDate - timeEntry.getDate(), 86400);

                // Set clock out to 11:59:59 of clock in day
                stmt.executeUpdate(String.format("update time_table set employee_id = %d, entry_date = %d, check_in = %d, check_out = %d;",
                        employeeID, timeEntry.getDate(), timeEntry.getCheckIn(), timeEntry.getDate() + 86400 - 1));

                // Fill in entire day(s) in between
                for (int i = 1; i < entryDaySpan; i++) {
                    long clockIn = timeEntry.getDate() + i*86400;
                    stmt.executeUpdate(String.format("insert into time_table (employee_id, entry_date, check_in, check_out) " +
                            String.format("values (%d, %d, %d, %d)", employeeID, clockIn, clockIn, clockIn + 86400 - 1)));
                }

                // Finally, clock out on the proper day
                stmt.executeUpdate(String.format("insert into time_table (employee_id, entry_date, check_in, check_out) " +
                        String.format("values (%d, %d, %d, %d)", employeeID, timeEntry.getDate() + 86400*entryDaySpan, timeEntry.getDate() + 86400*entryDaySpan,
                                checkOut)));
            } else {
                stmt.executeUpdate(String.format("update time_table set employee_id = %d, entry_date = %d, check_in = %d, check_out = %d where (employee_id = %d and check_out = 0);",
                        employeeID, timeEntry.getDate(), timeEntry.getCheckIn(), checkOut, employeeID));
            }
            return "ok";
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return "error";
        }
    }

    @RequestMapping("/time/entry_range")
    public String getClockEntries(@RequestParam int employeeID, @RequestParam long fromDate, @RequestParam long toDate) throws SQLException {
        LinkedList<TimeTable> entries = new LinkedList<>();
        try (Statement stmt = db.createStatement()) {
            try (ResultSet result = stmt.executeQuery(String.format("select * from time_table where (entry_date between %d and %d and check_out != 0 and employee_id = %d);",
                    fromDate, toDate, employeeID))) {
                while (result.next()) {
                    TimeTable entry = new TimeTable(result.getLong(1), result.getLong(2),
                            result.getLong(3), result.getLong(4));
                    entries.add(entry);
                }
            }
        }

        return GSON.toJson(entries.toArray(), TimeTable[].class);
    }

    /**
     * Obtain the last check in for an employee. There should only ever be one
     * open check in.
     * @param employeeID
     *  The employee to search for
     * @return
     *  null if no such open check in exists, otherwise a time table entry detailing the check in
     */
    private TimeTable getOpenCheckIn(long employeeID) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            try (ResultSet result = stmt.executeQuery(String.format("select * from time_table where (employee_id = %d and check_out = 0);", employeeID))) {
                if (result.next()) {
                    return new TimeTable(result.getLong(1), result.getLong(2),
                            result.getLong(3), result.getLong(4));
                }
            }
        }
        return null;
    }

}
