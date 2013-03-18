package com.xstreamcompare.request.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.xstreamcompare.utils.SqlServerDbManager;
import com.xstreamcompare.utils.Utilities;

public class DatabaseTest {
	Connection conn = null;
	Utilities utils = new Utilities();
	private Properties properties = null;
	 SqlServerDbManager   dbManager = null;

	public DatabaseTest() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		 properties =  utils.getTestProperties();
		 dbManager = new SqlServerDbManager(properties);
		 try {
			conn = dbManager.getConnection();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		DatabaseTest test = new DatabaseTest();
		test.getCehckinTraceResults(test.conn);
      	}

	public  void getCehckinTraceResults(Connection conn) throws SQLException {
		String creativeIds = "233,2807,244,3098,239,2825,";
    	
		java.sql.Timestamp currTime = dbManager.getDatabaseTimeStamp();
				
		
		String sql = "SELECT COUNT(*) FROM CheckinTrace WHERE Creatives ='"
				+ creativeIds + "'";
		String sql2 = " AND (CreateDate < '" + currTime + "') ";
		System.out.println(sql + sql2);

		ResultSet resultSet = dbManager.executeQuery( sql + sql2);

		while (resultSet.next()) {
			System.out.println("count: " + resultSet.getInt(1));
		}
	}
}
