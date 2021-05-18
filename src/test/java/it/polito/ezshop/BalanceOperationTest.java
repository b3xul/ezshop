package it.polito.ezshop;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.Implementations.BalanceOperationImpl;


public class BalanceOperationTest {
	
	@Test
	public void testCase1() {
		
		BalanceOperation bo = new BalanceOperationImpl(1, LocalDate.now(), 5, "CREDIT");
		
		assertTrue(bo.getBalanceId() == 1);
		assertTrue(bo.getDate().equals(LocalDate.now()));
		assertTrue(bo.getMoney() == 5);
		assertTrue(bo.getType().equals("CREDIT"));
		
		bo.setBalanceId(5);
		bo.setDate(LocalDate.now().minusDays(1));
		bo.setMoney(2.75);
		bo.setType("DEBIT");
		
		assertTrue(bo.getBalanceId() == 5);
		assertTrue(bo.getDate().equals(LocalDate.now().minusDays(1)));
		assertTrue(bo.getMoney() == 2.75);
		assertTrue(bo.getType().equals("DEBIT"));
	}
}
