package dev.DataLayer.DTO;

import dev.BusinessLayer.SupplierBL.Item;

import java.util.Map;

public class DiscountNoteDTO {
    private Map <Item, Map<Integer, Float>> discounts;
    public DiscountNoteDTO(Map<Item, Map<Integer, Float>> discounts){
        this.discounts = discounts;
    }
    public Map<Item, Map<Integer, Float>> getDiscounts() {
        return discounts;
    }
}
