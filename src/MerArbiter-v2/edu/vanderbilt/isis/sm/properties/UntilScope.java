package edu.vanderbilt.isis.sm.properties;

import edu.vanderbilt.isis.sm.Interpreter;;

public class UntilScope implements Scope {

	private int active = INACTIVE;

	public int isActive(Interpreter interpreter) {
		if (active == INACTIVE && checkAfterExpression(interpreter)){
			active = ACTIVE;
		}
		if (active == ACTIVE && checkBeforeExpression(interpreter)){
			active = INACTIVE;
		}
		return active;
	}

	public boolean checkBeforeExpression(Interpreter interpreter){
		return true;
	}
	
	public boolean checkAfterExpression(Interpreter interpreter){
		return true;
	}
}
