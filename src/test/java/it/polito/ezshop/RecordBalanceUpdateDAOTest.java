package it.polito.ezshop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.Implementations.EZShopDAO;

public class RecordBalanceUpdateDAOTest {
	
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
		//This method asserts that the balance operation can't be added to the system due to negative balance
		assertFalse(ezShop.recordBalanceUpdate(-200));
	}
	
	@Test
	public void testCase2() {
		//This method asserts that the balance operation is added correctly to the system
		assertTrue(ezShop.recordBalanceUpdate(100));
	}
}
