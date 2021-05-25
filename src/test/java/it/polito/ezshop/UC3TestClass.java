package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class UC3TestClass {
	
	EZShopInterface ezShop;
	int orderId;
	int productId;
	
	@Before
	public void init() throws InvalidUsernameException, InvalidPasswordException {
		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.login("admin", "admin");
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	
	@Test
	public void testCaseScenario3_1() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException {
		ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
		assertTrue(ezShop.issueOrder("12637482635892", 5, 5.10) > 0);
	}
	
	@Test
	public void testCaseScenario3_2() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidOrderIdException {
		ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
		ezShop.recordBalanceUpdate(1000);   							//recording a positive balance before paying an order, so it can ba placed
		orderId = ezShop.issueOrder("12637482635892", 5, 5.10);
		assertTrue(ezShop.payOrder(orderId) == true);
	}
	
	@Test
	public void testCaseScenario3_3() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidOrderIdException {
		productId = ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
		ezShop.updatePosition(productId, "3-aisle-2");
		ezShop.recordBalanceUpdate(1000);
		orderId = ezShop.payOrderFor("12637482635892", 5, 5);
		assertTrue(ezShop.recordOrderArrival(orderId));
	}
	
	@Test
	public void testCaseScenario3_4() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException {
		productId = ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
		ezShop.recordBalanceUpdate(1000);
		assertTrue(ezShop.payOrderFor("12637482635892", 5, 5) > 0);
	}
	
	@Test
	public void testCaseScenario3_5() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidOrderIdException {
		ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
		ezShop.createProductType("latte", "6253478956438", 1.75, "medio");
		ezShop.recordBalanceUpdate(1000); 
		orderId = ezShop.issueOrder("12637482635892", 5, 0.1);
		ezShop.issueOrder("12637482635892", 10, 0.15);
		ezShop.payOrderFor("6253478956438", 12, 0.20);
		ezShop.payOrder(orderId);
		assertTrue((ezShop.getAllOrders().size()) > 0);
	}
	
}
