package edu.vanderbilt.isis.sm.properties;

import edu.vanderbilt.isis.sm.Interpreter;;

public interface Scope {

	public static int STATE = 0;
	public static int EVENT = 1;
	
	public static int INACTIVE = 0;
	public static int ACTIVE = 1;
	public static int UNKNOWN = 2;
	public static int POST_ACTIVE = 3;
	
	public int isActive(Interpreter interpreter);
}
