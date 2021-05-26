package it.polito.ezshop;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.Implementations.EZShopDAO;

public class PayOrderDAOTest {
	
	static EZShopDAO ezShop;
	int orderId;
	
	@BeforeClass
	public static void init() {
		ezShop = new it.polito.ezshop.data.Implementations.EZShopDAO();
	}
	
	@Before
	public void initDb() {
		ezShop.reset();
		ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
		orderId = ezShop.issueOrder("12637482635892", 5, 2.5);
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	@Test
	public void testCase1() {
		ezShop.recordBalanceUpdate(1000);   			//recording a positive balance before paying an order, so it can be placed
		assertTrue(ezShop.payOrder(orderId) == true);
	}
	
	@Test
	public void testCase2() {
		//This test asserts there is no issued order with this id in the system
		assertTrue(ezShop.payOrder(10) == false);
	}
	
	@Test
	public void testCase3() {
		//This test asserts that an issued order can't be payed since the balance of the system will turn negative
		assertTrue(ezShop.payOrder(orderId) == false);
	}
}
