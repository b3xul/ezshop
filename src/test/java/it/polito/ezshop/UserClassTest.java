package it.polito.ezshop;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.data.User;
import it.polito.ezshop.data.Implementations.UserImpl;

public class UserClassTest {

	User user = new UserImpl();

	@Test
	public void testCase1() {

		user = new UserImpl(10, "Marco", "xxx", "Adminitrator");
		assertTrue((user.getId() == 10) && (user.getUsername() == "Marco") && (user.getPassword() == "xxx")
				&& (user.getRole() == "Adminitrator"));

		user.toString();

	}

	@Test
	public void testCase2() {

		user.setId(25);
		assertTrue(user.getId() == 25);

	}

	@Test
	public void testCase3() {

		user.setUsername("Pippo");
		assertTrue(user.getUsername() == "Pippo");

	}

	@Test
	public void testCase4() {

		user.setPassword("pass");
		assertTrue(user.getPassword() == "pass");

	}

	@Test
	public void testCase5() {

		user.setRole("Cashier");
		assertTrue(user.getRole() == "Cashier");

	}

}
