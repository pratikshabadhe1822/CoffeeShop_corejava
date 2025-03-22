package CoffeeShop;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CoffeeShop {
    private static final String URL = "jdbc:postgresql://localhost:5432/pdea";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    static void createTables() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Create CoffeeShop table
            String coffeeTable = "CREATE TABLE IF NOT EXISTS CoffeeShop (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "price DECIMAL(5,2) NOT NULL)";
            stmt.executeUpdate(coffeeTable);
            
            // Create Order_table
            String orderTable = "CREATE TABLE IF NOT EXISTS Order_table (" +
                    "order_id SERIAL PRIMARY KEY, " +
                    "coffee_id INT REFERENCES CoffeeShop(id), " +
                    "quantity INT NOT NULL, " +
                    "total_price DECIMAL(7,2) NOT NULL)";
            stmt.executeUpdate(orderTable);
            
            System.out.println("Tables created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addCoffeeItem(String name, double price) {
        String query = "INSERT INTO CoffeeShop (name, price) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();
            System.out.println("Item added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewMenu() {
        String query = "SELECT * FROM CoffeeShop";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\nCoffee Menu:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("name") + " - $" + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateItemPrice(int id, String newName, double newPrice) {
        String query = "UPDATE CoffeeShop SET name = ?, price = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newName);
            pstmt.setDouble(2, newPrice);
            pstmt.setInt(3, id);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Price updated successfully!" : "Item not found!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteItem(int id) {
        String query = "DELETE FROM CoffeeShop WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted > 0 ? "Item deleted successfully!" : "Item not found!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void placeOrder(int coffeeId, int quantity) {
        String selectQuery = "SELECT price FROM CoffeeShop WHERE id = ?";
        String insertQuery = "INSERT INTO Order_table (coffee_id, quantity, total_price) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
            selectStmt.setInt(1, coffeeId);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                double price = rs.getDouble("price");
                double totalPrice = price * quantity;
                try (PreparedStatement orderStmt = conn.prepareStatement(insertQuery)) {
                    orderStmt.setInt(1, coffeeId);
                    orderStmt.setInt(2, quantity);
                    orderStmt.setDouble(3, totalPrice);
                    orderStmt.executeUpdate();
                    System.out.println("Order placed successfully! Total: $" + totalPrice);
                }
            } else {
                System.out.println("Item not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        createTables();

        while (true) {
            System.out.println("\nCoffee Shop Management System");
            System.out.println("1. Add Coffee Item");
            System.out.println("2. View Menu");
            System.out.println("3. Update Item Price");
            System.out.println("4. Delete Item");
            System.out.println("5. Place Order");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter coffee name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    addCoffeeItem(name, price);
                    break;
                case 2:
                    viewMenu();
                    break;
                case 3:
                    System.out.print("Enter item ID to update: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    updateItemPrice(id, newName, newPrice);
                    break;
                case 4:
                    System.out.print("Enter item ID to delete: ");
                    int idToDelete = scanner.nextInt();
                    deleteItem(idToDelete);
                    break;
                case 5:
                    System.out.print("Enter coffee ID: ");
                    int coffeeId = scanner.nextInt();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    placeOrder(coffeeId, quantity);
                    break;
                case 6:
                    System.out.println("Exiting... Have a nice day!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
