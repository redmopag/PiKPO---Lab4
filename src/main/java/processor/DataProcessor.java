package processor;
import joinery.DataFrame;
import joinery.impl.Index;

public abstract class DataProcessor {
    protected String datasource;  // путь для подключения к источнику данных
    protected DataFrame dataset = null;  // входной датасет, который инициализируется методом read()
    protected DataFrame result = null;   // выходной датасет (результат обработки)

    // Setter для установки пути подключения к источнику данных
    public void setDatasource(String source) {
        this.datasource = source;
    }
    // Getter для получения результата (выходного DataFrame)
    public DataFrame getGetResult() {
        return this.result;
    }

    // Чтение источника данных
    public abstract boolean read();
    // Запуск обработки данных
    public abstract void run();
    // Вывод результатов обработки данных
    public abstract void printResult();

    protected DataFrame sortByColName(DataFrame df, String colName, boolean asc) {
        // если asc == true сортируем по возрастанию (по умолчанию), иначе добавляем к имени колонки символ '-' (сортировка по убыванию)
        if (asc) return df.sortBy(colName);
        else return df.sortBy("-" + colName);
    }
    protected DataFrame cutYears(DataFrame df, int sizeOfDel) {
        Integer[] cols = new Integer[sizeOfDel];
        for(int i = 0; i < sizeOfDel; ++i)
            cols[i] = i + 1;
        return df.drop(cols);
    }
}

