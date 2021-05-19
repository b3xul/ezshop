package it.polito.ezshop;

import static org.junit.Assert.*;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class RecordBalanceUpdateTest {
	
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();

	@Test
	public void testCase1() throws UnauthorizedException {
		
		assertTrue(ezShop.recordBalanceUpdate(120) == true);
		assertTrue(ezShop.recordBalanceUpdate(-200) == true);
	}
	
}
