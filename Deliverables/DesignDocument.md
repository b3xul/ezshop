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
- [FR1](#fr1)
    - [UC3](#uc3)
    - [Scenario 6.2](#scenario-62)
- [FR3](#fr3)
- [FR7](#fr7)
- [UC9 - Accounting](#uc9---accounting)
  - [Scenario 9-1](#scenario-9-1)

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

class EZShopInterface{
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
    username
    password
    role
    userID
}

Shop -down- EZShopInterface
Shop -down- "*" User 
class AccountBook{
    operations: List<BalanceOperation>
    +List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)
    +double computeBalance()
}
AccountBook - Shop
AccountBook - BalanceOperation


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

Shop - "*" ProductType

class SaleTransaction {
    ID 
    date
    time
    cost
    paymentType
    discount rate
    productTypes: Map<ProductType>< Quantity>
    customer: Customer
    +double receiveCashPayment(Integer transactionId, double cash)
    +boolean receiveCreditCardPayment(Integer transactionId, String creditCard)
}
SaleTransaction - "*" ProductType

class Customer {
    name
    surname
    customerID
    loyaltyCard: LoyaltyCard
}
Customer "*" - Shop
class LoyaltyCard {
    ID
    points
}

LoyaltyCard "0..1" - Customer

SaleTransaction "*" - "0..1" LoyaltyCard

class Position {
    aisleID
    rackID
    levelID
}

ProductType - "0..1" Position

class Order {
  supplier
  pricePerUnit
  productType: ProductType
  quantity
  status
  orderID??
}

Order "*" - ProductType
Order -down-|> Debit


class ReturnTransaction {
  -returnID
  quantity
  productType: ProductType
  returnedValue
  saleTransaction: SaleTransaction
  +double returnCashPayment(Integer returnId)
  +double returnCreditCardPayment(Integer returnId, String creditCard)
}

class BalanceOperation{
    description
    date
    +boolean recordBalanceUpdate(double toBeAdded)
}
ReturnTransaction "*" - SaleTransaction
ReturnTransaction "*" - ProductType
ReturnTransaction -down-|> Debit
SaleTransaction -down-|> Credit
Debit -down-|> BalanceOperation
Credit -down-|> BalanceOperation


@enduml
```

class Quantity {
    quantity
}
(SaleTransaction, ProductType)  .. Quantity
???

# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>


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

# FR1

```plantuml
@startuml

participant Administrator
participant Shop
participant User

autonumber 
Administrator -> Shop
Shop -> User: createUser()
Shop -> User: DeleteUser()
Shop -> User: UpdateUserRights()

@enduml
```

### UC3
```plantuml
@startuml

participant Shop
participant Order
participant User

autonumber 
Shop -> Order: issueOrder()
Order --> Shop: Order is issued
Shop -> Shop: getAllOrders()
Shop --> User: search for the order by scrolling manually the list
Shop -> Order: payOrder()
Order --> Shop: Order's state updated to PAYED
Shop -> Shop: recordOrderArrival()
Shop -> ProductType: getProductTypeByBarCode()
ProductType --> Shop: ProductType
Shop -> ProductType: updateQuantity()
ProductType --> Shop: ProductType with Quantity updated

@enduml
```


### Scenario 6.2

```plantuml
@startuml

participant User
participant Shop
participant SaleTransaction
participant ProductType
participant AccounBook

autonumber
Shop -> SaleTransaction : startSaleTransaction()
SaleTransaction --> Shop: return unique transaction's ID
Shop -> ProductType : getProductTypeByBarCode()
ProductType --> Shop: return ProductType
Shop -> SaleTransaction : addProductToSale()
Shop -> ProductType : UpdateQuantity()
Shop -> SaleTransaction : applyDiscountRateToProduct()
Shop -> SaleTransaction : endSaleTransaction()
Shop -> User: ask for payment type
User -> Shop : payment type credit card
Shop -> Shop : receiveCreditCardPayment() 
Shop -> User: ask confirmation
User -> Shop: confirm
Shop -> AccounBook: recordBalanceUpdate()

@enduml
```

# FR3 
```plantuml
@startuml

participant User 
participant Shop
participant ProductType

autonumber
User -> Shop
Shop -> ProductType: createProductType()

@enduml
```

# FR7 
```plantuml
@startuml

participant User 
participant Shop
participant ProductType

autonumber
User -> Shop
Shop -> ProductType: createProductType()

@enduml
```

# UC9 - Accounting 
## Scenario 9-1

```plantuml
@startuml

participant Manager 
participant Shop
participant accountBook

autonumber
Manager --> Shop : select start and end date
Shop -> accountBook: getCreditsAndDebits()
accountBook -> Shop: List<BalanceOperation>
Shop --> Manager: displayOperations()

@enduml
```