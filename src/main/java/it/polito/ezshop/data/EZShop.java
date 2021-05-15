package it.polito.ezshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.data.Implementations.ProductTypeImpl;
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
	// ArrayList<ProductType> productTypes = new ArrayList<ProductType>();
	SaleTransactionImpl openSaleTransaction = null;

	// method to open the connection to database
	public Connection dbAccess() {

		Connection conn = null;
		try {
			// String url =
			// "jdbc:sqlite:C:\\Users\\andre\\OneDrive\\Desktop\\prova_java.db";
			String url = "jdbc:sqlite:prova_java.db";
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

	// method to create a new product
	@Override
	public Integer createProductType(String description, String productCode, double pricePerUnit, String note)
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException {

		Integer id;
		Connection conn = null;
		try {
			if (description == null || description == "")
				throw new InvalidProductDescriptionException("invalid description");
			else if (productCode == null || productCode == "")
				throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
			else if (productCode.length() < 12 || productCode.length() > 14)
				throw new InvalidProductCodeException("invalid barcode: wrong length");
			else if (!isStringOnlyNumbers(productCode))
				throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
			else if (pricePerUnit <= 0)
				throw new InvalidPricePerUnitException("invalid price");
			else if (false)
				throw new UnauthorizedException("user error");
			else {
				ProductTypeImpl newProduct = new ProductTypeImpl(note, description, productCode, pricePerUnit);
				conn = dbAccess();
				String sql1 = "SELECT barcode FROM product WHERE barcode = '" + productCode + "'";
				Statement statement1 = conn.createStatement();
				ResultSet result1 = statement1.executeQuery(sql1);
				if (result1.next()) {
					System.out.println("a product with this barcode already exists");
					id = -1;
				} else {
					// Statement to insert a new product into the database, populating its fields
					// (except quantity and location)
					String sql = "INSERT INTO product (description, price, barcode, location, quantity, note, discount) VALUES ('"
							+ description + "', '" + pricePerUnit + "', '" + productCode + "', '"
							+ newProduct.getLocation() + "', " + newProduct.getQuantity() + ", '" + note + "', "
							+ newProduct.getDiscountRate() + ")";
					Statement statement = conn.createStatement();
					statement.executeUpdate(sql);
					System.out.println("Product created");
					// Statement to select the ID of the new product created, that is what the
					// method returns: the ID is automatically generated by the database,
					// which sets for each new entry a number as ID, increasing the value of the
					// last ID created. The ID is the primary key of the database, so
					// it's unique and it cannot be set at a value already used.
					String sql2 = "SELECT id FROM product WHERE id=(SELECT max(id) FROM product)";
					Statement statement2 = conn.createStatement();
					ResultSet result = statement2.executeQuery(sql2);
					id = result.getInt("id");
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			id = -1;
		} finally {
			dbClose(conn);
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
		try {
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
			else if (newPrice <= 0)
				throw new InvalidPricePerUnitException("invalid price");
			else if (false)
				throw new UnauthorizedException("user error");
			else {
				Long.parseLong(newCode);
				conn = dbAccess();
				// Statement to select the barcode of a product when the id does not match: it
				// is needed to control if the new barcode is already assigned
				String sql1 = "SELECT barcode FROM product WHERE barcode = '" + newCode + "'AND id != '" + id + "'";
				Statement statement1 = conn.createStatement();
				ResultSet result1 = statement1.executeQuery(sql1);
				// Statement to select the id of a product when the id matches: it is needed to
				// control if the product exists
				String sql2 = "SELECT id FROM product WHERE id == '" + id + "'";
				Statement statement2 = conn.createStatement();
				ResultSet result2 = statement2.executeQuery(sql2);
				if (result1.next()) {
					System.out.println("a product with this barcode already exists");
					success = false;
				} else if (!result2.next()) {
					System.out.println("a product with this id does not exists");
					success = false;
				} else {
					// Statement to update the fields of a product when the bercode matches: the
					// fields updated are description, barcode, price, note
					String sql = "UPDATE product SET description = '" + newDescription + "', barcode = '" + newCode
							+ "', price = " + newPrice + ", note = '" + newNote + "' WHERE id = '" + id + "'";
					Statement statement = conn.createStatement();
					statement.executeUpdate(sql);
					System.out.println("product correctly updated");
					success = true;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			success = false;
		} finally {
			dbClose(conn);
		}
		return success;

	};

	// method to delete a product
	@Override
	public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {

		Boolean success = false;
		Connection conn = null;
		try {
			if (id <= 0 || id == null)
				throw new InvalidProductIdException("invalid ID");
			else if (false)
				throw new UnauthorizedException("user error");
			else {
				conn = dbAccess();
				// Statement to delete a product from database
				String sql = "DELETE FROM product WHERE id =" + id;
				Statement statement = conn.createStatement();
				statement.executeUpdate(sql);
				success = true;
				System.out.println("product deleted");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			success = false;
		} finally {
			dbClose(conn);
		}
		return success;

	}

	// method to get the list of all products
	@Override
	public List<ProductType> getAllProductTypes() throws UnauthorizedException {

		List<ProductType> inventory = new ArrayList<ProductType>();
		Connection conn = null;
		try {
			if (false)
				throw new UnauthorizedException("user error");
			else {
				conn = dbAccess();
				// Statement to select all the fields from the database for each product
				String sql = "SELECT * FROM product";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()) {
					String n = result.getString("note");
					String d = result.getString("description");
					String b = result.getString("barcode");
					Double p = result.getDouble("price");
					Integer id = result.getInt("id");
					Integer q = result.getInt("quantity");
					String l = result.getString("location");
					Double dr = result.getDouble("discountRate");
					// Creation of a new ProductTypeImpl for each iteration: the object created is
					// appended to the list that will be returned
					ProductTypeImpl product = new ProductTypeImpl(n, d, b, p);
					product.setId(id);
					product.setQuantity(q);
					if (!l.equals(" "))
						product.setLocation(l);
					product.setDiscountRate(dr);
					inventory.add(product);
				}
				System.out.println("inventory:");
				// for(ProductType p: inventory) p.print();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return inventory;

	}

	// method to get a product by barcode
	@Override
	public ProductType getProductTypeByBarCode(String barCode)
			throws InvalidProductCodeException, UnauthorizedException {

		ProductTypeImpl product = new ProductTypeImpl();
		Connection conn = null;
		try {
			if (barCode == null || barCode == "")
				throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
			else if (barCode.length() < 12 || barCode.length() > 14)
				throw new InvalidProductCodeException("invalid barcode: wrong length");
			else if (!isStringOnlyNumbers(barCode))
				throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
			else if (false)
				throw new UnauthorizedException("user error");
			else {
				conn = dbAccess();
				// Statement to select all the fields of a product when the barcode matches
				String sql = "SELECT * FROM product WHERE barcode = '" + barCode + "'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (!result.next()) {
					System.out.println("no product with this barcode");
					product = null;
				} else {
					String n = result.getString("note");
					String d = result.getString("description");
					String b = result.getString("barcode");
					Double p = result.getDouble("price");
					Integer id = result.getInt("id");
					Integer q = result.getInt("quantity");
					String l = result.getString("location");
					Double dr = result.getDouble("discountRate");
					// Creation of a new ProductTypeImpl: the object created is what then will be
					// returned
					product.setBarCode(b);
					product.setNote(n);
					product.setPricePerUnit(p);
					product.setProductDescription(d);
					product.setId(id);
					product.setQuantity(q);
					if (!l.equals(" "))
						product.setLocation(l);
					product.setDiscountRate(dr);
					System.out.println("product found");
					// product.print();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			product = null;
		} finally {
			dbClose(conn);
		}
		return product;

	}

	public ProductTypeImpl getProductTypeImplByBarCode(String barCode)
			throws InvalidProductCodeException, UnauthorizedException {

		ProductTypeImpl product = new ProductTypeImpl();
		Connection conn = null;
		try {
			if (barCode == null || barCode == "")
				throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
			else if (barCode.length() < 12 || barCode.length() > 14)
				throw new InvalidProductCodeException("invalid barcode: wrong length");
			else if (!isStringOnlyNumbers(barCode))
				throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
			else if (false)
				throw new UnauthorizedException("user error");
			else {
				conn = dbAccess();
				// Statement to select all the fields of a product when the barcode matches
				String sql = "SELECT * FROM product WHERE barcode = '" + barCode + "'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (!result.next()) {
					System.out.println("no product with this barcode");
					product = null;
				} else {
					String n = result.getString("note");
					String d = result.getString("description");
					String b = result.getString("barcode");
					Double p = result.getDouble("price");
					Integer id = result.getInt("id");
					Integer q = result.getInt("quantity");
					String l = result.getString("location");
					Double dr = result.getDouble("discountRate");
					// Creation of a new ProductTypeImpl: the object created is what then will be
					// returned
					product.setBarCode(b);
					product.setNote(n);
					product.setPricePerUnit(p);
					product.setProductDescription(d);
					product.setId(id);
					product.setQuantity(q);
					if (!l.equals(" "))
						product.setLocation(l);
					product.setDiscountRate(dr);
					System.out.println("product found");
					// product.print();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			product = null;
		} finally {
			dbClose(conn);
		}
		return product;

	}

	// method to get a product by description
	@Override
	public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {

		List<ProductType> matchingProducts = new ArrayList<ProductType>();
		Connection conn = null;
		try {
			if (false)
				throw new UnauthorizedException("user error");
			else {
				conn = dbAccess();
				// Statement to select all the fields of a product when the description matches
				String sql = "SELECT DISTINCT * FROM product WHERE description LIKE '%" + description + "%'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()) {
					String n = result.getString("note");
					String d = result.getString("description");
					String b = result.getString("barcode");
					Double p = result.getDouble("price");
					Integer id = result.getInt("id");
					Integer q = result.getInt("quantity");
					String l = result.getString("location");
					Double dr = result.getDouble("discount");
					// Creation of a new ProductTypeImpl for each iteration: the object created is
					// appended to the list that will be returned
					ProductTypeImpl product = new ProductTypeImpl(n, d, b, p);
					product.setId(id);
					product.setQuantity(q);
					if (!l.equals(" "))
						product.setLocation(l);
					product.setDiscountRate(dr);
					matchingProducts.add(product);
				}
				System.out.println("products found:");
				// for(ProductType p: matchingProducts) p.print();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return matchingProducts;

	}

	// method to update quantity of product
	@Override
	public boolean updateQuantity(Integer productId, int toBeAdded)
			throws InvalidProductIdException, UnauthorizedException {

		boolean success = false;
		Connection conn = null;
		try {
			if (productId <= 0 || productId == null)
				throw new InvalidProductIdException("ID incorrect");
			else if (false)
				throw new UnauthorizedException("user error");
			else {
				conn = dbAccess();
				// Statement to select quantity and location of a product given its ID: those
				// value are needed for future checks
				String sql = "SELECT quantity, location FROM product WHERE id = '" + productId + "'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (!result.next()) {
					System.out.println("no product with this ID");
					success = false;
				} else {
					Integer oldQuantity = result.getInt("quantity");
					String location = result.getString("location");
					if (oldQuantity + toBeAdded <= 0) {
						System.out.println("subtracting too much");
						success = false;
					} else if (location.equals("") || location.equals(" ")) {
						System.out.println("no location for that product");
						success = false;
					} else {
						int newQuantity = oldQuantity + toBeAdded;
						// Statement to update quantity of a product given its ID
						String sql2 = "UPDATE product SET quantity = " + newQuantity + " WHERE id = '" + productId
								+ "'";
						Statement statement2 = conn.createStatement();
						statement2.executeUpdate(sql2);
						System.out.println("quantity correctly updated");
						success = true;
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			success = false;
		} finally {
			dbClose(conn);
		}
		return success;

	}

	// method to update the position of a product
	@Override
	public boolean updatePosition(Integer productId, String newPos)
			throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {

		boolean success = false;
		Connection conn = null;
		conn = dbAccess();
		try {
			if (productId <= 0 || productId == null)
				throw new InvalidProductIdException("ID incorrect");
			else if (newPos == null || newPos == "") {
				// Statement to update the location of a product given its ID to an empty string
				String sql2 = "UPDATE product SET location = '" + "" + "' WHERE id = '" + productId + "'";
				Statement statement2 = conn.createStatement();
				statement2.executeUpdate(sql2);
				success = true;
			} else if (false)
				throw new UnauthorizedException("user error");
			else if (newPos.split(" ").length != 3)
				throw new InvalidLocationException("wrong format for location: wrong field(s)");
			else if (!isStringOnlyNumbers(newPos.split(" ")[0]))
				throw new InvalidLocationException("wrong format for location: aisle must be a number");
			else if (!isStringOnlyAlphabet(newPos.split(" ")[1]))
				throw new InvalidLocationException("wrong format for location: ID must contains only character");
			else if (!isStringOnlyNumbers(newPos.split(" ")[2]))
				throw new InvalidLocationException("wrong format for location: level must be a number");
			else {
				Integer aisleNumber = Integer.parseInt(newPos.split(" ")[0]);
				String alphabeticId = newPos.split(" ")[1];
				Integer levelNumber = Integer.parseInt(newPos.split(" ")[2]);
				if (aisleNumber == null || alphabeticId == null || alphabeticId == "" || levelNumber == null)
					throw new InvalidLocationException("wrong format for location");
				else {
					// Statement to select the location of a product given its ID
					String sql1 = "SELECT location FROM product WHERE id = '" + productId + "'";
					Statement statement1 = conn.createStatement();
					ResultSet result1 = statement1.executeQuery(sql1);
					if (!result1.next()) {
						System.out.println("no product with this ID");
						success = false;
					} else {
						// Statement to select the location of a product given its ID and location:
						// those value are needed for future checks
						String sql3 = "SELECT location FROM product WHERE location = '" + newPos + "' AND id != '"
								+ productId + "'";
						Statement statement3 = conn.createStatement();
						ResultSet result3 = statement3.executeQuery(sql3);
						if (result3.next()) {
							System.out.println("position already assigned");
							success = false;
						} else {
							// Statement to update the location of a product given its ID
							String sql4 = "UPDATE product SET location = '" + newPos + "' WHERE id = '" + productId
									+ "'";
							Statement statement4 = conn.createStatement();
							statement4.executeUpdate(sql4);
							System.out.println("position correctly updated");
							success = true;
						}
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			success = false;
		} finally {
			dbClose(conn);
		}
		return success;

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
		Integer id = 0; // 0=error
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
		ProductTypeImpl productType = getProductTypeImplByBarCode(productCode); // could throw
																				// InvalidProductCodeException
		System.out.println(productType);
		if (productType == null || amount > productType.getQuantity() || openSaleTransaction == null
				|| transactionId != openSaleTransaction.getTicketNumber())
			return false;
		String barCode = productType.getBarCode();
		String productDescription = productType.getProductDescription();
		double pricePerUnit = productType.getPricePerUnit();
		double discountRate = productType.getDiscountRate();
		openSaleTransaction.upsertEntry(barCode, productDescription, pricePerUnit, discountRate, amount);
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
		ProductTypeImpl productType = getProductTypeImplByBarCode(productCode); // could throw
																				// InvalidProductCodeException
		if (productType == null || openSaleTransaction == null
				|| transactionId != openSaleTransaction.getTicketNumber())
			return false;
		String barCode = productType.getBarCode();
		double pricePerUnit = productType.getPricePerUnit();
		double discountRate = productType.getDiscountRate();
		Boolean result = openSaleTransaction.removeAmountFromEntry(barCode, pricePerUnit, discountRate, amount);
		return result;

	}

	@Override
	public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException,
			UnauthorizedException {

		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (discountRate < 0 || discountRate >= 1)
			throw new InvalidDiscountRateException("Discount Rate must be >=0 and <1");
		if (loggedUser == null)
			throw new UnauthorizedException("User not logged in");
		ProductTypeImpl productType = getProductTypeImplByBarCode(productCode); // could throw
																				// InvalidProductCodeException
		if (productType == null || openSaleTransaction == null
				|| transactionId != openSaleTransaction.getTicketNumber())
			return false;
		openSaleTransaction.setDiscountRateToProduct(productType.getBarCode(), discountRate);
		return true;

	}

	@Override
	public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
			throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (discountRate < 0 || discountRate >= 1)
			throw new InvalidDiscountRateException("Discount Rate must be >=0 and <1");
		if (loggedUser == null)
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber())
			return false;
		openSaleTransaction.setDiscountRate(discountRate);
		return true;

	}

	@Override
	public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

		int points = -1;
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (loggedUser == null)
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

		String insertSale = "INSERT INTO saleTransaction(ticketNumber,price,discountRate) VALUES(?,?,?)";
		String insertTicketEntry = "INSERT INTO ticketEntry(ticketNumber,barCode,productDescription,pricePerUnit,discountRate,amount) VALUES(?,?,?,?,?,?)";
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (loggedUser == null)
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber()
				|| getSaleTransaction(transactionId) != null)
			return false;
		try (Connection conn = this.dbAccess();) {
			PreparedStatement pstmt = conn.prepareStatement(insertSale);
			pstmt.setInt(1, openSaleTransaction.getTicketNumber());
			pstmt.setDouble(2, openSaleTransaction.getPrice());
			pstmt.setDouble(3, openSaleTransaction.getDiscountRate());
			pstmt.executeUpdate();
			pstmt.close();
			for (TicketEntry entry : openSaleTransaction.getEntries()) {
				pstmt = conn.prepareStatement(insertTicketEntry);
				pstmt.setInt(1, openSaleTransaction.getTicketNumber());
				pstmt.setString(2, entry.getBarCode());
				pstmt.setString(3, entry.getProductDescription());
				pstmt.setDouble(4, entry.getPricePerUnit());
				pstmt.setDouble(5, entry.getDiscountRate());
				pstmt.setInt(6, entry.getAmount());
				pstmt.executeUpdate();
				pstmt.close();
			}
			openSaleTransaction = null;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
//		finally {
//			dbClose(conn);
//		} this is not necessary if we use the try-with-resources sintax
		return true;

	}

	@Override
	public boolean deleteSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		String deleteSale = "DELETE FROM saleTransaction WHERE ticketNumber=?";
		String deleteTicketEntries = "DELETE FROM ticketEntry WHERE ticketNumber=?";
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (loggedUser == null)
			throw new UnauthorizedException("User not logged in");
		try (Connection conn = this.dbAccess();) {
			PreparedStatement pstmt = conn.prepareStatement(deleteSale);
			pstmt.setInt(1, transactionId);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = conn.prepareStatement(deleteTicketEntries);
			pstmt.setInt(1, transactionId);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;

	}

	@Override
	public SaleTransaction getSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		String getSale = "SELECT price,discountRate FROM saleTransaction WHERE ticketNumber=?";
		String getTicketEntries = "SELECT barCode,productDescription,pricePerUnit,discountRate,amount FROM ticketEntry WHERE ticketNumber=?";
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (loggedUser == null)
			throw new UnauthorizedException("User not logged in");
		SaleTransactionImpl result = null;
		try (Connection conn = this.dbAccess();) {
			PreparedStatement pstmt = conn.prepareStatement(getSale);
			pstmt.setInt(1, transactionId);
			ResultSet rs = pstmt.executeQuery();
			if (rs == null)
				return null;
			// only 1 result because ticketNumber is primary key
			result = new SaleTransactionImpl(transactionId);
			result.setPrice(rs.getDouble("price"));
			result.setDiscountRate(rs.getDouble("discountRate"));
			System.out.println(result.getTicketNumber() + " " + result.getDiscountRate());
			pstmt.close();
			pstmt = conn.prepareStatement(getTicketEntries);
			pstmt.setInt(1, transactionId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result.addEntry(rs.getString("barCode"), rs.getString("productDescription"),
						rs.getDouble("pricePerUnit"), rs.getDouble("discountRate"), rs.getInt("amount"));
			}
			System.out.println(result);
			pstmt.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return result;

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
