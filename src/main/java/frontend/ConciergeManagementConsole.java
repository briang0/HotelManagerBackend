package frontend;

import domain.ConciergeEntry;
import service.ConciergeEntryController;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * A console for interacting with the concierge management system.
 * @author Collin
 */
public class ConciergeManagementConsole {
    private final ConciergeEntryController controller;
    private Scanner scanner;
    private static final String[] statuses = {
        "open", "in progress", "done", "cancelled"
    };

    public ConciergeManagementConsole(Scanner scanner) {
        controller = new ConciergeEntryController();
        this.scanner = scanner;
    }

    /**
     * Run the concierge management system
     */
    public void run() {
        System.out.println("Welcome to the concierge management console.\n");
        help();
        while (true) {
            System.out.print("Concierge Management > ");
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                break;
            }
            dispatchCommand(command);
        }
    }

    /**
     * Route and execute the console command
     * @param command
     *  The command to execute
     */
    private void dispatchCommand(String command) {
        switch (command) {
            case "show":
                show();
                break;
            case "create":
                create();
                break;
            case "delete":
                delete();
                break;
            case "status":
                status();
                break;
            case "add":
                add();
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
     * List the concierge tab for a given customer
     */
    private void show() {
        try {
            System.out.print("Enter a customer ID: ");
            //long customerID = new Scanner(System.in).nextInt();
            long customerID = scanner.nextLong();
            LinkedList<ConciergeEntry> entries = controller.readConciergeEntries(customerID);
            for (ConciergeEntry entry : entries) {
                System.out.println(
                        "Entry  : " + entry.getIndex() + "\n" +
                        "Status : " + entry.getStatus() + "\n" +
                        "Charge : " + entry.getCharge() + "\n" +
                        "Details: " + entry.getDescription() + "\n"
                );
            }
        } catch (SQLException e) {
            System.err.println("Failed to display customer concierge tab.");
        }
    }

    /**
     * Create a concierge tab for a given customer
     */
    private void create() {
        try {
            System.out.print("Enter a customer ID: ");
            //int customerID = new Scanner(System.in).nextInt();
            long customerID = scanner.nextLong();
            controller.createConciergeTab(customerID);
            System.out.println("Created concierge tab for customer.");
        } catch (SQLException e) {
            System.err.println("Failed to create concierge tab for customer.");
        }
    }

    /**
     * Delete the concierge tab for a given customer
     */
    private void delete() {
        try {
            //Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a customer ID: ");
            //int customerID = scanner.nextInt();
            long customerID = scanner.nextLong();
            System.out.print("Enter an entry number: ");
            int entryNo = scanner.nextInt();
            controller.deleteConciergeEntry(customerID, entryNo);
            System.out.println("Deleted concierge tab entry.");
        } catch (SQLException e) {
            System.err.println("Failed to create concierge tab for customer.");
        }
    }

    private void printStatusDialog() {
        System.out.println("Select a status to set:");
        System.out.println("1: open");
        System.out.println("2: in progress");
        System.out.println("3: done");
        System.out.println("4: cancelled");
        System.out.print("Status: ");
    }

    /**
     * Change the status of a concierge entry for a customer
     */
    private void status() {
        try {
            //Scanner scanner = new Scanner(System.in);
            System.out.print("Enter customer ID: ");
            //int customerID = scanner.nextInt();
            long customerID = scanner.nextLong();
            System.out.print("Enter entry number: ");
            int entryNo = scanner.nextInt();
            printStatusDialog();
            int status = scanner.nextInt();
            ConciergeEntry entry = controller.readConciergeEntry(customerID, entryNo);
            controller.updateConciergeEntry(customerID, statuses[status-1], entry.getCharge(), entry.getDescription(), entryNo);
            System.out.println("Updated entry status");
        } catch (SQLException e) {
            System.err.println("Failed to update concierge entry status");
        }
    }

    /**
     * Add an entry to a customer's concierge tab
     */
    private void add() {
        try {
            //Scanner scanner = new Scanner(System.in);
            System.out.print("Enter customer ID: ");
            int customerID = scanner.nextInt();
            printStatusDialog();
            String status = statuses[scanner.nextInt()-1];
            System.out.print("Enter charge ($): ");
            float charge = scanner.nextFloat();
            System.out.print("Enter description: ");
            scanner.nextLine();
            String description = scanner.nextLine();
            controller.addConciergeEntry(customerID, status, charge, description);
            System.out.println("Added new entry to customer's concierge tab.");
        } catch (SQLException e) {
            System.err.println("Failed to add entry to concierge tab");
        }
    }

    private void help() {
        System.out.println(
                "Accepted commands:\n" +
                "show      - show a customer's current concierge tab\n" +
                "create    - create a concierge tab for a customer\n" +
                "status    - change a concierge tab status\n" +
                "delete    - delete a concierge tab entry\n" +
                "add       - add a concierge tab entry\n" +
                "edit      - edit a concierge tab entry\n" +
                "help      - print this command\n" +
                "exit      - exit the employee management system"
                );
    }
}
