package edu.vanderbilt.isis.sm.properties;

import edu.vanderbilt.isis.sm.Interpreter;;

public class Existence extends Pattern {
	
	private boolean propertyViolated = false;
	
	private boolean propertyEncountered = false;
	
	private boolean scopeWasActive = false;
	
	public boolean checkProperty(Interpreter interpreter){
		int scopeIsActive = scope.isActive(interpreter);
		if (scopeIsActive == Scope.ACTIVE || scopeIsActive == Scope.UNKNOWN){
			scopeWasActive = true;
			if (checkExpression(interpreter))
				propertyEncountered = true;
		}
		if (scopeIsActive == Scope.POST_ACTIVE || (scopeIsActive == Scope.INACTIVE && scopeWasActive)){
			if (!propertyEncountered)
				propertyViolated = true;
			propertyEncountered = false;
			scopeWasActive = false;
		}
		return propertyViolated;
	}
}
