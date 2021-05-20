package it.polito.ezshop;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.data.Implementations.BalanceOperationImpl;

public class BalanceOperationClassTest {

	BalanceOperationImpl bo = new BalanceOperationImpl();

	@Test
	public void testCase1() {

		BalanceOperationImpl bo = new BalanceOperationImpl(1, LocalDate.now(), 5, "CREDIT");
		assertTrue(bo.getBalanceId() == 1);
		assertTrue(bo.getDate().equals(LocalDate.now()));
		assertTrue(bo.getMoney() == 5);
		assertTrue(bo.getType().equals("CREDIT"));

	}

	@Test
	public void testCase2() {

		bo.setBalanceId(5);
		assertTrue(bo.getBalanceId() == 5);

	}

	@Test
	public void testCase3() {

		bo.setDate(LocalDate.now().minusDays(1));
		assertTrue(bo.getDate().equals(LocalDate.now().minusDays(1)));

	}

	@Test
	public void testCase4() {

		bo.setMoney(2.75);
		assertTrue(bo.getMoney() == 2.75);

	}

	@Test
	public void testCase5() {

		bo.setType("DEBIT");
		assertTrue(bo.getType().equals("DEBIT"));

	}

	@Test
	public void testCase6() {

		bo.toString();

	}

}
