package dev.SupplierServiceLayer;
import dev.BusinessLayer.SupplierBL.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderService {
    private Map<Integer, Order> orders;
    private int currentOrderId;

    public OrderService() {
        this.orders = new HashMap<>();
        this.currentOrderId = 1;
    }

    public int createOrder(Supplier supplier) {
        Order order = new Order(supplier);
        int orderId = currentOrderId++;
        orders.put(orderId, order);
        return orderId;
    }

    public void addItemToOrder(int orderId, Item item, int quantity) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.addItem(item, quantity);
        }
    }

    public double calculateOrderPrice(int orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            return order.calculatePrice();
        }
        return 0.0;
    }

    public double calculateOrderDiscountedPrice(int orderId, DiscountNote discountNote) {
        Order order = orders.get(orderId);
        if (order != null) {
            return order.calculateDiscount(discountNote);
        }
        return 0.0;
    }

    public Order getOrder(int orderId) {
        return orders.get(orderId);
    }

    public void setOrderDeliveryDate(int orderId, Date providingDate) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setProvidingDate(providingDate);
        }
    }
}
