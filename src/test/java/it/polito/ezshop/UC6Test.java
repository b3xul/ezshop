package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.Implementations.ProductTypeImpl;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class UC6Test {

	static EZShopInterface ezShop;

	@BeforeClass
	public static void init() {

		ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		try {
			// ezShop.createUser("admin", "admin", "Administrator");
			ezShop.login("admin", "admin");

			// Product type X exists and has enough units to complete the sale
			Integer productId = ezShop.createProductType("biscotti", "12637482635892", 1.5, "piccoli");
			ezShop.updatePosition(productId, "2-aisle-2");
			ezShop.updateQuantity(productId, 25);

			Integer productId2 = ezShop.createProductType("insalata", "6253478956438", 1.99, "in busta");
			ezShop.updatePosition(productId2, "3-aisle-3");
			ezShop.updateQuantity(productId2, 30);

			ezShop.createUser("Casper", "casper101", "Cashier");
			ezShop.logout();

			// Cashier C exists and is logged in
			ezShop.login("Casper", "casper101");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:creditCards.sqlite");) {
			String updateCard = "UPDATE creditCards SET balance = 150 WHERE creditCardNumber = 4485370086510891";
			PreparedStatement pstmt = conn.prepareStatement(updateCard);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@AfterClass
	public static void teardown() {

		ezShop.reset();

	}

	@Test
	public void testCase1() {
		// Scenario 6-1 Sale of product type X completed (credit card)

		try {
			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			// ProductType pt = ezShop.getProductTypeByBarCode(barcode);
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}

			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			assertTrue(ezShop.addProductToSale(transactionId, barcode, 5));
			assertTrue(ezShop.addProductToSale(transactionId, barcode, 13));

			assertTrue(ezShop.receiveCreditCardPayment(transactionId, "4485370086510891"));

			assertTrue(ezShop.endSaleTransaction(transactionId));

			double price = ezShop.getSaleTransaction(transactionId).getPrice();

			// assertTrue(ezShop.recordBalanceUpdate(price));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase2() {
		// Scenario 6-2 Sale of product type X with product discount

		try {
			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			assertTrue(ezShop.addProductToSale(transactionId, barcode, 4)); // 6 euro

			assertTrue(ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5)); // 3 euro

			// assertTrue(ezShop.applyDiscountRateToSale(transactionId, 0.1));

			assertTrue(ezShop.receiveCreditCardPayment(transactionId, "4485370086510891"));

			assertTrue(ezShop.endSaleTransaction(transactionId));

			double price = ezShop.getSaleTransaction(transactionId).getPrice();

			// assertTrue(ezShop.recordBalanceUpdate(price)); I can't

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase3() {
		// Scenario 6-3 Sale of product type X with sale discount

		try {
			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			assertTrue(ezShop.addProductToSale(transactionId, barcode, 4)); // 6 euro

			// assertTrue(ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5)); // 3 euro

			assertTrue(ezShop.applyDiscountRateToSale(transactionId, 0.1));

			assertTrue(ezShop.receiveCreditCardPayment(transactionId, "4485370086510891"));

			assertTrue(ezShop.endSaleTransaction(transactionId));

			double price = ezShop.getSaleTransaction(transactionId).getPrice();

			// assertTrue(ezShop.recordBalanceUpdate(price)); I can't

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase4() {
		// Scenario 6-4 Sale of product type X with Loyalty Card update

		try {
			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			assertTrue(ezShop.addProductToSale(transactionId, barcode, 16)); // 24 euro

			// assertTrue(ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5)); // 3 euro

			// assertTrue(ezShop.applyDiscountRateToSale(transactionId, 0.1));

			assertTrue(ezShop.receiveCreditCardPayment(transactionId, "4485370086510891"));

			int points = ezShop.computePointsForSale(transactionId);
			assertEquals(2, points);

			assertTrue(ezShop.modifyPointsOnCard("1000000000", points));

			assertTrue(ezShop.endSaleTransaction(transactionId));

			double price = ezShop.getSaleTransaction(transactionId).getPrice();

			// assertTrue(ezShop.recordBalanceUpdate(price)); I can't

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase5() {
		// Scenario 6-5 Sale of product type X cancelled

		try {
			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			assertTrue(ezShop.addProductToSale(transactionId, barcode, 16)); // 24 euro

			assertTrue(ezShop.deleteProductFromSale(transactionId, barcode, 4)); // 18 euro

			// assertTrue(ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5)); // 3 euro

			// assertTrue(ezShop.applyDiscountRateToSale(transactionId, 0.1));

			assertTrue(ezShop.receiveCreditCardPayment(transactionId, "4485370086510891"));

			int points = ezShop.computePointsForSale(transactionId);
			assertEquals(1, points);

			assertTrue(ezShop.modifyPointsOnCard("1000000000", points));

			assertTrue(ezShop.endSaleTransaction(transactionId));

			double price = ezShop.getSaleTransaction(transactionId).getPrice();

			// assertTrue(ezShop.recordBalanceUpdate(price)); I can't

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase6() {
		// Scenario 6-6 Sale of product type X completed (Cash)

		try {
			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			assertTrue(ezShop.addProductToSale(transactionId, barcode, 16)); // 24 euro

			assertTrue(ezShop.deleteProductFromSale(transactionId, barcode, 4)); // 18 euro

			// assertTrue(ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5));

			// assertTrue(ezShop.applyDiscountRateToSale(transactionId, 0.1));

			assertEquals(2, ezShop.receiveCashPayment(transactionId, 20), 0.01); // 2 euro back

			int points = ezShop.computePointsForSale(transactionId);
			assertEquals(1, points);

			assertTrue(ezShop.modifyPointsOnCard("1000000000", points));

			assertTrue(ezShop.endSaleTransaction(transactionId));

			double price = ezShop.getSaleTransaction(transactionId).getPrice();

			// assertTrue(ezShop.recordBalanceUpdate(price)); I can't

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase7() {
		// Scenario 6-7 Delete of an open SaleTransaction

		try {
			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			assertTrue(ezShop.addProductToSale(transactionId, barcode, 16)); // 24 euro

			assertTrue(ezShop.deleteProductFromSale(transactionId, barcode, 4)); // 18 euro

			// assertTrue(ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5));

			// assertTrue(ezShop.applyDiscountRateToSale(transactionId, 0.1));

			assertTrue(ezShop.deleteSaleTransaction(transactionId));

			// assertEquals(2, ezShop.receiveCashPayment(transactionId, 20), 0.01); // 2 euro back
			//
			// int points = ezShop.computePointsForSale(transactionId);
			// assertEquals(1, points);
			//
			// assertTrue(ezShop.modifyPointsOnCard("1000000000", points));
			//
			// assertTrue(ezShop.endSaleTransaction(transactionId));
			//
			// double price = ezShop.getSaleTransaction(transactionId).getPrice();

			// assertTrue(ezShop.recordBalanceUpdate(price)); I can't

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase8() {
		// startSaleTransaction exceptions

		try {
			ezShop.logout();
			assertThrows(UnauthorizedException.class, () -> {
				ezShop.startSaleTransaction();
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase9() {
		// addProductToSale exceptions

		try {

			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			String barcode2 = "6253478956438";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			ezShop.logout();
			assertThrows(UnauthorizedException.class, () -> {
				ezShop.addProductToSale(transactionId, barcode, 16);
			});
			ezShop.login("Casper", "casper101");

			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.addProductToSale(null, barcode, 16);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.addProductToSale(0, barcode, 16);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.addProductToSale(-1, barcode, 16);
			});

			assertThrows(InvalidQuantityException.class, () -> {
				ezShop.addProductToSale(transactionId, barcode, 0);
			});
			assertThrows(InvalidQuantityException.class, () -> {
				ezShop.addProductToSale(transactionId, barcode, -1);
			});

			ezShop.addProductToSale(transactionId, barcode2, 16);
			assertFalse(ezShop.addProductToSale(transactionId, "22637482635892", 16)); // !productType.getBarCode().equals(productCode)
			assertTrue(ezShop.addProductToSale(transactionId, "12637482635892", 16)); // productType.getBarCode().equals(productCode),
																						// amount <=
																						// productType.getQuantity()

			assertFalse(ezShop.addProductToSale(transactionId, barcode, 26)); // amount > productType.getQuantity()

			assertFalse(ezShop.addProductToSale(2, barcode, 16)); // transactionId !=
																	// openSaleTransaction.getTicketNumber()

			ezShop.deleteSaleTransaction(transactionId);
			assertFalse(ezShop.addProductToSale(transactionId, barcode, 16)); // openSaleTransaction.getTicketNumber()
																				// == -1

			// assertTrue(ezShop.deleteProductFromSale(transactionId, barcode, 4)); // 18 euro

			// assertTrue(ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5));

			// assertTrue(ezShop.applyDiscountRateToSale(transactionId, 0.1));

			// assertTrue(ezShop.deleteSaleTransaction(transactionId));

			// assertEquals(2, ezShop.receiveCashPayment(transactionId, 20), 0.01); // 2 euro back
			//
			// int points = ezShop.computePointsForSale(transactionId);
			// assertEquals(1, points);
			//
			// assertTrue(ezShop.modifyPointsOnCard("1000000000", points));
			//
			// assertTrue(ezShop.endSaleTransaction(transactionId));
			//
			// double price = ezShop.getSaleTransaction(transactionId).getPrice();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase10() {
		// deleteProductFromSale exceptions

		try {

			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			ezShop.addProductToSale(transactionId, barcode, 16);

			ezShop.logout();
			assertThrows(UnauthorizedException.class, () -> {
				ezShop.deleteProductFromSale(transactionId, barcode, 16);
			});
			ezShop.login("Casper", "casper101");

			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.deleteProductFromSale(null, barcode, 16);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.deleteProductFromSale(0, barcode, 16);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.deleteProductFromSale(-1, barcode, 16);
			});

			assertThrows(InvalidQuantityException.class, () -> {
				ezShop.deleteProductFromSale(transactionId, barcode, 0);
			});
			assertThrows(InvalidQuantityException.class, () -> {
				ezShop.deleteProductFromSale(transactionId, barcode, -1);
			});

			assertFalse(ezShop.deleteProductFromSale(transactionId, "22637482635892", 16)); // !productType.getBarCode().equals(productCode)
			assertFalse(ezShop.deleteProductFromSale(transactionId, barcode, 20)); // amount > previousAmount

			assertTrue(ezShop.deleteProductFromSale(transactionId, barcode, 10)); // productType.getBarCode().equals(productCode),
																					// amount < previousAmount
			assertTrue(ezShop.deleteProductFromSale(transactionId, barcode, 6)); // productType.getBarCode().equals(productCode),
			// amount == previousAmount
			assertFalse(ezShop.deleteProductFromSale(2, barcode, 16)); // transactionId !=
																		// openSaleTransaction.getTicketNumber()

			ezShop.deleteSaleTransaction(transactionId);
			assertFalse(ezShop.deleteProductFromSale(transactionId, barcode, 16)); // openSaleTransaction.getTicketNumber()
																					// == -1

			// assertTrue(ezShop.deleteProductFromSale(transactionId, barcode, 4)); // 18 euro

			// assertTrue(ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5));

			// assertTrue(ezShop.applyDiscountRateToSale(transactionId, 0.1));

			// assertTrue(ezShop.deleteSaleTransaction(transactionId));

			// assertEquals(2, ezShop.receiveCashPayment(transactionId, 20), 0.01); // 2 euro back
			//
			// int points = ezShop.computePointsForSale(transactionId);
			// assertEquals(1, points);
			//
			// assertTrue(ezShop.modifyPointsOnCard("1000000000", points));
			//
			// assertTrue(ezShop.endSaleTransaction(transactionId));
			//
			// double price = ezShop.getSaleTransaction(transactionId).getPrice();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase11() {
		// applyDiscountRateToProduct exceptions

		try {

			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			ezShop.addProductToSale(transactionId, barcode, 16);

			ezShop.logout();
			assertThrows(UnauthorizedException.class, () -> {
				ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5);
			});
			ezShop.login("Casper", "casper101");

			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.applyDiscountRateToProduct(null, barcode, 0.5);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.applyDiscountRateToProduct(0, barcode, 0.5);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.applyDiscountRateToProduct(-1, barcode, 0.5);
			});

			assertThrows(InvalidDiscountRateException.class, () -> {
				ezShop.applyDiscountRateToProduct(transactionId, barcode, -0.1);
			});
			assertThrows(InvalidDiscountRateException.class, () -> {
				ezShop.applyDiscountRateToProduct(transactionId, barcode, 1);
			});

			assertFalse(ezShop.applyDiscountRateToProduct(transactionId, "22637482635892", 0.5)); // !productType.getBarCode().equals(productCode)

			assertFalse(ezShop.applyDiscountRateToProduct(2, barcode, 0.5)); // transactionId !=
			// openSaleTransaction.getTicketNumber()

			ezShop.deleteSaleTransaction(transactionId);
			assertFalse(ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5)); // openSaleTransaction.getTicketNumber()
			// == -1

			// assertTrue(ezShop.deleteProductFromSale(transactionId, barcode, 4)); // 18 euro

			// assertTrue(ezShop.applyDiscountRateToProduct(transactionId, barcode, 0.5));

			// assertTrue(ezShop.applyDiscountRateToSale(transactionId, 0.1));

			// assertTrue(ezShop.deleteSaleTransaction(transactionId));

			// assertEquals(2, ezShop.receiveCashPayment(transactionId, 20), 0.01); // 2 euro back
			//
			// int points = ezShop.computePointsForSale(transactionId);
			// assertEquals(1, points);
			//
			// assertTrue(ezShop.modifyPointsOnCard("1000000000", points));
			//
			// assertTrue(ezShop.endSaleTransaction(transactionId));
			//
			// double price = ezShop.getSaleTransaction(transactionId).getPrice();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase12() {
		// applyDiscountRateToSale exceptions

		try {

			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			ezShop.addProductToSale(transactionId, barcode, 16);

			ezShop.logout();
			assertThrows(UnauthorizedException.class, () -> {
				ezShop.applyDiscountRateToSale(transactionId, 0.5);
			});
			ezShop.login("Casper", "casper101");

			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.applyDiscountRateToSale(null, 0.5);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.applyDiscountRateToSale(0, 0.5);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.applyDiscountRateToSale(-1, 0.5);
			});

			assertThrows(InvalidDiscountRateException.class, () -> {
				ezShop.applyDiscountRateToSale(transactionId, -0.1);
			});
			assertThrows(InvalidDiscountRateException.class, () -> {
				ezShop.applyDiscountRateToSale(transactionId, 1);
			});

			assertFalse(ezShop.applyDiscountRateToSale(2, 0.5)); // transactionId !=
			// openSaleTransaction.getTicketNumber()

			ezShop.deleteSaleTransaction(transactionId);
			assertFalse(ezShop.applyDiscountRateToSale(transactionId, 0.5)); // openSaleTransaction.getTicketNumber()==
																				// -1
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase13() {
		// computePointsForSale exceptions

		try {

			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			ezShop.addProductToSale(transactionId, barcode, 16);

			ezShop.logout();
			assertThrows(UnauthorizedException.class, () -> {
				ezShop.computePointsForSale(transactionId);
			});
			ezShop.login("Casper", "casper101");

			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.computePointsForSale(null);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.computePointsForSale(0);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.computePointsForSale(-1);
			});

			assertEquals(-1, ezShop.computePointsForSale(2)); // transactionId !=
			// openSaleTransaction.getTicketNumber()

			ezShop.deleteSaleTransaction(transactionId);
			assertEquals(-1, ezShop.computePointsForSale(transactionId)); // openSaleTransaction.getTicketNumber()==
																			// -1
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase14() {
		// endSaleTransaction exceptions

		try {

			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			ezShop.addProductToSale(transactionId, barcode, 16);

			ezShop.logout();
			assertThrows(UnauthorizedException.class, () -> {
				ezShop.endSaleTransaction(transactionId);
			});
			ezShop.login("Casper", "casper101");

			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.endSaleTransaction(null);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.endSaleTransaction(0);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.endSaleTransaction(-1);
			});

			assertFalse(ezShop.endSaleTransaction(2)); // transactionId !=
			// openSaleTransaction.getTicketNumber()
			assertTrue(ezShop.endSaleTransaction(transactionId));
			assertFalse(ezShop.endSaleTransaction(transactionId));

			ezShop.deleteSaleTransaction(transactionId);
			assertFalse(ezShop.endSaleTransaction(transactionId)); // openSaleTransaction.getTicketNumber()==
																	// -1
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase15() {
		// deleteSaleTransaction exceptions

		try {

			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			ezShop.addProductToSale(transactionId, barcode, 16);

			ezShop.logout();
			assertThrows(UnauthorizedException.class, () -> {
				ezShop.deleteSaleTransaction(transactionId);
			});
			ezShop.login("Casper", "casper101");

			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.deleteSaleTransaction(null);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.deleteSaleTransaction(0);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.deleteSaleTransaction(-1);
			});

			assertFalse(ezShop.deleteSaleTransaction(2)); // transactionId !=
			// openSaleTransaction.getTicketNumber()

			ezShop.deleteSaleTransaction(transactionId);
			assertFalse(ezShop.endSaleTransaction(transactionId)); // openSaleTransaction.getTicketNumber()==
																	// -1
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCase16() {
		// getSaleTransaction exceptions

		try {

			Integer transactionId = ezShop.startSaleTransaction();
			assertEquals(Integer.valueOf(1), transactionId);

			String barcode = "12637482635892";
			List<ProductType> productTypes = ezShop.getAllProductTypes();
			ProductType productType = null;
			for (ProductType pt : productTypes) {
				if (pt.getBarCode().equals(barcode)) {
					productType = pt;
				}
			}
			ProductType expectedPt = new ProductTypeImpl("piccoli", "biscotti", barcode, 1.5);
			expectedPt.setId(1);
			expectedPt.setLocation("2-aisle-2");
			expectedPt.setQuantity(25);
			assertEquals(expectedPt, productType);

			ezShop.addProductToSale(transactionId, barcode, 16);

			ezShop.logout();
			assertThrows(UnauthorizedException.class, () -> {
				ezShop.getSaleTransaction(transactionId);
			});
			ezShop.login("Casper", "casper101");

			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.getSaleTransaction(null);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.getSaleTransaction(0);
			});
			assertThrows(InvalidTransactionIdException.class, () -> {
				ezShop.getSaleTransaction(-1);
			});

			assertEquals(null, ezShop.getSaleTransaction(transactionId)); // transactionId has not been ended yet

			// TODO: assertTrue(ezShop.recordBalanceUpdate(price)); I can't
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
