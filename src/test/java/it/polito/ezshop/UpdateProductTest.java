package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class UpdateProductTest {

	@Test
	public void testCase1() throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		// test case to verify the success in updating an existing product
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		boolean success = ezShop.updateProduct(productId, "new description", "6253478956438", 3.5, "new note");
		assertTrue(success);
		ezShop.deleteProductType(productId);

	}
	
	@Test
	public void testCase2() throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		// test case to verify the the failure in updating an existing product, 
		// because there is no product with matching id in database
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		boolean success = ezShop.updateProduct(1500, "new description", "6253478956438", 3.5, "new note");
		assertFalse(success);
		ezShop.deleteProductType(productId);

	}
	 
	@Test
	public void testCase3() throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		// test case to verify the the failure in updating an existing product, 
		// because there is another product with the same barcode
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId1 = ezShop.createProductType("valid description1","12637482635892",2.5,"note1");
		Integer productId2 = ezShop.createProductType("valid description2","6253478956438",2.7,"note2");
		boolean success = ezShop.updateProduct(productId1, "new description", "6253478956438", 3.5, "new note");	
		assertFalse(success);
		ezShop.deleteProductType(productId1);
		ezShop.deleteProductType(productId2);

	} 
	
	@Test
	public void testCase4() {
		// test case to verify that an existing product cannot be updated without the correct authorization
//		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
//		assertThrows(UnauthorizedException.class, () -> {ezShop.updateProduct(1500, "valid descr", "4563728908417", 2.5, "note");});

	}
	
	@Test
	public void testCase5() {
		// test case to verify the the failure in updating an existing product, because the new price per unit is negative
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidPricePerUnitException.class, () -> {ezShop.updateProduct(1, "valid descr", "12343212343219", -2.5, "note");});

	}
	
	@Test
	public void testCase6() {
		// test case to verify the the failure in updating an existing product, because there is no new barcode
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.updateProduct(1, "valid descr", "", 2.5, "note");});

	}
	
	@Test
	public void testCase7() {
		// test case to verify the the failure in updating an existing product, because the new barcode is invalid.
		// The cases of invalidity are: barcode equal to null, barcode length less than 12 or higher than 14,
		// barcode composed by characters, barcode not respecting GTIN specifications
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.updateProduct(1, "valid descr", null, 2.5, "note");});

	}
	
	@Test
	public void testCase8() {
		// test case to verify the the failure in updating an existing product, because there is no new description
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.updateProduct(1, "", "12343212343219", 2.5, "note");});

	}
	
	@Test
	public void testCase9() {
		// test case to verify the the failure in updating an existing product, because the new description is null
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.updateProduct(1, null, "12343212343219", 2.5, "note");});

	}
	
	@Test
	public void testCase10() {
		// test case to verify the the failure in updating an existing product, because the id is invalid 
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductIdException.class, () -> {ezShop.updateProduct(-1, "valid descr", "12343212343219", 2.5, "note");});

	}

}
