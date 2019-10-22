package edu.vanderbilt.isis.sm;
import java.util.*;

/**
 * Transition is the base class for all transitions in a StateMachine.
 * The source and target of a Transition are either a State or PseudoState.
 * Each Transition much also belong to a Region, which is its parent.
 * A Transition has a List of triggering Events.  If this list is empty,
 * then by default the Transition can always fire.
 * The optional priority can be compared to other Transitions originating
 * from the same source.
 * If custom behavior for actions, guards or triggers is needed, subclass
 * Transition and overwrite the action, guard, or trigger methods,
 * respectively.
 * Events can be added to the Interpreter's event queue by using the addEvent
 * method of the IEventHolder that is passed as a parameter to the action
 * method. 
 * 
 * @author Daniel Balasubramanian
 *
 */

public class Transition implements Comparable<Transition> {
	private IVertex source, target;
	private Region parent;
	private ArrayList<Event> trigger;
	private int priority;  // lower number is higher priority!
	
	public Transition(IVertex source, IVertex target, Region parent, Event trigger) {
		this.source = source;
		this.target = target;
		this.parent = parent;
		this.trigger = new ArrayList<Event>();
		
		this.trigger.add(trigger);
		this.source.addOutgoing(this);
		this.target.addIncoming(this);
		this.parent.addTransitionChild(this);
	}
	
	public Transition(IVertex source, IVertex target, Region parent, List<Event> trigger) {
		this.source = source;
		this.target = target;
		this.parent = parent;
		this.trigger = new ArrayList<Event>();
		
		this.trigger.addAll(trigger);
		this.source.addOutgoing(this);
		this.target.addIncoming(this);
		this.parent.addTransitionChild(this);
	}
	
	public Transition(IVertex source, IVertex target, Region parent, Event trigger, int priority) {
		this.source = source;
		this.target = target;
		this.parent = parent;
		this.trigger = new ArrayList<Event>();
		this.priority = priority;
		
		this.trigger.add(trigger);
		this.source.addOutgoing(this);
		this.target.addIncoming(this);
		this.parent.addTransitionChild(this);
	}
	
	public Transition(IVertex source, IVertex target, Region parent, List<Event> trigger, int priority) {
		this.source = source;
		this.target = target;
		this.parent = parent;
		this.trigger = new ArrayList<Event>();
		this.priority = priority;
		
		this.trigger.addAll(trigger);
		this.source.addOutgoing(this);
		this.target.addIncoming(this);
		this.parent.addTransitionChild(this);
	}
	
	public Transition(Region parent, List<Event> trigger, int priority) {
		this.parent = parent;
		this.priority = priority;
		this.trigger = new ArrayList<Event>();
		this.trigger.addAll(trigger);
	}
	
	public void initialize(IVertex source, IVertex target) {
		this.source = source;
		this.target = target;
		this.source.addOutgoing(this);
		this.target.addIncoming(this);
	}
	
	public int compareTo(Transition t) {
		if (this.getPriority() < t.getPriority()) {
			return -1;
		}
		else if (this.getPriority() > t.getPriority()) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public final int getPriority() {
		return this.priority;
	}
	
	public final List<Event> getTrigger() {
		return this.trigger;
	}
	
	public final IVertex getSource() {
		return this.source;
	}
	
	public final IVertex getTarget() {
		return this.target;
	}
	
	public final boolean canFire(Event e) {
		return this.trigger(e) && this.guard();
	}

	// Overwrite these for custom behavior
	public boolean guard() {
		return true;
	}
	
	public final boolean trigger(Event e) {
		if (this.trigger.isEmpty()) {
			return true;
		}
		else {
			String s = e.name();
			for (Event event : this.trigger) {
				String otherName = event.name();
				if (s.equals(otherName) || otherName.isEmpty()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void action(IEventHolder e) {
	}
	
	// 4-5-10: Added for Stateflow semantics
	public void conditionAction(IEventHolder e) {
	}
}
