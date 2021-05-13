
- Good afternoon professor, in the 8.1 scenario the Cashier has to manage the transaction and, at the end, the balance has to be updated. The problem is that the API method that allows to record a balance update can be called only by the Administrator or the Shop Manager. We notice that doing the sequence diagram.
What should we do? -> do the update internally, not through the API
- you do not need to have the official requirements on your repo - just refer to the one here https://git-softeng.polito.it/se-2021/ezshop
- The core of the project is inside the package called it.polito.ezshop.data. In that folder, you can find the EZShopInterface (that you analyzed these days). You can also see other interfaces BalanceOperation, Customer, Order, ProductType, SaleTransaction, TicketEntry, and User. Those interfaces are the ones included in the EZShopInterface. **You are not allowed to edit these interfaces for any reason.** This package also contains a class called EzShop, which implements EzShopInterface. This class is the starting point of your implementation. You are not allowed to move it in other packages or to rename it. This class must call all the code you produce. It means that you are free to design the software as you wish as long as this class is the only one calling it. **you must list all the dependencies in the pom.xml file**. **If you want to avoid issues like this, please install maven in your pc (and make sure that the mvn command is in the build path) and run mvn clean install**
-  the Api will not change. TicketEntry should be called ProductEntry (refers to one entry for a product in a sale), but I prefer not to change the name right now.  In SaleTransaction getTicketNumber() setTicketNumber() should be called get/setTransactionID but also in this case I prefer not to change the names right now.
-  @Luca Pezzolla you should add the db dependency in the pom.xml. Furthermore you need to use an embedded DB (such as sqlite or h2) so that you are not introducing a dependency on username and password for accessing it. Also remember that your code should set up all the tables if they are not present in the db.
For the Creditcards, it is an implementation choice. You can create it as you wish. But for this it is better to ask prof. @maurizio.morisio
- BalanceOperationImpl, CustomerImpl
- Just throw exceptions, don't handle
- In many interfaces that are to be implemented there are many methods, such as setID for User or Customer that can cause discrepancies inside the data classes, I would modify the interface or make these methods private but I'm not allowed to make changes to the given files.
Can we assume that such methods will not be used in "dangerous" manners or we have to find a workaround for these methods? -> Yes you can assume it. The testing phase will verify this assumption.
- should the Integer getBalanceId();  method in the Order interface return int? Because the same method (int getBalanceId(); ) in the BalanceOperation  interface, rerturns it.
- FR8 can be dono only by administrator and shopmanager
- yes could be Integer, anyway it will be cast automatically
- Sorry prof, should we use Spring as a java framework to deal with db and something more? -> You can simply use a jdbc driver.
- Good evening,
in our design we use a map <ProductType, Integer> to keep trace of products in a sale, but the interface you provided to us expect s list of TicketEntry:
do we have to strictly follow our design or we can change a little bit respect to our design?
in the second case, do we have to modify the design document?
in general, if we meet other cases like this, how do we have to approach them? -> you have to comply to the interfaces we give you. if you have to change your design for this reason, you can - anyway this a case where you can use the adapter pattern
- Hello,
inside the new interfaces you provided to us (like BalanceOperation, SaleTransaction, ..) there are some Setter methods. -> the interfaces are mandatory - so you have to implement all the methods in the interface - they could be called and tested. If this obliges you to change your design, dont worry, changes caused by our design are not considered
- Why should I check if an order is COMPLETED in order to put it as COMPLETED? Shouldn't I check if the order was paid before set it as completed? -> completed means that the physical product has been received and should be added to the inventory - the payment is a concern of the supplier (the supplier may send also if not paid)
- We need to create a class AccountBook to hold the balance of the system or we can put it in EZShop? It's up to us
- Why SaleTransaction class doesn't extend BalanceOperation one?
Why there is no ReturnTransaction class? -> we defined the interfaces so separate API and GUI, so dont consider them for your design (apart the fact that you have to comply with them..)
- Excuse me professor, how many status for an Order are there? I found there to be three: Issued, Payed and Completed. Is this correct? -> yes, issued is sent to the supplier (and not paid), completed is when the physical product is received (and recorded in inventory) and paid when the supplier is paid
- We did plan the role as an Enum in design, but now Role is a String. We have to change our design and adapt it to the interface right? -> yes
- Good morining, I have a question about the function getAllOrder() .
In the API I read:
This method return the list of all orders ISSUED, ORDERED and COMLPETED.
Is it a typo or we don't actually have to return orders in the PAYED  status? -> yes it is a typo - just return all orders
- Consider discountRate as a property of productType (as on the glossary) so it does not change during a transaction
- Good morning, can we use an Object-Relational Mapping library to abstract the data layer for EZShop?
Also, do we have any requirements for logging of error conditions apart from throwing the exceptions specified in the interfaces? ->
Using an ORM (like hibernate) is an implementation choice. You can use it as long as you are able to return the correct data when someone calls the APIs. 
for the error condition, you need to throw the exceptions when required. other logs outside the APIs cannot be checked automatically.
- yes there is a bug in the GUI, a new GUi will be published soon. Anyway dont consider the GUi as a reference, consider only officialRequirements and API
- Good morning@maurizio.morisio, in the design  we put a class LoyaltyCard that is associated with the class Customer. In the coding phase we will create an EZCustomer class that will implement the Customer interface. The Customer interface has methods strictly correlated to the fidelity card. Must EZCustomer have an attribute FidelityCard in order to follow our design implementation or is it redundant? -> so you are asking if you should stick to having a FidelityCard class also in your design/code? In fact it does not change much, so either option is ok
- Good afternoon, I saw that some methods throw the InvalidCustomerCardException when the card is in an “invalid format”. But what does it mean to have an invalid format for the customer card? Differently from the Credit Card, there's not a standard format. How should I treat it? -> see line 390 in API (string with 10 digits) - I agree that it should be clearer - open an issue on official requirements
- Is it possible to have more than one saleTransaction open at a given time? Since, from the GUI of course is not (because the app runs on a single pc), but from a testing point of view this could happen. Should we have to consider this possibility? -> no, only one at a time
- Sorry for asking... should the constructor of the class be included in the testing? -> only if it contains any logic that can be verified. it is not necessary for simple classes where the constructor is just a sequence of sets
- Good morning professors @maurizio.morisio @Luca Ardito:
Do we have to deliver the DB too?
Do we have to deliver a document for testing? I thought we would get a template for it but we have none at the moment
Is it fine to save in the DB transactions which haven't ended yet? We'll mark them with a status. We'll load also only the closed ones -> you have to use an integrated db (e.g., SQLite or H2) and deliver it;
you will receive in a short while the "official" template which is anyway the merge of the black and white box templates you find in the lab exercises repo: https://git-softeng.polito.it/se-2021/softeng_blackbox_lab
it is not necessary to save transactions which have not ended yet
-  I've encountered a problem in testing: in order to test create-update-delete user, I have to affect directly the DB. For that reason, i've created a user which i'll use to modify user rights and then i'll delete that user in deleteUser. In this way I've created "dependencies", but I'm not sure about other ways to do it.  For these reasons, i had to force an order between tests regarding the same FR. (alphanumerical order)
Is this an error-> It is not wrong to have this in your test cases when you deal with db. The options are:
wipe out everything before each test (see doc about @BeforeEach)
introduce dependencies between test cases. it is not uncommon to work with dependencies but of course you have to take them in mind when defining each new test case
- saletransaction:student
- balanceoperation:studentDao
- balanceoperationImpl:studentDaoImpl

- productType:student
- saletransaction:studentDao
- saletransactionImpl:studentDaoImpl

- shop
- ezshopinterface
- ezshop

