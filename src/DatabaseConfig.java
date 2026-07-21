import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String JDBC_URL = "";
    private static final String DB_USER = "";
    private static final String DB_PASS = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver Not Found.", e);
        }
    }
}
