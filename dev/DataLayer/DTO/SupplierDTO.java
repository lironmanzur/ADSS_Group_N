package dev.DataLayer.DTO;

public class SupplierDTO {
    private Long id;
    private String name;
    private String address;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
