
import java.io.*;

public class Expression implements Serializable{
	
	public String expression;
	public int ID;
	
	public Expression(String expression,int ID) {
		this.expression = expression;
		this.ID = ID;
	}

}
