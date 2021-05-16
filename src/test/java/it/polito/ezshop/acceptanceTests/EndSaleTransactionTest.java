package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class EndSaleTransactionTest {

	@Test
	public void testCase1() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			UnauthorizedException {

		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer tid = ezShop.startSaleTransaction();
		System.out.println(tid);
		assertTrue(ezShop.addProductToSale(tid, "98765432123456", 2));
		assertTrue(ezShop.addProductToSale(tid, "1234467898423", 1));
		assertTrue(ezShop.endSaleTransaction(tid));

	}

}