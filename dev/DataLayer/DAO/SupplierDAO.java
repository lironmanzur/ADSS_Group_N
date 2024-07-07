package dev.DataLayer.DAO;

import dev.BusinessLayer.SupplierBL.*;
import dev.DataLayer.DatabaseConnection;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    private final Connection connection;
    private final ItemDAO itemDAO;
    private final ContactDAO contactDAO;
    private final DiscountNoteDAO discountNoteDAO;

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
        String createTableSQL = "CREATE TABLE IF NOT EXISTS suppliers ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "address VARCHAR(255) NOT NULL, "
                + "is_stationary CHAR(1) NOT NULL, "
                + "delivery_days VARCHAR(255)"
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table: " + e.getMessage(), e);
        }
    }

    // Method to add a Supplier to the database
    public void addSupplier(Supplier supplier) throws SQLException {

        String sql = "INSERT INTO suppliers (name, address, is_stationary, delivery_days) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getAddress());
            pstmt.setString(3, supplier instanceof StationarySupplier ? "Y" : "N");
            String deliveryDays = "";
            if (supplier instanceof StationarySupplier) {
                for (DayOfWeek day : ((StationarySupplier) supplier).getDeliveryDays()) {
                    deliveryDays += day.toString() + ",";
                }
                if (!deliveryDays.isEmpty()) {
                    deliveryDays = deliveryDays.substring(0, deliveryDays.length() - 1); // Remove last comma
                }
            }
            pstmt.setString(4, deliveryDays);

            int updateCount = pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int supplierId = generatedKeys.getInt(1);
                    saveSupplierItems(supplierId, supplier.getItems(), conn);
                    saveSupplierContacts(supplierId, supplier.getContacts(), conn);
                    if (supplier.getDiscountNote() != null) saveDiscountNoteForSupplier(supplierId, supplier.getDiscountNote(), conn);
                } else {
                    System.out.println("No ID was generated for the supplier.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException occurred: " + e.getMessage());
            throw e;
        }
    }

    private void saveSupplierItems(int supplierId, List<Item> items, Connection conn) throws SQLException {
        for (Item item : items) {
            itemDAO.addItemForSupplier(supplierId, item, conn);
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
                    int supplierId = rs.getInt("id");
                    String isStationary = rs.getString("is_stationary");
                    String deliveryDays = rs.getString("delivery_days");

                    Supplier supplier;
                    if ("Y".equals(isStationary)) {
                        supplier = new StationarySupplier(name, address);
                        if (deliveryDays != null && !deliveryDays.isEmpty()) {
                            List<DayOfWeek> days = new ArrayList<>();
                            for (String day : deliveryDays.split(",")) {
                                days.add(DayOfWeek.valueOf(day));
                            }
                            ((StationarySupplier) supplier).setDeliveryDays(days);
                        }
                    } else {
                        supplier = new Supplier(name, address);
                    }

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
                int supplierId = rs.getInt("id");
                String isStationary = rs.getString("is_stationary");
                String deliveryDays = rs.getString("delivery_days");

                Supplier supplier;
                if ("Y".equals(isStationary)) {
                    supplier = new StationarySupplier(name, address);
                    if (deliveryDays != null && !deliveryDays.isEmpty()) {
                        List<DayOfWeek> days = new ArrayList<>();
                        for (String day : deliveryDays.split(",")) {
                            days.add(DayOfWeek.valueOf(day));
                        }
                        ((StationarySupplier) supplier).setDeliveryDays(days);
                    }
                } else {
                    supplier = new Supplier(name, address);
                }

                supplier.setItems(itemDAO.getItemsBySupplierId(supplierId));
                supplier.setContacts(contactDAO.getContactsBySupplierId(supplierId));
                supplier.setDiscountNote(discountNoteDAO.getDiscountNoteBySupplierId(supplierId));

                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            System.err.println("SQLException occurred in getAllSuppliers: " + e.getMessage());
            throw e;
        }
        return suppliers;
    }


    // Method to update a Supplier in the database
    public void updateSupplier(Supplier supplier) throws SQLException {
        String sql = "UPDATE suppliers SET address = ?, is_stationary = ?, delivery_days = ? WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getAddress());
            pstmt.setString(2, supplier instanceof StationarySupplier ? "Y" : "N");

            String deliveryDays = "";
            if (supplier instanceof StationarySupplier) {
                for (DayOfWeek day : ((StationarySupplier) supplier).getDeliveryDays()) {
                    deliveryDays += day.toString() + ",";
                }
                if (!deliveryDays.isEmpty()) {
                    deliveryDays = deliveryDays.substring(0, deliveryDays.length() - 1); // Remove last comma
                }
            }
            pstmt.setString(3, deliveryDays);
            pstmt.setString(4, supplier.getName());
            pstmt.executeUpdate();

            int supplierId = getSupplierIdByName(supplier.getName(), conn);
            updateSupplierItems(supplierId, supplier.getItems(), conn);
            updateSupplierContacts(supplierId, supplier.getContacts(), conn);
            if (supplier.getDiscountNote() != null) updateDiscountNoteForSupplier(supplierId, supplier.getDiscountNote(), conn);
        }
    }

    // Method to delete a Supplier from the database
    public void deleteSupplierByName(String name) throws SQLException {
        String sql = "DELETE FROM suppliers WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int supplierId = getSupplierIdByName(name, conn);
            deleteSupplierItems(supplierId, conn);
            deleteSupplierContacts(supplierId, conn);
            deleteDiscountNoteForSupplier(supplierId, conn);

            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    // Helper method to get supplier ID by name
    private int getSupplierIdByName(String name, Connection conn) throws SQLException {
        String sql = "SELECT id FROM suppliers WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    // Helper methods to save, update, and delete related entities
    private void saveSupplierContacts(int supplierId, List<Contact> contacts, Connection conn) throws SQLException {
        for (Contact contact : contacts) {
            contactDAO.addContactForSupplier(supplierId, contact, conn);
        }
    }

    private void saveDiscountNoteForSupplier(int supplierId, DiscountNote discountNote, Connection conn) throws SQLException {
        discountNoteDAO.addDiscountNoteForSupplier(supplierId, discountNote, conn);
    }

    private void updateSupplierItems(int supplierId, List<Item> items, Connection conn) throws SQLException {
        itemDAO.updateItemsForSupplier(supplierId, items, conn);
    }

    private void updateSupplierContacts(int supplierId, List<Contact> contacts, Connection conn) throws SQLException {
        contactDAO.updateContactsForSupplier(supplierId, contacts, conn);
    }

    private void updateDiscountNoteForSupplier(int supplierId, DiscountNote discountNote, Connection conn) throws SQLException {
        discountNoteDAO.updateDiscountNoteForSupplier(supplierId, discountNote, conn);
    }

    private void deleteSupplierItems(int supplierId, Connection conn) throws SQLException {
        itemDAO.deleteItemsBySupplierId(supplierId, conn);
    }

    private void deleteSupplierContacts(int supplierId, Connection conn) throws SQLException {
        contactDAO.deleteContactsBySupplierId(supplierId, conn);
    }

    private void deleteDiscountNoteForSupplier(int supplierId, Connection conn) throws SQLException {
        discountNoteDAO.deleteDiscountNoteBySupplierId(supplierId, conn);
    }

    public Supplier getSupplierById(int supplierId) {
        String sql = "SELECT * FROM suppliers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    String isStationary = rs.getString("is_stationary");
                    String deliveryDays = rs.getString("delivery_days");

                    Supplier supplier;
                    if ("Y".equals(isStationary)) {
                        supplier = new StationarySupplier(name, address);
                        if (deliveryDays != null && !deliveryDays.isEmpty()) {
                            List<DayOfWeek> days = new ArrayList<>();
                            for (String day : deliveryDays.split(",")) {
                                days.add(DayOfWeek.valueOf(day));
                            }
                            ((StationarySupplier) supplier).setDeliveryDays(days);
                        }
                    } else {
                        supplier = new Supplier(name, address);
                    }

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