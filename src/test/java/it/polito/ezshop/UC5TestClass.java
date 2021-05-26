package it.polito.ezshop;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;

public class UC5TestClass {
	static EZShop shop = new EZShop();	
	
	@BeforeClass
	public static void beforeOp() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		shop.createUser("Giuseppe","pass","Administrator");
	}
	@AfterClass
	public static void AfterOp() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		shop.reset();
	}
	
	// Scenario 5-1 
	@Test
	public void testCase5_1() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		assertTrue(shop.login("Giuseppe","pass")!=null);
	}
				
	// Scenario 5-1 variant (wrong password) 
	@Test
	public void testCase5_1_variant() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		assertTrue(shop.login("Giuseppe","password")==null);
	}
	
	// Scenario 5-2 	
	@Test
	public void testCase5_2() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		shop.login("Maria","pass");
		assertTrue(shop.logout());
	}
	
}
