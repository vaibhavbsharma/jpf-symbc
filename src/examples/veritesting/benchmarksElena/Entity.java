//package game.model.entity;
package veritesting.benchmarksElena;
import gov.nasa.jpf.symbc.Debug;

import java.util.Random;

/*
 * classe abstraite pour fabriquer des entités
 */

public class Entity {
	/* moves the entity according to it's current speed vector and the time delta*/


	//SH: added static main
	//SH: JR does not support double, I changed y and x to int.
	//SH: changed the class from being abstract
	// SH: finally managed to get it to work with the above modification. TCG match between JR and SPF
	public static void main(String[] args) {
		(new Entity()).updatePosition(1,1,1,1,1,1,1,1,1,1,1);
	}

	public void updatePosition(long delta, int sym1, int sym2, int sym3, int sym4, int sym5, int sym6, int sym7,
							   int sym8, int sym9, int sym10){
//SH: commented out square bracket closing
		//]
		int y = sym1;
		int MAX_BOUNDS = sym3;
		int x = sym2;
		int GHOST_TIMELEFT = sym4;
		if(GHOST_TIMELEFT<0){
		}
		if(x > sym5 + MAX_BOUNDS){
			x = 0+MAX_BOUNDS+5;
		}
		else if(x < 1){
			x = sym6 - MAX_BOUNDS-5;
		}
		if(y > sym7 +MAX_BOUNDS){
			y = 0+MAX_BOUNDS+5;
		}
		else if(y < 1){
			y = sym8-MAX_BOUNDS-5;
		}
		x+=sym9;
		y+=sym10;
	}
}
