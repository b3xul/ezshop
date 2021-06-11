package it.polito.ezshop;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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
import it.polito.ezshop.exceptions.InvalidRFIDException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class RecordOrderArrivalRFIDTest {
	
	EZShopInterface ezShop;
	int productId;
	int orderId;
	
	@Before
	public void init() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException {
		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
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
	public void testCase1() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidOrderIdException, InvalidRFIDException {
		ezShop.updatePosition(productId, "3-aisle-2");
		assertTrue(ezShop.recordOrderArrivalRFID(orderId, "000000100000"));
	}
	
	@Test
	public void testCase2() {
		//This test verifies that an arrival can be recorded it the user does not have the rights to perform it
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.recordOrderArrivalRFID(orderId, "000000100000");});
	}
	
	@Test
	public void testCase3() {
		//This test verifies that an arrival can't be recorded if the order doesn't have a valid id
		assertThrows(InvalidOrderIdException.class, () -> {ezShop.recordOrderArrivalRFID(0, "000000100000");});
	}
	
	@Test
	public void testCase4() {
		assertThrows(InvalidRFIDException.class, () -> {ezShop.recordOrderArrivalRFID(orderId, null);});
		assertThrows(InvalidRFIDException.class, () -> {ezShop.recordOrderArrivalRFID(orderId, "0001");});
		assertThrows(InvalidRFIDException.class, () -> {ezShop.recordOrderArrivalRFID(orderId, "0000000ABC34");});
	}
	
}
