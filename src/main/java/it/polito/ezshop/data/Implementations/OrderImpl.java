package it.polito.ezshop.data.Implementations;

import java.time.LocalDate;
import it.polito.ezshop.data.Order;


public class OrderImpl extends BalanceOperationImpl implements Order {
	
	private String productCode;
	private double pricePerUnit;
	private int quantity;
	private String status;
	private Integer orderId;
	
	public OrderImpl(int balanceId, LocalDate date, double money, String type, String productCode, double pricePerUnit, int quantity, String status, Integer orderId){
		super(balanceId, date, money, type);
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
		this.status = status;
		this.orderId = orderId;
	}
	
	@Override
	public Integer getBalanceId() {
		
		return super.getBalanceId();
	}

	@Override
	public void setBalanceId(Integer balanceId) {
		
		this.balanceId = balanceId;

	}

	@Override
	public String getProductCode() {
		
		return this.productCode;
	}

	@Override
	public void setProductCode(String productCode) {
		
		this.productCode = productCode;

	}

	@Override
	public double getPricePerUnit() {
		
		return this.pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		
		this.pricePerUnit = pricePerUnit;

	}

	@Override
	public int getQuantity() {
		
		return this.quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		
		this.quantity = quantity;

	}

	@Override
	public String getStatus() {
		
		return status;
	}

	@Override
	public void setStatus(String status) {
		
		this.status = status;
	}

	@Override
	public Integer getOrderId() {
		
		return this.orderId;
	}

	@Override
	public void setOrderId(Integer orderId) {
		
		this.orderId = orderId;

	}

}
