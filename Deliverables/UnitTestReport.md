# Unit Testing Documentation

Authors:

Date:

Version:

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >

<<<<<<< HEAD
### **Class EZShop**
#### createProductType
**Criteria for method *createProductType*:**

 - Validity of the String parameter *description*
 - Length of the String *description*
 - Validity of the String parameter *productCode*
 - Length of the String *productCode*
 - Sign of the Double parameter *pricePerUnit*
 - Existence of product with same *productCode* in db
 - User logged in has authority

**Predicates for method *createProductType*:**

| Criteria                                       | Predicate |
| ---------------------------------------------- | --------- |
| Validity of the String parameter *description* | Valid     |
|                                                | NULL      |
| Length of the String *description*             | > 0       |
|                                                | = 0 ("")  |
| Validity of the String parameter *productCode* | Valid     |
|                                                | Invalid   |
| Length of the String *productCode*             | > 0       |
|                                                | = 0 ("")  |
| Sign of the Double parameter *pricePerUnit*    | > 0       |
|                                                | <= 0      |
| Existence of product with same barcode in db   | False     |
|                                                | True      |
| User logged in has authority                   | True      |
|                                                | False     |

**Boundaries**:

| Criteria                                    | Boundary values    |
| ------------------------------------------- | ------------------ |
| Sign of the Double parameter *pricePerUnit* | -0.0001, 0, 0.0001 |

**Combination of predicates**:

| Validity of the String parameter *description* | Length of the String *description* | Validity of the String parameter *productCode* | Length of the String *productCode* | Sign of the Double parameter *pricePerUnit* | Existence of product with same barcode in db | User logged in has authority | Valid / Invalid | Description of the test case                                                        | JUnit test case CreateProductTypeTest |
| ---------------------------------------------- | ---------------------------------- | ---------------------------------------------- | ---------------------------------- | ------------------------------------------- | -------------------------------------------- | ---------------------------- | --------------- | ----------------------------------------------------------------------------------- | ------------------------------------- |
| Valid                                          | > 0                                | Valid                                          | > 0                                | > 0                                         | True                                         | False                        | Valid           | T1("valid descr", "12637482635892", 2.5, "note") &rarr; productId (product created) | testCase1                             |
| "                                              | "                                  | "                                              | "                                  | "                                           | "                                            | True                         | Invalid         | Unauthorized user logged in &rarr; error                                            | testCase2                             |
| "                                              | "                                  | "                                              | "                                  | "                                           | False                                        | -                            | Invalid         | Existence of product with same barcode in db &rarr; -1 (product not created)        | testCase3                             |
| "                                              | "                                  | "                                              | "                                  | <= 0                                        | -                                            | -                            | Invalid         | T4("valid descr", "12637482635892", -2.5, "note") &rarr; error                      | testCase4                             |
| "                                              | "                                  | "                                              | = 0                                | -                                           | -                                            | -                            | Invalid         | T5("valid descr", "", 2.5, "note") &rarr; error                                     | testCase5                             |
| "                                              | "                                  | Invalid                                        | -                                  | -                                           | -                                            | -                            | Invalid         | T6("valid descr", null, 2.5, "note") &rarr; error                                   | testCase6                             |
| *                                              | = 0                                | -                                              | -                                  | -                                           | -                                            | -                            | Invalid         | T7("", "12637482635892", 2.5, "note") &rarr; error                                  | testCase7                             |
| NULL                                           | -                                  | -                                              | -                                  | -                                           | -                                            | -                            | Invalid         | T8(null, "12637482635892", 2.5, "note") &rarr; error                                | testCase8                             |
=======
### Class ProductTypeImpl
#### constructor
**Criteria for method *constructor*:**

 - Validity of the String *note*
 - Validity of the String *description*
 - Validity of the String *barcode*
 - Validity of the Double *pricePerUnit*

**Predicates for method *constructor*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the String *note*                        |  Valid    |
| Validity of the String *description*                 |  Valid    |
| Validity of the String *barcode*                     |  Valid    |
| Validity of the Double *pricePerUnit*                |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the String *note*|Validity of the String *description*|Validity of the String *barcode*|Validity of the Double *pricePerUnit*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|-------|-------|-------|
|Valid  |Valid  |Valid  |Valid  |Valid  |product = new ProductTypeImpl("note","description","12637482635892",2.5);|testCase1|
>>>>>>> 0a0da029e29ecb128e1d5ed48895148caa6764be

