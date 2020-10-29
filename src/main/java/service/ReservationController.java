package service;

import db.Connector;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;

@RestController
public class ReservationController {

    Connection jdbc;

    /**
     * Creates a reservation
     * @param checkIn - The check in time
     * @param checkOut - The checkout time
     * @param reservationId The unique reservation id
     * @param customerId The unique customer id of the customer making the reservation
     * @param rateId The unique rate id associated with the room
     * @param billId The unique bill id assciated with the room
     * @return Returns a string with the HTTP status
     * @throws ParseException
     */
    @RequestMapping("/reservation/create")
    public String createReservation(@RequestParam(value = "checkInDate") String checkIn,
                                    @RequestParam(value = "checkOutDate") String checkOut,
                                    @RequestParam(value = "reservationId") long reservationId,
                                    @RequestParam(value = "customerId") long customerId,
                                    @RequestParam(value = "rateId") long rateId,
                                    @RequestParam(value = "billId") long billId,
                                    @RequestParam(value = "roomId") long roomId) throws ParseException {
        Date date1 = DateUtils.parseDate(checkIn,
                "yyyy-MM-dd HH:mm:ss", "dd/MM-yyyy");
        Date date2 = DateUtils.parseDate(checkOut,
                "yyyy-MM-dd HH:mm:ss", "dd/MM-yyyy");
        java.sql.Date sqld1 = new java.sql.Date(date1.getTime());
        java.sql.Date sqld2 = new java.sql.Date(date2.getTime());

        String query = "INSERT INTO reservation VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, customerId);
            p.setLong(2, billId);
            p.setLong(3, reservationId);
            p.setDate(4, sqld1);
            p.setDate(5, sqld2);
            p.setLong(6, rateId);
            p.setLong(7, roomId);
            p.setBoolean(8, false);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";

    }

    @RequestMapping("reservation/getAllActiveReservationsAssociatedToHotel")
    public String getAllActiveReservationsInHotel(@RequestParam(value = "hotelId") long hotelId) throws SQLException {
        String query = "SELECT reservation.* FROM reservation INNER JOIN room ON reservation.roomId = room.roomId WHERE room.hotelId = " + hotelId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);
        while(rs.next()) {
            long customerId = rs.getLong(1);
            long reservationId = rs.getLong(3);
            java.sql.Date checkIn = rs.getDate(4);
            java.sql.Date checkOut = rs.getDate(5);
            long rateId = rs.getLong(6);
            long roomId = rs.getLong(7);
            boolean paid = rs.getBoolean(8);
            java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
            if (now.before(checkOut) && now.after(checkIn))
                sb.append("Customer ID: ").append(customerId).append(" Reservation ID: ").append(reservationId)
                        .append(" Check in: ").append(checkIn).append(" Check Out: ").append(checkOut)
                        .append(" rate ID: ").append(rateId).append(" Room ID: ").append(roomId)
                        .append(" Paid: ").append(paid).append("\n");
        }
        result = sb.toString();
        return result;
    }

    @RequestMapping("reservation/getAllReservationsAssociatedToHotel")
    public String getAllReservationsInHotel(@RequestParam(value = "hotelId") long hotelId) throws SQLException {
        String query = "SELECT reservation.* FROM reservation INNER JOIN room ON reservation.roomId = room.roomId WHERE room.hotelId = " + hotelId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);
        while(rs.next()) {
            long customerId = rs.getLong(1);
            long reservationId = rs.getLong(3);
            java.sql.Date checkIn = rs.getDate(4);
            java.sql.Date checkOut = rs.getDate(5);
            long rateId = rs.getLong(6);
            long roomId = rs.getLong(7);
            sb.append("Customer ID: ").append(customerId).append(" Reservation ID: ").append(reservationId)
                    .append(" Check in: ").append(checkIn).append(" Check Out: ").append(checkOut)
                    .append(" rate ID: ").append(rateId).append(" Room ID: ").append(roomId).append("\n");
        }
        result = sb.toString();
        return result;
    }

    @RequestMapping("reservation/setWakeupTime")
    public String setWakeupTime(@RequestParam(value = "wakeUpTime") String wakeUpTime,
                                @RequestParam(value = "roomId") long roomId) throws ParseException {
        String query = "UPDATE room SET wakeUpTime = ? WHERE roomId = ?;";
        Date date1 = DateUtils.parseDate(wakeUpTime,
                "yyyy-MM-dd HH:mm:ss", "dd/MM-yyyy");
        java.sql.Date sqld1 = new java.sql.Date(date1.getTime());
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setDate(1, sqld1);
            p.setLong(2, roomId);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    @RequestMapping("reservation/markAsPaid")
    public String markReservationAsPaid(@RequestParam(value = "reservationId") long reservationId) {
        String query = "UPDATE reservation SET paid = true WHERE reservationId = ?;";
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, reservationId);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    @RequestMapping("reservation/markAllAsPaid")
    public String markAllReservationAsPaid(@RequestParam(value = "customerId") long customerId) {
        String query = "UPDATE reservation SET paid = true WHERE customerId = ?;";
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, customerId);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

}
