package edu.vanderbilt.isis.sm;

import java.util.*;

import edu.vanderbilt.isis.sm.Pseudostate.Kind;

public class SFInterpreter extends Interpreter {

	public SFInterpreter(StateMachine machine, IDataReader reader) {
		super(machine, reader);
	}
	
	public SFInterpreter(StateMachine machine, IDataReader reader, ILooper looper) {
		super(machine, reader, looper);
	}
	
	private List<State> getAllActiveStates() {
		ArrayList<State> allActive = new ArrayList<State>();
		for (State s : this.currStates) {
			for (State curr : s.getPathToRoot()) {
				if (!allActive.contains(curr)) {
					allActive.add(curr);
				}
			}
		}
		
		return allActive;
	}
	
	private State getRegionActive(Region r, List<State> activeStates) {
		for (State curr : r.getStates()) {
			if (activeStates.contains(curr)) {
				return curr;
			}
		}
		
		return null;
	}
	
	public void step()  {
		while (this.events.size() > 0) {
			this.currEvent = this.events.pop(); 
			assert this.currEvent != null;
			
			// If it is a junction machine
			if (this.machine.isJunctionMachine() || this.getAllActiveStates().isEmpty()) {
				List<Pseudostate> initial = this.machine.getInitial();
				for (Pseudostate p : initial) {
					Transition t = this.getSingleOutgoing(p);
					if ((t != null) && (this.testAndFire(t))) {
						this.validPath(t.getTarget());
					}
				}
			}
			else {
				this.transQ.clear();
				ArrayList<ArrayList<Transition>> allPaths = new ArrayList<ArrayList<Transition>>();
				LinkedList<Region> pendingRegions = new LinkedList<Region>();
				LinkedList<Region> nextRegions = new LinkedList<Region>(); 
				
				// Get the top-level states to try their transitions first
				List<State> allActive = this.getAllActiveStates();
				pendingRegions.addAll(this.machine.getRegions());
				
				this.oldStates.clear();
				this.nextStates.clear();
				boolean process = true;
				while (process) { // For each iteration, pendingRegions has a set of orthogonal Regions
					nextRegions.clear();
					while (!pendingRegions.isEmpty()) {
						Region currR = pendingRegions.pop();
						State currS = this.getRegionActive(currR, allActive);
						if (this.calculatePath(currS)) {
							allPaths.add(this.copyAndReverseTransQ());
							process = false;
						}
						else {
							nextRegions.addAll(currS.getRegions());
						}
					}
					
					if (nextRegions.isEmpty()) {
						process = false;
					}
					else {
						pendingRegions.addAll(nextRegions);
					}
				}
	
				// Process transitions
				this.nextStates.clear();
				for (ArrayList<Transition> currList : allPaths) {
					this.processTransitions(currList);
				}
				
				// Reset the state configuration
				for (State s : this.oldStates) {
					this.currStates.remove(s);
				}
				
				for (State t : this.nextStates) {
					if (!this.currStates.contains(t)) {
						this.currStates.add(t);
					}
				}
				
				this.printConfiguration();
			}
		}
	}
	
	private boolean calculatePath(State s) {
		this.transQ.clear();
		for (Transition t : s.outgoing()) {
			if (this.testAndFire(t)) {
				if (this.validPath(t.getTarget())) {
					this.transQ.add(t);
					return true;
				}
			}
		}
		
		return false;
	}
	
