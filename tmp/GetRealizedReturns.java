// class for manipulating stock data

import java.util.*;
import java.io.*;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

class GetRealizedReturns{  
       
    // main program
	  public static void main(String[] args){
	  	
	  	 // initial parameters
		   String inputFile = "c:\\java\\TWStocks\\StockList.txt";      
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
			         	     outputData="c:\\java\\TWStocks\\returns\\"+tmpLine+"_RR.csv";
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
		   int tmpCounter = 0; // searching from the oldest date in the data file			  	 
	  	 
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
		   String [][] output = new String[input.size()][2+testDays.length]; // 2000 is a tmp size and will be adjusted in the end
		   output = setHeadings(output,testDays); // a function that sets the headings of the output csv file
		   double currentReturn=0.0;
		   
       for(tmpCounter=1;tmpCounter<input.size();tmpCounter++){ // search until recent dates. e.g., 5 days AR, then stop at 6 days ago so we can verify the trading strategy
       	  try{
       	         output[tmpCounter][0] = input.get(tmpCounter)[0]; // copy date of the AR
       	         currentReturn = Double.parseDouble(input.get(tmpCounter)[8]);
				   	  	 output[tmpCounter][1] = Double.toString(currentReturn); // copy decision AR
				      	 
				      	 for(int k=2;k<=(testDays.length+1);k++){
				      	 	  try{
				      	       output[tmpCounter][k] =	Double.toString(setTSReturn(input,tmpCounter,k,testDays[k-2]));
				      	    }catch(Exception ee){;}   
				      	 }           	  
				   	  }catch(NumberFormatException nfe){;}
       }// end of while				
       
       // this part is to adjust the length of the final output
       //String [][] finalOutput = new String[outputCounter][2+testDays.length];	
       //System.out.println(outputCounter+"===="+(2+testDays.length));
       
       //for(int i=0;i<outputCounter;i++){
       //	  for(int j=0; j<(2+testDays.length); j++){
      // 	     finalOutput[i][j]=output[i][j];
      // 	     //System.out.println(finalOutput[i][j]);
       //	  }
      // }
       					    				
			 writeCSV(outputData, output);
			   
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
				output[0][0] = "Date";
				output[0][1] = "CurrentAR";
				
				for(tmp=2;tmp<=(testDays.length+1);tmp++){
	         output[0][tmp] = "AR_"+testDays[tmp-2]; // assignement the # of days into the heading
				}
					    				
				return output;	      
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