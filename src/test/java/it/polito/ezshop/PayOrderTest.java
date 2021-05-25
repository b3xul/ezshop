package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class PayOrderTest {
	
	EZShopInterface ezShop;
	
	@Before
	public void init() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException {
		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.login("admin", "admin");
		ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
		ezShop.issueOrder("12637482635892", 5, 2.5);
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	@Test
	public void testCase1() throws InvalidOrderIdException, UnauthorizedException {
		
		ezShop.recordBalanceUpdate(1000);   							//recording a positive balance before paying an order, so it can ba placed
		assertTrue(ezShop.payOrder(1) == true);
//		assertTrue(ezShop.payOrder(2) == true);
//		assertTrue(ezShop.payOrder(3) == true);
//		assertThrows(InvalidOrderIdException.class,() -> {ezShop.payOrder(0);});
	}
	
	@Test
	public void testCase2() {
		//This test verifies that the order can't be payed if the user doesn't have the rights
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.payOrder(1);});
	}
	
	@Test
	public void testCase3() {
		//This test verifies that the order can't be payed if the order doesn't have a valid id
		assertThrows(InvalidOrderIdException.class, () -> {ezShop.payOrder(0);});
	}
	
	@Test
	public void testCase4() throws InvalidOrderIdException, UnauthorizedException {
		//This test asserts there is no issued order with this id in the system
		assertTrue(ezShop.payOrder(10) == false);
	}
	
	@Test
	public void testCase5() throws InvalidOrderIdException, UnauthorizedException {
		//This test asserts that an issued order can't be payed since the balance of the system will turn negative
		assertTrue(ezShop.payOrder(1) == false);

	}
}
