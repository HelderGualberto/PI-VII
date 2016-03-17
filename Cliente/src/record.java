import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.*;
import java.util.*;



public class record {

	public int id;
	public List<CSVRecord> serie;
	
	public record(int id, String file_path){
		this.id = id;
		try {
    		FileReader reader = new FileReader(file_path);
			CSVParser fileParser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
			serie = fileParser.getRecords();
			fileParser.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("erro carga dados");
		}
	}
	
}
