public class User {
    private int userID;
    private String username;
    private String role; // HR_ADMIN or EMPLOYEE
    private Integer empID;

    public User(int userID, String username, String role, Integer empID) {
        this.userID = userID;
        this.username = username;
        this.role = role;
        this.empID = empID;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public Integer getEmpID() {
        return empID;
    }
}