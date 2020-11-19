package service;

import com.google.gson.Gson;
import domain.Employee;
import domain.Mail;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * A controller for managing DB transactions for the Employee table
 * @author Collin
 */
@RestController 
public class EmployeeController {
    private Connection db;
    private static final Gson GSON = new Gson();

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
     */
    @RequestMapping("/employee/add")
    public String addEmployee(@RequestParam String firstName, @RequestParam String lastName, @RequestParam long hotelID) {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("insert into employee(first_name, last_name, hotel_id) values('%s', '%s', %d);",
                    firstName, lastName, hotelID));
            // Unfortunately has to be done to obtain employee id, since sql generates the id..
            Employee employee = Employee.list().stream()
                    .filter(e -> e.getFirstName().equals(firstName) && e.getLastName().equals(lastName) && e.getHotelID() == hotelID)
                    .findFirst()
                    .get();
            Mail.create(employee.getEmployeeID());
            return "ok";
        } catch (SQLException | NoSuchElementException e) {
            return "error";
        }
    }

    /**
     * Delete an employee from the database
     * @param employeeID
     *  The ID of the employee to delete
     */
    @RequestMapping("/employee/delete")
    public String deleteEmployee(@RequestParam int employeeID) {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("delete from employee where employee_id = %d", employeeID));
            return "ok";
        } catch (SQLException e) {
            return "error";
        }
    }

    /**
     * List all employees in the database
     * @return
     *  A list of employees
     */
    @RequestMapping("/employee/list")
    public String listEmployees() {
        return listEmployees(-1);
    }

    /**
     * List all employees working at a given hotel
     * @param hotelID
     *  The hotel where the employees work
     * @return
     *  A list of employees that work at the hotel
     */
    @RequestMapping("/employee/listHotel")
    public String listEmployees(@RequestParam long hotelID) {
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
            // Any issues with an empty employee list..?
            return GSON.toJson(employees.toArray(), Employee[].class);

            //return employees;
        } catch (SQLException e) {
            return "error";
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
     */
    @RequestMapping("/employee/update")
    public String updateEmployee(@RequestParam String firstName, @RequestParam String lastName, @RequestParam long hotelID, @RequestParam int employeeID) {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("update employee set first_name = '%s', last_name = '%s', hotel_id = %d where employee_id = %d",
                    firstName, lastName, hotelID, employeeID));
            return "ok";
        } catch (SQLException e) {
            return "error";
        }
    }
}