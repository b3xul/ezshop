package it.polito.ezshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.data.Implementations.SaleTransactionImpl;
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

public class EZShop implements EZShopInterface {

	// LinkedList<BalanceOperation> accountBook = new
	// LinkedList<BalanceOperation>();
	// User loggedUser = new User();
	Boolean loggedUser = true;
	ArrayList<ProductType> productTypes = new ArrayList<ProductType>();
	SaleTransactionImpl openSaleTransaction = null;

	// method to open the connection to database
	public Connection dbAccess() {

		Connection conn = null;
		try {
			String url = "jdbc:sqlite:prova_java.db";
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return conn;

	}

	// method to close the connection to database
	public void dbClose(Connection conn) {

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}

	@Override
	public void reset() {

	}

	@Override
	public Integer createUser(String username, String password, String role)
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

		return null;

	}

	@Override
	public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		return false;

	}

	@Override
	public List<User> getAllUsers() throws UnauthorizedException {

		return null;

	}

	@Override
	public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		return null;

	}

	@Override
	public boolean updateUserRights(Integer id, String role)
			throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {

		return false;

	}

	@Override
	public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {

		return null;

	}

	@Override
	public boolean logout() {

		return false;

	}

	@Override
	public Integer createProductType(String description, String productCode, double pricePerUnit, String note)
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException {

		return null;

	}

	@Override
	public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
			throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, UnauthorizedException {

		return false;

	}

	@Override
	public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {

		return false;

	}

	@Override
	public List<ProductType> getAllProductTypes() throws UnauthorizedException {

		return null;

	}

	@Override
	public ProductType getProductTypeByBarCode(String barCode)
			throws InvalidProductCodeException, UnauthorizedException {

		return null;

	}

	@Override
	public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {

		return null;

	}

	@Override
	public boolean updateQuantity(Integer productId, int toBeAdded)
			throws InvalidProductIdException, UnauthorizedException {

		return false;

	}

	@Override
	public boolean updatePosition(Integer productId, String newPos)
			throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {

		return false;

	}

	@Override
	public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException,
			InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

		return null;

	}

	@Override
	public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
			throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException,
			UnauthorizedException {

		return null;

	}

	@Override
	public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {

		return false;

	}

	@Override
	public boolean recordOrderArrival(Integer orderId)
			throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {

		return false;

	}

	@Override
	public List<Order> getAllOrders() throws UnauthorizedException {

		return null;

	}

	@Override
	public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {

		return null;

	}

	@Override
	public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
			throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException,
			UnauthorizedException {

		return false;

	}

	@Override
	public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

		return false;

	}

	@Override
	public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

		return null;

	}

	@Override
	public List<Customer> getAllCustomers() throws UnauthorizedException {

		return null;

	}

	@Override
	public String createCard() throws UnauthorizedException {

		return null;

	}

	@Override
	public boolean attachCardToCustomer(String customerCard, Integer customerId)
			throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {

		return false;

	}

	@Override
	public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
			throws InvalidCustomerCardException, UnauthorizedException {

		return false;

	}

	@Override
	public Integer startSaleTransaction() throws UnauthorizedException {

		if (loggedUser == null)
			throw new UnauthorizedException("User not logged in");
		Connection c = null;
		Statement stmt = null;
		Integer id = -1;
		try {
			c = dbAccess();
			stmt = c.createStatement();
			String sql = "SELECT seq FROM sqlite_sequence WHERE name=\"saleTransaction\"";
			ResultSet rs = stmt.executeQuery(sql);
			id = rs.getInt("seq") + 1;
			rs.close();
			stmt.close();
			// c.setAutoCommit(false); // all operations until the next c.commit will be
			// included in a single db
			// transaction
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
//		System.out.println(id);
//		System.out.println((BalanceOperation) new SaleTransactionImpl(id));
		openSaleTransaction = new SaleTransactionImpl(id); // transaction will be added to the db only
															// when it ends
		return id;

	}

	@Override
	public boolean addProductToSale(Integer transactionId, String productCode, int amount)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			UnauthorizedException {

		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (amount <= 0)
			throw new InvalidQuantityException("Amount to add cannot be <=0");
		if (loggedUser == null)
			throw new UnauthorizedException("User not logged in");
		ProductType productType = getProductTypeByBarCode(productCode); // could throw InvalidProductCodeException
		System.out.println(productType);
		if (productType == null || amount > productType.getQuantity() || openSaleTransaction == null
				|| transactionId != openSaleTransaction.getTicketNumber())
			return false;
		openSaleTransaction.upsertEntry(productType, amount);
		return true;

	}

	@Override
	public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			UnauthorizedException {

		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (amount <= 0)
			throw new InvalidQuantityException("Amount to remove cannot be <=0");
		if (loggedUser == null)
			throw new UnauthorizedException("User not logged in");
		ProductType productType = getProductTypeByBarCode(productCode); // could throw InvalidProductCodeException
		if (productType == null || openSaleTransaction == null
				|| transactionId != openSaleTransaction.getTicketNumber())
			return false;
		Boolean result = openSaleTransaction.removeAmountFromEntry(productType, amount);
		return result;

	}

	@Override
	public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException,
			UnauthorizedException {

		return false;

	}

	@Override
	public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
			throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

		return false;

	}

	@Override
	public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

		return 0;

	}

	@Override
	public boolean endSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		return false;

	}

	@Override
	public boolean deleteSaleTransaction(Integer saleNumber)
			throws InvalidTransactionIdException, UnauthorizedException {

		return false;

	}

	@Override
	public SaleTransaction getSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		return null;

	}

	@Override
	public Integer startReturnTransaction(Integer saleNumber)
			throws /* InvalidTicketNumberException, */InvalidTransactionIdException, UnauthorizedException {

		return null;

	}

	@Override
	public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

		return false;

	}

	@Override
	public boolean endReturnTransaction(Integer returnId, boolean commit)
			throws InvalidTransactionIdException, UnauthorizedException {

		return false;

	}

	@Override
	public boolean deleteReturnTransaction(Integer returnId)
			throws InvalidTransactionIdException, UnauthorizedException {

		return false;

	}

	@Override
	public double receiveCashPayment(Integer ticketNumber, double cash)
			throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {

		return 0;

	}

	@Override
	public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard)
			throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		return false;

	}

	@Override
	public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {

		return 0;

	}

	@Override
	public double returnCreditCardPayment(Integer returnId, String creditCard)
			throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		return 0;

	}

	@Override
	public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {

		return false;

	}

	@Override
	public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {

		return null;

	}

	@Override
	public double computeBalance() throws UnauthorizedException {

		return 0;

	}

}
