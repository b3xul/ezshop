package it.polito.ezshop.methodsTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class UpdateQuantityTest { 

	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	
	@Test
	public void testCase1() throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidLocationException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		ezShop.updatePosition(productId, "1-aisle-1");
		assertTrue(ezShop.updateQuantity(productId, 100));
		assertTrue(ezShop.updateQuantity(productId, -10));
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}
	 
	
	@Test
	public void testCase2() throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertFalse(ezShop.updateQuantity(productId, 10));
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}
	
	@Test
	public void testCase3() throws UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		ezShop.updatePosition(productId, "1-aisle-1");
		ezShop.updateQuantity(productId, 100);
		assertFalse(ezShop.updateQuantity(productId, -1000));
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}
	
	@Test
	public void testCase4() throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertFalse(ezShop.updateQuantity(1000, 10));
		ezShop.deleteProductType(productId); 
		ezShop.logout();

	}
	 
	@Test
	public void testCase5() {
		assertThrows(UnauthorizedException.class, () -> {ezShop.updateQuantity(1, 10);});
		
	}
	
	@Test
	public void testCase6() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidProductIdException.class, () -> {ezShop.updateQuantity(-1, 10);});
		ezShop.logout();

	}

	@After
	  public void teardown() {

	    ezShop.reset();

	}


}
