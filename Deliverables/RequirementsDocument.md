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
    - [Use case 2, FR2, shop owner/shop employee updates inventory](#use-case-2-fr2-shop-ownershop-employee-updates-inventory)
    - [Use case 2.4, FR2.4, application notifies shop owner/shop employee about low amount of a certain item](#use-case-24-fr24-application-notifies-shop-ownershop-employee-about-low-amount-of-a-certain-item)
    - [Use case 4, FR4,](#use-case-4-fr4)
    - [Use case x, UCx](#use-case-x-ucx)
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
| Shop Employee             |                               Who works in the store: cashiers, cleaners, inventory manager, inventory workers                               |
| Supplier                  |                                                           Sells items to the owner                                                           |
| customer                  |                                                           Buys items from the shop                                                           |
| Computer Engineer         | Create and maintain the application (us) and support the user of the application in case of problems (IT, database, security administrators) |
| Inventory                 |                                                        Items with specific properties                                                        |
| Catalogue                 |                                                         Items with common properties                                                         |

# Context Diagram and interfaces

## Context Diagram

```plantuml
 
rectangle Application {
    (admin GUI)
    (shop employee GUI)
    (inventory employee GUI)
}

:Owner/Manager of the shop: --> (admin GUI) : admin GUI
(admin GUI) --> :Supplier: : Inventory and Accounting section
(admin GUI) --> :Customer: : Accounting section
(admin GUI) --> :Shop employee: : Accounting section

(admin GUI) <..> (shop employee GUI)
(admin GUI) <..> (inventory employee GUI)

(inventory employee GUI) --> :Inventory:
:Shop employee: --> (shop employee GUI) : employee GUI

(admin GUI) --> :Inventory:
:Supplier: --> :Inventory:
:Customer: --> :Inventory:
(shop employee GUI) --> :Inventory:

(admin GUI) --> :Catalogue:
:Inventory: <-- :Catalogue:
```

## Interfaces

| Actor                     |                  Logical Interface                  | Physical Interface |
| ------------------------- | :-------------------------------------------------: | -----------------: |
| Owner/Manager of the shop |               Application GUI (admin)               |  His PC/Smartphone |
| Shop Employee             |        Accounting section of the application        |                  x |
| Shop Employee             |             Application GUI (employee)              |   Payment terminal |
| Supplier                  | Inventory and Accounting section of the application |                  x |
| Customer                  |        Accounting section of the application        |                  x |
| Customer                  |             Application GUI (customer)              |   Their smartphone |
| Computer Engineer         |               Programming Environment               |          Their  PC |

# Stories and personas
\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as scenarios in use cases>

TODO

# Functional and non functional requirements

## Functional Requirements

| ID    |                                                       Description                                                        |
| ----- | :----------------------------------------------------------------------------------------------------------------------: |
| FR1   |                                                       manage sales                                                       |
| FR1.1 |              log sales data (type, amount, price of items/discounts, type of customer, time of transaction)              |
| FR1.2 | Payment terminal ( credit card reader) integration (discount if registered customer, update inventory status, log sales) |
| FR2   |                                                     manage inventory                                                     |
| FR2.1 |                            log inventory status (type, amount of items), available space (?)                             |
| FR2.2 |                                            Add/Remove items to/from inventory                                            |
| FR2.3 |                                              Search through items (ordered)                                              |
| FR2.4 |                                         Notice if item quantity under threshold                                          |
| FR3   |                                                     manage catalogue                                                     |
| FR3.1 |                                             log information about promotion                                              |
| FR3.2 |                                            Add/Remove items to/from catalogue                                            |
| FR3.3 |                                              Search through items (ordered)                                              |
| FR4   |                                                      manage orders                                                       |
| FR4.1 |                                      log information about suppliers (items costs)                                       |
| FR4.2 |                                                Add/Remove items to order                                                 |
| FR4.3 |                                             Send order and pay the supplier                                              |
| FR5   |                                                     manage customers                                                     |
| FR5.1 |                                      manage fidelity cards (points, special offers)                                      |
| FR5.2 |                                                   Track user purchase                                                    |
| FR5.3 |                                         Send customized offers (advertisements)                                          |
| FR6   |                              manage employees (information, role, salary costs, timetables)                              |
| FR6.1 |                                                   Add/Remove Employee                                                    |
| FR6.2 |                                                     Update Employee                                                      |
| FR7   |                                                    manage accounting                                                     |
| FR7.1 |                          log information about the shop (rent,maintenance,advertisement costs)                           |
| FR7.2 |                                                  analize profits/losses                                                  |

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
```

### Use case 1, FR1 customer buys item

| Actors Involved  |                            customer, shop employee                            |
| ---------------- | :---------------------------------------------------------------------------: |
| Precondition     |                               item in inventory                               |
| Post condition   | item sold, amount of items in shop updated, gains updated, transaction logged |
|                  |                                                                               |
| Nominal Scenario |                                                                               |
|                  |                        1. Shop employee scans products                        |
|                  |                        2. Applying general promotions                         |
|                  |                               3. Customer pays                                |
|                  |           4. Application updates amount of items in shop and gains            |
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
| 7                | Update customer's points                                                      |

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

### Use case 2, FR2, shop owner/shop employee updates inventory

| Actors Involved  |                   shop owner or shop employee, inventory                   |
| ---------------- | :------------------------------------------------------------------------: |
| Precondition     |                inventory in consistent state, order arrives                |
| Post condition   |                        inventory updated correctly                         |
|                  |                                                                            |
| Nominal Scenario |                                                                            |
|                  |           1. Shop owner/employee searches through items (FR2.3)            |
|                  | 2. Shop owner/employee adds/removes/updates items in the inventory (FR2.2) |
|                  |                                                                            |

### Use case 2.4, FR2.4, application notifies shop owner/shop employee about low amount of a certain item

| Actors Involved  |                 shop owner or shop employee, inventory                 |
| ---------------- | :--------------------------------------------------------------------: |
| Precondition     |             one item amount drop under a certain threshold             |
| Post condition   |            notification sent to shop owner or shop employee            |
|                  |                                                                        |
| Nominal Scenario |                                                                        |
|                  | 1. Application updates amount of items in shop and gains (UC1 step 4)  |
|                  | 2. Application sends a notification to the shop owner or shop employee |
|                  |                                                                        |

### Use case 4, FR4, 
\                 manage orders                        
 log information about suppliers (items costs)        
           Add/Remove items to order                  
        Send order and pay the supplier               

| Actors Involved  |                   shop owner or shop employee, supplier                    |
| ---------------- | :------------------------------------------------------------------------: |
| Precondition     |                                                                            |
| Post condition   |                                order placed                                |
|                  |                                                                            |
| Nominal Scenario |                                                                            |
|                  |           1. Shop owner/employee searches through items (FR2.3)            |
|                  | 2a. Application automatically queries the suppliers about the items prices |
|                  |           2b. Shop owner/employee manually updates items prices            |
|                  |            3. Shop owner/employee selects items and quantities             |
|                  |                     4. Order is placed to the supplier                     |
|                  |                     5.(calendar with estimate dates?)                      |

### Use case x, UCx
..

# Glossary

\<use UML class diagram to define important terms, or concepts in the domain of the system, and their relationships> 

\<concepts are used consistently all over the document, ex in use cases, requirements etc>

TODO 

# System Design

Payment terminal is already existing, we don't implement it.

# Deployment Diagram 

```plantuml
file Application
database Database
node ownerPC
node CashRegister

Application ..> ownerPC : deploy
Application ..> CashRegister : deploy
Database ..> ownerPC : deploy
```
