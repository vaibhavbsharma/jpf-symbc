package edu.vanderbilt.isis.sm;

import edu.vanderbilt.isis.sm.properties.*;

public class DefaultLooper implements ILooper {

	private Interpreter interpreter;
	
	public DefaultLooper() {}
	
	public void setInterpreter(Interpreter interpreter){
		this.interpreter = interpreter;
	}
	
	public void doEventLoop() {
		String event = "";
		while (interpreter.reader.hasData()) {
			event = interpreter.reader.readEvent();
			interpreter.addEvent(new Event(event));
			interpreter.step();
			interpreter.reader.writeOutput();
		}
	}
	
	public void doDataAndEventLoop() {
		String event = "";
		try {
			while (interpreter.reader.hasData()) {
				event = interpreter.reader.readEvent();
				interpreter.reader.setInput();
				interpreter.addEvent(new Event(event));
				interpreter.step();
				interpreter.checkProperties();
				interpreter.reader.writeOutput();
			}
		} catch(PropertyException e) {
			System.out.println("Property violated!");
		}
	}

}
