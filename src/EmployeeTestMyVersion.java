import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeeTestMyVersion {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection(); 
             Scanner scanner = new Scanner(System.in)) {
            
            EmployeeDAO empDAO = new EmployeeDAO(conn);
            UserDAO userDAO = new UserDAO(conn);

            // --- STEP 1: LOGIN ---
            System.out.println("=== Company Z Portal ===");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            User currentUser = userDAO.authenticate(username, password);

            if (currentUser != null) {
                System.out.println("Login sucessful. Well, " + currentUser.getUsername());

                if ("HR_ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                    showHRMenu(empDAO, scanner);
                } else {
                    showEmployeeMenu(empDAO, scanner, currentUser.getEmpID());
                }
            } else {
                System.out.println("Invalid Login. Access Denied");
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
    }

    private static void showHRMenu(EmployeeDAO dao, Scanner sc) throws SQLException {
        while (true) { 
            System.out.println("\n--- HR Admin Dashboard ---");
            System.out.println("1. Search/View Employee");
            System.out.println("2. Update Salary (%)");
            System.out.println("3. report: Pay by Divison");
            System.err.println("0. Logout");
            System.out.println("Choice: ");

            String choice = sc.nextLine();
            if (choice.equals("0")) break;

            switch (choice) {
                
                case "1" -> {
                    System.out.println("Enter Employee ID: ");
                    int id = Integer.parseInt(sc.nextLine());
                    Employee e = dao.getEmployeeByID(id);
                    if (e != null) System.out.println("Found: " + e.getFirstName() + " " + e.getLastName());
                }

                case "2" -> {
                    System.out.print("Enter ID: ");
                    int id = Integer.parseInt(sc.nextLine());
                    Employee e = dao.getEmployeeByID(id);

                    if (e != null) {
                        double currentSalary = 60000; 
                        System.out.print("Enter % Increase: ");
                        double pct = Double.parseDouble(sc.nextLine());

                        if (currentSalary < 50000 && pct > 10) {
                            System.out.println("Error: Max increase for this range is 10%");
                        } else {
                            double newSalary = currentSalary + (currentSalary* (pct / 100));
                            dao.updateSalary(id, newSalary); 
                            System.out.println("Salary updated successfully.");
                        }
                    }
                }

                case "3" -> {
                    Map<String, Double> report = dao.getTotalPayByDivision(4, 2026);
                    report.forEach((name, total) -> System.out.println(name + ": $" + total));
                }
            }
        }
    }

    private static void showEmployeeMenu(EmployeeDAO dao, Scanner sc, int empID) throws SQLException {
        while (true) {
            System.out.println("\n--- EMPLOYEE SELF-SERVICE ---");
            System.out.println("1. View My Demographic Data");
            System.out.println("2. View My Pay History");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            String choice = sc.nextLine();
            if (choice.equals("0")) break;

            if (choice.equals("1")) {
                Employee e = dao.getEmployeeByID(empID);
                System.out.println("Your Data: " + e.getFirstName() + " living at " + e.getAddress().getStreet());
            } else if (choice.equals("2")) {
                List<String> history = dao.getPayHistory(empID);
                history.forEach(System.out::println);
            }
        }
    }
}