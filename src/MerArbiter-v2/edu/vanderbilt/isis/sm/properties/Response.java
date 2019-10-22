package edu.vanderbilt.isis.sm.properties;

import edu.vanderbilt.isis.sm.Interpreter;;

public class Response extends Pattern {
	
	private boolean propertyViolated = false;
	
	private boolean firstPropertyEncountered = false;
	
	private boolean secondPropertyEncountered = false;
	
	private boolean scopeWasActive = false;
	
	public boolean checkProperty(Interpreter interpreter){
		int scopeIsActive = scope.isActive(interpreter);
		if (scopeIsActive == Scope.ACTIVE || scopeIsActive == Scope.UNKNOWN){
			scopeWasActive = true;
			if (checkExpression(interpreter))
				firstPropertyEncountered = true;
			if (firstPropertyEncountered && checkExpression2(interpreter))
				secondPropertyEncountered = true;
		}
		if (scopeIsActive == Scope.POST_ACTIVE || (scopeIsActive == Scope.INACTIVE && scopeWasActive)){
			if (firstPropertyEncountered && !secondPropertyEncountered)
				propertyViolated = true;
			firstPropertyEncountered = false;
			secondPropertyEncountered = false;
			scopeWasActive = false;
		}
		return propertyViolated;
	}
	
	protected boolean checkExpression(Interpreter interpreter) {
		return false;
	}
	
	protected boolean checkExpression2(Interpreter interpreter) {
		return false;
	}
}