#### setId
**Criteria for method *setId*:**

 - Validity of the Integer *id*

**Predicates for method *setId*:**

| Criteria                                             | Predicate |
<<<<<<< HEAD
| ---------------------------------------------------- | --------- |
| Sign of the Integer *productId*                      | > 0       |
|                                                      | <= 0      |
| Validity of the String parameter *description*       | Valid     |
|                                                      | NULL      |
| Length of the String *description*                   | > 0       |
|                                                      | = 0 ("")  |
| Validity of the String parameter *productCode*       | Valid     |
|                                                      | Invalid   |
| Length of the String *productCode*                   | > 0       |
|                                                      | = 0 ("")  |
| Sign of the Double parameter *pricePerUnit*          | > 0       |
|                                                      | <= 0      |
| User logged in has authority                         | True      |
|                                                      | False     |
| Existence of product with same *barcode* in db       | False     |
|                                                      | True      |
| Existence of product with matching *productId* in db | True      |
|                                                      | False     |

**Boundaries**:

| Criteria                                    | Boundary values    |
| ------------------------------------------- | ------------------ |
| Sign of the Integer *productId*             | -1, 0, 1           |
| Sign of the Double parameter *pricePerUnit* | -0.0001, 0, 0.0001 |

**Combination of predicates**:

| Sign of the Integer *productId* | Validity of the String parameter *description* | Length of the String *description* | Validity of the String parameter *productCode* | Length of the String *productCode* | Sign of the Double parameter *pricePerUnit* | User logged in has authority | Existence of product with same *barcode* in db | Existence of product with matching *productId* in db | Valid / Invalid | Description of the test case                                                             | JUnit test case UpdateProductTest |
| ------------------------------- | ---------------------------------------------- | ---------------------------------- | ---------------------------------------------- | ---------------------------------- | ------------------------------------------- | ---------------------------- | ---------------------------------------------- | ---------------------------------------------------- | --------------- | ---------------------------------------------------------------------------------------- | --------------------------------- |
| > 0                             | Valid                                          | > 0                                | Valid                                          | > 0                                | > 0                                         | True                         | False                                          | True                                                 | Valid           | T1(1, "valid descr", "6253478956438", 2.5, "note") &rarr; true (product updated)         | testCase1                         |
| "                               | "                                              | "                                  | "                                              | "                                  | "                                           | "                            | "                                              | False                                                | Invalid         | T2(1500, "valid descr", "6253478956438", 2.5, "note") &rarr; false (product not updated) | testCase2                         |
| "                               | "                                              | "                                  | "                                              | "                                  | "                                           | "                            | True                                           | -                                                    | Invalid         | T3(1, "valid descr", "12343212343219", 2.5, "note") &rarr; false (product not updated)   | testCase3                         |
| "                               | "                                              | "                                  | "                                              | "                                  | "                                           | False                        | -                                              | -                                                    | Invalid         | Unauthorized user logged in &rarr; error                                                 | testCase4                         |
| "                               | "                                              | "                                  | "                                              | "                                  | <= 0                                        | -                            | -                                              | -                                                    | Invalid         | T5(1, "valid descr", "12343212343219", -2.5, "note") &rarr; error                        | testCase5                         |
| "                               | "                                              | "                                  | "                                              | = 0                                | -                                           | -                            | -                                              | -                                                    | Invalid         | T6(1, "valid descr", "", 2.5, "note") &rarr; error                                       | testCase6                         |
| "                               | "                                              | "                                  | Invalid                                        | -                                  | -                                           | -                            | -                                              | -                                                    | Invalid         | T7(1, "valid descr", null, 2.5, "note") &rarr; error                                     | testCase7                         |
| "                               | "                                              | = 0                                | -                                              | -                                  | -                                           | -                            | -                                              | -                                                    | Invalid         | T8(1, "", "12343212343219", 2.5, "note") &rarr; error                                    | testCase8                         |
| "                               | NULL                                           | -                                  | -                                              | -                                  | -                                           | -                            | -                                              | -                                                    | Invalid         | T9(1, null, "12343212343219", 2.5, "note") &rarr; error                                  | testCase9                         |
| <= 0                            | -                                              | -                                  | -                                              | -                                  | -                                           | -                            | -                                              | -                                                    | Invalid         | T10(-1, "valid descr", "12343212343219", 2.5, "note") &rarr; error                       | testCase10                        |
=======
| --------                                             | --------- |
| Validity of the Integer *id*                         |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the Integer *id*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setId(1);     |testCase2       |

