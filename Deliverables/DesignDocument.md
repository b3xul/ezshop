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
- [FR1](#fr1)
- [FR3](#fr3)
- [notes](#notes)

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
    issueReorder()
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

Shop -- EZShopInterface
Shop -- "*" User 
class AccountBook{
    debits: Debit
    credits: Credit
    
}
AccountBook -- Shop


class Debit{
    orders: List<Order>
    returns: List<ReturnTransaction>
}
class Credit{
    sales: List<SaleTransactions>
}

AccountBook --  Credit
AccountBook --  Debit


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
    
}
SaleTransaction - "*" ProductType

class Quantity {
    quantity
}
(SaleTransaction, ProductType)  .. Quantity

class LoyaltyCard {
    ID
    points
}

class Customer {
    name
    surname
    customerID
    loyaltyCard: LoyaltyCard
    
    
}

LoyaltyCard "0..1" - Customer

SaleTransaction "*" --  Customer

class Position {
    aisleID
    rackID
    levelID
}

ProductType - "0..1" Position

ProductType -- "*" Product : describes

class Order {
  supplier
  pricePerUnit
  productType: ProductType
  quantity
  status
  orderID??
  date
}

Order "*" - ProductType
Order "*"-- Debit


class ReturnTransaction {
  quantity
  productType: ProductType
  returnedValue
  date
  saleTransaction: SaleTransaction
  
}

ReturnTransaction "*" - SaleTransaction
ReturnTransaction "*" - ProductType
ReturnTransaction "*"-- Debit
SaleTransaction "*"-- Credit
Customer "*" -- Shop

@enduml
```

# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>


# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

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


# notes
- servono 3 package: data, model, exceptions
- ci daranno la classe Customer, User, ProductType e le Exceptions perche' ci sono nell'API (non serve metterle nel nostro class diagram perche' tanto non sappiamo come sono fatte)
- volendo si possono aggiungere altre exception al package Exception
- sequence diagrams nomi funzioni
- Sequence diagrams anche solo per gli scenari piu' importanti, l'importante e' avere tutte le classi che servono
- differenza tra ticket e saleTransaction e' che si puo' fare rollback della transazione ma non del ticket, decidiamo noi se avere una classe apposta per il ticket o meno
- scrivere tipo di collezioni usate
- nota vicino alle classi persistenti, non e' necessario mostrare le interazioni con lo storage
- Shop implements API interface
- No implementazione delle relationship, solo frecce e indicare collection usata 
- Nel sequence diagram basta function name senza parametri\
- Issue: ID of customer integer o string?
- Per gestire file di credit card per ora basta una classe credit card
- estimation approach non scrivere niente
