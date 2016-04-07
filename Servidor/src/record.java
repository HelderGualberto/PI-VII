
import java.io.*;
import java.util.*;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.*;
import java.util.*;
import java.io.*;


public class Record implements Serializable{

	public int id;
	public List<CSVRecord> serie;
	
	public Record(int id, String record){
		this.id = id;
		try {
			CSVParser fileParser = CSVParser.parse(record,CSVFormat.DEFAULT);
			this.serie = fileParser.getRecords();
			fileParser.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error loading data");
		}
	}
}
