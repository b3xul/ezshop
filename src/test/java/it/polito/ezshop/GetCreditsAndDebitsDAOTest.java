package it.polito.ezshop;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.Implementations.EZShopDAO;


public class GetCreditsAndDebitsDAOTest {
	
static EZShopDAO ezShop;
	
	@BeforeClass
	public static void init() {
		ezShop = new it.polito.ezshop.data.Implementations.EZShopDAO();
	}
	
	@Before
	public void initDb() {
		ezShop.reset();
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	@Test
	public void testCase1() {
		//This method asserts that the list of all the balance operations can be retrieved correctly 
		ezShop.recordBalanceUpdate(20);
		ezShop.recordBalanceUpdate(30);
		ezShop.recordBalanceUpdate(50);
		assertTrue((ezShop.getCreditsAndDebits(LocalDate.now(), LocalDate.now().plusDays(1))).size() > 0);
	}
	
	@Test
	public void testCase2() {
		//This method asserts that the list of all the balance operations can be retrieved correctly even though the from and to date are inverted
		ezShop.recordBalanceUpdate(20);
		ezShop.recordBalanceUpdate(30);
		ezShop.recordBalanceUpdate(50);
		assertTrue((ezShop.getCreditsAndDebits(LocalDate.now().plusDays(1), LocalDate.now())).size() > 0);
	}
	
	@Test
	public void testCase3() {
		//This method asserts that the list of all the balance operations can be retrieved correctly even though there is a null date
		ezShop.recordBalanceUpdate(20);
		ezShop.recordBalanceUpdate(30);
		ezShop.recordBalanceUpdate(50);
		assertTrue((ezShop.getCreditsAndDebits(LocalDate.now(), null)).size() > 0);
	}
	
	
}
