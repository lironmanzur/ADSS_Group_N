package dev.SupplierServiceLayer;
import dev.BusinessLayer.SupplierBL.*;
import dev.DataLayer.DAO.OrderDAO;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {

    private static OrderService instance = new OrderService(SupplierService.getInstance());
    private Map<Integer, Order> orders;
    private SupplierService supplierService;  // Assume SupplierService is accessible here
    private int currentOrderId;
    static  int itemID = 4537865;
    private OrderDAO orderDAO;

    private OrderService(SupplierService supplierService) {
        this.orders = new HashMap<>();
        this.currentOrderId = 1;
        this.supplierService = supplierService;
        this.orderDAO = new OrderDAO(supplierService.getSupplierDAO(), supplierService.getItemDAO());
        //assuming the program runs daily
        sendMemos();
    }

    public static OrderService getInstance() {
        return instance;
    }

    public int getQuantityOfItem(int itemID){
        int quantity = 20;//demo value
        //todo ask for stock module for the quantity of the item
        return quantity;
    }
    public int getItemMinQuantity(int itemID){
        int minQuantity = 10;//demo value
        //todo ask for stock module for the minimum quantity of the item
        return minQuantity;
    }
    public void sendMemo(Supplier supplier){
        //todo send memo to stock module to send in order to the supplier
    }
    public void sendMemos(){
        for (Supplier supplier : supplierService.getSuppliersList()){
            //if suplier is instance of stationary
            if(supplier instanceof StationarySupplier){
                StationarySupplier stationarySupplier = (StationarySupplier) supplier;
                stationarySupplier.getDeliveryDays().forEach(day -> {
                    if(day.equals(LocalDate.now().plusDays(1).getDayOfWeek())){
                        sendMemo(stationarySupplier);
}
                });
            }

        }
    }

    public void createShortageOrder(int itemID, int quantity){
        Order order = getCheapestOrder(itemID, quantity);
        if(order == null){
            System.out.println("No supplier found for item " + itemID);
            return;
        }
        orders.put(currentOrderId++, order);
    }
    public Order getCheapestOrder(int itemID, int quantity){
        Order returnOrder = null;
        double minPrice = Double.MAX_VALUE;
        for(Supplier supplier : supplierService.getSuppliersList()){
            if(supplier.getItemByID(itemID) != null){
                Order order = new Order(supplier);
                order.addItem(supplier.getItemByID(itemID), quantity);
                double price = order.calculatePrice();
                if(price < minPrice){
                    minPrice = price;
                    returnOrder = order;
                }
            }
        }
        return returnOrder;
    }

    public int createOrder(String supplierName) {
        Supplier supplier = supplierService.getSupplier(supplierName);
        if (supplier != null) {
            Order order = new Order(supplier);
            int orderId = currentOrderId++;
            orders.put(orderId, order);
            return orderId;
        }
        return -1;  // Return -1 or throw an exception if supplier not found
    }

    public String addItemToOrder(int orderId, String itemName, int quantity) {
        Order order = orders.get(orderId);
        if (order != null) {
            Supplier supplier = order.getSupplier();
            Item item = supplier.getItemByName(itemName);
            // Assume you can create a new Item here
            if (item == null) return "Item not found";
            order.addItem(item, quantity);
            return "Item added successfully.";
        }
        return "Order not found.";
    }

    public double calculateOrderPrice(int orderId) {
        Order order = orders.get(orderId);
//        DiscountNote discountNote = order.getSupplier().getDiscountNote();
//        if (discountNote != null) return order.calculateDiscount(discountNote);
//        else if (order != null) {
            return order.calculatePrice();
  //      }
//        return 0.0;
    }

    public double calculateOrderDiscountedPrice(int orderId) {
        Order order = orders.get(orderId);
        if (order != null && order.getSupplier() != null) {
            DiscountNote discountNote = order.getSupplier().getDiscountNote();  // Assuming getDiscountNote() method exists
            if (discountNote != null) {
                return order.calculateDiscount(discountNote);
            }
        }
        return 0.0;
    }

    public String setOrderDeliveryDate(int orderId, Date providingDate) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setProvidingDate(providingDate);
            return "Delivery date set.";
        }
        return "Order not found.";
    }

    public Order getOrder(int orderId) {
        return orders.get(orderId);
    }

    public void loadDataFromDB() {
        try {
            List<Order> allOrders = orderDAO.getAllOrders();
            for (Order order : allOrders) {
                orders.put(order.getId(), order);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load orders from DB: " + e.getMessage(), e);
        }
    }

    public void saveDataToDB() {
        try {
            for (Order order : orders.values()) {
                if (order.getId() == 0) { // Assuming getId() returns 0 if not set (newly created)
                    orderDAO.addOrder(order);
                } else {
                    orderDAO.updateOrder(order);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save orders to DB: " + e.getMessage(), e);
        }
    }
}
