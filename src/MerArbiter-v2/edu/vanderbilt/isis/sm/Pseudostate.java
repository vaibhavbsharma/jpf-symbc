package edu.vanderbilt.isis.sm;
import java.util.*;

public class Pseudostate implements IVertex {
	
	private IPseudoParent parent;
	private Kind kind;
	private ArrayList<Transition> outTransitions, inTransitions;
	
	public Pseudostate(IPseudoParent parent, Kind kind){
		this.parent = parent;
		this.kind = kind;
		this.outTransitions = new ArrayList<Transition>();
		this.inTransitions = new ArrayList<Transition>();
		
		parent.addPseudoChild(this);
	}
	
	public final Kind getKind() {
		return this.kind;
	}
	
	public final boolean isPseudostate() {
		return true;
	}
	
	public final boolean isState() {
		return false;
	}
	
	public final Region getRegionParent() {
		if (!parent.isState()) {
			return (Region)parent;
		}
		else {
			return null;
		}
	}
	
	// Used by history pseudostates to see if the region that
	// contains this pseudostate has been active before.
	public final State getRegionLastActive() {
		assert (this.kind == Kind.SHALLOWHISTORY || this.kind == Kind.DEEPHISTORY);
		if (!this.parent.isState()) {
			return ((Region) parent).getLastActive();
		}
		else {
			return null;
		}
	}
	
	public final State getStateForInitial() {
		assert this.kind == Kind.INITIAL;
		return ((Region)this.parent).getStateParent();
	}
	
	public final List<Transition> outgoing() {
		Collections.sort(this.outTransitions);
		return this.outTransitions;
	}
	
	public final List<Transition> incoming() {
		return this.inTransitions;
	}
	
	public final void addOutgoing(Transition t) {
		this.outTransitions.add(t);
	}
	
	public final void addIncoming(Transition t) {
		this.inTransitions.add(t);
	}
	
	public final List<State> getPathToRoot() {
		ArrayList<State> path = new ArrayList<State>();
		State s = null;
		if (!this.parent.isState()) {
			s = ((Region)this.parent).getStateParent(); // may be null
		}
		else if(this.parent.isState()) {
			s = (State)this.parent;
		}
		
		while (s != null) {
			path.add(s);
			s = s.getGrandParent();
		}
		
		return path;
	}
	
	// Assumption: EXITPOINT can have only one outgoing transition
	public enum Kind {
		INITIAL, DEEPHISTORY, SHALLOWHISTORY, JOIN, FORK, JUNCTION, CHOICE,
		ENTRYPOINT, EXITPOINT, TERMINATE, CONDITIONAL
	}
}
