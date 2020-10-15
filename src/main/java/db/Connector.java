package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    public static Connection getConnection(String username, String password) {
        Connection conn = null;
        try {

            String url = "jdbc:mysql://localhost:3306/hotel?serverTimezone=America/Chicago";

            conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
