package dev.Backend;

import java.util.Map;

public class DiscountNote {
//    a map of items. for each item thers a map of <ammount, price>
    Map<Item, Map<Integer, Float>> disscounts;

    public DiscountNote(Map<Item, Map<Integer, Float>> disscounts) {
        this.disscounts = disscounts;
    }

    public void setDisscounts(Map<Item, Map<Integer, Float>> disscounts) {
        this.disscounts = disscounts;
    }

    public Map<Item, Map<Integer, Float>> getDisscounts() {
        return disscounts;
    }
}
