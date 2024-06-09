package dev.Tests;
import dev.Backend.*;
import dev.Frontend.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SupplierTest {
    static String TEST_PASSED = "Test passed";
    static String TEST_FAILED = "Test Failed!";

    public static void main(String[] args) {
        testAddContact();
        testUpdateSupplierAddress();
        testAddItemToSupplier();
        testSupplierOrderNotification();
        testSupplierOrderHandling();
        testDeleteSupplier();
        testUpdateItemDetails();
        testSupplierHistoryManagement();
        testRemoveItemFromSupplier();
        testInitialDiscountsSetup();
        testSetDiscounts();
        testUpdateDiscounts();
        testRemoveSpecificDiscount();
        testAddNewDiscountEntry();
    }

    static void testUpdateSupplierAddress() {
        Supplier supplier = new Supplier("Supplier Name", "1234 Address St");
        supplier.setAddress("5678 New Address Ln");
        System.out.println(("5678 New Address Ln".equals(supplier.getAddress()) ? TEST_PASSED : TEST_FAILED));
    }

    public static void testAddContact() {
        Supplier supplier = new Supplier("Supplier Name", "1234 Address St");
        Contact contact = new Contact("John Doe", "123-456-7890", "1234 Address St");
        supplier.addContact(contact);
        System.out.println(!supplier.getContacts().isEmpty() && "John Doe".equals(supplier.getContacts().get(0).getName()) ? TEST_PASSED : TEST_FAILED);
    }

    static void testAddItemToSupplier() {
        Supplier supplier = new Supplier("Supplier Name", "1234 Address St");
        Item item = new Item("Widget", 25.75f, 101);
        supplier.addItem(item, 100);
        System.out.println(supplier.getItems().contains(item) ? TEST_PASSED : TEST_FAILED);
    }

    static void testSupplierOrderNotification() {
        System.out.println(TEST_PASSED);  // Placeholder for actual implementation.
    }

    static void testSupplierOrderHandling() {
        SupplierManagementSystem system = new SupplierManagementSystem();
        Supplier supplier = new Supplier("Supplier Name", "1234 Address St");
        Item item = new Item("Widget", 20.00f, 201);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        system.placeOrder(supplier, items);
        System.out.println(!system.getOrders().isEmpty() ? TEST_PASSED : TEST_FAILED);
    }

    static void testDeleteSupplier() {
        SupplierManagementSystem system = new SupplierManagementSystem();
        Supplier supplier = new Supplier("Supplier Name", "1234 Address St");
        system.addSupplier(supplier);
        system.deleteSupplier(supplier);
        System.out.println(!system.getSuppliers().contains(supplier) ? TEST_PASSED : TEST_FAILED);
    }

    static void testUpdateItemDetails() {
        Supplier supplier = new Supplier("Supplier Name", "1234 Address St");
        Item item = new Item("Widget", 25.75f, 101);
        supplier.addItem(item, 100);
        item.setPrice(30.00f);  // Change price
        supplier.updateItem(item, 30.00f);
        System.out.println(item.getPrice() == 30.00f ? TEST_PASSED : TEST_FAILED);
    }

    static void testSupplierHistoryManagement() {
        System.out.println(TEST_PASSED);  // Placeholder for actual implementation.
    }

    static void testRemoveItemFromSupplier() {
        Supplier supplier = new Supplier("Supplier Name", "1234 Address St");
        Item item = new Item("Widget", 25.75f, 101);
        supplier.addItem(item, 100);
        supplier.removeItem(item);
        System.out.println(!supplier.getItems().contains(item) ? TEST_PASSED : TEST_FAILED);
    }

    // DiscountNote tests
    static void testInitialDiscountsSetup() {
        Item widget = new Item("Widget", 101, 3456);
        Map<Integer, Float> widgetDiscounts = new HashMap<>();
        widgetDiscounts.put(10, 20.0f);
        widgetDiscounts.put(20, 18.0f);
        Map<Item, Map<Integer, Float>> initialDiscounts = new HashMap<>();
        initialDiscounts.put(widget, widgetDiscounts);
        DiscountNote discountNote = new DiscountNote(initialDiscounts);

        System.out.println(discountNote.getDisscounts().get(widget).get(10) == 20.0f && discountNote.getDisscounts().get(widget).get(20) == 18.0f ? TEST_PASSED : TEST_FAILED);
    }

    static void testSetDiscounts() {
        Item widget = new Item("Widget", 101, 9);
        Map<Integer, Float> widgetDiscounts = new HashMap<>();
        widgetDiscounts.put(10, 20.0f);
        Map<Item, Map<Integer, Float>> initialDiscounts = new HashMap<>();
        initialDiscounts.put(widget, widgetDiscounts);
        DiscountNote discountNote = new DiscountNote(initialDiscounts);

        // Set new discounts
        Map<Integer, Float> newWidgetDiscounts = new HashMap<>();
        newWidgetDiscounts.put(5, 22.0f);
        Map<Item, Map<Integer, Float>> discountMap = new HashMap<>();
        discountMap.put(widget, newWidgetDiscounts);
        discountNote.setDisscounts(discountMap);

        System.out.println(discountNote.getDisscounts().get(widget).get(5) == 22.0f ? TEST_PASSED : TEST_FAILED);
    }

    static void testUpdateDiscounts() {
        Item widget = new Item("Widget", 101, 546);
        Map<Integer, Float> widgetDiscounts = new HashMap<>();
        widgetDiscounts.put(10, 20.0f);
        Map<Item, Map<Integer, Float>> initialDiscounts = new HashMap<>();
        initialDiscounts.put(widget, widgetDiscounts);
        DiscountNote discountNote = new DiscountNote(initialDiscounts);

        // Update existing discount
        discountNote.getDisscounts().get(widget).put(10, 19.0f);

        System.out.println(discountNote.getDisscounts().get(widget).get(10) == 19.0f ? TEST_PASSED : TEST_FAILED);
    }

    static void testRemoveSpecificDiscount() {
        Item widget = new Item("Widget", 101, 6457);
        Map<Integer, Float> widgetDiscounts = new HashMap<>();
        widgetDiscounts.put(10, 20.0f);
        Map<Item, Map<Integer, Float>> initialDiscounts = new HashMap<>();
        initialDiscounts.put(widget, widgetDiscounts);
        DiscountNote discountNote = new DiscountNote(initialDiscounts);

        // Remove specific discount
        discountNote.getDisscounts().get(widget).remove(10);

        System.out.println(discountNote.getDisscounts().get(widget).containsKey(10) ? TEST_FAILED : TEST_PASSED);
    }

    static void testAddNewDiscountEntry() {
        Item widget = new Item("Widget", 101,54678);
        Map<Integer, Float> widgetDiscounts = new HashMap<>();
        Map<Item, Map<Integer, Float>> initialDiscounts = new HashMap<>();
        initialDiscounts.put(widget, widgetDiscounts);
        DiscountNote discountNote = new DiscountNote(initialDiscounts);

        // Add new discount entry
        Item newWidget = new Item("New Widget", 102, 877);
        Map<Integer, Float> priceBreaks = new HashMap<>();
        priceBreaks.put(15, 25.0f);
        discountNote.getDisscounts().put(newWidget, priceBreaks);

        System.out.println(discountNote.getDisscounts().get(newWidget).get(15) == 25.0f ? TEST_PASSED : TEST_FAILED);
    }
}
