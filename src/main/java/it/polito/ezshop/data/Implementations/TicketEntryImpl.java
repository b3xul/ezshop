package it.polito.ezshop.data.Implementations;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;

public class TicketEntryImpl implements TicketEntry {

	ProductType productType;
	int amount;

	TicketEntryImpl(ProductType productType, int amount)
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {

		this.productType = productType;
		this.amount = amount;

	}

	@Override
	public String getBarCode() {

		return productType.getBarCode();

	}

	@Override
	public void setBarCode(String barCode) {

		this.productType.setBarCode(barCode);

	}

	@Override
	public String getProductDescription() {

		return productType.getProductDescription();

	}

	@Override
	public void setProductDescription(String productDescription) {

		this.productType.setProductDescription(productDescription);

	}

	@Override
	public int getAmount() {

		return amount;

	}

	@Override
	public void setAmount(int amount) {

		this.amount = amount;

	}

	@Override // ->double != Double!
	public double getPricePerUnit() {

		return productType.getPricePerUnit();

	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {

		this.productType.setPricePerUnit(pricePerUnit);

	}

	@Override
	public double getDiscountRate() {

		return productType.getDiscountRate();

	}

	@Override
	public void setDiscountRate(double discountRate) {

		this.productType.setDiscountRate(discountRate);

	}

}
