package it.polito.ezshop;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Test;


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
		
		assertTrue(shop.isStringOnlyAlphabet("abcdefghijk"));
		assertFalse(shop.isStringOnlyAlphabet("123abcd"));
		assertFalse(shop.isStringOnlyAlphabet(""));
		assertFalse(shop.isStringOnlyAlphabet(null));
		
	}

	@Test
	public void testCase5() {
		
		assertTrue(shop.isStringOnlyNumbers("1234567890"));
		assertFalse(shop.isStringOnlyNumbers("123abcd"));
		assertFalse(shop.isStringOnlyNumbers(""));
		assertFalse(shop.isStringOnlyNumbers(null));
		
	}
	
	@Test
	public void testCase6() {
		
		assertTrue(shop.isBarcodeValid("12637482635892"));
		assertTrue(shop.isBarcodeValid("1234567840"));
		assertFalse(shop.isBarcodeValid("123abcd"));
		assertFalse(shop.isBarcodeValid("1111111111111"));
		
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
