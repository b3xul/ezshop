package it.polito.ezshop;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PayOrderForTest {
	
	EZShopInterface ezShop;
	
	@Before
	public void init() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.login("admin", "admin");
		ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	@Test
	public void testCase1() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		
		ezShop.recordBalanceUpdate(1000);   							//recording a positive balance before paying an order, so it can ba placed
		assertTrue(ezShop.payOrderFor("12637482635892", 5, 5.10) > 0);
//		assertTrue(ezShop.payOrderFor("12637482635892", 10, 2.30) > 0);
//		assertTrue(ezShop.payOrderFor("6253478956438", 3, 5.45) > 0);
//		assertTrue(ezShop.payOrderFor("6253478956438", 5, 10.45) > 0);
	}
	
	
	@Test
	public void testCase2() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		//This test verifies that the order can't be placed and payed if the quantity of the product is not valid
		assertThrows(InvalidQuantityException.class, () -> {ezShop.payOrderFor("12637482635892", -2, 5);});
	}
	
	
	@Test
	public void testCase3() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		//This test verifies that the order can't be placed and payed if the price per unit of the product is not valid
		assertThrows(InvalidPricePerUnitException.class, () -> {ezShop.payOrderFor("12637482635892", 3, -3);});
	}
	
	@Test
	public void testCase5() {
		//This test verifies that the order can't be placed and payed if the user doesn't have the rights
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.payOrderFor("12637482635892", 5, 5.10);});
	}
	
	@Test
	public void testCase6() {
		//This test verifies that the order can't be placed and payed if the product code is null
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.payOrderFor(null, 1, 1);});
		//This test verifies that the order can't be placed and payed if the product code length is either < 12 or > 14
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.payOrderFor("1234", 1, 1);});
		//This test verifies that the order can't be placed and payed if the product code contains letters
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.payOrderFor("no letters!!!", 1, 1);});
		//This test verifies that the order can't be placed and payed if the product code does not match GTIN specifications
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.payOrderFor("0123456789100", 10, 10);});
	}
	
	@Test
	public void testCase7() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		//This test verifies there is no product with this barcode in the db
		assertTrue(ezShop.payOrderFor("6253478956438", 10, 10) < 0);
	}
	
	@Test
	public void testCase8() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		//This test returns a negative id since the balance of the system will turn negative if the order is placed
		assertTrue(ezShop.payOrderFor("12637482635892", 10, 10) < 0);
	}
}
