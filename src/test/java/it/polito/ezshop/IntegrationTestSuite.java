package it.polito.ezshop;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BalanceOperationClassTest.class, ComputeBalanceDAOTest.class, ComputeBalanceTest.class,
		CreateProductTypeTest.class, CustomerClassTest.class, CustomerMethodsClassTest.class,
		DAOProductTypeMethodsClassTest.class, DAOSaleTransactionClassTest.class, DeleteProductTypeTest.class,
		ExceptionsClassTest.class, EZShopClassTest.class, GetAllOrdersDAOTest.class, GetAllOrdersTest.class,
		GetAllProductTypesTest.class, GetCreditsAndDebitsDAOTest.class, getCreditsAndDebitsTest.class,
		GetProductTypeByBarCodeTest.class, GetProductTypeByDescriptionTest.class, issueOrderDAOTest.class,
		IssueOrderTest.class, OrderClassTest.class, PayOrderDAOTest.class, payOrderForDAOTest.class,
		PayOrderForTest.class, PayOrderTest.class, PositionClassTest.class, ProductTypeClassTest.class,
		RecordBalanceUpdateDAOTest.class, RecordBalanceUpdateTest.class, RecordOrderArrivalDAOTest.class,
		RecordOrderArrivalTest.class, ReturnTransactionClassTest.class, SaleTransactionClassTest.class,
		TicketEntryClassTest.class, UC1TestClass.class, UC2TestClass.class, UC3TestClass.class, UC4TestClass.class,
		UC5TestClass.class, UC6TestClass.class, UC8TestClass.class, UC9TestClass.class, UnitTestSuite.class,
		UpdatePositionTest.class, UpdateProductTest.class, UpdateQuantityTest.class, UserClassTest.class,
		UserMethodsClassTest.class })

public class IntegrationTestSuite {

}
