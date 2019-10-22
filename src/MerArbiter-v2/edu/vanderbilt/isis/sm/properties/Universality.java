package edu.vanderbilt.isis.sm.properties;

import edu.vanderbilt.isis.sm.Interpreter;;

public class Universality extends Pattern {
	
	protected boolean propertyViolated = false;
	
	protected boolean propertyPotentiallyViolated = false;
	
	public boolean checkProperty(Interpreter interpreter){
		// if we are in scope and the expression is false, then it is not universal
		int scopeIsActive = scope.isActive(interpreter);
		if (scopeIsActive == Scope.ACTIVE && !checkExpression(interpreter)){
			propertyViolated = true;
		}
		if (scopeIsActive == Scope.UNKNOWN && !checkExpression(interpreter)){
			propertyPotentiallyViolated = true;
		}
		if (scopeIsActive == Scope.POST_ACTIVE){
			if (propertyPotentiallyViolated)
				propertyViolated = true;
			propertyPotentiallyViolated = false;
		}
		return propertyViolated;
	}
}
