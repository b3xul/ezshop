package it.polito.ezshop.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class BalanceOperationDAO {

	boolean insertBalanceOperation(double toBeAdded, Connection conn) {

		boolean result;
		try {
			String sql2 = "INSERT INTO balanceOperation (date, money, type) VALUES (?,?,?)";
			PreparedStatement statement2 = conn.prepareStatement(sql2);
			statement2.setString(1, LocalDate.now().toString());
			statement2.setDouble(2, toBeAdded < 0 ? -toBeAdded : toBeAdded);
			statement2.setString(3, toBeAdded < 0 ? "DEBIT" : "CREDIT");
			statement2.executeUpdate();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		}
		return result;

	}

}
