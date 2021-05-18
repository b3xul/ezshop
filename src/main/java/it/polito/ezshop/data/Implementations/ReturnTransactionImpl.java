package it.polito.ezshop.data.Implementations;

import it.polito.ezshop.data.SaleTransaction;

public class ReturnTransactionImpl implements it.polito.ezshop.data.ReturnTransaction {

	Integer returnId;
	Integer productId;
	String productCode;
	int amount;
	double price;
	SaleTransaction saleTransaction;

	public ReturnTransactionImpl(Integer returnId, SaleTransaction saleTransaction) {

		this.returnId = returnId;
		this.productId = -1;
		this.productCode = "";
		this.amount = 0;
		this.price = 0;
		this.saleTransaction = saleTransaction;

	}

	@Override
	public Integer getReturnId() {

		return returnId;

	}

	@Override
	public void setReturnId(Integer returnId) {

		this.returnId = returnId;

	}

	public Integer getProductId() {

		return productId;

	}

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

	@Override
	public SaleTransaction getSaleTransaction() {

		return saleTransaction;

	}

	@Override
	public void setSaleTransaction(SaleTransaction saleTransaction) {

		this.saleTransaction = saleTransaction;

	}

}
