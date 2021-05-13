package it.polito.ezshop.data.Implementations;

import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;

public class TicketEntryImpl extends ProductTypeImpl implements TicketEntry {

	int amount;

	TicketEntryImpl(String note, String productDescription, String barCode, Double pricePerUnit, double discountRate,
			int amount)
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {

		super(note, productDescription, barCode, pricePerUnit); // TODO, discountRate);
		this.amount = amount;

	}

	@Override
	public String getBarCode() {

		return this.getBarCode();

	}

	@Override
	public void setBarCode(String barCode) {

		this.setBarCode(barCode);

	}

	@Override
	public String getProductDescription() {

		return this.getProductDescription();

	}

	@Override
	public void setProductDescription(String productDescription) {

		this.setProductDescription(productDescription);

	}

	@Override
	public int getAmount() {

		return this.amount;

	}

	@Override
	public void setAmount(int amount) {

		this.amount = amount;

	}
//	@Override //->double != Double!
//	public double getPricePerUnit() {
//
//		return this.getPricePerUnit();
//
//	}
//
//	@Override
//	public void setPricePerUnit(double pricePerUnit) {
//
//		this.setPricePerUnit(pricePerUnit);

	}

	@Override
	public double getDiscountRate() {

		return this.getDiscountRate();

	}

	@Override
	public void setDiscountRate(double discountRate) {

		this.setDiscountRate(discountRate);

	}

}
