package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class RecordOrderArrivalTest {
	
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	
	@Test
	public void testCase1() throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
		
		assertTrue(ezShop.recordOrderArrival(1) == true);
		assertTrue(ezShop.recordOrderArrival(7) == true);
		assertTrue(ezShop.recordOrderArrival(20) == false);
		assertThrows(InvalidOrderIdException.class, () -> {ezShop.recordOrderArrival(0);});
	}
}
