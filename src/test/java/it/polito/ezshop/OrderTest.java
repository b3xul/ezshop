package it.polito.ezshop;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.data.Implementations.OrderImpl;


public class OrderTest {
		
	OrderImpl order = new OrderImpl();
	
	@Test
	public void testCase1() {
		
		OrderImpl order = new OrderImpl(1, 1, LocalDate.now(), 20.10, "12637482635892", 5, 4, "ISSUED");
		assertTrue(order.getOrderId() == 1);
		assertTrue(order.getBalanceId() == 1);
		assertTrue(order.getDate().equals(LocalDate.now()));
		assertTrue(order.getMoney() == 20.10);
		assertTrue(order.getProductCode().equals("12637482635892"));
		assertTrue(order.getPricePerUnit() == 5);
		assertTrue(order.getQuantity() == 4);
		assertTrue(order.getStatus().equals("ISSUED"));
	}
	
	@Test
	public void testCase2() {
		order.setBalanceId(5);
		assertTrue(order.getBalanceId() == 5);
	}
	
	
	@Test
	public void testCase3() {
		order.setOrderId(5);
		assertTrue(order.getOrderId() == 5);
	}
	
	
	@Test
	public void testCase4() {
		order.setPricePerUnit(12.15);
		assertTrue(order.getPricePerUnit() == 12.15);
	}
	
	
	@Test
	public void testCase5() {
		order.setQuantity(12);
		assertTrue(order.getQuantity() == 12);
	}
	
	
	@Test
	public void testCase6() {
		order.setProductCode("12637482635892");
		assertTrue(order.getProductCode().equals("12637482635892"));
	}
	
	@Test
	public void testCase7() {
		order.setStatus("PAYED");
		assertTrue(order.getStatus().equals("PAYED"));
	}
	
	@Test
	public void testCase8() {
		OrderImpl order = new OrderImpl(1, 1, LocalDate.now(), 20.10, "12637482635892", 5, 4, "ISSUED");
		System.out.println(order.toString());
	}
	
}
