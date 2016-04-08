import java.io.Serializable;

public class ExpressionResult implements Serializable{

	double result;
	int id;
	int id_serie;
	
	public ExpressionResult(double result,int id,int id_serie) {
		this.result = result;
		this.id = id;
		this.id_serie = id_serie;
		// TODO Auto-generated constructor stub
	}

}
