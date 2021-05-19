package it.polito.ezshop;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BalanceOperationClassTest.class, CustomerClassTest.class, ExceptionsClassTest.class,
		EZShopClassTest.class, OrderClassTest.class, PositionClassTest.class, ProductTypeClassTest.class,
		ReturnTransactionClassTest.class, SaleTransactionClassTest.class, TicketEntryClassTest.class,
		UserClassTest.class })

public class UnitTestSuite {

}
