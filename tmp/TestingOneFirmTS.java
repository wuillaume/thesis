// class for manipulating stock data

import java.util.*;
import java.io.*;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

class TestingOneFirmTS{  
       
    // main program
	  public static void main(String[] args){
	  	
	  	 // initial parameters
		   String inputFile = "c:\\java\\TWStocks\\output\\StockList.txt";      
		   String tmpLine ="";
		   String inputData="";		   	   
		   String outputData=""; 
	   
		   try {		   	
	      	 BufferedReader input1 =  new BufferedReader(new FileReader(inputFile));	 
	      	 tmpLine = null;
	      	 
	         while (( tmpLine = input1.readLine()) != null){ //looping for company codes        	     
	         	     	         	     
	         	     try{
			         	     // read the input csv file
			         	     inputData="c:\\java\\TWStocks\\data\\"+tmpLine+"_R.csv";
			         	     outputData="c:\\java\\TWStocks\\results\\"+tmpLine+"_Results.csv";
			               tStrategy(inputData, outputData);            
			               
	              }catch(FileNotFoundException fnfe){fnfe.printStackTrace();}
	         }      	 
	         input1.close();
	    	}catch (Exception e){
					      		e.printStackTrace();					      		
	    	}
	  }
	  
	  
	  /********************************************************************
      Function: Trading Strategy
    ***********************************************************************/	  
	  public static void tStrategy(String inputData, String outputData) throws Exception {
	  	 
	  	 List<String[]> input = readCSV(inputData);
	  	 int arDays = 13; //column #13. the input AR as the main decision rule, e.g. 5 days AR >= 0.07
		   double upperBound = -0.15;
		   double lowerBound = -1;
		   double currentReturn = 0.0;
		   int outputCounter = 1;
		   int tmpCounter = input.size()-1; // searching from the oldest date in the data file			  	 
	  	 
	  	 // input parameters: setting the array of testing days
	  	 int [] testDays = new int[17];
		   testDays[0]=1;
		   testDays[1]=2;
		   testDays[2]=3;
		   testDays[3]=4;
		   testDays[4]=5;
		   testDays[5]=6;
		   testDays[6]=7;
		   testDays[7]=8;
		   testDays[8]=9;
		   testDays[9]=10;
		   testDays[10]=15;
		   testDays[11]=20;
		   testDays[12]=25;
		   testDays[13]=30;
		   testDays[14]=35;
		   testDays[15]=40;
		   testDays[16]=50;
		   
		   // input parameter: setting the array of tmp output
		   String [][] output = new String[2000][2+testDays.length]; // 2000 is a tmp size and will be adjusted in the end
		   output = setHeadings(output,testDays); // a function that sets the headings of the output csv file
		   
       while(tmpCounter>51){ // search until recent dates. e.g., 5 days AR, then stop at 6 days ago so we can verify the trading strategy
       	  try{
       	      currentReturn = Double.parseDouble(input.get(tmpCounter)[arDays-1]);
       	  
       	      if(currentReturn>=lowerBound && currentReturn<upperBound){
       	      	 //System.out.println(currentReturn);     	      	 
       	      	 
				   	  	 output[outputCounter][0] = Double.toString(currentReturn); // copy decision AR
				      	 output[outputCounter][1] = input.get(tmpCounter)[0]; // copy date of the AR
				      	 for(int k=2;k<=(testDays.length+1);k++){
				      	    output[outputCounter][k] =	Double.toString(setTSReturn(input,tmpCounter,k,testDays[k-2]));				      	    
				      	 }				      	 				      	 				      	               	  	
				   	  	 outputCounter++;
				   	     tmpCounter=tmpCounter-testDays[4]; // skip the trading days within the range of AR
				   	     
				   	  }else{
				   	     tmpCounter--;	               	  
				   	  }
       	  }catch(NumberFormatException nfe){tmpCounter--;}
       }// end of while				
       
       // this part is to adjust the length of the final output
       String [][] finalOutput = new String[outputCounter][2+testDays.length];	
       System.out.println(outputCounter+"===="+(2+testDays.length));
       
       for(int i=0;i<outputCounter;i++){
       	  for(int j=0; j<(2+testDays.length); j++){
       	     finalOutput[i][j]=output[i][j];
       	     //System.out.println(finalOutput[i][j]);
       	  }
       }
       					    				
			 writeCSV(outputData, finalOutput);  
			   
    }
	  
    
    /********************************************************************
      Function to get the historical realized return
    ***********************************************************************/
    public static double setTSReturn(List<String[]> input, int tmpCounter, int ARColumn, int targetARDays) throws Exception {    	
				double finalOutput = Double.parseDouble(input.get(tmpCounter-targetARDays)[ARColumn+6]); //"6" depends on the column number of AR in the file XXXX_R.csv					    				
				return finalOutput;
				
    }
    
    
    /********************************************************************
      Function to set headings
    ***********************************************************************/
    public static String[][] setHeadings(String[][] output, int [] testDays) throws Exception {
				
				int tmp=0;
				output[0][0] = "Decision_AR";
				output[0][1] = "Date";
				
				for(tmp=2;tmp<=(testDays.length+1);tmp++){
	         output[0][tmp] = "AR_"+testDays[tmp-2]; // assignement the # of days into the heading
				}
					    				
				return output;	      
    }
    
    
    /********************************************************************
      Functions to calculate statistics
    ***********************************************************************/
    /*
    public static double average(double [] input) throws Exception {
				double output=0.0;
				for(int i=0;i<input.length();i++){
					  output=output+input[i];
				}	
				output=output/input.length();		    				
				return output;	      
    }
    
    public static double variance(double [] input) throws Exception {
				double output=0.0;
				double mean=average(input);
				
				for(int i=0;i<input.length();i++){
					  output=output+(input[i]-mean)^2;
				}	
				output=output/input.length();		    				
				return output;
    }
    
    public static double stdev(double [] input) throws Exception {					    				
				return (variance(input))^(0.5);	      
    }
    */
    
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