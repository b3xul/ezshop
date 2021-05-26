package it.polito.ezshop;

import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import it.polito.ezshop.data.Implementations.EZShopDAO;

public class DAOUserMethodsClassTest {

	static EZShopDAO DAO = new EZShopDAO();
	
	
	@BeforeClass
	  static public void BeforeClass() {
		DAO.reset();
	}
	
	@AfterClass
	  static public void AfterClass() {
	    DAO.reset();
	}
	
	// CREATE
	@Test
	public void testCase1() {
		int id = DAO.createUser("Luca","pass","Cashier");
		assertTrue(id>0);
		DAO.deleteUser(id);

	}
	
	// User already exist 
	@Test
	public void testCase2() {
		int id = DAO.createUser("Luca","pass","Cashier");
		assertTrue(DAO.createUser("Luca","pass","Cashier")==-1);
		DAO.deleteUser(id);
	}			

	// DELETE
	@Test
	public void testCase3() {
		int id = DAO.createUser("Federico","pass","Cashier");
		assertTrue(DAO.deleteUser(id));
	}

	// User not found
	@Test
	public void testCase4() {
		int id = DAO.createUser("Federico","pass","Cashier");
		DAO.deleteUser(id);
		assertFalse(DAO.deleteUser(id));
	}	
	// GET USERS
	@Test
	public void testCase5() {
		int id1 = DAO.createUser("Giovanni","pass","Cashier");
		int id2 = DAO.createUser("Federico","pass","ShopManager");
		assertTrue(DAO.getAllUsers()!=null);
		DAO.deleteUser(id1);
		DAO.deleteUser(id2);

	}
	
	// GET USER
	@Test
	public void testCase6() {
		int id  = DAO.createUser("Federico","pass","Cashier");
		assertTrue(DAO.getUser(id)!=null);
		DAO.deleteUser(id);
	}
	
	// User not found
	@Test
	public void testCase7() {
		int id  = DAO.createUser("Federico","pass","Cashier");
		DAO.deleteUser(id);
		assertTrue(DAO.getUser(id)==null);	
	}
			
	
	// UPDATE USER RIGHTS
	@Test
	public void testCase8() {
		int id = DAO.createUser("Luca","pass","Cashier");
		assertTrue(DAO.getUser(id)!=null);
		assertTrue(DAO.updateUserRights(id,"Administrator"));
		assertTrue(DAO.getUser(id).getRole().equals("Administrator"));
		DAO.deleteUser(id);
	}
	
	// User not found
	@Test
	public void testCase9() {
		int id = DAO.createUser("Luca","pass","Cashier");
		DAO.deleteUser(id);
		assertFalse(DAO.updateUserRights(id,"Administrator"));
	}						

	// LOGIN
	@Test
	public void testCase10() {
		int id = DAO.createUser("Giuseppe","pass","Administrator");
		assertTrue(DAO.login("Giuseppe","pass")!=null);
		DAO.deleteUser(id);
	}
	
	// wrong password 
	@Test
	public void testCase11() {
		int id = DAO.createUser("Giuseppe","pass","Administrator");
		assertTrue(DAO.login("Giuseppe","password")==null);
		DAO.deleteUser(id);
	}						


}
