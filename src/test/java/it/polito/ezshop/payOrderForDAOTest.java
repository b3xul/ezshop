package it.polito.ezshop;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.Implementations.EZShopDAO;

public class payOrderForDAOTest {
	
	static EZShopDAO ezShop;
	
	@BeforeClass
	public static void init() {
		ezShop = new it.polito.ezshop.data.Implementations.EZShopDAO();
	}
	
	@Before
	public void initDb() {
		ezShop.reset();
		ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	@Test
	public void testCase1() {
		ezShop.recordBalanceUpdate(1000);   							//recording a positive balance before paying an order, so it can ba placed
		assertTrue(ezShop.payOrderFor("12637482635892", 5, 5.10) > 0);
	}
	
	@Test
	public void testCase2() {
		//This test verifies there is no product with this barcode in the db
		assertTrue(ezShop.payOrderFor("6253478956438", 10, 10) < 0);
	}
	
	@Test
	public void testCase3() {
		//This test returns a negative id since the balance of the system will turn negative if the order is placed
		assertTrue(ezShop.payOrderFor("12637482635892", 10, 10) < 0);
	}
}
