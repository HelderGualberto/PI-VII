package StandardObjects;

import java.io.*;

public class Expression implements Serializable{
    
    public String expression;
    public String ID;
    
    public Expression(String expression,String ID) {
        this.expression = expression;
        this.ID = ID;
    }

}