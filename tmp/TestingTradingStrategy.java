// class for manipulating stock data

import java.util.*;
import java.io.*;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

class TestingTradingStrategy{  
       
    // main program
	  public static void main(String[] args){
		   String inputFile = "c:\\java\\TWStocks\\StockList.txt";		      
		   String tmpLine ="";
		   String inputData="";
		   int arDays = 13; //column #13. the input AR as the main decision rule, e.g. 5 days AR >= 0.07
		   double criticalReturn = 0.07;
		   int [] testDays = new int[11];
		   String outputData="c:\\java\\TWStocks\\output\\"+arDays+".csv";
		   List<String[]> input = null;
		   int tmpCounter = 0;
		   
		   // setting the array of testing days
		   testDays[0]=1;
		   testDays[1]=2;
		   testDays[2]=3;
		   testDays[3]=4;
		   testDays[4]=5;
		   testDays[5]=6;
		   testDays[6]=7;
		   testDays[7]=10;
		   testDays[8]=15;
		   testDays[9]=20;
		   testDays[10]=30;		   
	   
		   try {
		   	
	      	 BufferedReader input1 =  new BufferedReader(new FileReader(inputFile));     	 
	      	 tmpLine = null;
	      	 String[][] output = new String[50000][4];
	      	 
	      	 output[0][0] = "code";
	      	 output[0][1] = "date";
	      	 output[0][2] = "inputCAR_5";
	      	 output[0][3] = "outputCAR_5";
	      	 int outputCounter = 1;	      	 
	      	 
	         while (( tmpLine = input1.readLine()) != null){ //looping for company codes
	         	     
	         	     // initialize parameters
	         	     //tmpCounter = 0;
	         	     
	         	     try{
			         	     // read the input csv file
			         	     inputData="c:\\java\\TWStocks\\data\\"+tmpLine+"_R.csv";	         	             
			               input = readCSV(inputData);			               
			               tmpCounter = input.size()-1;
			               
			               while(tmpCounter>testDays[4]+1){
			               	  try{
			               	  if(Double.parseDouble(input.get(tmpCounter)[arDays-1])>=criticalReturn){
			               	  	 output[outputCounter][0] = tmpLine;
									      	 output[outputCounter][1] = input.get(tmpCounter)[0];
									      	 output[outputCounter][2] = input.get(tmpCounter)[arDays-1];
									      	 output[outputCounter][3] = input.get(tmpCounter-testDays[4])[12];// it is important to note that 13 here is the CAR days for output, not input.	               	  	
			               	  	 outputCounter++;
			               	     tmpCounter=tmpCounter-testDays[4];
			               	  }else{
			               	     tmpCounter--;	               	  
			               	  }
			               	  }catch(NumberFormatException nfe){tmpCounter--;}
			               }// end of while
			               
			               writeCSV(outputData, output);
	              }catch(FileNotFoundException fnfe){fnfe.printStackTrace();}
	         }      	 
	         input1.close();
	    	}catch (Exception e){
					      		e.printStackTrace();					      		
	    	}   
	  }
  
    
    /********************************************************************
      Functions to read and write CSV files
    ***********************************************************************/
    public static List<String[]> readCSV(String csv_in) throws Exception {
				
						    CsvReader reader = new CsvReader(new FileReader(csv_in));
						    List entries = new ArrayList<String[]>();						    
						    while (reader.readRecord())
							  entries.add(reader.getValues());					
						    return entries;
	      
    }

    public static void writeCSV(String csv_out, String data[][]) {
				try{
				    CsvWriter writer = new CsvWriter(new FileWriter(csv_out), ',');
				    for (int i=0; i<data.length; i++)
					  writer.writeRecord(data[i]);
				    writer.close();
				}catch(IOException e) {
				}
    }   
}