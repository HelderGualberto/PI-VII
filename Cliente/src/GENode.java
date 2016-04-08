import java.io.Serializable;

public class GENode implements Serializable{

	double result;
	
	String expression;
	
	public GENode(double result,String exp) {
		this.result = result;
		this.expression = exp;
		// TODO Auto-generated constructor stub
	}
}
