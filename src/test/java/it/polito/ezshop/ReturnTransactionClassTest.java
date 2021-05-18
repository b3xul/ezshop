package it.polito.ezshop;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.Implementations.ReturnTransactionImpl;

public class ReturnTransactionClassTest {

	@Test
	public void testCase1() {

		SaleTransaction saleTransaction = null;
		ReturnTransactionImpl returnTransaction = new ReturnTransactionImpl(1, saleTransaction);
		assertEquals(Integer.valueOf(1), returnTransaction.getReturnId());
		assertEquals(saleTransaction, returnTransaction.getSaleTransaction());

		returnTransaction.setReturnId(2);
		assertEquals(Integer.valueOf(2), returnTransaction.getReturnId());

		returnTransaction.setProductId(3);
		assertEquals(Integer.valueOf(3), returnTransaction.getProductId());

		returnTransaction.setProductCode("12637482635892");
		assertEquals("12637482635892", returnTransaction.getProductCode());

		returnTransaction.setAmount(4);
		assertEquals(4, returnTransaction.getAmount());

		returnTransaction.setPrice(5.0);
		assertEquals(5.0, returnTransaction.getPrice(), 0.001);

		SaleTransaction saleTransaction2 = null;
		returnTransaction.setSaleTransaction(saleTransaction2);
		assertEquals(saleTransaction2, returnTransaction.getSaleTransaction());

		System.out.println(returnTransaction.toString());
	}

}
