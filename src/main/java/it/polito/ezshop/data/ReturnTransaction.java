package it.polito.ezshop.data;

import it.polito.ezshop.data.Implementations.SaleTransactionImpl;

public interface ReturnTransaction {

	Integer getReturnId();

	void setReturnId(Integer ticketNumber);

	Integer getProductId();

	void setProductId(Integer productId);

	String getProductCode();

	void setProductCode(String productCode);

	double getPricePerUnit();

	void setPricePerUnit(double pricePerUnit);

	double getDiscountRate();

	void setDiscountRate(double discountRate);

	int getAmount();

	void setAmount(int quantity);

	double getPrice();

	void setPrice(double price);

	SaleTransactionImpl getSaleTransaction();

	void setSaleTransaction(SaleTransactionImpl saleTransaction);

	boolean returnProduct(ProductType productType, int amount);

}
