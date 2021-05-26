package it.polito.ezshop;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.Implementations.EZShopDAO;
import it.polito.ezshop.exceptions.InvalidLocationException;

public class RecordOrderArrivalDAOTest {
	
	static EZShopDAO ezShop;
	int productId;
	int orderId;
	
	@BeforeClass
	public static void init() {
		ezShop = new it.polito.ezshop.data.Implementations.EZShopDAO();
	}
	
	@Before
	public void initDb() {
		ezShop.reset();
		productId = ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
		ezShop.recordBalanceUpdate(1000);   							//recording a positive balance before paying an order, so it can be placed
		orderId = ezShop.payOrderFor("12637482635892", 5, 5);
		
	}
	
	@After
	public void teardown() {
		ezShop.reset();
	}
	
	@Test
	public void testCase1() throws InvalidLocationException {
		ezShop.updatePosition(productId, "3-aisle-2");
		//record order arrival performed
		assertTrue(ezShop.recordOrderArrival(orderId) == true);
	}
	
	@Test
	public void testCase2() {
		assertThrows(InvalidLocationException.class, () -> {ezShop.recordOrderArrival(orderId);});
	}
	
	@Test
	public void testCase3() throws InvalidLocationException {
		//This test verifies that there is not a PAYED order with this id
		assertTrue(ezShop.recordOrderArrival(10) == false);
	}
}
