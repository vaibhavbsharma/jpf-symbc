package edu.vanderbilt.isis.sm;

import java.util.List;
//import gov.nasa.jpf.jvm.Verify;

public class NondeterministicLooper implements ILooper {
	
	private Interpreter interpreter;

	public NondeterministicLooper() { }
		
	public void setInterpreter(Interpreter interpreter){
		this.interpreter = interpreter;
	}

	public void doDataAndEventLoop() {
		while(true) {
			interpreter.reader.setInput();
			int i = 0;
			List<Event> l = interpreter.getEnabled();
			//int i= choose(0,l.size()-1);
			interpreter.addEvent(new Event(l.get(i).name()));
			interpreter.step();
			interpreter.reader.writeOutput();
		}
	}

	public void doEventLoop() {
		while(true) {
			List<Event> l = interpreter.getEnabled();
			int i = 0;
			//int i= choose(0,l.size()-1);
			interpreter.addEvent(new Event(l.get(i).name()));
			interpreter.step();
			interpreter.reader.writeOutput();
		}
	}

	// Added by Corina
	// This method uses JPF to non-deterministically pick a value between min and max.
	//protected int choose(int min, int max) {
	//	if (min == max) {
	//		Verify.breakTransition();
	//	}
		
	//	return Verify.getInt(min,max);
	//}
}
