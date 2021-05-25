package it.polito.ezshop;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class getCreditsAndDebitsTest {

	EZShopInterface ezShop;
	
	@Before
	public void init() throws InvalidUsernameException, InvalidPasswordException {
		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.login("admin", "admin");
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	@Test
	public void testCase1() throws UnauthorizedException {
		//This method asserts that the list of all the balance operations can be retrieved correctly 
		ezShop.recordBalanceUpdate(20);
		ezShop.recordBalanceUpdate(30);
		ezShop.recordBalanceUpdate(50);
		assertTrue((ezShop.getCreditsAndDebits(LocalDate.now(), LocalDate.now().plusDays(1))).size() > 0);
	}
	
	@Test
	public void testCase2() throws UnauthorizedException {
		//This method asserts that the list of all the balance operations can be retrieved correctly even though the from and to date are inverted
		ezShop.recordBalanceUpdate(20);
		ezShop.recordBalanceUpdate(30);
		ezShop.recordBalanceUpdate(50);
		assertTrue((ezShop.getCreditsAndDebits(LocalDate.now().plusDays(1), LocalDate.now())).size() > 0);
	} 
	
	//inserting null date
	@Test
	public void testCase3() throws UnauthorizedException {

		ezShop.recordBalanceUpdate(20);
		ezShop.recordBalanceUpdate(30);
		ezShop.recordBalanceUpdate(50);
		assertTrue((ezShop.getCreditsAndDebits(LocalDate.now(), null)).size() > 0);
	}
	
	@Test
	public void testCase4() {
		//This test verifies that the list of all the balance operations can't be retrieved if the user does not have the rights
		ezShop.logout();
		assertThrows(UnauthorizedException.class, () -> {ezShop.getCreditsAndDebits(LocalDate.now(), LocalDate.now().plusDays(1));});
	}
}
