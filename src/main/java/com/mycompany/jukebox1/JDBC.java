package com.mycompany.jukebox1;

import java.sql.DriverManager;
import java.sql.Connection;
public class JDBC 
{
		public static Connection connection;
		public static Connection getConnection() throws Exception
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection  =DriverManager.getConnection("jdbc:mysql://localhost:3306/Jukebox1","root","1321@asdfgf");
			return connection;
		}
}



