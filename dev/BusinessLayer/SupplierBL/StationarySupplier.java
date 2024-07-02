package dev.BusinessLayer.SupplierBL;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StationarySupplier extends Supplier {
    ArrayList<DayOfWeek> deliveryDays;
    HashMap<Item, Integer> regularItems = new HashMap<>();
    public ArrayList<DayOfWeek> getDeliveryDays() {
        return deliveryDays;
    }


    public StationarySupplier(String name, String address) {
        super(name, address);
    }

    public void setDeliveryDays(List<DayOfWeek> deliveryDays) {
        this.deliveryDays = new ArrayList<>(deliveryDays);
    }
}
