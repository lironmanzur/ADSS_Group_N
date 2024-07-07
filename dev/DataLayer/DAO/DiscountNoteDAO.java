package dev.DataLayer.DAO;

import dev.BusinessLayer.SupplierBL.DiscountNote;
import dev.BusinessLayer.SupplierBL.Item;
import dev.DataLayer.DTO.DiscountNoteDTO;
import dev.DataLayer.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountNoteDAO {
    Connection connection;

    public DiscountNoteDAO() {
        try {
            connection = DatabaseConnection.getConnection();
            createTableIfNotExists();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS discount_notes ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "supplier_id INT NOT NULL, "
                + "item_name VARCHAR(255) NOT NULL, "
                + "quantity INT NOT NULL, "
                + "discount_price FLOAT NOT NULL"
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table: " + e.getMessage(), e);
        }
    }

    // Method to save a DiscountNote to the database
    public void saveDiscountNote(DiscountNote discountNote) throws SQLException {
        String sql = "INSERT INTO discount_notes (supplier_id, item_name, quantity, discount_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Map.Entry<Item, Map<Integer, Float>> entry : discountNote.getDiscounts().entrySet()) {
                Item item = entry.getKey();
                Map<Integer, Float> discountMap = entry.getValue();
                for (Map.Entry<Integer, Float> discountEntry : discountMap.entrySet()) {
                    pstmt.setInt(1, discountNote.getSupplierId()); // Assuming DiscountNote has a supplierId
                    pstmt.setString(2, item.getName());
                    pstmt.setInt(3, discountEntry.getKey());
                    pstmt.setFloat(4, discountEntry.getValue());
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
        }
    }

    // Method to get a DiscountNote from the database by item name

    // Method to get all DiscountNotes from the database
    public List<DiscountNoteDTO> getAllDiscountNotes() throws SQLException {
        // Implementation not provided
        return null;
    }

    // Method to delete a DiscountNote from the database by item name
    public void deleteDiscountNoteByItemName(String itemName) throws SQLException {
        String sql = "DELETE FROM discount_notes WHERE item_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itemName);
            pstmt.executeUpdate();
        }
    }

    // Method to get a DiscountNote by supplier ID
    public DiscountNote getDiscountNoteBySupplierId(int supplierId) throws SQLException {
        String sql = "SELECT * FROM discount_notes WHERE supplier_id = ?";
        Map<Item, Map<Integer, Float>> discounts = new HashMap<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String itemName = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    float discountPrice = rs.getFloat("discount_price");
                    Item item = new Item(itemName, 0, 0); // Assuming a constructor or method to create an Item with only a name
                    discounts.computeIfAbsent(item, k -> new HashMap<>()).put(quantity, discountPrice);
                }
            }
        }
        return new DiscountNote(discounts, supplierId);
    }

    // Method to add a DiscountNote for a supplier
    public void addDiscountNoteForSupplier(long supplierId, DiscountNote discountNote, Connection conn) {
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
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a DiscountNote for a supplier
    public void updateDiscountNoteForSupplier(long supplierId, DiscountNote discountNote, Connection conn) {
        // Delete existing discount notes for the supplier first
        deleteDiscountNoteBySupplierId(supplierId, conn);

        // Add new discount notes
        addDiscountNoteForSupplier(supplierId, discountNote, conn);
    }

    // Method to delete a DiscountNote by supplier ID
    public void deleteDiscountNoteBySupplierId(long supplierId, Connection conn) {
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
