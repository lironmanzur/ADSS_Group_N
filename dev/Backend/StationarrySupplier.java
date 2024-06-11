package dev.Backend;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class StationarrySupplier extends Supplier {
    ArrayList<DayOfWeek> deliveryDays;

    public StationarrySupplier(String name, String address) {
        super(name, address);
    }
}
