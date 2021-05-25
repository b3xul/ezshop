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
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class RecordOrderArrivalTest {
	
	EZShopInterface ezShop;
	int productId;
	int orderId;
	
	@Before
	public void init() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException {
		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.login("admin", "admin");
		productId = ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
		ezShop.recordBalanceUpdate(1000);   							//recording a positive balance before paying an order, so it can be placed
		orderId = ezShop.payOrderFor("12637482635892", 5, 5);
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	@Test
	public void testCase1() throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException, InvalidProductIdException {
		ezShop.updatePosition(productId, "3-aisle-2");
		//record order arrival performed
		assertTrue(ezShop.recordOrderArrival(orderId) == true);
//		assertTrue(ezShop.recordOrderArrival(7) == true);
//		assertTrue(ezShop.recordOrderArrival(20) == false);
//		assertThrows(InvalidOrderIdException.class, () -> {ezShop.recordOrderArrival(0);});
	}
	
	@Test
	public void testCase2() {
		//This test verifies that an arrival can be recorded it the user does not have the rights to perform it
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.recordOrderArrival(orderId);});
	}
	
	@Test
	public void testCase3() {
		//This test verifies that an arrival can't be recorded if the order doesn't have a valid id
		assertThrows(InvalidOrderIdException.class, () -> {ezShop.recordOrderArrival(0);});
	}
	
	
	@Test
	public void testCase4() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
		//ezShop.updatePosition(productId, "3-aisle-2");
		//This test verifies that an arrival can't be recorded if the product does not have a valid location
		assertThrows(InvalidLocationException.class, () -> {ezShop.recordOrderArrival(orderId);});
	}
	
	
	@Test
	public void testCase5() throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
		//This test verifies that there is not a PAYED order with this id
		assertTrue(ezShop.recordOrderArrival(10) == false);
	}
	
	
}
