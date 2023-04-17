package processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.ConnectionAPI;
import database.ConnectionFactory;

import joinery.DataFrame;

/**
 * В данном модуле реализуется класс с основной бизнес-логикой приложения.
 * Обычно такие модули / классы имеют в названии слово "Service".
 */

public class DataProcessorService {

    private final DataProcessorFactory procFactory;

    private final String datasource;

    private final String dbConnectionUrl;

    public DataProcessorService(String datasource, String dbConnectionUrl) {
        this.datasource = datasource;
        this.dbConnectionUrl = dbConnectionUrl;
        procFactory = new DataProcessorFactory();
    }

    /**
     * ВАЖНО! Обратите внимание, что метод runService использует только методы базового абстрактного класса DataProcessor
     *        и, таким образом, будет выполняться для любого типа обработчика данных (CSV или TXT), что позволяет в дальнейшем
     *        расширять приложение, просто добавляя другие классы обработчиков, которые, например, работают с базой данных или
     *        сетевым хранилищем файлов (например, FTP-сервером).
     */
    public void runService() {
        DataProcessor processor = procFactory.getProcessor(datasource);
        processor.run();    // обработка данных
        processor.printResult();    // вывод результата
        saveToDataBase(processor.getGetResult());   // сохранения результата обработки в БД
    }

    public void saveToDataBase(DataFrame result) {
        // Работа с БД
        if (result != null) {
            // Подключаемся к БД
            ConnectionFactory conFactory = new ConnectionFactory();
            Connection dbConnector = conFactory.getConnection(dbConnectionUrl);

            if (dbConnector != null) {
                try {
                    // Добавляем информацию о новом обработанном файле
                    ConnectionAPI.insertIntoSourceFiles(dbConnector, datasource);
                    // Выводим все обработанные файлы данных
                    ResultSet sourceFilesResultSet = ConnectionAPI.selectAllFromSourceFiles(dbConnector);
                    while (sourceFilesResultSet.next()) {
                        System.out.println("--------------------");
                        System.out.println(sourceFilesResultSet.getInt("id"));
                        System.out.println(sourceFilesResultSet.getString("filename"));
                        System.out.println(sourceFilesResultSet.getDate("processed"));
                    }
                    // Добавляем результат обработки в Базу Данных
                    ConnectionAPI.insertRowsIntoProcessedData(dbConnector, result);
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    // Закрываем соединение с БД
                    try {
                        dbConnector.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    public String getDatasource() {
        return datasource;
    }
}
