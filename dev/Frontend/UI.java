package dev.Frontend;

import dev.BusinessLayer.SupplierBL.DiscountNote;
import dev.BusinessLayer.SupplierBL.Item;
import dev.BusinessLayer.SupplierBL.Order;
import dev.BusinessLayer.SupplierBL.Supplier;
import dev.SupplierServiceLayer.OrderService;
import dev.SupplierServiceLayer.SupplierService;

import java.util.*;
import java.text.SimpleDateFormat;

public class UI {
    static SupplierService supplierService = new SupplierService();
    static OrderService orderService = new OrderService(supplierService);

    static Scanner scanner = new Scanner(System.in);
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        System.out.println("would you like to load demo data? \nif yes enter 'y'. else enter 'n' ");
        String ans = scanner.nextLine();
        if (ans.equals("y") || ans.equals("Y")){ loadDemoData();}

        String[] options = {
                "1) Add a new supplier",
                "2) Add item to a supplier",
                "3) Update item price",
                "4) Remove item from supplier",
                "5) List items by supplier",
                "6) Create a new order",
                "7) Add item to order",
                "8) Calculate order price",
                "9) Set order delivery date",
                "a) List Suppliers",
                "b) Add discount to supplier",
                "0) Exit"

        };
        while (true) {
            printOptions(options);
            String input = scanner.nextLine();
            if (input.length() != 1) {
                System.out.println("Invalid input. Please choose an option from the list.");
            } else {
                char inputChar = input.charAt(0);
                switch(inputChar) {
                    case '1':
                        addSupplier();
                        break;
                    case '2':
                        addItemToSupplier();
                        break;
                    case '3':
                        updateItemPrice();
                        break;
                    case '4':
                        removeItemFromSupplier();
                        break;
                    case '5':
                        listItemsBySupplier();
                        break;
                    case '6':
                        createOrder();
                        break;
                    case '7':
                        addItemToOrder();
                        break;
                    case '8':
                        calculateOrderPrice();
                        break;
                    case '9':
                        setOrderDeliveryDate();
                        break;
                    case 'a':
                        listSuppliers();
                        break;
                    case 'b':
                        assDiscountToSupplier();
                        break;
                    case '0':
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Option not recognized. Please try again.");
                }
            }
        }
    }

    private static void listSuppliers() {
        System.out.println( supplierService.getSuppliers());
    }

    private static void assDiscountToSupplier() {
        System.out.println("choose a suplier: here is a list of supliers\n" + supplierService.getSuppliers());
        String supplierName = scanner.nextLine();
        System.out.println("enter item name\n");
        String itemName = scanner.nextLine();
        System.out.println("enter item Ammount\n");
        String itemAmmount = scanner.nextLine();
        System.out.println("enter item price\n");
        String itemprice = scanner.nextLine();
        System.out.println(supplierService.addDiscountToSupplier(supplierName, itemName, itemAmmount, itemprice));

    }


    static void createOrder() {
        System.out.println("Enter supplier name for the order:");
        String supplierName = scanner.nextLine();
        int orderId = orderService.createOrder(supplierName);
        if (orderId == -1) {
            System.out.println("Supplier not found. Order could not be created.");
        } else {
            System.out.println("Order created successfully. Order ID: " + orderId);
        }
    }

    static void addItemToOrder() {
        try {
            System.out.println("Enter order ID:");
            int orderId = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter item name:");
            String itemName = scanner.nextLine();
//            System.out.println("Enter item price:");
//            float price = Float.parseFloat(scanner.nextLine());
            System.out.println("Enter quantity:");
            int quantity = Integer.parseInt(scanner.nextLine());
            String result = orderService.addItemToOrder(orderId, itemName, quantity);
            System.out.println(result);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please ensure all entries are correct.");
        }
    }




    static void calculateOrderPrice() {
        System.out.println("Enter order ID:");
        int orderId = Integer.parseInt(scanner.nextLine());
        double price = orderService.calculateOrderPrice(orderId);
        System.out.println("Total order price is: $" + price);
    }

    static void setOrderDeliveryDate() {
        System.out.println("Enter order ID:");
        int orderId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter delivery date (dd/MM/yyyy):");
        try {
            Date deliveryDate = dateFormatter.parse(scanner.nextLine());
            String result = orderService.setOrderDeliveryDate(orderId, deliveryDate);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy format.");
        }
    }



    static void addItemToSupplier() {
        System.out.println("Enter supplier name:");
        String supplierName = scanner.nextLine();
        System.out.println("Enter item name:");
        String itemName = scanner.nextLine();
        System.out.println("Enter item price:");
        try {
            float price = Float.parseFloat(scanner.nextLine());
            String result = supplierService.addItemToSupplier(supplierName, itemName, price);
            System.out.println(result);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price entered. Please enter a valid numeric value.");
        }
    }

    static void updateItemPrice() {
        System.out.println("Enter supplier name:");
        String supplierName = scanner.nextLine();
        System.out.println("Enter item name:");
        String itemName = scanner.nextLine();
        System.out.println("Enter new price:");
        try {
            float newPrice = Float.parseFloat(scanner.nextLine());
            String result = supplierService.updateItemPrice(supplierName, itemName, newPrice);
            System.out.println(result);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price entered. Please enter a valid numeric value.");
        }
    }

    static void removeItemFromSupplier() {
        System.out.println("Enter supplier name:");
        String supplierName = scanner.nextLine();
        System.out.println("Enter item name:");
        String itemName = scanner.nextLine();
        String result = supplierService.removeItemFromSupplier(supplierName, itemName);
        System.out.println(result);
    }

    static void listItemsBySupplier() {
        System.out.println("Enter supplier name:");
        String supplierName = scanner.nextLine();
        List<String> items = supplierService.listItemsBySupplier(supplierName);
        if (items.isEmpty()) {
            System.out.println("No items found for this supplier.");
        } else {
            items.forEach(System.out::println);
        }
    }

    static void printOptions(String[] options) {
        System.out.println("Please choose an option from the list:");
        for (String option : options) {
            System.out.println(option);
        }
    }

    static void addSupplier() {
        System.out.println("Please enter supplier name:");
        String name = scanner.nextLine();
        System.out.println("Please enter supplier address:");
        String address = scanner.nextLine();
        String answer = supplierService.addSupplier(name, address);
        System.out.println(answer);
    }

    static void loadDemoData() {
        // Adding Suppliers
        supplierService.addSupplier("Supplier A", "1234 Market St.");
        supplierService.addSupplier("Supplier B", "5678 Trade Ave.");

        // Adding Items to Suppliers
        supplierService.addItemToSupplier("Supplier A", "Widget", 3.50f);
        supplierService.addItemToSupplier("Supplier A", "Gadget", 2.75f);
        supplierService.addItemToSupplier("Supplier B", "Gizmo", 1.99f);

        // Creating Orders
        int orderA = orderService.createOrder("Supplier A");
        orderService.addItemToOrder(orderA, "Widget", 10);
        orderService.addItemToOrder(orderA, "Gadget", 5);

        int orderB = orderService.createOrder("Supplier B");
        orderService.addItemToOrder(orderB, "Gizmo", 20);

        // Optionally, pre-setting some order dates
        try {
            Date deliveryDateA = dateFormatter.parse("25/12/2024");
            Date deliveryDateB = dateFormatter.parse("01/01/2025");
            orderService.setOrderDeliveryDate(orderA, deliveryDateA);
            orderService.setOrderDeliveryDate(orderB, deliveryDateB);
        } catch (Exception e) {
            System.out.println("Error setting demo delivery dates.");
        }
    }
}
