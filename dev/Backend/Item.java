package dev.Backend;

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

    int itemId;

    public String getName() {
        return itemName;
    }
}
