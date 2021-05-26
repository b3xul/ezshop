package it.polito.ezshop;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class UC9TestClass {

	EZShopInterface ezShop;
	int orderId;
	int productId;

	@Before
	public void init()
			throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidRoleException {

		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		ezShop.recordBalanceUpdate(20);
		ezShop.recordBalanceUpdate(30);
		ezShop.recordBalanceUpdate(-10);

	}

	@After
	public void teardown() {

		ezShop.reset();

	}

	@Test
	public void testCaseScenario9_1() throws UnauthorizedException {

		assertTrue((ezShop.getCreditsAndDebits(LocalDate.now(), LocalDate.now().plusDays(1))).size() > 0);

	}

}
