package service;

import db.Connector;
import domain.CustomerComplaint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.Date;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * A controller to connect to customer feedback databases
 * @author Jack Piscitello
 */
@RestController
public class CustomerFeedbackController {
    Connection jdbc;

    /**
     * Creates a new review in the database
     * @param name Name of the customer, or anonymous
     * @param rating Rating out of 5 stars
     * @param review Review details
     * @return HTTP Status code
     */
    @RequestMapping("/customerFeedback/createReview")
    public String createReview(@RequestParam(value = "name") String name,
                               @RequestParam(value = "rating") int rating,
                               @RequestParam(value = "review") String review){
        String query = "INSERT INTO review VALUES(?, ?, ?, ?, ?);";
        Date date = new Date();
        java.sql.Timestamp time = new java.sql.Timestamp(date.getTime());
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setString(1, name);
            p.setInt(2, rating);
            p.setString(3, review);
            p.setTimestamp(4, time);
            p.setInt(5,0);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";

    }

    /**
     * Creates a new customer complaint in the database
     * @param customerName Name of the customer
     * @param customerId ID of the customer
     * @param complaint Details about the complaint
     * @param employeeId ID of the employee handling the complaint
     * @param refund Refund amount if any
     * @return HTTP Status code
     * @throws SQLException
     */
    @RequestMapping("/customerFeedback/createComplaint")
    public String createComplaint(@RequestParam(value = "customerName") String customerName,
                                  @RequestParam(value = "customerId") long customerId,
                                  @RequestParam(value = "complaint") String complaint,
                                  @RequestParam(value = "employeeId") long employeeId,
                                  @RequestParam(value = "refund") float refund) throws SQLException {
        String query = "INSERT INTO customerComplaint VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        Date date = new Date();
        java.sql.Timestamp time = new java.sql.Timestamp(date.getTime());
        long id = abs(new Random().nextLong());

        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setString(1, customerName);
            p.setLong(2, customerId);
            p.setString(3, complaint);
            p.setLong(4, employeeId);
            p.setFloat(5, refund);
            p.setTimestamp(6, time);
            p.setString(7,"NEW_COMPLAINT");
            p.setLong(8, id);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Edits an existing customer complaint
     * @param complaintId ID of the complaint being edited
     * @param col Name of the column being edited in the database
     * @param param New value being input
     * @return HTTP Status code
     */
    @RequestMapping("/customerFeedback/editComplaint")
    public String editComplaint(@RequestParam(value = "complaintId") long complaintId,
                                @RequestParam(value = "col") String col,
                                @RequestParam(value = "param") String param){
        String query = "UPDATE customerComplaint SET " + col + " = '" + param + "' where complaintId = " + complaintId + ";";
        try{
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.execute();
        }

        catch(SQLException e){
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * View existing complaints of a particular status
     * @param complaintStatus Status of complaints to be viewed
     * @return List of complaints
     * @throws SQLException
     */
    @RequestMapping("/customerFeedback/viewComplaints")
    public String viewComplaints(@RequestParam(value = "complaintStatus") String complaintStatus) throws SQLException{
        String query = "SELECT * FROM customerComplaint WHERE complaintStatus='" + complaintStatus + "';";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        while(rs.next()) {
            long complaintId = rs.getLong(8);
            long customerId = rs.getLong(2);
            long employeeId = rs.getLong(4);
            String customerName = rs.getString(1);
            float refund = rs.getFloat(5);
            String complaint = rs.getString(3);

            sb.append("Customer Name(ID): ").append(customerName).append("(").append(customerId).append(")\t\tRefund amount: $").append(refund);
            sb.append("\t\tEmployee ID: ").append(employeeId).append("\t\tComplaint ID: ").append(complaintId).append("\n").append(complaint).append("\n\n");

        }
        result = sb.toString();
        return result;
    }

    /**
     * Shows 10 reviews
     * @param pageNumber Page number of the reviews being shown
     * @return List of reviews
     * @throws SQLException
     */
    @RequestMapping("customerFeedback/displayReviews")
    public String displayReviews(@RequestParam(value = "pageNumber") int pageNumber) throws SQLException{
        int startingIndex = 1 + 10*(pageNumber-1);
        String query = "SELECT * FROM review WHERE reviewIndex>="+startingIndex+";";
        String fullQuery = "";

        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();

        String result = "";
        StringBuilder sb = new StringBuilder(result);
        sb.append("=== Reviews Page ").append(pageNumber).append(" ===\n");
        ResultSet rs = stmt.executeQuery(query);

        int count = 0;

        while(rs.next() && count < 10){
            String name = rs.getString(1);
            int rating = rs.getInt(2);
            Timestamp time = rs.getTimestamp(4);
            String review = rs.getString(3);

            sb.append(startingIndex+count).append(")\tCustomer Name: ").append(name).append("\t\tRating: ").append(rating);
            sb.append("/5\t\tReview Time: ").append(time.toString()).append("\nReview: ").append(review).append("\n\n");
            count++;
        }
        String output = sb.toString();
        return output;
    }

    /**
     * Shows relevant information for settling a complaint
     * @param complaintId ID of the complaint of interest
     * @return Settle complaint relevant info
     * @throws SQLException
     */
    @RequestMapping("customerFeedback/showSettleInfo")
    public String showSettleInfo(@RequestParam(value = "complaintId") long complaintId) throws SQLException{
        String query = "SELECT * FROM customerComplaint WHERE complaintId="+complaintId + ";";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        if(rs.next()) {
            long customerId = rs.getLong(2);
            long employeeId = rs.getLong(4);
            String customerName = rs.getString(1);
            float refund = rs.getFloat(5);
            String complaint = rs.getString(3);

            sb.append("Customer Name(ID): ").append(customerName).append("(").append(customerId).append(")\t\tRefund amount: $").append(refund);
            sb.append("\t\tEmployee ID: ").append(employeeId).append("\nComplaint: ").append(complaint).append("\n\n");

        }
        result = sb.toString();
        return result;
    }

    /**
     * Returns the ID of the customer making a complaint
     * @param complaintId ID of the complaint
     * @return The ID of the customer
     * @throws SQLException
     */
    @RequestMapping("/customerFeedback/getCustomerId")
    public String getCustomerId(@RequestParam(value = "complaintId") long complaintId) throws SQLException{
        String query = "SELECT * FROM customerComplaint WHERE complaintId="+complaintId + ";";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        if(rs.next()) {
            long customerId = rs.getLong(2);
            sb.append(customerId);
        }
        result = sb.toString();
        return result;
    }

    /**
     * Gets the refund related to a complaint
     * @param complaintId Id of the complaint of interest
     * @return The refund amount
     * @throws SQLException
     */
    @RequestMapping("/customerFeedback/getRefund")
    public String getRefund(@RequestParam(value = "complaintId") long complaintId) throws SQLException{
        String query = "SELECT * FROM customerComplaint WHERE complaintId="+complaintId + ";";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        if(rs.next()) {
            float refund = rs.getFloat(5);
            sb.append(refund);
        }
        result = sb.toString();
        return result;
    }

    /**
     * Settles a complaint by closing it and adding funds to customer credit
     * @param complaintId Complaint being settled
     * @param customerId ID of the customer
     * @param refund Amount being refunded to the customer
     * @param credit Existing credit in the customer's account
     * @return HTTP status code
     * @throws SQLException
     */
    @RequestMapping("/customerFeedback/settleComplaint")
    public String settleComplaint(@RequestParam(value = "complaintId") long complaintId,
                                  @RequestParam(value = "customerId") long customerId,
                                  @RequestParam(value = "refund") float refund,
                                  @RequestParam(value = "credit") float credit) throws SQLException{
        float newCredit = credit + refund;
        String query1 = "UPDATE customerComplaint SET complaintStatus='CLOSED' where complaintId=" + complaintId + ";";
        String query2 = "UPDATE customer SET customerCredit=" + newCredit + "WHERE customerId=" + customerId + ";";
        try{
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p1 = jdbc.prepareStatement(query1);
            p1.execute();
            PreparedStatement p2 = jdbc.prepareStatement(query2);
            p2.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

}
