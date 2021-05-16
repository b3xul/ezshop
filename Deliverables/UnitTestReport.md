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
| --------                                       | --------- |
| Validity of the String parameter *description* |  Valid    |
|                                                |  NULL     |
| Length of the String *description*             |  > 0      |
|                                                |  = 0 ("") |
| Validity of the String parameter *productCode* |  Valid    |
|                                                |  Invalid  |
| Length of the String *productCode*             |  > 0      |
|                                                |  = 0 ("") |
| Sign of the Double parameter *pricePerUnit*    |  > 0      |
|                                                |  <= 0     |
| Existence of product with same barcode in db   |  False    |
|                                                |  True     |
| User logged in has authority                   |  True     |
|                                                |  False    |

**Boundaries**:

| Criteria                                       | Boundary values    |
| --------                                       | ---------------    |
| Sign of the Double parameter *pricePerUnit*    | -0.0001, 0, 0.0001 |

**Combination of predicates**:

|Validity of the String parameter *description*|Length of the String *description*|Validity of the String parameter *productCode*|Length of the String *productCode*|Sign of the Double parameter *pricePerUnit*|Existence of product with same barcode in db|User logged in has authority| Valid / Invalid | Description of the test case | JUnit test case CreateProductTypeTest |
|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|
| Valid | > 0   | Valid | > 0   | > 0   | True  | False |Valid  | T1("valid descr", "12637482635892", 2.5, "note") &rarr; productId (product created) |testCase1       |
| "     | "     | "     | "     | "     | "     | True  |Invalid| Unauthorized user logged in &rarr; error                                            |testCase2       |
| "     | "     | "     | "     | "     | False | -     |Invalid| Existence of product with same barcode in db &rarr; -1 (product not created)        |testCase3       |
| "     | "     | "     | "     | <= 0  | -     | -     |Invalid| T4("valid descr", "12637482635892", -2.5, "note") &rarr; error                      |testCase4       |
| "     | "     | "     | = 0   | -     | -     | -     |Invalid| T5("valid descr", "", 2.5, "note") &rarr; error                                     |testCase5       |
| "     | "     |Invalid| -     | -     | -     | -     |Invalid| T6("valid descr", null, 2.5, "note") &rarr; error                                   |testCase6       |
| *     | = 0   | -     | -     | -     | -     | -     |Invalid| T7("", "12637482635892", 2.5, "note") &rarr; error                                  |testCase7       |
| NULL  | -     | -     | -     | -     | -     | -     |Invalid| T8(null, "12637482635892", 2.5, "note") &rarr; error                                |testCase8       |



#### updateProduct
**Criteria for method *updateProduct*:**
	
 - Sign of the Integer *productId*
 - Validity of the String parameter *description*
 - Length of the String *description* 
 - Validity of the String parameter *productCode*
 - Length of the String *productCode* 
 - Sign of the Double parameter *pricePerUnit* 
 - User logged in has authority 
 - Existence of product with same *barcode* in db
 - Existence of product with matching *productId* in db 

**Predicates for method *updateProduct*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Sign of the Integer *productId*                      |  > 0      |
|                                                      |  <= 0     | 
| Validity of the String parameter *description*       |  Valid    |
|                                                      |  NULL     |
| Length of the String *description*                   |  > 0      |
|                                                      |  = 0 ("") |
| Validity of the String parameter *productCode*       |  Valid    |
|                                                      |  Invalid  |
| Length of the String *productCode*                   |  > 0      |
|                                                      |  = 0 ("") |
| Sign of the Double parameter *pricePerUnit*          |  > 0      |
|                                                      |  <= 0     |
| User logged in has authority                         |  True     |
|                                                      |  False    |
| Existence of product with same *barcode* in db       |  False    |
|                                                      |  True     |
| Existence of product with matching *productId* in db |  True     |
|                                                      |  False    |

**Boundaries**:

| Criteria                                       | Boundary values    |
| --------                                       | ---------------    |
| Sign of the Integer *productId*                |      -1, 0, 1      |
| Sign of the Double parameter *pricePerUnit*    | -0.0001, 0, 0.0001 |

**Combination of predicates**:

