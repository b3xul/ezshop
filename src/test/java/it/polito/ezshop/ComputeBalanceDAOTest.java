package it.polito.ezshop;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.Implementations.EZShopDAO;

public class ComputeBalanceDAOTest {
	
	static EZShopDAO ezShop;
	
	@BeforeClass
	public static void init() {
		ezShop = new it.polito.ezshop.data.Implementations.EZShopDAO();
	}
	
	@Before
	public void initDb() {
		ezShop.reset();
		ezShop.login("admin", "admin");
		ezShop.recordBalanceUpdate(10);
		ezShop.recordBalanceUpdate(70);
		ezShop.recordBalanceUpdate(-80);
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	@Test
	public void testCase1() {
		
		assertTrue(ezShop.computeBalance() == 0);
	}
}
