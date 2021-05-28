package it.polito.ezshop;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class ComputeBalanceTest {
	
	EZShopInterface ezShop;
	
	@Before
	public void init() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidRoleException {
		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		ezShop.recordBalanceUpdate(10);
		ezShop.recordBalanceUpdate(70);
		ezShop.recordBalanceUpdate(-80);
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}

	@Test
	public void testCase1() throws UnauthorizedException {
		
		assertTrue(ezShop.computeBalance() == 0);
	}
	
	
	@Test
	public void testCase4() {
		//This test verifies that the balance of the system can't be computed if the user does not have the rights
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.computeBalance();});
	}
	
}
