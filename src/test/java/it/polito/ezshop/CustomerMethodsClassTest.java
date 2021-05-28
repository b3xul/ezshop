package it.polito.ezshop;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class CustomerMethodsClassTest {

			
	static EZShop shop = new EZShop();	
	static Integer idAdmin = null;
	static Integer idCashier = null;
	
	@BeforeClass
	public static void beforeOp() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		shop.reset();
		idAdmin = shop.createUser("Giuseppe","pass","Administrator");
		idCashier = shop.createUser("Francesco","pass","Cashier");
	}
	
	
	@AfterClass
	public static void AfterClassOp() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		shop.reset();
	}
		
//Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException;
		
		//UnauthorizedException //InvalidCustomerNameException
		@Test
		public void testCase1() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			//UnauthorizedException
			shop.logout();	
			assertThrows(UnauthorizedException.class, ()-> { shop.defineCustomer("Giada");});
			//InvalidCustomerNameException
			shop.login("Francesco","pass");
			assertThrows(InvalidCustomerNameException.class, ()-> { shop.defineCustomer("");});
			assertThrows(InvalidCustomerNameException.class, ()-> { shop.defineCustomer(null);});
			shop.logout();
		}
		
		// Scenario 4-1
		@Test
		public void testCase2() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
			shop.login("Francesco","pass");
			int customerId = shop.defineCustomer("Andrea");
			assertTrue(customerId>0);
			assertTrue(shop.deleteCustomer(customerId));
			shop.logout();
		}
		
//boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException;
		
		//Exception
		@Test	
		public void testCase3() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			//UnauthorizedException
			shop.logout();	
			assertThrows(UnauthorizedException.class, ()-> { shop.modifyCustomer(2,"newCustomerName","newCustomerCard");});
			//InvalidCustomerNameException
			shop.login("Francesco","pass");
			assertThrows(InvalidCustomerNameException.class, ()-> { shop.modifyCustomer(2,"","newCustomerCard");});
			assertThrows(InvalidCustomerNameException.class, ()-> { shop.modifyCustomer(2,null,"newCustomerCard");});
			//InvalidCustomerIdException
			assertThrows(InvalidCustomerIdException.class, ()-> { shop.modifyCustomer(0,"newCustomerName","newCustomerCard");});
			assertThrows(InvalidCustomerIdException.class, ()-> { shop.modifyCustomer(null,"newCustomerName","newCustomerCard");});
			//InvalidCustomerCardException
			assertThrows(InvalidCustomerCardException.class, ()-> { shop.modifyCustomer(2,"newCustomerName","12352");});
			shop.logout();
		}
		
		
		// Attach a card to the customer
		@Test
		public void testCase4() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
			shop.login("Francesco","pass");
			String customerName = "Carla";
			int customerId = shop.defineCustomer(customerName);
			//create a card 
			String customerCard = shop.createCard();
			assertTrue(shop.modifyCustomer(customerId,customerName,customerCard));
			Customer customer = shop.getCustomer(customerId);
			assertTrue(customer.getCustomerCard().equals(customerCard));
			shop.deleteCustomer(customerId);
			shop.logout();	
		}
		
		// Card value doesn't change
		@Test
		public void testCase5() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
			shop.login("Francesco","pass");
			String customerName = "Carla";
			int customerId = shop.defineCustomer(customerName);
			assertTrue(shop.modifyCustomer(customerId,customerName,null));
			Customer customer = shop.getCustomer(customerId);
			assertTrue(customer.getCustomerName().equals(customerName));
			shop.deleteCustomer(customerId);
			shop.logout();	
		}
		
		// Detach a card to the customer
		@Test
		public void testCase6() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
			shop.login("Francesco","pass");
			String customerName = "Carla";
			int customerId = shop.defineCustomer(customerName);
			//create a card 
			String customerCard = shop.createCard();
			shop.modifyCustomer(customerId,customerName,customerCard);
			assertTrue(shop.modifyCustomer(customerId,customerName,""));
			Customer customer = shop.getCustomer(customerId);
			assertTrue(customer.getCustomerCard()==null);
			shop.deleteCustomer(customerId);
			shop.logout();	
		}
		
		// Customer not found
		@Test
		public void testCase7() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
			shop.login("Francesco","pass");
			String customerName = "Carla";
			int customerId = shop.defineCustomer(customerName);
			shop.deleteCustomer(customerId);
			assertFalse(shop.modifyCustomer(customerId,customerName,null));
			shop.logout();	
		}
		
//boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException;
		
		// Exception
		@Test	
		public void testCase8() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			//UnauthorizedException
			shop.logout();	
			assertThrows(UnauthorizedException.class, ()-> { shop.deleteCustomer(2);});
			shop.login("Francesco","pass");			
			//InvalidCustomerIdException
			assertThrows(InvalidCustomerIdException.class, ()-> { shop.deleteCustomer(0);});
			assertThrows(InvalidCustomerIdException.class, ()-> {shop.deleteCustomer(null);});
			shop.logout();
		}
		
		// Scenario 4-5
		@Test
		public void testCase9() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
			shop.login("Francesco","pass");
			int customerId = shop.defineCustomer("Andrea");
			assertTrue(shop.deleteCustomer(customerId));
			assertTrue(shop.getCustomer(customerId) == null);
			shop.logout();
		}
		
		@Test
		public void testCase10() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
			shop.login("Francesco","pass");
			int customerId = shop.defineCustomer("Andrea");
			assertTrue(shop.deleteCustomer(customerId));
			assertFalse(shop.deleteCustomer(customerId));
			shop.logout();
		}
		
		
//Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException;
		
		// Exception
		@Test
		public void testCase11() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			//UnauthorizedException
			shop.logout();	
			assertThrows(UnauthorizedException.class, ()-> { shop.getCustomer(2);});
			shop.login("Francesco","pass");			
			//InvalidCustomerIdException
			assertThrows(InvalidCustomerIdException.class, ()-> {shop.getCustomer(0);});
			assertThrows(InvalidCustomerIdException.class, ()-> {shop.getCustomer(null);});
			shop.logout();
		}
		
		
// List<Customer> getAllCustomers() throws UnauthorizedException;
		
		// Exception
		@Test
		public void testCase12() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			//UnauthorizedException
			shop.logout();	
			assertThrows(UnauthorizedException.class, ()-> { shop.getAllCustomers();});
		}	
		
		// OK
		@Test
		public void testCase13() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
			shop.login("Francesco","pass");
			int id1 = shop.defineCustomer("Andrea");
			int id2 = shop.defineCustomer("Carla");
			String customerCard = shop.createCard();
			shop.modifyCustomer(id2,"Carla",customerCard);
			assertTrue(shop.getAllCustomers()!=null);
			shop.deleteCustomer(id1);
			shop.deleteCustomer(id2);
			shop.logout();
		}
	
		
// String createCard() throws UnauthorizedException;
		
		// Exception
		@Test
		public void testCase14() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			//UnauthorizedException
			shop.logout();	
			assertThrows(UnauthorizedException.class, ()-> { shop.createCard();});
		}
		
		// Create a second card
		@Test
		public void testCase15() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
			shop.login("Francesco","pass");
			String customerName = "Carla";
			int customerId = shop.defineCustomer(customerName);
			String customerCard = shop.createCard();
			assertTrue(shop.attachCardToCustomer(customerCard,customerId));
			customerCard = shop.createCard();
			assertTrue(customerCard.isEmpty() == false);
			assertTrue(shop.createCard().equals(customerCard));
			shop.deleteCustomer(customerId);
			shop.logout();	
		}

		
// boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException;
		
		// Exception

		@Test
		public void testCase16() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			//UnauthorizedException
			shop.logout();	
			assertThrows(UnauthorizedException.class, ()-> { shop.attachCardToCustomer("1234567891",15);});
			shop.login("Francesco","pass");	
			//InvalidCustomerIdException
			assertThrows(InvalidCustomerIdException.class, ()-> { shop.attachCardToCustomer("1231566774",0);});
			assertThrows(InvalidCustomerIdException.class, ()-> {shop.attachCardToCustomer("1231566774",null);});
			//InvalidCustomerIdException
			assertThrows(InvalidCustomerCardException.class, ()-> { shop.attachCardToCustomer("",5);});
			assertThrows(InvalidCustomerCardException.class, ()-> {shop.attachCardToCustomer(null,5);});
			assertThrows(InvalidCustomerCardException.class, ()-> { shop.attachCardToCustomer("1234aas",5);});
			shop.logout();
		}
		
		
// boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException;
		
		// Exception
		@Test
		public void testCase17() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			//UnauthorizedException
			shop.logout();	
			assertThrows(UnauthorizedException.class, ()-> { shop.modifyPointsOnCard("1234567891",15);});
			shop.login("Francesco","pass");	
			//InvalidCustomerCardException
			assertThrows(InvalidCustomerCardException.class, ()-> {shop.modifyPointsOnCard("",15);});
			assertThrows(InvalidCustomerCardException.class, ()-> {shop.modifyPointsOnCard(null,15);});
			assertThrows(InvalidCustomerCardException.class, ()-> {shop.modifyPointsOnCard("1234",15);});
			assertThrows(InvalidCustomerCardException.class, ()-> {shop.modifyPointsOnCard("12345re89q",15);});	
			shop.logout();
		}

		// OK
		@Test
		public void testCase18() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerNameException, InvalidCustomerIdException {
			shop.login("Francesco","pass");	
			String customerCard = shop.createCard();
			assertTrue(shop.modifyPointsOnCard(customerCard,15));
			int customerId = shop.defineCustomer("Sara");
			shop.attachCardToCustomer(customerCard,customerId);
			Customer c = shop.getCustomer(customerId);
			assertTrue(c.getPoints()==15);
			shop.deleteCustomer(customerId);
			shop.logout();
		}
		
		// Not enough point to do a subtraction
		@Test
		public void testCase19() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerNameException, InvalidCustomerIdException {
			shop.login("Francesco","pass");	
			String customerCard = shop.createCard();
			assertFalse(shop.modifyPointsOnCard(customerCard,-15));
			shop.logout();
		}
			
		// Attach card to customer, customer not found
		@Test
		public void testCase20() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
			shop.login("Francesco","pass");
			String customerName = "Carla";
			int customerId = shop.defineCustomer(customerName);
			shop.deleteCustomer(customerId);
			String customerCard = shop.createCard();
			assertFalse(shop.attachCardToCustomer(customerCard,customerId));
			shop.login("Francesco","pass");
		}
				
}
			
