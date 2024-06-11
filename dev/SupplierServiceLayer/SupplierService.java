package dev.SupplierServiceLayer;
import dev.BusinessLayer.SupplierBL.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierService {
    private Map< String, Supplier> suppliers;
    public SupplierService() {
        this.suppliers =  new HashMap<>();
    }


    public void addSupplier(Supplier supplier) {
        suppliers.put(supplier.getName(), supplier);
    }

    public Supplier getSupplier(String name) {
        return suppliers.get(name);
    }


    public void addItemToSupplier( String supplierName , Item item ) {
        Supplier supplier = suppliers.get(supplierName);
        if (supplier != null)
            supplier.addItem(item);

    }

    public void updateItemPrice(String supplierName, Item item, float newPrice) {
        Supplier supplier = suppliers.get(supplierName);
        if (supplier != null)
            supplier.updateItem(item, newPrice);

    }



    public void removeItemFromSupplier(String supplierName , Item item ) {
        Supplier supplier = suppliers.get(supplierName);
        if (supplier != null)
            supplier.removeItem(item);

    }


    public List<Item> getItemsBySupplier(String supplierName) {
        Supplier supplier = suppliers.get(supplierName);

        if (supplier!=null) {
            return supplier.getItems();
        }
        return new ArrayList<>();
    }
}
