package edu.vanderbilt.isis.sm;

import java.util.*;

import edu.vanderbilt.isis.sm.Pseudostate.Kind;
import edu.vanderbilt.isis.sm.properties.*;

public class StateflowInterpreter extends Interpreter {
	protected ArrayDeque<ArrayDeque<Transition>> transitionStack;
	protected ArrayList<State> currentStates;

	public StateflowInterpreter(StateMachine machine, IDataReader reader) {
		super(machine, reader);
		this.transitionStack = new ArrayDeque<ArrayDeque<Transition>>();
		this.currentStates = new ArrayList<State>();
	}

	public StateflowInterpreter(StateMachine machine, IDataReader reader, ILooper looper) {
		super(machine, reader, looper);
		this.transitionStack = new ArrayDeque<ArrayDeque<Transition>>();
		this.currentStates = new ArrayList<State>();
	}

	public StateflowInterpreter(StateMachine machine, IDataReader reader, ILooper looper, List<Pattern> patterns) {
		this(machine, reader, looper);
		this.patterns = patterns;
	}

	protected List<State> getAllActiveStates() {
		ArrayList<State> allActive = new ArrayList<State>();
		for (State s : this.currentStates) {
			for (State curr : s.getPathToRoot()) {
				if (!allActive.contains(curr)) {
					allActive.add(curr);
				}
			}
		}

		return allActive;
	}

	// Returns the active State of Region r, or null if no State in r is active
	protected State getRegionActive(Region r) {
		List<State> allActive = this.getAllActiveStates();
		for (State s : r.getStates()) {
			if (allActive.contains(s)) {
				return s;
			}
		}

		return null;
	}

	// Recursively exits State s starting with the most nested State.
	protected void exitState(State s, ArrayList<State> exitedStates) {
		if (s.isOrthogonal()) {
			List<Region> regions = s.getRegions(); // They are in order now
			for (int i = regions.size() - 1; i >= 0; --i) {
				State active = this.getRegionActive(regions.get(i));
				if (active != null) {
					this.exitState(active, exitedStates);
				}
			}
		}
		else if (s.isComposite()) {
			List<Region> regions = s.getRegions();
			if (!regions.isEmpty()) {
				State active = this.getRegionActive(regions.get(0));
				if (active != null) {
					this.exitState(active, exitedStates);
				}
			}
		}

		exitedStates.add(s);
	}

