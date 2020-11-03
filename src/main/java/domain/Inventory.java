package domain;

import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * A class representing an inventory entry
 * @author Collin
 */
public class Inventory {
    private final static Gson GSON = new Gson();
    private long hotelID;
    private long inventoryID;
    private String item;
    private int quantity;

    public Inventory(long hotelID, long inventoryID, String item, int quantity) {
        this.hotelID = hotelID;
        this.inventoryID = inventoryID;
        this.item = item;
        this.quantity = quantity;
    }

    public long getInventoryID() {
        return inventoryID;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Add an inventory entry to the database
     * @param hotelID
     *  Hotel where inventory resides
     * @param itemName
     *  The name of the inventory entry
     * @param quantity
     *  The quantity of inventory available
     * @return
     *  Whether or not the operation was successful
     */
    public static boolean add(long hotelID, String itemName, int quantity) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/inventory/add")
                .queryParam("hotelID", hotelID)
                .queryParam("itemName", itemName)
                .queryParam("quantity", quantity)
                .build();

        RestTemplate request = new RestTemplate();
        return request.getForEntity(uri.toUri(), String.class).getBody().equals("ok");
    }

    /**
     * Delete an inventory entry from a hotel
     * @param hotelID
     *  The hotel where the inventory resides
     * @param inventoryID
     *  The inventory id of the inventory entry
     * @return
     *  Whether or not the operation succeeded
     */
    public static boolean delete(long hotelID, long inventoryID) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/inventory/delete")
                .queryParam("hotelID", hotelID)
                .queryParam("inventoryID", inventoryID)
                .build();

        RestTemplate request = new RestTemplate();
        return request.getForEntity(uri.toUri(), String.class).getBody().equals("ok");
    }

    /**
     * Update an inventory entry
     * @param hotelID
     *  Hotel where inventory is located
     * @param inventoryID
     *  The inventory ID
     * @param itemName
     *  The new name of the inventory entry
     * @param quantity
     *  The new quantity of the inventory entry
     * @return
     */
    public static boolean update(long hotelID, long inventoryID, String itemName, int quantity) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/inventory/update")
                .queryParam("hotelID", hotelID)
                .queryParam("itemName", itemName)
                .queryParam("quantity", quantity)
                .queryParam("inventoryID", inventoryID)
                .build();

        RestTemplate request = new RestTemplate();
        return request.getForEntity(uri.toUri(), String.class).getBody().equals("ok");
    }

    /**
     * List all inventory in a given hotel
     * @param hotelID
     *  List all inventory at a given hotel
     * @return
     *  List of hotel inventory
     */
    public static LinkedList<Inventory> list(long hotelID) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/inventory/list")
                .queryParam("hotelID", hotelID)
                .build();

        RestTemplate request = new RestTemplate();
        String resp = request.getForEntity(uri.toUri(), String.class).getBody();
        if (resp.equals("error")) return new LinkedList<>();
        Inventory[] inventories = GSON.fromJson(resp, Inventory[].class);

        return new LinkedList<>(Arrays.asList(inventories));
    }

    /**
     * Obtain a specific inventory entry from the database
     * @param hotelID
     *  The hotel to check
     * @param inventoryID
     *  The inventory ID to retrieve
     * @return
     *  The inventory entry
     */
    public static Inventory get(long hotelID, long inventoryID) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/inventory/get")
                .queryParam("hotelID", hotelID)
                .queryParam("inventoryID", inventoryID)
                .build();

        RestTemplate request = new RestTemplate();
        String resp = request.getForEntity(uri.toUri(), String.class).getBody();

        return GSON.fromJson(resp, Inventory.class);
    }

    // Link to hotel creation
    /**
     * Create a new hotel inventory database
     * @param hotelID
     *  The hotel ID to create an inventory database for
     * @return
     *  True if creation succeeded, false otherwise
     */
    public static boolean create(long hotelID) {
        RestTemplate request = new RestTemplate();
        String resp = request.getForEntity("http://localhost:8080/inventory/create?hotelID=" + hotelID, String.class).getBody();

        return resp.equals("ok");
    }
}