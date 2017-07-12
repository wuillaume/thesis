// class for manipulating stock data

import java.util.*;
import java.io.*;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

class DataPreprocessing{  
       
    // main program
	  public static void main(String[] args){
		   String inputFile = "c:\\java\\TWStocks\\StockList.txt";
		   String inputDataFile = "";
		   String outputDataFile = "";
		   String outputCodeFile = "c:\\java\\TWStocks\\output\\StockList.txt";   
		   String [][] outputCodes = new String [800][1];
		   int outputCodeCounter=0;
		   String tmpLine ="";
		   int flag = 0; //a flag to represent whether a company code is included or not
	   
		   try {
		   	
	      	 BufferedReader input =  new BufferedReader(new FileReader(inputFile));     	 
	      	 tmpLine = null;	      	 
	      	 
	         while (( tmpLine = input.readLine()) != null){
	         	     
	         	     inputDataFile="c:\\java\\TWStocks\\data\\"+tmpLine+".csv";
	         	     outputDataFile="c:\\java\\TWStocks\\output\\"+tmpLine+".csv";  
	         	     
	         	     try{      
	                   flag = preProcess1(inputDataFile, outputDataFile);	                    
	                   if(flag==1){
	                      	outputCodes[outputCodeCounter][0]=tmpLine;
	                      	outputCodeCounter++;
	                   }	
	               }catch(Exception ee){
	               	   ee.printStackTrace();	
	                   System.out.println("company with this error is===>"+tmpLine);
	               }	                   
	         }
	         
	         input.close();
	         writeCSV(outputCodeFile, outputCodes);
	         	         
	    	}catch (Exception e){
					      		e.printStackTrace();	      		
	    	}
	  }
    

    // Removing Data Records with zero volume: most of times, it is a erros in Yahoo! (not a trading day) finance rather than having zero volume.    
    public static int preProcess1(String csv_in, String csv_out) throws Exception{		
				
				int stoppingRecordNumber = 1547; // drop records when it is longer than 2003/1/1 NOT a good way to write program....modify later
				List<String[]> input = readCSV(csv_in);				
				String[][] output = new String[stoppingRecordNumber+1][7];
				int finalFlag=0;   
				
	      
				int j = 1;
				output[0][0] = input.get(0)[0];
		   	output[0][1] = input.get(0)[1];
		   	output[0][2] = input.get(0)[2];
		  	output[0][3] = input.get(0)[3];
		    output[0][4] = input.get(0)[4];
		    output[0][5] = input.get(0)[5];
		    output[0][6] = input.get(0)[6];				
				
        try{
						// fill up rows with data copied from input and calculating return
						// drop the data if its vol=0
						for (int i=1; i<input.size()-1; i++) {
												 
							 if(Double.parseDouble(input.get(i)[5])>0){
							 	   if(input.get(i)[0].equals("2008-06-09")){
							 	      ; // a tedious step to remove record on 2007/6/9 although that is a trading day, it is not captured in Yahoo! Finance.
							 	   }else{
							 	      if(j<=stoppingRecordNumber){ // drop records when it is longer than 2003/1/1 NOT a good way to write program....modify later
												 output[j][0] = input.get(i)[0];
											   output[j][1] = input.get(i)[1];
											   output[j][2] = input.get(i)[2];
											   output[j][3] = input.get(i)[3];
											   output[j][4] = input.get(i)[4];
											   output[j][5] = input.get(i)[5];
											   output[j][6] = input.get(i)[6];
											   j++;
								      }
								   }   
							 }else{ ;
							 }
								   		    
						}//end of for loop
						
						if (j<1537){
							// outputPoorCodes[poorCodesCounter][0]=firmCode;
							//	writeCSV(csv_out, output);
			        //  writeCSV(outputPoorCodesFile, outputPoorCodes);
			        //  poorCodesCounter++;
			      }else{			          
			          writeCSV(csv_out, output);
			          finalFlag=1;  
			      }
										
	      }catch (Exception e){e.printStackTrace();}
	      
	      return finalFlag;
	      
    }//end of preProcess1   
    
    
    // Create a list of codes 
    public static void preProcess2(String csv_in, String csv_out) throws Exception{
       List<String[]> input = readCSV(csv_in);				
			 String[][] output = new String[800][1];
			 
			 writeCSV(csv_out, output);    
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