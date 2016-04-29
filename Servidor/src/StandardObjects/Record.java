package StandardObjects;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.*;
import java.util.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import java.io.*;


public class Record implements Serializable{

	public String active_name;
	public List<CSVRecord> serie;
	
	public ScriptEngineManager factory = new ScriptEngineManager();
    public ScriptEngine engine = factory.getEngineByName("JavaScript");
	
	public Record(String record){
		try {
			CSVParser fileParser = CSVParser.parse(record,CSVFormat.EXCEL.withHeader());
			this.serie = fileParser.getRecords();
			fileParser.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error loading data");
		}
	}
}
