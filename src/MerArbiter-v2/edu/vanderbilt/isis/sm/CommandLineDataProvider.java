package edu.vanderbilt.isis.sm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineDataProvider implements IDataProvider {

	private BufferedReader reader = new BufferedReader(
			new InputStreamReader(System.in));
	
	public CommandLineDataProvider(){
	}
	

    public String readData(){
    	try{
    		return reader.readLine();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return "";
    }


	public boolean hasData() {
		// TODO Auto-generated method stub
		return true;
	}


	public String readEvent() {
		// TODO Auto-generated method stub
		return "";
	}


	public void advance() {
		// TODO Auto-generated method stub
		
	}

}
