package dev;

import dev.BusinessLayer.SupplierBL.Item;
import dev.BusinessLayer.SupplierBL.Supplier;
import dev.Frontend.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        SupplierManagementSystem managementSystem = new SupplierManagementSystem();

        // Create suppliers
        Supplier supplier1 = new Supplier("Global Parts", "1234 Industrial Way");
        Supplier supplier2 = new Supplier("Widget Corp", "5678 Widget Blvd");

        // Add suppliers to the management system
        managementSystem.addSupplier(supplier1);
        managementSystem.addSupplier(supplier2);

        // Create items and add to suppliers
        Item item1 = new Item("Gadget",  19.99f, 001);
        Item item2 = new Item("Widget",  29.99f, 002);

        supplier1.addItem(item1);
        supplier2.addItem(item2);

        // Simulate placing an order for items from Supplier1
        ArrayList<Item> orderItems = new ArrayList<>();
        orderItems.add(item1);
        managementSystem.placeOrder(supplier1, orderItems);

        // Output the current status of suppliers
        for (Supplier supplier : managementSystem.getSuppliers()) {
            System.out.println("Supplier: " + supplier.getName());
            System.out.println("Supplier: " + supplier.getName());
            for (Item item : supplier.getItems()) {
                System.out.println("Item: " + item.getName() + ", Price: " + item.getPrice());
            }
        }
    }
}
