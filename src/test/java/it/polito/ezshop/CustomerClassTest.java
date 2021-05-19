package it.polito.ezshop;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.Implementations.CustomerImpl;

public class CustomerClassTest {

	Customer customer = new CustomerImpl();

	@Test
	public void testCase1() {

		customer = new CustomerImpl(2, "Giulia", "1124432525", 15);
		assertTrue((customer.getId() == 2) && (customer.getCustomerName() == "Giulia")
				&& (customer.getCustomerCard() == "1124432525") && (customer.getPoints() == 15));

		customer.toString();

	}

	@Test
	public void testCase2() {

		customer.setId(1);
		assertTrue(customer.getId() == 1);

	}

	@Test
	public void testCase3() {

		customer.setCustomerName("Chiara");
		assertTrue(customer.getCustomerName() == "Chiara");

	}

	@Test
	public void testCase4() {

		customer.setCustomerCard("1234567890");
		assertTrue(customer.getCustomerCard() == "1234567890");

	}

	@Test
	public void testCase5() {

		customer.setPoints(10);
		assertTrue(customer.getPoints() == 10);

	}

}
