package service;

import domain.Employee;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

/**
 * A controller for managing DB transactions for the Employee table
 * @author Collin
 */
public class EmployeeController {
    private Connection db;

    // Maybe move to a database object?
    private void connectToDB() throws SQLException {
        Properties props = new Properties();
        props.put("user", "brian");
        props.put("password", "YuckyP@ssw0rd");
        db = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?serverTimezone=America/Chicago",
                props);
    }

    public EmployeeController() {
        // maybe not the most appropriate place..
        try {
            connectToDB();
        } catch (SQLException e) {
            System.err.println("error: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Add a new employee to the database
     * @param firstName
     *  The employee's first name
     * @param lastName
     *  The employee's last name
     * @param hotelID
     *  The hotel of the ID to assign the employee
     * @throws SQLException
     *  If the employee was unable to be added
     */
    public void addEmployee(String firstName, String lastName, long hotelID) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            //should actually create an employee id
            //(presumably use primary key + autoincrement
            //stmt.executeUpdate(String.format("INSERT INTO employee(first_name,last_name,hotel_id) values('%s', '%s', %d));",
            stmt.executeUpdate(String.format("insert into employee(first_name, last_name, hotel_id) values('%s', '%s', %d);",
                    firstName, lastName, hotelID));
        }
    }

    /**
     * Delete an employee from the database
     * @param employeeID
     *  The ID of the employee to delete
     * @throws SQLException
     *  If the employee was unable to be deleted
     */
    public void deleteEmployee(int employeeID) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("delete from employee where employee_id = %d", employeeID));
        }
    }

    /**
     * List all employees in the database
     * @return
     *  A list of employees
     * @throws SQLException
     *  If a list of employees could not be produced
     */
    public LinkedList<Employee> listEmployees() throws SQLException {
        return listEmployees(-1);
    }

    /**
     * List all employees working at a given hotel
     * @param hotelID
     *  The hotel where the employees work
     * @return
     *  A list of employees that work at the hotel
     * @throws SQLException
     *  If a list of employees could not be produced from a given hotel
     */
    public LinkedList<Employee> listEmployees(long hotelID) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            LinkedList<Employee> employees = new LinkedList<>();
            // pagination would maybe be better..
            try (ResultSet result = stmt.executeQuery(String.format("select * from employee %s;",
                    (hotelID == -1) ? "" : "where hotel_id = " + hotelID))) {
                // Any better way? (Maybe just define constants..
                while (result.next()) {
                    Employee employee = new Employee(result.getString(2), result.getString(3),
                            result.getInt(1), result.getInt(4));
                    employees.add(employee);
                }
            }

            return employees;
        }
    }

    /**
     * Update an employee entry in the database
     * @param firstName
     *  The new first name of the employee
     * @param lastName
     *  The new last name of the employee
     * @param hotelID
     *  The new hotel to list the employee at
     * @param employeeID
     *  The employee ID to update with this information
     * @throws SQLException
     *  If the employee could not be updated
     */
    public void updateEmployee(String firstName, String lastName, long hotelID, int employeeID) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("update employee set first_name = '%s', last_name = '%s', hotel_id = %d where employee_id = %d",
                    firstName, lastName, hotelID, employeeID));
        }
    }
}