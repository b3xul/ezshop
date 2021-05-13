package it.polito.ezshop.data;

import java.util.List;

public interface ReturnTransaction {

    Integer getReturnId();

    void setReturnId(Integer ticketNumber);
    
    String getProductCode();

    void setProductCode(String productCode);

    int getAmount();

    void setAmount(int quantity);

    double getPrice();

    void setPrice(double price);
    
    SaleTransaction getSaleTransaction();
    
    void setSaleTransaction();
}
