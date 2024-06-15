package dev.SupplierServiceLayer;
import dev.BusinessLayer.SupplierBL.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierService {
    private Map<String, Supplier> suppliers;
    static int itemid = 2134256;

    public SupplierService() {
        this.suppliers = new HashMap<>();
    }

    public void addSupplier(Supplier supplier) {
        suppliers.put(supplier.getName(), supplier);
    }

    public Supplier getSupplier(String name) {
        return suppliers.get(name);
    }

    public String addItemToSupplier(String supplierName, String itemName, float price) {
        Supplier supplier = suppliers.get(supplierName);
        if (supplier != null) {
            Item item = new Item(itemName, price, itemid ++);
            supplier.addItem(item);
            return "Item added successfully.";
        } else {
            return "Supplier not found.";
        }
    }

    public String updateItemPrice(String supplierName, String itemName, float newPrice) {
        Supplier supplier = suppliers.get(supplierName);
        if (supplier != null) {
            Item item = supplier.getItemByName(itemName);
            if (item != null) {
                item.setPrice(newPrice);
                return "Price updated successfully.";
            } else {
                return "Item not found.";
            }
        } else {
            return "Supplier not found.";
        }
    }

    public String removeItemFromSupplier(String supplierName, String itemName) {
        Supplier supplier = suppliers.get(supplierName);
        if (supplier != null) {
            Item item = supplier.getItemByName(itemName);
            if (item != null) {
                supplier.removeItem(item);
                return "Item removed successfully.";
            } else {
                return "Item not found.";
            }
        } else {
            return "Supplier not found.";
        }
    }

    public List<String> listItemsBySupplier(String supplierName) {
        Supplier supplier = suppliers.get(supplierName);
        List<String> itemList = new ArrayList<>();
        if (supplier != null) {
            for (Item item : supplier.getItems()) {
                itemList.add(item.getName() + " - $" + item.getPrice());
            }
            return itemList;
        }
        return itemList; // Empty list if no items or supplier not found
    }

    public String addSupplier(String name, String address) {
        if (suppliers.containsKey(name)) return "A supplier with the name " + name + " already exists.";
        Supplier supplier = new Supplier(name, address);
        addSupplier(supplier);
        return "Supplier " + name + " added successfully.";
    }

    public String getSuppliers() {
        StringBuilder suppliers = new StringBuilder();
        for( Supplier supplier: this.suppliers.values()){
            suppliers.append(supplier.getName()).append('\n');
        }
        return suppliers.toString();

    }
}
