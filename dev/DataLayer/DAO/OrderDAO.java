package dev.DataLayer.DAO;

import dev.BusinessLayer.SupplierBL.Item;
import dev.BusinessLayer.SupplierBL.Order;
import dev.BusinessLayer.SupplierBL.Supplier;

import java.sql.*;
import java.sql.Date;
import java.util.*;


public class OrderDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/yourdatabase";
    private static final String USER = "yourusername";
    private static final String PASSWORD = "yourpassword";

    private SupplierDAO supplierDAO;
    private ItemDAO itemDAO;

    public OrderDAO(SupplierDAO supplierDAO, ItemDAO itemDAO) {
        this.supplierDAO = supplierDAO;
        this.itemDAO = itemDAO;
    }

    // Method to add an Order to the database
    public void addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (supplier_id, create_date, providing_date, delivered, total_price_before_discount, total_price_after_discount) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, order.getSupplier().getId());
            pstmt.setDate(2, new java.sql.Date(order.getCreatDate().getTime()));
            pstmt.setDate(3, new java.sql.Date(order.getProvidingDate().getTime()));
            pstmt.setBoolean(4, order.isDelivered());
            pstmt.setDouble(5, order.getTotalPriceBeforeDiscount());
            pstmt.setDouble(6, order.getTotalPriceAfterDiscount());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long orderId = generatedKeys.getLong(1);
                    saveOrderItems(orderId, order.getItems(), conn);
                }
            }
        }
    }

    // Method to get an Order by ID from the database
    public Order getOrderById(long orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    long supplierId = rs.getLong("supplier_id");
                    Date createDate = rs.getDate("create_date");
                    Date providingDate = rs.getDate("providing_date");
                    boolean delivered = rs.getBoolean("delivered");
                    double totalPriceBeforeDiscount = rs.getDouble("total_price_before_discount");
                    double totalPriceAfterDiscount = rs.getDouble("total_price_after_discount");

                    Supplier supplier = supplierDAO.getSupplierById(supplierId);
                    Map<Item, Integer> items = getOrderItems(orderId, conn);

                    Order order = new Order(supplier, items);
                    order.setCreatDate(createDate);
                    order.setProvidingDate(providingDate);
                    order.setDelivered(delivered);
                    order.setTotalPriceBeforeDiscount(totalPriceBeforeDiscount);
                    order.setTotalPriceAfterDiscount(totalPriceAfterDiscount);

                    return order;
                }
            }
        }
        return null;
    }

    // Method to get all Orders from the database
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                long orderId = rs.getLong("order_id");
                orders.add(getOrderById(orderId));
            }
        }
        return orders;
    }

    // Method to update an Order in the database
    public void updateOrder(Order order) throws SQLException {
        String sql = "UPDATE orders SET supplier_id = ?, create_date = ?, providing_date = ?, delivered = ?, total_price_before_discount = ?, total_price_after_discount = ? WHERE order_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, order.getSupplier().getId());
            pstmt.setDate(2, new java.sql.Date(order.getCreatDate().getTime()));
            pstmt.setDate(3, new java.sql.Date(order.getProvidingDate().getTime()));
            pstmt.setBoolean(4, order.isDelivered());
            pstmt.setDouble(5, order.getTotalPriceBeforeDiscount());
            pstmt.setDouble(6, order.getTotalPriceAfterDiscount());
            pstmt.setLong(7, order.getId());
            pstmt.executeUpdate();

            deleteOrderItems(order.getId(), conn);
            saveOrderItems(order.getId(), order.getItems(), conn);
        }
    }

    // Method to delete an Order from the database
    public void deleteOrderById(long orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            pstmt.executeUpdate();

            deleteOrderItems(orderId, conn);
        }
    }

    // Helper method to save order items
    private void saveOrderItems(long orderId, Map<Item, Integer> items, Connection conn) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, item_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                pstmt.setLong(1, orderId);
                pstmt.setInt(2, entry.getKey().getItemID());
                pstmt.setInt(3, entry.getValue());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    // Helper method to get order items
    private Map<Item, Integer> getOrderItems(long orderId, Connection conn) throws SQLException {
        Map<Item, Integer> items = new HashMap<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    int quantity = rs.getInt("quantity");
                    Item item = itemDAO.getItemById(itemId);
                    items.put(item, quantity);
                }
            }
        }
        return items;
    }

    // Helper method to delete order items
    private void deleteOrderItems(long orderId, Connection conn) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            pstmt.executeUpdate();
        }
    }
}
