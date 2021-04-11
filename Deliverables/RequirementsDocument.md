# Requirements Document 

Authors: Elia Fontana, Andrea Palomba, Leonardo Perugini, Francesco Sattolo

Date: 06/04/2020

Version: 1.0

# Contents

- [Requirements Document](#requirements-document)
- [Contents](#contents)
- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
  - [Context Diagram](#context-diagram)
  - [Interfaces](#interfaces)
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
  - [Functional Requirements](#functional-requirements)
  - [Non Functional Requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
  - [Use case diagram](#use-case-diagram)
    - [Use case 1, FR1 customer buys item](#use-case-1-fr1-customer-buys-item)
        - [Scenario 1.1, customer with fidelity card](#scenario-11-customer-with-fidelity-card)
        - [Scenario 1.2, payment failure](#scenario-12-payment-failure)
    - [Use case 2, FR2.1, shop owner/shop employee updates inventory](#use-case-2-fr21-shop-ownershop-employee-updates-inventory)
        - [Scenario2.1, order arrives](#scenario21-order-arrives)
        - [Scenario2.2, sale completed](#scenario22-sale-completed)
    - [Use case 3, FR2.3, shop owner/inventory manager searches for a product](#use-case-3-fr23-shop-ownerinventory-manager-searches-for-a-product)
    - [Use case 3, FR3, shop owner applies a promotion to a certain item](#use-case-3-fr3-shop-owner-applies-a-promotion-to-a-certain-item)
    - [Use case 4, FR3 shop owner handles item to catalogue](#use-case-4-fr3-shop-owner-handles-item-to-catalogue)
        - [Scenario4.1, FR3 shop owner remove an item from catalogue](#scenario41-fr3-shop-owner-remove-an-item-from-catalogue)
        - [Scenario4.2, FR3 shop owner add an item to catalogue](#scenario42-fr3-shop-owner-add-an-item-to-catalogue)
    - [Use case 5, FR4, shop owner/inventory manager places an order](#use-case-5-fr4-shop-ownerinventory-manager-places-an-order)
        - [Scenario5.1, payment failure](#scenario51-payment-failure)
    - [Use case , Send customized offers](#use-case--send-customized-offers)
    - [Use case , Add fidelity card](#use-case--add-fidelity-card)
    - [Use case 6, FR6, shop owner changes employee's contract](#use-case-6-fr6-shop-owner-changes-employees-contract)
    - [Use case 6, FR6, shop owner adds/removes employee](#use-case-6-fr6-shop-owner-addsremoves-employee)
    - [Use case 7, FR7.1 log information about the shop (rent,maintenance,advertisement costs)](#use-case-7-fr71-log-information-about-the-shop-rentmaintenanceadvertisement-costs)
    - [Use case 8, FR7.2 analize profits/losses](#use-case-8-fr72-analize-profitslosses)
- [Glossary](#glossary)
- [System Design](#system-design)
- [Deployment Diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting

# Stakeholders

| Stakeholder name          |                                                                 Description                                                                  |
| ------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------: |
| Owner/Manager of the shop |                                      The person that needs to manage all of the shop related activities                                      |
| Cashiers                  |                                                                                                                                              |
| Inventory manager         |                                                                                                                                              |
| Accounting administrator  |                                                                                                                                              |
| Supplier                  |                                                           Sells items to the owner                                                           |
| Customer                  |                                                           Buys items from the shop                                                           |
| Computer Engineer         | Create and maintain the application (us) and support the user of the application in case of problems (IT, database, security administrators) |

# Context Diagram and interfaces

## Context Diagram

```plantuml
 @startuml
rectangle Application {
    (EzShop)
}

:Owner of the shop: --> (EzShop)

:Shop employee: --> (EzShop)
:Inventory manager: --> :Shop employee:
:Cashier: --> :Shop employee:
:Accounting administrator: --> :Shop employee:

:payment terminal:-->(EzShop)

@enduml
```

## Interfaces

| Actor                     |      Logical Interface       |          Physical Interface |
| ------------------------- | :--------------------------: | --------------------------: |
| Owner/Manager of the shop |   Application GUI (admin)    | Screen keyboard mouse on PC |
| Cashier                   |  Application GUI (cashier)   | Screen keyboard mouse on PC |
| Inventory manager         | Application GUI (inventory)  | Screen keyboard mouse on PC |
| Accounting administrator  | Application GUI (accounting) | Screen keyboard mouse on PC |
| Payment terminal          |         payment API          |               internet link |

# Stories and personas
\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as scenarios in use cases>

Owner: ...
Shop employee: ...

# Functional and non functional requirements

## Functional Requirements

| ID    |                                           Description                                           |
| ----- | :---------------------------------------------------------------------------------------------: |
| FR1   |                                          manage sales                                           |
| FR1.1 | log sales data (type, amount, price of items/discounts, type of customer, time of transaction)  |
| FR1.2 | Payment terminal ( credit card reader) integration (discount if registered customer, log sales) |
| FR2   |                                        manage inventory                                         |
| FR2.1 |                          log inventory status (type, amount of items)                           |
| FR2.2 |                               Add/Remove items to/from inventory                                |
| FR2.3 |                                 Search through items (ordered)                                  |
| FR2.4 |                             Notice if item quantity under threshold                             |
| FR3   |                                        manage catalogue                                         |
| FR3.1 |                                 log information about promotion                                 |
| FR3.2 |                               Add/Remove items to/from catalogue                                |
| FR3.3 |                                 Search through items (ordered)                                  |
| FR4   |                                          manage orders                                          |
| FR4.1 |             log information about suppliers (items costs, estimated delivery date)              |
| FR4.2 |                                    Add/Remove items to order                                    |
| FR4.3 |                                 Send order and pay the supplier                                 |
| FR5   |                                        manage customers                                         |
| FR5.1 |                                     register fidelity cards                                     |
| FR5.2 |                             Send customized offers (advertisements)                             |
| FR6   |                 manage employees (information, role, salary costs, timetables)                  |
| FR6.1 |                                       Add/Remove Employee                                       |
| FR6.2 |                                   Update Employee information                                   |
| FR7   |                                        manage accounting                                        |
| FR7.1 |              log information about the shop (rent,maintenance,advertisement costs)              |
| FR7.2 |                                     analize profits/losses                                      |

## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID   | Type (efficiency, reliability, .. see iso 9126) |                                     Description                                     |   Refers to FR |
| ---- | :---------------------------------------------: | :---------------------------------------------------------------------------------: | -------------: |
| NFR1 |                    usability                    | The owner must learn to use all the functions within 30 minutes of training session |            all |
| NFR2 |                   efficiency                    |                   every function must have <0.1 ms response time                    | easy functions |
| NFR2 |                   efficiency                    |                     every function must have <1 s response time                     | hard functions |
| NFR3 |                 maintainability                 |                              add new functionalities?                               |            all |
| NFR4 |                   reliability                   |                      bugfixes must be completed within 1 week                       |            all |
| NFR4 |                  availability                   |                                     uptime 99%                                      |            all |
| NFR5 |                    security                     |            Restrict different views of the GUI only to authorized people            |            all |
| NFR6 |                     domain                      |               Possibility to change currency (euro,dollar), language                |                |

# Use case diagram and use cases


## Use case diagram

```plantuml
@startuml
:Owner/Manager of the shop:
:Shop employee: 
:Customer:
:Supplier:

rectangle application {
  :Owner/Manager of the shop: --> (FR4: manage accounting)
  :Owner/Manager of the shop: --> (FR1: manage sales)
  :Shop employee: --> (FR1: manage sales)
  :Customer: <--> :Shop employee:
  :Owner/Manager of the shop: --> (FR2: manage inventory)
  (FR2: manage inventory) --> :Supplier:
  :Owner/Manager of the shop: --> (FR3: manage customers)
  (FR3: manage customers) --> :Customer:
}
@enduml
```

### Use case 1, FR1 customer buys item

| Actors Involved  |                                 shop employee                                 |
| ---------------- | :---------------------------------------------------------------------------: |
| Precondition     |                               item in inventory                               |
| Post condition   | item sold, amount of items in shop updated, gains updated, transaction logged |
|                  |                                                                               |
| Nominal Scenario |                                                                               |
|                  |                        1. Shop employee scans products                        |
|                  |                        2. Applying general promotions                         |
|                  |                               3. Customer pays                                |
|                  |                4. Application updates amount of items in shop                 |
|                  |                              5. Update database                               |
|                  |                                                                               |
| Variant1:        |                          customer with fidelity card                          |
| Variant2:        |                                payment failure                                |

##### Scenario 1.1, customer with fidelity card 

| Scenario ID: SC1 | Corresponds to UC 1                                                           |
| ---------------- | :---------------------------------------------------------------------------- |
| Description      | customer with fidelity card                                                   |
| Precondition     | item in shop, customer already registered in the system                       |
| Postcondition    | item sold, amount of items in shop updated, gains updated, transaction logged |
| Step#            | Step description                                                              |
| 1                | Accept client fidelity card                                                   |
| 2                | shop employee scans products                                                  |
| 3                | Apply general promotions                                                      |
| 4                | apply customized promotions                                                   |
| 5                | payment success                                                               |
| 6                | Update amount of items in shop and gains                                      |
| 7                | Update (add or subtract) customer's points                                    |
| 8                | Update customer's purchase history                                            |

##### Scenario 1.2, payment failure

| Scenario ID: SC2 | Corresponds to UC 1          |
| ---------------- | :--------------------------- |
| Description      | payment failure              |
| Precondition     | item in shop                 |
| Postcondition    | transaction logged           |
| Step#            | Step description             |
| 1                | shop employee scans products |
| 2                | Applying general promotions  |
| 3                | payment failure              |
| 4                | Transaction aborted          |

### Use case 2, FR2.1, shop owner/shop employee updates inventory

| Actors Involved  |                 shop owner, inventory manager                  |
| ---------------- | :------------------------------------------------------------: |
| Precondition     |      inventory in consistent state, item is in catalogue       |
| Post condition   |                  inventory updated correctly                   |
|                  |                                                                |
| Nominal Scenario |                                                                |
|                  | 1. Shop owner/inventory manager searches through items (FR2.3) |
|                  |                                                                |
| Variant1         |                         order arrives                          |
| Variant2         |                         sale completed                         |

##### Scenario2.1, order arrives
| Scenario ID: SC2 | Corresponds to UC 1                                                      |
| ---------------- | :----------------------------------------------------------------------- |
| Description      | order arrives                                                            |
| Precondition     | order arrives                                                            |
| Postcondition    |                                                                          |
| Step#            | Step description                                                         |
| 1                | Shop owner/inventory manager searches through items (FR2.3)              |
| 2                | Shop owner/inventory manager adds/updates items in the inventory (FR2.2) |

##### Scenario2.2, sale completed

| Scenario ID: SC2 | Corresponds to UC 1                                                                                                    |
| ---------------- | :--------------------------------------------------------------------------------------------------------------------- |
| Description      | sale completed                                                                                                         |
| Precondition     | sale completed                                                                                                         |
| Postcondition    |                                                                                                                        |
| Step#            | Step description                                                                                                       |
| 1                | Shop owner/inventory manager searches through items (FR2.3)                                                            |
| 2                | Shop owner/inventory manager removes items in the inventory (FR2.2)                                                    |
| 3                | If one item amount drop under a certain threshold, Application sends a notification to the shop owner or shop employee |

### Use case 3, FR2.3, shop owner/inventory manager searches for a product

| Actors Involved  |             shop owner, inventory manager              |
| ---------------- | :----------------------------------------------------: |
| Precondition     |  inventory in consistent state, item is in catalogue   |
| Post condition   |                                                        |
|                  |                                                        |
| Nominal Scenario |                                                        |
|                  | 1. Shop owner/inventory manager searches through items |


### Use case 3, FR3, shop owner applies a promotion to a certain item                                          

| Actors Involved  |                              shop owner                               |
| ---------------- | :-------------------------------------------------------------------: |
| Precondition     |                     catalogue in consistent state                     |
| Post condition   |                         catalogue is updated                          |
|                  |                                                                       |
| Nominal Scenario |                                                                       |
|                  |                  1. Shop owner searches through item                  |
|                  |        2. Shop owner decide/select discount/promotion to apply        |
|                  | 3. Shop owner decide/select the starting and ending date of promotion |
|                  |                        4. Promotion is applied                        |

### Use case 4, FR3 shop owner handles item to catalogue
| Actors Involved  |       shop owner, shop employee        |
| ---------------- | :------------------------------------: |
| Precondition     | catalogue is update and work propertly |
| Post condition   |           catalogue updated            |
|                  |                                        |
| Nominal Scenario |                                        |
|                  |                                        |
| Variant1         |             remove an item             |
| Variant2         |              add an item               |

##### Scenario4.1, FR3 shop owner remove an item from catalogue

| Actors Involved  |               shop owner, shop employee               |
| ---------------- | :---------------------------------------------------: |
| Precondition     |                                                       |
| Post condition   |                                                       |
|                  |                                                       |
| Nominal Scenario |                                                       |
|                  |          1. Shop owner searches through item          |
|                  |     2. Shop owner remove item from the catalogue      |
|                  | 3. Shop owner/employee remove item from the inventory |

##### Scenario4.2, FR3 shop owner add an item to catalogue

| Actors Involved  |           shop owner, shop employee            |
| ---------------- | :--------------------------------------------: |
| Precondition     |                                                |
| Post condition   |                                                |
|                  |                                                |
| Nominal Scenario |                                                |
|                  |  1. Shop owner adds product to the catalogue   |
|                  | 2. Shop owner/employee adds item to order list |

### Use case 5, FR4, shop owner/inventory manager places an order     

| Actors Involved  |                 shop owner, inventory manager                  |
| ---------------- | :------------------------------------------------------------: |
| Precondition     |      list size > minimum threshold, new item in catalogue      |
| Post condition   |           item added to order list, empty order list           |
|                  |                                                                |
| Nominal Scenario |                                                                |
|                  | 1. Shop owner/inventory manager searches through items (FR2.3) |
|                  |  2. Shop owner/inventory manager selects items and quantities  |
|                  |     3. Shop owner/inventory manager adds item to the list      |
|                  |                       4.Select supplier                        |
|                  |                         5. Order sent                          |
|                  |                       6. Payment succes                        |
|                  |               7. Order is placed to the supplier               |
|                  |                      8. Empty order list                       |
|                  |                                                                |
| Variant:         |                        payment failure                         |
##### Scenario5.1, payment failure

| Scenario ID: SC2 | Corresponds to UC 1 |
| ---------------- | :------------------ |
| Description      |                     |
| Precondition     |                     |
| Postcondition    | Full list           |
| Step#            | Step description    |
| 1                | Abort transaction   |

### Use case , Send customized offers

| Scenario ID: SC2 | Corresponds to UC 5                                              |
| ---------------- | :--------------------------------------------------------------- |
| Description      | Send customized offers                                           |
| Precondition     | Customer with fidelity card, promotions updated                  |
| Postcondition    | customer's fidelity card updated                                 |
| Step#            | Step description                                                 |
| 1                | Application studies info about customer's past purchases         |
| 2                | Application selects n items constantly bought by customer        |
| 3                | promotions associated with one or more of those items is updates |
| 4                | Application send email to customer notifying those promotions    |

### Use case , Add fidelity card

| Scenario ID: SC2 | Corresponds to UC 5                               |
| ---------------- | :------------------------------------------------ |
| Description      | Add fidelity card                                 |
| Precondition     | Customer wants fidelity card                      |
| Postcondition    | Customer receives fidelity card, database updated |
| Step#            | Step description                                  |
| 1                | Insert customer data into database                |

### Use case 6, FR6, shop owner changes employee's contract     

| Actors Involved  |                                      shop owner                                      |
| ---------------- | :----------------------------------------------------------------------------------: |
| Precondition     |                                                                                      |
| Post condition   |                       employee's information updated correctly                       |
|                  |                                                                                      |
| Nominal Scenario |                                                                                      |
|                  | 1. Shop owner updates employee information (role, timetable, salary) in the database |

### Use case 6, FR6, shop owner adds/removes employee     

| Actors Involved  |                        shop owner                         |
| ---------------- | :-------------------------------------------------------: |
| Precondition     |                                                           |
| Post condition   |         employee's information updated correctly          |
|                  |                                                           |
| Nominal Scenario |                                                           |
|                  |    1. Shop owner adds/removes employee in the database    |
|                  | 2. Shop owner adds/removes employee account to the system |

### Use case 7, FR7.1 log information about the shop (rent,maintenance,advertisement costs)

| Actors Involved  |            shop owner, accounting administrator             |
| ---------------- | :---------------------------------------------------------: |
| Precondition     |                   List of payment/incomes                   |
| Post condition   |                        List updated                         |
|                  |                                                             |
| Nominal Scenario |
|                  | 1. Owner add payment (rent,maintenance,advertisement costs) |
|                  |                 2. Calculate profit/losses                  |

### Use case 8, FR7.2 analize profits/losses

| Actors Involved  |              shop owner, accounting administrator              |
| ---------------- | :------------------------------------------------------------: |
| Precondition     | transaction database (incomes, expenses) in a consistent state |
| Post condition   |          system shows to the owner financial balance           |
|                  |                                                                |
| Nominal Scenario |                                                                |
|                  |    1. List all income and expensives (grouped by category)     |
|                  |           2. Show statistics about profit and losses           |
# Glossary

\<use UML class diagram to define important terms, or concepts in the domain of the system, and their relationships> 

\<concepts are used consistently all over the document, ex in use cases, requirements etc>

TODO 

# System Design

Payment terminal is already existing, we don't implement it.

# Deployment Diagram 

```plantuml
@startuml
file Application
database Database
node ownerPC
node CashRegister

Application ..> ownerPC : deploy
Application ..> CashRegister : deploy
Database ..> ownerPC : deploy
@enduml
```
