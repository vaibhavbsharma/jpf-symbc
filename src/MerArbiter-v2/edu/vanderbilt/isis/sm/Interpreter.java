package edu.vanderbilt.isis.sm;

import java.util.*;
import edu.vanderbilt.isis.sm.properties.*;

import edu.vanderbilt.isis.sm.Pseudostate.Kind;

public class Interpreter implements IEventHolder {

	protected StateMachine machine;
	protected ArrayList<State> currStates, oldStates, nextStates;
	protected Vector<Transition> transQ;
	protected LinkedList<Event> events;
	protected Event currEvent;
	protected IDataReader reader;
	protected ILooper looper;
	protected List<Pattern> patterns;

	public Interpreter(StateMachine machine, IDataReader reader) {
		this.machine = machine;
		this.currStates = new ArrayList<State>();
		this.oldStates = new ArrayList<State>();
		this.nextStates = new ArrayList<State>();
		this.transQ = new Vector<Transition>();
		this.events = new LinkedList<Event>();
		this.currEvent = null;
		this.reader = reader;
		this.looper = new DefaultLooper();
		this.patterns = new ArrayList<Pattern>();
	}
	
	public Interpreter(StateMachine machine, IDataReader reader, ILooper looper) {
		this(machine, reader);
		this.looper = looper;
	}

	public ILooper getLooper() {
		return looper;
	}
	
	public void addEvent(Event e) {
		this.events.add(e);
	}

	public List<Event> getEnabled() {
		ArrayList<Event> enabled = new ArrayList<Event>();
		for (State s : this.currStates) {
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
	}

	// Returns a List of all the States contained between this state and
	// any active current states.
	protected List<State> getTransitiveExited(State exited) {
		ArrayList<State> rawStates = new ArrayList<State>();
		Stack<State> states = new Stack<State>();
		for (State s : this.currStates) { // Bottom-up
			List<State> tempPath = s.getPathToRoot();
			if (tempPath.contains(exited)) {
				rawStates.addAll(tempPath);
			}
		}

		Vector<Region> regionQ = new Vector<Region>();
		regionQ.addAll(exited.getRegions());
		while (!regionQ.isEmpty()) { // Top-down
			Region r = regionQ.remove(0);
			for (State s : r.getStates()) {
				if (rawStates.contains(s)) {
					states.push(s);
					if (s.isComposite()) {
						regionQ.addAll(s.getRegions());
					}

					break; // At most one active State in a single Region
				}
			}
		}

		return states;
	}

	protected void performEntryActions(List<State> enteredStates) {
		for (State s : enteredStates) {
			s.setLastActive();
			s.entryAction();
		}
	}

	protected void performExitActions(List<State> exitedStates) {
		for (State s : exitedStates) {
			s.setLastActive();
			s.exitAction();
			if (s.isSimple()) {
				this.oldStates.add(s);
			}
		}
	}

	// TODO: May need to add a special case for EXITSTATE
	protected List<State> exitedStates(Transition t) {
		ArrayList<State> exited = new ArrayList<State>();

		// Check if we are exiting a composite state
		if (t.getSource().isState()) {
			State compTest = (State) t.getSource();
			if (!compTest.isSimple()) {
				for (Region r : compTest.getRegions()) {
					exited.addAll(this.getTransitiveExited(compTest));
				}
			}
		}

		List<State> sourcePath = t.getSource().getPathToRoot();
		List<State> destPath = t.getTarget().getPathToRoot();
		for (State s : sourcePath) {
			if (!destPath.contains(s)) {
				exited.add(s);
			} else {
				break;
			}
		}

		return exited;
	}

	protected List<State> enteredStates(Transition t) {
		List<State> sourcePath = t.getSource().getPathToRoot();
		List<State> destPath = t.getTarget().getPathToRoot();
		Stack<State> entered = new Stack<State>();
		for (State s : destPath) {
			if (!sourcePath.contains(s)) {
				entered.push(s);
			} else {
				break;
			}
		}

		return entered;
	}

	protected ArrayList<Transition> copyAndReverseTransQ() {
		ArrayList<Transition> temp = new ArrayList<Transition>();
		for (int i = this.transQ.size() - 1; i >= 0; i--) {
			temp.add(this.transQ.get(i));
		}

		return temp;
	}

	protected ArrayList<Transition> copyTransQ() {
		ArrayList<Transition> temp = new ArrayList<Transition>();
		temp.addAll(this.transQ);
		return temp;
	}

	public void step() {
	}

	protected Transition getSingleOutgoing(Pseudostate p) {
		Pseudostate.Kind kind = p.getKind();
		if (kind == Kind.INITIAL || kind == Kind.ENTRYPOINT
				|| kind == Kind.JOIN || kind == Kind.SHALLOWHISTORY
				|| kind == Kind.DEEPHISTORY) {
			List<Transition> outgoing = p.outgoing();
			assert outgoing.size() == 1;
			return outgoing.get(0);
		}

		return null;
	}

	protected IVertex getSingleTarget(Pseudostate p) {
		Pseudostate.Kind kind = p.getKind();
		assert (kind == Kind.SHALLOWHISTORY || kind == Kind.DEEPHISTORY
				|| kind == Kind.EXITPOINT || kind == Kind.ENTRYPOINT
				|| kind == Kind.INITIAL || kind == Kind.JOIN);
		List<Transition> outgoing = p.outgoing();
		assert outgoing.size() == 1;
		return outgoing.get(0).getTarget();
	}

	public void dataAndEventLoop() {
		looper.doDataAndEventLoop();
	}

	public void eventLoop() {
		looper.doEventLoop();
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
		System.out.println("----Output data values-----------------");
		this.machine.getTopLevelData();


	}
	
	public boolean inState(State s) {
		return false;
	}
	
	/* Virtual method to be overwritten in various interpreters */
	public void checkProperties() throws PropertyException {
	}
}