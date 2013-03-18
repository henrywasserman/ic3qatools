package com.xstreamcompare.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class OracleDbManager extends DatabaseManager {

	public OracleDbManager(Properties props) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		super(props);
		
	}
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String dbURL = properties.getProperty("URL.ORACLE");
			String user = properties.getProperty("UserID.ORACLE");
			String pass = properties.getProperty("Password.ORACLE");
			conn = DriverManager.getConnection(dbURL, user, pass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   return conn;	
	}
}
