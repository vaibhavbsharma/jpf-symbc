package edu.vanderbilt.isis.sm;

import java.util.ArrayList;

public class SymbolicDataProvider implements IDataProvider, IDataPrinter {

	private int steps;
	
	public SymbolicDataProvider(int steps){
		this.steps = steps;
	}
	
	public void advance() {
		steps--;
	}

	public boolean hasData() {
		return steps > 0;
	}

	public String readData() {
		return "0";
	}

	public String readEvent() {
		return "";
	}

	public void writeData(ArrayList<String> values) {

	}

}
