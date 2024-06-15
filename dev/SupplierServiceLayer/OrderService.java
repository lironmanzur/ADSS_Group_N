package dev.SupplierServiceLayer;
import dev.BusinessLayer.SupplierBL.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderService {
    private Map<Integer, Order> orders;
    private SupplierService supplierService;  // Assume SupplierService is accessible here
    private int currentOrderId;
    static  int itemID = 4537865;

    public OrderService(SupplierService supplierService) {
        this.orders = new HashMap<>();
        this.currentOrderId = 1;
        this.supplierService = supplierService;
    }

    public int createOrder(String supplierName) {
        Supplier supplier = supplierService.getSupplier(supplierName);
        if (supplier != null) {
            Order order = new Order(supplier);
            int orderId = currentOrderId++;
            orders.put(orderId, order);
            return orderId;
        }
        return -1;  // Return -1 or throw an exception if supplier not found
    }

    public String addItemToOrder(int orderId, String itemName, float price, int quantity) {
        Order order = orders.get(orderId);
        if (order != null) {
            Item item = new Item(itemName, price, itemID ++);  // Assume you can create a new Item here
            order.addItem(item, quantity);
            return "Item added successfully.";
        }
        return "Order not found.";
    }

    public double calculateOrderPrice(int orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            return order.calculatePrice();
        }
        return 0.0;
    }

    public double calculateOrderDiscountedPrice(int orderId) {
        Order order = orders.get(orderId);
        if (order != null && order.getSupplier() != null) {
            DiscountNote discountNote = order.getSupplier().getDiscountNote();  // Assuming getDiscountNote() method exists
            if (discountNote != null) {
                return order.calculateDiscount(discountNote);
            }
        }
        return 0.0;
    }

    public String setOrderDeliveryDate(int orderId, Date providingDate) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setProvidingDate(providingDate);
            return "Delivery date set.";
        }
        return "Order not found.";
    }

    public Order getOrder(int orderId) {
        return orders.get(orderId);
    }
}
