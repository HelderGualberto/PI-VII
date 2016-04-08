import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.*;
import java.util.*;
import java.io.*;


public class Record implements Serializable{

	public String active_name;
	public List<CSVRecord> serie;
	
	public Record(String record){
		try {
			CSVParser fileParser = CSVParser.parse(record,CSVFormat.EXCEL.withHeader());
			this.serie = fileParser.getRecords();
			this.active_name = this.serie.get(this.serie.size()-2).get(0);
			fileParser.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error loading data");
		}
	}
}
