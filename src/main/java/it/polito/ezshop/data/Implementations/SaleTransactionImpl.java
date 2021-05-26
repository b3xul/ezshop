package it.polito.ezshop.data.Implementations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class SaleTransactionImpl implements SaleTransaction {

	private Integer ticketNumber;
	private double price;
	private LinkedList<TicketEntry> entries;
	private double discountRate;
	private String creditCard;
	private BalanceOperation balanceOperation;

	public SaleTransactionImpl(Integer ticketNumber) {

		this.ticketNumber = ticketNumber;
		this.price = 0;
		this.entries = new LinkedList<TicketEntry>();
		this.discountRate = 0;
		this.creditCard = "";
		this.balanceOperation = null;

	}

	public Integer startSaleTransaction() {

		String getNextAutoincrement = "SELECT seq FROM sqlite_sequence WHERE name=\"saleTransaction\"";
		Integer id = 0; // 0=error
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:EZShopDB.sqlite");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(getNextAutoincrement)) {
			id = rs.getInt("seq") + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.setTicketNumber(id);
		// transaction will be added to the db only when it ends
		return id;

	}

	public TicketEntry addProductToSale(ProductType productType, int amount) {

		String barCode = productType.getBarCode();
		// decreaseProductQuantity
		String decreaseProductQuantity = "UPDATE product SET quantity=quantity - ? WHERE barcode=?";
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:EZShopDB.sqlite");) {
			PreparedStatement pstmt = conn.prepareStatement(decreaseProductQuantity);
			pstmt.setInt(1, amount);
			pstmt.setString(2, barCode);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// update in entry or insert new entry if it doesn't exist

		String productDescription = productType.getProductDescription();
		double pricePerUnit = productType.getPricePerUnit();
		for (TicketEntry entry : entries) {
			if (entry.getBarCode().equals(barCode)) {
				entry.setAmount(entry.getAmount() + amount);
				this.setPrice(this.price + amount * pricePerUnit * (1 - entry.getDiscountRate()));
				return entry;
			}
		}

		TicketEntry newEntry = new TicketEntryImpl(barCode, productDescription, pricePerUnit, 0, amount);
		entries.add(newEntry);
		this.setPrice(this.price + amount * pricePerUnit); // discountRate=0 until applyDiscountRateToProduct is called

		return newEntry;

	}

	public boolean deleteProductFromSale(String barCode, int amount) {
		// amount to remove from sale, amount to add to store

		// remove amount from entry if amount<previous amount, deletes entry if
		// amount==previous amount, return false if amount>previous amount

		boolean result = false;
		Iterator<TicketEntry> iter = entries.iterator();
		while (iter.hasNext()) {
			TicketEntry entry = iter.next();
			if (entry.getBarCode().equals(barCode)) { // product present in the saleTransaction
				int previousAmount = entry.getAmount();
				if (amount < previousAmount) {
					entry.setAmount(previousAmount - amount);
					this.setPrice(this.price - (amount * entry.getPricePerUnit() * (1 - entry.getDiscountRate())));
					result = true;
				} else if (amount == previousAmount) {
					iter.remove();
					this.setPrice(this.price - (amount * entry.getPricePerUnit() * (1 - entry.getDiscountRate())));
					result = true;
				}
				// else if (amountToRemove > previousAmount) updated=false;
				// System.out.println("Found item to remove" + entry);
			}
		}
		// if product not present in the saleTransaction result==false
		if (result == true) {
			// increaseProductQuantity
			String increaseProductQuantity = "UPDATE product SET quantity=quantity + ? WHERE barcode=?";
			try (Connection conn = DriverManager.getConnection("jdbc:sqlite:EZShopDB.sqlite");) {
				PreparedStatement pstmt = conn.prepareStatement(increaseProductQuantity);
				pstmt.setInt(1, amount);
				pstmt.setString(2, barCode);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;

	}

	public boolean applyDiscountRateToProduct(String barCode, double discountRate) {

		for (TicketEntry entry : entries) {
			if (entry.getBarCode().equals(barCode)) {
				double entryCost = entry.getAmount() * entry.getPricePerUnit() * (1 - entry.getDiscountRate());
				this.price = this.price - entryCost; // removed entry cost
				entry.setDiscountRate(discountRate);
				this.price = entry.getAmount() * entry.getPricePerUnit() * (1 - entry.getDiscountRate()); // new entry
																											// cost
				// System.out.println(entry);
				return true;
			}
		}
		return false;

	}

	public void applyDiscountRateToSale(double discountRate) {

		this.price = this.price / (1 - this.discountRate);// full price
		this.discountRate = discountRate;
		this.price = this.price * (1 - this.discountRate);// new discounted price

	}

	public boolean deleteSaleTransaction() {

		for (TicketEntry entry : this.getEntries()) {
			// increaseProductQuantity
			String increaseProductQuantity = "UPDATE product SET quantity=quantity + ? WHERE barcode=?";
			try (Connection conn = DriverManager.getConnection("jdbc:sqlite:EZShopDB.sqlite");) {
				PreparedStatement pstmt = conn.prepareStatement(increaseProductQuantity);
				pstmt.setInt(1, entry.getAmount()); // amount to remove from sale, amount to add to store
				pstmt.setString(2, entry.getBarCode());
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;

	}

	public Boolean endSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException {

		String getNextAutoincrement = "SELECT seq FROM sqlite_sequence WHERE name=\"balanceOperation\"";
		String insertSale = "INSERT INTO saleTransaction(price,discountRate,creditCard,balanceId) VALUES(?,?,?,?)";
		String insertTicketEntry = "INSERT INTO ticketEntry(ticketNumber,barCode,productDescription,pricePerUnit,discountRate,amount) VALUES(?,?,?,?,?,?)";

		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:EZShopDB.sqlite");) {

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getNextAutoincrement);
			int balanceId = rs.getInt("seq") + 1;
			BalanceOperationImpl balanceOperation = new BalanceOperationImpl(balanceId, LocalDate.now(),
					this.getPrice(), "CREDIT");
			this.setBalanceOperation(balanceOperation);
			stmt.close();
			rs.close();
			// Insert SaleTransaction
			PreparedStatement pstmt = conn.prepareStatement(insertSale);
			pstmt.setDouble(1, this.getPrice());
			pstmt.setDouble(2, this.getDiscountRate());
			pstmt.setString(3, this.getCreditCard());
			pstmt.setInt(4, balanceId);
			pstmt.executeUpdate();
			pstmt.close();
			for (TicketEntry entry : this.getEntries()) {
				// InsertTicketEntry
				pstmt = conn.prepareStatement(insertTicketEntry);
				pstmt.setInt(1, this.getTicketNumber());
				pstmt.setString(2, entry.getBarCode());
				pstmt.setString(3, entry.getProductDescription());
				pstmt.setDouble(4, entry.getPricePerUnit());
				pstmt.setDouble(5, entry.getDiscountRate());
				pstmt.setInt(6, entry.getAmount());
				pstmt.executeUpdate();
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

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

		this.discountRate = discountRate;

	}

	public String getCreditCard() {

		return creditCard;

	}

	public void setCreditCard(String creditCard) {

		this.creditCard = creditCard;

	}

	public BalanceOperation getBalanceOperation() {

		return balanceOperation;

	}

	public void setBalanceOperation(BalanceOperation balanceOperation) {

		this.balanceOperation = balanceOperation;

	}

	@Override
	public String toString() {

		return "SaleTransactionImpl [ticketNumber=" + ticketNumber + ", price=" + price + ", entries=" + entries
				+ ", discountRate=" + discountRate + ", creditCard=" + creditCard + ", balanceOperation="
				+ balanceOperation + "]";

	}

}
