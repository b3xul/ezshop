package it.polito.ezshop.data.Implementations;

import java.util.List;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;

public class ReturnTransactionImpl implements it.polito.ezshop.data.ReturnTransaction {

	private Integer returnId;
	private Integer productId;
	private String productCode;
	private double pricePerUnit;
	private double discountRate;
	private int amount;
	private double price;
	private SaleTransactionImpl saleTransaction;
	// BalanceOperation balanceOperation; isn't necessary because no getReturnTransaction exists

	public ReturnTransactionImpl(Integer returnId) {

		this.returnId = returnId;
		this.productId = -1;
		this.productCode = "";
		this.pricePerUnit = 0;
		this.discountRate = 0;
		this.amount = 0;
		this.price = 0;
		this.saleTransaction = null;

	}

	public boolean returnProduct(ProductType productType, int amount) {

		List<TicketEntry> entries = this.getSaleTransaction().getEntries();
		Boolean updated = false;
		for (TicketEntry entry : entries) {
			if (entry.getBarCode().equals(productType.getBarCode())) {
				if (amount > entry.getAmount()) // the amount is higher than the one in the sale transaction
					updated = false;
				else {
					this.setProductId(productType.getId());
					this.setProductCode(entry.getBarCode());
					this.setPricePerUnit(entry.getPricePerUnit());
					this.setDiscountRate(entry.getDiscountRate());
					this.setAmount(amount);
					this.setPrice(this.getPrice() + entry.getPricePerUnit() * amount * (1 - entry.getDiscountRate()));
					updated = true;
					// System.out.println(entry);
				}
				break;
			}
		}
		// if (updated == false) productCode was not in the transaction
		return updated;

	}

	@Override
	public Integer getReturnId() {

		return returnId;

	}

	@Override
	public void setReturnId(Integer returnId) {

		this.returnId = returnId;

	}

	@Override
	public Integer getProductId() {

		return productId;

	}

	@Override
	public void setProductId(Integer productId) {

		this.productId = productId;

	}

	@Override
	public String getProductCode() {

		return productCode;

	}

	@Override
	public void setProductCode(String productCode) {

		this.productCode = productCode;

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

	@Override
	public double getPrice() {

		return price;

	}

	@Override
	public void setPrice(double price) {

		this.price = price;

	}

	public SaleTransactionImpl getSaleTransaction() {

		return saleTransaction;

	}

	public void setSaleTransaction(SaleTransactionImpl saleTransaction) {

		this.saleTransaction = saleTransaction;

	}

	@Override
	public String toString() {

		return "ReturnTransactionImpl [returnId=" + returnId + ", productId=" + productId + ", productCode="
				+ productCode + ", pricePerUnit=" + pricePerUnit + ", amount=" + amount + ", price=" + price
				+ ", saleTransaction=" + saleTransaction + "]";

	}

}
