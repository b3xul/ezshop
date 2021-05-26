package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;

import it.polito.ezshop.exceptions.*;

public class GetAllProductTypesTest {

	static EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();

	
	@Test
	public void testCase1() throws UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId1 = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		Integer productId2 = ezShop.createProductType("valid description","6253478956438",2.5,"note");
		assertTrue(ezShop.getAllProductTypes().size() >= 0);
		ezShop.deleteProductType(productId1);
		ezShop.deleteProductType(productId2);
		ezShop.logout();

	}
	
	@Test 
	public void testCase2() throws InvalidUsernameException, InvalidPasswordException {
		assertThrows(UnauthorizedException.class, () -> {ezShop.getAllProductTypes();});

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
