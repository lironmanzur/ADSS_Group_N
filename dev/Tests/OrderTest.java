package dev.Tests;
import java.util.HashMap;
import java.util.Map;

public class OrderTest {
    public static void main(String[] args) {
        // Create items
        Item item1 = new Item("item1", 10.0);
        Item item2 = new Item("item2", 20.0);

        // Create shopping cart and add items
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(item1, 2); // 2 units of item1
        cart.addItem(item2, 3); // 3 units of item2

        // Test calculatePrice method
        double totalPrice = cart.calculatePrice();
        System.out.println("Total price: " + totalPrice); // Expected: 80.0

        // Create discount note
        DiscountNote discountNote = new DiscountNote();
        Map<Integer, Float> discountMap1 = new HashMap<>();
        discountMap1.put(2, 0.8f); // 20% discount if buying 2 or more
        discountNote.addDiscount(item1, discountMap1);

        Map<Integer, Float> discountMap2 = new HashMap<>();
        discountMap2.put(3, 0.7f); // 30% discount if buying 3 or more
        discountNote.addDiscount(item2, discountMap2);

        // Test calculateDiscount method
        double discountedPrice = cart.calculateDiscount(discountNote);
        System.out.println("Discounted price: " + discountedPrice); // Expected: 64.0

        // Test calculateItemDiscount method
        double itemDiscountedPrice1 = ShoppingCart.calculateItemDiscount(item1, discountMap1, 2);
        System.out.println("Item1 discounted price for 2 units: " + itemDiscountedPrice1); // Expected: 16.0

        double itemDiscountedPrice2 = ShoppingCart.calculateItemDiscount(item2, discountMap2, 3);
        System.out.println("Item2 discounted price for 3 units: " + itemDiscountedPrice2); // Expected: 42.0
    }
}

class Item {
    private String name;
    private double price;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    // Assuming equals and hashCode are overridden for correct map behavior
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return Double.compare(item.price, price) == 0 &&
                name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

class DiscountNote {
    private Map<Item, Map<Integer, Float>> discounts = new HashMap<>();

    public void addDiscount(Item item, Map<Integer, Float> discountMap) {
        discounts.put(item, discountMap);
    }

    public Map<Item, Map<Integer, Float>> getDisscounts() {
        return discounts;
    }
}

class ShoppingCart {
    Map<Item, Integer> items = new HashMap<>();

    public void addItem(Item item, int quantity) {
        items.put(item, quantity);
    }

    public double calculatePrice() {
        double sum = 0;
        for (Item item : this.items.keySet()) {
            sum += item.getPrice() * items.get(item);
        }
        return sum;
    }

    public double calculateDiscount(DiscountNote discountNote) {
        double sum = 0;
        for (Item item : this.items.keySet()) {
            if (!discountNote.getDisscounts().keySet().contains(item)) {
                sum += item.getPrice() * items.get(item);
            } else {
                sum += calculateItemDiscount(item, discountNote.getDisscounts().get(item), items.get(item));
            }
        }
        return sum;
    }

    public static double calculateItemDiscount(Item item, Map<Integer, Float> map, int count) {
        double price = 0;
        for (Integer i : map.keySet()) {
            if (count >= i) {
                price = map.get(i);
            }
        }
        return price * count;
    }
}

