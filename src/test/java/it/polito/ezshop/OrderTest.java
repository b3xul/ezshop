package it.polito.ezshop;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.Implementations.OrderImpl;


public class OrderTest {
	
	@Test
	public void testCase1() {
		OrderImpl order = new OrderImpl(1, 1, LocalDate.now(), 20.10, "1234567891234", 5, 4, "ISSUED");
		
		assertTrue(order.getOrderId() == 1);
		assertTrue(order.getBalanceId() == 1);
		assertTrue(order.getDate().equals(LocalDate.now()));
		assertTrue(order.getMoney() == 20.10);
		assertTrue(order.getProductCode().equals("1234567891234"));
		assertTrue(order.getPricePerUnit() == 5);
		assertTrue(order.getQuantity() == 4);
		assertTrue(order.getStatus().equals("ISSUED"));
		
		order.setBalanceId(5);
		order.setOrderId(5);
		order.setPricePerUnit(12.15);
		order.setQuantity(12);
		order.setProductCode("1234567891111");
		order.setStatus("PAYED");
		
		assertTrue(order.getOrderId() == 5);
		assertTrue(order.getBalanceId() == 5);
		assertTrue(order.getProductCode().equals("1234567891111"));
		assertTrue(order.getPricePerUnit() == 12.15);
		assertTrue(order.getQuantity() == 12);
		assertTrue(order.getStatus().equals("PAYED"));
		
	}
}
