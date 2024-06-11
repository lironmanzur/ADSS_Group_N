package dev.Frontend;

import dev.Backend.Item;
import dev.Backend.Order;
import dev.Backend.Supplier;

import java.util.ArrayList;

public class SupplierManagementSystem {
    private ArrayList<Supplier> suppliers;
    private ArrayList<Order> orders;

    public void createSupplier(String name, String address) {
        Supplier supplier = new Supplier(name, address);
        suppliers.add(supplier);
    }

    public void placeOrder(Supplier supplier, ArrayList<Item> items) {
        // Logic to place an order
        Order order = new Order(supplier, items);
        if (orders == null) orders = new ArrayList<Order>();
        orders.add(order);
        // Notify the supplier if needed
    }

    public void updateItemDetails(Supplier supplier, Item item, int price) {
        // Update item details in supplier's catalog
        supplier.updateItem(item, price);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addSupplier(Supplier supplier) {
        if (suppliers == null) suppliers = new ArrayList<>();
        this.suppliers.add(supplier);
    }

    public void deleteSupplier(Supplier supplier) {
        suppliers.remove(supplier);
    }

    public ArrayList<Supplier> getSuppliers() {
        return suppliers;
    }

    // Additional functionalities as required

    public static class Main {
        public static void main(String[] args) {
            System.out.println("Hello world!");
        }
    }
}
