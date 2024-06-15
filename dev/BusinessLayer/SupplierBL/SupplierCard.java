package dev.BusinessLayer.SupplierBL;

public abstract class SupplierCard {
    String name;
    int het_peh;
    PaymentConditions paymentConditions;
    int accountNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHet_peh(int het_peh) {
        this.het_peh = het_peh;
    }

    public void setPaymentConditions(PaymentConditions paymentConditions) {
        this.paymentConditions = paymentConditions;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getHet_peh() {
        return het_peh;
    }

    public PaymentConditions getPaymentConditions() {
        return paymentConditions;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public SupplierCard(String name, int het_peh, PaymentConditions paymentConditions, int accontNumber) {
        this.name = name;
        this.het_peh = het_peh;
        this.paymentConditions = paymentConditions;
        this.accountNumber = accontNumber;
    }
}
