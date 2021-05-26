package it.polito.ezshop.methodsTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class CreateProductTypeTest { 

	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	
	@Test
	public void testCase1() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidUsernameException, InvalidPasswordException  {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("new product","12637482635892",2.5,"note");
		assertTrue(productId > 0);
		ezShop.deleteProductType(productId);
		ezShop.logout();
	}
	
	@Test
	public void testCase2() {		
		assertThrows(UnauthorizedException.class, () -> {ezShop.createProductType("valid description","4563728908417",2.5,"note");});
	}
	
	@Test
	public void testCase3() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("first product","12637482635892",2.5,"note");
		Integer invalidId = ezShop.createProductType("creating product with same barcode","12637482635892",2.5,"note");
		assertTrue(invalidId == -1);
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}
	
	@Test
	public void testCase4() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidPricePerUnitException.class, () -> {ezShop.createProductType("valid description","12637482635892",-2.5,"note");});
		ezShop.logout();

	}
	
	@Test
	public void testCase5() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description","",2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description",null ,2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description","123" ,2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description","12345678909aa" ,2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description","123456889098" ,2.5,"note");});
		ezShop.logout();

	}
	
	@Test
	public void testCase6() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.createProductType("","12637482635892",2.5,"note");});
		ezShop.logout();

	}
	
	@Test
	public void testCase7() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.createProductType(null, "12637482635892",2.5,"note");});
		ezShop.logout();

	}
	
	@After
	  public void teardown() {

	    ezShop.reset();

	}

	


}
