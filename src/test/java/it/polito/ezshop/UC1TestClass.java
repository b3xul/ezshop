package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
public class UC1TestClass {

	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	
	@Test
	public void testCaseScenario1_1() {
		try {
			ezShop.login("admin","admin");
			Integer productId = ezShop.createProductType("new product","12637482635892",2.5,"note");
			assertTrue(productId > 0);
			Integer invalidId = ezShop.createProductType("creating product with same barcode","12637482635892",2.5,"note");
			assertTrue(invalidId == -1);
			ezShop.deleteProductType(productId);
		} catch (Exception e) {
			
		} 
		ezShop.logout();
	}
	
	@Test
	public void testCaseExceptionsScenario1_1(){
		assertThrows(UnauthorizedException.class, () -> {ezShop.createProductType("valid description","4563728908417",2.5,"note");});
		assertThrows(InvalidPricePerUnitException.class, () -> {ezShop.createProductType("valid description","12637482635892",-2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description","",2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description",null ,2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description","123" ,2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description","12345678909aa" ,2.5,"note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description","123456889098" ,2.5,"note");});
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.createProductType(null, "12637482635892",2.5,"note");});

	}
	
	@Test
	public void testCaseScenario1_2() {
		try {
			ezShop.login("admin","admin");
			Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
			assertTrue(ezShop.updatePosition(productId, "3-aisle-2"));
			ezShop.deleteProductType(productId);
		} catch (Exception e) {
			
		} 
		ezShop.logout();
	}
	
	@Test
	public void testCaseExceptionsScenario1_2(){
		try {
			ezShop.login("admin","admin");
			Integer productId1 = ezShop.createProductType("valid description","12637482635892",2.5,"note");
			Integer productId2 = ezShop.createProductType("valid description","6253478956438",2.5,"note");
			ezShop.updatePosition(productId1, "17-corridor-42");
			assertFalse(ezShop.updatePosition(productId2, "17-corridor-42"));
			assertTrue(ezShop.updatePosition(productId2, ""));
			assertFalse(ezShop.updatePosition(1000, "17-corridor-42"));
			ezShop.deleteProductType(productId1);
			ezShop.deleteProductType(productId2);
		} catch (Exception e) {
			
		}
		assertThrows(InvalidLocationException.class, () -> {ezShop.updatePosition(1, "corridor");});
		assertThrows(InvalidLocationException.class, () -> {ezShop.updatePosition(1, "a-corridor-2");});
		assertThrows(InvalidLocationException.class, () -> {ezShop.updatePosition(1, "2-corr4idor-2");});
		assertThrows(InvalidLocationException.class, () -> {ezShop.updatePosition(1, "2-corridor-a");});
		assertThrows(InvalidProductIdException.class, () -> {ezShop.updatePosition(-1, "4-aisle-4");});
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.updatePosition(1, "4-aisle-4");});
	}
	
	@Test
	public void testCaseScenario1_3() {
		try {
			ezShop.login("admin","admin");
			Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
			boolean success = ezShop.updateProduct(productId, "new description", "6253478956438", 3.5, "new note");
			assertTrue(success);
			ezShop.deleteProductType(productId);
		} catch (Exception e) {
			
		} 
		ezShop.logout();
	}
	
	@Test
	public void testCaseExceptionsScenario1_3(){
		try {
			ezShop.login("admin","admin");
			Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
			boolean success1 = ezShop.updateProduct(1500, "new description", "6253478956438", 3.5, "new note");
			assertFalse(success1);
			ezShop.deleteProductType(productId);
			boolean success2 = ezShop.updateProduct(productId, "new description", "6253478956438", 3.5, "new note");	
			assertFalse(success2);
			
			ezShop.deleteProductType(productId);
		} catch (Exception e) {
			
		}
		assertThrows(InvalidPricePerUnitException.class, () -> {ezShop.updateProduct(1, "valid descr", "12343212343219", -2.5, "note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.updateProduct(1, "valid descr", "", 2.5, "note");});
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.updateProduct(1, "valid descr", null, 2.5, "note");});
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.updateProduct(1, "", "12343212343219", 2.5, "note");});
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.updateProduct(1, null, "12343212343219", 2.5, "note");});
		assertThrows(InvalidProductIdException.class, () -> {ezShop.updateProduct(-1, "valid descr", "12343212343219", 2.5, "note");});
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.updateProduct(1, "valid descr", "4563728908417", 2.5, "note");});
	}
	
	@Test
	public void testCaseScenario1_4() {
		try {
			ezShop.login("admin","admin");
			Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
			boolean success = ezShop.deleteProductType(productId);
			assertTrue(success);
		} catch (Exception e) {
			
		} 
		ezShop.logout();
	}
	
	@Test
	public void testCaseExceptionsScenario1_4(){
		try {
			ezShop.login("admin","admin");
			
		} catch (Exception e) {
			
		}
		assertThrows(InvalidProductIdException.class, () -> {ezShop.deleteProductType(-1);});
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.deleteProductType(1);});
	}
	
	@Test
	public void testCaseScenario1_5() {
		try {
			ezShop.login("admin","admin");
			Integer productId1 = ezShop.createProductType("valid description","12637482635892",2.5,"note");
			Integer productId2 = ezShop.createProductType("valid description","6253478956438",2.5,"note");
			assertTrue(ezShop.getAllProductTypes().size() >= 0);
			ezShop.deleteProductType(productId1);
			ezShop.deleteProductType(productId2);
		} catch (Exception e) {
			
		} 
		ezShop.logout();
	}
	
	@Test
	public void testCaseExceptionsScenario1_5(){
		try {
			ezShop.login("admin","admin");
			
		} catch (Exception e) {
			
		}
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.getAllProductTypes();});
	}

	@After
	  public void teardown() {

	    ezShop.reset();

	}
}
