package it.polito.ezshop;
import static org.junit.Assert.*;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;




public class UserMethodsClassTest {
		
		static EZShop shop = new EZShop();
		static Integer idAdmin = null;
		static Integer idShopMan = null;
		
		@BeforeClass
		public static void initialOperation() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			shop.reset();
			idAdmin = shop.createUser("Giuseppe","pass","Administrator");
			idShopMan = shop.createUser("Maria","pass","ShopManager");
			
		}
		@AfterClass
		public static void AfterClassOp() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
			shop.reset();
		}
		
//Integer createUser(String username, String password, String role)
	//throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException
		
		// OK Scenario 2-1
		@Test
		public void testCase1() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
			shop.login("Giuseppe","pass");
			int id = shop.createUser("Luca","pass","Cashier");
			assertTrue(id>0);
			shop.deleteUser(id);
			shop.logout();
		}
		
		// User already exist 
		@Test
		public void testCase2() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
			int id = shop.createUser("Luca","pass","Cashier");
			assertTrue(shop.createUser("Luca","pass","Cashier")==-1);
			shop.login("Giuseppe","pass");
			shop.deleteUser(id);
			shop.logout();
		}
	
		// Exception  
		@Test
		public void testCase3() {
			//InvalidUsernameException
			assertThrows(InvalidUsernameException.class, ()-> { shop.createUser(null,"pass","Administrator"); } );
			assertThrows(InvalidUsernameException.class, ()-> { shop.createUser("","pass","Administrator"); } );
			//InvalidPasswordException
			assertThrows(InvalidPasswordException.class, ()-> { shop.createUser("Paolo",null,"Administrator"); } );
			assertThrows(InvalidPasswordException.class, ()-> { shop.createUser("Paolo","","Administrator"); } );
			//InvalidRoleException
			assertThrows(InvalidRoleException.class, ()-> { shop.createUser("Paolo","pass",null); } );
			assertThrows(InvalidRoleException.class, ()-> { shop.createUser("Paolo","pass",""); } );
			assertThrows(InvalidRoleException.class, ()-> { shop.createUser("Paolo","pass","Employee"); } );
		}

		
// User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException 
	
		
		// OK
		@Test
		public void testCase4() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			assertTrue(shop.login("Giuseppe","pass")!=null);
		}
		
		// wrong password 
		@Test
		public void testCase5() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			//shop.createUser("Giuseppe","pass","Administrator");
			assertTrue(shop.login("Giuseppe","password")==null);
		}		
		
		// Exception 
		@Test
		public void testCase6() {
			//InvalidUsernameException 
			assertThrows(InvalidUsernameException.class, ()-> { shop.login(null,"pass"); } );
			assertThrows(InvalidUsernameException.class, ()-> { shop.login("","pass"); } );
			//InvalidPasswordException
			assertThrows(InvalidPasswordException.class, ()-> { shop.login("Giuseppe",null); } );
			assertThrows(InvalidPasswordException.class, ()-> { shop.login("Giuseppe",""); } );
		
		}				
		
		
//boolean logout()
		
		// OK
		@Test
		public void testCase7() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			shop.login("Maria","pass");
			assertTrue(shop.logout());
		}
		
		// OK
		@Test
		public void testCase8() {
			shop.logout();
			assertFalse(shop.logout());
		}
		

		
// boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException 
		
		// OK
		@Test
		public void testCase9() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
			shop.login("Giuseppe","pass");
			int id = shop.createUser("Federico","pass","Cashier");
			assertTrue(shop.deleteUser(id));
			shop.logout();
		}
		
		//Exception
		@Test
		public void testCase10() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			// UnauthorizedException 
			shop.login("Maria","pass");
			assertThrows(UnauthorizedException.class, ()-> { shop.deleteUser(2); } );
			shop.login("Giuseppe","pass");
			//InvalidUserIdException
			assertThrows(InvalidUserIdException.class, ()-> { shop.deleteUser(null); } );
			assertThrows(InvalidUserIdException.class, ()-> { shop.deleteUser(0); } );
			shop.logout();
		}
		
		// User not found
		@Test
		public void testCase11() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
			shop.login("Giuseppe","pass");
			int id = shop.createUser("Federico","pass","Cashier");
			shop.deleteUser(id);
			assertFalse(shop.deleteUser(id));
			shop.logout();
		}	
		
			
		
		
