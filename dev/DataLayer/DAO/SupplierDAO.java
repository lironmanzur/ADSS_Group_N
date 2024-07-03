package dev.DataLayer.DAO;

import dev.BusinessLayer.SupplierBL.*;
import dev.DataLayer.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierDAO {


    Connection connection;
    private ItemDAO itemDAO;
    private ContactDAO contactDAO;
    private DiscountNoteDAO discountNoteDAO;

    public SupplierDAO(ItemDAO itemDAO, ContactDAO contactDAO, DiscountNoteDAO discountNoteDAO) {
        this.itemDAO = itemDAO;
        this.contactDAO = contactDAO;
        this.discountNoteDAO = discountNoteDAO;
        try {
            connection = DatabaseConnection.getConnection();
            createTableIfNotExists();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Supplier ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "price DOUBLE NOT NULL"
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table: " + e.getMessage(), e);
        }
    }

    // Method to add a Supplier to the database
    public void addSupplier(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO suppliers (name, address) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getAddress());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long supplierId = generatedKeys.getLong(1);
                    saveSupplierItems(supplierId, supplier.getItems(), conn);
                    saveSupplierContacts(supplierId, supplier.getContacts(), conn);
                    saveDiscountNoteForSupplier(supplierId, supplier.getDiscountNote(), conn);
                }
            }
        }
    }

    // Method to get a Supplier by name from the database
    public Supplier getSupplierByName(String name) throws SQLException {
        String sql = "SELECT * FROM suppliers WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String address = rs.getString("address");
                    long supplierId = rs.getLong("supplier_id");

                    Supplier supplier = new Supplier(name, address);
                    supplier.setItems(itemDAO.getItemsBySupplierId(supplierId));
                    supplier.setContacts(contactDAO.getContactsBySupplierId(supplierId));
                    supplier.setDiscountNote(discountNoteDAO.getDiscountNoteBySupplierId(supplierId));

                    return supplier;
                }
            }
        }
        return null;
    }

    // Method to get all Suppliers from the database
    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("name");
                String address = rs.getString("address");
                long supplierId = rs.getLong("supplier_id");

                Supplier supplier = new Supplier(name, address);
                supplier.setItems(itemDAO.getItemsBySupplierId(supplierId));
                supplier.setContacts(contactDAO.getContactsBySupplierId(supplierId));
                supplier.setDiscountNote(discountNoteDAO.getDiscountNoteBySupplierId(supplierId));

                suppliers.add(supplier);
            }
        }
        return suppliers;
    }

    // Method to update a Supplier in the database
    public void updateSupplier(Supplier supplier) throws SQLException {
        String sql = "UPDATE suppliers SET address = ? WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getAddress());
            pstmt.setString(2, supplier.getName());
            pstmt.executeUpdate();

            long supplierId = getSupplierIdByName(supplier.getName(), conn);
            updateSupplierItems(supplierId, supplier.getItems(), conn);
            updateSupplierContacts(supplierId, supplier.getContacts(), conn);
            updateDiscountNoteForSupplier(supplierId, supplier.getDiscountNote(), conn);
        }
    }

    // Method to delete a Supplier from the database
    public void deleteSupplierByName(String name) throws SQLException {
        String sql = "DELETE FROM suppliers WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            long supplierId = getSupplierIdByName(name, conn);
            deleteSupplierItems(supplierId, conn);
            deleteSupplierContacts(supplierId, conn);
            deleteDiscountNoteForSupplier(supplierId, conn);

            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    // Helper method to get supplier ID by name
    private long getSupplierIdByName(String name, Connection conn) throws SQLException {
        String sql = "SELECT supplier_id FROM suppliers WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("supplier_id");
                }
            }
        }
        return -1;
    }

    // Helper methods to save, update, and delete related entities
    private void saveSupplierItems(long supplierId, List<Item> items, Connection conn) throws SQLException {
        for (Item item : items) {
            itemDAO.addItemForSupplier(supplierId, item, conn);
        }
    }

    private void saveSupplierContacts(long supplierId, List<Contact> contacts, Connection conn) throws SQLException {
        for (Contact contact : contacts) {
            contactDAO.addContactForSupplier(supplierId, contact, conn);
        }
    }

    private void saveDiscountNoteForSupplier(long supplierId, DiscountNote discountNote, Connection conn) throws SQLException {
        discountNoteDAO.addDiscountNoteForSupplier(supplierId, discountNote, conn);
    }

    private void updateSupplierItems(long supplierId, List<Item> items, Connection conn) throws SQLException {
        itemDAO.updateItemsForSupplier(supplierId, items, conn);
    }

    private void updateSupplierContacts(long supplierId, List<Contact> contacts, Connection conn) throws SQLException {
        contactDAO.updateContactsForSupplier(supplierId, contacts, conn);
    }

    private void updateDiscountNoteForSupplier(long supplierId, DiscountNote discountNote, Connection conn) throws SQLException {
        discountNoteDAO.updateDiscountNoteForSupplier(supplierId, discountNote, conn);
    }

    private void deleteSupplierItems(long supplierId, Connection conn) throws SQLException {
        itemDAO.deleteItemsBySupplierId(supplierId, conn);
    }

    private void deleteSupplierContacts(long supplierId, Connection conn) throws SQLException {
        contactDAO.deleteContactsBySupplierId(supplierId, conn);
    }

    private void deleteDiscountNoteForSupplier(long supplierId, Connection conn) throws SQLException {
        discountNoteDAO.deleteDiscountNoteBySupplierId(supplierId, conn);
    }

    public Supplier getSupplierById(long supplierId) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String address = rs.getString("address");

                    Supplier supplier = new Supplier(name, address);
                    supplier.setItems(itemDAO.getItemsBySupplierId(supplierId));
                    supplier.setContacts(contactDAO.getContactsBySupplierId(supplierId));
                    supplier.setDiscountNote(discountNoteDAO.getDiscountNoteBySupplierId(supplierId));

                    return supplier;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
}
