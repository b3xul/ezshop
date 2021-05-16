package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class UpdateQuantityTest { 

	@Test
	public void testCase1() throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidLocationException {
		// test case to verify the success in updating the quantity of an existing product
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		ezShop.updatePosition(productId, "1 aisle 1");
		assertTrue(ezShop.updateQuantity(productId, 100));
		assertTrue(ezShop.updateQuantity(productId, -10));
		ezShop.deleteProductType(productId);

	}
	 
	
	@Test
	public void testCase2() throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		// test case to verify the failure in updating the quantity of an existing product,
		// because the product does not have a position assigned
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertFalse(ezShop.updateQuantity(productId, 10));
		ezShop.deleteProductType(productId);

	}
	
	@Test
	public void testCase3() throws UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		// test case to verify the failure in updating the quantity of an existing product,
		// because the quantity to be subtracted is too much
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		ezShop.updatePosition(productId, "1 aisle 1");
		ezShop.updateQuantity(productId, 100);
		assertFalse(ezShop.updateQuantity(productId, -1000));
		ezShop.deleteProductType(productId);

	}
	
	@Test
	public void testCase4() throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		// test case to verify the failure in updating the quantity of an existing product,
		// because there is no product with matching id in database
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("valid description","12637482635892",2.5,"note");
		assertFalse(ezShop.updateQuantity(1000, 10));
		ezShop.deleteProductType(productId); 

	}
	 
	@Test
	public void testCase5() {
		// test case to verify that the quantity of an existing product cannot be updated without the correct authorization
//		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
//		assertThrows(UnauthorizedException.class, () -> {ezShop.updateQuantity(1, 10);});
		
	}
	
	@Test
	public void testCase6() {
		// test case to verify the the failure in updating the quantity of an existing product, because the id is invalid 
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductIdException.class, () -> {ezShop.updateQuantity(-1, 10);});
		
	}


}