|Sign of the Integer *productId*|Validity of the String parameter *description*|Length of the String *description*|Validity of the String parameter *productCode*|Length of the String *productCode*|Sign of the Double parameter *pricePerUnit*|User logged in has authority|Existence of product with same *barcode* in db|Existence of product with matching *productId* in db| Valid / Invalid | Description of the test case | JUnit test case UpdateProductTest|
|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|
| > 0   | Valid | > 0   | Valid | > 0   | > 0   | True  | False | True  |Valid  | T1(1, "valid descr", "6253478956438", 2.5, "note") &rarr; true (product updated)         |testCase1       |
| "     | "     | "     | "     | "     | "     | "     | "     | False |Invalid| T2(1500, "valid descr", "6253478956438", 2.5, "note") &rarr; false (product not updated) |testCase2       |
| "     | "     | "     | "     | "     | "     | "     | True  | -     |Invalid| T3(1, "valid descr", "12343212343219", 2.5, "note") &rarr; false (product not updated)   |testCase3       |
| "     | "     | "     | "     | "     | "     | False | -     | -     |Invalid| Unauthorized user logged in &rarr; error                                                 |testCase4       |
| "     | "     | "     | "     | "     | <= 0  | -     | -     | -     |Invalid| T5(1, "valid descr", "12343212343219", -2.5, "note") &rarr; error                        |testCase5       |
| "     | "     | "     | "     | = 0   | -     | -     | -     | -     |Invalid| T6(1, "valid descr", "", 2.5, "note") &rarr; error                                       |testCase6       |
| "     | "     | "     |Invalid| -     | -     | -     | -     | -     |Invalid| T7(1, "valid descr", null, 2.5, "note") &rarr; error                                     |testCase7       |
| "     | "     | = 0   | -     | -     | -     | -     | -     | -     |Invalid| T8(1, "", "12343212343219", 2.5, "note") &rarr; error                                    |testCase8       |
| "     | NULL  | -     | -     | -     | -     | -     | -     | -     |Invalid| T9(1, null, "12343212343219", 2.5, "note") &rarr; error                                  |testCase9       |
| <= 0  | -     | -     | -     | -     | -     | -     | -     | -     |Invalid| T10(-1, "valid descr", "12343212343219", 2.5, "note") &rarr; error                       |testCase10      |



#### deleteProductType
**Criteria for method *deleteProduct*:**

 - Sign of the Integer *productId*
 - User logged in has authority

**Predicates for method *deleteProduct*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Sign of the Integer *productId*                      |  > 0      |
|                                                      |  <= 0     | 
| User logged in has authority                         |  True     |
|                                                      |  False    |
**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |
| Sign of the Integer *productId*   |     -1, 0, 1       |

**Combination of predicates**:

|Sign of the Integer *productId*|User logged in has authority| Valid / Invalid | Description of the test case | JUnit test case DeleteProductTypeTest|
|-------|-------|-------|-------|-------|
| > 0   | True  |Valid  | T1(2) &rarr; true (product deleted)       |testCase1       |
| "     | False |Invalid| Unauthorized user logged in &rarr; error  |testCase2       |
| <= 0  | -     |Invalid| T3(-1) &rarr; error                       |testCase3       |


#### getAllProductTypes
**Criteria for method *getAllProductTypes*:**

 - User logged in has authority

**Predicates for method *getAllProductTypes*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| User logged in has authority                         |  True     |
|                                                      |  False    |
**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|User logged in has authority| Valid / Invalid | Description of the test case | JUnit test case GetAllProductTypesTest|
|-------|-------|-------|-------|
|True   |Valid  | T1() &rarr; List<ProductTypes>           |testCase1       |
|False  |Invalid| Unauthorized user logged in &rarr; error |testCase2       |


#### getProductTypeByBarCode
**Criteria for method *getProductTypeByBarCode*:**

 - Validity of the String parameter *productCode* 
 - Length of the String *productCode*
 - User logged in has authority
 - Existence of product with matching *productId* in db

**Predicates for method *getProductTypeByBarCode*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the String parameter *productCode*       |  Valid    |
|                                                      |  Invalid  |
| Length of the String *productCode*                   |  > 0      |
|                                                      |  = 0 ("") |
| User logged in has authority                         |  True     |
|                                                      |  False    |
| Existence of product with matching *productId* in db |  True     |
|                                                      |  False    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the String parameter *productCode*|Length of the String *productCode*|User logged in has authority|Existence of product with matching *productId* in db| Valid / Invalid | Description of the test case | JUnit test case GetProductTypeByBarCodeTest|
|-------|-------|-------|-------|-------|-------|-------|
|Valid  | > 0   |True   |True   |Valid  | T1("12343212343219") &rarr; ProductTypes |testCase1       |
| "     | "     | "     |False  |Valid  | T2("6253478956438") &rarr; null          |testCase2       |
| "     | "     |False  | -     |Invalid|  Unauthorized user logged in &rarr; error|testCase3       |
| "     | = 0   | -     | -     |Invalid| T4("") &rarr; error                      |testCase4       |
|Invalid| -     | -     | -     |Invalid| T4(null) &rarr; error                    |testCase5       |




#### getProductTypesByDescription
**Criteria for method *getProductTypesByDescription*:**

 - User logged in has authority
 - Length of the String *description*  
 - Validity of the String parameter *description*

**Predicates for method *getProductTypesByDescription*:**

