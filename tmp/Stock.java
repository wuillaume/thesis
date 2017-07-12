// class for manipulating stock data

import java.util.*;
import java.io.*;

//import au.com.bytecode.opencsv.CSVReader;
//import au.com.bytecode.opencsv.CSVWriter;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

class Stock {
    
    // main
    public static void main(String args[]) {
	     csvCalcAbReturn("2.csv", "3.csv", 200);
    }

    // reads csv list of prices (stock, market, riskfree) and calculates abnormal return
    // uses hist_no as the number of days before each day to predict price
    // outputs data to csv
    public static void csvCalcAbReturn(String csv_in, String csv_out, int hist_no) {
 	   	double[] stock_return = new double[hist_no];
			double[] market_return = new double[hist_no];
			double[] riskfree_rate = new double[hist_no];

			List<String[]> input = readCSV(csv_in);
			String[][] output = new String[input.size()][4];

			// fill up rows used as history
			for (int i=0; i<hist_no; i++) {
	    			output[i][0] = input.get(i)[0];
	    			output[i][1] = input.get(i)[1];
	    			output[i][2] = input.get(i)[2];
	    			output[i][3] = "";
			}

	    // calculate abnormal return for other row
	    //	for (int i=hist_no; i<hist_no+1; i++) {
    	for (int i=hist_no; i<input.size(); i++) {
    	    // build the history arrays
    	    for (int j=0; j<hist_no; j++) {
									stock_return[j] = Double.parseDouble(input.get(i-hist_no+j)[0]);
									market_return[j] = Double.parseDouble(input.get(i-hist_no+j)[1]);
									riskfree_rate[j] = Double.parseDouble(input.get(i-hist_no+j)[2]);
	        }
	    
			double curr_stock_return = Double.parseDouble(input.get(i)[0]);
			double curr_market_return = Double.parseDouble(input.get(i)[1]);
			double curr_riskfree_rate= Double.parseDouble(input.get(i)[2]);

	    double ab_return = calcABReturn(stock_return, market_return, riskfree_rate,
					    curr_stock_return, curr_market_return,
					    curr_riskfree_rate);

	    output[i][0] = input.get(i)[0];
	    output[i][1] = input.get(i)[1];
	    output[i][2] = input.get(i)[2];
	    output[i][3] = Double.toString(ab_return);
	 }

	    writeCSV(csv_out, output);
	    
    }

    public static double calcABReturn(double stock_return[], double market_return[],
				      double riskfree_rate[], double curr_stock_return,
				      double curr_market_return, double curr_riskfree_rate) {
				      	
				      	
							double[] sr_rf = new double[stock_return.length];
							double[] mr_rf = new double[stock_return.length];
							for (int i=0; i<stock_return.length; i++) {
							    sr_rf[i] = stock_return[i] - riskfree_rate[i];
							    mr_rf[i] = market_return[i] - riskfree_rate[i];
	            }
	            
							RegressionCalculator reg = new RegressionCalculator(mr_rf, sr_rf);
							double a = reg.getIntercept();
							double b = reg.getSlope();
							double sr_expected = a + b*(curr_market_return-curr_riskfree_rate) + curr_riskfree_rate;
							double ab_return = curr_stock_return - sr_expected;							
							
							return ab_return;
							
    }

    public static List<String[]> readCSV(String csv_in) {
    	
				try {				    
					
				    CsvReader reader = new CsvReader(new FileReader(csv_in));
				    List entries = new ArrayList<String[]>();				    
				    while (reader.readRecord())
					  entries.add(reader.getValues());			
				    return entries;
				    
				}catch(IOException e) {
				    return null;
				}
	  }

    public static void writeCSV(String csv_out, String data[][]) {
				try {
				     CsvWriter writer = new CsvWriter(new FileWriter(csv_out), ',');
				     for (int i=0; i<data.length; i++)
					   Writer.writeRecord(data[i]);
				     Writer.close();
				}catch(IOException e) {
				}
    }
        
}