package it.polito.ezshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import it.polito.ezshop.data.Implementations.BalanceOperationImpl;
import it.polito.ezshop.data.Implementations.CustomerImpl;
import it.polito.ezshop.data.Implementations.OrderImpl;
import it.polito.ezshop.data.Implementations.EZShopDAO;
import it.polito.ezshop.data.Implementations.ProductTypeImpl;
import it.polito.ezshop.data.Implementations.ReturnTransactionImpl;
import it.polito.ezshop.data.Implementations.SaleTransactionImpl;
import it.polito.ezshop.data.Implementations.TicketEntryImpl;
import it.polito.ezshop.data.Implementations.UserImpl;
import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class EZShop1 implements EZShopInterface {

	private User userLoggedIn = new UserImpl();
	private SaleTransactionImpl openSaleTransaction = null;
	private ReturnTransactionImpl openReturnTransaction = null;
	private EZShopDAO DAO = new EZShopDAO();

	// method to open the connection to database
	public Connection dbAccess() {

		Connection conn = null;
		try {
			String url = "jdbc:sqlite:EZShopDB.sqlite";
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return conn;

	};

	// method to close the connection to database
	public void dbClose(Connection conn) {

		try {
			if (conn != null) {
				conn.close();
				System.out.println("connection closed");
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}

	// method to verify if a string contains only letters
	public static boolean isStringOnlyAlphabet(String str) {

		return ((str != null) && (!str.equals("")) && (str.matches("^[a-zA-Z]*$")));

	}

	// method to verify if a string contains only numbers
	public static boolean isStringOnlyNumbers(String str) {

		return ((str != null) && (!str.equals("")) && (str.matches("^[0-9]*$")));

	};

	// method to verify the correctness of the barcode
	public static boolean isBarcodeValid(String barcode) {

		boolean valid = false;
		if (!isStringOnlyNumbers(barcode))
			return valid;

		List<Integer> b = new ArrayList<Integer>();
		for (String c : barcode.split("")) {
			b.add(Integer.parseInt(c));
		}
		Collections.reverse(b);
		Integer tot = 0;
		for (int i = 1; i < b.size(); i++) {
			if (i % 2 != 0)
				tot = tot + b.get(i) * 3;
			else
				tot = tot + b.get(i);
		}
		if (tot % 10 == 0)
			tot = 0;
		else
			tot = 10 - (tot % 10);
		if (tot == b.get(0))
			valid = true;
		else
			valid = false;
		return valid;

	}

	public boolean checkLuhn(String cardNo) {

		if (!isStringOnlyNumbers(cardNo))
			return false;
		int nDigits = cardNo.length();

		int nSum = 0;
		boolean isSecond = false;
		for (int i = nDigits - 1; i >= 0; i--) {
			int d = cardNo.charAt(i) - '0';

			if (isSecond == true)
				d = d * 2;

			nSum += d / 10;
			nSum += d % 10;

			isSecond = !isSecond;
		}
		return (nSum % 10 == 0);

	}

	@Override
	public void reset() {
		Connection conn = null;
		DAO.reset();		
	    openSaleTransaction = null;
		openReturnTransaction = null;
	}

	@Override
	public Integer createUser(String username, String password, String role)
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

		if (username != null && username.isEmpty() == false) {
			if (password != null && password.isEmpty() == false) {
				if (role != null
						&& (role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManager"))) {
						Integer id = DAO.createUser(username, password, role);
						return id;
				} else
					throw new InvalidRoleException("Invalid role");
			} else
				throw new InvalidPasswordException("Invalid password");
		} else
			throw new InvalidUsernameException("Invalid username");

	}

	@Override
	public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		Connection conn = null;
		if (userLoggedIn.getRole().equals("Administrator")) {
			if (id != null && id > 0) {
				return DAO.deleteUser(id);
			} else
				throw new InvalidUserIdException("Invalid User");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public List<User> getAllUsers() throws UnauthorizedException {

		if (userLoggedIn.getRole().equals("Administrator")) {
			return DAO.getAllUsers();
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		if (userLoggedIn.getRole().equals("Administrator")) {
			if (id != null && id > 0) {
				return DAO.getUser(id);
			} else
				throw new InvalidUserIdException("Invalid user");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public boolean updateUserRights(Integer id, String role)
			throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {

		if (userLoggedIn.getRole().equals("Administrator")) {
			if (id != null && id > 0) {
				if (role != null
						&& (role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManager"))) {
					return DAO.updateUserRights(id, role);
				} else
					throw new InvalidRoleException("Invalid role");
			} else
				throw new InvalidUserIdException("Invalid user");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {

		if (username != null && username.isEmpty() == false) {
			if (password != null && password.isEmpty() == false) {
				userLoggedIn = DAO.login(username, password);
				return userLoggedIn;
			} else
				throw new InvalidPasswordException("Password not valid");
		} else
			throw new InvalidUsernameException("Username not valid");

	}

	@Override
	public boolean logout() {

		if (userLoggedIn.getId() != -1) { // no users already logged in
			userLoggedIn.setId(-1);
			userLoggedIn.setUsername("");
			userLoggedIn.setPassword("");
			userLoggedIn.setRole("");
			return true;
		} else
			return false;

	}

	@Override
	public Integer createProductType(String description, String productCode, double pricePerUnit, String note)
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException {

		Integer id;
		Connection conn = null;
		if (description == null || description == "")
			throw new InvalidProductDescriptionException("invalid description");
		else if (productCode == null || productCode == "")
			throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
		else if (productCode.length() < 12 || productCode.length() > 14)
			throw new InvalidProductCodeException("invalid barcode: wrong length");
		else if (!isStringOnlyNumbers(productCode))
			throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
		else if (!isBarcodeValid(productCode))
			throw new InvalidProductCodeException("invalid barcode: barcode does not respect GTIN specifications");
		else if (pricePerUnit <= 0)
			throw new InvalidPricePerUnitException("invalid price");
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
			throw new UnauthorizedException("user error");
		else {
			id = DAO.createProductType(description, productCode, pricePerUnit, note);
		}
		return id;

	}

	// method to update product's fields
	@Override
	public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
			throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, UnauthorizedException {

		Boolean success = false;
		Connection conn = null;
		if (id <= 0 || id == null)
			throw new InvalidProductIdException("invalid ID");
		else if (newDescription == null || newDescription == "")
			throw new InvalidProductDescriptionException("invalid description");
		else if (newCode == null || newCode == "")
			throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
		else if (newCode.length() < 12 || newCode.length() > 14)
			throw new InvalidProductCodeException("invalid barcode: wrong length");
		else if (!isStringOnlyNumbers(newCode))
			throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
		else if (!isBarcodeValid(newCode))
			throw new InvalidProductCodeException("invalid barcode: barcode does not respect GTIN specifications");
		else if (newPrice <= 0)
			throw new InvalidPricePerUnitException("invalid price");
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
			throw new UnauthorizedException("user error");
		else {
			success = DAO.updateProduct(id, newDescription, newCode, newPrice, newNote);
		}
		return success;
	};

	// method to delete a product
	@Override
	public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {

		Boolean success = false;
		if (id <= 0 || id == null)
			throw new InvalidProductIdException("invalid ID");
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
			throw new UnauthorizedException("user error");
		else {
			success = DAO.deleteProductType(id);
		}
		return success;

	}

	// method to get the list of all products
	@Override
	public List<ProductType> getAllProductTypes() throws UnauthorizedException {
		List<ProductType> inventory = new ArrayList<ProductType>();
		if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")
				&& !userLoggedIn.getRole().equals("Cashier")))
			throw new UnauthorizedException("user error");
		else {
			inventory = DAO.getAllProductTypes();
		}
		return inventory;

	}

	// method to get a product by barcode
	@Override
	public ProductType getProductTypeByBarCode(String barCode)
			throws InvalidProductCodeException, UnauthorizedException {

		ProductType product = new ProductTypeImpl();
		if (barCode == null || barCode == "")
			throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
		else if (barCode.length() < 12 || barCode.length() > 14)
			throw new InvalidProductCodeException("invalid barcode: wrong length");
		else if (!isStringOnlyNumbers(barCode))
			throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
		else if (!isBarcodeValid(barCode))
			throw new InvalidProductCodeException("invalid barcode: barcode does not respect GTIN specifications");
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
			throw new UnauthorizedException("user error");
		else {
			product = DAO.getProductTypeByBarCode(barCode);
		}
		return product;

	}

	// method to get a product by description
	@Override
	public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {

		if (description == "")
			description = null;
		List<ProductType> matchingProducts = new ArrayList<ProductType>();
		if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
			throw new UnauthorizedException("user error");
		else {
			matchingProducts = DAO.getProductTypesByDescription(description);
		}
		return matchingProducts;

	}

	// method to update quantity of product
	@Override
	public boolean updateQuantity(Integer productId, int toBeAdded)
			throws InvalidProductIdException, UnauthorizedException {

		boolean success = false;
		if (productId <= 0 || productId == null)
			throw new InvalidProductIdException("ID incorrect");
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
			throw new UnauthorizedException("user error");
		else {
			success = DAO.updateQuantity(productId, toBeAdded);
		}
		return success;

	}

	// method to update the position of a product
	@Override
	public boolean updatePosition(Integer productId, String newPos)
			throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {

		boolean success = false;
		if (productId <= 0 || productId == null)
			throw new InvalidProductIdException("ID incorrect");
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
			throw new UnauthorizedException("user error");
		else if (newPos == null || newPos == "") {
			success = DAO.updatePosition(productId, newPos);
		} else if (newPos.split("-").length != 3)
			throw new InvalidLocationException("wrong format for location: wrong field(s)");
		else if (!isStringOnlyNumbers(newPos.split("-")[0]))
			throw new InvalidLocationException("wrong format for location: aisle must be a number");
		else if (!isStringOnlyAlphabet(newPos.split("-")[1]))
			throw new InvalidLocationException(
					"wrong format for location: ID must contains only alphabetic characters");
		else if (!isStringOnlyNumbers(newPos.split("-")[2]))
			throw new InvalidLocationException("wrong format for location: level must be a number");
		else {
			Integer aisleNumber = Integer.parseInt(newPos.split("-")[0]);
			String alphabeticId = newPos.split("-")[1];
			Integer levelNumber = Integer.parseInt(newPos.split("-")[2]);
			if (aisleNumber == null || alphabeticId == null || alphabeticId == "" || levelNumber == null)
				throw new InvalidLocationException("wrong format for location");
			else { 
				success = DAO.updatePosition(productId, newPos);
			}
		}
		return success;

	}

	@Override
	public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException,
			InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

		int id = -1;

		if (quantity <= 0)
			throw new InvalidQuantityException("The quantity you've inserted is not accepted");
		if (pricePerUnit <= 0)
			throw new InvalidPricePerUnitException("The price you've inserted is not accepted");
		if (!userLoggedIn.getRole().equals("Administrator") &&  !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException("Either the user doesn't have the rights to perform this action or doesn't exist");
		if (productCode == null ||  productCode == "")
            throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
        if (productCode.length() < 12 || productCode.length() > 14)
            throw new InvalidProductCodeException("invalid barcode: wrong length");
        if (!isStringOnlyNumbers(productCode))
            throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
        if (!isBarcodeValid(productCode))
            throw new InvalidProductCodeException("invalid barcode: barcode does not respect GTIN specifications");
		id = DAO.issueOrder(productCode, quantity, pricePerUnit);
		System.out.println(id);
		return id;

	}

	@Override
	public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
			throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException,
			UnauthorizedException {

		int orderId = -1;

		if (quantity <= 0)
			throw new InvalidQuantityException("The quantity you've inserted is not accepted");
		if (pricePerUnit <= 0)
			throw new InvalidPricePerUnitException("The price you've inserted is not accepted");
		if (!userLoggedIn.getRole().equals("Administrator") &&  !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException("Either the user doesn't have the rights to perform this action or doesn't exist");
		if (productCode == null ||  productCode == "")
            throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
        if (productCode.length() < 12 || productCode.length() > 14)
            throw new InvalidProductCodeException("invalid barcode: wrong length");
        if (!isStringOnlyNumbers(productCode))
            throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
        if (!isBarcodeValid(productCode))
            throw new InvalidProductCodeException("invalid barcode: barcode does not respect GTIN specifications");
		if(!recordBalanceUpdate(-pricePerUnit * quantity))
			return orderId;
		orderId = DAO.payOrderFor(productCode, quantity, pricePerUnit);	
		return orderId;

	}

	@Override
	public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {

		boolean validOrderId = false;
		double money = 0;
		if (!userLoggedIn.getRole().equals("Administrator") &&  !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException("Either the user doesn't have the rights to perform this action or doesn't exist");
		if(orderId == null || orderId <=0)
			throw new InvalidOrderIdException("There is no order with this id");
		validOrderId = DAO.payOrder(orderId, money);
		if(!recordBalanceUpdate(-money))
			return validOrderId = false;
			
		return validOrderId;

	}

	@Override
	public boolean recordOrderArrival(Integer orderId)
			throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {

		boolean valid = false;
		if (!userLoggedIn.getRole().equals("Administrator") &&  !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException("Either the user doesn't have the rights to perform this action or doesn't exist");
		if (orderId <= 0 || orderId == null)
			throw new InvalidOrderIdException("The order id is not valid");
		valid = DAO.recordOrderArrival(orderId);
		return valid;

	}

	@Override
	public List<Order> getAllOrders() throws UnauthorizedException {

		List<Order> orders = new ArrayList<Order>();
		if (!userLoggedIn.getRole().equals("Administrator") &&  !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException("Either the user doesn't have the rights to perform this action or doesn't exist");
		orders = DAO.getAllOrders();
		return orders;

	}

	@Override
	public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {

		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			if (customerName != null && customerName.isEmpty() == false) {
				return DAO.defineCustomer(customerName);
			} else
				throw new InvalidCustomerNameException("Invalid customer name");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
			throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException,
			UnauthorizedException {

		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			if (id != null && id > 0) {
				if (newCustomerName != null && newCustomerName.isEmpty() == false) {
					return DAO.modifyCustomer(id, newCustomerName, newCustomerCard);
				} else
					throw new InvalidCustomerNameException("Invalid customer name");
			} else
				throw new InvalidCustomerIdException("Invalid customer id");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			if (id != null && id > 0) {
				return DAO.deleteCustomer(id);
			} else
				throw new InvalidCustomerIdException("Invalid customer id");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			if (id != null && id > 0) {
				return DAO.getCustomer(id);
			} else
				throw new InvalidCustomerIdException("Invalid customer id");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public List<Customer> getAllCustomers() throws UnauthorizedException {

		List<Customer> customers = new ArrayList<Customer>();
		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			return customers = DAO.getAllCustomers();
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public String createCard() throws UnauthorizedException {

		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			return DAO.createCard();
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public boolean attachCardToCustomer(String customerCard, Integer customerId)
			throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {

		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			if (customerId != null && customerId > 0) {
				Pattern p = Pattern.compile("\\d+");
				if (customerCard != null && customerCard.isEmpty() == false && customerCard.length() >= 10
						&& p.matcher(customerCard).matches()) {
					return DAO.attachCardToCustomer(customerCard, customerId);
				} else
					throw new InvalidCustomerCardException("Invalid card");
			} else
				throw new InvalidCustomerIdException("Invalid customer id");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
			throws InvalidCustomerCardException, UnauthorizedException {

		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			Pattern p = Pattern.compile("\\d+");
			if (customerCard != null && customerCard.isEmpty() == false && customerCard.length() >= 10
					&& p.matcher(customerCard).matches()) {
				return DAO.modifyPointsOnCard(customerCard, pointsToBeAdded);
			} else
				throw new InvalidCustomerCardException("Invalid card");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public Integer startSaleTransaction() throws UnauthorizedException {

		System.out.println("Executing startSaleTransaction...");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		Integer id = DAO.startSaleTransaction();
		openSaleTransaction = new SaleTransactionImpl(id); // transaction will be added to the db only when it ends
		return id;

	}

	@Override
	public boolean addProductToSale(Integer transactionId, String productCode, int amount)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			UnauthorizedException {

		System.out.println("Executing addProductToSale...");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (amount <= 0)
			throw new InvalidQuantityException("Amount to add cannot be <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber())
			return false;
		ProductType productType = DAO.getProductTypeByBarCode(productCode); // could throw InvalidProductCodeException
		System.out.println(productType);
		if (productType == null || amount > productType.getQuantity())
			return false;

		openSaleTransaction.upsertEntry(productType, amount);
		System.out.println("openSaleTransaction->" + openSaleTransaction);
		DAO.updateQuantity(productType.getId(), -amount);
		return true;

	}

	@Override
	public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			UnauthorizedException {

		System.out.println("Executing deleteProductFromSale...");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (amount <= 0)
			throw new InvalidQuantityException("Amount to remove cannot be <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber())
			return false;
		Boolean result = openSaleTransaction.removeAmountFromEntry(productCode, amount);
		DAO.updateQuantity(DAO.getProductTypeByBarCode(productCode).getId(), amount);
		return result;

	}

	@Override
	public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException,
			UnauthorizedException {

		System.out.println("Executing applyDiscountRateToProduct...");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (discountRate < 0 || discountRate >= 1)
			throw new InvalidDiscountRateException("Discount Rate must be >=0 and <1");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber())
			return false;

		openSaleTransaction.setDiscountRateToProduct(productCode, discountRate);
		
		return true;

	}

	@Override
	public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
			throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

		System.out.println("Executing applyDiscountRateToSale...");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (discountRate < 0 || discountRate >= 1)
			throw new InvalidDiscountRateException("Discount Rate must be >=0 and <1");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber())
			return false;
		openSaleTransaction.setDiscountRate(discountRate);
		return true;

	}

	@Override
	public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing computePointsForSale...");
		int points = -1;
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction != null && transactionId == openSaleTransaction.getTicketNumber()) {
			points = (int) (openSaleTransaction.getPrice() / 10);
		}
		// else points==-1
		return points;

	}

	@Override
	public boolean endSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing endSaleTransaction...");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber()
				|| getSaleTransaction(transactionId) != null)
			return false;

		return DAO.endSaleTransaction(transactionId, openSaleTransaction);

	}

	@Override
	public boolean deleteSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		// System.out.println("Executing deleteSaleTransaction...");
		// String deleteSale = "DELETE FROM saleTransaction WHERE ticketNumber=?";
		// String deleteTicketEntries = "DELETE FROM ticketEntry WHERE ticketNumber=?";
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		// try (Connection conn = this.dbAccess();) {
		// PreparedStatement pstmt = conn.prepareStatement(deleteSale);
		// pstmt.setInt(1, transactionId);
		// pstmt.executeUpdate();
		// pstmt.close();
		// pstmt = conn.prepareStatement(deleteTicketEntries);
		// pstmt.setInt(1, transactionId);
		// pstmt.executeUpdate();
		// pstmt.close();
		// } catch (SQLException e) {
		// System.out.println(e.getMessage());
		// return false;
		// }
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber()
				|| getSaleTransaction(transactionId) != null)
			return false;
		openSaleTransaction = null;
		return true;

	}

	@Override
	public SaleTransaction getSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing getSaleTransaction...");
		String getSale = "SELECT price,discountRate,balanceId FROM saleTransaction WHERE ticketNumber=?";
		String getBalance = "SELECT balanceId,date,money,type FROM balanceOperation WHERE balanceId=?";
		String getTicketEntries = "SELECT barCode,productDescription,pricePerUnit,discountRate,amount FROM ticketEntry WHERE ticketNumber=?";
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		SaleTransactionImpl result = null;
		try (Connection conn = this.dbAccess();) {
			// getSale
			PreparedStatement pstmt = conn.prepareStatement(getSale);
			pstmt.setInt(1, transactionId);
			ResultSet rs = pstmt.executeQuery();
			if (rs == null)
				return null;
			// only 1 result because ticketNumber is primary key
			result = new SaleTransactionImpl(transactionId);
			result.setPrice(rs.getDouble("price"));
			result.setDiscountRate(rs.getDouble("discountRate"));
			Integer balanceId = rs.getInt("balanceId");
			System.out.println(result.getTicketNumber() + " " + result.getDiscountRate());
			pstmt.close();
			rs.close();
			// getBalance
			pstmt = conn.prepareStatement(getBalance);
			pstmt.setInt(1, balanceId);
			rs = pstmt.executeQuery();
			if (rs == null)
				return null;
			// only 1 result because balanceId is primary key
			BalanceOperationImpl balanceOperation = new BalanceOperationImpl(rs.getInt("balanceId"),
					LocalDate.parse(rs.getString("date")), rs.getDouble("money"), rs.getString("type"));
			openSaleTransaction.setBalanceOperation(balanceOperation);
			pstmt.close();
			rs.close();
			// getTicketEntries
			pstmt = conn.prepareStatement(getTicketEntries);
			pstmt.setInt(1, transactionId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result.getEntries().add(new TicketEntryImpl(rs.getString("barCode"), rs.getString("productDescription"),
						rs.getDouble("pricePerUnit"), rs.getDouble("discountRate"), rs.getInt("amount")));
			}
			System.out.println(result);
			pstmt.close();
			rs.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return result;

	}

	@Override
	public Integer startReturnTransaction(Integer transactionId)
			throws /* InvalidTicketNumberException, */InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing startReturnTransaction...");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		SaleTransaction saleTransaction = getSaleTransaction(transactionId);
		if (saleTransaction == null)
			return -1;
		Integer returnId = 0; // 0=error
		String getNextAutoincrement = "SELECT seq FROM sqlite_sequence WHERE name=\"returnTransaction\"";
		try (Connection conn = this.dbAccess();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(getNextAutoincrement)) {
			returnId = rs.getInt("seq") + 1;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		openReturnTransaction = new ReturnTransactionImpl(returnId, saleTransaction); // transaction will be added to
																						// the db only when it ends
		return returnId;

	}

	@Override
	public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

		System.out.println("Executing returnProduct...");
		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException("Return id cannot be null or <=0");
		if (amount <= 0)
			throw new InvalidQuantityException("Amount to add cannot be <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openReturnTransaction == null || returnId != openReturnTransaction.getReturnId()) // the transaction does
																								// not exist
			return false;
		ProductType productType = getProductTypeByBarCode(productCode); // could throw InvalidProductCodeException
		System.out.println(productType);
		if (productType == null) // the product to be returned does not exists
			return false;
		List<TicketEntry> entries = openReturnTransaction.getSaleTransaction().getEntries();
		Boolean updated = false;
		for (TicketEntry entry : entries) {
			if (entry.getBarCode() == productCode) {
				if (amount > entry.getAmount()) // the amount is higher than the one in the sale transaction
					return false;
				// else
				openReturnTransaction.setProductId(productType.getId());
				openReturnTransaction.setProductCode(productCode);
				openReturnTransaction.setPricePerUnit(entry.getPricePerUnit());
				openReturnTransaction.setDiscountRate(entry.getDiscountRate());
				openReturnTransaction.setAmount(amount);
				openReturnTransaction.setPrice(openReturnTransaction.getPrice()
						+ entry.getPricePerUnit() * amount * (1 - entry.getDiscountRate()));
				updated = true;
				System.out.println(entry);
				break;
			}
		}
		if (updated == false) // productCode was not in the transaction
			return false;
		return true;

	}

	@Override
	public boolean endReturnTransaction(Integer returnId, boolean commit)
			throws InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing endReturnTransaction...");
		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException("Return id cannot be null or <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openReturnTransaction == null || returnId != openReturnTransaction.getReturnId())
			return false;
		if (commit == false) {
			openReturnTransaction = null;
			return true;
		}
		// commit==true
		try {
			updateQuantity(openReturnTransaction.getProductId(), openReturnTransaction.getAmount());
		} catch (Exception ex) { // InvalidProductIdException
			System.out.println(ex.getMessage());
		}
		String insertReturn = "INSERT INTO returnTransaction(productId,productCode,pricePerUnit,discountRate,amount,price,transactionId) VALUES(?,?,?,?,?,?,?)";
		// insertReturn
		try (Connection conn = this.dbAccess();) {
			PreparedStatement pstmt = conn.prepareStatement(insertReturn);
			pstmt.setInt(1, openReturnTransaction.getProductId());
			pstmt.setString(2, openReturnTransaction.getProductCode());
			pstmt.setDouble(3, openReturnTransaction.getPricePerUnit());
			pstmt.setDouble(4, openReturnTransaction.getDiscountRate());
			pstmt.setInt(5, openReturnTransaction.getAmount());
			pstmt.setDouble(6, openReturnTransaction.getPrice());
			pstmt.setInt(7, openReturnTransaction.getSaleTransaction().getTicketNumber());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
		String updateTicket = "UPDATE ticketEntry SET amount = ? WHERE ticketNumber = ?";
		String removeTicket = "DELETE FROM ticketEntry WHERE ticketNumber=?";
		// update or remove ticket
		Boolean updated = false;
		Iterator<TicketEntry> iter = openReturnTransaction.getSaleTransaction().getEntries().iterator();
		while (iter.hasNext() && updated == false) {
			TicketEntry entry = iter.next();
			if (entry.getBarCode() == openReturnTransaction.getProductCode()) { // product present in the
																				// saleTransaction
				int previousAmount = entry.getAmount();
				int amountToRemove = openReturnTransaction.getAmount();
				if (amountToRemove < previousAmount) {
					try (Connection conn = this.dbAccess();) {
						PreparedStatement pstmt = conn.prepareStatement(updateTicket);
						pstmt.setDouble(1, previousAmount - amountToRemove);
						pstmt.setInt(2, openReturnTransaction.getSaleTransaction().getTicketNumber());
						pstmt.executeUpdate();
						pstmt.close();
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
						return false;
					}
					updated = true;
				} else if (amountToRemove == previousAmount) {
					try (Connection conn = this.dbAccess();) {
						PreparedStatement pstmt = conn.prepareStatement(removeTicket);
						pstmt.setInt(1, openReturnTransaction.getSaleTransaction().getTicketNumber());
						pstmt.executeUpdate();
						pstmt.close();
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
						return false;
					}
					updated = true;
				}
				// else if (amountToRemove > previousAmount) updated=false; (can't happen)
				System.out.println("Found item to remove" + entry);
				break;
			}
		}
		// increaseProductQuantity
		String increaseProductQuantity = "UPDATE product SET quantity=quantity + ? WHERE barcode=?";
		try (Connection conn = this.dbAccess();) {
			PreparedStatement pstmt = conn.prepareStatement(increaseProductQuantity);
			pstmt.setInt(1, openReturnTransaction.getAmount());
			pstmt.setString(2, openReturnTransaction.getProductCode());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}

		String updateSale = "UPDATE saleTransition SET price = ? WHERE ticketNumber = ?";
		// updateSale
		try (Connection conn = this.dbAccess();) {
			PreparedStatement pstmt = conn.prepareStatement(updateSale);
			pstmt.setDouble(1,
					openReturnTransaction.getSaleTransaction().getPrice() - openReturnTransaction.getPrice());
			pstmt.setInt(2, openReturnTransaction.getSaleTransaction().getTicketNumber());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
		// Insert BalanceOperation
		recordBalanceUpdate(-openReturnTransaction.getPrice());
		openReturnTransaction = null;
		return true;

	}

	@Override
	public boolean deleteReturnTransaction(Integer returnId)
			throws InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing deleteReturnTransaction...");
		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException("Return id cannot be null or <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openReturnTransaction == null || returnId != openReturnTransaction.getReturnId())
			return false;
		openReturnTransaction = null;
		return true;

	}

	@Override
	public double receiveCashPayment(Integer transactionId, double cash)
			throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {

		System.out.println("Executing receiveCashPayment...");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (cash <= 0)
			throw new InvalidPaymentException("cash cannot be <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber())
			return -1;

		double price = openSaleTransaction.getPrice();
		if (cash < price)
			return -1;

		return (cash - price);

	}

	@Override
	public boolean receiveCreditCardPayment(Integer transactionId, String creditCard)
			throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		System.out.println("Executing receiveCreditCardPayment...");

		if (transactionId <= 0 || transactionId == null)
			throw new InvalidTransactionIdException();
		if (creditCard.equals(null) || !checkLuhn(creditCard))
			throw new InvalidCreditCardException();
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber())
			return false;

		Connection conn = null;
		try {
			String url = "jdbc:sqlite:creditCards.sqlite";
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		double price = openSaleTransaction.getPrice();
		try {
			String getCard = "SELECT balance FROM creditCards WHERE creditCardNumber = ?";
			PreparedStatement pstmt = conn.prepareStatement(getCard);
			pstmt.setString(1, creditCard);
			ResultSet rs = pstmt.executeQuery();
			if (rs == null) // card not registered
				return false;
			// only 1 result because creditCard is primary key
			pstmt.close();
			double credit = rs.getDouble("balance");
			rs.close();
			if (credit < price) // card has not enough money
				return false;
			double newCredit = credit - price;
			String updateCard = "UPDATE creditCards SET balance = ? WHERE creditCardNumber = ?";
			pstmt = conn.prepareStatement(updateCard);
			pstmt.setDouble(1, newCredit);
			pstmt.setString(2, creditCard);
			pstmt.executeUpdate(updateCard);
			pstmt.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}

		return true;

	}

	@Override
	public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing returnCashPayment...");
		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openReturnTransaction == null || returnId != openReturnTransaction.getReturnId())
			return -1;

		return openReturnTransaction.getPrice();

	}

	@Override
	public double returnCreditCardPayment(Integer returnId, String creditCard)
			throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		System.out.println("Executing returnCreditCardPayment...");
		if (returnId <= 0 || returnId == null)
			throw new InvalidTransactionIdException();
		if (creditCard.equals(null) || !checkLuhn(creditCard))
			throw new InvalidCreditCardException();
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openReturnTransaction == null || returnId != openReturnTransaction.getReturnId())
			return -1;

		Connection conn = null;
		try {
			String url = "jdbc:sqlite:creditCards.sqlite";
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		double price = openReturnTransaction.getPrice();
		try {
			String getCard = "SELECT balance FROM creditCards WHERE creditCardNumber = ?";
			PreparedStatement pstmt = conn.prepareStatement(getCard);
			pstmt.setString(1, creditCard);
			ResultSet rs = pstmt.executeQuery();
			if (rs == null) // card not registered
				return -1;
			// only 1 result because creditCard is primary key
			pstmt.close();
			double credit = rs.getDouble("balance");
			rs.close();
			double newCredit = credit + price;
			String updateCard = "UPDATE creditCards SET balance = ? WHERE creditCardNumber = ?";
			pstmt = conn.prepareStatement(updateCard);
			pstmt.setDouble(1, newCredit);
			pstmt.setString(2, creditCard);
			pstmt.executeUpdate(updateCard);
			pstmt.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return -1;
		}

		return price;

	}

	@Override
	public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {

		System.out.println("Executing recordBalanceUpdate...");
		Connection conn = null;
		boolean positiveBalance = false;
		
		if (!userLoggedIn.getRole().equals("Administrator") &&  !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException("Either the user doesn't have the rights to perform this action or doesn't exist");

		if (computeBalance() + toBeAdded < 0) {
			System.out.println("The operation can't be performed due to negative balance");
			return positiveBalance;
		}
		try {
			conn = dbAccess();
			String sql2 = "INSERT INTO balanceOperation (date, money, type) VALUES (?,?,?)";
			PreparedStatement statement2 = conn.prepareStatement(sql2);
			statement2.setString(1, LocalDate.now().toString());
			statement2.setDouble(2, toBeAdded < 0 ? -toBeAdded : toBeAdded);
			statement2.setString(3, toBeAdded < 0 ? "DEBIT" : "CREDIT");
			statement2.executeUpdate();
			positiveBalance = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return positiveBalance;

	}

	@Override
	public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {

		System.out.println("Executing getCreditsAndDebits...");
		List<BalanceOperation> bo = new ArrayList<BalanceOperation>();
		LocalDate tmp;
		Connection conn = null;
		if (from != null && to != null && from.isAfter(to)) {
			tmp = to;
			to = from;
			from = tmp;
		}
		if (!userLoggedIn.getRole().equals("Administrator") &&  !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException("Either the user doesn't have the rights to perform this action or doesn't exist");
		try {
			conn = dbAccess();
			String sql = "SELECT * FROM balanceOperation WHERE date >= ? AND date <= ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, from == null ? "0001-01-01" : from.toString());
			statement.setString(2, to == null ? "9999-12-31" : to.toString());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				bo.add(new BalanceOperationImpl(rs.getInt("balanceId"), LocalDate.parse(rs.getString("date")),
						rs.getDouble("money"), rs.getString("type")));
//				System.out.println(bo.get(bo.size()-1).toString());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return bo;

	}

	@Override
	public double computeBalance() throws UnauthorizedException {

		System.out.println("Executing computeBalance...");
		Connection conn = null;
		double balance = 0;
		String type = null;
		double money = 0;
		if (!userLoggedIn.getRole().equals("Administrator") &&  !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException("Either the user doesn't have the rights to perform this action or doesn't exist");
		try {
			conn = dbAccess();
			String sql = "SELECT money, type FROM balanceOperation";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				type = rs.getString("type");
				if (type.equals("DEBIT"))
					money = -rs.getDouble("money");
				else
					money = rs.getDouble("money");
				balance += money;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return balance;

	}

}
