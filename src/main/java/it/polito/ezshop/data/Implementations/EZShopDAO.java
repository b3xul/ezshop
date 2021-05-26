package it.polito.ezshop.data.Implementations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidLocationException;

public class EZShopDAO {

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

	public void reset() {

		Connection conn = dbAccess();
		try {
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			dbClose(conn);
		}

	}

	public Integer createUser(String username, String password, String role) {

		Connection conn = dbAccess();
		try {
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

	}

	public boolean deleteUser(Integer id) {

		Connection conn = dbAccess();
		try {
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

	}

	public List<User> getAllUsers() {

		Connection conn = null;
		List<User> users = new ArrayList<User>();
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
		}

	}

	public User getUser(Integer id) {

		Connection conn = null;
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
		}

	}

	public boolean updateUserRights(Integer id, String role) {

		Connection conn = null;
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
		}

	}

	public User login(String username, String password) {

		Connection conn = null;
		User userLoggedIn = new UserImpl();
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

	}

	public Integer createProductType(String description, String productCode, double pricePerUnit, String note) {

		Connection conn = dbAccess();
		Integer id = -1;
		ProductTypeImpl newProduct = new ProductTypeImpl(note, description, productCode, pricePerUnit);
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
						+ description + "', '" + pricePerUnit + "', '" + productCode + "', '" + newProduct.getLocation()
						+ "', " + newProduct.getQuantity() + ", '" + note + "', " + newProduct.getDiscountRate() + ")";
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
		return id;

	}

	public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) {

		boolean success = false;
		Connection conn = dbAccess();
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
		return success;

	}

	public boolean deleteProductType(Integer id) {

		boolean success = false;
		Connection conn = null;
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
		return success;

	}

	public List<ProductType> getAllProductTypes() {

		Connection conn = null;
		List<ProductType> inventory = new ArrayList<ProductType>();
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
		return inventory;

	}

	public ProductType getProductTypeByBarCode(String barCode) {

		ProductType product = new ProductTypeImpl();
		Connection conn = null;
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
				// product.setDiscountRate(dr);
				System.out.println("product found");
				// product.print();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose(conn);
		}
		return product;

	}

	public List<ProductType> getProductTypesByDescription(String description) {

		List<ProductType> matchingProducts = new ArrayList<ProductType>();
		Connection conn = null;
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
		return matchingProducts;

	}

	public boolean updateQuantity(Integer productId, int toBeAdded) {

		boolean success = false;
		Connection conn = null;
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
					String sql2 = "UPDATE product SET quantity = " + newQuantity + " WHERE id = '" + productId + "'";
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
		return success;

	}

	public boolean updatePosition(Integer productId, String newPos) {

		boolean success = false;
		Connection conn = null;
		conn = dbAccess();
		if (newPos == null || newPos == "") {
			// Statement to update the location of a product given its ID to an empty string
			String sql2 = "UPDATE product SET location = '" + "" + "' WHERE id = '" + productId + "'";
			Statement statement2;
			try {
				statement2 = conn.createStatement();
				statement2.executeUpdate(sql2);
				return success = true;
			} catch (SQLException e) {
				e.printStackTrace();
				return success = false;
			}
		}
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
				String sql3 = "SELECT location FROM product WHERE location = '" + newPos + "' AND id != '" + productId
						+ "'";
				Statement statement3 = conn.createStatement();
				ResultSet result3 = statement3.executeQuery(sql3);
				if (result3.next()) {
					System.out.println("position already assigned");
					success = false;
				} else {
					// Statement to update the location of a product given its ID
					String sql4 = "UPDATE product SET location = '" + newPos + "' WHERE id = '" + productId + "'";
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
		return success;

	}

	public Integer issueOrder(String productCode, int quantity, double pricePerUnit) {

		Connection conn = null;
		int id = -1;
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
		return id;

	}

	public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) {

		Connection conn = null;
		int orderId = -1;
		int balanceId = 0;
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

	public boolean payOrder(Integer orderId) {

		boolean validOrderId = false;
		Connection conn = null;
		double money = 0;
		int balanceId = 0;
		try {
			conn = dbAccess();
			String sql = "SELECT orderId FROM order_ WHERE orderId = ? AND status = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, orderId);
			statement.setString(2, "ISSUED");
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
				return validOrderId = false;
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

	public boolean recordOrderArrival(Integer orderId) throws InvalidLocationException {

		boolean valid = false;
		Connection conn = null;
		String barcode = null;
		int qty = 0;
		String location = null;
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

	public List<Order> getAllOrders() {

		List<Order> orders = new ArrayList<Order>();
		Connection conn = null;
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

	public Integer defineCustomer(String customerName) {

		Connection conn = null;
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

	}

	public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) {

		Connection conn = null;
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

	}

	public boolean deleteCustomer(Integer id) {

		Connection conn = null;
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

	}

	public Customer getCustomer(Integer id) {

		Connection conn = null;
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

	}

	public List<Customer> getAllCustomers() {

		Connection conn = null;
		List<Customer> customers = new ArrayList<Customer>();
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

	}

	public String createCard() {

		Connection conn = null;
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

	}

	public boolean attachCardToCustomer(String customerCard, Integer customerId) {

		Connection conn = null;
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

	}

	public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) {

		Connection conn = null;
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

	}

	public Integer startSaleTransaction() {

		String getNextAutoincrement = "SELECT seq FROM sqlite_sequence WHERE name=\"saleTransaction\"";
		Integer id = 0; // 0=error
		Connection conn = this.dbAccess();
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(getNextAutoincrement)) {
			id = rs.getInt("seq");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose(conn);
		}
		return id;

	}

	public boolean endSaleTransaction(SaleTransactionImpl openSaleTransaction) {

		String getNextAutoincrement = "SELECT seq FROM sqlite_sequence WHERE name=\"saleTransaction\"";
		String insertSale = "INSERT INTO saleTransaction(price,discountRate,balanceId) VALUES(?,?,?)";
		String insertTicketEntry = "INSERT INTO ticketEntry(ticketNumber,barCode,productDescription,pricePerUnit,discountRate,amount) VALUES(?,?,?,?,?,?)";
		Connection conn = this.dbAccess();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getNextAutoincrement);
			BalanceOperationImpl balanceOperation = new BalanceOperationImpl(rs.getInt("seq") + 1, LocalDate.now(),
					openSaleTransaction.getPrice(), "CREDIT");
			openSaleTransaction.setBalanceOperation(balanceOperation);
			stmt.close();
			rs.close();
			// Insert SaleTransaction
			PreparedStatement pstmt = conn.prepareStatement(insertSale);
			pstmt.setDouble(1, openSaleTransaction.getPrice());
			pstmt.setDouble(2, openSaleTransaction.getDiscountRate());
			pstmt.executeUpdate();
			pstmt.close();
			for (TicketEntry entry : openSaleTransaction.getEntries()) {
				// InsertTicketEntry
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
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			this.dbClose(conn);
		}
		return true;

	}

	public boolean addProductsFromSaleTransaction(SaleTransactionImpl openSaleTransaction) {

		for (TicketEntry entry : openSaleTransaction.getEntries()) {
			// increaseProductQuantity
			String increaseProductQuantity = "UPDATE product SET quantity=quantity + ? WHERE barcode=?";
			try (Connection conn = DriverManager.getConnection("jdbc:sqlite:EZShopDB.sqlite");) {
				PreparedStatement pstmt = conn.prepareStatement(increaseProductQuantity);
				pstmt.setInt(1, entry.getAmount()); // amount to remove from sale, amount to add to store
				pstmt.setString(2, entry.getBarCode());
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;

	}

	public SaleTransactionImpl getSaleTransaction(Integer transactionId) {

		String getSale = "SELECT price,discountRate,creditCard,balanceId FROM saleTransaction WHERE ticketNumber=?";
		String getBalance = "SELECT balanceId,date,money,type FROM balanceOperation WHERE balanceId=?";
		String getTicketEntries = "SELECT barCode,productDescription,pricePerUnit,discountRate,amount FROM ticketEntry WHERE ticketNumber=?";
		SaleTransactionImpl result = null;
		Connection conn = this.dbAccess();
		try {
			// getSale
			PreparedStatement pstmt = conn.prepareStatement(getSale);
			pstmt.setInt(1, transactionId);
			ResultSet rs = pstmt.executeQuery();
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
		} finally {
			this.dbClose(conn);
		}
		return result;

	}

	public Integer startReturnTransaction() {

		Integer returnId = -1;
		String getNextAutoincrement = "SELECT seq FROM sqlite_sequence WHERE name=\"returnTransaction\"";
		Connection conn = this.dbAccess();
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(getNextAutoincrement)) {
			returnId = rs.getInt("seq");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.dbClose(conn);
		}
		return returnId;

	}

	public boolean endReturnTransaction(ReturnTransactionImpl openReturnTransaction) {

		Connection conn = dbAccess();

		try {
			conn.setAutoCommit(false); // single transaction on the database
			// 1. close the return transaction
			String insertReturn = "INSERT INTO returnTransaction(productId,productCode,pricePerUnit,discountRate,amount,price,ticketNumber) VALUES(?,?,?,?,?,?,?)";
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

			// 2. updates the transaction status (decreasing the number of units sold by the number of returned one)
			String updateTicket = "UPDATE ticketEntry SET amount = ? WHERE ticketNumber = ? AND barCode = ? ";
			String removeTicket = "DELETE FROM ticketEntry WHERE ticketNumber=? AND barCode = ?";
			Iterator<TicketEntry> iter = openReturnTransaction.getSaleTransaction().getEntries().iterator();
			while (iter.hasNext()) {
				TicketEntry entry = iter.next();
				if (entry.getBarCode().equals(openReturnTransaction.getProductCode())) { // product present in the
					// saleTransaction
					int previousAmount = entry.getAmount();
					int amountToRemove = openReturnTransaction.getAmount();
					if (amountToRemove < previousAmount) {

						pstmt = conn.prepareStatement(updateTicket);
						pstmt.setDouble(1, previousAmount - amountToRemove);
						pstmt.setInt(2, openReturnTransaction.getSaleTransaction().getTicketNumber());
						pstmt.setString(3, openReturnTransaction.getProductCode());
						pstmt.executeUpdate();
						pstmt.close();

					} else if (amountToRemove == previousAmount) {

						pstmt = conn.prepareStatement(removeTicket);
						pstmt.setInt(1, openReturnTransaction.getSaleTransaction().getTicketNumber());
						pstmt.setString(2, openReturnTransaction.getProductCode());
						pstmt.executeUpdate();
						pstmt.close();

					}
					// else if (amountToRemove > previousAmount) updated=false; (can't happen by construction)
					// System.out.println("Found item to remove" + entry);
					break;
				}
			}

			// 3. updates the transaction status (decreasing the final price)
			String updateSale = "UPDATE saleTransaction SET price = ? WHERE ticketNumber = ?";
			// updateSale
			pstmt = conn.prepareStatement(updateSale);
			pstmt.setDouble(1,
					openReturnTransaction.getSaleTransaction().getPrice() - openReturnTransaction.getPrice());
			pstmt.setInt(2, openReturnTransaction.getSaleTransaction().getTicketNumber());
			pstmt.executeUpdate();
			pstmt.close();

			// 4. increases the product quantity available on the shelves
			updateQuantity(openReturnTransaction.getProductId(), openReturnTransaction.getAmount());

			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			dbClose(conn);
		}

		return true;

	}

	public double getCreditCardCredit(String creditCard) {

		Connection conn = null;
		try {
			String url = "jdbc:sqlite:creditCards.sqlite";
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
		double credit = -1;
		try {
			String getCard = "SELECT balance FROM creditCards WHERE creditCardNumber = ?";
			PreparedStatement pstmt = conn.prepareStatement(getCard);
			pstmt.setString(1, creditCard);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.isBeforeFirst()) // card not registered
				return -1;
			// only 1 result because creditCard is primary key
			credit = rs.getDouble("balance");
			pstmt.close();
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			dbClose(conn);
		}

		return credit;

	}

	public boolean setCreditCardCredit(double newCredit, String creditCard) {

		Connection conn = null;
		try {
			String url = "jdbc:sqlite:creditCards.sqlite";
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		try {
			String updateCard = "UPDATE creditCards SET balance = ? WHERE creditCardNumber = ?";
			PreparedStatement pstmt = conn.prepareStatement(updateCard);
			pstmt.setDouble(1, newCredit);
			pstmt.setString(2, creditCard);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			dbClose(conn);
		}
		return true;

	}

	public boolean recordBalanceUpdate(double toBeAdded) {

		Connection conn = null;
		boolean positiveBalance = false;
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

	public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) {

		List<BalanceOperation> bo = new ArrayList<BalanceOperation>();
		LocalDate tmp;
		if (from != null && to != null && from.isAfter(to)) {
			tmp = to;
			to = from;
			from = tmp;
		}
		Connection conn = null;
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

	public double computeBalance() {

		Connection conn = null;
		double balance = 0;
		String type = null;
		double money = 0;
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