	// Called after a valid path is calculated.
	// Here, a valid path is one that ends at a State.
	protected void performTransitionPath() {
		assert !this.transitionStack.isEmpty();
		Stack<Transition> transitions = new Stack<Transition>();
		transitions.addAll(this.transitionStack.pop()); // Remove and copy

		// Loop over all transitions and calculate all exited states
		ArrayList<State> exitedStates = new ArrayList<State>();
		for (Transition t : transitions) {
			List<State> sourcePath = t.getSource().getPathToRoot();
			List<State> targetPath = t.getTarget().getPathToRoot();
			boolean addedState = false;

			for (State s: sourcePath) { // Get the exited states between source and target
				if (!targetPath.contains(s)) {
					exitedStates.add(s);
					addedState = true;
				}
				else {
					// We should be able to break here
				}
			}

			if (addedState) {
				addedState = false;
			}
			else {
				break;
			}
		}

		// Check and see if the source and target are the same; if so, exit it and re-enter
		if (exitedStates.isEmpty()) {
			if (!transitions.isEmpty()) {
				Transition t = transitions.peek();
				if (t.getSource().isState()) {
					State s = (State)t.getSource();
					if (s.equals(t.getTarget())) {
						exitedStates.add(s);
					}
				}
			}
		}

		ArrayList<State> transitiveExited = new ArrayList<State>();
		boolean foundParallel = false;
		int loc = -1;
		for (int i = exitedStates.size() - 1; i >= 0; --i) {
			State curr = exitedStates.get(i);
			if(curr.hasParallelSiblings()) { // TODO : Does this need to be an if instead of else if
				// Exit everything in curr's grandparent in reverse order
				List<Region> parallelRegions = curr.getParent().getParent().getRegions();
				for (int j = parallelRegions.size() - 1; j >= 0; --j) {
					ArrayList<State> nextExited = new ArrayList<State>();
					List<State> currSiblings = parallelRegions.get(j).getStates(); // Should be size 1
					if (!currSiblings.isEmpty()) {
						this.exitState(currSiblings.get(0), nextExited);
						transitiveExited.addAll(nextExited);
					}
				}

				foundParallel = true;
				loc = i;
				break;
			}
			else if (curr.isComposite()) {
				this.exitState(curr, transitiveExited);
				foundParallel = true;
				loc = i;
				break; // TODO: may need to remove this and test for parallel siblings
			}
			else {
				transitiveExited.add(curr); // can probably break
			}
		}

		// transitiveExited contains all the States to exit
		for (State s : transitiveExited) {
			s.exitAction();
			this.currentStates.remove(s);
		}

		if (foundParallel) {
			for (++loc; loc < exitedStates.size(); ++loc) { // Exit the remaining states
				State s = exitedStates.get(loc);
				s.exitAction();
				this.currentStates.remove(s);
			}
		}

		// Now for the entry part
		List<State> sourcePath = transitions.firstElement().getSource().getPathToRoot();
		List<State> targetPath = transitions.lastElement().getTarget().getPathToRoot();
		Stack<State> enteredStates = new Stack<State>();

		for (State s : targetPath) {
			if (!sourcePath.contains(s)) {
				enteredStates.push(s);
			}
			else {
				break;
			}
		}

		// If entered states is empty, the source and target should be the same
		if (enteredStates.isEmpty()) {
			if (!transitions.isEmpty()) {
				Transition t = transitions.peek();
				if (t.getSource().isState()) {
					State s = (State)t.getSource();
					if (s.equals(t.getTarget())) {
						enteredStates.add(s);
					}
				}
			}
		}

		boolean didDefault = false;
		int enteredSize = enteredStates.size();
		for (int i = 0; i < enteredSize; ++i) {
			State s = enteredStates.get(i);
			if (s.hasParallelSiblings()) {
				// Don't do the entry action; it will be done in order
				List<Region> parallelRegions = s.getParent().getParent().getRegions();
				for (Region r : parallelRegions) {
					List<State> parallelStates = r.getStates(); // TODO: Could test default transitions
					if (!parallelStates.isEmpty()) {
						this.doDefaultEntry(parallelStates.get(0), enteredStates);
					}
				}

				didDefault = true;
				break;
			}
			else if (s.isOrthogonal()) {
				s.entryAction(); // TODO: could add to active state list if we decide to keep everything
				List<Region> regions = s.getRegions();
				for (Region r : regions) {
					List<State> parallelStates = r.getStates();
					if (!parallelStates.isEmpty()) {
						this.doDefaultEntry(parallelStates.get(0), enteredStates);
					}
				}

				didDefault = true;
				break;
			}
			else if (s.isComposite()) {
				if (i < enteredSize - 1) {
					s.entryAction(); // Otherwise it will be entered twice
				}
			}
			else if (s.isSimple()){
				this.currentStates.add(s);
				s.entryAction();
			}
		}

		// The final state won't be fully entered above if it's composite, so do it here
		if (!didDefault) {
			State targetState = (State)transitions.lastElement().getTarget(); // final target is always a State
			if (targetState.isComposite()) {
				this.doDefaultEntry(targetState, new Stack<State>());
			}
		}

		// Do the transition actions; this isn't entirely correct, because the default paths can add
		// several transitions whose actions also need to be performed.  We'll fix this by adding a stack
		// of vectors of transitions and adding all executed transitions to it.
		for (Transition t : transitions) {
			t.action(this);
		}
	}