| Criteria                                             | Predicate         |
| --------                                             | ---------         |
| User logged in has authority                         |  True             |
|                                                      |  False            |
| Length of the String *description*                   |  > 0              |
|                                                      |  = 0 ("" or NULL) |
| Validity of the String parameter *description*       |  Valid            |
|                                                      |  NULL             |
**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|User logged in has authority|Length of the String *description*|Validity of the String parameter *description*| Valid / Invalid | Description of the test case | JUnit test case GetProductTypeByDescriptionTest|
|-------|-------|-------|-------|-------|-------|
|TRUE   | > =   |Valid  |Valid  | T1("description") &rarr; List<ProductTypes>   |testCase1       |
| "     | = 0   |NULL   |Valid  | T2("") &rarr; List<ProductTypes> with size = 0|testCase2       |
|FALSE  | -     |       |Invalid|  Unauthorized user logged in &rarr; error     |testCase3       |


#### updateQuantity
**Criteria for method *updateQuantity*:**

 - Sign of the Integer *productId*
 - User logged in has authority   
 - Existence of product with matching *productId* in db
 - Sign of the int *toBeAdded*

**Predicates for method *updateQuantity*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Sign of the Integer *productId*                      |  > 0      |
|                                                      |  <= 0     | 
| User logged in has authority                         |  True     |
|                                                      |  False    |
| Existence of product with matching *productId* in db |  True     |
|                                                      |  False    |
| Sign of (ProductType.quantity + *toBeAdded*)         |  > 0      |
|                                                      |  <= 0     |
| Existence of location for the product                |  True     |
|                                                      |  False    |
**Boundaries**:

| Criteria                                             | Boundary values    |
| --------                                             | ---------------    |
| Sign of (ProductType.quantity + *toBeAdded*)         |  -1, 0, 1          |

**Combination of predicates**:

|Sign of the Integer *productId*|User logged in has authority|Existence of product with matching *productId* in db|Sign of (ProductType.quantity + *toBeAdded*)|Existence of location for the product| Valid / Invalid | Description of the test case | JUnit test case UpdateQuantityTest|
|-------|-------|-------|-------|-------|-------|-------|-------|
| > 0   | True  | True  | > 0   | True  |Valid  | T1(1, 10) &rarr; true                                 |testCase1       |
| "     | "     | "     | "     | False |Valid  | T2(1, 10) &rarr; false (no location)                  |testCase2       |
| "     | "     | "     | <= 0  | -     |Valid  | T3(1, -1000) &rarr; false (subtracting too much)      |testCase3       |
| "     | "     | False | -     | -     |Valid  | T4(1000, 10) &rarr; false (no id matching)            |testCase4       |
| "     | False | -     | -     | -     |Invalid| Unauthorized user logged in &rarr; error              |testCase5       |
| <= 0  | -     | -     | -     | -     |Invalid| T6(-1, 10 ) &rarr; error                              |testCase6       |



#### updatePosition
**Criteria for method *updatePosition*:**

 - Sign of the Integer *productId*
 - User logged in has authority   
 - Existence of product with matching *productId* in db
 - Sign of the int *toBeAdded*

**Predicates for method *updatePosition*:**

| Criteria                                                  | Predicate          |
| --------                                                  | ---------          |
| Sign of the Integer *productId*                           |  > 0               |
|                                                           |  <= 0              | 
| User logged in has authority                              |  True              |
|                                                           |  False             |
| Length of the String *newPos*                             |  > 0               |
|                                                           |  = 0 ("" or null)  |
| Validity of the String parameter *newPos*                 |  Valid             |
|                                                           |  Invalid           |
| Existence of product with matching *productId* in db      |  True              |
|                                                           |  False             |
| Existence of a product with matching location as *newPos* |  True              |
|                                                           |  False             |
**Boundaries**:

| Criteria                                             | Boundary values    |
| --------                                             | ---------------    |

**Combination of predicates**:

|Sign of the Integer *productId*|Length of the String *newPos*|User logged in has authority|Validity of the String parameter *newPos*|Existence of product with matching *productId* in db|Existence of a product with matching location as *newPos*| Valid / Invalid | Description of the test case | JUnit test case UpdatePositionTest|
|-------|-------|-------|-------|-------|-------|-------|-------|-------|
| > 0   | True  | > 0   | Valid | True  | True  |Valid  | T1(1, "3 aisle 2") &rarr; true                                    |testCase1       |
| "     | "     | "     | "     | "     | False |Valid  | T2(3, "17 corridor 42") &rarr; false (location already assigned)  |testCase2       |
| "     | "     | "     | "     | False | -     |Valid  | T3(1000, "4 aisle 4") &rarr; false (no matching id)               |testCase3       |
| "     | "     | "     |Invalid| -     | -     |Invalid| T4(1, "corridor") &rarr; error                                    |testCase4       |
| "     | "     | =0    | -     | -     | -     |Invalid| T5(1, "") &rarr; true                                             |testCase5       |
| "     | False | -     | -     | -     | -     |Valid  | Unauthorized user logged in &rarr; error                          |testCase6       |
| <= 0  | -     | -     | -     | -     | -     |Invalid| T7(-1, "4 aisle 4") &rarr; error                                  |testCase7       |






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
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||




# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|||
|||
||||

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|||||
|||||
||||||



