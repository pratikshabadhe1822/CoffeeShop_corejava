package coffeeshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTbl {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		createTbl("postgres","CoffeShop");

	}



static void createTbl(String dbName, String tblName ) {
	
	try
	{
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbName,"postgres","123");
		String query =  "CREATE TABLE IF NOT EXISTS " + tblName + " (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL," +
                "price DECIMAL(5,2) NOT NULL)";
		
		System.out.println("TABLE CREATED "+ tblName +" SUCCESSFULLY");
		
		Statement stmt = conn.createStatement();
		stmt.executeLargeUpdate(query);
		
	
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}
