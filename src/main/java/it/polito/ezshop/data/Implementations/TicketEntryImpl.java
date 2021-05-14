package it.polito.ezshop.data.Implementations;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;

public class TicketEntryImpl implements TicketEntry {

	int amount;
	ProductType product;
	Double discountRate;

	TicketEntryImpl(String note, String productDescription, String barCode, double pricePerUnit, double discountRate,
			int amount){
		this.product = new ProductTypeImpl(note, productDescription, barCode, pricePerUnit);
		//this.product.setDiscountRate(discountRate);
		this.amount = amount;
		this.discountRate = discountRate;
	}

	@Override
	public String getBarCode() {

		return this.product.getBarCode();

	}

	@Override
	public void setBarCode(String barCode) {

		this.product.setBarCode(barCode);

	}

	@Override
	public String getProductDescription() {

		return this.product.getProductDescription();

	}

	@Override
	public void setProductDescription(String productDescription) {

		this.product.setProductDescription(productDescription);

	}

	@Override
	public int getAmount() {

		return this.amount;

	}

	@Override
	public void setAmount(int amount) {

		this.amount = amount;

	}
	
	@Override 
	public double getPricePerUnit() {

		return this.product.getPricePerUnit();

	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {

		this.product.setPricePerUnit(pricePerUnit);

	}

	@Override
	public double getDiscountRate() {
		
		return this.discountRate;
		//return this.getDiscountRate();
		
	}

	@Override
	public void setDiscountRate(double discountRate) {
		
		this.discountRate = discountRate;
		//this.product.setDiscountRate(discountRate);
		
	}

}
