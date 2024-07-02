package dev.DataLayer.DTO;

public class DiscountNoteDTO {
    private Long id;
    private double discount;
    private String description;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public double getDiscount() {
        return discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
