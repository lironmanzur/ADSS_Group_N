package dev.Backend;

import java.util.ArrayList;

public class Supplier {
    private String name;
    private String address;
    private SupplierCard card;
    private ArrayList<Contact> contacts;  // List of contact persons
    private ArrayList<Item> items;

    public Supplier(String name, String address) {
        this.name = name;
        this.address = address;
        contacts = new ArrayList<Contact>();
        items = new ArrayList<Item>();
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
    public ArrayList<Contact> getContacts(){
        return contacts;
    }

    public void updateItem(Item item, float v) {
        item.setPrice(v);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}
