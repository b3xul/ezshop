package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.Implementations.EZShopDAO;
import it.polito.ezshop.data.Implementations.SaleTransactionImpl;
import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class DAOSaleTransactionClassTest {

	static EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();;
	static EZShopDAO DAO = new EZShopDAO();
	Integer transactionId;
	private SaleTransactionImpl openSaleTransaction = new SaleTransactionImpl(-1);
	String barcode = "12637482635892";
	String barcode2 = "6253478956438";

	@Test
	public void testCase1() {

		transactionId = DAO.startSaleTransaction();

		assertEquals(Integer.valueOf(1), transactionId);

	}

	@Test
	public void testCase2() {

		assertTrue(DAO.endSaleTransaction(openSaleTransaction));

	}

	// @Test
	// public void testCase3() {
	//
	// assertTrue(DAO.addProductsFromSaleTransaction(openSaleTransaction));
	//
	// }

	@Test
	public void testCase4() throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException {

		ezShop.login("admin", "admin");
		transactionId = ezShop.startSaleTransaction();
		System.out.println(transactionId);
		ezShop.endSaleTransaction(transactionId);
		assertTrue(ezShop.receiveCreditCardPayment(transactionId, "4485370086510891"));
		assertTrue(DAO.getSaleTransaction(transactionId) != null);
		assertTrue(DAO.getSaleTransaction(6) == null);

	}

	@BeforeClass
	static public void BeforeClass() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

		DAO.reset();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");

	}

	@AfterClass
	static public void AfterClass() {

		DAO.reset();
		ezShop.reset();

	}

}
