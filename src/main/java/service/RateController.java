package service;

import db.Connector;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**A controller to handle rates
 * @author Brian Guidarini
 */
@RestController
public class RateController {

    Connection jdbc;

    /**
     * Creates a rate for a room
     * @param rateId The unique id of the rate
     * @param cost The cost of the room
     * @param payPeriod The amount of days you have to pay the cost
     * @param currency The currency of the cost
     * @return A string for the HTTP status
     */
    @RequestMapping("rate/create")
    public String createRate(@RequestParam(value = "rateId") long rateId,
                             @RequestParam(value = "cost") float cost,
                             @RequestParam(value = "payPeriod") int payPeriod,
                             @RequestParam(value = "currency") int currency) {

        String query = "INSERT INTO rate VALUES(?, ?, ?, ?);";
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, rateId);
            p.setFloat(2, cost);
            p.setInt(3, payPeriod);
            p.setInt(4, currency);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Gets every rate that is assigned to a room within a specific hotel
     * @param hotelId
     * The id of the hotel you're looking to view the rooms for
     * @return
     * All of the rate data associated with the hotel
     * @throws SQLException
     */
    @RequestMapping("rate/getAllRatesAssociatedToHotel")
    public String getAllRatesAssociatedWithHotel(@RequestParam(value = "hotelId") long hotelId) throws SQLException {
        String query = "SELECT DISTINCT rate.* FROM rate\n" +
                " INNER JOIN room ON rate.rateID = room.rateId\n" +
                "WHERE hotelID = " + hotelId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);
        while(rs.next()) {
            long rateId = rs.getLong(1);
            float cost = rs.getFloat(2);
            int payPeriod = rs.getInt(3);
            int currency = rs.getInt(4);
            sb.append("rateId: ").append(rateId).append(" Cost: ").append(cost).append(" Pay Period: ").append(payPeriod).append(" Currency ").append(currency).append("\n");
        }
        result = sb.toString();
        return result;
    }


}
