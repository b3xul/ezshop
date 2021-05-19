package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.TicketEntry;
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

		System.out.println(saleTransaction.toString());

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

		System.out.println(saleTransaction.toString());

		assertTrue(saleTransaction.removeAmountFromEntry("12637482635892", 1));

		System.out.println(saleTransaction.toString());

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

		System.out.println(saleTransaction.toString());

		assertTrue(saleTransaction.removeAmountFromEntry("12637482635892", 10));

		System.out.println(saleTransaction.toString());

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

		System.out.println(saleTransaction.toString());

		assertFalse(saleTransaction.removeAmountFromEntry("12637482635892", 11));

		System.out.println(saleTransaction.toString());

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

		System.out.println(saleTransaction.toString());

		assertFalse(saleTransaction.removeAmountFromEntry("22637482635892", 4));

		System.out.println(saleTransaction.toString());

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

		System.out.println(saleTransaction.toString());

		assertFalse(saleTransaction.removeAmountFromEntry("12637482635892", 11));

		System.out.println(saleTransaction.toString());

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

		System.out.println(saleTransaction.toString());

		saleTransaction.setDiscountRateToProduct("12637482635892", 0.2);

		System.out.println(saleTransaction.toString());

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

		System.out.println(saleTransaction.toString());

		saleTransaction.setDiscountRateToProduct("22637482635892", 0.2);

		System.out.println(saleTransaction.toString());

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

		System.out.println(saleTransaction.toString());

		saleTransaction.setDiscountRateToProduct("12637482635892", 0.2);

		System.out.println(saleTransaction.toString());

	}

	// @Test
	// public void testCase7() {
	//
	// // upsertEntry
	//
	// SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
	// assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());
	//
	// saleTransaction.setTicketNumber(2);
	// assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());
	//
	// saleTransaction.setPrice(3.0);
	// assertEquals(3.0, saleTransaction.getPrice(), 0.001);
	//
	// saleTransaction.setDiscountRate(0.4);
	// assertEquals(0.4, saleTransaction.getDiscountRate(), 0.001);
	//
	// LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
	// saleTransaction.setEntries(entries);
	// assertEquals(entries, saleTransaction.getEntries());
	//
	// BalanceOperation balanceOperation = null;
	// saleTransaction.setBalanceOperation(balanceOperation);
	// assertEquals(balanceOperation, saleTransaction.getBalanceOperation());
	//
	// System.out.println(saleTransaction.toString());
	//
	// // saleTransaction.upsertEntry("12637482635892", "description1", 4.0, 5.1, 6);
	// // TicketEntryImpl t1 = new TicketEntryImpl("12637482635892", "description1", 4.0, 5.1, 6);
	// // assertEquals(t1, saleTransaction.getEntry("12637482635892"));
	// //
	// // System.out.println(saleTransaction.toString() + saleTransaction.getEntry("12637482635892"));
	// //
	// // saleTransaction.upsertEntry("6253478956438", "description2", 7.0, 8.1, 9);
	// // TicketEntryImpl t2 = new TicketEntryImpl("6253478956438", "description2", 7.0, 8.1, 9);
	// // assertEquals(t2, saleTransaction.getEntry("6253478956438"));
	//
	// System.out.println(saleTransaction.toString());
	//
	// }
}
