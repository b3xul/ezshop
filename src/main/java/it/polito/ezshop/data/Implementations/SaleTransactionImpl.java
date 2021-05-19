package it.polito.ezshop.data.Implementations;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;

public class SaleTransactionImpl implements SaleTransaction {

	private Integer ticketNumber;
	private double price;
	private LinkedList<TicketEntry> entries;
	private double discountRate;
	private BalanceOperation balanceOperation;

	public SaleTransactionImpl(Integer ticketNumber) {

		this.ticketNumber = ticketNumber;
		this.price = 0;
		this.entries = new LinkedList<TicketEntry>();
		this.discountRate = 0;
		this.balanceOperation = null;

	}

	public TicketEntry upsertEntry(ProductType productType, int amount) {
		// update in entry or insert new entry if it doesn't exist

		String barCode = productType.getBarCode();
		String productDescription = productType.getProductDescription();
		double pricePerUnit = productType.getPricePerUnit();
		for (TicketEntry entry : entries) {
			if (entry.getBarCode() == barCode) {
				entry.setAmount(entry.getAmount() + amount);
				this.setPrice(this.price + amount * pricePerUnit * (1 - entry.getDiscountRate()));
				return entry;
			}
		}

		TicketEntry newEntry = new TicketEntryImpl(barCode, productDescription, pricePerUnit, 0, amount);
		entries.add(newEntry);
		this.setPrice(this.price + amount * pricePerUnit); // discountRate=0 until setDiscountRateToProduct is called

		return newEntry;

	}

	// public TicketEntry getEntry(String barCode) {
	//
	// TicketEntry entry = null;
	// for (TicketEntry e : entries) {
	// if (e.getBarCode() == barCode) {
	// entry = e;
	// break;
	// }
	// }
	// return entry;
	//
	// }

	public Boolean removeAmountFromEntry(String barCode, int amountToRemove) {
		// remove amount from entry if amount<previous amount, deletes entry if
		// amount==previous amount, return false if amount>previous amount

		Iterator<TicketEntry> iter = entries.iterator();
		while (iter.hasNext()) {
			TicketEntry entry = iter.next();
			if (entry.getBarCode() == barCode) { // product present in the saleTransaction
				int previousAmount = entry.getAmount();
				if (amountToRemove < previousAmount) {
					entry.setAmount(previousAmount - amountToRemove);
					this.setPrice(
							this.price - (amountToRemove * entry.getPricePerUnit() * (1 - entry.getDiscountRate())));
					return true;
				} else if (amountToRemove == previousAmount) {
					iter.remove();
					this.setPrice(
							this.price - (amountToRemove * entry.getPricePerUnit() * (1 - entry.getDiscountRate())));
					return true;
				}
				// else if (amountToRemove > previousAmount) updated=false;
				System.out.println("Found item to remove" + entry);
				break;
			}
		}
		// if product not present in the saleTransaction updated==false
		return false;

	}

	public void setDiscountRateToProduct(String barCode, double discountRate) {

		for (TicketEntry entry : entries) {
			if (entry.getBarCode() == barCode) {
				double entryCost = entry.getAmount() * entry.getPricePerUnit() * (1 - entry.getDiscountRate());
				this.price = this.price - entryCost; // removed entry cost
				entry.setDiscountRate(discountRate);
				this.price = entry.getAmount() * entry.getPricePerUnit() * (1 - entry.getDiscountRate()); // new entry
																											// cost
				System.out.println(entry);
				break;
			}
		}

	}

	@Override
	public Integer getTicketNumber() {

		return ticketNumber;

	}

	@Override
	public void setTicketNumber(Integer ticketNumber) {

		this.ticketNumber = ticketNumber;

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

		this.price = this.price / (1 - this.discountRate);// full price
		this.discountRate = discountRate;
		this.price = this.price * (1 - this.discountRate);// new discounted price

	}

	public BalanceOperation getBalanceOperation() {

		return balanceOperation;

	}

	public void setBalanceOperation(BalanceOperation balanceOperation) {

		this.balanceOperation = balanceOperation;

	}

	@Override
	public String toString() {

		return "SaleTransactionImpl [ticketNumber=" + this.getTicketNumber() + ", price=" + this.price + " entries="
				+ entries + ", discountRate=" + discountRate + "]";

	}

}