	// Assumes we want to enter s and recursively enter its children
	protected void doDefaultEntry(State s, List<State> enteredStates) {
		if (s.isSimple()) {
			this.currentStates.add(s);
			s.entryAction();
		}
		else if (s.isOrthogonal()) {
			// May need to add it to the current state list and store all States instead of only simple states
			s.entryAction();
			List<Region> regions = s.getRegions();
			for (Region r : regions) {
				List<State> parallelStates = r.getStates();
				if (!parallelStates.isEmpty()) {
					this.doDefaultEntry(parallelStates.get(0), enteredStates); // Should be size 1
				}
			}
		}
		else if (s.isComposite()) {
			s.entryAction();
			for (Region r: s.getRegions()) { // Should be size 1
				// If r has a state that is in the path of entered states, do that one
				boolean foundState = false;
				for (State possibleDefault : r.getStates()) {
					// If possibleDefault is in the list of entered states, do it
					if (enteredStates.contains(possibleDefault)) {
						this.doDefaultEntry(possibleDefault, enteredStates);
						foundState = true;
						break;
					}
				}

				// TODO: Check if r has a history pseudostate

				// Otherwise find the initial state starting from the default transition
				if (!foundState) {
					Pseudostate p = r.getInitial();
					if (p != null) { // TODO: Signal error if no initial pseudostate
						this.openTransitionScope();
						State initialState = this.findDefault(p);
						ArrayDeque<Transition> defaultTransitions = this.transitionStack.pop();
						if (s == null) {
							// Error!
						}
						else {
							// TODO: We could optimize and use a method that doesn't check for a possible default
							this.doDefaultEntry(initialState, new Stack<State>());
						}
					}
				}
			}
		}
	}

	protected State findDefault(Pseudostate p) {
		for (Transition t : p.outgoing()) {
			if (this.testAndFire(t)) {
				IVertex target = t.getTarget();
				if (target.isState()) {
					this.pushTransition(t);
					return (State)target;
				}
				else { // It's a junction
					State s = this.findDefault((Pseudostate)target);
					if (s != null) {
						this.pushTransition(t);
						return s;
					}
				}
			}
		}

		return null;
	}

	protected boolean isValidPath(Transition t) {
		IVertex v = t.getTarget();
		if (v.isState()) {
			return true;
		}
		else {
			Pseudostate p = (Pseudostate) v;
			Pseudostate.Kind kind = p.getKind();
			if (kind == Kind.JUNCTION) {
				List<Transition> transitions = p.outgoing();
				if (transitions.isEmpty()) {
					return true; // Done if no outgoing transitions
				}
				else {
					//boolean foundPath = false;
					//this.openTransitionScope(); // A temporary stack
					for (Transition currTrans : transitions) {
						//this.clearTopTransitions();
						if (this.testAndFire(currTrans)) {
							if (this.isValidPath(currTrans)) {
								//foundPath = true;
								this.pushTransition(currTrans);
								return true;
								//break;
							}
						}
					}
					//if (foundPath) { // Copy top to next and pop
					//	this.copyJunctionTransitions();
					//}
					//else {
					//	this.closeTransitionScope();
					//}
				}
			}
		}

		return false;
	}

	protected boolean calculatePath(State s) {
		boolean foundPath = false;
		this.openTransitionScope();
		for (Transition t : s.outgoing()) {
			if (this.testAndFire(t)) {
				if (this.isValidPath(t)) {
					this.pushTransition(t);
					foundPath = true;
					break;
				}
			}
		}

		if (foundPath) {
			// Check the last transition: if its target is a junction, we don't exit
			if (this.pathEndsWithJunction()) {
				this.closeTransitionScope(); // Don't exit anything
			}
			else {
				this.performTransitionPath();
			}
		}
		else {
			this.closeTransitionScope();
		}

		return foundPath;
	}

	public void step() {
		while (this.events.size() > 0) {
			this.currEvent = this.events.pop();
			assert this.currEvent != null;

			if (this.machine.isJunctionMachine() || this.getAllActiveStates().isEmpty()) {
				List<Pseudostate> initial = this.machine.getInitial();
				for (Pseudostate p : initial) {
					boolean foundPath = false;
					this.openTransitionScope();
					Transition t = this.getSingleOutgoing(p);
					if ((t != null) && (this.testAndFire(t))) {
						if (foundPath = this.isValidPath(t)) {
							this.pushTransition(t);
						}
					}

					if (foundPath) {
						if (this.pathEndsWithJunction()) {
							this.closeTransitionScope();
						}
						else {
							this.performTransitionPath();
						}
					}
					else {
						this.closeTransitionScope();
					}
				}
			}
			else {
				for (Region r: this.machine.getRegions()) {
					this.tryExitRegion(r);
				}
			}

			this.printConfiguration();
		}
	}

