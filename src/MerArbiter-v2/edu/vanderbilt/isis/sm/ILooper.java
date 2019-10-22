package edu.vanderbilt.isis.sm;

public interface ILooper {
	void setInterpreter(Interpreter interpreter);	
	void doEventLoop();
	void doDataAndEventLoop();
}
