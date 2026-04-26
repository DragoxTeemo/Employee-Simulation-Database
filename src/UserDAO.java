import java.sql.*;

public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT userID, role, empID FROM users WHERE username = ? AND password_hash = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("userID"),
                        username,
                        rs.getString("role"),
                        (Integer) rs.getObject("empID")
                    );
                }
            }
        }
        return null;
    }
}
