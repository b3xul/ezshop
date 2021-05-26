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

public class RecordBalanceUpdateTest {
	
	EZShopInterface ezShop;
	
	@Before
	public void init() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}

	@Test
	public void testCase1() throws UnauthorizedException {
		//This method asserts that the balance operation can't be added to the system due to negative balance
		assertFalse(ezShop.recordBalanceUpdate(-200));
	}
	
	@Test
	public void testCase2() {
		//This test verifies that the balance operation can't be added if the user doesn't have the rights
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.recordBalanceUpdate(40);});
		
	}
	
	@Test
	public void testCase3() throws UnauthorizedException {
		//This method asserts that the balance operation is added correctly to the system
		assertTrue(ezShop.recordBalanceUpdate(100));
	}
}
