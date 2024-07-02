package dev.DataLayer.DAO;

import dev.BusinessLayer.SupplierBL.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/yourdatabase";
    private static final String USER = "yourusername";
    private static final String PASSWORD = "yourpassword";

    // Method to add a new contact to the database
    public void addContact(Contact contact) throws SQLException {
        String sql = "INSERT INTO contacts (name, phone_number, address) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getPhoneNumber());
            pstmt.setString(3, contact.getAddress());
            pstmt.executeUpdate();
        }
    }

    // Method to get a contact by name from the database
    public Contact getContactByName(String name) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Contact(
                            rs.getString("name"),
                            rs.getString("phone_number"),
                            rs.getString("address")
                    );
                }
            }
        }
        return null;
    }

    // Method to get all contacts from the database
    public List<Contact> getAllContacts() throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Contact contact = new Contact(
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("address")
                );
                contacts.add(contact);
            }
        }
        return contacts;
    }

    // Method to update a contact in the database
    public void updateContact(Contact contact) throws SQLException {
        String sql = "UPDATE contacts SET phone_number = ?, address = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.getPhoneNumber());
            pstmt.setString(2, contact.getAddress());
            pstmt.setString(3, contact.getName());
            pstmt.executeUpdate();
        }
    }

    // Method to delete a contact from the database
    public void deleteContact(String name) throws SQLException {
        String sql = "DELETE FROM contacts WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    public List<Contact> getContactsBySupplierId(long supplierId) {

        //return all contacts by supplier id
        String sql = "SELECT * FROM contacts WHERE supplier_id = ?";
        List<Contact> contacts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Contact contact = new Contact(
                            rs.getString("name"),
                            rs.getString("phone_number"),
                            rs.getString("address")
                    );
                    contacts.add(contact);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public void addContactForSupplier(long supplierId, Contact contact, Connection conn) {
        //add contact for supplier
        String sql = "INSERT INTO contacts (name, phone_number, address, supplier_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getPhoneNumber());
            pstmt.setString(3, contact.getAddress());
            pstmt.setLong(4, supplierId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateContactsForSupplier(long supplierId, List<Contact> contacts, Connection conn) {
        //update contacts for supplier
        String sql = "UPDATE contacts SET phone_number = ?, address = ? WHERE name = ? AND supplier_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Contact contact : contacts) {
                pstmt.setString(1, contact.getPhoneNumber());
                pstmt.setString(2, contact.getAddress());
                pstmt.setString(3, contact.getName());
                pstmt.setLong(4, supplierId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteContactsBySupplierId(long supplierId, Connection conn) {
        //delete contacts by supplier id
        String sql = "DELETE FROM contacts WHERE supplier_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, supplierId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
