package dev.DataLayer.DAO;

import dev.BusinessLayer.SupplierBL.DiscountNote;
import dev.BusinessLayer.SupplierBL.Item;
import dev.DataLayer.DTO.DiscountNoteDTO;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountNoteDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/yourdatabase";
    private static final String USER = "yourusername";
    private static final String PASSWORD = "yourpassword";

    // Method to save a DiscountNote to the database
    public void saveDiscountNote(DiscountNote discountNote) throws SQLException {
        String sql = "INSERT INTO discount_notes (item_name, quantity, discount_price) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Map.Entry<Item, Map<Integer, Float>> entry : discountNote.getDiscounts().entrySet()) {
                Item item = entry.getKey();
                Map<Integer, Float> discountMap = entry.getValue();
                for (Map.Entry<Integer, Float> discountEntry : discountMap.entrySet()) {
                    pstmt.setString(1, item.getName());
                    pstmt.setInt(2, discountEntry.getKey());
                    pstmt.setFloat(3, discountEntry.getValue());
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
        }
    }
    public DiscountNote getDiscountNoteBySupplierName(String supplierName) throws SQLException {
        String sql = "SELECT * FROM discount_notes WHERE item_name = ?";
        Map<Item, Map<Integer, Float>> discounts = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplierName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    float discountPrice = rs.getFloat("discount_price");
                    Item item = new Item(name, 0, 0); // Assuming a constructor or method to create an Item with only a name
                    discounts.computeIfAbsent(item, k -> new HashMap<>()).put(quantity, discountPrice);
                }
            }
        }
        return new DiscountNote(discounts);
    }

    // Method to get a DiscountNote from the database
    public DiscountNote getDiscountNoteByItemName(String itemName) throws SQLException {
        String sql = "SELECT * FROM discount_notes WHERE item_name = ?";
        Map<Item, Map<Integer, Float>> discounts = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, itemName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    float discountPrice = rs.getFloat("discount_price");
                    Item item = new Item(name, 0, 0); // Assuming a constructor or method to create an Item with only a name
                    discounts.computeIfAbsent(item, k -> new HashMap<>()).put(quantity, discountPrice);
                }
            }
        }
        return new DiscountNote(discounts);
    }

    // Method to get all DiscountNotes from the database
    public List<DiscountNoteDTO> getAllDiscountNotes() throws SQLException {
        return null; // Implementation not provided
    }

    // Method to delete a DiscountNote from the database by item name
    public void deleteDiscountNoteByItemName(String itemName) throws SQLException {
        String sql = "DELETE FROM discount_notes WHERE item_name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itemName);
            pstmt.executeUpdate();
        }
    }

    public DiscountNote getDiscountNoteBySupplierId(long supplierId) throws SQLException{
        //return all items by supplier id
        String sql = "SELECT * FROM discount_notes WHERE supplier_id = ?";
        Map<Item, Map<Integer, Float>> discounts = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    float discountPrice = rs.getFloat("discount_price");
                    Item item = new Item(name, 0, 0); // Assuming a constructor or method to create an Item with only a name
                    discounts.computeIfAbsent(item, k -> new HashMap<>()).put(quantity, discountPrice);
                }
            }
        }
        return new DiscountNote(discounts);
    }

    public void addDiscountNoteForSupplier(long supplierId, DiscountNote discountNote, Connection conn) {
        //add discount note for supplier
        String sql = "INSERT INTO discount_notes (supplier_id, item_name, quantity, discount_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Map.Entry<Item, Map<Integer, Float>> entry : discountNote.getDiscounts().entrySet()) {
                Item item = entry.getKey();
                Map<Integer, Float> discountMap = entry.getValue();
                for (Map.Entry<Integer, Float> discountEntry : discountMap.entrySet()) {
                    pstmt.setLong(1, supplierId);
                    pstmt.setString(2, item.getName());
                    pstmt.setInt(3, discountEntry.getKey());
                    pstmt.setFloat(4, discountEntry.getValue());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDiscountNoteForSupplier(long supplierId, DiscountNote discountNote, Connection conn) {
        //update discount note for supplier
        String sql = "UPDATE discount_notes SET item_name = ?, quantity = ?, discount_price = ? WHERE supplier_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Map.Entry<Item, Map<Integer, Float>> entry : discountNote.getDiscounts().entrySet()) {
                Item item = entry.getKey();
                Map<Integer, Float> discountMap = entry.getValue();
                for (Map.Entry<Integer, Float> discountEntry : discountMap.entrySet()) {
                    pstmt.setString(1, item.getName());
                    pstmt.setInt(2, discountEntry.getKey());
                    pstmt.setFloat(3, discountEntry.getValue());
                    pstmt.setLong(4, supplierId);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDiscountNoteBySupplierId(long supplierId, Connection conn) {
        //delete discount note for supplier
        String sql = "DELETE FROM discount_notes WHERE supplier_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, supplierId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Additional DAO methods for CRUD operations can be added as needed
}
