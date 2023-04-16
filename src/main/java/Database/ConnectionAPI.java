package Database;

import joinery.DataFrame;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/*
    В данном классе реализуется API (Application Programming Interface)
    для взаимодействия с БД с помощью объектов-коннекторов.

    ВАЖНО! Методы должны быть названы таким образом, чтобы по названию
    можно было понять выполняемые действия.

    todo: Подробнее, ознакомиться с JDBC можно здесь: https://proselyte.net/tutorials/jdbc/introduction/
*/

public class ConnectionAPI {

    /**
     * Вывод списка обработанных файлов с сортировкой по дате в порядке убывания (DESCENDING)
     */
    public static ResultSet selectAllFromSourceFiles(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        String sql = "SELECT * FROM source_files ORDER BY processed DESC";
        ResultSet result = statement.executeQuery(sql);
        return result;
    }

    /**
     * Вставка в таблицу обработанных файлов
     */
    public static void insertIntoSourceFiles(Connection con, String fileName) throws SQLException {
        Statement statement = con.createStatement();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    // создаем шаблон для форматирования времени
        String dateTime = simpleDateFormat.format(new Date());      // создаем строку с timestamp
        String sql = String.format("INSERT INTO source_files (filename, processed) VALUES ('%s', '%s')", fileName, dateTime);
        System.out.println(sql);
        statement.execute(sql);
    }

    /**
     * Вставка строк из DataFrame в БД с привязкой данных к последнему обработанному файлу (по дате)
     */
    public static void insertRowsIntoProcessedData(Connection con, DataFrame<String> dataFrame) throws SQLException {
        ResultSet sourceFilesResultSet = ConnectionAPI.selectAllFromSourceFiles(con);       // получаем список обработанных файлов
        int lastFileId = 0;
        // Если список файлов не пуст, то получаем (из первой строки) получаем индекс последней записи из таблицы с файлами
        if (sourceFilesResultSet.next()) {
            lastFileId = sourceFilesResultSet.getInt("id");
        }
        Statement statement = con.createStatement();

        Set<Object> col = dataFrame.columns();

        for (List<String> row : dataFrame) {
            Iterator<String> it = row.iterator();
            // Настройка названия страны для ввода в sqlite
            String name = it.next();
            name = "'" + name.replace("'", " ") + "'";

            Iterator<Object> yeariter = col.iterator();
            yeariter.next();

            while(it.hasNext() && yeariter.hasNext()) {
                String cnt = "'" + it.next() + "'";
                cnt = (cnt == null) ? "'0.00'" : cnt;

                String year = (String) yeariter.next();

                statement.execute(String.format("INSERT INTO processed_data (country, yr, cnt, source_file) VALUES ("
                        + name + "," + year + "," + cnt + "," + Integer.toString(lastFileId) + ")"));
            }
        }
    }

}
