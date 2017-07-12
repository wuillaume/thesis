// class for manipulating stock data

import java.util.*;
import java.io.*;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

class TodayAR{  

    // this program will compile the list of firms with the most recent AR and CARs
       
    // main program
	  public static void main(String[] args){
		   String inputFile = "c:\\java\\TWStocks\\output\\StockList.txt";		      
		   String tmpLine ="";
		   String inputData="";
		   String outputData="c:\\java\\TWStocks\\data\\todayAR.csv";
		   String[][] output = new String[800][29];
		   List<String[]> input = null;
		   int outputCounter = 1;
		   							// fill up headings								    
								    output[0][0] = "Date";
								    output[0][1] = "Open";
								    output[0][2] = "High";
								    output[0][3] = "Low";
								    output[0][4] = "Close";
								    output[0][5] = "Vol";
								    output[0][6] = "Adj Close";
								    output[0][7] = "Return";
								    output[0][8] = "AR";								    
								    output[0][9] = "CAR2";
								    output[0][10] = "CAR3";
								    output[0][11] = "CAR4";
								    output[0][12] = "CAR5";
								    output[0][13] = "CAR6";
								    output[0][14] = "CAR7";
								    output[0][15] = "CAR8";
								    output[0][16] = "CAR9";
								    output[0][17] = "CAR10";
								    output[0][18] = "CAR15";
								    output[0][19] = "CAR20";
								    output[0][20] = "CAR25";
								    output[0][21] = "CAR30";
								    output[0][22] = "CAR35";
								    output[0][23] = "CAR40";
								    output[0][24] = "CAR50";
								    output[0][25] = "alpha";
								    output[0][26] = "beta";
								    output[0][27] = "r^2";
								    output[0][28] = "Code";						    
								    							    
								       
		   try {
		   	
	      	 BufferedReader input1 =  new BufferedReader(new FileReader(inputFile));     	 
	      	 tmpLine = null;	
	         while (( tmpLine = input1.readLine()) != null){
	         	     try{
			         	     inputData="c:\\java\\TWStocks\\data\\"+tmpLine+"_R.csv";     	            
			               input = readCSV(inputData);
			               output[outputCounter][28] = tmpLine;
			               for(int j=0;j<28;j++){
			                  output[outputCounter][j] = input.get(1)[j];
			               }
			               outputCounter++;
			           }catch(FileNotFoundException fnfe){fnfe.printStackTrace();}             
	         }
	         input1.close();
	         writeCSV(outputData, output);
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