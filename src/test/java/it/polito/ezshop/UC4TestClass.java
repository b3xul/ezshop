package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
public class UC4TestClass {

	static EZShop shop = new EZShop();	
	static Integer idCustomer = null;
	
	@BeforeClass
	public static void beforeOp() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {		
		shop.createUser("Giuseppe","pass","Administrator");
		shop.login("Giuseppe","pass");	
	}
	
	
	@AfterClass
	public static void AfterClassOp() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		shop.reset();
	}
	
	@After
	public void AfterOp() throws InvalidCustomerIdException, UnauthorizedException {
		shop.deleteCustomer(idCustomer);
	}
	
	
	//Scenario 4-1
	@Test
	public void testCaseScenario4_1() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException {
		idCustomer = shop.defineCustomer("Andrea");
		assertTrue(idCustomer>0);
	}	
	
	//Scenario 4-2
	@Test
	public void testCaseScenario4_2() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
		idCustomer = shop.defineCustomer("Andrea");
		String customerCard = shop.createCard();
		assertTrue(customerCard != null && !customerCard.isEmpty());
		assertTrue(shop.attachCardToCustomer(customerCard,idCustomer));
	}
	
	// Scenario 4-3
	@Test
	public void testCaseScenario4_3() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
		idCustomer = shop.defineCustomer("Andrea");
		String customerCard = shop.createCard();
		shop.attachCardToCustomer(customerCard,idCustomer);	
		Customer customer = shop.getCustomer(idCustomer);
		assertTrue(customer!= null);
		assertTrue(shop.modifyCustomer(idCustomer,customer.getCustomerName(),""));
		// check if the card is detached
		customer = shop.getCustomer(idCustomer);
		assertTrue(customer.getCustomerCard() == null);
	}
	// Scenario 4-4
	@Test
	public void testCaseScenario4_4() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
		String newName = "Fabio";
		idCustomer = shop.defineCustomer("Andrea");
		String customerCard = shop.createCard();
		shop.attachCardToCustomer(customerCard,idCustomer);	
		Customer customer = shop.getCustomer(idCustomer);
		assertTrue(customer!= null);
		assertTrue(shop.modifyCustomer(idCustomer,newName,null));
		customer = shop.getCustomer(idCustomer);
		assertTrue(customer.getCustomerName().equals(newName));
	}
	
	// Scenario 4-5
		@Test
		public void testCaseScenario4_5() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
			idCustomer = shop.defineCustomer("Andrea");
			assertTrue(shop.deleteCustomer(idCustomer));
			assertTrue(shop.getCustomer(idCustomer) == null);
		}
	
}
