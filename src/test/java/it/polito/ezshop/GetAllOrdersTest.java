package it.polito.ezshop;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class GetAllOrdersTest {

	EZShopInterface ezShop;
	int orderId;
	
	@Before
	public void init() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidOrderIdException {
		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
		ezShop.createProductType("latte", "6253478956438", 1.75, "medio");
		ezShop.recordBalanceUpdate(1000); 
		orderId = ezShop.issueOrder("12637482635892", 5, 0.1);
		ezShop.issueOrder("12637482635892", 10, 0.15);
		ezShop.payOrderFor("6253478956438", 12, 0.20);
		ezShop.payOrder(orderId);
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}

	@Test
	public void testCase1() {
		//This test verifies that the orders can't be listed if the user does not have the rights to perform the operation
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.getAllOrders();});
	}
	
	
	@Test
	public void testCase2() throws UnauthorizedException {
		//This test verifies that the list of orders is retrieved correctly
		assertTrue((ezShop.getAllOrders().size()) > 0);
		
	}
}
