package it.polito.ezshop.data.Implementations;

import it.polito.ezshop.data.TicketEntry;

public class TicketEntryImpl implements TicketEntry {

	String barCode;
	String productDescription;
	double pricePerUnit;
	double discountRate;
	int amount;

	public TicketEntryImpl(String barCode, String productDescription, double pricePerUnit, double discountRate,
			int amount) {

		this.barCode = barCode;
		this.productDescription = productDescription;
		this.pricePerUnit = pricePerUnit;
		this.discountRate = discountRate;
		this.amount = amount;

	}

	@Override
	public String getBarCode() {

		return barCode;

	}

	@Override
	public void setBarCode(String barCode) {

		this.barCode = barCode;

	}

	@Override
	public String getProductDescription() {

		return productDescription;

	}

	@Override
	public void setProductDescription(String productDescription) {

		this.productDescription = productDescription;

	}

	@Override
	public double getPricePerUnit() {

		return pricePerUnit;

	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {

		this.pricePerUnit = pricePerUnit;

	}

	@Override
	public double getDiscountRate() {

		return discountRate;

	}

	@Override
	public void setDiscountRate(double discountRate) {

		this.discountRate = discountRate;

	}

	@Override
	public int getAmount() {

		return amount;

	}

	@Override
	public void setAmount(int amount) {

		this.amount = amount;

	}

}
