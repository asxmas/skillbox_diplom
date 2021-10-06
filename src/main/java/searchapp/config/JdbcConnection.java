package searchapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcConnection {

    private static Optional connection = Optional.empty();

    @Value("${PSQL_USERNAME}")
    private static String us;

    @Value("${PSQL_PASSWORD}")
    private static String pass;

    public static Optional getConnection() {
        if (connection.isEmpty()) {
            String user = JdbcConnection.us;
            String password = JdbcConnection.pass;
            String url = "jdbc:postgresql://localhost:5432/search_engine";
            try {
                connection = Optional.ofNullable(
                        DriverManager.getConnection(url, user, password));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return connection;
    }
}
