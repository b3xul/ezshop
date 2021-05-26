package it.polito.ezshop;

import static org.junit.Assert.*;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.Implementations.EZShopDAO;

public class DAOCustomerMethodsClassTest {

	static EZShopDAO DAO = new EZShopDAO();
	
	@BeforeClass
	  static public void BeforeClass(){	
		DAO.reset();
	}
	
	@AfterClass
	  static public void AfterClass() {
	    DAO.reset();
	}
	
	// DEFINE CUSTOMER
	@Test
	public void testCase1() {
		int customerId = DAO.defineCustomer("Andrea");
		assertTrue(customerId>0);
		DAO.deleteCustomer(customerId);

	}
	
	// MODIFY CUSTOMER + GET CUSTOMER
	// Attach a card to the customer 
	@Test
	public void testCase2() {
		String customerName = "Carla";
		int customerId = DAO.defineCustomer(customerName);
		String customerCard = DAO.createCard();
		assertTrue(DAO.modifyCustomer(customerId,customerName,customerCard));
		Customer customer = DAO.getCustomer(customerId);
		assertTrue(customer.getCustomerCard().equals(customerCard));
		DAO.deleteCustomer(customerId);
	}
	
	// Card value doesn't change
	@Test
	public void testCase3() {
		String customerName = "Carla";
		int customerId = DAO.defineCustomer(customerName);
		assertTrue(DAO.modifyCustomer(customerId,customerName,null));
		Customer customer = DAO.getCustomer(customerId);
		assertTrue(customer.getCustomerName().equals(customerName));
		DAO.deleteCustomer(customerId);

	}
	
	// Detach a card to the customer
	@Test
	public void testCase4() {
		String customerName = "Carla";
		int customerId = DAO.defineCustomer(customerName);
		//create a card 
		String customerCard = DAO.createCard();
		DAO.modifyCustomer(customerId,customerName,customerCard);
		assertTrue(DAO.modifyCustomer(customerId,customerName,""));
		Customer customer = DAO.getCustomer(customerId);
		assertTrue(customer.getCustomerCard()==null);
		DAO.deleteCustomer(customerId);
	}
	
	// Customer not found
	@Test
	public void testCase5() {
		String customerName = "Carla";
		int customerId = DAO.defineCustomer(customerName);
		DAO.deleteCustomer(customerId);
		assertFalse(DAO.modifyCustomer(customerId,customerName,null));
	}
	
	// DELETE CUSTOMER + GET CUSTOMER not found
	@Test
	public void testCase6() {
		int customerId = DAO.defineCustomer("Andrea");
		assertTrue(DAO.deleteCustomer(customerId));
		assertTrue(DAO.getCustomer(customerId) == null);
	}
	
	// Customer already deleted
	@Test
	public void testCase7() {
		int customerId = DAO.defineCustomer("Andrea");
		assertTrue(DAO.deleteCustomer(customerId));
		assertFalse(DAO.deleteCustomer(customerId));
	}				

	// GET CUSTOMERS
	@Test
	public void testCase8() {
		int id1 = DAO.defineCustomer("Andrea");
		int id2 = DAO.defineCustomer("Carla");
		String customerCard = DAO.createCard();
		DAO.modifyCustomer(id2,"Carla",customerCard);
		assertTrue(DAO.getAllCustomers()!=null);
		DAO.deleteCustomer(id1);
		DAO.deleteCustomer(id2);

	}
	
	// CREATE CARD + ATTACH CARD TO CUSTOMER
	@Test
	public void testCase9() {
		String customerName = "Carla";
		int customerId = DAO.defineCustomer(customerName);
		String customerCard = DAO.createCard();
		assertTrue(DAO.attachCardToCustomer(customerCard,customerId));
		// Create more than 1 card
		customerCard = DAO.createCard();
		assertTrue(customerCard.isEmpty() == false);
		assertTrue(DAO.createCard().equals(customerCard));
		DAO.deleteCustomer(customerId);
	}	
	
	// ATTACH CARD TO CUSTOMER, customer not found
	@Test
	public void testCase10() {
		String customerName = "Carla";
		int customerId = DAO.defineCustomer(customerName);
		DAO.deleteCustomer(customerId);
		String customerCard = DAO.createCard();
		assertFalse(DAO.attachCardToCustomer(customerCard,customerId));
	}
	
	//MODIFY POINTS ON CARD
	@Test
	public void testCase11()  {
		String customerCard = DAO.createCard();
		assertTrue(DAO.modifyPointsOnCard(customerCard,15));
		int customerId = DAO.defineCustomer("Sara");
		DAO.attachCardToCustomer(customerCard,customerId);
		Customer c = DAO.getCustomer(customerId);
		assertTrue(c.getPoints()==15);
		DAO.deleteCustomer(customerId);
	}
	
	// Not enough point to do a subtraction
	@Test
	public void testCase12() {
		String customerCard = DAO.createCard();
		assertFalse(DAO.modifyPointsOnCard(customerCard,-15));
	}	
							
									

}
