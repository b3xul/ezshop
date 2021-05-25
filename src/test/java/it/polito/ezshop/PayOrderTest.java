package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class PayOrderTest {
	
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();

	@Test
	public void testCase1() throws InvalidOrderIdException, UnauthorizedException {
		
		
		assertTrue(ezShop.payOrder(1) == true);
//		assertTrue(ezShop.payOrder(2) == true);
//		assertTrue(ezShop.payOrder(3) == true);
//		assertThrows(InvalidOrderIdException.class,() -> {ezShop.payOrder(0);});
	}
}
