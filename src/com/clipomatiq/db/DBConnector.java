package com.clipomatiq.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

	private static DBConnector instance;
	private Connection conn;
	
	private  DBConnector() {
		init();
	}

	public static DBConnector getInstance(){
		if(instance==null)
			instance= new DBConnector();
		return instance;
		
	}
	
	private void init(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			
			
		} catch (InstantiationException e) {
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Connection getConnection() {

		try { 
			if (conn == null|| conn.isClosed()) {
						
				conn = DriverManager
				.getConnection("jdbc:mysql://localhost/clipomatiqdb?user=root&password=speccy");
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		return conn;

	}
	
	public void closeConnection(){
		try{
			if(conn!=null)conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
