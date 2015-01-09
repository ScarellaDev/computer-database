package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnector{

	private Connection connection;
	private Statement statement;
	private String driver;
	private String address;
	private String port;
	private String compte;
	private String userName;
	private String password;
	private String url;

	public MySQLConnector() {
		url =  "jdbc:mysql://localhost:3306/computer-database-db?zeroDateTimeBehavior=convertToNull";
		driver = "com.mysql.jdbc.Driver";
		userName = "admincdb";
		password = "qwerty1234";
	}
	
	public boolean connect(){
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, userName, password);
			statement = connection.createStatement();
			return true;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return false;
		} catch(ClassNotFoundException ex){
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public boolean close()
	{
		try	{
			this.connection.close();
			return true;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return false;
		} 
	}

	public ResultSet selectRequest(String req)
	{
		ResultSet rs=null;	
		try {
			rs=statement.executeQuery(req);
			return rs;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			rs=null;
			return rs;
		}
	} 
	
	public int updateRequest(String req)
	{
		int rs=0;	
		try {
			rs=statement.executeUpdate(req);
			return rs;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			rs=0;
			return rs;
		}
	
	} 
	
	public Connection getConnection() {
    return connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public Statement getStatement() {
    return statement;
  }

  public void setStatement(Statement statement) {
    this.statement = statement;
  }

  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getCompte() {
    return compte;
  }

  public void setCompte(String compte) {
    this.compte = compte;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  private boolean isClosed(){
		try {
			return connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}
}
