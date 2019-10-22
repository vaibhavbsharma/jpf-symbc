package edu.vanderbilt.isis.sm.properties;

import edu.vanderbilt.isis.sm.Interpreter;

public class GlobalScope implements Scope {
	
	public int isActive(Interpreter interpreter) {
		return ACTIVE;
	}
}
