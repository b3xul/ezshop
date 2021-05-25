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

public class EZShop implements EZShopInterface {

	private User userLoggedIn = new UserImpl();
	private SaleTransactionImpl openSaleTransaction = new SaleTransactionImpl(-1);
	private ReturnTransactionImpl openReturnTransaction = new ReturnTransactionImpl(-1);

	private BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();

	// method to open the connection to database
	public static Connection dbAccess() {

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
		try {
			conn = dbAccess();
			String sql = "DELETE FROM product";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			sql = "DELETE FROM order_";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			sql = "DELETE FROM saleTransaction";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			sql = "DELETE FROM returnTransaction";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			sql = "DELETE FROM balanceOperation";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			sql = "DELETE FROM ticketEntry";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			sql = "UPDATE sqlite_sequence SET seq=0 WHERE name != 'Users'";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			openSaleTransaction = new SaleTransactionImpl(-1);
			openReturnTransaction = new ReturnTransactionImpl(-1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);

		}

	}

	@Override
	public Integer createUser(String username, String password, String role)
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

		Connection conn = null;
		if (username != null && username.isEmpty() == false) {
			if (password != null && password.isEmpty() == false) {
				if (role != null
						&& (role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManager"))) {
					try {
						conn = dbAccess();
						String sql = "SELECT seq FROM sqlite_sequence WHERE name = 'Users'";
						Statement statement = conn.createStatement();
						ResultSet rs = statement.executeQuery(sql);
						int id = rs.getInt("seq") + 1;
						sql = "INSERT INTO Users (id, username, password, role) VALUES(?,?,?,?)";
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

					} catch (Exception e) {
						System.out.println(e.getMessage());
						return -1;

					} finally {

						dbClose(conn);
					}
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
				try {
					conn = dbAccess();
					String sql = "DELETE FROM Users WHERE id = ?";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, id);
					int rs = pstmt.executeUpdate();
					if (rs == 0) { // no modified row
						return false;
					} else {
						return true;
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					return false;

				} finally {
					dbClose(conn);

				}
			} else
				throw new InvalidUserIdException("Invalid User");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public List<User> getAllUsers() throws UnauthorizedException {

		Connection conn = null;
		List<User> users = new ArrayList<User>();
		if (userLoggedIn.getRole().equals("Administrator")) {
			try {
				conn = dbAccess();
				String sql = "SELECT id, username, password, role FROM Users";
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {
					users.add(new UserImpl(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
							rs.getString("role")));
				}
				return users;

			} catch (Exception e) {
				System.out.println(e.getMessage());
				return users;

			} finally {
				dbClose(conn);
				// System.out.println("connection closed");

			}
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		Connection conn = null;
		if (userLoggedIn.getRole().equals("Administrator")) {
			if (id != null && id > 0) {
				try {
					conn = dbAccess();
					String sql = "SELECT id, username, password, role FROM Users WHERE id = " + id;
					Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(sql);
					if (rs.next()) {
						return new UserImpl(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
								rs.getString("role"));
					} else {
						return null;
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					return null;

				} finally {
					dbClose(conn);
					// System.out.println("connection closed");
				}
			} else
				throw new InvalidUserIdException("Invalid user");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public boolean updateUserRights(Integer id, String role)
			throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {

		Connection conn = null;
		if (userLoggedIn.getRole().equals("Administrator")) {
			if (id != null && id > 0) {
				if (role != null
						&& (role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManager"))) {
					try {
						conn = dbAccess();
						String sql = "UPDATE Users SET role = ? WHERE id = ?";
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, role);
						pstmt.setInt(2, id);
						int rs = pstmt.executeUpdate();
						if (rs == 0) { // no modified row
							return false;
						} else {
							return true;
						}

					} catch (Exception e) {
						System.out.println(e.getMessage());
						return false;

					} finally {
						dbClose(conn);
						// System.out.println("connection closed");
					}
				} else
					throw new InvalidRoleException("Invalid role");
			} else
				throw new InvalidUserIdException("Invalid user");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {

		Connection conn = null;
		if (username != null && username.isEmpty() == false) {
			if (password != null && password.isEmpty() == false) {
				try {
					conn = dbAccess();
					String sql = "SELECT id, username, password, role FROM Users WHERE username = '" + username + "'";
					Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(sql);

					if (rs.next() == true && password.equals(rs.getString("password"))) {
						// System.out.println("Log in");
						userLoggedIn.setId(rs.getInt("id"));
						userLoggedIn.setUsername(rs.getString("username"));
						userLoggedIn.setPassword(rs.getString("password"));
						userLoggedIn.setRole(rs.getString("role"));
						return userLoggedIn;
					} else {
						return null;
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					return null;

				} finally {
					dbClose(conn);
					// System.out.println("connection closed");
				}
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
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
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
			} finally {
				dbClose(conn);
			}
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
			} finally {
				dbClose(conn);
			}
		}
		return success;

	};

	// method to delete a product
	@Override
	public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {

		Boolean success = false;
		Connection conn = null;
		if (id <= 0 || id == null)
			throw new InvalidProductIdException("invalid ID");
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
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
			} finally {
				dbClose(conn);
			}
		}
		return success;

	}

	// method to get the list of all products
	@Override
	public List<ProductType> getAllProductTypes() throws UnauthorizedException {

		List<ProductType> inventory = new ArrayList<ProductType>();
		Connection conn = null;
		if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")
				&& !userLoggedIn.getRole().equals("Cashier")))
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
			} finally {
				dbClose(conn);
			}
		}
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
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
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
			} finally {
				dbClose(conn);
			}
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
		if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
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
			} finally {
				dbClose(conn);
			}
		}
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
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
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
			} finally {
				dbClose(conn);
			}
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
		if (productId <= 0 || productId == null)
			throw new InvalidProductIdException("ID incorrect");
		else if ((!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager")))
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
				} finally {
					dbClose(conn);
				}
			}
		}
		return success;

	}

	@Override
	public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException,
			InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

		Connection conn = null;
		int id = -1;

		if (quantity <= 0)
			throw new InvalidQuantityException("The quantity you've inserted is not accepted");
		if (pricePerUnit <= 0)
			throw new InvalidPricePerUnitException("The price you've inserted is not accepted");
		if (!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException(
					"Either the user doesn't have the rights to perform this action or doesn't exist");
		if (productCode == null || productCode == "")
			throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
		if (productCode.length() < 12 || productCode.length() > 14)
			throw new InvalidProductCodeException("invalid barcode: wrong length");
		if (!isStringOnlyNumbers(productCode))
			throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
		if (!isBarcodeValid(productCode))
			throw new InvalidProductCodeException("invalid barcode: barcode does not respect GTIN specifications");
		try {
			conn = dbAccess();
			String sql = "SELECT id FROM product WHERE barcode = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, productCode);
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				System.out.println("There is no product with this barcode");
				return id;
			}
			String sql3 = "INSERT INTO order_ (balanceId, date, money, productCode, pricePerUnit, quantity, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement3 = conn.prepareStatement(sql3);
			statement3.setInt(1, 0);
			statement3.setString(2, LocalDate.now().toString());
			statement3.setDouble(3, pricePerUnit * quantity);
			statement3.setString(4, productCode);
			statement3.setDouble(5, pricePerUnit);
			statement3.setInt(6, quantity);
			statement3.setString(7, "ISSUED");
			statement3.executeUpdate();
			String sql4 = "SELECT MAX(orderId) AS MaxId FROM order_";
			Statement statement4 = conn.createStatement();
			ResultSet rs4 = statement4.executeQuery(sql4);
			id = rs4.getInt("MaxId");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		System.out.println(id);
		return id;

	}

	@Override
	public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
			throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException,
			UnauthorizedException {

		Connection conn = null;
		int orderId = -1;
		int balanceId = 0;

		if (quantity <= 0)
			throw new InvalidQuantityException("The quantity you've inserted is not accepted");
		if (pricePerUnit <= 0)
			throw new InvalidPricePerUnitException("The price you've inserted is not accepted");
		if (!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException(
					"Either the user doesn't have the rights to perform this action or doesn't exist");
		if (productCode == null || productCode == "")
			throw new InvalidProductCodeException("invalid barcode: barcode not inserted");
		if (productCode.length() < 12 || productCode.length() > 14)
			throw new InvalidProductCodeException("invalid barcode: wrong length");
		if (!isStringOnlyNumbers(productCode))
			throw new InvalidProductCodeException("invalid barcode: in barcode must not be letters");
		if (!isBarcodeValid(productCode))
			throw new InvalidProductCodeException("invalid barcode: barcode does not respect GTIN specifications");
		try {
			conn = dbAccess();
			String sql = "SELECT id FROM product WHERE barcode = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, productCode);
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				System.out.println("There is no product with this barcode");
				return orderId;
			}
			dbClose(conn);
			if (!recordBalanceUpdate(-pricePerUnit * quantity))
				return orderId;
			conn = dbAccess();
			String sql2 = "SELECT MAX(balanceId) AS MaxBId FROM balanceOperation";
			Statement statement2 = conn.createStatement();
			ResultSet rs2 = statement2.executeQuery(sql2);
			balanceId = rs2.getInt("MaxBId");
			String sql4 = "INSERT INTO order_ (balanceId, date, money, productCode, pricePerUnit, quantity, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement4 = conn.prepareStatement(sql4);
			statement4.setInt(1, balanceId);
			statement4.setString(2, LocalDate.now().toString());
			statement4.setDouble(3, pricePerUnit * quantity);
			statement4.setString(4, productCode);
			statement4.setDouble(5, pricePerUnit);
			statement4.setInt(6, quantity);
			statement4.setString(7, "PAYED");
			statement4.executeUpdate();
			String sql5 = "SELECT MAX(orderId) AS MaxId FROM order_";
			Statement statement5 = conn.createStatement();
			ResultSet rs5 = statement5.executeQuery(sql5);
			orderId = rs5.getInt("MaxId");
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
		double money = 0;
		int balanceId = 0;
		if (!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException(
					"Either the user doesn't have the rights to perform this action or doesn't exist");
		if (orderId == null || orderId <= 0)
			throw new InvalidOrderIdException("There is no order with this id");
		try {
			conn = dbAccess();
			String sql = "SELECT orderId FROM order_ WHERE orderId = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, orderId);
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				System.out.println("There is no order with this ID");
				return validOrderId;
			}
			String sql2 = "UPDATE order_ SET status = ? WHERE orderId = ?";
			PreparedStatement statement2 = conn.prepareStatement(sql2);
			statement2.setString(1, "PAYED");
			statement2.setInt(2, orderId);
			statement2.executeUpdate();
			String sql3 = "SELECT money FROM order_ WHERE orderId = ?";
			PreparedStatement statement3 = conn.prepareStatement(sql3);
			statement3.setInt(1, orderId);
			ResultSet rs3 = statement3.executeQuery();
			money = rs3.getDouble("money");
			dbClose(conn);
			if (!recordBalanceUpdate(-money))
				return validOrderId;
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
			validOrderId = true;
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
		String barcode = null;
		int qty = 0;
		String location = null;
		if (!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException(
					"Either the user doesn't have the rights to perform this action or doesn't exist");
		if (orderId <= 0 || orderId == null)
			throw new InvalidOrderIdException("The order id is not valid");
		try {
			conn = dbAccess();
			String sql = "SELECT productCode, quantity, status FROM order_ WHERE orderId = ? AND status = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, orderId);
			statement.setString(2, "PAYED");
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				barcode = rs.getString("productCode");
				qty = rs.getInt("quantity");
			} else {
				System.out.println("There is no PAYED order with this id");
				return valid;
			}
			String sql2 = "SELECT location FROM product WHERE barcode = ?";
			PreparedStatement statement2 = conn.prepareStatement(sql2);
			statement2.setString(1, barcode);
			ResultSet rs2 = statement2.executeQuery();
			location = rs2.getString("location");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}
		if (location.equals("") || location.equals(" "))
			throw new InvalidLocationException("The product type has no location assigned");
		try {
			conn = dbAccess();
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
		if (!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException(
					"Either the user doesn't have the rights to perform this action or doesn't exist");
		try {
			conn = dbAccess();
			String sql = "SELECT * FROM order_";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				orders.add(new OrderImpl(rs.getInt("orderId"), rs.getInt("balanceId"),
						LocalDate.parse(rs.getString("date")), rs.getDouble("money"), rs.getString("productCode"),
						rs.getDouble("pricePerUnit"), rs.getInt("quantity"), rs.getString("status")));
				// System.out.println(orders.get(orders.size()-1).toString());
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

		Connection conn = null;
		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			if (customerName != null && customerName.isEmpty() == false) {
				try {
					conn = dbAccess();
					String sql = "SELECT seq FROM sqlite_sequence WHERE name = 'Customers'";
					Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(sql);
					int id = rs.getInt("seq") + 1;
					sql = "INSERT INTO Customers (id, name, card) VALUES(?,?,NULL)";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, id);
					pstmt.setString(2, customerName);
					int res = pstmt.executeUpdate();
					if (res == 0) {
						return -1;
					} else {
						return id;
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					return -1;

				} finally {
					dbClose(conn);

				}
			} else
				throw new InvalidCustomerNameException("Invalid customer name");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
			throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException,
			UnauthorizedException {

		Connection conn = null;
		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			if (id != null && id > 0) {
				if (newCustomerName != null && newCustomerName.isEmpty() == false) {
					try {
						Pattern p = Pattern.compile("\\d+");
						conn = dbAccess();
						String cardQuery = "";
						if (newCustomerCard != null)
							cardQuery = ", card = ?";
						String sql = "UPDATE Customers SET name = ?" + cardQuery + "WHERE id = ?";
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, newCustomerName);
						if (newCustomerCard == null) // card value doesn't change
							pstmt.setInt(2, id);
						else if (newCustomerCard.isEmpty()) { // card value erased (no more card for this customer)
							pstmt.setString(2, null);
							pstmt.setInt(3, id);
						} else if (newCustomerCard.length() >= 10 && p.matcher(newCustomerCard).matches()) { // check
																												// card
																												// validity
							pstmt.setString(2, newCustomerCard); // set new card value
							pstmt.setInt(3, id);
						} else {
							throw new InvalidCustomerCardException("Invalid card");
						}
						int rs = pstmt.executeUpdate();
						if (rs == 0) {
							return false;
						} else {
							return true;
						}

					} catch (Exception e) {
						System.out.println(e.getMessage());
						return false;

					} finally {
						dbClose(conn);
					}
				} else
					throw new InvalidCustomerNameException("Invalid customer name");
			} else
				throw new InvalidCustomerIdException("Invalid customer id");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

		Connection conn = null;
		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			if (id != null && id > 0) {
				try {
					conn = dbAccess();
					String sql = "DELETE FROM Customers WHERE id = ?";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, id);
					int rs = pstmt.executeUpdate();
					if (rs == 0) {
						return false;
					} else {
						return true;
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					return false;

				} finally {
					dbClose(conn);
				}
			} else
				throw new InvalidCustomerIdException("Invalid customer id");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

		Connection conn = null;
		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			if (id != null && id > 0) {
				try {
					conn = dbAccess();
					String sql = "SELECT CU.id, name, card, points FROM Customers CU, Cards CA WHERE CU.card = CA.id AND CU.id = "
							+ id;
					Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(sql);
					if (rs.next()) {
						return new CustomerImpl(rs.getInt("id"), rs.getString("name"), rs.getString("card"),
								rs.getInt("points"));
					} else { // search for a customer without an associated card
						sql = "SELECT id, name FROM Customers WHERE id = " + id;
						statement = conn.createStatement();
						rs = statement.executeQuery(sql);
						if (rs.next()) {
							return new CustomerImpl(rs.getInt("id"), rs.getString("name"), null, null);
						} else
							return null;
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					return null;

				} finally {
					dbClose(conn);
				}
			} else
				throw new InvalidCustomerIdException("Invalid customer id");
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public List<Customer> getAllCustomers() throws UnauthorizedException {

		Connection conn = null;
		List<Customer> customers = new ArrayList<Customer>();
		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			try {
				conn = dbAccess();
				String sql = "SELECT CU.id, name, card, points FROM Customers CU, Cards CA WHERE CU.card = CA.id";
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {
					customers.add(new CustomerImpl(rs.getInt("id"), rs.getString("name"), rs.getString("card"),
							rs.getInt("points")));
				}
				// add all customers without an associated card
				sql = "SELECT id, name FROM Customers WHERE card IS NULL";
				statement = conn.createStatement();
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					customers.add(new CustomerImpl(rs.getInt("id"), rs.getString("name"), null, null));
				}
				return customers;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return customers;

			} finally {
				dbClose(conn);
			}
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public String createCard() throws UnauthorizedException {

		Connection conn = null;
		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			try {

				String cardId;
				conn = dbAccess();
				String sql = "SELECT COALESCE(MAX(id),'') AS lastId FROM Cards";
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				rs.next();
				if (rs.getString("lastId").isEmpty()) {
					// System.out.println("first element");
					cardId = "1000000000";
				} else {
					// System.out.println("next element");
					Integer id = Integer.parseInt(rs.getString("lastId")) + 1;
					cardId = Integer.toString(id);
				}

				sql = "INSERT INTO Cards (id, points) VALUES(?,0)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, cardId);
				int res = pstmt.executeUpdate();
				if (res == 0) {
					return "";
				} else {
					return cardId;
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
				return "";

			} finally {
				dbClose(conn);

			}
		} else
			throw new UnauthorizedException("Permission denied");

	}

	@Override
	public boolean attachCardToCustomer(String customerCard, Integer customerId)
			throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {

		Connection conn = null;
		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			if (customerId != null && customerId > 0) {
				Pattern p = Pattern.compile("\\d+");
				if (customerCard != null && customerCard.isEmpty() == false && customerCard.length() >= 10
						&& p.matcher(customerCard).matches()) {
					try {
						conn = dbAccess();
						String sql = "UPDATE Customers SET card = ? WHERE id = ?";
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, customerCard);
						pstmt.setInt(2, customerId);
						int rs = pstmt.executeUpdate();
						if (rs == 0) {
							return false;
						} else {
							return true;
						}

					} catch (Exception e) {
						System.out.println(e.getMessage());
						return false;

					} finally {
						dbClose(conn);
					}
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

		Connection conn = null;
		if (userLoggedIn.getRole().equals("Administrator") || userLoggedIn.getRole().equals("Cashier")
				|| userLoggedIn.getRole().equals("ShopManager")) {
			Pattern p = Pattern.compile("\\d+");
			if (customerCard != null && customerCard.isEmpty() == false && customerCard.length() >= 10
					&& p.matcher(customerCard).matches()) {
				try {
					conn = dbAccess();
					String sql = "SELECT points FROM Cards WHERE id = '" + customerCard + "'";
					Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(sql);
					if (rs.next() && rs.getInt("points") + pointsToBeAdded >= 0) {
						int points = rs.getInt("points") + pointsToBeAdded;
						sql = "UPDATE Cards SET points = ? WHERE id = ?";
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, points);
						pstmt.setString(2, customerCard);
						int res = pstmt.executeUpdate();
						if (res == 0) {
							return false;
						} else {
							return true;
						}
					} else {
						return false;
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					return false;

				} finally {
					dbClose(conn);
				}
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

		return openSaleTransaction.startSaleTransaction();

	}

	@Override
	public boolean addProductToSale(Integer transactionId, String productCode, int amount)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			UnauthorizedException {

		System.out.println("Executing addProductToSale...");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (amount <= 0)
			throw new InvalidQuantityException("Amount to add cannot be <=0");

		// TODO: add InvalidProductCodeException (also tests!)
		if (openSaleTransaction.getTicketNumber() == -1 || transactionId != openSaleTransaction.getTicketNumber())
			return false;

		// ProductType productType = getProductTypeByBarCode(productCode); // could throw InvalidProductCodeException
		boolean result = false;
		List<ProductType> productTypes = getAllProductTypes();
		for (ProductType productType : productTypes) {
			if (productType.getBarCode().equals(productCode)) {
				if (amount <= productType.getQuantity()) {
					result = true;
					openSaleTransaction.addProductToSale(productType, amount);
				}
			}
		}
		return result;

	}

	@Override
	public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			UnauthorizedException {

		System.out.println("Executing deleteProductFromSale...");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (amount <= 0)
			throw new InvalidQuantityException("Amount to remove cannot be <=0");
		// TODO: add InvalidProductCodeException (also tests!)

		if (openSaleTransaction.getTicketNumber() == -1 || transactionId != openSaleTransaction.getTicketNumber())
			return false;
		boolean result = openSaleTransaction.deleteProductFromSale(productCode, amount);
		return result;

	}

	@Override
	public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException,
			UnauthorizedException {

		System.out.println("Executing applyDiscountRateToProduct...");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (discountRate < 0 || discountRate >= 1)
			throw new InvalidDiscountRateException("Discount Rate must be >=0 and <1");
		// TODO: add InvalidProductCodeException (also tests!)

		if (openSaleTransaction.getTicketNumber() == -1 || transactionId != openSaleTransaction.getTicketNumber())
			return false;

		boolean result = openSaleTransaction.applyDiscountRateToProduct(productCode, discountRate);

		return result;

	}

	@Override
	public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
			throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

		System.out.println("Executing applyDiscountRateToSale...");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (discountRate < 0 || discountRate >= 1)
			throw new InvalidDiscountRateException("Discount Rate must be >=0 and <1");
		if (openSaleTransaction.getTicketNumber() == -1 || transactionId != openSaleTransaction.getTicketNumber())
			return false;
		openSaleTransaction.applyDiscountRateToSale(discountRate);
		return true;

	}

	@Override
	public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing computePointsForSale...");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		int points = -1;
		if (openSaleTransaction.getTicketNumber() != -1 && transactionId == openSaleTransaction.getTicketNumber()) {
			points = (int) (openSaleTransaction.getPrice() / 10);
		}
		// else points==-1
		return points;

	}

	@Override
	public boolean endSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing endSaleTransaction...");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (openSaleTransaction.getTicketNumber() == -1 || transactionId != openSaleTransaction.getTicketNumber())
			// || getSaleTransaction(transactionId) != null can't happen
			return false;

		boolean result = openSaleTransaction.endSaleTransaction();
		openSaleTransaction = new SaleTransactionImpl(-1);
		return result;

	}

	@Override
	public boolean deleteSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing deleteSaleTransaction...");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (openSaleTransaction.getTicketNumber() == -1 || transactionId != openSaleTransaction.getTicketNumber())
			// || getSaleTransaction(transactionId) != null can't happen
			return false;
		if (openSaleTransaction.deleteSaleTransaction()) {
			openSaleTransaction = new SaleTransactionImpl(-1);
			return true;
		}
		return false;

	}

	@Override
	public SaleTransaction getSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");

		System.out.println("Executing getSaleTransaction...");
		String getSale = "SELECT price,discountRate,creditCard,balanceId FROM saleTransaction WHERE ticketNumber=?";
		String getBalance = "SELECT balanceId,date,money,type FROM balanceOperation WHERE balanceId=?";
		String getTicketEntries = "SELECT barCode,productDescription,pricePerUnit,discountRate,amount FROM ticketEntry WHERE ticketNumber=?";

		SaleTransactionImpl result = null;
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:EZShopDB.sqlite");) {
			// getSale
			PreparedStatement pstmt = conn.prepareStatement(getSale);
			pstmt.setInt(1, transactionId);
			ResultSet rs = pstmt.executeQuery();
			// System.out.println(rs.toString());
			if (!rs.isBeforeFirst())
				return null;
			// only 1 result because ticketNumber is primary key
			result = new SaleTransactionImpl(transactionId);
			result.setPrice(rs.getDouble("price"));
			result.setDiscountRate(rs.getDouble("discountRate"));
			result.setCreditCard(rs.getString("creditCard"));
			Integer balanceId = rs.getInt("balanceId");
			// System.out.println(result.getTicketNumber() + " " + result.getDiscountRate());
			pstmt.close();
			rs.close();
			// getBalance
			pstmt = conn.prepareStatement(getBalance);
			pstmt.setInt(1, balanceId);
			rs = pstmt.executeQuery();
			// only 1 result because balanceId is primary key
			if (rs.isBeforeFirst()) { // false if there are no rows in the ResultSet
				BalanceOperationImpl balanceOperation = new BalanceOperationImpl(rs.getInt("balanceId"),
						LocalDate.parse(rs.getString("date")), rs.getDouble("money"), rs.getString("type"));
				result.setBalanceOperation(balanceOperation);
			}
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
			// System.out.println(result);
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;

	}

	@Override
	public Integer startReturnTransaction(Integer transactionId)
			throws /* InvalidTicketNumberException, */InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing startReturnTransaction...");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		SaleTransaction saleTransaction = getSaleTransaction(transactionId);
		if (saleTransaction == null)
			return -1;
		Integer returnId = 0; // 0=error
		String getNextAutoincrement = "SELECT seq FROM sqlite_sequence WHERE name=\"returnTransaction\"";
		try (Connection conn = dbAccess();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(getNextAutoincrement)) {
			returnId = rs.getInt("seq") + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		openReturnTransaction.setReturnId(returnId);
		openReturnTransaction.setSaleTransaction(saleTransaction); // transaction will be added to the db only when it
																	// ends
		return returnId;

	}

	@Override
	public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

		System.out.println("Executing returnProduct...");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException("Return id cannot be null or <=0");
		if (amount <= 0)
			throw new InvalidQuantityException("Amount to add cannot be <=0");
		if (openReturnTransaction.getReturnId() == -1 || returnId != openReturnTransaction.getReturnId()) // the
																											// transaction
																											// does
			// not exist
			return false;
		ProductType productType = getProductTypeByBarCode(productCode); // could throw InvalidProductCodeException
		System.out.println(productType);
		if (productType == null) // the product to be returned does not exists
			return false;
		List<TicketEntry> entries = openReturnTransaction.getSaleTransaction().getEntries();
		Boolean updated = false;
		for (TicketEntry entry : entries) {
			if (entry.getBarCode().equals(productCode)) {
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
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException("Return id cannot be null or <=0");
		if (openReturnTransaction.getReturnId() == -1 || returnId != openReturnTransaction.getReturnId())
			return false;
		if (commit == false) {
			openReturnTransaction = new ReturnTransactionImpl(-1);
			return true;
		}
		// commit==true
		try {
			updateQuantity(openReturnTransaction.getProductId(), openReturnTransaction.getAmount());
		} catch (Exception ex) { // InvalidProductIdException
			ex.printStackTrace();
		}
		String insertReturn = "INSERT INTO returnTransaction(productId,productCode,pricePerUnit,discountRate,amount,price,ticketNumber) VALUES(?,?,?,?,?,?,?)";
		// insertReturn
		try (Connection conn = dbAccess();) {
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
			ex.printStackTrace();
			return false;
		}
		String updateTicket = "UPDATE ticketEntry SET amount = ? WHERE ticketNumber = ? AND barCode = ? ";
		String removeTicket = "DELETE FROM ticketEntry WHERE ticketNumber=? AND barCode = ?";
		// update or remove ticket
		Boolean updated = false;
		Iterator<TicketEntry> iter = openReturnTransaction.getSaleTransaction().getEntries().iterator();
		while (iter.hasNext() && updated == false) {
			TicketEntry entry = iter.next();
			if (entry.getBarCode().equals(openReturnTransaction.getProductCode())) { // product present in the
				// saleTransaction
				int previousAmount = entry.getAmount();
				int amountToRemove = openReturnTransaction.getAmount();
				if (amountToRemove < previousAmount) {
					try (Connection conn = dbAccess();) {
						PreparedStatement pstmt = conn.prepareStatement(updateTicket);
						pstmt.setDouble(1, previousAmount - amountToRemove);
						pstmt.setInt(2, openReturnTransaction.getSaleTransaction().getTicketNumber());
						pstmt.setString(3, openReturnTransaction.getProductCode());
						pstmt.executeUpdate();
						pstmt.close();
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
						return false;
					}
					updated = true;
				} else if (amountToRemove == previousAmount) {
					try (Connection conn = dbAccess();) {
						PreparedStatement pstmt = conn.prepareStatement(removeTicket);
						pstmt.setInt(1, openReturnTransaction.getSaleTransaction().getTicketNumber());
						pstmt.setString(2, openReturnTransaction.getProductCode());
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
		try (Connection conn = dbAccess();) {
			PreparedStatement pstmt = conn.prepareStatement(increaseProductQuantity);
			pstmt.setInt(1, openReturnTransaction.getAmount());
			pstmt.setString(2, openReturnTransaction.getProductCode());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}

		String updateSale = "UPDATE saleTransaction SET price = ? WHERE ticketNumber = ?";
		// updateSale
		try (Connection conn = dbAccess();) {
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
		openReturnTransaction = new ReturnTransactionImpl(-1);
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
		if (openReturnTransaction.getReturnId() == -1 || returnId != openReturnTransaction.getReturnId())
			return false;
		openReturnTransaction = new ReturnTransactionImpl(-1);
		return true;

	}

	// TODO: NEXT 4 METHOD SHOULD UPDATE THE BALANCE OF THE SHOP!
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
		if (openSaleTransaction.getTicketNumber() == -1 || transactionId != openSaleTransaction.getTicketNumber())
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
		if (openSaleTransaction.getTicketNumber() == -1 || transactionId != openSaleTransaction.getTicketNumber())
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
			if (!rs.isBeforeFirst()) // card not registered
				return false;
			// only 1 result because creditCard is primary key
			double credit = rs.getDouble("balance");
			pstmt.close();
			rs.close();
			if (credit < price) // card has not enough money
				return false;
			double newCredit = credit - price;
			String updateCard = "UPDATE creditCards SET balance = ? WHERE creditCardNumber = ?";
			pstmt = conn.prepareStatement(updateCard);
			pstmt.setDouble(1, newCredit);
			pstmt.setString(2, creditCard);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		openSaleTransaction.setCreditCard(creditCard);
		return true;

	}

	@Override
	public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {

		System.out.println("Executing returnCashPayment...");
		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException("Transaction id cannot be null or <=0");
		if (userLoggedIn.getRole() == "")
			throw new UnauthorizedException("User not logged in");
		if (openReturnTransaction.getReturnId() == -1 || returnId != openReturnTransaction.getReturnId())
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
		if (openReturnTransaction.getReturnId() == -1 || returnId != openReturnTransaction.getReturnId())
			return -1;

		// if(openReturnTransaction.getSaleTransaction().ge)

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
			if (!rs.isBeforeFirst()) // card not registered
				return -1;
			// only 1 result because creditCard is primary key
			double credit = rs.getDouble("balance");
			pstmt.close();
			rs.close();
			double newCredit = credit + price;
			String updateCard = "UPDATE creditCards SET balance = ? WHERE creditCardNumber = ?";
			pstmt = conn.prepareStatement(updateCard);
			pstmt.setDouble(1, newCredit);
			pstmt.setString(2, creditCard);
			pstmt.executeUpdate();
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
		boolean positiveBalance = false;

		if (!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException(
					"Either the user doesn't have the rights to perform this action or doesn't exist");

		if (computeBalance() + toBeAdded < 0) {
			System.out.println("The operation can't be performed due to negative balance");
			return positiveBalance;
		}
		if (!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException(
					"Either the user doesn't have the rights to perform this action or doesn't exist");

		Connection conn = dbAccess();
		positiveBalance = balanceOperationDAO.insertBalanceOperation(toBeAdded, conn);
		dbClose(conn);

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
		if (!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException(
					"Either the user doesn't have the rights to perform this action or doesn't exist");
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
				// System.out.println(bo.get(bo.size()-1).toString());
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
		if (!userLoggedIn.getRole().equals("Administrator") && !userLoggedIn.getRole().equals("ShopManager"))
			throw new UnauthorizedException(
					"Either the user doesn't have the rights to perform this action or doesn't exist");
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
