package domain;

import javax.persistence.Entity;

public class Employee {
    private String firstName;
    private String lastName;
    private int employeeID;
    private int hotelID;

    public Employee(String firstName, String lastName, int employeeID, int hotelID) {
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

    public int getHotelID() {
        return hotelID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
