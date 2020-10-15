package service;

import domain.Employee;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

public class EmployeeController {
    private Connection db;

    // Maybe move to a database object?
    private void connectToDB() throws SQLException {
        Properties props = new Properties();
        props.put("user", "user");
        props.put("password", "password");
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

    public void addEmployee(String firstName, String lastName, int hotelID) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            //should actually create an employee id
            //(presumably use primary key + autoincrement
            //stmt.executeUpdate(String.format("INSERT INTO employee(first_name,last_name,hotel_id) values('%s', '%s', %d));",
            stmt.executeUpdate(String.format("insert into employee(first_name, last_name, hotel_id) values('%s', '%s', %d);",
                    firstName, lastName, hotelID));
        }
    }

    public void deleteEmployee(int employeeID) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("delete from employee where employee_id = %d", employeeID));
        }
    }

    public LinkedList<Employee> listEmployees() throws SQLException {
        return listEmployees(-1);
    }

    public LinkedList<Employee> listEmployees(int hotelID) throws SQLException {
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

    public void updateEmployee(String firstName, String lastName, int hotelID, int employeeID) throws SQLException {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("update employee set first_name = '%s', last_name = '%s', hotel_id = %d where employee_id = %d",
                    firstName, lastName, hotelID, employeeID));
        }
    }
}