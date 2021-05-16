package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class GetProductTypeByBarCodeTest {

	@Test
	public void testCase1() throws UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException {
		// test case to verify the success in retrieving an existing product by barcode
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("new product","12637482635892",2.5,"note");
		assertNotNull(ezShop.getProductTypeByBarCode("12637482635892"));
		ezShop.deleteProductType(productId);
		
	}
	 
	
	@Test
	public void testCase2() throws UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException {
		// test case to verify the failure in retrieving an existing product by barcode,
		// because there is no product with matching barcode
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		Integer productId = ezShop.createProductType("new product","12637482635892",2.5,"note");
		assertNull(ezShop.getProductTypeByBarCode("6253478956438"));
		ezShop.deleteProductType(productId);
		
	}
	
	@Test 
	public void testCase3() {
		// test case to verify that the search cannot be done without the correct authorization
//		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
//		assertThrows(UnauthorizedException.class, () -> {ezShop.getProductTypeByBarCode("12343212347217");});
		
	}
	
	@Test
	public void testCase4() {
		// test case to verify the failure in retrieving an existing product by barcode,
		// because the barcode passed is empty
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.getProductTypeByBarCode("");});

	}
	
	@Test
	public void testCase5() {
		// test case to verify the failure in retrieving an existing product by barcode, because the barcode passed is invalid.
		// The cases of invalidity are: barcode equal to null, barcode length less than 12 or higher than 14,
		// barcode composed by characters, barcode not respecting GTIN specifications
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		assertThrows(InvalidProductCodeException.class, () -> {ezShop.getProductTypeByBarCode(null);});

	}
}
