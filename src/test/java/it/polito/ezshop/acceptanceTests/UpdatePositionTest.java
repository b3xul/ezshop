package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class UpdatePositionTest {

	@Test
	public void testCase1() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException  {
		// test case to verify the success in updating the position of an existing product
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertTrue(ezShop.updatePosition(productId, "3 aisle 2"));
		ezShop.deleteProductType(productId);
		
	}
	 
	
	@Test
	public void testCase2() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		// test case to verify the failure in updating the quantity of an existing product,
		// because the new osition is already assigned to another product
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId1 = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		Integer productId2 = ezShop.createProductType("valid description","6253478956438",2.5,"note");
		ezShop.updatePosition(productId1, "17 corridor 42");
		assertFalse(ezShop.updatePosition(productId2, "17 corridor 42"));
		ezShop.deleteProductType(productId1);
		ezShop.deleteProductType(productId2);

	}
	
	@Test
	public void testCase3() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		// test case to verify the failure in updating the quantity of an existing product,
		// because there is no product with matching id in database
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertFalse(ezShop.updatePosition(1000, "17 corridor 42"));
		ezShop.deleteProductType(productId);

	}
	
	@Test
	public void testCase4() {
		// test case to verify the failure in updating the quantity of an existing product, because the location passed is invalid.
		// The cases of invalidity are: string new position is not composed of three substring divided by a blank, 
		// string new position is not in the form <Integer - alphabetic String - Integer>
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidLocationException.class, () -> {ezShop.updatePosition(1, "corridor");});
		
	}
		
	@Test
	public void testCase5() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		// test case to verify the success in updating the quantity of an existing product to blank
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertTrue(ezShop.updatePosition(productId, ""));
		ezShop.deleteProductType(productId);

	}

	@Test
	public void testCase6() {
		// test case to verify that the position of an existing product cannot be updated without the correct authorization
//		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
//		assertThrows(UnauthorizedException.class, () -> {ezShop.updatePosition(1, "4 aisle 4");});
		
	}
	
	@Test
	public void testCase7() {
		// test case to verify the the failure in updating the position of an existing product, because the id is invalid 
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductIdException.class, () -> {ezShop.updatePosition(-1, "4 aisle 4");});
		
	}
}
