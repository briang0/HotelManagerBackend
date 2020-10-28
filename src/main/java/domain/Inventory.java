package domain;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class Inventory {
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

    public static boolean add(String itemName, int quantity) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/inventory/add")
                .queryParam("item", itemName)
                .queryParam("quantity", quantity)
                .build();

        RestTemplate request = new RestTemplate();
        return request.getForEntity(uri.toUri(), String.class).getBody().equals("ok");
    }
}
