package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private final String host;
    private final String port;
    private final String dbname;
    private final String login;
    private final String password;
    private int timeout = 15;

    /*
       В конструкторе разбираем строку подключения в формате:
       jdbc:mysql://usr:qwerty@192.168.56.104:3306/testdb
     */

    public MySQLConnection(String connectionStr) {
        String param = connectionStr.substring(13);
        String[] params = param.split(":");
        this.login = params[0];
        this.password = params[1].split("@")[0];
        this.host = params[1].split("@")[1];
        this.port = params[2].split("/")[0];
        this.dbname = params[2].split("/")[1];
    }

    public void setTimeout(int timeout){
        this.timeout = timeout;
    }

    public Connection getMySQLConnection() throws SQLException {
        String connectionURL = String.format("jdbc:mysql://%s:%s/%s", host, port, dbname);
        System.out.println("Connecting to " + connectionURL + " ...");
        DriverManager.setLoginTimeout(timeout);
        Connection connection = DriverManager.getConnection(connectionURL, login, password);
        return connection;
    }

}
