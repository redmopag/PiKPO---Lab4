package database;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection(String connectionURL) {
        Connection conn = null;
        try {
            if (connectionURL.startsWith("jdbc:sqlite:")) {
                SQLiteConnection sqliteConn = new SQLiteConnection(connectionURL);
                conn = sqliteConn.getSQLiteConnection();
                System.out.println("SQLite connection initialized");
            }
            else if (connectionURL.startsWith("jdbc:mysql:")) {
                MySQLConnection sqliteConn = new MySQLConnection(connectionURL);
                conn = sqliteConn.getMySQLConnection();
                System.out.println("MySQL connection initialized");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return conn;
    }
}
