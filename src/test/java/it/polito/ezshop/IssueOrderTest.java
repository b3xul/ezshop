package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.User;
import it.polito.ezshop.data.Implementations.UserImpl;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class IssueOrderTest {
	
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	
	@Test
	public void testCase1() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		
		assertTrue(ezShop.issueOrder("12637482635892", 5, 5.10) > 0);
		assertTrue(ezShop.issueOrder("12637482635892", 10, 2.30) > 0);
		assertTrue(ezShop.issueOrder("6253478956438", 3, 5.45) > 0);
		assertTrue(ezShop.issueOrder("6253478956438", 5, 10.45) > 0);
		
	}
	
	//Invalid productCode
//	@Test
//	public void testCase2() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
//		
//		assertThrows(InvalidProductCodeException.class, () -> {(ezShop.issueOrder("34567891230089", 10, 10) < 0);});
//	}
//	
//	//no proudct with this barcode
//	@Test
//	public void testCase3() throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
//		
//		assertTrue(ezShop.issueOrder("34567891230089", 10, 10) < 0);
//	}
	
	//invalid quantity ex.
	
	//invalid pricePerunit ex.
}
