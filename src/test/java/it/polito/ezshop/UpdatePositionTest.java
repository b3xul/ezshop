package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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

public class UpdatePositionTest {

	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();

	
	@Test
	public void testCase1() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException  {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertTrue(ezShop.updatePosition(productId, "3-aisle-2"));
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}
	 
	
	@Test
	public void testCase2() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId1 = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		Integer productId2 = ezShop.createProductType("valid description","6253478956438",2.5,"note");
		ezShop.updatePosition(productId1, "17-corridor-42");
		assertFalse(ezShop.updatePosition(productId2, "17-corridor-42"));
		ezShop.deleteProductType(productId1);
		ezShop.deleteProductType(productId2);
		ezShop.logout();

	}
	
	@Test
	public void testCase3() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertFalse(ezShop.updatePosition(1000, "17-corridor-42"));
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}
	
	@Test
	public void testCase4() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidLocationException.class, () -> {ezShop.updatePosition(1, "corridor");});
		assertThrows(InvalidLocationException.class, () -> {ezShop.updatePosition(1, "a-corridor-2");});
		assertThrows(InvalidLocationException.class, () -> {ezShop.updatePosition(1, "2-corr4idor-2");});
		assertThrows(InvalidLocationException.class, () -> {ezShop.updatePosition(1, "2-corridor-a");});
		ezShop.logout();

	}
		
	@Test
	public void testCase5() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertTrue(ezShop.updatePosition(productId, ""));
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}

	@Test
	public void testCase6() {
		assertThrows(UnauthorizedException.class, () -> {ezShop.updatePosition(1, "4-aisle-4");});
		
	}
	
	@Test
	public void testCase7() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidProductIdException.class, () -> {ezShop.updatePosition(-1, "4-aisle-4");});
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
