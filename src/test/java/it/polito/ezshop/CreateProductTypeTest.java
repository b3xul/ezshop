package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class CreateProductTypeTest { 

	@Test
	public void testCase1() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException  {
		// test case to verify the correct creation of a new product
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("new product","12637482635892",2.5,"note");
		assertTrue(productId > 0);
		ezShop.deleteProductType(productId);

	}
	
	@Test
	public void testCase2() {
		// test case to verify that a new product cannot be created without the correct authorization
		//EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		//assertThrows(UnauthorizedException.class, () -> {ezShop.createProductType("valid description","4563728908417",2.5,"note");});
				
	}
	
	@Test
	public void testCase3() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException {
		// test case to verify that is not possible to create a new product with the same barcode of a product already existent
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("first product","12637482635892",2.5,"note");
		Integer invalidId = ezShop.createProductType("creating product with same barcode","12637482635892",2.5,"note");
		assertTrue(invalidId == -1);
		ezShop.deleteProductType(productId);
		
	}
	
	@Test
	public void testCase4() {
		// test case to verify that is not possible to create a new product with negative price per unit
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidPricePerUnitException.class, () -> {ezShop.createProductType("valid description","12637482635892",-2.5,"note");});
		
	}
	
	@Test
	public void testCase5() {
		// test case to verify that is not possible to create a new product with no barcode
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description","",2.5,"note");});
		
	}
	
	@Test
	public void testCase6() {
		// test case to verify that is not possible to create a new product with invalid barcode.
		// The cases of invalidity are: barcode equal to null, barcode length less than 12 or higher than 14,
		// barcode composed by characters, barcode not respecting GTIN specifications
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.createProductType("valid description",null ,2.5,"note");});
		
	}
	
	@Test
	public void testCase7() {
		// test case to verify that is not possible to create a new product with no description
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.createProductType("","12637482635892",2.5,"note");});
		
	}
	
	@Test
	public void testCase8() {
		// test case to verify that is not possible to create a new product with description null
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductDescriptionException.class, () -> {ezShop.createProductType(null, "12637482635892",2.5,"note");});
		
	}

}