#### setProductDescription
**Criteria for method *setProductDescription*:**

 - Validity of the String *description*

**Predicates for method *setProductDescription*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the String *description*                 |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |
>>>>>>> 0a0da029e29ecb128e1d5ed48895148caa6764be

**Combination of predicates**:

|Validity of the String *description*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setProductDescription("new desc");     |testCase3       |

#### setBarCode
**Criteria for method *setBarCode*:**

 - Validity of the String *barCode*

**Predicates for method *setBarCode*:**

<<<<<<< HEAD
| Criteria                        | Predicate |
| ------------------------------- | --------- |
| Sign of the Integer *productId* | > 0       |
|                                 | <= 0      |
| User logged in has authority    | True      |
|                                 | False     |
**Boundaries**:

| Criteria                        | Boundary values |
| ------------------------------- | --------------- |
| Sign of the Integer *productId* | -1, 0, 1        |

**Combination of predicates**:

| Sign of the Integer *productId* | User logged in has authority | Valid / Invalid | Description of the test case             | JUnit test case DeleteProductTypeTest |
| ------------------------------- | ---------------------------- | --------------- | ---------------------------------------- | ------------------------------------- |
| > 0                             | True                         | Valid           | T1(2) &rarr; true (product deleted)      | testCase1                             |
| "                               | False                        | Invalid         | Unauthorized user logged in &rarr; error | testCase2                             |
| <= 0                            | -                            | Invalid         | T3(-1) &rarr; error                      | testCase3                             |

=======
| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the String *barCode*                     |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the String *varCode*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setBarCode("6253478956438");     |testCase4      |
>>>>>>> 0a0da029e29ecb128e1d5ed48895148caa6764be

#### setPricePerUnit
**Criteria for method *setPricePerUnit*:**

 - Validity of the Double *pricePerUnit*

**Predicates for method *setPricePerUnit*:**

<<<<<<< HEAD
| Criteria                     | Predicate |
| ---------------------------- | --------- |
| User logged in has authority | True      |
|                              | False     |
=======
| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the Double *pricePerUnit*                |  Valid    |

>>>>>>> 0a0da029e29ecb128e1d5ed48895148caa6764be
**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |

**Combination of predicates**:

<<<<<<< HEAD
| User logged in has authority | Valid / Invalid | Description of the test case             | JUnit test case GetAllProductTypesTest |
| ---------------------------- | --------------- | ---------------------------------------- | -------------------------------------- |
| True                         | Valid           | T1() &rarr; List<ProductTypes>           | testCase1                              |
| False                        | Invalid         | Unauthorized user logged in &rarr; error | testCase2                              |
=======
|Validity of the Double *pricePerUnit*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setPricePerUnit(25.0);     |testCase5      |

#### setNote
**Criteria for method *setNote*:**

 - Validity of the String *note*

**Predicates for method *setNote*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the Double *note*                        |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:
>>>>>>> 0a0da029e29ecb128e1d5ed48895148caa6764be

|Validity of the String *note*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setNote("new note");     |testCase6     |

#### setLocation
**Criteria for method *setLocation*:**

 - Validity of the String *location*

**Predicates for method *setLocation*:**

| Criteria                                             | Predicate |
<<<<<<< HEAD
| ---------------------------------------------------- | --------- |
| Validity of the String parameter *productCode*       | Valid     |
|                                                      | Invalid   |
| Length of the String *productCode*                   | > 0       |
|                                                      | = 0 ("")  |
| User logged in has authority                         | True      |
|                                                      | False     |
| Existence of product with matching *productId* in db | True      |
|                                                      | False     |
=======
| --------                                             | --------- |
| Validity of the String *location*                    |  Valid    |
>>>>>>> 0a0da029e29ecb128e1d5ed48895148caa6764be

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |

