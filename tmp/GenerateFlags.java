// class for manipulating stock data

import java.util.*;
import java.io.*;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

class GenerateFlags{  
       
    // main program
      public static void main(String[] args){
           
           String inputFile = "c:\\java\\TWStocks\\StockList.txt"; 
           String tmpLine ="";
           String inputData1="";
           String inputData2="c:\\java\\TWStocks\\data\\todayAR.csv";              
           String outputData="";
           String outputReport="c:\\java\\TWStocks\\reports\\FinalReport.csv";
           String [][] finalOutput = null; //final output array for report
           int inputColNumber=11; //use the 7th column (CAR3) of the input file for generating flags
           double range=0.01; // the value +- 0.01 will be identified as flag=1
           int i,k =0; //(firm codes record count)
       
           try{
            
             BufferedReader input1 =  new BufferedReader(new FileReader(inputFile));
             tmpLine = null;
             List<String[]> input2 = readCSV(inputData2);
             finalOutput = new String[input2.size()][35]; // 1 col. for code, 17 columns for Average ARs, 17 columns for number of records
             
             // preparing headings
             finalOutput[0][0]="Code";
             for(k=1; k<=17;k++){
                    finalOutput[0][k]="Avg AR_"+k;
                    finalOutput[0][k+17]="Count AR_"+k;
             }
             
             for(i=1;i<input2.size(); i++){
                   finalOutput[i][0]=input2.get(i)[28];
                     for(k=1; k<=17;k++){
                            finalOutput[i][k]="0.0";
                            finalOutput[i][k+17]="0.0";
                     }
             }
             
             // main loop
             for (i=1; i < input2.size(); i++) { //looping for company codes
                try {
                       // read the input csv file
                       inputData1="c:\\java\\TWStocks\\returns\\"+input2.get(i)[28]+"_RR.csv";            
                       outputData="c:\\java\\TWStocks\\flags\\"+input2.get(i)[28]+"_flag.csv";                 
                       finalOutput=getFlag(inputData1, outputData, Double.parseDouble(input2.get(i)[inputColNumber-1]), range, inputColNumber, finalOutput, i);                       
                    }catch (NumberFormatException nfe){
                                nfe.printStackTrace();                              
                    }    
             }                     
             input1.close();
             writeCSV(outputReport, finalOutput);             
          }catch(Exception e){
             e.printStackTrace();
          }            
      }
    
      public static String [][] getFlag(String inputCSV, String outputCSV, double inputAR, double range, int inputColNumber, String [][] outputReport, int recordNumber) throws Exception {
                List<String[]> input = readCSV(inputCSV);
                String [][] output = new String [input.size()][2];          
                output[0][0]="Date";
                output[0][1]="Flag";
                double tmpValue = 0.0;
                double tmpAvg = 0.0;
                double tmpCount = 0.0;
                int k=1; //column counter
                for(int j=1;j<input.size();j++){
                   try{
                       tmpValue=Double.parseDouble(input.get(j)[inputColNumber-3]);// target value
                       if(tmpValue<=inputAR+range && tmpValue>=inputAR-range){
                            output[j][0]=input.get(j)[0];
                            output[j][1]="1";
                            for(k=1; k<=17;k++){ //preparing average and record count
                                  try{
                                      tmpAvg=Double.parseDouble(outputReport[recordNumber][k]);
                                      tmpCount=Double.parseDouble(outputReport[recordNumber][k+17]);
                                      outputReport[recordNumber][k]=Double.toString(tmpAvg+Double.parseDouble(input.get(j)[k+1]));
                                      outputReport[recordNumber][k+17]=Double.toString(tmpCount+1);
                                }catch(Exception ee2){;}
                            }                                           
                       }else{
                            output[j][0]=input.get(j)[0];
                            output[j][1]="0";
                       }
                   }catch(Exception ee){
                       output[j][0]=input.get(j)[0];
                       output[j][1]="0";
                   }
                }
                            
                writeCSV(outputCSV, output);
                
                // calculate the final average. 
                for( k=1; k<=17;k++){
                  //System.out.println(outputReport[recordNumber][k]+"==="+outputReport[recordNumber][k+17]);                  
                  outputReport[recordNumber][k]=Double.toString(Double.parseDouble(outputReport[recordNumber][k])/Double.parseDouble(outputReport[recordNumber][k+17]));                                      
                }            
          return outputReport;
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
