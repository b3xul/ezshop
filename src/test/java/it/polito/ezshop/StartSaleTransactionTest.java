package it.polito.ezshop;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class StartSaleTransactionTest {

	@Test
	public void testCase1() throws UnauthorizedException {

		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		// ezShop.login("Mario", "ahahah");
		assertEquals(Integer.valueOf(0), ezShop.startSaleTransaction());

	}

}
