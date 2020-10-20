package frontend;

import domain.Employee;
import service.EmployeeController;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * A console for interacting with the employee management system
 *
 * @author Collin
 */
public class EmployeeManagementConsole {
    private final EmployeeController controller;
    private Scanner scanner;

    public EmployeeManagementConsole(Scanner scanner) {
        controller = new EmployeeController();
        this.scanner = scanner;
    }

    /**
     * Run the console for interacting with the employee management system
     */
    public void run() {
        System.out.println("Welcome to the employee management console.\n");
        help();
        while (true) {
            System.out.println("Employee Management > ");
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                break;
            }
            dispatchCommand(command);
        }
    }

    /**
     * Route and execute the given command
     * @param command
     *  The command to execute
     */
    private void dispatchCommand(String command) {
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
            case "help":
                help();
                break;
            default:
                System.out.println("Unrecognized command.");
                break;
        }
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
        System.out.println("Enter hotel ID: ");
        //int hotelID = new Scanner(System.in).nextInt();
        long hotelID = scanner.nextLong();

        try {
            displayEmployees(controller.listEmployees(hotelID));
        } catch (SQLException e) {
            System.err.println("Failed to list employees.");
        }
    }

    /**
     * List all employees in the system, regardless of hotel
     */
    private void list() {
        try {
            displayEmployees(controller.listEmployees());
        } catch (SQLException e) {
            System.err.println("Failed to list employees.");
        }
    }

    /**
     * Delete an employee from the employee management system
     */
    private void delete() {
        try {
            //Scanner scanner = new Scanner(System.in);
            System.out.println("To delete an employee, enter an employee ID.");
            System.out.println("Employee ID: ");
            int employeeID = scanner.nextInt();

            controller.deleteEmployee(employeeID);
            System.out.printf("Employee %d deleted.\n", employeeID);
        } catch (SQLException e) {
            System.err.println("Failed to delete employee.");
        }
    }

    /**
     * Add an employee to the management system
     */
    private void add() {
        try {
            String firstName, lastName;
            long hotelID;

            System.out.println("Please enter the following information.");
//            Scanner scanner = new Scanner(System.in);

            System.out.println("First name: ");
            firstName = scanner.next();
            System.out.println("Last name: ");
            lastName = scanner.next();
            System.out.println("Hotel ID: ");
            hotelID = scanner.nextLong();
            controller.addEmployee(firstName, lastName, hotelID);
            System.out.println("Added employee.");
        } catch (SQLException e) {
            System.err.println("Failed to add employee.");
        }
    }

    /**
     * Update an employee's information in the management system
     */
    private void update() {
        try {
            String firstName, lastName;
            //int hotelID, employeeID;
            long hotelID;
            int employeeID;

            System.out.println("Please enter the following information.");
            //Scanner scanner = new Scanner(System.in);

            System.out.println("Employee ID: ");
            employeeID = scanner.nextInt();
            System.out.println("New first name: ");
            firstName = scanner.next();
            System.out.println("New last name: ");
            lastName = scanner.next();
            System.out.println("New hotel ID: ");
            //hotelID = scanner.nextInt();
            hotelID = scanner.nextLong();
            controller.updateEmployee(firstName, lastName, hotelID, employeeID);
            System.out.println("Updated employee.");
        } catch (SQLException e) {
            System.err.println("Failed to update employee.");
        }
    }

    /**
     * Produce a total count of all employees in the management system.
     */
    private void total() {
        try {
            System.out.printf("Total employees: %d\n", controller.listEmployees().size());
        } catch (SQLException e) {
            System.err.println("Failed to count employees.");
        }
    }

    private void help() {
        System.out.println(
                "Accepted commands:\n" +
                "list      - list employees\n" +
                "listhotel - list employees at a given hotel\n" +
                "delete    - remove an employee\n" +
                "add       - add an employee\n" +
                "update    - update an employee\n" +
                "total     - display the total employee count\n" +
                "help      - print this command\n" +
                "exit      - exit the employee management system\n");
    }
}
