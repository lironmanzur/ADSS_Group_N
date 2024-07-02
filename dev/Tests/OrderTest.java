package dev.Tests;
import java.util.HashMap;
import java.util.Map;
import dev.BusinessLayer.SupplierBL.*;

public class OrderTest {

    public static void main(String[] args) {
        // Test Setup
        testCalculatePrice();
        testCalculateDiscount();
    }

    public static void testCalculatePrice() {
        // Setup for test
        Order order = createSampleOrder();

        // Corrected Expected value
        double expectedTotalPrice = (5 * 1.0) + (10 * 3.0) + (15 * 1.5) + (20 * 2.0) + (25 * 3.0);

        // Calculate total price
        double calculatedTotalPrice = order.calculatePrice();

        // Test result
        if (Double.compare(calculatedTotalPrice, expectedTotalPrice) == 0) {
            System.out.println("Test passed: Total price calculation is correct.");
        } else {
            System.out.println("Test failed: Expected total price " + expectedTotalPrice + ", but got " + calculatedTotalPrice + ".");
        }
    }

    public static void testCalculateDiscount() {
        // Setup for test
        Order order = createSampleOrder();
        DiscountNote discountNote = createSampleDiscountNote();

        // Corrected Expected value: manually calculate what the expected discounted price should be
        double expectedDiscountedPrice = 5 * 0.8 + 10 * 3.0 + 15 * 1.3 + 20 * 1.8 + 25 * 2.5;

        // Calculate discounted price
        double calculatedDiscountedPrice = order.calculateDiscount(discountNote);

        // Test result
        if (Double.compare(calculatedDiscountedPrice, expectedDiscountedPrice) == 0) {
            System.out.println("Test passed: Discounted price calculation is correct.");
        } else {
            System.out.println("Test failed: Expected discounted price " + expectedDiscountedPrice + ", but got " + calculatedDiscountedPrice + ".");
        }
    }

    // Helper method to create an order with sample items
    private static Order createSampleOrder() {
        Order order = new Order(new Supplier("moshe hagag", "beer-sheva"));
        order.addItem(new Item("Apple", 1, 45), 5);
        order.addItem(new Item("Banana", 3, 235748), 10);
        order.addItem(new Item("Orange", 1.5f, 4256789), 15);
        order.addItem(new Item("Mango", 2.0f, 245), 20);
        order.addItem(new Item("Pineapple", 3.0f, 765), 25);
        return order;
    }

    // Helper method to create a sample discount note
    private static DiscountNote createSampleDiscountNote() {
        Map<Item, Map<Integer, Float>> discountMap = new HashMap<>();
        discountMap.put(new Item("Apple", 1.0f, 9789), createDiscountMap(5, 0.8f));
        discountMap.put(new Item("Orange", 1.5f, 477), createDiscountMap(15, 1.3f));
        discountMap.put(new Item("Mango", 2.0f, 678839), createDiscountMap(20, 1.8f));
        discountMap.put(new Item("Pineapple", 3.0f, 324), createDiscountMap(25, 2.5f));
        DiscountNote discountNote = new DiscountNote();
        discountNote.setDisscounts(discountMap);
        return discountNote;
    }

    // Helper method to create a discount map for an item
    private static Map<Integer, Float> createDiscountMap(int threshold, float price) {
        Map<Integer, Float> discountMap = new HashMap<>();
        discountMap.put(threshold, price);
        return discountMap;
    }
}
