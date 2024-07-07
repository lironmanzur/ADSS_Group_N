package dev.Tests;
import dev.BusinessLayer.SupplierBL.*;
import dev.DataLayer.DAO.ContactDAO;
import dev.DataLayer.DAO.DiscountNoteDAO;
import dev.DataLayer.DAO.ItemDAO;
import dev.Frontend.*;
import dev.DataLayer.DAO.SupplierDAO;
import dev.SupplierServiceLayer.SupplierService;

import java.sql.SQLException;
import java.util.List;

public class SupplierTest {
    static String TEST_PASSED = "Test passed";
    static String TEST_FAILED = "Test Failed!";

    private static SupplierService supplierService; // Assumed to be initialized with the DAO

    public static void main(String[] args) {
        // Initialize with test DAOs or mocks
        supplierService = SupplierService.getInstance();
        testLoadDataFromDB();
        testSaveDataToDB();
        // Other tests
    }

    static void testLoadDataFromDB() {
        try {
            // Assuming the test setup includes inserting suppliers into the database
            supplierService.loadDataFromDB(); // Load data from DB
            List<Supplier> suppliers = supplierService.getSuppliersList();
            if (!suppliers.isEmpty()) {
                System.out.println(TEST_PASSED);
            } else {
                System.out.println(TEST_FAILED);
            }
        } catch (Exception e) {
            System.out.println(TEST_FAILED + ": " + e.getMessage());
        }
    }

    static void testSaveDataToDB() {
        try {
            // Create a new supplier and add it to service
            Supplier newSupplier = new Supplier("Test Supplier", "Test Address");
            supplierService.addSupplier(newSupplier);
            supplierService.saveDataToDB(); // Save data to DB

            // Optionally, re-fetch or clear and reload data to verify persistence
            supplierService.loadDataFromDB();
            Supplier loadedSupplier = supplierService.getSupplier("Test Supplier");
            if (loadedSupplier != null && "Test Address".equals(loadedSupplier.getAddress())) {
                System.out.println(TEST_PASSED);
            } else {
                System.out.println(TEST_FAILED);
            }
        } catch (Exception e) {
            System.out.println(TEST_FAILED + ": " + e.getMessage());
        }
    }

    // Existing test methods...
}
