package dev.BusinessLayer.SupplierBL;
public class Contact {
    private String name;
    private String phoneNumber;
    private String Address;

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return Address;
    }

    public Contact(String name, String phoneNumber, String address){
        this.Address = address;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

}
