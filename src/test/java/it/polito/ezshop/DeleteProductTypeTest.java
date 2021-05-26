package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class DeleteProductTypeTest {

	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	
	@Test
	public void testCase1() throws InvalidProductIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		boolean success = ezShop.deleteProductType(productId);
		assertTrue(success);
		ezShop.logout();

	}
	
	@Test
	public void testCase2() {
		
		assertThrows(UnauthorizedException.class, () -> {ezShop.deleteProductType(1);});

	}
	
	@Test
	public void testCase3() throws InvalidProductIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidProductIdException.class, () -> {ezShop.deleteProductType(-1);});
		ezShop.logout();

	}
	
	@BeforeClass
	  static public void BeforeClass() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		
	}
	
	@AfterClass
	  static public void AfterClass() {

	    ezShop.reset();

	}


}
