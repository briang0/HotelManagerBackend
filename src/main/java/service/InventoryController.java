package service;

import com.google.gson.Gson;
import db.Connector;
import domain.Employee;
import domain.Inventory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Random;

@RestController
public class InventoryController {
    private Connection db;
    private static final Gson GSON = new Gson();

    private static long generateID() {
        return new Random().nextLong();
    }

    public InventoryController() {
        db = Connector.getConnection("brian", "YuckyP@ssw0rd");
    }

    @RequestMapping("/inventory/create")
    public String create(@RequestParam long hotelID) {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("create table inventory_%d (", hotelID) +
                    "inventory_id long, item varchar(255), quantity int);");
            return "ok";
        } catch (SQLException e) {
            return "error";
        }
    }

    @RequestMapping("/inventory/add")
    public String add(@RequestParam long hotelID, @RequestParam String itemName, @RequestParam int quantity) {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("insert into inventory_%d(inventory_id, item, quantity) values('%d', '%s', %d);",
                    hotelID, generateID(), itemName, quantity));
            return "ok";
        } catch (SQLException e) {
            return "error";
        }
    }

    @RequestMapping("/inventory/delete")
    public String delete(@RequestParam long hotelID, @RequestParam long inventoryID) {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("delete from inventory_%d where inventory_id = %d", hotelID, inventoryID));
            return "ok";
        } catch (SQLException e) {
            return "error";
        }
    }

    @RequestMapping("/inventory/update")
    public String update(@RequestParam long hotelID, @RequestParam long inventoryID, @RequestParam String itemName, @RequestParam int quantity) {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("update inventory_%d set inventory_id = '%d', item = '%s', quantity = %d where inventory_id = %d",
                    hotelID, inventoryID, itemName, quantity, inventoryID));
            return "ok";
        } catch (SQLException e) {
            return "error";
        }
    }

    @RequestMapping("/inventory/get")
    public String get(@RequestParam long hotelID, @RequestParam long inventoryID) {
        try (Statement stmt = db.createStatement()) {
            try (ResultSet result = stmt.executeQuery(String.format("select * from inventory_%d where inventory_id = %d;", hotelID, inventoryID))) {
                if (result.next()) {
                    return GSON.toJson(new Inventory(hotelID, result.getLong(1), result.getString(2),
                            result.getInt(3)), Inventory.class);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return "error";
    }

    @RequestMapping("/inventory/list")
    public String list(@RequestParam long hotelID) {
        try (Statement stmt = db.createStatement()) {
            LinkedList<Inventory> inventoryList = new LinkedList<>();
            try (ResultSet result = stmt.executeQuery(String.format("select * from inventory_%d;", hotelID))) {
                while (result.next()) {
                    Inventory inventory = new Inventory(hotelID, result.getLong(1),
                            result.getString(2), result.getInt(3));
                    inventoryList.add(inventory);
                }
            }
            return GSON.toJson(inventoryList.toArray(), Employee[].class);
        } catch (SQLException e) {
            return "error";
        }
    }
}