	public void printConfiguration() {
		System.out.println("\n----Current State Set------------------");
		for (State s : this.currentStates) {
			System.out.println("State: " + s.getName());
			if((s.getName()).compareTo("WaitForCancel")==0)
				assert(false);
		}

		System.out.println("----Enabled Event Set------------------");
		for (Event e : this.getEnabled()) {
			System.out.println(e.name());
		}
		System.out.println("----Output data values-----------------");
		this.machine.getTopLevelData();
		System.out.println("---------------------------------------");
	}

	public List<Event> getEnabled() {
		ArrayList<Event> enabled = new ArrayList<Event>();
		for (State s : this.currentStates) {
			List<State> rootPath = s.getPathToRoot();
			for (State t : rootPath) {
				List<Transition> trans = t.outgoing();
				for (Transition tran : trans) {
					List<Event> events = tran.getTrigger();
					for (Event trig : events) {
						if (trig != null) {
							boolean canAdd = true;
							for (Event currEnabled : enabled) {
								if (currEnabled.name() == trig.name()) {
									canAdd = false;
									break;
								}
							}
							if (canAdd) {
								enabled.add(trig);
							}
						}
					}
				}
			}
		}

		return enabled;
	}

	public void initialize() {
		assert this.machine != null;
		this.events.push(new Event(""));
		this.step();
	}

	protected boolean tryExitRegion(Region r) {
		State s = this.getRegionActive(r);
		if (s != null) {
			s.doAction(); // 7-23-2010 -- added this to perform during action
			if (this.calculatePath(s)) {
				return true;
			}
			else {
				for (Region nextRegion : s.getRegions()) {
					this.tryExitRegion(nextRegion);
				}
			}
		}

		return false;
	}

	// Returns true if the last transition on the current stack has a junction target
	protected boolean pathEndsWithJunction() {
		assert !this.transitionStack.isEmpty();
		ArrayDeque<Transition> transitions = this.transitionStack.peek();
		if (transitions.isEmpty()) {
			return false;
		}
		else {
			Transition t = transitions.peekLast();
			if (t.getTarget().isPseudostate()) {
				if (((Pseudostate)t.getTarget()).getKind() == Kind.JUNCTION) {
					return true;
				}
			}
		}

		return false;
	}

	// Executes all of the transition actions at the top of the stack and pops stack
	protected void executeTransitionActions() {
		assert !this.transitionStack.isEmpty();
		Stack<Transition> transitions = new Stack<Transition>();
		transitions.addAll(this.transitionStack.pop()); // Remove and copy
		for (Transition t : transitions) {
			t.action(this); // Do we execute these unconditionally?
		}
	}

	protected void openTransitionScope() {
		this.transitionStack.push(new ArrayDeque<Transition>());
	}

	protected void closeTransitionScope() {
		assert !this.transitionStack.isEmpty();
		this.transitionStack.pop();
	}

	protected void pushTransition(Transition t) {
		assert !this.transitionStack.isEmpty();
		this.transitionStack.peek().push(t);
	}

	protected boolean testAndFire(Transition t) {
		if (t.canFire(this.currEvent)) {
			t.conditionAction(this);
			// We could ensure that the condition action didn't force an exit from t.source
			return true;
		}
		else {
			return false;
		}
	}

	public boolean inState(State s) {
		ArrayList<State> allStates = new ArrayList<State>();
		for (State curr : this.currentStates) {
			allStates.addAll(curr.getPathToRoot());
		}

		if (allStates.contains(s)) {
			return true;
		}

		return false;
	}

	// Send the event e only to State s
	protected void sendToState(State s, Event e) {
	}

	@Override
	public void checkProperties() throws PropertyException {
		for (Pattern p : this.patterns) {
			if (p.checkProperty(this)) {
				throw new PropertyException();
			}

		}
	}
}