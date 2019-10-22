package edu.vanderbilt.isis.sm.properties;

import edu.vanderbilt.isis.sm.Interpreter;;

public class Precedence extends Pattern {

	private boolean propertyViolated = false;
	
	private boolean propertyPotentiallyViolated = false;
	
	private boolean firstPropertyEncountered = false;
	
	private boolean scopeWasActive = false;
	
	public boolean checkProperty(Interpreter sm) {
		int scopeIsActive = scope.isActive(sm);
		if (scopeIsActive == Scope.ACTIVE || scopeIsActive == Scope.UNKNOWN) {
			scopeWasActive = true;
			if (checkExpression(sm))
				firstPropertyEncountered = true;
			if (checkExpression2(sm) && !firstPropertyEncountered)
				if (scopeIsActive == Scope.ACTIVE)
					propertyViolated = true;
				else
					propertyPotentiallyViolated = true;
		}
		if (scopeIsActive == Scope.POST_ACTIVE || (scopeIsActive == Scope.INACTIVE && scopeWasActive)) {
			if (scopeIsActive == Scope.POST_ACTIVE && propertyPotentiallyViolated){
				propertyViolated = true;
			}
			propertyPotentiallyViolated = false;
			firstPropertyEncountered = false;
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
