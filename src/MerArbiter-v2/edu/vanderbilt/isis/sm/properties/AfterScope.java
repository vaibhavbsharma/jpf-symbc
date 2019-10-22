package edu.vanderbilt.isis.sm.properties;

import edu.vanderbilt.isis.sm.Interpreter;

public class AfterScope implements Scope {

	private int active = INACTIVE;

	public int isActive(Interpreter interpreter) {
		if (active == INACTIVE && checkAfterExpression(interpreter)){
			active = ACTIVE;
		}
		return active;
	}

	public boolean checkAfterExpression(Interpreter interpreter){
		return true;
	}
}
