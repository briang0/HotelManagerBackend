package service;

import db.Connector;
import domain.ConciergeEntry;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;

/**
 * A controller to handle customers
 * @author Brian Guidarini
 */
@RestController
public class CustomerController {

    Connection jdbc;

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
            boolean paid = rs.getBoolean(8);
            sb.append("billId: ").append(billId).append(" Reservation Id: ").append(reservationId).append(" Check in: ")
                    .append(checkIn).append(" Check out: ").append(checkOut).append(" Rate id: ").append(rateId)
                    .append(" Room id: ").append(roomId).append(" Paid: ").append(paid).append("\n");
        }
        result = sb.toString();
        return result;
    }

    /**
     * Gets all customers that match a given last name
     * @param lastName
     * The last name you're looking for
     * @return
     * A list of all customers with the given last name
     * @throws SQLException
     */
    @RequestMapping("customer/getFromLastName")
    public String getCustomerFromLastName(@RequestParam(value = "lastName") String lastName) throws SQLException {
        String query = "SELECT * FROM customer WHERE lastName = " + "\"" + lastName + "\";";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);
        while(rs.next()) {
            Date dob = rs.getDate(1);
            String firstName = rs.getString(2);
            long customerId = rs.getLong(4);
            sb.append("customer ID: ").append(customerId).append(" First Name: ").append(firstName).append(" Last Name: ").append(lastName).append(" DOB: ").append(dob).append("\n");
        }
        result = sb.toString();
        return result;
    }

    /**
     * Gets the bill containing all charges of a customer
     * @param customerId
     * The id of the customer
     * @return
     * The total charges
     * @throws SQLException
     */
    @RequestMapping("customer/getBill")
    public String getBill(@RequestParam(value = "customerId") long customerId) throws SQLException {
        String reservationTotalQuery = "SELECT DISTINCT rate.cost, rate.payPeriod, rate.currency, checkIn, checkOut, reservation.paid, reservation.reservationId FROM rate\n" +
                " INNER JOIN room ON rate.rateID = room.rateId \n" +
                " INNER JOIN reservation ON reservation.roomId = room.roomId\n" +
                " WHERE reservation.customerId = " + customerId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(reservationTotalQuery);
        float total = 0;
        String output = "";
        StringBuilder sb = new StringBuilder(output);
        while(rs.next()) {
            float cost = rs.getFloat(1);
            java.sql.Date checkIn = rs.getDate(4);
            java.sql.Date checkOut = rs.getDate(5);
            boolean paid = rs.getBoolean(6);
            long reservationId = rs.getLong(7);

            if (!paid) {
                long diff = checkOut.getTime() - checkIn.getTime();
                diff /= 86400000;
                total += cost * diff;
                sb.append("Reservation ID: ").append(reservationId).append(" Charge: ").append(cost).append(" * days ").append(diff).append("\n");
            }
        }
        ConciergeEntryController cec = new ConciergeEntryController();
        LinkedList<ConciergeEntry> conciergeEntries = cec.readConciergeEntries(customerId);
        for (ConciergeEntry c: conciergeEntries) {
            total += c.getCharge();
        }
        sb.append("Total: $").append(total);
        return sb.toString();
    }

}
