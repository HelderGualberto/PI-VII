import java.io.FileReader;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class MathResponse {
	
	List<CSVRecord> records;
	private double saldoInicial = 1000;
	private ScriptEngineManager factory = new ScriptEngineManager();
    private ScriptEngine engine = factory.getEngineByName("JavaScript");
    
    public void setup(Record r){
    	try {
    		this.records = r.serie;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("erro carga dados");
		}	
    }
    
    private boolean isToBuy(List<CSVRecord> records2, int i, String formula) {
		int n = records2.size();
		if(i + 4 >= n  ){
			return false;
		} else {
			
			ScriptContext newContext = new SimpleScriptContext();
	        Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);
	        			
			for(int j=1; j<=5;j++){
				int idx = i+j-1;
				String openStr = records.get(idx).get("Open");
				double open = Double.parseDouble(openStr);
				
				String highStr = records.get(idx).get("High");
				double max = Double.parseDouble(openStr);
				
				String lowStr = records.get(idx).get("Low");
				double min = Double.parseDouble(openStr);
				
				String closeStr = records.get(idx).get("Close");
				double close = Double.parseDouble(openStr);
	        	String cotacao = "P"+j;
	        	engineScope.put(cotacao+"_OPEN" ,open );
	        	engineScope.put(cotacao+"_CLOSE",close);
	        	engineScope.put(cotacao+"_MAX"  , max);
	        	engineScope.put(cotacao+"_MIN"  , min);
	        }

			String result="false";
			try {
				result = engine.eval(formula, newContext).toString();
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//throw new RuntimeException("erro javascript ou formula");
			}
            return Boolean.parseBoolean(result.toString());
		}
		
	}

    double testFormula(String formula) throws Exception{
		double saldo = saldoInicial;
		double saldoComprado = 0;
		double saldoVendido = 0;
		int qtdAtual = 0;
		int qtdVender = 0;
		int sucessCount = 0;
		int unsucessCount = 0;

		int n = records.size()-1;
		for(int i=n-1;i>1;i--){
			double priceAtual = Double.parseDouble(records.get(i).get("Close"));
			
			if( qtdAtual == 0 ){
				boolean isToBuy = isToBuy(records,i,formula);
				if ( isToBuy && saldo > priceAtual ){
					qtdAtual = (int) Math.floor(saldo/priceAtual);
					saldoComprado = (qtdAtual * priceAtual);
					saldo = saldo - (qtdAtual * priceAtual);
					qtdVender = (int)Math.ceil(((double)qtdAtual)/3d);
				}
			} else if(qtdAtual > 0){
				if(i > 0){ //não é ultimo dia da série
					if( qtdAtual - qtdVender >= 0 ){
						saldoVendido += priceAtual * qtdVender;
						saldo = saldo + priceAtual * qtdVender;
						qtdAtual = qtdAtual - qtdVender;
					} else {
						saldo = saldo + priceAtual * qtdAtual;
						saldoVendido += priceAtual * qtdAtual;
						qtdAtual = 0;
						
						double txLocal = saldoVendido/saldoComprado;
						if( txLocal > 1){
							sucessCount++;
						}else{
							unsucessCount++;
						}
						saldoVendido=0;
						saldoComprado=0;
					}
				}else{
					saldo = saldo + priceAtual * qtdAtual;
					saldoVendido += priceAtual * qtdAtual;
					qtdAtual = 0;
					
					double txLocal = saldoVendido/saldoComprado;
					if( txLocal > 1){
						sucessCount++;
					}else{
						unsucessCount++;
					}
					saldoVendido=0;
					saldoComprado=0;
				}
			} else{
				
				throw new RuntimeException("logica error");
			}
			
			
		}
		
		double p =0;
		if(sucessCount+unsucessCount>0){
			p = (double)sucessCount/(double)(sucessCount+unsucessCount);
			
		}
		
		
		return p;
	}

}
