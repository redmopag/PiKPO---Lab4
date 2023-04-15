package processor;

public class DataProcessorService {

    private final DataProcessorFactory procFactory;

    private final String datasource;

    public DataProcessorService(String datasource) {
        this.datasource = datasource;
        procFactory = new DataProcessorFactory();
    }
    public void runService() {
        DataProcessor processor = procFactory.getProcessor(datasource);
        processor.run();
        processor.printResult();
    }

    public String getDatasource() {
        return datasource;
    }
}
