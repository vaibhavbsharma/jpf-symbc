package edu.vanderbilt.isis.sm;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSVDataProvider implements IDataProvider,IDataPrinter {
    private ArrayList<ArrayList<String>> dataMatrix = new ArrayList<ArrayList<String>>();
    private int lineIndex;
    private int columnIndex;
    private boolean hasData = false;
    private boolean doAdvance = false;
    public boolean checkOutput = false;
    
    private boolean problemFound = false;
    
    public CSVDataProvider(String fileName, boolean checkOutput ) throws Exception{
    	this.checkOutput = checkOutput;
        this.lineIndex = 0;
        this.columnIndex = 0;
        
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String strLine;
        
        // Throw the first line away, it contains the header
        br.readLine();
        
        //Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
        	if (!strLine.isEmpty()){
	        	String[] inputs = strLine.split(",");
	        	ArrayList<String> line = new ArrayList<String>(); 
	            for( int ix = 0 ; ix < inputs.length ; ix++ ) {
	            	line.add(inputs[ix]);
	            }
	            dataMatrix.add(line);
        	}
        }
        fis.close();
        if (dataMatrix.size() > 0){
        	hasData = true;
        }
        
        if (checkOutput){
        	File resultFile = new File("result.txt");
        	if (resultFile.exists()){
        		resultFile.delete();
        	}
        	resultFile.createNewFile();
        	FileWriter fw = new FileWriter(resultFile);
        	fw.write("OK");
        	fw.close();
        }

    }
    
    public String readData(){
    	String returnValue = "";
    	if (doAdvance){
    		lineIndex++;
    		columnIndex = 0;
    		doAdvance = false;
    	}
    	if (lineIndex < dataMatrix.size()){
    		if (lineIndex == dataMatrix.size()-1){
    	    	hasData = false;
    		}
    		if (columnIndex < dataMatrix.get(lineIndex).size()){
    			returnValue = dataMatrix.get(lineIndex).get(columnIndex);
    			columnIndex++;
    		} else{
    			returnValue = "";
    		}
    	}else{
    		returnValue = "";
    	}
    	return returnValue;
    }

	public boolean hasData() {
		return hasData;
	}

	public String readEvent() {
		// for now it returns the empty event
		return "";
	}

	public void advance() {
		doAdvance = true;
	}

	public void writeData(ArrayList<String> values) {
		// If we have already triggered this flag why bother checking
		if (problemFound) return;
		
		// Tries to match the received vector to the and of the actual row
		ArrayList<String> line = dataMatrix.get(lineIndex);
		int li = line.size() - values.size();
		for (int i = 0; i < values.size(); i++) {
			
			String val1 = values.get(i);
			String val2 = line.get(li+i);
			if (!val1.equals(val2)){
				problemFound = true;
				try{
		        	File resultFile = new File("result.txt");
		        	if (resultFile.exists()){
		        		resultFile.delete();
		        	}
		        	resultFile.createNewFile();
		        	FileWriter fw = new FileWriter(resultFile);
		        	fw.write("FAILED");
		        	fw.close();
				}catch (Exception e){}
			}
		}
	}
    
}
