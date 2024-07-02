package dev.SupplierServiceLayer;
import dev.BusinessLayer.SupplierBL.*;

import java.time.LocalDate;
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

    public String addDiscountToSupplier(String supplierName, String itemName, String quantity, String discountPrice) {
        // Check if the supplier exists
        Supplier supplier = suppliers.get(supplierName);
        if (supplier == null) {
            return "Supplier not found.";
        }

        // Check if the item exists in the supplier's inventory
        Item item = supplier.getItemByName(itemName);
        if (item == null) {
            return "Item not found.";
        }

        // Validate and convert quantity and discountPrice to integer and float
        try {
            int qty = Integer.parseInt(quantity);
            float price = Float.parseFloat(discountPrice);

            // Create a map for the discount
            Map<Integer, Float> discountMap = new HashMap<>();
            discountMap.put(qty, price);

            // Add the discount to the supplier
            supplier.addDiscount(item, discountMap);
            return "Discount added successfully to " + itemName + " for supplier " + supplierName + ".";
        } catch (NumberFormatException e) {
            return "Invalid format for quantity or discount price. Please enter valid numbers.";
        }
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
    //every morning this function will run
    public void automatedOrder(){
        for(Supplier supplier: suppliers.values()){
            if (supplier instanceof StationarySupplier){
                StationarySupplier stationarySupplier = (StationarySupplier) supplier;
                if (stationarySupplier.getDeliveryDays().contains(LocalDate.now().getDayOfWeek())) performAutomaticOrder(stationarySupplier);

            }

        }
    }

    private void performAutomaticOrder(StationarySupplier stationarySupplier) {
        //todo implement
    }

    public String addSupplier(String name, String address) {
        if (suppliers.containsKey(name)) return "A supplier with the name " + name + " already exists.";
        Supplier supplier = new Supplier(name, address);
        addSupplier(supplier);
        //testcode
//        Item item1 = new Item("apple", 5, 3245374);
//        HashMap<Integer, Float> aplleDiscount = new HashMap<Integer, Float>();
//        HashMap <Item, HashMap<Integer, Float> myhasmap = new HashMap<>();
//        myhasmap.put(item1, myhasmap);


        supplier.setDiscountNote(new DiscountNote(new HashMap<>()));
        return "Supplier " + name + " added successfully.";
    }

    public String getSuppliers() {
        StringBuilder suppliers = new StringBuilder();
        for( Supplier supplier: this.suppliers.values()){
            suppliers.append(supplier.getName()).append('\n');
        }
        return suppliers.toString();

    }
    public ArrayList<Supplier> getSuppliersList(){
        return new ArrayList<>(suppliers.values());
    }

}
