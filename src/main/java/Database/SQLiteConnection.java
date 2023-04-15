package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private final String connectionURL;

    public SQLiteConnection(String connectionStr) {
        this.connectionURL = connectionStr;
    }

    public Connection getSQLiteConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(connectionURL);
        return connection;
    }

}
