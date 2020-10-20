package service;

import domain.ConciergeEntry;
import domain.Employee;

import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Manage DB transactions related to concierge services.
 * @author Collin
 */
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

    /**
     * Create an empty concierge tab for a customer.
     * @param customerID
     *  The customer's ID
     * @throws SQLException
     *  The system was unable to create the concierge tab.
     */
    public void createConciergeTab(int customerID) throws SQLException {
        // Also place in global concierge table list..? (Indicates which customer IDs have a concierge tab..?)
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("create table concierge_%d (entry_id int auto_increment primary key,", customerID) +
                    "status varchar(255), charge float, description varchar(255))");
        }
    }

    /**
     * Add a new entry to a customer's concierge tab
     * @param customerID
     *  The customer's ID
     * @param status
     *  The desired status
     * @param charge
     *  The charge in US dollars
     * @param description
     *  The description of the charge
     * @throws SQLException
     *  The entry was unable to be added to the concierge tab
     */
    public void addConciergeEntry(int customerID, String status, float charge, String description) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("insert into concierge_%d(status, charge, description) values('%s', '%f', '%s');",
                    customerID, status, charge, description));
        }
    }

    /**
     * Update an entry in a concierge tab for a customer
     * @param customerID
     *  The customer's ID
     * @param status
     *  The new event status
     * @param charge
     *  The new charge for the entry, in US dollars
     * @param description
     *  The new description for the entry
     * @param entryNumber
     *  The entry number to update
     * @throws SQLException
     */
    public void updateConciergeEntry(int customerID, String status, float charge, String description, int entryNumber) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("update concierge_%d set status='%s', charge=%f, description='%s' where entry_id=%d;",
                    customerID, status, charge, description, entryNumber));
        }
    }

    /**
     * Delete an entry from a customer's concierge tab
     * @param customerID
     *  The customer's ID
     * @param entryNumber
     *  The entry number in the tab to delete
     * @throws SQLException
     */
    public void deleteConciergeEntry(int customerID, int entryNumber) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("delete from concierge_%d where entry_id=%d", customerID, entryNumber));
        }
    }

    /**
     * Read all concierge entries in the customers' concierge tab
     * @param customerID
     *  The customer's ID
     * @return
     *  A list of concierge entries
     * @throws SQLException
     *  If the entries were unable to be read
     */
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

    /**
     * Read a specific concierge entry from a customer's concierge tab
     * @param customerID
     *  The customer's ID
     * @param entryNumber
     *  The number of the concierge entry
     * @return
     *  The concierge entry in question
     * @throws SQLException
     *  If the entry was unable to be read
     */
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
