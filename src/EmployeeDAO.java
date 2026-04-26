
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmployeeDAO {
    private final Connection conn;

    public EmployeeDAO(Connection conn) {
        this.conn = conn;
    }
    // Finds a single employee and their joined address data
    public Employee getEmployeeByID(int id) throws SQLException {
        String sql = "SELECT e.*, a.street, a.zip, c.city_name, s.state_abbreviation " +
                     "FROM employees e " +
                     "LEFT JOIN addresses a ON e.addressID = a.addressID " +
                     "LEFT JOIN cities c ON a.cityID = c.cityID " + 
                     "LEFT JOIN states s ON a.stateID = s.stateID " + 
                     "WHERE e.empID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 1. Build Address Model
                    Address addr = new Address(
                        rs.getString("street"),
                        rs.getString("city_name"),
                        rs.getString("state_abbreviation"),
                        rs.getString("zip")
                    );
                    // 2. Build Employee Model
                    return new Employee(
                        rs.getInt("empID"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("ssn"),
                        rs.getDate("dob").toLocalDate(),
                        addr
                    );
                }
            }
        }
        return null;
    }

    // Update functionality
    public void updateEmployeeContact(int empID, String phone, String emergencyName, String emergencyPhone) throws SQLException {
        String sql = "UPDATE employees SET phone = ?, emergency_contact_name = ?, emergency_contact_phone = ?  WHERE empID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setString(2, emergencyName);
            ps.setString(3, emergencyPhone);
            ps.setInt(4, empID);
            ps.executeUpdate();
        }
    }

    public void updateSalary(int empID, double newSalary) throws SQLException {
        String sql = "UPDATE payroll SET salary = ? WHERE empID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newSalary);
            ps.setInt(2, empID);
            ps.executeUpdate();
        }
    }

    // Reporting Functionality
    public Map<String, Double> getTotalPayByJobTitle(int month, int year) throws SQLException {
    Map<String, Double> report = new HashMap<>();
    String sql = "SELECT jt.job_title_name, SUM(p.salary) as total " +
                 "FROM payroll p " +
                 "JOIN employee_job_titles ejt ON p.empID = ejt.empID " +
                 "JOIN job_titles jt ON ejt.job_titleID = jt.job_titleID " +
                 "WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ? " +
                 "GROUP BY jt.job_title_name";
    
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, month);
        ps.setInt(2, year);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                report.put(rs.getString("job_title_name"), rs.getDouble("total"));
            }
        }
    }
    return report;
}

    public Map<String, Double> getTotalPayByDivision(int month, int year) throws SQLException {
        Map<String, Double> report = new HashMap<>();
        String sql = "SELECT d.division_name, SUM(p.salary) as total " +
                     "FROM payroll p " +
                     "JOIN employee_division ed ON p.empID = ed.empID " +
                     "JOIN divisions d ON ed.divID = d.divID " +
                     "WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ? " +
                     "GROUP BY d.division_name";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    report.put(rs.getString("division_name"), rs.getDouble("total"));
                }
            }
        }

        return report;
    }
    
    public List<String> getPayHistory(int empID) throws SQLException {
        List<String> history = new ArrayList<>();
        String sql = "SELECT pay_date, salary FROM payroll WHERE empID = ? ORDER BY pay_date DESC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    history.add("Date: " + rs.getDate("pay_date") + " | Amount: $" + rs.getDouble("salary"));
                }
            }
        }
        return history;
    }
    
    public List<Employee> getNewHires(LocalDate start, LocalDate end) throws SQLException {
        List<Employee> hires = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE hire_date BETWEEN ? AND ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));
            try (ResultSet rs = ps. executeQuery()) {
                while (rs.next()) {
                    hires.add(new Employee(
                        rs.getInt("empId"), 
                        rs.getString("first_name"), 
                        rs.getString("last_name"), 
                        rs.getString("ssn"), 
                        rs.getDate("dob").toLocalDate(), 
                        null
                    ));
                }
            }
        }
        return hires;
    }

    public void deleteEmployee(int id) throws SQLException {
        String sql1 = "DELETE FROM payroll WHERE empID = ?";
        String sql2 = "DELETE FROM employee_division WHERE empID = ?";
        String sql3 = "DELETE FROM employee_job_titles WHERE empID = ?";
        String sqlMain = "DELETE FROM employees WHERE empID = ?";

        try (PreparedStatement ps1 = conn.prepareStatement(sql1);
             PreparedStatement ps2 = conn.prepareStatement(sql2);
             PreparedStatement ps3 = conn.prepareStatement(sql3);
             PreparedStatement psMain = conn.prepareStatement(sqlMain)) {
            
            ps1.setInt(1, id); ps1.executeUpdate();
            ps2.setInt(1, id); ps2.executeUpdate();
            ps3.setInt(1, id); ps3.executeUpdate();
            psMain.setInt(1, id); psMain.executeUpdate();
        }
    }
}
