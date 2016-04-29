package StandardObjects;

import java.io.Serializable;

public class ExpressionResult implements Serializable{

	public double result;
	public String active;
	public String id;
	
	public ExpressionResult(double result,String active,String id) {
		this.result = result;
		this.active = active;
		this.id = id;
		// TODO Auto-generated constructor stub
	}
}