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
import java.util.List;

import it.polito.ezshop.data.Implementations.BalanceOperationImpl;
import it.polito.ezshop.data.Implementations.OrderImpl;
import it.polito.ezshop.data.Implementations.ProductTypeImpl;
import it.polito.ezshop.data.Implementations.ReturnTransactionImpl;
import it.polito.ezshop.data.Implementations.SaleTransactionImpl;
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

public class EZShop implements EZShopInterface {

	// LinkedList<BalanceOperation> accountBook = new
	// LinkedList<BalanceOperation>();
	// User loggedUser = new User();
	// Boolean loggedUser = true;
	User userLoggedIn = new UserImpl();
	// ArrayList<ProductType> productTypes = new ArrayList<ProductType>();
	SaleTransactionImpl openSaleTransaction = null;
	ReturnTransactionImpl openReturnTransaction = null;

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

	// method to verify the correctness of the barcode
	public static boolean isBarcodeValid(String barcode) {

		boolean valid = false;
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

	@Override
	public void reset() {

	}

	@Override
	public Integer createUser(String username, String password, String role)
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

		Connection conn = null;
		try {
			if (username != null && username.isEmpty() == false) {
				if (password != null && password.isEmpty() == false) {
					if (role != null
							&& (role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManage"))) {
						conn = dbAccess();
						String sql = "SELECT seq FROM sqlite_sequence WHERE name = 'User'";
						Statement statement = conn.createStatement();
						ResultSet rs = statement.executeQuery(sql);
						int id = rs.getInt("seq") + 1;
						sql = "INSERT INTO User (id, username, password, role) VALUES(?,?,?,?)";
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, id);
						pstmt.setString(2, username);
						pstmt.setString(3, password);
						pstmt.setString(4, role);
						int res = pstmt.executeUpdate();
						if (res == 0) { // no modified row
							return -1;
						} else {
							return id;
						}
					} else
						throw new InvalidRoleException("Invalid role");
				} else
					throw new InvalidPasswordException("Invalid password");
			} else
				throw new InvalidUsernameException("Invalid username");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return -1;
		} finally {
			dbClose(conn);
		}

	}

