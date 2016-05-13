import java.io.FileReader;
import java.io.PrintStream;
import java.util.LinkedList;
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

import StandardObjects.Record;
import StandardObjects.*;

public class MathResponse{
	
	List<CSVRecord> records;
	private double saldoInicial = 8000.0;
	private ScriptEngineManager factory ;
    private ScriptEngine engine ;
    private double lucroLiquido = 0;
    
    public void setup(Record r){
    	try {
    		this.records = r.serie;
    		this.factory = r.factory;
    		this.engine = r.engine;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("erro carga dados");
		}	
    }
    
    ScriptContext newContext = new SimpleScriptContext();
    Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);
    
    private boolean isToBuy(List<CSVRecord> records2, int i, String formula) {
		int n = records2.size();
		if(i + 4 >= n  ){
			return false;
		} else {
			
						
			for(int j=1; j<=5;j++){
				int idx = i+j-1;
				String openStr = records.get(idx).get("Open");
				double open = Double.parseDouble(openStr);
				
				String highStr = records.get(idx).get("High");
				double max = Double.parseDouble(highStr);
				
				String lowStr = records.get(idx).get("Low");
				double min = Double.parseDouble(lowStr);
				
				String closeStr = records.get(idx).get("Close");
				double close = Double.parseDouble(closeStr);
	        	String cotacao = "P"+j;
	        	engineScope.put(cotacao+"_OPEN" ,open );
	        	engineScope.put(cotacao+"_CLOSE",close);
	        	engineScope.put(cotacao+"_MAX"  , max);
	        	engineScope.put(cotacao+"_MIN"  , min);
	        }

			String result="false";
			try {
				result = engine.eval(formula, newContext).toString();
				if(result.equals("1"))
					result = "true";
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//throw new RuntimeException("erro javascript ou formula");
			}
			
            return new Boolean(result);
		}
		
	}
    
    public double testFormula(String formula) throws Exception{
    	
    	//Quantidade possivel de vezes que pode ocorrer as regras: 
    	//(Tamanho da serie - 5) - Numero de dias sem efetuar compra
    	//Dias em que as acoes estao sendo vendidas
		
    	double saldoAtual = saldoInicial;
		double saldoComprado = 0;
		double saldoVendido = 0;
		double saldoAnterior = 0;
		int qtdAtual = 0;
		int qtdVender = 0;
		double sucessCount = 0.0;
		double unsucessCount = 0.0;
				
		int n = records.size()-1;
		int quantidadeMax = (records.size() - 5)/4;
				
		boolean isToBuy;
		double priceAtual;
		int modQtdVender = 0;
		
		for(int i=n-1;i>1;i--){
			priceAtual = Double.parseDouble(records.get(i).get("Close"));
			if( qtdAtual == 0 ){
				
				 isToBuy = isToBuy(records,i,formula);
				if ( isToBuy && saldoAtual > priceAtual ){
					saldoAnterior = saldoAtual;
					
					qtdAtual = (int)(saldoAtual/priceAtual);
					saldoComprado = (qtdAtual * priceAtual);
					saldoAtual = saldoAtual - saldoComprado;
					qtdVender = (int)(qtdAtual/3);
					modQtdVender = qtdAtual%3;
				}
				
				
			} 
			else if(qtdAtual > 0 && i > 3){
				for(int j=0;j < 3;j++){
					priceAtual = Double.parseDouble(records.get(i).get("Close"));
					saldoVendido+=priceAtual*qtdVender;					
					i--;
				}
				saldoVendido+=priceAtual*modQtdVender;
				saldoAtual = saldoAtual + saldoVendido;

				if((saldoAtual - saldoAnterior) < 0)
					unsucessCount++;
				else
					sucessCount++;
				/*
				if(this.lucroLiquido < (saldoAtual - saldoAnterior)){
					this.lucroLiquido = saldoAtual - saldoAnterior;
					file.println(this.lucroLiquido);
				}
				*/
				qtdAtual = 0;
				qtdVender = 0;
				modQtdVender = 0;
				saldoVendido = 0;
			}
		}
				
		double p =0;
		//System.out.println("Sucess: "+sucessCount);
		//System.out.println("Unsucess: "+unsucessCount);
		
		if(sucessCount+unsucessCount > 0){
			p = sucessCount/(unsucessCount+sucessCount);
			double vezesAconteceu = (unsucessCount+sucessCount)/quantidadeMax;
			p *= vezesAconteceu;
 		}
		
		//System.out.println("Lucro liquido: " + this.lucroLiquido);
		//System.out.println("Taxa de sucesso: " + p);
		return p;
	}

    
}
