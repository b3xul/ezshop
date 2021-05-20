package it.polito.ezshop.data;

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

	void setSaleTransaction(SaleTransaction saleTransaction);

}
