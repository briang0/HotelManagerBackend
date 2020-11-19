package service;

import db.Connector;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;

/**
 * A controller to handle security and analytics
 * @author Brian Guidarini
 */
@RestController
public class SecurityController {

    Connection jdbc;

    /**
     * An endpoint to put security analytics into the database
     * @param hotelId
     * The hotel camera is in
     * @param numPeople
     * The number of people in the shot
     * @param timestamp
     * The timestamp associated to the number of people
     * @return
     * Returns 200 on success, 400 otherwise
     * @throws ParseException
     */
    @RequestMapping("security/put")
    public String putSecurityLogs(@RequestParam(value="hotelId") long hotelId,
                                  @RequestParam(value = "numPeople") int numPeople,
                                  @RequestParam(value = "timestamp") long timestamp) throws ParseException {
        String query = "INSERT INTO analytic VALUES(?, ?, ?);";
        Timestamp date = new Timestamp(timestamp);
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, hotelId);
            p.setTimestamp(2, date);
            p.setInt(3, numPeople);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }

        return "{\nstatus: 200\n}";
    }

    /**
     * Gets security logs in a given period of time for a given hotel
     * @param hotelId
     * The hotelId you're looking for analytics for
     * @param startTime
     * The start time of the search
     * @param endTime
     * The end time of the search
     * @return
     * Returns all analytics matching
     * @throws SQLException
     */
    @RequestMapping("security/get")
    public String getSecurityLogs(@RequestParam(value = "hotelId") long hotelId,
                                  @RequestParam(value = "startTime") String startTime,
                                  @RequestParam (value = "endTime") String endTime) throws SQLException, ParseException {
        String query = "SELECT timeofday, numOccupants FROM analytic WHERE hotelId = ? AND timeofday >= ? AND timeofday <= ?";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        java.util.Date date1 = DateUtils.parseDate(startTime,
                "yyyy-MM-dd HH:mm:ss", "dd/MM-yyyy");
        Date date2 = DateUtils.parseDate(endTime,
                "yyyy-MM-dd HH:mm:ss", "dd/MM-yyyy");
        Timestamp start = new Timestamp(date1.getTime());
        Timestamp end = new Timestamp(date2.getTime());
        PreparedStatement stmt = jdbc.prepareStatement(query);
        stmt.setLong(1, hotelId);
        stmt.setTimestamp(2, start);
        stmt.setTimestamp(3, end);

        ResultSet rs = stmt.executeQuery();
        String result = "";
        StringBuilder sb = new StringBuilder(result);
        while(rs.next()) {
            Timestamp t = rs.getTimestamp(1);
            int numPeople = rs.getInt(2);
            sb.append("Time: ").append(t).append(" Number of people: ").append(numPeople).append("\n");
        }
        result = sb.toString();
        return result;
    }
}
