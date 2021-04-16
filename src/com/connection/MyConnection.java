package com.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
	
	public Connection getConnection() {
		Connection connection=null;
		
		//1.Load Driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//2.Obtain connection
			connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/inputFiles","root","mysql");
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
		
	}


}
