package service;

import db.Connector;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;

public class SecurityController {

    Connection jdbc;

    @RequestMapping("security/put")
    public String putSecurityLogs(@RequestParam(value="hotelId") long hotelId,
                                  @RequestParam(value = "numPeople") int numPeople,
                                  @RequestParam(value = "timestamp") String timestamp) throws ParseException {
        String query = "INSERT INTO analytic VALUES(?, ?, ?);";
        Timestamp date1 = (Timestamp) DateUtils.parseDate(timestamp,
                "yyyy-MM-dd");
        Timestamp sqlDate = new Timestamp(date1.getTime());
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, hotelId);
            p.setInt(2, numPeople);
            p.setTimestamp(3, sqlDate);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }

        return "{\nstatus: 200\n}";
    }

    @RequestMapping("security/get")
    public String putSecurityLogs(@RequestParam(value = "hotelId") String hotelId,
                                  @RequestParam(value = "startTime") String startTime,
                                  @RequestParam (value = "endTime") String endTime) throws SQLException {
        String query = "SELECT timeofday, numOccupants FROM analytic WHERE hotelId = " + hotelId + " AND timeofday >= '" + startTime + "' AND timeofday <= " + endTime + ";";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
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
