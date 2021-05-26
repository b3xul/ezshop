package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;

public class UpdateProductTest {

	static EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	
	@Test
	public void testCase1() throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		System.out.println(productId);
		boolean success = ezShop.updateProduct(productId, "new description", "6253478956438", 3.5, "new note");
		assertTrue(success);
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}
	
	@Test
	public void testCase2() throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		boolean success = ezShop.updateProduct(1500, "new description", "6253478956438", 3.5, "new note");
		assertFalse(success);
		ezShop.deleteProductType(productId);
		ezShop.logout();

	}
	 
	@Test
	public void testCase3() throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		Integer productId1 = ezShop.createProductType("valid description1","12637482635892",2.5,"note1");
		Integer productId2 = ezShop.createProductType("valid description2","6253478956438",2.7,"note2");
		boolean success = ezShop.updateProduct(productId1, "new description", "6253478956438", 3.5, "new note");	
		assertFalse(success);
		ezShop.deleteProductType(productId1);
		ezShop.deleteProductType(productId2);
		ezShop.logout();

	} 
	
	@Test
	public void testCase4() {
		assertThrows(UnauthorizedException.class, () -> {ezShop.updateProduct(1500, "valid descr", "4563728908417", 2.5, "note");});

	}
	
	@Test
	public void testCase5() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidPricePerUnitException.class, () -> {ezShop.updateProduct(1, "valid descr", "12343212343219", -2.5, "note");});
		ezShop.logout();

	}
	
	@Test
	public void testCase6() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.updateProduct(1, "valid descr", "", 2.5, "note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.updateProduct(1, "valid description","123" ,2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.updateProduct(1, "valid description","12345678909aa" ,2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.updateProduct(1, "valid description","123456889098" ,2.5,"note");});
		ezShop.logout();

	}
	
	@Test
	public void testCase7() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.updateProduct(1, "valid descr", null, 2.5, "note");});
		ezShop.logout();

	}
	
	@Test
	public void testCase8() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("admin","admin");
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.updateProduct(1, "", "12343212343219", 2.5, "note");});
		ezShop.logout();

	}
	
	@Test
	public void testCase9() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("MRKrab","wow");
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.updateProduct(1, null, "12343212343219", 2.5, "note");});
		ezShop.logout();

	}
	
	@Test
	public void testCase10() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.login("MRKrab","wow");
		assertThrows(InvalidProductIdException.class, () -> {ezShop.updateProduct(-1, "valid descr", "12343212343219", 2.5, "note");});
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
