package service;

import db.Connector;
import domain.MaintenanceReq;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;

/**
 * A controller to connect to maintenance request database
 * @author Jack Piscitello
 */
@RestController
public class MaintenanceRequestController {
    Connection jdbc;

    /**
     * Endpoint to create a new maintenance request
     * @param subject Maintenance request subject line
     * @param description Description of the request
     * @param cost Cost, if known, of fixing the problem
     * @param reqId ID of the maintenance request
     * @param status Status code for the maintenance request
     * @param comments Any additional information about the request
     * @return HTTP status code
     * @throws ParseException
     */
    @RequestMapping("/maintenanceRequest/create")
    public String createMaintenanceRequest(@RequestParam(value = "subject") String subject,
                                           @RequestParam(value = "description") String description,
                                           @RequestParam(value = "cost") float cost,
                                           @RequestParam(value = "reqId") long reqId,
                                           @RequestParam(value = "status") MaintenanceReq.Status status,
                                           @RequestParam(value = "comments") String comments) throws ParseException {

        String query = "INSERT INTO maintReq VALUES(?, ?, ?, ?, ?, ?, ?);";
        Date date = new Date();
        java.sql.Timestamp time = new java.sql.Timestamp(date.getTime());
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setString(1, subject);
            p.setString(2, description);
            p.setTimestamp(3, time);
            p.setFloat(4, cost);
            p.setLong(5, reqId);
            p.setString(6, status.name());
            p.setString(7, comments);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Endpoint to view an existing maintenance request
     * @param reqId ID of the maintenance request
     * @return Details of the requested maintenance request
     * @throws SQLException
     */
    @RequestMapping("/maintenanceRequest/get")
    public String viewMaintenanceRequest(@RequestParam(value = "reqId") long reqId) throws SQLException{

        String query = "SELECT * FROM maintReq where reqId =" + reqId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        if(rs.next()) {
            String subject = rs.getString(1);
            String description = rs.getString(2);
            Date time = rs.getDate(3);
            float cost = rs.getFloat(4);
            String status = rs.getString(6);
            String comments = rs.getString(7);

            sb.append("Subject: ").append(subject).append("\nDescription: ").append(description).append("\nDate created: ");
            sb.append(time).append("\nCost of repair: ").append(cost).append("\nRequest ID: ").append(reqId);
            sb.append("\nStatus: ").append(status).append("\nComments: ").append(comments).append("\n");
            result = sb.toString();
        }
        return result;
    }

    /**
     * Endpoint to edit an existing maintenance request
     * @param reqId ID for the maintenance request
     * @param col Column identifier
     * @param param Value of the parameter being changed
     * @return HTTP status code
     */
    @RequestMapping("/maintenanceRequest/edit")
    public String editMaintenanceRequest(@RequestParam(value = "reqId") long reqId,
                                         @RequestParam(value = "col") String col,
                                         @RequestParam(value = "p") String param){

        String query = "UPDATE maintReq SET " + col + " = '" + param + "' where reqId = " + reqId;
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
     * Endpoint to delete an existing maintenance request
     * @param reqId Id for the maintenance request
     * @return HTTP status code
     */
    @RequestMapping("/maintenanceRequest/delete")
    public String deleteMaintenanceRequest(@RequestParam(value = "reqId") long reqId){
        String query = "DELETE FROM maintReq WHERE reqId = " + reqId;
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

}
