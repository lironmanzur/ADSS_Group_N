package dev.BusinessLayer.SupplierBL;

import java.util.ArrayList;
import java.util.Date;
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



    public double calculatePrice(){
        double sum=0;
        for (Item item:this.items.keySet()){
            sum+=item.getPrice()*items.get(item);
        }
        return sum;
    }

    public double calculateDiscount(DiscountNote discountNote){
        double sum=0;
        for (Item item:this.items.keySet()){
            if (!discountNote.getDisscounts().keySet().contains(item)){
                sum+=item.getPrice()*items.get(item);
            }
            else {
                sum+=calculateItemDiscount(item, discountNote.getDisscounts().get(item), items.get(item));
            }
        }
        return sum;
    }

    public static double calculateItemDiscount( Item item, Map<Integer, Float> map, int count){
        double price=0;
        for (Integer i: map.keySet()){
            if (count>= i){
                price=map.get(i);
            }
        }
        return price*count;

    }
}