**Combination of predicates**:

<<<<<<< HEAD
| Validity of the String parameter *productCode* | Length of the String *productCode* | User logged in has authority | Existence of product with matching *productId* in db | Valid / Invalid | Description of the test case             | JUnit test case GetProductTypeByBarCodeTest |
| ---------------------------------------------- | ---------------------------------- | ---------------------------- | ---------------------------------------------------- | --------------- | ---------------------------------------- | ------------------------------------------- |
| Valid                                          | > 0                                | True                         | True                                                 | Valid           | T1("12343212343219") &rarr; ProductTypes | testCase1                                   |
| "                                              | "                                  | "                            | False                                                | Valid           | T2("6253478956438") &rarr; null          | testCase2                                   |
| "                                              | "                                  | False                        | -                                                    | Invalid         | Unauthorized user logged in &rarr; error | testCase3                                   |
| "                                              | = 0                                | -                            | -                                                    | Invalid         | T4("") &rarr; error                      | testCase4                                   |
| Invalid                                        | -                                  | -                            | -                                                    | Invalid         | T4(null) &rarr; error                    | testCase5                                   |
=======
|Validity of the String *location*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setLocation("3 aisle 3");   |testCase7      |
>>>>>>> 0a0da029e29ecb128e1d5ed48895148caa6764be

#### setQuantity
**Criteria for method *setQuantity*:**

 - Validity of the Integer *quantity*

**Predicates for method *setQuantity*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the Integer *quantity*                   |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the Integer *quantity*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setQuantity(4);   |testCase7      |

#### setDiscountRate
**Criteria for method *setDiscountRate*:**

 - Validity of the Integer *discountRate*

**Predicates for method *setDiscountRate*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the Integer *discountRate*               |  Valid    |

<<<<<<< HEAD
| Criteria                                       | Predicate        |
| ---------------------------------------------- | ---------------- |
| User logged in has authority                   | True             |
|                                                | False            |
| Length of the String *description*             | > 0              |
|                                                | = 0 ("" or NULL) |
| Validity of the String parameter *description* | Valid            |
|                                                | NULL             |
=======
>>>>>>> 0a0da029e29ecb128e1d5ed48895148caa6764be
**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |

**Combination of predicates**:

<<<<<<< HEAD
| User logged in has authority | Length of the String *description* | Validity of the String parameter *description* | Valid / Invalid | Description of the test case                   | JUnit test case GetProductTypeByDescriptionTest |
| ---------------------------- | ---------------------------------- | ---------------------------------------------- | --------------- | ---------------------------------------------- | ----------------------------------------------- |
| TRUE                         | > =                                | Valid                                          | Valid           | T1("description") &rarr; List<ProductTypes>    | testCase1                                       |
| "                            | = 0                                | NULL                                           | Valid           | T2("") &rarr; List<ProductTypes> with size = 0 | testCase2                                       |
| FALSE                        | -                                  |                                                | Invalid         | Unauthorized user logged in &rarr; error       | testCase3                                       |
=======
|Validity of the Integer *discountRate*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |ProductTypeImpl product2 = new ProductTypeImpl();  product2.setDiscountRate(20.0);  |testCase7      |

>>>>>>> 0a0da029e29ecb128e1d5ed48895148caa6764be

### Class TicketEntryImpl

#### constructor
**Criteria for method *constructor*:**

 - Validity of the String *barcode*
 - Validity of the String *description*
 - Validity of the Double *pricePerUnit*
 - Validity of the Double *discountRate*
 - Validity of the Integer *amount*

**Predicates for method *constructor*:**

| Criteria                                             | Predicate |
<<<<<<< HEAD
| ---------------------------------------------------- | --------- |
| Sign of the Integer *productId*                      | > 0       |
|                                                      | <= 0      |
| User logged in has authority                         | True      |
|                                                      | False     |
| Existence of product with matching *productId* in db | True      |
|                                                      | False     |
| Sign of (ProductType.quantity + *toBeAdded*)         | > 0       |
|                                                      | <= 0      |
| Existence of location for the product                | True      |
|                                                      | False     |
**Boundaries**:

| Criteria                                     | Boundary values |
| -------------------------------------------- | --------------- |
| Sign of (ProductType.quantity + *toBeAdded*) | -1, 0, 1        |

