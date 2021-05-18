package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.Implementations.TicketEntryImpl;

public class TicketEntryClassTest {
	TicketEntry product = new TicketEntryImpl("12637482635892", "description", 2.0, 0.0, 10);

	@Test
	public void testCase1() {
		
		assertTrue((product.getProductDescription() == "description") && 
				(product.getBarCode() == "12637482635892") &&
				(product.getPricePerUnit() == 2.0) &&
				(product.getDiscountRate() == 0.0) &&
				(product.getAmount() == 10));

	}

	@Test
	public void testCase2() {
		
			product.setProductDescription("new desc");
			assertTrue(product.getProductDescription() == "new desc");
			
	}
	
	@Test
	public void testCase3() {
	
		product.setBarCode("6253478956438");
		assertTrue(product.getBarCode() == "6253478956438");
		
	}
	
	@Test
	public void testCase4() {
		
		product.setPricePerUnit(25.0);
		assertTrue(product.getPricePerUnit() == 25.0);
		
	}
	
	@Test
	public void testCase5() {
		
		product.setDiscountRate(20.0);
		assertTrue(product.getDiscountRate() == 20.0);
		
	}
	
	@Test
	public void testCase6() {
		
		product.setAmount(15);
		assertTrue(product.getAmount() == 15);
		
	}

	
}
