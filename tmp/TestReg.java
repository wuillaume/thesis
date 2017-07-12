import java.util.*;
import java.io.*;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

class testReg{  
       
    // main program
	  public static void main(String[] args){		   
         
         double[] y = new double[4];
			   double[] x = new double[4];			  	   
			   			   
			   y[0]=1;
			   y[1]=2;
			   y[2]=3;
			   y[3]=4;
			   
			   x[0]=5;
			   x[1]=6;
			   x[2]=7;
			   x[3]=5;
			   
			   RegressionCalculator reg = new RegressionCalculator(x, y);
			   System.out.println(reg.getIntercept());
			   System.out.println(reg.getSlope());
			   System.out.println(reg.getSSR()/(reg.getSSR()+reg.getSSE()));
      } 
}	  