package service;

import domain.ConciergeEntry;
import domain.Employee;

import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

public class ConciergeEntryController {
    // Separate out after merge
    private Connection db;

    private void connectToDB() throws SQLException {
        Properties props = new Properties();
        props.put("user", "user");
        props.put("password", "password");
        db = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?serverTimezone=America/Chicago",
                props);
    }

    public ConciergeEntryController() {
        // maybe not the most appropriate place..
        try {
            connectToDB();
        } catch (SQLException e) {
            System.err.println("error: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    public void createConciergeTab(int customerID) throws SQLException {
        // Also place in global concierge table list..? (Indicates which customer IDs have a concierge tab..?)
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate("create table concierge_%d (entry_id int auto_increment primary key,".formatted(customerID) +
                    "status varchar(255), charge float, description varchar(255))");
        }
    }

    public void addConciergeEntry(int customerID, String status, float charge, String description) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("insert into concierge_%d(status, charge, description) values('%s', '%f', '%s');",
                    customerID, status, charge, description));
        }
    }

    public void updateConciergeEntry(int customerID, String status, float charge, String description, int entryNumber) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("update concierge_%d set status='%s', charge=%f, description='%s' where entry_id=%d;",
                    customerID, status, charge, description, entryNumber));
        }
    }

    public void deleteConciergeEntry(int customerID, int entryNumber) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate("delete from concierge_%d where entry_id=%d".formatted(customerID, entryNumber));
        }
    }

    public LinkedList<ConciergeEntry> readConciergeEntries(int customerID) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            LinkedList<ConciergeEntry> entries = new LinkedList<>();
            // pagination would maybe be better..
            try (ResultSet result = stmt.executeQuery(String.format("select * from concierge_%d;", customerID))) {
                // Any better way? (Maybe just define constants..
                while (result.next()) {
                    ConciergeEntry entry = new ConciergeEntry(result.getInt(1), result.getString(2),
                            result.getFloat(3), result.getString(4));
                    entries.add(entry);
                }
            }

            return entries;
        }
    }

    public ConciergeEntry readConciergeEntry(int customerID, int entryNumber) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            try (ResultSet result = stmt.executeQuery(String.format("select * from concierge_%d where entry_id=%d;", customerID, entryNumber))) {
                if (result.next()) {
                    return new ConciergeEntry(result.getInt(1), result.getString(2),
                            result.getFloat(3), result.getString(4));
                }
            }

            return null;
        }
    }
}
