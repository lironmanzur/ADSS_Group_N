package dev.Backend;

import java.util.ArrayList;
import java.util.Date;


public class Order {
    Date orderDate;
    Date providingDate;
    ArrayList<Item> items;

    public Order(Supplier supplier, ArrayList<Item> items) {
        this.items = items;
    }
}
