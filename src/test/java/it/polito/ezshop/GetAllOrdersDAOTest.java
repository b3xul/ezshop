package it.polito.ezshop;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.Implementations.EZShopDAO;

public class GetAllOrdersDAOTest {
	
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
		ezShop.createProductType("latte", "6253478956438", 1.75, "medio");
		ezShop.recordBalanceUpdate(1000); 
		orderId = ezShop.issueOrder("12637482635892", 5, 0.1);
		ezShop.issueOrder("12637482635892", 10, 0.15);
		ezShop.payOrderFor("6253478956438", 12, 0.20);
		ezShop.payOrder(orderId);
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	
	@Test
	public void testCase1() {
		//This test verifies that the list of orders is retrieved correctly
		assertTrue((ezShop.getAllOrders().size()) > 0);
	}
	
}
