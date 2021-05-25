package it.polito.ezshop;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import static org.junit.Assert.*;

import org.junit.Test;

public class PayOrderForTest {
	
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	
	@Test
	public void testCase1() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		
		assertTrue(ezShop.payOrderFor("12637482635892", 5, 5.10) > 0);
		assertTrue(ezShop.payOrderFor("12637482635892", 10, 2.30) > 0);
		assertTrue(ezShop.payOrderFor("6253478956438", 3, 5.45) > 0);
		assertTrue(ezShop.payOrderFor("6253478956438", 5, 10.45) > 0);
	}
	
}
