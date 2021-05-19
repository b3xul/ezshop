package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.Implementations.ProductTypeImpl;

public class ProductTypeClassTest {
	
	ProductType product = new ProductTypeImpl();
	
	@Test
	public void testCase1() {
		product = new ProductTypeImpl("note","description","12637482635892",2.5);
		assertTrue((product.getProductDescription() == "description") && 
				(product.getBarCode() == "12637482635892") &&
				(product.getPricePerUnit() == 2.5) &&
				(product.getNote() == "note"));
		
	}
		
	@Test
	public void testCase2() {
		
		product.setId(1);
		assertTrue(product.getId() == 1);
		
	}
	
	@Test
	public void testCase3() {
		
			product.setProductDescription("new desc");
			assertTrue(product.getProductDescription() == "new desc");
			
	}
	
	@Test
	public void testCase4() {
	
		product.setBarCode("6253478956438");
		assertTrue(product.getBarCode() == "6253478956438");
		
	}
	
	@Test
	public void testCase5() {
	
		product.setPricePerUnit(25.0);
		assertTrue(product.getPricePerUnit() == 25.0);
		
	}
	
	@Test
	public void testCase6() {
		
			product.setNote("new note");
			assertTrue(product.getNote() == "new note");
			
	}

	@Test
	public void testCase7() {
		
			product.setLocation("3-aisle-3");
			assertTrue(product.getLocation().equals(product.getLocation()));
			
	}
	
	@Test
	public void testCase8() {
		
			product.setQuantity(4);
			assertTrue(product.getQuantity() == 4);
			
	}
	
	@Test
	public void testCase9() {
		
			ProductTypeImpl product2 = new ProductTypeImpl();
			product2.setDiscountRate(0.2);
			assertTrue(product2.getDiscountRate() == 0.2);
			
	}
	
	@Test
	public void testCase10() {
		
			ProductTypeImpl product2 = new ProductTypeImpl();
			product2.print();
			product2.setId(1);
			product2.print();
			
	}
}
