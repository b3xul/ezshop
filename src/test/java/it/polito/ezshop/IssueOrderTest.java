package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class IssueOrderTest {
	
	EZShopInterface ezShop;
	
	@Before
	public void init() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
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
	public void testCase1() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductDescriptionException {
		
		assertTrue(ezShop.issueOrder("12637482635892", 5, 5.10) > 0);
//		assertTrue(ezShop.issueOrder("12637482635892", 10, 2.30) > 0);
//		assertTrue(ezShop.issueOrder("6253478956438", 3, 5.45) > 0);
//		assertTrue(ezShop.issueOrder("6253478956438", 5, 10.45) > 0);
		
	}
	

	@Test
	public void testCase2() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		//This test verifies there is no product with this barcode in the db
		assertTrue(ezShop.issueOrder("6253478956438", 10, 10) < 0);
	}
	
	
	@Test
	public void testCase3() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		//This test verifies that the order can't be issued if the quantity of the product is not valid
		assertThrows(InvalidQuantityException.class, () -> {ezShop.issueOrder("12637482635892", -2, 5);});
	}
	
	
	@Test
	public void testCase4() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		//This test verifies that the order can't be issued if the price per unit of the product is not valid
		assertThrows(InvalidPricePerUnitException.class, () -> {ezShop.issueOrder("12637482635892", 3, -3);});
	}
	
	
	@Test
	public void testCase5() {
		//This test verifies that the order can't be issued if the user doesn't have the rights
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.issueOrder("12637482635892", 5, 5.10);});
	}
	
	@Test
	public void testCase6() {
		//This test verifies that the order can't be issued if the product code is null
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.issueOrder(null, 1, 1);});
		//This test verifies that the order can't be issued if the product code length is either < 12 or > 14
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.issueOrder("1234", 1, 1);});
		//This test verifies that the order can't be issued if the product code contains letters
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.issueOrder("no letters!!!", 1, 1);});
		//This test verifies that the order can't be issued if the product code does not match GTIN specifications
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.issueOrder("0123456789100", 10, 10);});
	}
	
	
}
