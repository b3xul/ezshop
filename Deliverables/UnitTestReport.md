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

#### setId
**Criteria for method *setId*:**

 - Validity of the Integer *id*

**Predicates for method *setId*:**

| Criteria                                             | Predicate |
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

**Combination of predicates**:

|Validity of the String *description*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setProductDescription("new desc");     |testCase3       |

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

|Validity of the String *varCode*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setBarCode("6253478956438");     |testCase4      |

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

|Validity of the String *note*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setNote("new note");     |testCase6     |

#### setLocation
**Criteria for method *setLocation*:**

 - Validity of the String *location*

**Predicates for method *setLocation*:**

| Criteria                                             | Predicate |
| --------                                             | --------- |
| Validity of the String *location*                    |  Valid    |

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the String *location*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |product.setLocation("3 aisle 3");   |testCase7      |

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

**Boundaries**:

| Criteria                          | Boundary values    |
| --------                          | ---------------    |

**Combination of predicates**:

|Validity of the Integer *discountRate*| Valid / Invalid | Description of the test case | JUnit test case ProductTypeClassTest|
|-------|-------|-------|-------|
|Valid  |Valid  |ProductTypeImpl product2 = new ProductTypeImpl();  product2.setDiscountRate(20.0);  |testCase7      |


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



