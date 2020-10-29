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
 * The controller for functionality related to the hotel rooms, and general hotel management
 * @Author: Brian Guidarini
 */
@RestController
public class Controller {

    Connection jdbc;

    //http://localhost:8080/testPhrase/?phrase="Hello"

    /**
     * Test endpoint, ignore
     * @param phrase
     * @return
     */
    @RequestMapping("/testPhrase")
    public String testWithParam(@RequestParam(value = "phrase") String phrase) {
        return "Phrase received: " + phrase;
    }

    //http://localhost:8080/test

    /**
     * Test endpoint, ignore
     * @return
     */
    @RequestMapping("/test")
    public String getCode() {
        return "Hit the endpoint";
    }

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

        String query = "INSERT INTO reservation VALUES(?, ?, ?, ?, ?, ?, ?);";
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
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";

    }

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
     * An endpoint to create a customer in the database
     * @param dob The date of birth of the customer
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     * @param customerId The unique id of the customer
     * @return Returns an http status code
     * @throws ParseException
     */
    @RequestMapping("customer/create")
    public String createCustomer(@RequestParam(value = "dob") String dob,
                                 @RequestParam(value = "firstName") String firstName,
                                 @RequestParam(value = "lastName") String lastName,
                                 @RequestParam(value = "customerId") long customerId) throws ParseException {

        String query = "INSERT INTO customer VALUES(?, ?, ?, ?);";
        Date date1 = DateUtils.parseDate(dob,
                "yyyy-MM-dd");
        java.sql.Date sqlDate = new java.sql.Date(date1.getTime());
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setDate(1, sqlDate);
            p.setString(2, firstName);
            p.setString(3, lastName);
            p.setLong(4, customerId);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }

        return "{\nstatus: 200\n}";
    }

    /**
     * Endpoint to create a room in the database
     * @param roomNumber The room number within the hotel
     * @param roomId The unique id that represents the room
     * @param houseKeepingStatus False if no one is currently housekeeping, true otherwise
     * @param roomDescription A description of the room. Such as suite, room, or penthouse
     * @param rateId The id of the rate for this room
     * @param hotelId The id of the hotel in which this room resides
     * @return Returns the http status
     */
    @RequestMapping("/room/create")
    public String createRoom(@RequestParam(value = "roomNumber") int roomNumber,
                             @RequestParam(value = "roomId") long roomId,
                             @RequestParam(value = "houseKeepingStatus") boolean houseKeepingStatus,
                             @RequestParam(value = "roomDescription") String roomDescription,
                             @RequestParam(value = "rateId") long rateId,
                             @RequestParam(value = "hotelId") long hotelId) {
        String query = "INSERT INTO room VALUES(?, ?, ?, ?, ?, ?, ?);";
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setDate(1, null);
            p.setInt(2, roomNumber);
            p.setLong(3, roomId);
            p.setBoolean(4, houseKeepingStatus);
            p.setString(5, roomDescription);
            p.setLong(6, rateId);
            p.setLong(7, hotelId);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }

        return "{\nstatus: 200\n}";
    }

    /**
     * Creates a hotel in the database
     * @param hotelId The unique id that describes the hotel
     * @param address The address of the hotel
     * @return Returns the http status of the operation
     */
    @RequestMapping("/hotel/create")
    public String createHotel(@RequestParam(value = "hotelId") long hotelId,
                              @RequestParam(value = "address") String address) {

        String query = "INSERT INTO hotel VALUES(?, ?);";
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, hotelId);
            p.setString(2, address);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Gets all rooms for a given hotel room
     * @param hotelId The unique id of the hotel
     * @return Returns the http status code
     * @throws SQLException
     */
    @RequestMapping("/hotel/get")
    public String getHotelRooms(@RequestParam(value = "hotelId") long hotelId) throws SQLException {
        String query = "SELECT * FROM room WHERE hotelId = " + hotelId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);
        while(rs.next()) {
            int roomNumber = rs.getInt(2);
            long roomId = rs.getLong(3);
            boolean housekeepingStatus = rs.getBoolean(4);
            String roomDescription = rs.getString(5);
            long rateId = rs.getLong(6);
            sb.append("RoomNumber: ").append(roomNumber).append(" roomId: ").append(roomId).append(" House Keeping Status: ").append(housekeepingStatus).append(" Description: ").append(roomDescription).append(" rateId: ").append(rateId).append("\n");
        }
        result = sb.toString();
        return result;
    }

    @RequestMapping("/hotel/getAllHotels")
    public String getAllHotels() throws SQLException {
        String query = "SELECT * FROM hotel";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);
        while(rs.next()) {
            long hotelId = rs.getLong(1);
            String address = rs.getString(2);
            sb.append("HotelId: ").append(hotelId).append(" Address: ").append(address).append("\n");
        }
        result = sb.toString();
        return result;
    }


    /**
     * Gets all reservation of a given customer
     * @param customerId The id of the customer who you are trying to get all reservations for
     * @return Returns the http status
     * @throws SQLException
     */
    @RequestMapping("/customer/getReservations")
    public String getCustomerReservations(@RequestParam(value = "customerId") long customerId) throws SQLException {
        String query = "SELECT * FROM reservation WHERE customerId = " + customerId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);
        while(rs.next()) {
            long billId = rs.getLong(2);
            long reservationId = rs.getLong(3);
            java.sql.Date checkIn = rs.getDate(4);
            java.sql.Date checkOut = rs.getDate(5);
            long rateId = rs.getLong(6);
            long roomId = rs.getLong(7);
            sb.append("billId: ").append(billId).append(" Reservation Id: ").append(reservationId).append(" Check in: ").append(checkIn).append(" Check out: ").append(checkOut).append(" Rate id: ").append(rateId).append(" Room id: ").append(roomId).append("\n");
        }
        result = sb.toString();
        return result;
    }

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
            java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
            if (now.before(checkOut) && now.after(checkIn))
                sb.append("Customer ID: ").append(customerId).append(" Reservation ID: ").append(reservationId)
                        .append(" Check in: ").append(checkIn).append(" Check Out: ").append(checkOut)
                        .append(" rate ID: ").append(rateId).append(" Room ID: ").append(roomId).append("\n");
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

}
