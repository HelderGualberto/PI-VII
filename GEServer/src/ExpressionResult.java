import java.io.Serializable;

public class ExpressionResult implements Serializable{

	double result;
	String active;
	int id;
	
	public ExpressionResult(double result,String active,int id) {
		this.result = result;
		this.active = active;
		this.id = id;
		// TODO Auto-generated constructor stub
	}
}