package it.polito.ezshop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;

public class EZShopClassTest {

	it.polito.ezshop.data.EZShop shop = new it.polito.ezshop.data.EZShop();

	@Test
	public void testCase1() {

		assertNotNull(shop.dbAccess());

	}

	@Test
	public void testCase2() {

		Connection conn = shop.dbAccess();
		shop.dbClose(conn);

	}

	@Test
	public void testCase3() {

		shop.dbClose(null);

	}

	@Test
	public void testCase4() {

		assertTrue(EZShop.isStringOnlyAlphabet("abcdefghijk"));
		assertFalse(EZShop.isStringOnlyAlphabet("123abcd"));
		assertFalse(EZShop.isStringOnlyAlphabet(""));
		assertFalse(EZShop.isStringOnlyAlphabet(null));

	}

	@Test
	public void testCase5() {

		assertTrue(EZShop.isStringOnlyNumbers("1234567890"));
		assertFalse(EZShop.isStringOnlyNumbers("123abcd"));
		assertFalse(EZShop.isStringOnlyNumbers(""));
		assertFalse(EZShop.isStringOnlyNumbers(null));

	}

	@Test
	public void testCase6() {

		assertTrue(EZShop.isBarcodeValid("12637482635892"));
		assertTrue(EZShop.isBarcodeValid("1234567840"));
		assertFalse(EZShop.isBarcodeValid("123abcd"));
		assertFalse(EZShop.isBarcodeValid("1111111111111"));

	}

	@Test
	public void testCase7() {

		assertTrue(shop.checkLuhn("4716258050958645"));
		assertFalse(shop.checkLuhn("11111111"));
		assertFalse(shop.checkLuhn("123abcd"));
		assertFalse(shop.checkLuhn(""));
		assertFalse(shop.checkLuhn(null));

	}
	

}
