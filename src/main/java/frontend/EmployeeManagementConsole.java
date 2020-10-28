package frontend;

import domain.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * A console for interacting with the employee management system
 *
 * @author Collin
 */
public class EmployeeManagementConsole extends SystemConsole {
    private final static HelpDisplay helpDisplay = HelpDisplay.builder()
            .add("list", "list employees")
            .add("listhotel", "list employees at a given hotel")
            .add("delete", "remove an employee")
            .add("update", "update an employee")
            .add("total", "display the total employee count")
            .add("help", "print this command")
            .add("exit", "exit this system")
            .build();

    private final static InformationPrompt updateEmployeePrompt = InformationPrompt.builder()
            .add("Employee ID", "employee_id")
            .add("New first name", "first_name")
            .add("New last name", "last_name")
            .add("New hotel ID", "hotel_id")
            .build();

    private final static InformationPrompt addEmployeePrompt = InformationPrompt.builder()
            .add("First name", "first_name")
            .add("Last name", "last_name")
            .add("Hotel ID", "hotel_id")
            .build();

    @Override
    String getSystemName() {
        return "Employee Management System";
    }

    public EmployeeManagementConsole(Scanner scanner) {
        super(scanner);
    }

    /**
     * Route and execute the given command
     * @param command
     *  The command to execute
     */
    @Override
    protected boolean executeCommand(String command) {
        switch (command) {
            case "list":
                list();
                break;
            case "listhotel":
                listHotel();
                break;
            case "delete":
                delete();
                break;
            case "add":
                add();
                break;
            case "update":
                update();
                break;
            case "total":
                total();
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Display a list of employees in the console.
     * @param employees
     *  The employees to display.
     */
    private void displayEmployees(List<Employee> employees) {
        for (Employee employee : employees) {
            System.out.println(
                String.format("First name : %s\n", employee.getFirstName()) +
                String.format("Last name  : %s\n", employee.getLastName()) +
                String.format("Hotel ID   : %d\n", employee.getHotelID()) +
                String.format("Employee ID: %d\n", employee.getEmployeeID())
            );
        }
    }

    /**
     * List all employees that work at the given hotel
     */
    private void listHotel() {
        System.out.print("Enter hotel ID: ");
        long hotelID = Long.parseLong(scanner.nextLine());

        displayEmployees(Employee.listHotel(hotelID));
    }

    /**
     * List all employees in the system, regardless of hotel
     */
    private void list() {
        displayEmployees(Employee.list());
    }

    /**
     * Delete an employee from the employee management system
     */
    private void delete() {
        System.out.print("Employee ID: ");
        int employeeID = Integer.parseInt(scanner.nextLine());

        if (Employee.delete(employeeID)) {
            System.out.printf("Employee %d deleted.\n", employeeID);
        } else {
            System.err.println("Failed to delete employee.");
        }
    }

    /**
     * Add an employee to the management system
     */
    private void add() {
        HashMap<String, String> answers = addEmployeePrompt.prompt(scanner);
        boolean success = Employee.add(
                answers.get("first_name"),
                answers.get("last_name"),
                Long.parseLong(answers.get("hotel_id"))
        );

        if (success) {
            System.out.println("Added employee.");
        } else {
            System.err.println("Failed to add employee.");
        }
    }

    /**
     * Update an employee's information in the management system
     */
    private void update() {
        HashMap<String, String> answers = updateEmployeePrompt.prompt(scanner);
        boolean success = Employee.update(
                answers.get("first_name"),
                answers.get("last_name"),
                Long.parseLong(answers.get("hotel_id")),
                Integer.parseInt(answers.get("employee_id"))
        );

        if (success) {
            System.out.println("Updated employee.");
        } else {
            System.err.println("Failed to update employee.");
        }
    }

    /**
     * Produce a total count of all employees in the management system.
     */
    private void total() {
        System.out.printf("Total employees: %d\n", Employee.list().size());
    }

    @Override
    protected void displayHelp() {
        helpDisplay.display();
    }
}