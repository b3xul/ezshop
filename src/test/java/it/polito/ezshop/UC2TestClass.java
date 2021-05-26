package it.polito.ezshop;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class UC2TestClass {
	static EZShop shop = new EZShop();	
	static Integer idAdmin = null;
	
	@BeforeClass
	public static void beforeOp() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		idAdmin = shop.createUser("Giuseppe","pass","Administrator");
		shop.login("Giuseppe","pass");	
	}
	
	
	@AfterClass
	public static void AfterClassOp() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		shop.logout();
		shop.login("Mario","pass");
		shop.deleteUser(idAdmin);
	}
	
	

	//Scenario 2-1
	@Test
	public void testCaseScenario2_1() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
		int id = shop.createUser("Luca","pass","Cashier");
		assertTrue(id>0);
		shop.deleteUser(id);
	}	
	
	//Scenario 2-2
	@Test
	public void testCaseScenario2_2() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
		int id = shop.createUser("Luca","pass","Cashier");
		assertTrue(shop.deleteUser(id));
	}	
	
	//Scenario 2-3
	@Test
	public void testCaseScenario2_3() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
		int id = shop.createUser("Luca","pass","Cashier");
		assertTrue(shop.getUser(id)!=null);
		assertTrue(shop.updateUserRights(id,"Administrator"));
		assertTrue(shop.getUser(id).getRole().equals("Administrator"));
		shop.deleteUser(id);

	}
	// Scenario 2-4
	@Test
	public void testCase2_4() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidUserIdException {
		//assertTrue(shop.getAllUsers()==null);
		int id1 = shop.createUser("Giovanni","pass","Cashier");
		int id2 = shop.createUser("Giuanluca","pass","Cashier");
		assertTrue(shop.getAllUsers()!=null);
		shop.deleteUser(id1);
		shop.deleteUser(id2);
	}
	
	
}
