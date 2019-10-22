package edu.vanderbilt.isis.sm;

public interface IDataProvider {
	public String readData();
	public boolean hasData();
	public String readEvent();
	/* Advances the input stream to the next set of input data. Has to be called explicitly*/
	public void advance();
}
