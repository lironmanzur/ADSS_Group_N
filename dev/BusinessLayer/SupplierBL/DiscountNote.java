package dev.BusinessLayer.SupplierBL;

import java.util.Map;

public class DiscountNote {
//    a map of items. for each item thers a map of <ammount, price>
    Map<Item, Map<Integer, Float>> discounts;

    public DiscountNote(Map<Item, Map<Integer, Float>> discounts) {
        this.discounts = discounts;
    }

    public DiscountNote() {

    }

    public void setDiscounts(Map<Item, Map<Integer, Float>> discounts) {
        this.discounts = discounts;
    }

    public Map<Item, Map<Integer, Float>> getDiscounts() {
        return discounts;
    }


}