**Combination of predicates**:

| Sign of the Integer *productId* | User logged in has authority | Existence of product with matching *productId* in db | Sign of (ProductType.quantity + *toBeAdded*) | Existence of location for the product | Valid / Invalid | Description of the test case                     | JUnit test case UpdateQuantityTest |
| ------------------------------- | ---------------------------- | ---------------------------------------------------- | -------------------------------------------- | ------------------------------------- | --------------- | ------------------------------------------------ | ---------------------------------- |
| > 0                             | True                         | True                                                 | > 0                                          | True                                  | Valid           | T1(1, 10) &rarr; true                            | testCase1                          |
| "                               | "                            | "                                                    | "                                            | False                                 | Valid           | T2(1, 10) &rarr; false (no location)             | testCase2                          |
| "                               | "                            | "                                                    | <= 0                                         | -                                     | Valid           | T3(1, -1000) &rarr; false (subtracting too much) | testCase3                          |
| "                               | "                            | False                                                | -                                            | -                                     | Valid           | T4(1000, 10) &rarr; false (no id matching)       | testCase4                          |
| "                               | False                        | -                                                    | -                                            | -                                     | Invalid         | Unauthorized user logged in &rarr; error         | testCase5                          |
| <= 0                            | -                            | -                                                    | -                                            | -                                     | Invalid         | T6(-1, 10 ) &rarr; error                         | testCase6                          |



#### updatePosition
**Criteria for method *updatePosition*:**

 - Sign of the Integer *productId*
 - User logged in has authority   
 - Existence of product with matching *productId* in db
 - Sign of the int *toBeAdded*

**Predicates for method *updatePosition*:**

| Criteria                                                  | Predicate        |
| --------------------------------------------------------- | ---------------- |
| Sign of the Integer *productId*                           | > 0              |
|                                                           | <= 0             |
| User logged in has authority                              | True             |
|                                                           | False            |
| Length of the String *newPos*                             | > 0              |
|                                                           | = 0 ("" or null) |
| Validity of the String parameter *newPos*                 | Valid            |
|                                                           | Invalid          |
| Existence of product with matching *productId* in db      | True             |
|                                                           | False            |
| Existence of a product with matching location as *newPos* | True             |
|                                                           | False            |
**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |

**Combination of predicates**:

| Sign of the Integer *productId* | Length of the String *newPos* | User logged in has authority | Validity of the String parameter *newPos* | Existence of product with matching *productId* in db | Existence of a product with matching location as *newPos* | Valid / Invalid | Description of the test case                                     | JUnit test case UpdatePositionTest |
| ------------------------------- | ----------------------------- | ---------------------------- | ----------------------------------------- | ---------------------------------------------------- | --------------------------------------------------------- | --------------- | ---------------------------------------------------------------- | ---------------------------------- |
| > 0                             | True                          | > 0                          | Valid                                     | True                                                 | True                                                      | Valid           | T1(1, "3 aisle 2") &rarr; true                                   | testCase1                          |
| "                               | "                             | "                            | "                                         | "                                                    | False                                                     | Valid           | T2(3, "17 corridor 42") &rarr; false (location already assigned) | testCase2                          |
| "                               | "                             | "                            | "                                         | False                                                | -                                                         | Valid           | T3(1000, "4 aisle 4") &rarr; false (no matching id)              | testCase3                          |
| "                               | "                             | "                            | Invalid                                   | -                                                    | -                                                         | Invalid         | T4(1, "corridor") &rarr; error                                   | testCase4                          |
| "                               | "                             | =0                           | -                                         | -                                                    | -                                                         | Invalid         | T5(1, "") &rarr; true                                            | testCase5                          |
| "                               | False                         | -                            | -                                         | -                                                    | -                                                         | Valid           | Unauthorized user logged in &rarr; error                         | testCase6                          |
| <= 0                            | -                             | -                            | -                                         | -                                                    | -                                                         | Invalid         | T7(-1, "4 aisle 4") &rarr; error                                 | testCase7                          |

 ### **Class SaleTransactionImpl - method upsertEntry **



