package it.polito.ezshop;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Test;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.Implementations.SaleTransactionImpl;
import it.polito.ezshop.data.Implementations.TicketEntryImpl;

public class SaleTransactionClassTest {

	@Test
	public void testCase1() {

		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(15.4);
		assertEquals(15.4, saleTransaction.getDiscountRate(), 0.001);

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

		SaleTransactionImpl saleTransaction = new SaleTransactionImpl(1);
		assertEquals(Integer.valueOf(1), saleTransaction.getTicketNumber());

		saleTransaction.setTicketNumber(2);
		assertEquals(Integer.valueOf(2), saleTransaction.getTicketNumber());

		saleTransaction.setPrice(3.0);
		assertEquals(3.0, saleTransaction.getPrice(), 0.001);

		saleTransaction.setDiscountRate(15.4);
		assertEquals(15.4, saleTransaction.getDiscountRate(), 0.001);

		LinkedList<TicketEntry> entries = new LinkedList<TicketEntry>();
		saleTransaction.setEntries(entries);
		assertEquals(entries, saleTransaction.getEntries());

		BalanceOperation balanceOperation = null;
		saleTransaction.setBalanceOperation(balanceOperation);
		assertEquals(balanceOperation, saleTransaction.getBalanceOperation());

		System.out.println(saleTransaction.toString());

		saleTransaction.upsertEntry("12637482635892", "description1", 4.0, 5.1, 6);
		TicketEntryImpl t1 = new TicketEntryImpl("12637482635892", "description1", 4.0, 5.1, 6);
		assertEquals(t1, saleTransaction.getEntry("12637482635892"));

		System.out.println(saleTransaction.toString() + saleTransaction.getEntry("12637482635892"));

		saleTransaction.upsertEntry("6253478956438", "description2", 7.0, 8.1, 9);
		TicketEntryImpl t2 = new TicketEntryImpl("6253478956438", "description2", 7.0, 8.1, 9);
		assertEquals(t2, saleTransaction.getEntry("6253478956438"));

		System.out.println(saleTransaction.toString());

	}

}
