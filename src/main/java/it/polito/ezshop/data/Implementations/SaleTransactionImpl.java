package it.polito.ezshop.data.Implementations;

import java.util.LinkedList;
import java.util.List;

import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;

public class SaleTransactionImpl extends BalanceOperationImpl implements SaleTransaction {

	// Integer ticketNumber; -> BalanceOperation.balanceId
	LinkedList<TicketEntry> entries;
	double discountRate;
	// double price; -> BalanceOperation.money

	public SaleTransactionImpl(Integer ticketNumber) {

		super(ticketNumber, "saleTransaction");
		this.entries = new LinkedList<TicketEntry>();
		this.discountRate = 0;

	}

	public Boolean addEntry(String productCode, int amount) {

		Boolean updated = false;
		for (TicketEntry tc : entries) {
			if (tc.getBarCode() == productCode) {
				tc.setAmount(tc.getAmount() + amount);
				updated = true;
				System.out.println(tc);
				break;
			}
		}
		if (updated == false) {
			entries.add(null);
		}

	}

	@Override
	public Integer getTicketNumber() {

		return this.getBalanceId();

	}

	@Override
	public void setTicketNumber(Integer ticketNumber) {

		this.setBalanceId(ticketNumber);

	}

	@Override
	public List<TicketEntry> getEntries() {

		return entries;

	}

	@Override
	public void setEntries(List<TicketEntry> entries) {

		this.entries = (LinkedList<TicketEntry>) entries;

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
	public double getPrice() {

		return this.getMoney();

	}

	@Override
	public void setPrice(double price) {

		this.setMoney(price);

	}

	@Override
	public String toString() {

		return "SaleTransactionImpl [balanceId=" + this.getBalanceId() + ", date=" + this.getDate() + ", money="
				+ this.getMoney() + ", type=" + this.getType() + " entries=" + entries + ", discountRate="
				+ discountRate + "]";

	}

}
