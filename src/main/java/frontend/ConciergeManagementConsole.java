package frontend;

import domain.ConciergeEntry;
import service.ConciergeEntryController;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;

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

    private void dispatchCommand(String command) {
        switch (command) {
            case "show" -> show();
            case "create" -> create();
            case "delete" -> delete();
            case "status" -> status();
            case "add" -> add();
            case "help" -> help();
            default -> System.out.println("Unrecognized command.");
        }
    }

    public void show() {
        try {
            System.out.print("Enter a customer ID: ");
            int customerID = new Scanner(System.in).nextInt();
            LinkedList<ConciergeEntry> entries = controller.readConciergeEntries(customerID);
            for (ConciergeEntry entry : entries) {
                System.out.println("""
                        Entry  : %d
                        Status : %s
                        Charge : $%.02f
                        Details: %s
                        """.formatted(entry.getIndex(), entry.getStatus(), entry.getCharge(), entry.getDescription()));
            }
        } catch (SQLException e) {
            System.err.println("Failed to display customer concierge tab.");
        }
    }

    public void create() {
        try {
            System.out.print("Enter a customer ID: ");
            int customerID = new Scanner(System.in).nextInt();
            controller.createConciergeTab(customerID);
            System.out.println("Created concierge tab for customer.");
        } catch (SQLException e) {
            System.err.println("Failed to create concierge tab for customer.");
        }
    }

    public void delete() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a customer ID: ");
            int customerID = scanner.nextInt();
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

    public void status() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter customer ID: ");
            int customerID = scanner.nextInt();
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

    public void add() {
        try {
            Scanner scanner = new Scanner(System.in);
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
                """
                Accepted commands:
                show      - show a customer's current concierge tab
                create    - create a concierge tab for a customer
                status    - change a concierge tab status
                delete    - delete a concierge tab entry
                add       - add a concierge tab entry
                edit      - edit a concierge tab entry
                help      - print this command
                exit      - exit the employee management system
                """);
    }
}
