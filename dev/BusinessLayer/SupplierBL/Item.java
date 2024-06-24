package dev.BusinessLayer.SupplierBL;

import java.util.Objects;

public class Item {
    String itemName;
    float price;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public float getPrice() {
        return price;
    }

    public int getItemId() {
        return itemId;
    }

    public Item(String itemName, float price, int itemId) {
        this.itemName = itemName;
        this.price = price;
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Float.compare(item.price, price) == 0 &&
                Objects.equals(itemName, item.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, price);
    }


    int itemId;

    public String getName() {
        return itemName;
    }
}
