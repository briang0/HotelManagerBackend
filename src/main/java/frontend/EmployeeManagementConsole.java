package frontend;

import domain.Employee;
import service.EmployeeController;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagementConsole {
    private final EmployeeController controller;

    public EmployeeManagementConsole() {
        controller = new EmployeeController();
    }

    public void run() {
        System.out.println("Welcome to the employee management console.\n");
        help();
        try (Scanner scanner = new Scanner(System.in)){
            while (true) {
                System.out.print("Employee Management > ");
                String command = scanner.next();
                if (command.equals("exit")) {
                    break;
                }
                dispatchCommand(command);
            }
        }
    }

    private void dispatchCommand(String command) {
        switch (command) {
            case "list" -> list();
            case "listhotel" -> listHotel();
            case "delete" -> delete();
            case "add" -> add();
            case "update" -> update();
            case "total" -> total();
            case "help" -> help();
            default -> System.out.println("Unrecognized command.");
        }
    }

    private void displayEmployees(List<Employee> employees) {
        for (Employee employee : employees) {
            System.out.print(
                    """
                    First name : %s
                    Last name  : %s
                    Hotel ID   : %d
                    Employee ID: %d
                    
                    """.formatted(employee.getFirstName(), employee.getLastName(), employee.getHotelID(), employee.getEmployeeID()));
        }
    }

    private void listHotel() {
        System.out.print("Enter hotel ID: ");
        int hotelID = new Scanner(System.in).nextInt();

        try {
            displayEmployees(controller.listEmployees(hotelID));
        } catch (SQLException e) {
            System.err.println("Failed to list employees.");
        }
    }

    private void list() {
        try {
            displayEmployees(controller.listEmployees());
        } catch (SQLException e) {
            System.err.println("Failed to list employees.");
        }
    }

    private void delete() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("To delete an employee, enter an employee ID.");
            System.out.print("Employee ID: ");
            int employeeID = scanner.nextInt();

            controller.deleteEmployee(employeeID);
            System.out.printf("Employee %d deleted.\n", employeeID);
        } catch (SQLException e) {
            System.err.println("Failed to delete employee.");
        }
    }

    private void add() {
        try {
            String firstName, lastName;
            int hotelID;

            System.out.println("Please enter the following information.");
            Scanner scanner = new Scanner(System.in);

            System.out.print("First name: ");
            firstName = scanner.next();
            System.out.print("Last name: ");
            lastName = scanner.next();
            System.out.print("Hotel ID: ");
            hotelID = scanner.nextInt();
            controller.addEmployee(firstName, lastName, hotelID);
            System.out.println("Added employee.");
        } catch (SQLException e) {
            System.err.println("Failed to add employee.");
        }
    }

    private void update() {
        try {
            String firstName, lastName;
            int hotelID, employeeID;

            System.out.println("Please enter the following information.");
            Scanner scanner = new Scanner(System.in);

            System.out.print("Employee ID: ");
            employeeID = scanner.nextInt();
            System.out.print("New first name: ");
            firstName = scanner.next();
            System.out.print("New last name: ");
            lastName = scanner.next();
            System.out.print("New hotel ID: ");
            hotelID = scanner.nextInt();
            controller.updateEmployee(firstName, lastName, hotelID, employeeID);
            System.out.println("Updated employee.");
        } catch (SQLException e) {
            System.err.println("Failed to update employee.");
        }
    }

    private void total() {
        try {
            System.out.printf("Total employees: %d\n", controller.listEmployees().size());
        } catch (SQLException e) {
            System.err.println("Failed to count employees.");
        }
    }

    private void help() {
        System.out.println(
                """
                Accepted commands:\s
                list      - list employees
                listhotel - list employees at a given hotel
                delete    - remove an employee
                add       - add an employee
                update    - update an employee
                total     - display the total employee count
                help      - print this command
                exit      - exit the employee management system
                """);
    }
}
