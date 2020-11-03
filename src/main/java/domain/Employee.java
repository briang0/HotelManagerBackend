package domain;

import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.Entity;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * POJO for the Employee table
 * @author Collin
 */
public class Employee {
    private String firstName;
    private String lastName;
    private int employeeID;
    private long hotelID;
    private static final Gson GSON = new Gson();

    public Employee(String firstName, String lastName, int employeeID, long hotelID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeID = employeeID;
        this.hotelID = hotelID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Add an employee to the database
     * @param firstName
     *  Employee's first name
     * @param lastName
     *  Employee's last name
     * @param hotelID
     *  The hotel the employee is employed
     * @return
     *  Whether or not the operation was successful
     */
    public static boolean add(String firstName, String lastName, long hotelID) {
        UriComponents url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/employee/add")
                .queryParam("firstName", firstName)
                .queryParam("lastName", lastName)
                .queryParam("hotelID", hotelID)
                .build();

        RestTemplate request = new RestTemplate();
        return request.getForEntity(url.toUri(), String.class).getBody().equals("ok");
    }

    /**
     * Delete an employee from the database
     * @param employeeID
     *  The employee ID to delete
     * @return
     *  Whether or not the operation was successful
     */
    public static boolean delete(int employeeID) {
        RestTemplate request = new RestTemplate();
        return request.getForEntity("http://localhost:8080/employee/delete?employeeID=" + employeeID, String.class).getBody().equals("ok");
    }

    /**
     * Update the employee
     * @param firstName
     *  New first name
     * @param lastName
     *  New last name
     * @param hotelID
     *  New hotel ID
     * @param employeeID
     *  The employee id representing the employee
     * @return
     *  Whether or not the operation was successful
     */
    public static boolean update(String firstName, String lastName, long hotelID, int employeeID) {
        UriComponents url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/employee/update")
                .queryParam("firstName", firstName)
                .queryParam("lastName", lastName)
                .queryParam("hotelID", hotelID)
                .queryParam("employeeID", employeeID)
                .build();

        RestTemplate request = new RestTemplate();
        return request.getForEntity(url.toUri(), String.class).getBody().equals("ok");
    }

    /**
     * List all the employee's employed at a hotel
     * @param hotelID
     *  The hotel to list
     * @return
     *  List of employees
     */
    public static LinkedList<Employee> listHotel(long hotelID) {
        RestTemplate request = new RestTemplate();
        String json = request.getForEntity("http://localhost:8080/employee/listHotel?hotelID=" + hotelID, String.class).getBody();

        return new LinkedList<>(Arrays.asList(GSON.fromJson(json, Employee[].class)));
    }

    /**
     * List all employees at all hotels
     * @return
     *  A list of every employee in the organization
     */
    public static LinkedList<Employee> list() {
        RestTemplate request = new RestTemplate();
        String json = request.getForEntity("http://localhost:8080/employee/list", String.class).getBody();

        return new LinkedList<>(Arrays.asList(GSON.fromJson(json, Employee[].class)));
    }
}
