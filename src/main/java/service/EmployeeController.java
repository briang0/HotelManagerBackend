package service;

import domain.Employee;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

@RestController
@RequestMapping("/employee")
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

    @PostMapping(path="/add")
    public @ResponseBody String addEmployee(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam int hotelID
    ) {
        System.out.println("Adding employee: " + firstName);

        try (Statement stmt = db.createStatement()) {
            //should actually create an employee id
            //(presumably use primary key + autoincrement
            //stmt.executeUpdate(String.format("INSERT INTO employee(first_name,last_name,hotel_id) values('%s', '%s', %d));",
            stmt.executeUpdate(String.format("insert into employee(first_name, last_name, hotel_id) values('%s', '%s', %d);",
                    firstName, lastName, hotelID));
        } catch (SQLException e) {
            return "failed: " + e.getMessage();
        }

        return "add successful";
    }

    @PostMapping(path="/delete")
    public String deleteEmployee(@RequestParam int employeeID) {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("delete from employee where employee_id = %d", employeeID));
        } catch (SQLException e) {
            return "failed: " + e.getMessage();
        }

        return "success";
    }

    // Return a list of employee objects
    @GetMapping(path="/list")
    public LinkedList<Employee> listEmployees() {
        return getEmployees(-1);
    }

    @GetMapping(path="/list/{hotel_id}")
    public LinkedList<Employee> listEmployees(@PathVariable("hotel_id") int hotelID) {
        return getEmployees(hotelID);
    }

    private LinkedList<Employee> getEmployees(int hotelID) {
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
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return new LinkedList<>();
        }
    }

    // Maybe make a wrapper for this sql statement..
    @PostMapping(path="/update")
    public String updateEmployee(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam int hotelID,
            @RequestParam int employeeID
    ) {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("update employee set first_name = '%s', last_name = '%s', hotel_id = %d where employee_id = %d",
                    firstName, lastName, hotelID, employeeID));
        } catch (SQLException e) {
            return "failed: " + e.getMessage();
        }

        return "success";
    }
}