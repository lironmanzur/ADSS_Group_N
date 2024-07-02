package dev.DataLayer.DAO;

import dev.BusinessLayer.SupplierBL.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/yourdatabase";
    private static final String USER = "yourusername";
    private static final String PASSWORD = "yourpassword";

    // Method to add an Item to the database
    public void addItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (item_id, item_name, price) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getItemID());
            pstmt.setString(2, item.getItemName());
            pstmt.setFloat(3, item.getPrice());
            pstmt.executeUpdate();
        }
    }

    // Method to get an Item by ID from the database
    public Item getItemById(int itemId) throws SQLException {
        String sql = "SELECT * FROM items WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String itemName = rs.getString("item_name");
                    float price = rs.getFloat("price");
                    return new Item(itemName, price, itemId);
                }
            }
        }
        return null;
    }

    // Method to get all Items from the database
    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                float price = rs.getFloat("price");
                Item item = new Item(itemName, price, itemId);
                items.add(item);
            }
        }
        return items;
    }

    // Method to update an Item in the database
    public void updateItem(Item item) throws SQLException {
        String sql = "UPDATE items SET item_name = ?, price = ? WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setFloat(2, item.getPrice());
            pstmt.setInt(3, item.getItemID());
            pstmt.executeUpdate();
        }
    }

    // Method to delete an Item from the database
    public void deleteItemById(int itemId) throws SQLException {
        String sql = "DELETE FROM items WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();
        }
    }

    public List<Item> getItemsBySupplierId(long supplierId) {
        //return all items by supplier id
        String sql = "SELECT * FROM items WHERE supplier_id = ?";
        List<Item> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    String itemName = rs.getString("item_name");
                    float price = rs.getFloat("price");
                    Item item = new Item(itemName, price, itemId);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;


    }

    public void addItemForSupplier(long supplierId, Item item, Connection conn) {
        //add item for supplier
        String sql = "INSERT INTO items (item_id, item_name, price, supplier_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getItemID());
            pstmt.setString(2, item.getItemName());
            pstmt.setFloat(3, item.getPrice());
            pstmt.setLong(4, supplierId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItemsForSupplier(long supplierId, List<Item> items, Connection conn) {
        //update items for supplier
        String sql = "UPDATE items SET item_name = ?, price = ? WHERE item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Item item : items) {
                pstmt.setString(1, item.getItemName());
                pstmt.setFloat(2, item.getPrice());
                pstmt.setInt(3, item.getItemID());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteItemsBySupplierId(long supplierId, Connection conn) {
        //delete items by supplier id
        String sql = "DELETE FROM items WHERE supplier_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, supplierId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
