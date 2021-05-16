package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class DeleteProductTypeTest {

	@Test
	public void testCase1() throws InvalidProductIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		boolean success = ezShop.deleteProductType(productId);
		assertTrue(success);
		 
	}
	
	@Test
	public void testCase2() {
		
//		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
//		assertThrows(UnauthorizedException.class, () -> {ezShop.deleteProductType(1);});

	}
	
	@Test
	public void testCase3() throws InvalidProductIdException, UnauthorizedException {
		
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductIdException.class, () -> {ezShop.deleteProductType(-1);});
		
	}
	
}
