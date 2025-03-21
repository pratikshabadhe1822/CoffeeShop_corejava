package coffeeshop;
import java.sql.*;
import java.util.Scanner;

public class CoffeeShop {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
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
                case 1 -> addCoffeeItem(scanner);
                case 2 -> viewMenu();
                case 3 -> updateItemPrice(scanner);
                case 4 -> deleteItem(scanner);
                case 5 -> System.out.println("Exiting... Goodbye!");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);
        scanner.close();
    }

    private static void addCoffeeItem(Scanner scanner) {
        System.out.print("Enter coffee name: ");
        String name = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO coffee_menu (name, price) VALUES (?, ?)");) {
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
             ResultSet rs = stmt.executeQuery("SELECT * FROM coffee_menu");) {
            System.out.println("\nCoffee Menu:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("name") + " - $" + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateItemPrice(Scanner scanner) {
        System.out.print("Enter item ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new price: ");
        double newPrice = scanner.nextDouble();
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE coffee_menu SET price = ? WHERE id = ?");) {
            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) System.out.println("Price updated successfully!");
            else System.out.println("Item not found!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteItem(Scanner scanner) {
        System.out.print("Enter item ID to delete: ");
        int id = scanner.nextInt();
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM coffee_menu WHERE id = ?");) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) System.out.println("Item deleted successfully!");
            else System.out.println("Item not found!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
