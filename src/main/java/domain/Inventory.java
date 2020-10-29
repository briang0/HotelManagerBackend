package domain;

import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.LinkedList;

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

    public static boolean add(long hotelID, String itemName, int quantity) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/inventory/add")
                .queryParam("hotelID", hotelID)
                .queryParam("itemName", itemName)
                .queryParam("quantity", quantity)
                .build();

        RestTemplate request = new RestTemplate();
        return request.getForEntity(uri.toUri(), String.class).getBody().equals("ok");
    }

    public static boolean delete(long hotelID, long inventoryID) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/inventory/delete")
                .queryParam("hotelID", hotelID)
                .queryParam("inventoryID", inventoryID)
                .build();

        RestTemplate request = new RestTemplate();
        return request.getForEntity(uri.toUri(), String.class).getBody().equals("ok");
    }

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

    public static LinkedList<Inventory> list(long hotelID) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/inventory/list")
                .queryParam("hotelID", hotelID)
                .build();

        RestTemplate request = new RestTemplate();
        String resp = request.getForEntity(uri.toUri(), String.class).getBody();
        Inventory[] inventories = GSON.fromJson(resp, Inventory[].class);

        return new LinkedList<>(Arrays.asList(inventories));
    }
}