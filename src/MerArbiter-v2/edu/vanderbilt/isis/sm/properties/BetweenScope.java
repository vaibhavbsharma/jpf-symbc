package edu.vanderbilt.isis.sm.properties;

import edu.vanderbilt.isis.sm.Interpreter;

public class BetweenScope implements Scope {

	private int active = INACTIVE;

	public int isActive(Interpreter interpreter) {
		if (active == POST_ACTIVE){
			if (checkAfterExpression(interpreter)){
				active = UNKNOWN;
			}else{
				active = INACTIVE;
			}
		}
		if (active == INACTIVE && checkAfterExpression(interpreter)){
			active = UNKNOWN;
		}
		if (active == UNKNOWN && checkBeforeExpression(interpreter)){
			active = POST_ACTIVE;
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
