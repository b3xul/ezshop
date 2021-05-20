package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.Implementations.ProductTypeImpl;
import it.polito.ezshop.data.Implementations.SaleTransactionImpl;
import it.polito.ezshop.data.Implementations.TicketEntryImpl;

public class SaleTransactionClassTest {

	@Test
	public void testCase1() {
		// constructor+setters+getters

		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(0.4);
		assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		saleTransaction.setEntries(entries);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		saleTransaction.toString();
		;

	}

	@Test
	public void testCase2() {

		// removeAmountFromEntry(lower amount): 1 iteration
		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(0.4);
		assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		entries.add(new TicketEntryImpl("12637482635892", "description", 2.0, 0.1, 10));
		entries.add(new TicketEntryImpl("6253478956438", "description2", 3.0, 0.5, 2));
		saleTransaction.setEntries(entries);
		saleTransaction.setPrice(21);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		saleTransaction.toString();

		assertTrue(saleTransaction.removeAmountFromEntry("12637482635892", 1));

		saleTransaction.toString();

	}

	@Test
	public void testCase3() {

		// removeAmountFromEntry(same amount): 1 iteration
		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(0.4);
		assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		entries.add(new TicketEntryImpl("12637482635892", "description", 2.0, 0.1, 10));
		entries.add(new TicketEntryImpl("6253478956438", "description2", 3.0, 0.5, 2));
		saleTransaction.setEntries(entries);
		saleTransaction.setPrice(21);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		saleTransaction.toString();

		assertTrue(saleTransaction.removeAmountFromEntry("12637482635892", 10));

		saleTransaction.toString();

	}

	@Test
	public void testCase4() {

		// removeAmountFromEntry(higher amount): 1 iteration
		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(0.4);
		assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		entries.add(new TicketEntryImpl("12637482635892", "description", 2.0, 0.1, 10));
		entries.add(new TicketEntryImpl("6253478956438", "description2", 3.0, 0.5, 2));
		saleTransaction.setEntries(entries);
		saleTransaction.setPrice(21);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		saleTransaction.toString();

		assertFalse(saleTransaction.removeAmountFromEntry("12637482635892", 11));

		saleTransaction.toString();

	}

	@Test
	public void testCase5() {

		// removeAmountFromEntry(barcode not found): entries.size() iterations
		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(0.4);
		assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		entries.add(new TicketEntryImpl("12637482635892", "description", 2.0, 0.1, 10));
		entries.add(new TicketEntryImpl("6253478956438", "description2", 3.0, 0.5, 2));
		saleTransaction.setEntries(entries);
		saleTransaction.setPrice(21);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		saleTransaction.toString();

		assertFalse(saleTransaction.removeAmountFromEntry("22637482635892", 4));

		saleTransaction.toString();

	}

	@Test
	public void testCase6() {

		// removeAmountFromEntry(empty list): 0 iterations
		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(0.4);
		assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		saleTransaction.setEntries(entries);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		saleTransaction.toString();

		assertFalse(saleTransaction.removeAmountFromEntry("12637482635892", 11));

		saleTransaction.toString();

	}

	@Test
	public void testCase7() {

		// setDiscountRateToProduct: 1 iteration
		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(0.4);
		assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		entries.add(new TicketEntryImpl("12637482635892", "description", 2.0, 0.1, 10));
		entries.add(new TicketEntryImpl("6253478956438", "description2", 3.0, 0.5, 2));
		saleTransaction.setEntries(entries);
		saleTransaction.setPrice(21);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		saleTransaction.toString();

		saleTransaction.setDiscountRateToProduct("12637482635892", 0.2);

		saleTransaction.toString();

	}

	@Test
	public void testCase8() {

		// setDiscountRateToProduct(wrong barcode): entries.size() iterations
		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(0.4);
		assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		entries.add(new TicketEntryImpl("12637482635892", "description", 2.0, 0.1, 10));
		entries.add(new TicketEntryImpl("6253478956438", "description2", 3.0, 0.5, 2));
		saleTransaction.setEntries(entries);
		saleTransaction.setPrice(21);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		saleTransaction.toString();

		saleTransaction.setDiscountRateToProduct("22637482635892", 0.2);

		saleTransaction.toString();

	}

	@Test
	public void testCase9() {

		// setDiscountRateToProduct(empty list): 0 iterations
		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(0.4);
		assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		saleTransaction.setEntries(entries);
		saleTransaction.setPrice(21);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		saleTransaction.toString();

		saleTransaction.setDiscountRateToProduct("12637482635892", 0.2);

		saleTransaction.toString();

	}

	@Test
	public void testCase10() {

		// upsertEntry

		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(0.4);
		assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		saleTransaction.setEntries(entries);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		saleTransaction.toString();

		ProductType productType1 = new ProductTypeImpl("note", "description", "12637482635892", 2.5);
		saleTransaction.upsertEntry(productType1, 6);
		ProductType productType2 = new ProductTypeImpl("note", "description2", "6253478956438", 7.0);
		saleTransaction.upsertEntry(productType2, 9);
		saleTransaction.upsertEntry(productType2, 10);

		saleTransaction.toString();

	}

}
