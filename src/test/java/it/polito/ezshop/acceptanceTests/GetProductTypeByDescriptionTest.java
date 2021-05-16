package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class GetProductTypeByDescriptionTest {

	@Test
	public void testCase1() throws UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException {
		// test case to verify the success in retrieving existing product(s) by description, or part of description
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId1 = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		Integer productId2 = ezShop.createProductType("valid description","6253478956438",2.5,"note");
		assertTrue(ezShop.getProductTypesByDescription("description").size() > 0);
		ezShop.deleteProductType(productId1);
		ezShop.deleteProductType(productId2);
		
	}
	 
	
	@Test 
	public void testCase2() throws UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException {
		// test case to verify the success in retrieving an empty list of products in case of description empty or null
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertTrue(ezShop.getProductTypesByDescription("").size() == 0);
		ezShop.deleteProductType(productId);

	}
	
	@Test
	public void testCase3() {
		// test case to verify that the search cannot be done without the correct authorization
//		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
//		assertThrows(UnauthorizedException.class, () -> {ezShop.getProductTypesByDescription("description");});
		
	}

}
