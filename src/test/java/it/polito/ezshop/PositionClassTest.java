package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.Implementations.PositionImpl;

public class PositionClassTest {
	
	@Test
	public void testCase1() {
		
		PositionImpl position = new PositionImpl("3-aisle-4");
		assertTrue((position.getAisleNumber() == 3) && 
				(position.getAlphabeticId().equals("aisle")) &&
				(position.getLevelNumber() == 4));
	}
	
	@Test
	public void testCase2() {
		
		PositionImpl position = new PositionImpl("corridor");
		assertTrue(position.getAisleNumber() == null && 
				position.getAlphabeticId().equals("") && 
				position.getLevelNumber() == null);

	}
	
	@Test
	public void testCase3() {
		
		PositionImpl position = new PositionImpl();
		position.setPosition("2-corridor-3");
		assertTrue((position.getAisleNumber() == 2) && 
				(position.getAlphabeticId().equals("corridor")) &&
				(position.getLevelNumber() == 3));
	}
	
	@Test
	public void testCase4() {
		
		PositionImpl position = new PositionImpl();
		position.setPosition("");
		assertTrue(position.getAisleNumber() == null && 
				position.getAlphabeticId() == null && 
				position.getLevelNumber() == null);

	}
	
}
