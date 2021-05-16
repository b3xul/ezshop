package it.polito.ezshop.acceptanceTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@SuiteClasses({CreateProductTypeTest.class,UpdateProductTest.class,DeleteProductTypeTest.class,GetAllProductTypesTest.class,
	GetProductTypeByBarCodeTest.class,GetProductTypeByDescriptionTest.class, UpdateQuantityTest.class,UpdatePositionTest.class})

public class TestEZShop {
    
    
}
