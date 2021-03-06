// class for manipulating stock data

import java.util.*;
import java.io.*;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

class PriceToReturn{  
       
    // main program
	  public static void main(String[] args){
		   String inputFile = ".\\StockList.txt";
		   String tmpLine ="";
		   String inputData="";
		   String outputData="";
	   
		   try {
		   	
	      	 BufferedReader input =  new BufferedReader(new FileReader(inputFile));     	 
	      	 tmpLine = null;	
	         while (( tmpLine = input.readLine()) != null){
	         	     inputData=".\\output\\"+tmpLine+".csv";
	         	     outputData=".\\output1\\"+tmpLine+"_R.csv";      
	               csvCalcReturn(inputData, outputData);
	         }
	         input.close();
	    	}catch (Exception e){
					      		e.printStackTrace();					      		
	    	}
	  }
    

    // reads csv list of prices (stock, market, riskfree) and calculates abnormal return
    // uses hist_no as the number of days before each day to predict price
    // outputs data to csv
    public static void csvCalcReturn(String csv_in, String csv_out) {
				
				// the arrays for historical data
				String csv_Index = ".\\data\\TW.csv";
				
				// tmp output for AR
				double [] outputAR = new double[4]; //4 means AR, alpha, beta, and R-Square after computing AR
				
		try{
				 List<String[]> input = readCSV(csv_in);
				 List<String[]> inputIndexRf = readCSV(csv_Index);
				 String[][] output = new String[input.size()][28];
				 double tmpToday = 0.0;
				 double tmpYesterday = 0.0;
				
				// fill up headings
				    output[0][0] = input.get(0)[0];
				    output[0][1] = input.get(0)[1];
				    output[0][2] = input.get(0)[2];
				    output[0][3] = input.get(0)[3];
				    output[0][4] = input.get(0)[4];
				    output[0][5] = input.get(0)[5];
				    output[0][6] = input.get(0)[6];
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
				    output[0][27] = "R^2";				    

				// fill up rows with data copied from input and calculating return
				for (int i=1; i<input.size()-1; i++) {
				    output[i][0] = input.get(i)[0];
				    output[i][1] = input.get(i)[1];
				    output[i][2] = input.get(i)[2];
				    output[i][3] = input.get(i)[3];
				    output[i][4] = input.get(i)[4];
				    output[i][5] = input.get(i)[5];
				    output[i][6] = input.get(i)[6];
				    tmpToday=Double.parseDouble(input.get(i)[6]);
				    //System.out.println(input.get(i+1)[6]);
				    tmpYesterday=Double.parseDouble(input.get(i+1)[6]);
				    output[i][7] = Double.toString((tmpToday/tmpYesterday)-1);
				}//end of for loop
				
				
				// calculating abnormal return
				for (int i=1; i<input.size()-251; i++) {
					  try{
					  
				       outputAR = calcAR(output, inputIndexRf, 250, i); // inputIndexRf input array of stock index and risk-free rate
			         output[i][8] = Double.toString(outputAR[0]);	// 250 is used as the sample period to calculate beta
			         output[i][25] = Double.toString(outputAR[1]); // get alpha				         
			         output[i][26] = Double.toString(outputAR[2]); // get beta	
			         output[i][27] = Double.toString(outputAR[3]); // get R^2				         
				         
				    }catch (Exception e1){
				    	;
				    	//output[i][8] ="0";
				    };     
				}//end of for loop
				
				// calculating CAR
				for (int i=1; i<input.size()-251; i++) {
					  try{
						    output[i][9] = calcCAR(output, 2, i);
						    output[i][10] = calcCAR(output, 3, i);
						    output[i][11] = calcCAR(output, 4, i);
						    output[i][12] = calcCAR(output, 5, i);
						    output[i][13] = calcCAR(output, 6, i);
						    output[i][14] = calcCAR(output, 7, i);
						    output[i][15] = calcCAR(output, 8, i);
						    output[i][16] = calcCAR(output, 9, i);
						    output[i][17] = calcCAR(output, 10, i);
						    output[i][18] = calcCAR(output, 15, i);
						    output[i][19] = calcCAR(output, 20, i);
						    output[i][20] = calcCAR(output, 25, i);
						    output[i][21] = calcCAR(output, 30, i);
						    output[i][22] = calcCAR(output, 35, i);
						    output[i][23] = calcCAR(output, 40, i);
						    output[i][24] = calcCAR(output, 50, i);
						}catch (Exception e2){;}    
				}//end of for loop

				writeCSV(csv_out, output);
				
	  }catch (Exception e){e.printStackTrace();}
	  
    }//end of csvCalcReturn
    
    
    
    /********************************************************************
      Function for computing AR by Returns
    ***********************************************************************/    
    public static double [] calcAR(String[][] output, List<String[]> inputIndexRf, int hist_no, int recordCount) throws Exception{
         
         double [] output1 = new double[4];
         
         double[] stock_return = new double[hist_no];
			   double[] market_return = new double[hist_no];
			   double[] riskfree_rate = new double[hist_no];			   
			   			   
			   double tmpStockReturn = Double.parseDouble(output[recordCount][7]); // current stock return
			   double tmpMarketReturn = Double.parseDouble(inputIndexRf.get(recordCount)[1]); // current market return
			   double tmpRf = Double.parseDouble(inputIndexRf.get(recordCount)[2]); // current risk-free rate
			   
			   //System.out.println(tmpStockReturn);
			   //System.out.println(tmpMarketReturn);
			   //System.out.println(tmpRf);  

				 // fill up rows used as history
				 for (int k=0; k<hist_no; k++) {
				 	      riskfree_rate[k] = Double.parseDouble(inputIndexRf.get(recordCount+k+1)[2]); // risk-free rate
			    			stock_return[k] = Double.parseDouble(output[recordCount+k+1][7])-riskfree_rate[k]; // stock return
			    			market_return[k] = Double.parseDouble(inputIndexRf.get(recordCount+k+1)[1])-riskfree_rate[k]; // market return			    				    			
				 }
			   
			   //System.out.println(stock_return[50]);
			   //System.out.println(market_return[50]);
			   //System.out.println(riskfree_rate[50]);
			   
			   //output1 = calcABReturn(stock_return,market_return,riskfree_rate,tmpStockReturn,tmpMarketReturn,tmpRf);     
			   
			   	RegressionCalculator reg = new RegressionCalculator(market_return, stock_return);
				 	output1[1] = reg.getIntercept(); // get intercept
					output1[2] = reg.getSlope();		 // get beta
					output1[3]= reg.getSSR()/(reg.getSSR()+reg.getSSE());	 // get R^2
					output1[0] = (tmpStockReturn-tmpRf) - (output1[1] + output1[2]*(tmpMarketReturn-tmpRf)); //get abnormal return
					return output1;
    }
    
      
    /********************************************************************
      Function for computing CAR by summming ARs
    ***********************************************************************/    
    public static String calcCAR(String[][] output, int days, int recordCount) throws Exception{
    	   double finalCAR=0.0;
    	   String outputFinal = "";  	   
         for(int j=1; j<days+1; j++){
            finalCAR=finalCAR+Double.parseDouble(output[recordCount+days-j][8]);
         }
         outputFinal = Double.toString(finalCAR);
         return outputFinal;
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