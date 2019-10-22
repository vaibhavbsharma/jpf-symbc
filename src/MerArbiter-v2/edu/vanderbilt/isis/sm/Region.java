package edu.vanderbilt.isis.sm;
import java.util.*;

import edu.vanderbilt.isis.sm.Pseudostate.Kind;

public class Region implements IPseudoParent, Comparable<Region> {
	private IRegionParent parent; // Either a StateMachine or a State
	private ArrayList<Pseudostate> pseudostates;
	private ArrayList<State> states;
	private ArrayList<Transition> transitions;
	private State lastActive;
	private int order;
	
	public Region(IRegionParent parent) {
		this.parent = parent;
		
		this.pseudostates = new ArrayList<Pseudostate>();
		this.states = new ArrayList<State>();
		this.transitions = new ArrayList<Transition>();
		this.lastActive = null;
		parent.addRegionChild(this);
	}
	
	// Constructor for Stateflow semantics in which AND states have an order
	public Region(IRegionParent parent, int order) {
		this(parent);
		this.order = order;
	}
	
	public final Pseudostate getInitial() {
		for (Pseudostate p : this.pseudostates) {
			if (p.getKind() == Kind.INITIAL) {
				return p;
			}
		}
		
		return null;
	}
	
	public final boolean hasHistory() {
		for (Pseudostate p : this.pseudostates) {
			Pseudostate.Kind kind = p.getKind();
			if (kind == Kind.SHALLOWHISTORY || kind == Kind.DEEPHISTORY) {
				return true;
			}
		}
		
		return false;
	}
	
	public int compareTo(Region r) {
		if (this.getOrder() < r.getOrder()) {
			return -1;
		}
		else if (this.getOrder() > r.getOrder()) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public final List<State> getStates() {
		return this.states;
	}
	
	public final void setLastActive(State s) {
		assert s != null;
		this.lastActive = s;
	}
	
	public final State getLastActive() {
		return this.lastActive;
	}
	
	public final void addTransitionChild(Transition t) {
		if (!this.transitions.contains(t)) {
			this.transitions.add(t);
		}
	}
	
	public final void addStateChild(State s) {
		if (!this.states.contains(s)) {
			this.states.add(s);
		}
	}
	
	public final boolean isTopLevelRegion() {
		return !this.parent.isState();
	}
	
	public final State getStateParent() {
		if (this.parent.isState())
			return (State) this.parent;
		else
			return null;
	}
	
	public final IRegionParent getParent() {
		return this.parent;
	}
	
	public final List<Region> getOrthogonal() {
		return this.parent.getRegions();
	}
	
	public final void addPseudoChild(Pseudostate p) {
		if (!this.pseudostates.contains(p)) {
			this.pseudostates.add(p);
		}
	}
	
	public final boolean isState() {
		return false;
	}
}
