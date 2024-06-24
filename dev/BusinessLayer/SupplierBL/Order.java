package dev.BusinessLayer.SupplierBL;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Order {
    Date creatDate;
    Date providingDate;
    Map<Item, Integer> items;
    private boolean delivered;
    private double totalPriceBeforeDiscount;
    private double totalPriceAfterDiscount;
    Supplier supplier;

    public Order(Supplier supplier, Map<Item, Integer> items) {
        this.items = items;
        this.delivered = false;
        this.supplier= supplier;

    }
    public Order(Supplier supplier) {
        this.items = new HashMap<>();
        this.delivered = false;
        this.supplier= supplier;

    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    public Date getCreatDate() {
        return creatDate;
    }

    public Date getProvidingDate() {
        return providingDate;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public double getTotalPriceBeforeDiscount() {
        return totalPriceBeforeDiscount;
    }

    public double getTotalPriceAfterDiscount() {
        return totalPriceAfterDiscount;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setTotalPriceBeforeDiscount(double totalPriceBeforeDiscount) {
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public void setItems( Map<Item, Integer> items) {
        this.items = items;
    }

    public void setProvidingDate(Date providingDate) {
        this.providingDate = providingDate;
    }

    public void setTotalPriceAfterDiscount(double totalPriceAfterDiscount) {
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }



    public double calculatePrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            BigDecimal itemPrice = BigDecimal.valueOf(item.getPrice());

            Map<Integer, Float> itemDiscounts = supplier.getDiscountNote().getDiscounts().get(item);
            if (itemDiscounts != null) {
                BigDecimal maxApplicableDiscountPrice = findMaxApplicableDiscountPrice(itemDiscounts, quantity);
                if (maxApplicableDiscountPrice != null) {
                    itemPrice = maxApplicableDiscountPrice; // Set item price to the discounted price
                }
            }

            totalPrice = totalPrice.add(itemPrice.multiply(BigDecimal.valueOf(quantity)));
        }

        return totalPrice.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Finds the maximum applicable discount price for the given quantity.
     * It searches through all discounts and selects the highest discount price
     * for which the item quantity meets or exceeds the discount threshold.
     */
    private BigDecimal findMaxApplicableDiscountPrice(Map<Integer, Float> discounts, int quantity) {
        BigDecimal maxPrice = null;

        for (Map.Entry<Integer, Float> discountEntry : discounts.entrySet()) {
            if (quantity >= discountEntry.getKey()) { // Check if quantity is sufficient for the discount
                BigDecimal discountPrice = BigDecimal.valueOf(discountEntry.getValue());
                if (maxPrice == null || discountPrice.compareTo(maxPrice) > 0) {
                    maxPrice = discountPrice; // Update max price if this discount is higher
                }
            }
        }

        return maxPrice;
    }




    public double calculateDiscount(DiscountNote discountNote) {
        BigDecimal sum = BigDecimal.ZERO;  // Use BigDecimal for accurate sum calculations

        for (Item item : this.items.keySet()) {
            BigDecimal itemPrice = BigDecimal.valueOf(item.getPrice());
            int quantity = items.get(item);

            if (!discountNote.getDiscounts().keySet().contains(item)) {
                sum = sum.add(itemPrice.multiply(BigDecimal.valueOf(quantity)));
            } else {
                BigDecimal discountedPrice = BigDecimal.valueOf(calculateItemDiscount(item, discountNote.getDiscounts().get(item), quantity));
                sum = sum.add(discountedPrice.multiply(BigDecimal.valueOf(quantity)));
            }
        }

        // Rounding the sum to 2 decimal places
        sum = sum.setScale(2, RoundingMode.HALF_UP);
        return sum.doubleValue();  // Convert BigDecimal back to double for return
    }

    /**
     * This method needs to return a BigDecimal if used in a BigDecimal context
     * for consistency, or you should handle the conversion within this method.
     */

    public static double calculateItemDiscount( Item item, Map<Integer, Float> map, int count){
        double price=0;
        for (Integer i: map.keySet()){
            if (count>= i){
                price=map.get(i);
            }
        }
        return price;

    }

    public void addItem(Item item, int i) {
        if (items == null) items = new HashMap<>();
        items.put(item, (Integer) i);
    }
}
