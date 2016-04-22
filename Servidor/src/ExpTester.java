import java.awt.List;
import java.util.LinkedList;

import StandardObjects.*;

public class ExpTester extends Thread{
	private Record record;
	public LinkedList<ExpressionResult> results;
	public Expression exp_to_test;
	
	
	public ExpTester(Record r, LinkedList<ExpressionResult> results, Expression exp) {
		this.record = r;
		this.results = results;
		this.exp_to_test = exp;
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		MathResponse calculator = new MathResponse();
		calculator.setup(record);
		try {
			double result = calculator.testFormula(this.exp_to_test.expression);
			ExpressionResult rexp = new ExpressionResult(result,record.active_name,this.exp_to_test.ID);
			this.results.add(rexp);
			System.out.println("Resultado = "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