// List<User> getAllUsers() throws UnauthorizedException 
		
		//UnauthorizedException
		@Test
		public void testCase12() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			shop.logout();
			assertThrows(UnauthorizedException.class, ()-> { shop.getAllUsers(); } );
			
		}
		//	 Scenario 2-4
		@Test
		public void testCase13() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidUserIdException {
			shop.login("Giuseppe","pass");
			int id1 = shop.createUser("Giovanni","pass","Cashier");
			int id2 = shop.createUser("Federico","pass","ShopManager");
			assertTrue(shop.getAllUsers()!=null);
			shop.deleteUser(id1);
			shop.deleteUser(id2);
			shop.logout();
		}
				
		// User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException;
		
		// Exception
		@Test
		public void testCase14() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			shop.login("Maria","pass");
			// UnauthorizedException
			assertThrows(UnauthorizedException.class, ()-> { shop.getUser(10); } );
			//InvalidUserIdException
			shop.login("Giuseppe","pass");
			assertThrows(InvalidUserIdException.class, ()-> { shop.getUser(null); } );
			assertThrows(InvalidUserIdException.class, ()-> { shop.getUser(0); } );
			shop.logout();
		}
	
		// User not found
		@Test
		public void testCase15() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
			shop.login("Giuseppe","pass");
			int id  = shop.createUser("Federico","pass","Cashier");
			shop.deleteUser(id);
			assertTrue(shop.getUser(id)==null);	
			shop.logout();
		}
		
		// OK 
		@Test
		public void testCase16() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
			shop.login("Giuseppe","pass");
			int id  = shop.createUser("Federico","pass","Cashier");
			assertTrue(shop.getUser(id)!=null);
			shop.deleteUser(id);
			shop.logout();
		}
		
		
//  boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException;

		// Exception
		@Test
		public void testCase17() throws InvalidUsernameException, InvalidPasswordException {
			shop.logout();
			// UnauthorizedException
			assertThrows(UnauthorizedException.class, ()-> { shop.updateUserRights(10,"Administrator"); } );
			shop.login("Giuseppe","pass");
			//InvalidUserIdException
			assertThrows(InvalidUserIdException.class, ()-> { shop.updateUserRights(null,"Administrator"); } );
			assertThrows(InvalidUserIdException.class, ()-> { shop.updateUserRights(0,"Administrator"); } );
			//InvalidRoleException
			assertThrows(InvalidRoleException.class, ()-> { shop.updateUserRights(10,null); } );
			assertThrows(InvalidRoleException.class, ()-> { shop.updateUserRights(10,""); } );
			assertThrows(InvalidRoleException.class, ()-> { shop.updateUserRights(10,"Employee"); } );
		}
		
		
		//OK Scenario 2-3
		@Test
		public void testCase18() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
			shop.login("Giuseppe","pass");
			int id = shop.createUser("Luca","pass","Cashier");
			assertTrue(shop.getUser(id)!=null);
			assertTrue(shop.updateUserRights(id,"Administrator"));
			assertTrue(shop.getUser(id).getRole().equals("Administrator"));
			shop.deleteUser(id);
			shop.logout();

		}
		
		//OK User not found
		@Test
		public void testCase19() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
			shop.login("Giuseppe","pass");
			int id = shop.createUser("Luca","pass","Cashier");
			shop.deleteUser(id);
			assertFalse(shop.updateUserRights(id,"Administrator"));
			shop.logout();

		}				
		

	}
