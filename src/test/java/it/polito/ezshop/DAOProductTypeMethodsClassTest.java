package it.polito.ezshop;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import it.polito.ezshop.exceptions.*;

import org.junit.Test;

import it.polito.ezshop.data.Implementations.EZShopDAO;

public class DAOProductTypeMethodsClassTest {
	static EZShopDAO DAO = new EZShopDAO();
	@Test
	public void testCase1() {
		Integer productId = DAO.createProductType("new product", "12637482635892", 2.5, "note");
		assertTrue(productId > 0);
		DAO.deleteProductType(productId);
	}
	
	@Test
	public void testCase2() {
		Integer productId = DAO.createProductType("valid description","12637482635892",2.5,"note");
		boolean success = DAO.deleteProductType(productId);
		assertTrue(success);

	}
	
	@Test
	public void testCase3() {
		Integer productId1 = DAO.createProductType("valid description","12637482635892",2.5,"note");
		Integer productId2 = DAO.createProductType("valid description","6253478956438",2.5,"note");
		assertTrue(DAO.getAllProductTypes().size() >= 0);
		DAO.deleteProductType(productId1);
		DAO.deleteProductType(productId2);

	}
	
	@Test
	public void testCase4() {
		Integer productId = DAO.createProductType("new product","12637482635892",2.5,"note");
		assertNotNull(DAO.getProductTypeByBarCode("12637482635892"));
		DAO.deleteProductType(productId);


	}
	
	@Test
	public void testCase5() {
		Integer productId1 = DAO.createProductType("valid description","12637482635892",2.5,"note");
		Integer productId2 = DAO.createProductType("valid description","6253478956438",2.5,"note");
		assertTrue(DAO.getProductTypesByDescription("description").size() > 0);
		DAO.deleteProductType(productId1);
		DAO.deleteProductType(productId2);

	}
	
	@Test
	public void testCase6() {
		Integer productId = DAO.createProductType("valid description","12637482635892",2.5,"note");
		assertTrue(DAO.updatePosition(productId, "3-aisle-2"));
		DAO.deleteProductType(productId);


	}
	
	@Test
	public void testCase7() {
		Integer productId = DAO.createProductType("valid description","12637482635892",2.5,"note");
		boolean success = DAO.updateProduct(productId, "new description", "6253478956438", 3.5, "new note");
		assertTrue(success);
		DAO.deleteProductType(productId);


	}
	
	@Test
	public void testCase8() {
		Integer productId = DAO.createProductType("valid description","12637482635892",2.5,"note");
		DAO.updatePosition(productId, "1-aisle-1");
		assertTrue(DAO.updateQuantity(productId, 100));
		assertTrue(DAO.updateQuantity(productId, -10));
		DAO.deleteProductType(productId);

	}

	@BeforeClass
	  static public void BeforeClass() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		
		DAO.reset();
		
	}
	
	@AfterClass
	  static public void AfterClass() {

	    DAO.reset();

	}

}
