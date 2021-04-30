# Design Document 

Authors: Elia Fontana, Andrea Palomba, Leonardo Perugini, Francesco Sattolo

Date: 27/04/2021

Version: 1.0


# Contents

- [Design Document](#design-document)
- [Contents](#contents)
- [Instructions](#instructions)
- [High level design](#high-level-design)
- [Low level design](#low-level-design)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)
- [UC1 - Manage products](#uc1---manage-products)
    - [Scenario 1.1 - Create product type X](#scenario-11---create-product-type-x)
    - [Scenario 1.2 - Modify product type location](#scenario-12---modify-product-type-location)
    - [Scenario 1.3 - Modify product type price per unit](#scenario-13---modify-product-type-price-per-unit)
- [UC2 - Manage users and rights](#uc2---manage-users-and-rights)
    - [Scenario 2.1 Create user and define rights](#scenario-21-create-user-and-define-rights)
    - [Scenario 2.2 Delete user](#scenario-22-delete-user)
- [UC3 - Manage inventory and orders](#uc3---manage-inventory-and-orders)
    - [Scenario 3.1 - Order of product type X issued](#scenario-31---order-of-product-type-x-issued)
    - [Scenario 3.2 - Order of product type X payed](#scenario-32---order-of-product-type-x-payed)
    - [Scenario 3.3 - Record order of product type X arrival](#scenario-33---record-order-of-product-type-x-arrival)
- [UC4 - Manage Customers and  Cards](#uc4---manage-customers-and--cards)
    - [Scenario 4.1 - Create customer record](#scenario-41---create-customer-record)
    - [Scenario 4.2 - Attach Loyalty card to customer record](#scenario-42---attach-loyalty-card-to-customer-record)
    - [Scenario 4.4 - Update customer record](#scenario-44---update-customer-record)
- [UC6 - Manage sale transaction](#uc6---manage-sale-transaction)
    - [Scenario 6.2 - Sale of product type X with product discount (credit card)](#scenario-62---sale-of-product-type-x-with-product-discount-credit-card)
    - [Scenario 6.4 - Sale of product type X with Loyalty Card update (cash)](#scenario-64---sale-of-product-type-x-with-loyalty-card-update-cash)
- [UC8 - Manage return transaction](#uc8---manage-return-transaction)
    - [Scenario 8.1/8.2 - Return transaction of product type X completed (credit card/cash)](#scenario-8182---return-transaction-of-product-type-x-completed-credit-cardcash)
- [UC9 - Accounting](#uc9---accounting)
  - [Scenario 9.1 - List credits and debits](#scenario-91---list-credits-and-debits)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

<discuss architectural styles used, if any>
Architectures:
- Model View Controller
- Stand alone Application
- 3 tier layered

<report package diagram>

```plantuml
@startuml
package exceptions
package "Persistent data and API" as data
package "Application logic and model" as model

model ..> data
model ..> exceptions

@enduml
```

# Low level design

<for each package, report class diagram>

```plantuml
@startuml

interface EZShopInterface{
    API Interface
}

class Shop{
    users: List<User>
    productTypes: List<ProductType>
    customers: List<Customer>
    accounting: AccountBook

    reset()
    login()
    logout()
    createUser()
    deleteUser()
    getAllUsers()
    getUser()
    updateUsersRights()
    createProductType()
    deleteProductType()
    getAllProductTypes()
    updateProduct()
    updateQuantity()
    updatePosition()
    recordOrderArrival()
    getAllOrders()
    defineCustomer()
    deleteCustomer()
    getAllCustomer()
    createCard()
    attachCardToCustomer()
    startSaleTransaction()
    addProductToSale()
    deleteProductFromSale()
    applyDiscountRateToProduct()
    applyDiscountRateToSale()
    computePointForSale()
    modifyPointsOnCard()
    endSaleTransaction()
    startReturnTransaction()
    endReturnTransaction()
    deleteReturnTransaction()
    receiveCashPayment()
    receiveCreditCardPayment()
    returnCashPayment()
    returnCreditCardPayment()
    getCustomer()
    modifyCustomer()
    issueOrder()
    payOrderFor()
    payOrder()
    returnProduct()
    getProductTypeByBarCode()
    getProductTypeByDesription()
    recordBalanceUpdate()
    getCreditsAndDebits()
    computeBalance()
}
class User{
    userID
    username
    password
    role
}

class AccountBook{
    operations: List<BalanceOperation>
}

class Debit{
}

class Credit{
}

class ProductType{
    barCode
    description
    sellPrice
    quantity
    discountRate
    notes
    position: Position
}

class SaleTransaction {
    transactionID 
    date
    time
    cost
    paymentType
    discount rate
    productTypes: Map[<ProductType>: quantityInSale]
    customer: Customer
}

class Customer {
    name
    surname
    customerID
    loyaltyCard: LoyaltyCard
}
class LoyaltyCard {
    ID
    points
}

class Position {
    aisleID
    rackID
    levelID
}

class Order {
    orderID
    supplier
    pricePerUnit
    productType: ProductType
    quantity
    status
}

class ReturnTransaction {
    returnID
    quantity
    productType: ProductType
    returnedValue
    saleTransaction: SaleTransaction
}

class BalanceOperation{
    description
    date
}

Shop -up- EZShopInterface
AccountBook -right- Shop
Customer "*" -right- Shop
AccountBook -[hidden]- Customer
Shop -up- "*" User 
Shop -down- "*" ProductType
ProductType -up- "0..1" Position
ProductType "*" -down-  SaleTransaction

SaleTransaction -left- "*" ReturnTransaction
SaleTransaction -up-|> Credit
SaleTransaction "*" -right- "0..1" LoyaltyCard
LoyaltyCard "0..1" -up- Customer

ReturnTransaction -up-|> Debit
Order -up-|> Debit
Debit -right-|> BalanceOperation
Credit -[hidden]- Position
Credit -up-|> BalanceOperation
AccountBook -left- BalanceOperation
Order "*" - ProductType
ReturnTransaction "*" - ProductType

@enduml
```

# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>
|     | Shop | User | ProductType | Customer | AccountBook | LoyaltyCard | SaleTransaction | Position | Quantity | ReturnTransaction | Order | Debit | Credit | BalanceOperation |
| --- | ---- | ---- | ----------- | -------- | ----------- | ----------- | --------------- | -------- | -------- | ----------------- | ----- | ----- | ------ | ---------------- |
| FR1 | X    | X    |             |          |             |             |                 |          |          |                   |       |       |        |                  |
| FR3 | X    |      | X           |          |             |             |                 | X        |          |                   |       |       |        |                  |
| FR4 | X    |      | X           |          |             |             |                 | X        |          |                   | X     |       |        |                  |
| FR5 | X    |      |             | X        |             | X           |                 |          |          |                   |       |       |        |                  |
| FR6 | X    |      | X           |          | X           |             | X               |          |          | X                 |       |       |        |                  |
| FR7 | X    |      |             |          |             |             | X               |          |          | X                 |       |       |        |                  |
| FR8 | X    |      |             |          | X           |             |                 |          |          |                   |       | X     | X      | X                |


# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

# UC1 - Manage products
### Scenario 1.1 - Create product type X
```plantuml
@startuml

actor Manager

participant Shop
participant ProductType 
participant Position

autonumber 

Manager -> Shop :Insert description, barcode, price per unit, product notes, location
Shop --> Manager: ask confirmation
Manager -> Shop: confirm
Shop -> ProductType: createProductType()
ProductType --> Shop: return ProductType's ID
Shop -> Position: updatePosition()
Position --> Shop: ProductType with Position assigned


@enduml
```
### Scenario 1.2 - Modify product type location
```plantuml
@startuml

actor Manager

participant Shop
participant ProductType 
participant Position

autonumber
Manager -> Shop : Insert barcode
Shop -> ProductType: getProductTypeByBarCode()
ProductType --> Shop: return ProductType
Shop --> Manager: display ProductType
Manager -> Shop : Select ProductType record and insert new location
Shop -> Position: updatePosition()
Position --> Shop: ProductType with Position updated

@enduml
```

### Scenario 1.3 - Modify product type price per unit
```plantuml
@startuml

actor Manager

participant Shop
participant ProductType 

autonumber

Manager -> Shop : Insert barcode
Shop -> ProductType: getProductTypeByBarCode()
ProductType --> Shop: return ProductType
Shop --> Manager: display ProductType
Manager -> Shop : Select ProductType record and insert new price per unit>0
Shop --> Manager: ask confirmation
Manager -> Shop: confirm
Shop -> ProductType: updateProduct()
ProductType --> Shop: ProductType with Price per unit updated

@enduml
```

# UC2 - Manage users and rights
### Scenario 2.1 Create user and define rights

```plantuml
@startuml

actor Administrator
participant Shop
participant User

autonumber 
Administrator -> Shop : Define credentials and access rights
Shop -> User: createUser()
User --> Shop: User created

@enduml
```

### Scenario 2.2 Delete user

```plantuml
@startuml

actor Administrator
participant Shop
participant User

autonumber
Shop --> Administrator: Display users
Administrator -> Shop : Select user ID
Shop -> User: deleteUser()
Shop --> Administrator: deleted user

@enduml
```

# UC3 - Manage inventory and orders

### Scenario 3.1 - Order of product type X issued

```plantuml
@startuml

actor Manager
participant Shop
participant Order

autonumber 
Manager -> Shop: Insert productCode, quantity, price per unit
Shop -> Order: issueOrder()
Order --> Shop: Order is recorded in the system in ISSUED state

@enduml
```

### Scenario 3.2 - Order of product type X payed

```plantuml
@startuml

actor Manager
participant Shop
participant Order
participant AccountBook

autonumber 
Manager -> Shop: Insert productCode, quantity, price per unit
Shop -> Order: issueOrder()
Order --> Shop: Order is recorded in the system in ISSUED state
Shop --> Manager: getIssuedOrders()
Manager --> Shop: selects the order by manually scrolling the list
Shop -> Order: payOrder()
Shop -> AccountBook: recordBalanceUpdate()
Order --> Shop: Order's state updated to PAYED

@enduml
```

### Scenario 3.3 - Record order of product type X arrival

```plantuml
@startuml

actor Manager
participant Shop
participant Order

autonumber 
Manager -> Shop: Insert orderID
Shop -> Order: recordOrderArrival()
Order --> Shop: Order's state updated to COMPLETED
Shop -> ProductType: getProductTypeByBarCode()
ProductType --> Shop: ProductType
Shop -> ProductType: updateQuantity()
ProductType --> Shop: ProductType with Quantity updated

@enduml
```
# UC4 - Manage Customers and  Cards
### Scenario 4.1 - Create customer record

```plantuml
@startuml

actor User
participant Shop
participant Customer

autonumber 
User -> Shop: insert customer's personal data
Shop -> Customer: defineCustomer()
Customer --> Shop: Customer is created
Shop --> User: ask confirmation
User -> Shop: confirm

@enduml
```
### Scenario 4.2 - Attach Loyalty card to customer record

```plantuml
@startuml

participant Shop
participant LoyaltyCard
participant Customer

Shop -> LoyaltyCard: createCard()
LoyaltyCard --> Shop: return LoyaltyCard's code
Shop -> Customer: attachCardToCustomer()

@enduml
```

### Scenario 4.4 - Update customer record

```plantuml
@startuml

actor User
participant Shop
participant Customer

Shop -> Customer: getCustomer()
Customer --> Shop: return Customer
Shop --> User: displayCustomer()
User --> Shop: insert new data
Shop -> Customer: modifyCustomer()

@enduml
```

# UC5 - Authenticate, authorize
### Scenario 5.1/5.2 - Login/Logout

```plantuml
@startuml

actor Actor
participant Shop

autonumber 
Actor -> Shop: insert username and password
Shop -> Shop: login()
Shop --> Actor: show the functionalities offered by the access priviledges
Actor -> Shop: want to logout
Shop -> Shop: logout()
Shop --> Actor: show the login page

@enduml
```


# UC6 - Manage sale transaction
### Scenario 6.2 - Sale of product type X with product discount (credit card)

```plantuml
@startuml

actor User
participant Shop
participant SaleTransaction
participant ProductType
participant AccountBook

autonumber
Shop -> SaleTransaction : startSaleTransaction()
SaleTransaction --> Shop: return unique transaction's ID
User -> Shop: Read product barcode and insert quantity
Shop -> ProductType : getProductTypeByBarCode()
ProductType --> Shop: return ProductType
Shop -> SaleTransaction : addProductToSale()
Shop -> ProductType : UpdateQuantity()
Shop -> SaleTransaction : applyDiscountRateToProduct()
Shop -> SaleTransaction : endSaleTransaction()
Shop -> User: ask for payment type
User -> Shop : Read credit card number
Shop -> SaleTransaction : receiveCreditCardPayment() 
Shop -> User: ask to credit sale price
User -> Shop: confirm
Shop -> AccountBook: recordBalanceUpdate()

@enduml
```

### Scenario 6.4 - Sale of product type X with Loyalty Card update (cash)

```plantuml
@startuml

actor User
participant Shop
participant SaleTransaction
participant ProductType
participant LoyaltyCard
participant AccountBook

autonumber
Shop -> SaleTransaction : startSaleTransaction()
SaleTransaction --> Shop: return unique transaction's ID
User -> Shop: Read product barcode and insert quantity
Shop -> ProductType : getProductTypeByBarCode()
ProductType --> Shop: return ProductType
Shop -> SaleTransaction : addProductToSale()
Shop -> ProductType : UpdateQuantity()
Shop -> SaleTransaction : applyDiscountRateToProduct()
Shop -> SaleTransaction : endSaleTransaction()
User -> Shop: Read loyalty card serial number
Shop -> User: ask for payment type
User -> Shop: Collect banknotes and coins
Shop -> SaleTransaction: computePointsForSale()
SaleTransaction --> Shop: return points to add
Shop -> LoyaltyCard: modifyPointsOnCard()
LoyaltyCard --> Shop: updated points
Shop -> AccountBook: recordBalanceUpdate()

@enduml
```
# UC8 - Manage return transaction
### Scenario 8.1/8.2 - Return transaction of product type X completed (credit card/cash)

```plantuml
@startuml

actor User
participant Shop
participant ReturnTransaction
participant ProductType
participant AccountBook

autonumber 
User -> Shop: insert transaction ID
Shop -> ReturnTransaction: startReturnTransaction()
ReturnTransaction --> Shop: return ReturnTransaction unique ID
Shop -> ProductType: getProductTypeByBarcode()
ProductType --> Shop: return ProductType
Shop -> ReturnTransaction: returnProduct()
Shop -> ProductType: updateQuantity()
Shop -> ReturnTransaction: returnCashPayment()
ReturnTransaction --> Shop: credit card/cash amount to return
Shop --> User: display amount to return
Shop -> ReturnTransaction: endReturnTransaction()
Shop -> AccountBook: recordBalanceUpdate()

@enduml
```
# UC9 - Accounting 
## Scenario 9.1 - List credits and debits

```plantuml
@startuml

actor Manager 
participant Shop
participant accountBook

autonumber
Manager -> Shop : select start and end date
Shop -> accountBook: getCreditsAndDebits()
accountBook --> Shop: List<BalanceOperation>
Shop --> Manager: display Operations

@enduml
```
