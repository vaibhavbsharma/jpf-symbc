package edu.vanderbilt.isis.sm;
import java.util.*;

public class StateMachine implements IRegionParent {
	public ArrayList<Region> regions;

	public StateMachine(){
		this.regions = new ArrayList<Region>();
	}

	public List<Pseudostate> getInitial() {
		ArrayList<Pseudostate> initial = new ArrayList<Pseudostate>();
		Pseudostate p = null;
		for (Region r : this.regions) {
			if ((p = r.getInitial()) != null) {
				initial.add(p);
			}
		}

		return initial;
	}
	
	// Returns true if this machine has a top-level state with only junctions 
	public final boolean isJunctionMachine() {
		for (Region r : this.regions) { // TODO: could optimize this by statically checking earlier
			if (r.getStates().size() == 1) {
				if (!r.getStates().get(0).isJunctionState()) {
					return false;
				}
			}
			else {
				return false;
			}
		}
		
		return !this.regions.isEmpty();
	}

	public final void getTopLevelData() {
		if (this.regions.size() == 1) {
			Region r = this.regions.get(0);
			if (r.getStates().size() == 1) {
				State s = r.getStates().get(0);
			}
		}
	}

	public final void addRegionChild(Region r) {
		if (!this.regions.contains(r)) {
			this.regions.add(r);
			Collections.sort(this.regions);
		}
	}

	public final Region getRegionParent() {
		return null;
	}

	public final List<Region> getRegions() {
		return this.regions;
	}
	
	public boolean isState() {
		return false;
	}
}
