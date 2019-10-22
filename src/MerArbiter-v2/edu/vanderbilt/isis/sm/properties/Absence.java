package edu.vanderbilt.isis.sm.properties;

import edu.vanderbilt.isis.sm.Interpreter;;

public class Absence extends Pattern {
	
	protected boolean propertyViolated = false;
	
	protected boolean propertyPotentiallyViolated = false;
	
	public boolean checkProperty(Interpreter interpreter){
		// if we are in scope and the expression is true, then it is not absent
		int scopeIsActive = scope.isActive(interpreter);
		if (scopeIsActive == Scope.ACTIVE && checkExpression(interpreter)){
			propertyViolated = true;
		}
		if (scopeIsActive == Scope.UNKNOWN && checkExpression(interpreter)){
			propertyPotentiallyViolated = true;
		}
		if (scopeIsActive == Scope.POST_ACTIVE && propertyPotentiallyViolated){
			propertyViolated = true;
		}
		return propertyViolated;
	}
}
