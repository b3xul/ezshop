package it.polito.ezshop.data;


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

	boolean returnProduct(ProductType productType, int amount);

}
