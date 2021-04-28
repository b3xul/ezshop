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

```
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
left to right direction
class EZShop{
    API Interface
}
class Shop{
    reset()
    createUser()
    deleteUser()
    getAllUsers()
    getUser()
    login()
    logout()
    createProductType()
    deleteProductType()
    getAllProductTypes()
    recordOrderArrival()
    getAllOrders()
    defineCustomer()
    deleteCustomer()
    getAllCustomer()
    createCard()
    attachCardToCustomer()
    startSaleTransaction()
    endSaleTransaction()
    startReturnTransaction()
    endReturnTransaction()
    deleteReturnTransaction()
    receiveCashPayment()
    receiveCreditCardPayment()
    returnCashPayment()
    returnCreditCardPayment()
    getCustomer()
    getProductTypeByBarCode()
    getProductTypeByDesription()
}
class User{
    name
    surname
    password
    role
    userID??
    updateUsersRights()
    modifyUser()??
}
Shop -- EZShop
Shop -- "*" User 
class AccountBook{
    recordBalanceUpdate()
    getCreditsAndDebits()
    computeBalance()
}
AccountBook - Shop
class FinancialTransaction {
 description
 amount
 date
}
AccountBook -- "*" FinancialTransaction

class Credit 
class Debit

Credit --|> FinancialTransaction
Debit --|> FinancialTransaction

class Order
class Sale
class Return

Order --|> Debit


class ProductType{
    barCode
    description
    sellPrice
    quantity
    discountRate
    notes
    
    updateProduct()
    updateQuantity()
    updatePosition()
    
}

Shop - "*" ProductType

class SaleTransaction {
    ID 
    date
    time
    cost
    paymentType
    discount rate
    addProductToSale()
    deleteProductFromSale()
    applyDiscountRateToProduct()
    applyDiscountRateToSale()
    computePointForSale()
}
SaleTransaction - "*" ProductType

class Quantity {
    quantity
}
(SaleTransaction, ProductType)  .. Quantity

class LoyaltyCard {
    ID
    points
    modifyPointsOnCard()
}

class Customer {
    name
    surname
    ID
    modifyCustomer()
}

LoyaltyCard "0..1" - Customer

SaleTransaction "*" -- "0..1" LoyaltyCard

class Product {
    
}

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
  quantity
  status
  orderID??
  issueReorder()
  payOrderFor()
  payOrder()
}

Order "*" - ProductType

class ReturnTransaction {
  quantity
  returnedValue
  returnProduct()
}

ReturnTransaction "*" - SaleTransaction
ReturnTransaction "*" - ProductType
ReturnTransaction --|> Debit
SaleTransaction --|> Credit

note "ID is a number on 10 digits " as N1  
N1 .. LoyaltyCard
note "bar code is a number on 12 to 14  digits, compliant to GTIN specifications, see  https://www.gs1.org/services/how-calculate-check-digit-manually " as N2  
N2 .. ProductType
note "ID is a unique identifier of a transaction,  printed on the receipt (ticket number) " as N3
N3 .. SaleTransaction

```

# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>











# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

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
