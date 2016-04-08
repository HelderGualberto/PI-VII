import java.io.Serializable;

public class ExpressionResult implements Serializable{

	double result;
	int id_ativo;
	int id;
	
	public ExpressionResult(double result,int id_ativo,int id) {
		this.result = result;
		this.id_ativo = id_ativo;
		this.id = id;
		// TODO Auto-generated constructor stub
	}
}