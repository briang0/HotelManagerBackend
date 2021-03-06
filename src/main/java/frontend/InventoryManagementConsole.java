package frontend;

import domain.Inventory;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Console for inventory management
 * @author Collin
 */
public class InventoryManagementConsole extends SystemConsole {
    private long hotelID;

    private static final HelpDisplay helpDisplay = HelpDisplay.builder()
            .add("add", "add an inventory entry")
            .add("delete", "delete an inventory entry")
            .add("update", "update an inventory entry")
            .add("list", "list all inventory entries for this hotel")
            .add("help", "display this text")
            .add("exit", "exit the system")
            .build();

    private static final InformationPrompt addPrompt = InformationPrompt.builder()
            .add("Item name", "item_name")
            .add("Quantity", "quantity")
            .build();

    private static final InformationPrompt updatePrompt = InformationPrompt.builder()
            .add("Inventory ID", "inventory_id")
            .add("Item name", "item_name")
            .add("Quantity", "quantity")
            .build();

    private static final InformationPrompt deletePrompt = InformationPrompt.builder()
            .add("Inventory ID", "inventory_id")
            .build();

    /**
     * On initialization, system prompts user for hotel id
     */
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

    public InventoryManagementConsole(Scanner scanner) {
        super(scanner);
    }

    @Override
    String getSystemName() {
        return "Inventory Management";
    }

    @Override
    protected boolean executeCommand(String command) {
        switch (command) {
            case "add":
                add();
                return true;
            case "update":
                update();
                return true;
            case "delete":
                delete();
                return true;
            case "list":
                list();
                return true;
        }
        return false;
    }

    @Override
    protected void displayHelp() {
        helpDisplay.display();
    }

    /**
     * Add an inventory entry to the hotel's inventory
     */
    private void add() {
        HashMap<String, String> answers = addPrompt.prompt(scanner);
        if (Inventory.add(
                hotelID,
                answers.get("item_name"),
                Integer.parseInt(answers.get("quantity"))
        )) {
            System.out.println("Added inventory entry");
        } else {
            System.err.println("Failed to add inventory entry");
        }
    }

    /**
     * Update an inventory entry
     */
    private void update() {
        HashMap<String, String> answers = updatePrompt.prompt(scanner);
        if (Inventory.update(
                hotelID,
                Long.parseLong(answers.get("inventory_id")),
                answers.get("item_name"),
                Integer.parseInt(answers.get("quantity"))
        )) {
            System.out.println("Updated inventory entry");
        } else {
            System.err.println("Failed to update inventory entry");
        }
    }

    /**
     * Delete an inventory entry from the hotel's inventory
     */
    private void delete() {
        HashMap<String, String> answers = deletePrompt.prompt(scanner);

        if (Inventory.delete(
                hotelID,
                Long.parseLong(answers.get("inventory_id"))
        )) {
            System.out.println("Deleted inventory entry");
        } else {
            System.err.println("Failed to delete inventory entry");
        }
    }

    private void list() {
        list(hotelID);
    }

    /**
     * Display all inventory for a given hotel
     * @param hotelID
     *  The hotel inventory to display
     */
    public static void list(long hotelID) {
        Inventory.list(hotelID).forEach(
                entry -> HelpDisplay.builder()
                            .setDelimiter("|")
                            .add("Inventory ID", entry.getInventoryID()+"")
                            .add("Item", entry.getItem())
                            .add("Quantity", entry.getQuantity()+"")
                            .build()
                            .display()
        );
    }
}