	// Executes the condition action of t and returns true if t can fire
	private boolean testAndFire(Transition t) {
		if (t.canFire(this.currEvent)) {
			t.conditionAction(this);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void initialize() {
		assert this.machine != null;
		
		if (this.machine.isJunctionMachine()) {
			// TODO: should we run through the initial transitions or not?
		}
		
		else {
			this.currEvent = new Event(""); // Needed for testing if a Transition can fire
			this.currStates.clear();
			ArrayList<ArrayList<Transition>> allPaths = new ArrayList<ArrayList<Transition>>();
			for (Pseudostate p : this.machine.getInitial()) { // Should we check if more than 1 initial?
				List<Transition> trans = p.outgoing();
				assert trans.size() == 1;

				Transition t = trans.get(0);
				if (this.testAndFire(t)) {
					if (this.validPath(t.getTarget())) {
						this.transQ.add(t);
						allPaths.add(this.copyAndReverseTransQ());
					}
				}
			}

			for (ArrayList<Transition> l : allPaths) {
				this.processTransitions(l);
			}

			this.currStates.addAll(this.nextStates);
			this.printConfiguration();
		}
	}
	
	private void processTransitions(List<Transition> transitions) {
		for (Transition t : transitions) {
			List<State> exited = this.exitedStates(t);
			List<State> entered = this.enteredStates(t);
			IVertex source = t.getSource();
			IVertex target = t.getTarget();
			
			// If the source is an initial, do its state's entry action first
			if (source.isPseudostate()) {
				Pseudostate p = (Pseudostate)source;
				if (p.getKind() == Kind.INITIAL) {
					State entryActionState = p.getStateForInitial();
					if (entryActionState != null) { // May be null if top level
						entryActionState.entryAction();
					}
				}
			}
			
			// TODO: Split this into 3 levels:
			// (a) From exited states to the current one
			// (b) The transition action? -- check and see when transition actions are executed
			// (c) From the current state to the target
			t.action(this);
			this.performExitActions(exited);
			if (target.isState()) {
				// set its region parent's history to this state
				State s = (State)target;
				if (s.isSimple()) {
					this.performEntryActions(entered);
					this.nextStates.add(s);
				}
				else if (s.isOrthogonal()) {
					List<Region> regions = s.getRegions();
					for (Region r : regions) { // TODO: May need to check if some States are already active 
						List<State> rStates = r.getStates(); // Should be size 1
						if (rStates.size() == 1) { 
							rStates.get(0).entryAction();
						}
						else {
							// ERROR!
						}
					}
				}
				else { // Composite
					if (s.hasNonEmptyHistory()) {
						this.performEntryActions(entered);
					}
					else {
						this.performEntryActions(entered.subList(0, entered.size() - 1));
					}
				}
			}
			else if (target.isPseudostate()) {
				Pseudostate p = (Pseudostate) target;
				Pseudostate.Kind kind = p.getKind();
				
				if (kind == Kind.SHALLOWHISTORY || kind == Kind.DEEPHISTORY) {
					
				}
				else if (kind == Kind.JUNCTION) {
					this.performEntryActions(entered);
				}
			}
		}
	}
	
	private boolean validPath(IVertex v) {
		if (v.isState()) {
			State s = (State) v;
			if (s.isJunctionState()) {
				List<Region> regions = s.getRegions();
				for (Region r : regions) {
					Pseudostate initial = r.getInitial();
					if (initial != null) {
						Transition initOut = this.getSingleOutgoing(initial);
						if (this.testAndFire(initOut)) {
							this.validPath(initOut.getTarget());
						}
					}
				}
				
				return true; // We don't use it anyway
			}
			if (s.isSimple()) {
				return true;
			}
			else if(s.isFinal()) {
				return true;
			}
			else if(s.isOrthogonal()) {  // No history allowed; activate each Region
				List<Region> regionList = s.getRegions(); // Already sorted
				ArrayList<Transition> orthoQ = new ArrayList<Transition>();
				ArrayList<Transition> tempTransQ = new ArrayList<Transition>();
				tempTransQ.addAll(this.copyTransQ());
				boolean canAdd = true;
				
				for (Region r : regionList) {
					List<State> subStates = r.getStates(); // Should be size 1
					assert subStates.size() == 1;
					this.transQ.clear();
					if (this.validPath(subStates.get(0))) {
						orthoQ.addAll(this.transQ);
					}
					else {
						canAdd = false;
						break;
					}
				}
				
				this.transQ.clear();
				if (canAdd) {
					this.transQ.addAll(orthoQ);
				}
				this.transQ.addAll(tempTransQ);
				return canAdd;
			}
			else { // Check if it has a history junction				
				for(Region r : s.getRegions()) { // Will be size 1
					if (r.hasHistory()) { // First check and see if it has a history junction
						State lastActive = r.getLastActive();
						if (lastActive == null) { // Get the default transition
							Pseudostate initial = r.getInitial();
							if (initial == null) { // Probably a modeling error; could signal error here
								return false;
							}
							else {
								assert initial.getKind() == Kind.INITIAL;
								Transition initOut = this.getSingleOutgoing(initial);
								if (initOut == null) { // Again, a modeling error
								}
								else {
									if (this.testAndFire(initOut)) {
										if (this.validPath(initOut.getTarget())) {
											transQ.add(initOut);
											return true;
										}
									}
								}
							}
						}
						else { // Last active isn't null; make sure we have a valid path
							// We have 2 options here:
							// (a) Add a boolean parameter to ValidPath indicating if we came from a history
							// (b) Create a stack with both States and Transitions indicating what actions to perform
							return this.validPath(lastActive);
						}
					}
					else { // There was no history junction; try the initial state
						Pseudostate initial = r.getInitial();
						if (initial == null) { // Probably a modeling error
						}
						else {
							Transition initOut = this.getSingleOutgoing(initial);
							if (initOut == null) { // Again, modeling error
							}
							else {
								if (this.testAndFire(initOut)) {
									if (this.validPath(initOut.getTarget())) {
										transQ.add(initOut);
										return true;
									}
								}
							}
							
						}
					}
				}
				
				return false;
			}
		}
		else if (v.isPseudostate()) {
			Pseudostate p = (Pseudostate)v;
			Pseudostate.Kind kind = p.getKind();
			if (kind == Kind.SHALLOWHISTORY || kind == Kind.DEEPHISTORY) {
				Region r = p.getRegionParent();
				
				State lastActive = r.getLastActive();
				if (lastActive == null) { // Get the default transition
					Pseudostate initial = r.getInitial();
					if (initial == null) { // Probably a modeling error; could signal error here
						return false;
					}
					else {
						assert initial.getKind() == Kind.INITIAL;
						Transition initOut = this.getSingleOutgoing(initial);
						if (initOut == null) { // Again, a modeling error
						}
						else {
							if (this.testAndFire(initOut)) {
								if (this.validPath(initOut.getTarget())) {
									transQ.add(initOut);
									return true;
								}
							}
						}
					}
				}
				else { // Last active isn't null; make sure we have a valid path
					return this.validPath(lastActive);
				}
			}
			else if (kind == Kind.JUNCTION) {
				List<Transition> transitions = p.outgoing(); // Already sorted
				if (transitions.isEmpty()) {
					return true; // Added on 5.11.10 
				}
				
				ArrayList<Transition> tempTransQ = new ArrayList<Transition>();
				tempTransQ.addAll(this.copyTransQ());
				this.transQ.clear();
				
				for (Transition t : transitions) {
					if (this.testAndFire(t)) {
						if (this.validPath(t.getTarget())) {
							this.transQ.add(t); // Fixed
							this.transQ.addAll(tempTransQ);
							return true;
						}
						else {
							this.transQ.clear(); // For the last iteration
						}
					}
				}
				
				this.transQ.addAll(tempTransQ);
				return false;
			}
			
			return true;
		}
		
		return false;
	}
}