**Criteria for method *name*:**
	

 - 
 - 





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
| ---------- | ---------- | --- | --------------- | ---------------------------- | --------------- |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
=======
| --------                                             | --------- |
| Validity of the String *barcode*                     |  Valid    |
| Validity of the String *description*                 |  Valid    |
| Validity of the Double *pricePerUnit*                |  Valid    |
| Validity of the Double *discountRate*                |  Valid    |
| Validity of the Integer *amount*                     |  Valid   |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the String *barcode*|Validity of the String *description*|Validity of the Double *pricePerUnit*|Validity of the Double *discountRate*|Validity of the Integer *amount*| Valid / Invalid | Description of the test case | JUnit test case TicketEntryClassTest|
|-------|-------|-------|-------|-------|-------|-------|-------|
|Valid  |Valid  |Valid  |Valid  |Valid  |Valid  |product = product = new TicketEntryImpl("12637482635892", "description", 2.0, 0.0, 10);|testCase1|


#### setProductDescription
**Criteria for method *setProductDescription*:**

 - Validity of the String *description*

**Predicates for method *setProductDescription*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the String *description*                 |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the String *description*| Valid / Invalid | Description of the test case | JUnit test case TicketEntryClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setProductDescription("new desc");     |testCase2       |

#### setBarCode
**Criteria for method *setBarCode*:**

 - Validity of the String *barCode*

**Predicates for method *setBarCode*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the String *barCode*                     |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the String *varCode*| Valid / Invalid | Description of the test case | JUnit test case TicketEntryClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setBarCode("6253478956438");     |testCase3      |

#### setPricePerUnit
**Criteria for method *setPricePerUnit*:**

 - Validity of the Double *pricePerUnit*

**Predicates for method *setPricePerUnit*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the Double *pricePerUnit*                |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the Double *pricePerUnit*| Valid / Invalid | Description of the test case | JUnit test case TicketEntryClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setPricePerUnit(25.0);     |testCase4      |

#### setDiscountRate
**Criteria for method *setDiscountRate*:**

 - Validity of the Integer *discountRate*

**Predicates for method *setDiscountRate*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the Integer *discountRate*               |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the Integer *discountRate*| Valid / Invalid | Description of the test case | JUnit test case TicketEntryClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |ProductTypeImpl product2 = new ProductTypeImpl();  product2.setDiscountRate(20.0);  |testCase5      |

#### setAmount
**Criteria for method *setAmount*:**

 - Validity of the Integer *amount*

**Predicates for method *setAmount*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the Integer *amount*                     |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the Integer *amount*| Valid / Invalid | Description of the test case | JUnit test case TicketEntryClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setAmount(15);   |testCase6      |

### Class PositionImpl

#### constructor
*Criteria for method *constructor*:**

 - Validity of the String *position*

**Predicates for method *constructor*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Length of the String *position.split(" ")*           | = 3       |
|                                                      | != 3      |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Length of the String *position.split(" ")* | Valid / Invalid | Description of the test case | JUnit test case TicketEntryClassTest|
|-------|-------|-------|-------|
| = 3   |Valid  |PositionImpl position4 = new PositionImpl("3 aisle 4");   |testCase1      |
| != 3  |Valid  |PositionImpl position3 = new PositionImpl("corridor");    |testCase2      |

#### setPosition
*Criteria for method *setPosition*:**

 - Validity of the String *position*

**Predicates for method *setPosition*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Length of the String *position.split(" ")*           | = 3       |
|                                                      | != 3      |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Length of the String *position.split(" ")* | Valid / Invalid | Description of the test case | JUnit test case TicketEntryClassTest|
|-------|-------|-------|-------|
| = 3   |Valid  |PositionImpl position = new PositionImpl(); position.setPosition("2 corridor 3");   |testCase3      |
| != 3  |Valid  |PositionImpl position = new PositionImpl(); position.setPosition("");    |testCase4      |
>>>>>>> 0a0da029e29ecb128e1d5ed48895148caa6764be






 ### **Class *class_name* - method *name***



**Criteria for method *name*:**
	

 - 
 - 





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
| ---------- | ---------- | --- | --------------- | ---------------------------- | --------------- |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |




# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
| --------- | --------------- |
|           |                 |
|           |                 |
|           |                 |  |

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
|           |           |                      |                 |
|           |           |                      |                 |
|           |           |                      |                 |  |



