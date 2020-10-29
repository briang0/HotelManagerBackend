package frontend;

import domain.ConciergeEntry;
import domain.Inventory;
import service.ConciergeEntryController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * A console for interacting with the concierge management system.
 * @author Collin
 */
public class ConciergeManagementConsole extends SystemConsole {
    private long hotelID;
    private final ConciergeEntryController controller;
    private static final String[] statuses = {
        "open", "in progress", "done", "cancelled"
    };

    private static final InformationPrompt customerIDPrompt = InformationPrompt.builder()
            .add("Enter customer ID", "customer_id")
            .build();

    private static final InformationPrompt addInventoryConciergeEntry = InformationPrompt.builder()
            .add("Inventory ID", "inventory_id")
            .add("Quantity", "quantity")
            .add("Charge ($)", "charge")
            .build();

    private static final ConsoleSelection CustomOrInventoryPrompt = ConsoleSelection.builder()
            .add("Custom entry", "custom")
            .add("Inventory entry", "inventory")
            .build();

    private static final InformationPrompt addCustomConciergeEntry = InformationPrompt.builder()
            .add("Charge ($)", "charge")
            .add("Description", "description")
            .build();

    private static final ConsoleSelection selectNewEntryStatusPrompt = ConsoleSelection.builder()
            .add("Open", "open")
            .add("In progress", "in_progress")
            .build();

    @Override
    String getSystemName() {
        return "Concierge Management";
    }

    @Override
    protected void init() {
        hotelID = Long.parseLong(
                InformationPrompt.builder()
                .add("Hotel ID", "hotel_id")
                .build()
                .prompt(scanner)
                .get("hotel_id")
        );
    }

    @Override
    protected boolean executeCommand(String command) {
        switch (command) {
            case "show":
                show();
                return true;
            case "create":
                create();
                return true;
            case "delete":
                delete();
                return true;
            case "status":
                status();
                return true;
            case "add":
                add();
                return true;
        }

        return false;
    }

    @Override
    protected void displayHelp() {
        System.out.println(
                "Accepted commands:\n" +
                        "show      - show a customer's current concierge tab\n" +
                        "create    - create a concierge tab for a customer\n" +
                        "status    - change a concierge tab status\n" +
                        "delete    - delete a concierge tab entry\n" +
                        "add       - add a concierge tab entry\n" +
                        "help      - print this command\n" +
                        "exit      - exit the employee management system"
        );
    }

    public ConciergeManagementConsole(Scanner scanner) {
        super(scanner);
        controller = new ConciergeEntryController();
    }

    /**
     * List the concierge tab for a given customer
     */
    private void show() {
        try {
            System.out.println("Enter a customer ID: ");
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
            System.out.println("Enter a customer ID: ");
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
            System.out.println("Enter a customer ID: ");
            //int customerID = scanner.nextInt();
            long customerID = scanner.nextLong();
            System.out.println("Enter an entry number: ");
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
        System.out.println("Status: ");
    }

    /**
     * Change the status of a concierge entry for a customer
     */
    private void status() {
        try {
            //Scanner scanner = new Scanner(System.in);
            System.out.println("Enter customer ID: ");
            //int customerID = scanner.nextInt();
            long customerID = scanner.nextLong();
            System.out.println("Enter entry number: ");
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
            /*
            System.out.println("Enter customer ID: ");
            int customerID = scanner.nextInt();
            printStatusDialog();
            String status = statuses[scanner.nextInt()-1];
            System.out.println("Enter charge ($): ");
            float charge = scanner.nextFloat();
            System.out.println("Enter description: ");
            scanner.nextLine();
            String description = scanner.nextLine();
            controller.addConciergeEntry(customerID, status, charge, description);
             */
            long customerID = Long.parseLong(customerIDPrompt.prompt(scanner).get("customer_id"));
            String status = selectNewEntryStatusPrompt.select(scanner);
            System.out.println(status);
            HashMap<String, String> answers;
            if (CustomOrInventoryPrompt.select(scanner).equals("custom")) {
                answers = addCustomConciergeEntry.prompt(scanner);
                controller.addConciergeEntry(
                        customerID,
                        status,
                        Float.parseFloat(answers.get("charge")),
                        answers.get("description")
                );
            } else {
                // Need to print the inventory (probably needs to support empty inventory)
                System.out.println("Available inventory:");
                InventoryManagementConsole.list(hotelID);
                answers = addInventoryConciergeEntry.prompt(scanner);

                // Update the inventory
                Inventory item = Inventory.get(hotelID, Long.parseLong(answers.get("inventory_id")));
                Inventory.update(
                        hotelID,
                        item.getInventoryID(),
                        item.getItem(),
                        item.getQuantity() - Integer.parseInt(answers.get("quantity"))
                );

                //System.out.println(status);
                controller.addConciergeEntry(
                        customerID,
                        status,
                        Float.parseFloat(answers.get("charge")),
                        item.getItem()+"x"+Integer.parseInt(answers.get("quantity"))
                );

                // This is where inventory should be modified based on status.
                // We lookup inventory by hotel id and inventory id
                // Note: I don't think we should allow an initial status of cancelled..
            }
            System.out.println("Added new entry to customer's concierge tab.");
        } catch (SQLException e) {
            System.err.println("Failed to add entry to concierge tab");
        }
    }
}
