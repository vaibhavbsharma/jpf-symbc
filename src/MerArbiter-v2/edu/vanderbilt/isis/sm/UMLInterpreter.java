package edu.vanderbilt.isis.sm;
import java.util.*;

import edu.vanderbilt.isis.sm.Pseudostate.Kind;

public class UMLInterpreter extends Interpreter {

	public UMLInterpreter(StateMachine machine, IDataReader reader) {
		super(machine, reader);
	}

	public UMLInterpreter(StateMachine machine, IDataReader reader, ILooper looper) {
		super(machine, reader, looper);
	}

	public void step() {


		while (this.events.size() > 0) {
			this.currEvent = this.events.pop();
			assert this.currEvent != null;

			ArrayList<ArrayList<Transition>> allPaths = new ArrayList<ArrayList<Transition>>();
			ArrayList<Region> regionQ = new ArrayList<Region>();
			this.oldStates.clear();
			this.nextStates.clear();

			// See if there is a direct path out of each state in current configuration
			for (State s : this.currStates) {
				this.transQ.clear();
				if (this.calculatePath(s)) {
					allPaths.add(this.copyAndReverseTransQ());
				}
				else {
					regionQ.add(s.getParent());
				}
			}

			// Traverse the hierarchy from bottom to top (if necessary)
			boolean changed = true;
			ArrayList<Region> pendingRegions = new ArrayList<Region>();
			ArrayList<Region> removingRegions = new ArrayList<Region>();
			while (changed) {
				changed = false;
				for (Region r : regionQ) {
					if (r.isTopLevelRegion()) {
						continue;
					}

					if (!regionQ.containsAll(r.getOrthogonal())) {
						continue;
					}

					this.transQ.clear();
					if (this.calculatePath((State)r.getParent())) {
						allPaths.add(this.copyAndReverseTransQ());
					}
					else {
						Region gParent = r.getParent().getRegionParent();
						if (gParent != null) {
							changed = true;
							removingRegions.add(r);
							pendingRegions.add(gParent);
						}
					}
				}

				if (changed) {
					regionQ.removeAll(removingRegions);
					regionQ.addAll(pendingRegions);
					pendingRegions.clear();
				}
			}

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

	private boolean calculatePath(State s) {
		Collections.sort(s.outgoing());  // Random choice if multiple transitions have equal priority
		for (Transition t : s.outgoing()) {
			if (t.canFire(currEvent)) {  // If current event satisfies trigger and guard is satisfied
				if (this.validPath(t.getTarget())) {
					this.transQ.add(t);
					return true;
				}
			}
		}
		return false;
	}

	private boolean validPath(IVertex v) {
		if (v.isState()) {
			State s = (State) v;
			if (s.isSimple()) {
				return true;
			}
			else if(s.isFinal()) {
				return true;
			}
			else {
				for(Region r : s.getRegions()) {
					if (!this.validPath(r.getInitial())) {
						return false;
					}
				}

				return true;
			}
		}
		else if(v.isPseudostate()) {
			Pseudostate p = (Pseudostate) v;

			switch(p.getKind()) {
				case CHOICE:
					return true;

				case INITIAL:
					if (this.validPath(this.getSingleTarget(p))) {
						this.transQ.add(this.getSingleOutgoing(p));
						return true;
					}
					else
						return false;

				case ENTRYPOINT:
					return this.validPath(this.getSingleTarget(p));

				case EXITPOINT:
					return this.validPath(this.getSingleTarget(p));

				case JOIN:
					if (this.checkJoinCondition(p)) {
					}
					else {
						return false;
					}

				case JUNCTION:
					for (Transition t : p.outgoing()) {
						if (t.canFire(this.currEvent) && this.validPath(t.getTarget())) {
							this.transQ.add(t);
							return true;
						}
					}
					return false;

				case TERMINATE:
					return true;

				case FORK:
					ArrayList<Transition> forkQ = new ArrayList<Transition>();
					ArrayList<Transition> tempTransQ = new ArrayList<Transition>();
					tempTransQ.addAll(this.copyTransQ());
					boolean canAdd = true;

					for (Transition t : p.outgoing()) {
						this.transQ.clear();
						if (t.canFire(this.currEvent) && this.validPath(t.getTarget())) {
							this.transQ.add(t);
							forkQ.addAll(this.transQ);
						}
						else {
							canAdd = false;
							break;
						}
					}

					this.transQ.clear();
					if (canAdd) {
						this.transQ.addAll(forkQ);
					}
					this.transQ.addAll(tempTransQ);
					return canAdd;

				case SHALLOWHISTORY:
					State lastActive = p.getRegionLastActive();
					if (lastActive == null || lastActive.isFinal()) { // Section 15.3.11, Page 555
						if (this.validPath(this.getSingleTarget(p))) {
							this.transQ.add(this.getSingleOutgoing(p));
							return true;
						}
						else {
							return false;
						}
					}
					else {
						if (lastActive.isComposite()) {
							ArrayList<Transition> historyQ = new ArrayList<Transition>();
							ArrayList<Transition> tempTransitionQ = new ArrayList<Transition>();
							tempTransitionQ.addAll(this.copyTransQ());
							boolean canAddHistory = true;
							for (Region r : lastActive.getRegions()) {
								Pseudostate init = r.getInitial();
								if (init != null) {
									this.transQ.clear();
									if (this.validPath(this.getSingleTarget(init))) {
										this.transQ.add(this.getSingleOutgoing(init));
										historyQ.addAll(this.transQ);
									}
									else {
										canAddHistory = false;
										break;
									}
								}
								else {
									canAddHistory = false;
									break;
								}
							}

							this.transQ.clear();
							if (canAddHistory) {
								this.transQ.addAll(historyQ);
							}

							this.transQ.addAll(tempTransitionQ);
							return canAddHistory;
						}

						return true;
					}

				case DEEPHISTORY:
					State lastActiveD = p.getRegionLastActive();
					if (lastActiveD == null || lastActiveD.isFinal()) { // Section 15.3.11, Page 555
						if (this.validPath(this.getSingleTarget(p))) {
							this.transQ.add(this.getSingleOutgoing(p));
							return true;
						}
						else {
							return false;
						}
					}
					else {
						return true;
					}

				default:
					return false;
			}
		}

		return true;
	}

	// Check if the current state configuration contains all the sources of p
	private boolean checkJoinCondition(Pseudostate p) {
		assert p.getKind() == Kind.JOIN;

		List<Transition> incoming = p.incoming();
		for (Transition t : incoming) {
			IVertex source = t.getSource();
			if (source.isState()) {
				if (!this.currStates.contains((State) source)) {
					return false;
				}
			}
		}
		return true;
	}

	private void processDeepHistory(State s) {
		s.entryAction();
		if (s.isSimple()) {
			this.nextStates.add(s);
		}
		else if (s.isComposite()) {
			for (Region r : s.getRegions()) {
				State nextDeepHistory = r.getLastActive();
				assert nextDeepHistory != null;
				this.processDeepHistory(nextDeepHistory);
			}
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

			t.action(this);
			t.conditionAction(this); // Added on 6-4-2010
			this.performExitActions(exited);
			if (target.isState()) {
				// set its region parent's history to this state
				State s = (State)target;
				if (s.isSimple()) {
					this.performEntryActions(entered);
					this.nextStates.add(s);
				}
				else if(s.isComposite()) {
					// We need to skip the last state's entry action
					// It will be performed by being the parent of the source of an initial state
					this.performEntryActions(entered.subList(0, entered.size() - 1));
				}
			}
			else if (target.isPseudostate()) {
				Pseudostate p = (Pseudostate) target;
				Pseudostate.Kind kind = p.getKind();

				if (kind == Kind.INITIAL) {
					this.performEntryActions(entered.subList(0, entered.size() - 1));
				}
				else {
					this.performEntryActions(entered);
					if (kind == Kind.CHOICE) {
						for (Transition q : p.outgoing()) {
							this.transQ.clear();
							if (q.canFire(this.currEvent) && this.validPath(q.getTarget())) {
								this.transQ.add(q);
								this.processTransitions(this.copyAndReverseTransQ());
								break;
							}
							// Error if we reach this point -- choice should have a default transition
						}
					}
					else if (kind == Kind.DEEPHISTORY) {
						State lastDeep = p.getRegionLastActive();
						if (lastDeep != null) {
							this.processDeepHistory(lastDeep);
						}
					}
					else if (kind == Kind.SHALLOWHISTORY) {
						State lastShallow = p.getRegionLastActive();
						if (lastShallow != null) {
							lastShallow.entryAction();
							// No transition here, but do the entry action of the most recent state
							// Everything else will be taken care of by initial
						}
					}
				}
			}
		}
	}

	public void initialize() {
		assert this.machine != null;

		this.currStates.clear();
		ArrayList<ArrayList<Transition>> allPaths = new ArrayList<ArrayList<Transition>>();
		for (Pseudostate p : this.machine.getInitial()) {
			List<Transition> trans = p.outgoing();
			assert trans.size() == 1;

			Transition t = trans.get(0);
			if (this.validPath(t.getTarget())) {
				this.transQ.add(t);
				allPaths.add(this.copyAndReverseTransQ());
			}
		}

		for (ArrayList<Transition> l : allPaths) {
			this.processTransitions(l);
		}

		this.currStates.addAll(this.nextStates);
		this.printConfiguration();
	}

	public void printConfiguration() {
		System.out.println("\n----Current State Set------------------");
		for (State s : this.currStates) {
			System.out.println("State: " + s.getName());
		}

		System.out.println("----Enabled Event Set------------------");
		for (Event e : this.getEnabled()) {
			System.out.println(e.name());
		}
		System.out.println("---------------------------------------");
	}

}