	@Override
	public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		Connection conn = null;
		try {
			if (userLoggedIn.getRole().equals("Administrator")) {
				if (id != null && id > 0) {
					conn = dbAccess();
					String sql = "DELETE FROM User WHERE id = ?";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, id);
					int rs = pstmt.executeUpdate();
					if (rs == 0) { // no modified row
						return false;
					} else {
						return true;
					}
				} else
					throw new InvalidUserIdException("Invalid User");
			} else
				throw new UnauthorizedException("Permission denied");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			dbClose(conn);
		}

	}

	// ritornare la lista vuota o null in caso di eccezione?
	@Override
	public List<User> getAllUsers() throws UnauthorizedException {

		Connection conn = null;
		List<User> users = new ArrayList<User>();
		try {
			if (userLoggedIn.getRole().equals("Administrator")) {
				conn = dbAccess();
				String sql = "SELECT id, username, password, role FROM User";
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {
					users.add(new UserImpl(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
							rs.getString("role")));
				}
				return users;
			} else
				throw new UnauthorizedException("Permission denied");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			dbClose(conn);
		}

	}

	// ritornare null se scateno l'eccezione?
	@Override
	public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		Connection conn = null;
		try {
			if (userLoggedIn.getRole().equals("Administrator")) {
				if (id != null && id > 0) {
					conn = dbAccess();
					String sql = "SELECT id, username, password, role FROM User WHERE id = " + id;
					Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(sql);
					if (rs.next()) {
						return new UserImpl(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
								rs.getString("role"));
					} else {
						return null;
					}
				} else
					throw new InvalidUserIdException("Invalid user");
			} else
				throw new UnauthorizedException("Permission denied");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			dbClose(conn);
		}

	}

	@Override
	public boolean updateUserRights(Integer id, String role)
			throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {

		Connection conn = null;
		try {
			if (userLoggedIn.getRole().equals("Administrator")) {
				if (id != null && id > 0) {
					if (role != null
							&& (role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManage"))) {
						conn = dbAccess();
						String sql = "UPDATE User SET role = ? WHERE id = ?";
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, role);
						pstmt.setInt(2, id);
						int rs = pstmt.executeUpdate();
						if (rs == 0) { // no modified row
							return false;
						} else {
							return true;
						}
					} else
						throw new InvalidRoleException("Invalid role");
				} else
					throw new InvalidUserIdException("Invalid user");
			} else
				throw new UnauthorizedException("Permission denied");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			dbClose(conn);
		}

	}

	@Override
	public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {

		Connection conn = null;
		try {
			if (username != null && username.isEmpty() == false) {
				if (password != null && password.isEmpty() == false) {
					conn = dbAccess();
					String sql = "SELECT id, username, password, role FROM User WHERE username = '" + username + "'";
					Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(sql);
					if (rs.next() == true && password.equals(rs.getString("password"))) {
						// System.out.println("Log in");
						userLoggedIn.setId(rs.getInt("id"));
						userLoggedIn.setUsername(rs.getString("username"));
						userLoggedIn.setPassword(rs.getString("password"));
						userLoggedIn.setRole(rs.getString("role"));
						return new UserImpl(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
								rs.getString("role"));
					} else {
						return null;
					}
				} else
					throw new InvalidPasswordException("Password not valid");
			} else
				throw new InvalidUsernameException("Username not valid");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			dbClose(conn);
		}

	}

	@Override
	public boolean logout() {

		if (userLoggedIn.getId() != -1) { // no users already logged in
			userLoggedIn.setId(-1);
			userLoggedIn.setUsername("");
			userLoggedIn.setPassword("");
			userLoggedIn.setRole("");
			// System.out.println("Log out");
			return true;
		} else
			return false;

	}

	// method to create a new product
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
		else if (false)
			throw new UnauthorizedException("user error");
		else {
			ProductTypeImpl newProduct = new ProductTypeImpl(note, description, productCode, pricePerUnit);
			conn = dbAccess();
			String sql1 = "SELECT barcode FROM product WHERE barcode = '" + productCode + "'";
			Statement statement1;
			try {
				statement1 = conn.createStatement();
				ResultSet result1 = statement1.executeQuery(sql1);
				if (result1.next()) {
					System.out.println("a product with this barcode already exists");
					id = -1;
				} else {
					// Statement to insert a new product into the database, populating its fields
					// (except quantity and location)
					String sql = "INSERT INTO product (description, price, barcode, location, quantity, note, discountRate) VALUES ('"
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
			} catch (SQLException e) {
				e.printStackTrace();
				id = -1;
			}
		}
		dbClose(conn);
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
		else if (false)
			throw new UnauthorizedException("user error");
		else {
			Long.parseLong(newCode);
			conn = dbAccess();
			// Statement to select the barcode of a product when the id does not match: it
			// is needed to control if the new barcode is already assigned
			String sql1 = "SELECT barcode FROM product WHERE barcode = '" + newCode + "'AND id != '" + id + "'";
			Statement statement1;
			try {
				statement1 = conn.createStatement();
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
			} catch (SQLException e) {
				e.printStackTrace();
				success = false;
			}
		}
		dbClose(conn);
		return success;

	};

	// method to delete a product
	@Override
	public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {

		Boolean success = false;
		Connection conn = null;
		if (id <= 0 || id == null)
			throw new InvalidProductIdException("invalid ID");
		else if (false)
			throw new UnauthorizedException("user error");
		else {
			conn = dbAccess();
			// Statement to delete a product from database
			String sql = "DELETE FROM product WHERE id =" + id;
			Statement statement;
			try {
				statement = conn.createStatement();
				statement.executeUpdate(sql);
				success = true;
				System.out.println("product deleted");
			} catch (SQLException e) {
				e.printStackTrace();
				success = false;
			}
		}
		dbClose(conn);
		return success;

	}

	// method to get the list of all products
	@Override
	public List<ProductType> getAllProductTypes() throws UnauthorizedException {

		List<ProductType> inventory = new ArrayList<ProductType>();
		Connection conn = null;
		if (false)
			throw new UnauthorizedException("user error");
		else {
			conn = dbAccess();
			// Statement to select all the fields from the database for each product
			String sql = "SELECT * FROM product";
			Statement statement;
			try {
				statement = conn.createStatement();
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
					if (!l.isBlank())
						product.setLocation(l);
					product.setDiscountRate(dr);
					inventory.add(product);
				}
				System.out.println("inventory:");
				// for(ProductType p: inventory) p.print();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbClose(conn);
		return inventory;

	}

	// method to get a product by barcode
	@Override
	public ProductType getProductTypeByBarCode(String barCode)
			throws InvalidProductCodeException, UnauthorizedException {

		ProductTypeImpl product = new ProductTypeImpl();
		Connection conn = null;
		if (barCode == null || barCode == "")
			throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
		else if (barCode.length() < 12 || barCode.length() > 14)
			throw new InvalidProductCodeException("invalid barcode: wrong length");
		else if (!isStringOnlyNumbers(barCode))
			throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
		else if (!isBarcodeValid(barCode))
			throw new InvalidProductCodeException("invalid barcode: barcode does not respect GTIN specifications");
		else if (false)
			throw new UnauthorizedException("user error");
		else {
			conn = dbAccess();
			// Statement to select all the fields of a product when the barcode matches
			String sql = "SELECT * FROM product WHERE barcode = '" + barCode + "'";
			Statement statement;
			try {
				statement = conn.createStatement();
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
					if (!l.isBlank())
						product.setLocation(l);
					product.setDiscountRate(dr);
					System.out.println("product found");
					// product.print();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbClose(conn);
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
			else if (!isBarcodeValid(barCode))
				throw new InvalidProductCodeException("invalid barcode: barcode does not respect GTIN specifications");
			else if (false)
				throw new UnauthorizedException("user error");
			else {
				conn = dbAccess();
				// Statement to select all the fields of a product when the barcode matches
				String sql = "SELECT * FROM product WHERE barcode = '" + barCode + "'";
				Statement statement;
				statement = conn.createStatement();
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
					if (!l.isBlank())
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

		if (description == "")
			description = null;
		List<ProductType> matchingProducts = new ArrayList<ProductType>();
		Connection conn = null;
		if (false)
			throw new UnauthorizedException("user error");
		else {
			conn = dbAccess();
			// Statement to select all the fields of a product when the description matches
			String sql = "SELECT DISTINCT * FROM product WHERE description LIKE '%" + description + "%'";
			Statement statement;
			try {
				statement = conn.createStatement();
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
					if (!l.isBlank())
						product.setLocation(l);
					product.setDiscountRate(dr);
					matchingProducts.add(product);
				}
				System.out.println("products found:");
				// for(ProductType p: matchingProducts) p.print();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbClose(conn);
		return matchingProducts;

	}

	// method to update quantity of product
	@Override
	public boolean updateQuantity(Integer productId, int toBeAdded)
			throws InvalidProductIdException, UnauthorizedException {

		boolean success = false;
		Connection conn = null;
		if (productId <= 0 || productId == null)
			throw new InvalidProductIdException("ID incorrect");
		else if (false)
			throw new UnauthorizedException("user error");
		else {
			conn = dbAccess();
			// Statement to select quantity and location of a product given its ID: those
			// value are needed for future checks
			String sql = "SELECT quantity, location FROM product WHERE id = '" + productId + "'";
			Statement statement;
			try {
				statement = conn.createStatement();
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
			} catch (SQLException e) {
				e.printStackTrace();
				success = false;
			}
		}
		dbClose(conn);
		return success;

	}

	// method to update the position of a product
	@Override
	public boolean updatePosition(Integer productId, String newPos)
			throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {

		boolean success = false;
		Connection conn = null;
		conn = dbAccess();
		if (productId <= 0 || productId == null)
			throw new InvalidProductIdException("ID incorrect");
		else if (false)
			throw new UnauthorizedException("user error");
		else if (newPos == null || newPos == "") {
			// Statement to update the location of a product given its ID to an empty string
			String sql2 = "UPDATE product SET location = '" + "" + "' WHERE id = '" + productId + "'";
			Statement statement2;
			try {
				statement2 = conn.createStatement();
				statement2.executeUpdate(sql2);
				success = true;
			} catch (SQLException e) {
				e.printStackTrace();
				success = false;
			}
		} else if (newPos.split(" ").length != 3)
			throw new InvalidLocationException("wrong format for location: wrong field(s)");
		else if (!isStringOnlyNumbers(newPos.split(" ")[0]))
			throw new InvalidLocationException("wrong format for location: aisle must be a number");
		else if (!isStringOnlyAlphabet(newPos.split(" ")[1]))
			throw new InvalidLocationException(
					"wrong format for location: ID must contains only alphabetic characters");
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
				Statement statement1;
				try {
					statement1 = conn.createStatement();
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
				} catch (SQLException e) {
					e.printStackTrace();
					success = false;
				}
			}
		}
		dbClose(conn);
		return success;

	}

	@Override
	public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException,
			InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

		Connection conn = null;
		conn = dbAccess();
		int id = -1;
		try {
			if (quantity <= 0)
				throw new InvalidQuantityException("The quantity you've inserted is not accepted");
			if (pricePerUnit <= 0)
				throw new InvalidPricePerUnitException("The price you've inserted is not accepted");
			if (userLoggedIn == null)
				throw new UnauthorizedException("The user doesn't have the rights to perform this action");
			String sql = "SELECT id FROM product WHERE barcode = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, productCode);
			ResultSet rs = statement.executeQuery();
			System.out.println("* * *");
			if (!rs.next())
				throw new InvalidProductCodeException("There is no product with this barcode");
			String sql2 = "SELECT MAX(orderId) AS MaxId FROM order_";
			Statement statement2 = conn.createStatement();
			ResultSet rs2 = statement2.executeQuery(sql2);
			System.out.println("* * *");
			if (!rs2.next())
				id = 1;
			else
				id = rs2.getInt("MaxId") + 1;
			OrderImpl order = new OrderImpl(id, -1, LocalDate.now(), pricePerUnit * quantity, productCode, pricePerUnit,
					quantity, "ISSUED");
			String sql3 = "INSERT INTO order_ (orderId, balanceId, date, money, productCode, pricePerUnit, quantity, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement3 = conn.prepareStatement(sql3);
			statement3.setInt(1, order.getOrderId());
			statement3.setInt(2, order.getBalanceId());
			statement3.setString(3, order.getDate().toString());
			statement3.setDouble(4, order.getMoney());
			statement3.setString(5, order.getProductCode());
			statement3.setDouble(6, order.getPricePerUnit());
			statement3.setInt(7, order.getQuantity());
			statement3.setString(8, order.getStatus());
			statement3.executeUpdate();
			System.out.println("* * *");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return id;

	}

	@Override
	public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
			throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException,
			UnauthorizedException {

		Connection conn = null;
		conn = dbAccess();
		int orderId = -1;
		int balanceId = 0;
		try {
			if (quantity <= 0)
				throw new InvalidQuantityException("The quantity you've inserted is not accepted");
			if (pricePerUnit <= 0)
				throw new InvalidPricePerUnitException("The price you've inserted is not accepted");
			if (userLoggedIn == null)
				throw new UnauthorizedException("The user doesn't have the rights to perform this action");
			String sql = "SELECT id FROM product WHERE barcode = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, productCode);
			ResultSet rs = statement.executeQuery();
			if (!rs.next())
				throw new InvalidProductCodeException("There is no product with this barcode");
			dbClose(conn);
			recordBalanceUpdate(-pricePerUnit * quantity);
			conn = dbAccess();
			String sql2 = "SELECT MAX(balanceId) AS MaxBId FROM balanceOperation";
			Statement statement2 = conn.createStatement();
			ResultSet rs2 = statement2.executeQuery(sql2);
			balanceId = rs2.getInt("MaxBId");
			String sql3 = "SELECT MAX(orderId) AS MaxId FROM order_";
			Statement statement3 = conn.createStatement();
			ResultSet rs3 = statement3.executeQuery(sql3);
			if (!rs3.next())
				orderId = 1;
			else
				orderId = rs3.getInt("MaxId") + 1;
			OrderImpl order = new OrderImpl(orderId, balanceId, LocalDate.now(), pricePerUnit * quantity, productCode,
					pricePerUnit, quantity, "PAYED");
			String sql4 = "INSERT INTO order_ (orderId, balanceId, date, money, productCode, pricePerUnit, quantity, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement4 = conn.prepareStatement(sql4);
			statement4.setInt(1, order.getOrderId());
			statement4.setInt(2, order.getBalanceId());
			statement4.setString(3, order.getDate().toString());
			statement4.setDouble(4, order.getMoney());
			statement4.setString(5, order.getProductCode());
			statement4.setDouble(6, order.getPricePerUnit());
			statement4.setInt(7, order.getQuantity());
			statement4.setString(8, order.getStatus());
			statement4.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return orderId;

	}

	@Override
	public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {

		boolean validOrderId = false;
		Connection conn = null;
		conn = dbAccess();
		int balanceId = 0;
		try {
			if (userLoggedIn == null)
				throw new UnauthorizedException("The user doesn't have the rights to perform this action");
			String sql = "SELECT orderId FROM order_ WHERE orderId = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, orderId);
			ResultSet rs = statement.executeQuery();
			if (!rs.next())
				throw new InvalidOrderIdException("There is no order with this id");
			else
				validOrderId = true;
			String sql2 = "UPDATE order_ SET status = ? WHERE orderId = ?";
			PreparedStatement statement2 = conn.prepareStatement(sql2);
			statement2.setString(1, "PAYED");
			statement2.setInt(2, orderId);
			statement2.executeUpdate();
			String sql3 = "SELECT money FROM order_ WHERE orderId = ?";
			PreparedStatement statement3 = conn.prepareStatement(sql3);
			statement3.setInt(1, orderId);
			ResultSet rs3 = statement3.executeQuery();
			dbClose(conn);
			recordBalanceUpdate(-(rs3.getDouble("money")));
			conn = dbAccess();
			String sql4 = "SELECT MAX(balanceId) AS MaxBId FROM balanceOperation";
			Statement statement4 = conn.createStatement();
			ResultSet rs4 = statement4.executeQuery(sql4);
			balanceId = rs4.getInt("MaxBId");
			String sql5 = "UPDATE order_ SET balanceId = ? WHERE orderId = ?";
			PreparedStatement statement5 = conn.prepareStatement(sql5);
			statement5.setInt(1, balanceId);
			statement5.setInt(2, orderId);
			statement5.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return validOrderId;

	}

	@Override
	public boolean recordOrderArrival(Integer orderId)
			throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {

		boolean valid = false;
		Connection conn = null;
		conn = dbAccess();
		String barcode = null;
		int qty = 0;
		String location = null;
		try {
			if (userLoggedIn == null)
				throw new UnauthorizedException("The user doesn't have the rights to perform this action");
			if (orderId <= 0 || orderId == null)
				throw new InvalidOrderIdException("The order id is not valid");
			String sql = "SELECT productCode, quantity, status FROM order_ WHERE orderId = ? AND status = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, orderId);
			statement.setString(2, "PAYED");
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				barcode = rs.getString("productCode");
				qty = rs.getInt("quantity");
			}
			String sql2 = "SELECT location FROM product WHERE barcode = ?";
			PreparedStatement statement2 = conn.prepareStatement(sql2);
			statement2.setString(1, barcode);
			ResultSet rs2 = statement2.executeQuery();
			location = rs2.getString("location");
			if (location.equals("no location"))
				throw new InvalidLocationException("The product type has no location assigned");
			String sql3 = "UPDATE order_ SET status = ? WHERE orderId = ?";
			PreparedStatement statement3 = conn.prepareStatement(sql3);
			statement3.setString(1, "COMPLETED");
			statement3.setInt(2, orderId);
			statement3.executeUpdate();
			String sql4 = "UPDATE product SET quantity = quantity + ? WHERE barcode = ?";
			PreparedStatement statement4 = conn.prepareStatement(sql4);
			statement4.setInt(1, qty);
			statement4.setString(2, barcode);
			statement4.executeUpdate();
			valid = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return valid;

	}

	@Override
	public List<Order> getAllOrders() throws UnauthorizedException {

		List<Order> orders = new ArrayList<Order>();
		Connection conn = null;
		conn = dbAccess();
		try {
			if (userLoggedIn == null)
				throw new UnauthorizedException("The user doesn't have the rights to perform this action");
			String sql = "SELECT * FROM order_";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				orders.add(new OrderImpl(rs.getInt("orderId"), rs.getInt("balanceId"),
						LocalDate.parse(rs.getString("date")), rs.getDouble("money"), rs.getString("productCode"),
						rs.getDouble("pricePerUnit"), rs.getInt("quantity"), rs.getString("status")));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return orders;

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

		if (userLoggedIn == null)
			throw new UnauthorizedException("User not logged in");
		String getNextAutoincrement = "SELECT seq FROM sqlite_sequence WHERE name=\"saleTransaction\"";
		Integer id = 0; // 0=error
		try (Connection conn = this.dbAccess();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(getNextAutoincrement)) {
			id = rs.getInt("seq") + 1;
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
		if (userLoggedIn == null)
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber())
			return false;
		ProductTypeImpl productType = getProductTypeImplByBarCode(productCode); // could throw
																				// InvalidProductCodeException
		System.out.println(productType);
		if (productType == null || amount > productType.getQuantity())
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
		if (userLoggedIn == null)
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber())
			return false;
		Boolean result = openSaleTransaction.removeAmountFromEntry(productCode, amount);
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
		if (userLoggedIn == null)
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber())
			return false;
		openSaleTransaction.setDiscountRateToProduct(productCode, discountRate);
		return true;

	}

	@Override
	public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
			throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (discountRate < 0 || discountRate >= 1)
			throw new InvalidDiscountRateException("Discount Rate must be >=0 and <1");
		if (userLoggedIn == null)
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
		if (userLoggedIn == null)
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
		String decreaseProductQuantity = "UPDATE product SET quantity=quantity - ? WHERE barcode=?";
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (userLoggedIn == null)
			throw new UnauthorizedException("User not logged in");
		if (openSaleTransaction == null || transactionId != openSaleTransaction.getTicketNumber()
				|| getSaleTransaction(transactionId) != null)
			return false;
		// openSaleTransaction.setBalanceOperation(new BalanceOperationImpl(balanceId,
		// date, money, "DEBIT")); ???
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
				pstmt = conn.prepareStatement(decreaseProductQuantity);
				pstmt.setInt(1, entry.getAmount());
				pstmt.setString(2, entry.getBarCode());
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
		if (userLoggedIn == null)
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
		if (userLoggedIn == null)
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
	public Integer startReturnTransaction(Integer transactionId)
			throws /* InvalidTicketNumberException, */InvalidTransactionIdException, UnauthorizedException {

		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (userLoggedIn == null)
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

		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException("Return id cannot be null or <=0");
		if (userLoggedIn == null)
			throw new UnauthorizedException("User not logged in");
		if (openReturnTransaction == null || returnId != openReturnTransaction.getReturnId())
			return false;
		return true;

	}

	@Override
	public boolean endReturnTransaction(Integer returnId, boolean commit)
			throws InvalidTransactionIdException, UnauthorizedException {

		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException("Return id cannot be null or <=0");
		if (userLoggedIn == null)
			throw new UnauthorizedException("User not logged in");
		if (openReturnTransaction == null || returnId != openReturnTransaction.getReturnId())
			return false;
		return false;

	}

	@Override
	public boolean deleteReturnTransaction(Integer returnId)
			throws InvalidTransactionIdException, UnauthorizedException {

		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException("Return id cannot be null or <=0");
		if (userLoggedIn == null)
			throw new UnauthorizedException("User not logged in");
		if (openReturnTransaction == null || returnId != openReturnTransaction.getReturnId())
			return false;
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

		Connection conn = null;
		boolean positiveBalance = false;
		int id = 1;
		if (computeBalance() + toBeAdded < 0) {
			System.out.println("The operation can't be performed due to negative balance");
			return positiveBalance;
		}
		conn = dbAccess();
		try {
			if (userLoggedIn == null)
				throw new UnauthorizedException("The user doesn't have the rights to perform this action");
			String sql = "SELECT MAX(balanceId) AS MaxId FROM balanceOperation";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
				id = rs.getInt("MaxId") + 1;
			String sql2 = "INSERT INTO balanceOperation (balanceId, date, money, type) VALUES (?,?,?,?)";
			PreparedStatement statement2 = conn.prepareStatement(sql2);
			statement2.setInt(1, id);
			statement2.setString(2, LocalDate.now().toString());
			statement2.setDouble(3, toBeAdded < 0 ? -toBeAdded : toBeAdded);
			statement2.setString(4, toBeAdded < 0 ? "DEBIT" : "CREDIT");
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

		List<BalanceOperation> bo = new ArrayList<BalanceOperation>();
		LocalDate tmp;
		Connection conn = null;
		conn = dbAccess();
		if (from != null && to != null && from.isAfter(to)) {
			tmp = to;
			to = from;
			from = tmp;
		}
		try {
			if (userLoggedIn == null)
				throw new UnauthorizedException("The user doesn't have the rights to perform this action");
			String sql = "SELECT * FROM balanceOperation WHERE date >= ? AND date <= ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, from == null ? "0001-01-01" : from.toString());
			statement.setString(2, to == null ? "9999-12-31" : to.toString());
			ResultSet rs = statement.executeQuery();
			while (rs.next())
				bo.add(new BalanceOperationImpl(rs.getInt("balanceId"), LocalDate.parse(rs.getString("date")),
						rs.getDouble("money"), rs.getString("type")));
			for (BalanceOperation b : bo)
				System.out.println(b);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		return bo;

	}

	@Override
	public double computeBalance() throws UnauthorizedException {

		Connection conn = null;
		conn = dbAccess();
		double balance = 0;
		String type = null;
		double money = 0;
		try {
			if (userLoggedIn == null)
				throw new UnauthorizedException("The user doesn't have the rights to perform this action");
			String sql = "SELECT money, type FROM balanceOperation";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				type = rs.getString("type");
				if (type.equals("DEBIT"))
					money = -rs.getDouble("money");
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
