import java.io.*;

public class Expression implements Serializable{
	
	String expression;
	int ID;
	
	public Expression(String expression,int ID) {
		this.expression = expression;
		this.ID = ID;
	}

}