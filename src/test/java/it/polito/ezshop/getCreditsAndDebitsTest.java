package it.polito.ezshop;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class getCreditsAndDebitsTest {

	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	
	@Test
	public void testCase1() throws UnauthorizedException {
		
		ezShop.recordBalanceUpdate(20);
		ezShop.recordBalanceUpdate(30);
		ezShop.recordBalanceUpdate(50);
		assertTrue((ezShop.getCreditsAndDebits(LocalDate.now(), LocalDate.now().plusDays(1))).size() > 0);
	}
	
	//inverting from and to dates
	@Test
	public void testCase2() throws UnauthorizedException {

		ezShop.recordBalanceUpdate(20);
		ezShop.recordBalanceUpdate(30);
		ezShop.recordBalanceUpdate(50);
		assertTrue((ezShop.getCreditsAndDebits(LocalDate.now().plusDays(1), LocalDate.now())).size() > 0);
	} 
	
	//inserting null date
	@Test
	public void testCase3() throws UnauthorizedException {

		ezShop.recordBalanceUpdate(20);
		ezShop.recordBalanceUpdate(30);
		ezShop.recordBalanceUpdate(50);
		assertTrue((ezShop.getCreditsAndDebits(LocalDate.now(), null)).size() > 0);
	}
}
