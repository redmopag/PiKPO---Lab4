import processor.DataProcessorService;
public class Main {
    public static void main(String[] args)
    {
        // Без указания полного пути, программа будет читать файл из своей корневой папки
        DataProcessorService service = new DataProcessorService("Прирост населения.csv");
        service.runService();
    }
}