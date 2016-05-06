import java.util.List;
import java.util.LinkedList;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import StandardObjects.ExpressionResult;

import StandardObjects.*;

public class ExpTester extends Thread{
	private Record record;
	List<ExpressionResult> results;
	public Expression exp_to_test;
	
	
	public ExpTester(Record r, List<ExpressionResult> results, Expression exp) {
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
		//	System.out.println("Id: " + exp_to_test.ID + " --> " + "Resultado = " + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
