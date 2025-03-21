package CoffeeShop;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.*;

public class CoffeeShop {
    private static final String URL = "jdbc:postgresql://localhost:5432/pdea";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

   

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
    private static void addCoffeeItem(String name, double price ) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO CoffeShop (name, price) VALUES (?, ?)");) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();
            System.out.println("Item added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewMenu() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CoffeShop");) {
            System.out.println("\nCoffee Menu:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("name") + " - $" + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateItemPrice(String newName, double newPrice , int id) {
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE CoffeShop SET name = ?, price = ? WHERE id = ?");) {
        	pstmt.setString(1, newName);
            pstmt.setDouble(2, newPrice);
            pstmt.setInt(3, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) System.out.println("Price updated successfully!");
            else System.out.println("Item not found!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteItem(int id ) {
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM CoffeShop WHERE id = ?");) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) System.out.println("Item deleted successfully!");
            else System.out.println("Item not found!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        createTbl("pdea","CoffeShop");

//        String tableName;
        
        while(true) {
        	System.out.println("\nCoffee Shop Management System");
            System.out.println("1. Add Coffee Item");
            System.out.println("2. View Menu");
            System.out.println("3. Update Item Price");
            System.out.println("4. Delete Item");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
              	
            case 1:
//            	System.out.println("Enter table anme:");
//            	tableName = scanner.nextLine();
            	System.out.print("Enter coffee name: ");
                String name = scanner.nextLine();
                System.out.print("Enter price: ");
                double price = scanner.nextDouble();
                addCoffeeItem(name,price);
                break;
                
            case 2:
            	viewMenu();
            	break;
            	
            case 3:
            	System.out.print("Enter item ID to update: ");
            	int id = scanner.nextInt();
            	System.out.println("Enter new name: ");
            	String newName = scanner.nextLine();
            	scanner.nextLine();
            	System.out.print("Enter new price: ");
            	double newPrice = scanner.nextDouble();
            	updateItemPrice(newName,newPrice,id);
            	break;
           
            case 4:
            	System.out.print("Enter item ID to delete: ");
                int id1 = scanner.nextInt();
                deleteItem(id1);
                break;
                
              default:
            	  System.out.println("Invalid Choice");
            
        }
            
      }
           
           
    }
    
}




