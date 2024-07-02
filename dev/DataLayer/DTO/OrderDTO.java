package dev.DataLayer.DTO;

public class OrderDTO {
    private Long id;
    private String orderDate;
    private String deliveryDate;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String deliveryStatus;
    private String deliveryAddress;
    private String deliveryNote;
    private String orderNote;
    private Long customerId;
    private Long supplierId;
    private Long discountNoteId;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public Long getDiscountNoteId() {
        return discountNoteId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getStatus() {
        return status;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public void setDiscountNoteId(Long discountNoteId) {
        this.discountNoteId = discountNoteId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
}
