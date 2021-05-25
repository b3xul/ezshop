package it.polito.ezshop;

import static org.junit.Assert.*;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class ComputeBalanceTest {
	
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();

	@Test
	public void testCase1() throws UnauthorizedException {
		
		ezShop.recordBalanceUpdate(10);
		ezShop.recordBalanceUpdate(70);
		ezShop.recordBalanceUpdate(-80);
		assertTrue(ezShop.computeBalance() == 0);
	}
	
	@Test
	public void testCase2() throws UnauthorizedException {
		
		ezShop.recordBalanceUpdate(10);
		ezShop.recordBalanceUpdate(70);
		ezShop.recordBalanceUpdate(90);
		assertTrue(ezShop.computeBalance() == 170);
	}
	
}
