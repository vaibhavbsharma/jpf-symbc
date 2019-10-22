package edu.vanderbilt.isis.sm;

import java.util.*;

public class State implements IVertex, IRegionParent, IPseudoParent {
	private String name;
	private Region parent;
	private boolean isFinal;
	private ArrayList<Region> regions;
	private ArrayList<Transition> outTransitions, inTransitions;
	private ArrayList<Pseudostate> pseudostates;

	public State(String name, Region parent, boolean isFinal) {
		this.name = name;
		this.parent = parent;
		this.isFinal = isFinal;

		this.regions = new ArrayList<Region>();
		this.outTransitions = new ArrayList<Transition>();
		this.inTransitions = new ArrayList<Transition>();
		this.pseudostates = new ArrayList<Pseudostate>();
		this.parent.addStateChild(this);
	}

	public final boolean isJunctionState() {
		for (Region r : this.regions) {
			if (!r.getStates().isEmpty()) {
				return false;
			}
		}
		
		return true;
	}
	
	public final boolean isPseudostate() {
		return false;
	}

	public final boolean isState() {
		return true;
	}

	public void setLastActive() {
		this.parent.setLastActive(this);
	}

	public String getName() {
		return this.name;
	}

	public final State getGrandParent() {
		assert parent != null;
		if (this.parent.getParent().isState()) {
			return (State)(this.parent.getParent());
		}
		else {
			return null;
		}
	}
	
	// Returns true if this State is contained by a parallel State
	public final boolean hasParallelSiblings() {
		State s = this.getGrandParent();
		if (s != null) {
			return s.isOrthogonal();
		}
		else {
			return false;
		}
	}

	public final void addRegionChild(Region r) {
		if (!this.regions.contains(r)) {
			this.regions.add(r);
			Collections.sort(this.regions);
		}
	}

	public final Region getRegionParent() {
		return this.parent;
	}

	public final boolean hasNonEmptyHistory() {
		return (this.parent.hasHistory() && this.parent.getLastActive() != null);
	}

	public final List<Region> getRegions() {
		return this.regions;
	}

	public final Region getParent() {
		return this.parent;
	}

	public final boolean isSimple() {
		return this.regions.size() == 0;
	}

	public final boolean isComposite() {
		return this.regions.size() > 0;
	}

	public final boolean isOrthogonal() {
		return this.regions.size() > 1;
	}

	public final boolean isFinal() {
		return this.isFinal;
	}

	public final List<Transition> outgoing() {
		return this.outTransitions; // Already sorted
	}

	public final List<Transition> incoming() {
		return this.inTransitions; // Already sorted
	}

	public final void addOutgoing(Transition t) {
		if (!this.outTransitions.contains(t)) {
			this.outTransitions.add(t);
		}
		
		Collections.sort(this.outTransitions);
	}

	public final void addIncoming(Transition t) {
		if (!this.inTransitions.contains(t)) {
			this.inTransitions.add(t);
		}
		
		Collections.sort(this.inTransitions);
	}

	public final void addPseudoChild(Pseudostate p) {
		if (!this.pseudostates.contains(p)) {
			this.pseudostates.add(p);
		}
	}

	// Returns a list starting with this State to the top-level state
	public final List<State> getPathToRoot() {
		ArrayList<State> path = new ArrayList<State>();
		State s = this;
		while (s != null) {
			path.add(s);
			s = s.getGrandParent();
		}

		return path;
	}

	// Entry, Exit, and Do behaviors -- intended to be overwritten
	public void entryAction() {
	}

	public void exitAction() {
	}

	public void doAction() {
	}

}
