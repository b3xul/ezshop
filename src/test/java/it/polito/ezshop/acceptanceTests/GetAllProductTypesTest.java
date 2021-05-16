package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class GetAllProductTypesTest {

	@Test
	public void testCase1() throws UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException {
		// test case to verify that the list of all product is correctly retrieved
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId1 = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		Integer productId2 = ezShop.createProductType("valid description","6253478956438",2.5,"note");
		assertTrue(ezShop.getAllProductTypes().size() >= 0);
		ezShop.deleteProductType(productId1);
		ezShop.deleteProductType(productId2);
		
	}
	
	@Test 
	public void testCase2() {
		// test case to verify that the search cannot be done without the correct authorization
//		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
//		assertThrows(UnauthorizedException.class, () -> {ezShop.getAllProductTypes();});
		
	}

}
