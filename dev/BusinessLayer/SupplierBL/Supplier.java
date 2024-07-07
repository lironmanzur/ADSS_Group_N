package dev.BusinessLayer.SupplierBL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Supplier {
    private String name;
    private String address;
    static int idCounter = 435;
    int id = idCounter++;
    private DiscountNote discountNote;
    private ArrayList<Contact> contacts;  // List of contact persons
    private ArrayList<Item> items;

    public Supplier(String name, String address) {
        this.name = name;
        this.address = address;
        this.contacts = new ArrayList<Contact>();
        this.items = new ArrayList<Item>();
    }



    public void setDiscountNote(DiscountNote discountNote) {
        this.discountNote = discountNote;
        discountNote.setId(this.id);
    }

    public void createEmptyDiscountNoteIfNotExists(){
        if (discountNote == null) discountNote = new DiscountNote(new HashMap<Item, Map<Integer, Float>>(), this.id);
    }

    public void addDiscount(Item item, Map<Integer, Float> map){
        createEmptyDiscountNoteIfNotExists();
        discountNote.getDiscounts().put(item, map);

    }



    public DiscountNote getDiscountNote() {
        return discountNote;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void updateItem(Item item, float newPrice) {
        item.setPrice(newPrice);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Item getItemByName(String itemName) {
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;  // Return null if no item matches the given name
    }

    public Item getItemByID(int itemID) {
        for (Item item : items) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;  // Return null if no item matches the given ID
    }

    public long getId() {
        return id;
    }

    public void setItems(List<Item> itemsBySupplierId) {
        this.items = new ArrayList<>(itemsBySupplierId);
    }

    public void setContacts(List<Contact> contactsBySupplierId) {
        this.contacts = new ArrayList<>(contactsBySupplierId);
    }

    public void setId(int supplierId) {
        this.id = supplierId;
    }
}
