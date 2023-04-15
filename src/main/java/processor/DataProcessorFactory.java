package processor;

public class DataProcessorFactory {

    private DataProcessor processor = null;

    // Основной фабричный метод, возвращающий необходимый объект класса DataProcessor в зависимости от расширения файла
    public DataProcessor getProcessor(String datasource) {
        if (datasource.endsWith(".csv")) {
            createCsvProcessor(datasource);
        }
        else if (datasource.endsWith(".txt")) {
            createTxtProcessor(datasource);
        }
        return processor;
    }

    // Создаем TxtDataProcessor и пытаемся прочитать данные, если удачно, сохраняем объект в атрибуте класса
    public void createTxtProcessor(String source) {
        TxtDataProcessor txtProcessor = new TxtDataProcessor();
        txtProcessor.setDatasource(source);
        if (txtProcessor.read()) {
            processor = txtProcessor;
        }
    }

    // Создаем CsvDataProcessor и пытаемся прочитать данные, если удачно, сохраняем объект в атрибуте класса
    public void createCsvProcessor(String source) {
        CsvDataProcessor csvProcessor = new CsvDataProcessor();
        csvProcessor.setDatasource(source);
        if (csvProcessor.read()) {
            processor = csvProcessor;
        }
    }
}
