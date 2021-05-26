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

public class GetProductTypeByBarCodeTest {

	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();

	
	@Test
	public void testCase1() throws UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("new product","12637482635892",2.5,"note");
		assertNotNull(ezShop.getProductTypeByBarCode("12637482635892"));
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}
	 
	
	@Test
	public void testCase2() throws UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("new product","12637482635892",2.5,"note");
		assertNull(ezShop.getProductTypeByBarCode("6253478956438"));
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}
	
	@Test 
	public void testCase3() {
		assertThrows(UnauthorizedException.class, () -> {ezShop.getProductTypeByBarCode("12343212347217");});
		
	}
	
	@Test
	public void testCase4() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.getProductTypeByBarCode("");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.getProductTypeByBarCode("123");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.getProductTypeByBarCode("12345678909aa");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.getProductTypeByBarCode("123456889098");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.getProductTypeByBarCode(null);});
		ezShop.logout();

	}
	
	@After
	  public void teardown() {

	    ezShop.reset();

	}

	
	
}
