package MerArbiter;

import java.util.*;
import java.io.*;
import edu.vanderbilt.isis.sm.*;
import edu.vanderbilt.isis.sm.Pseudostate.Kind;
import edu.vanderbilt.isis.sm.properties.*;
import com.martiansoftware.jsap.*;

public class TopLevelUser1 implements Serializable {
	public StateMachine sm = new StateMachine();
	public Interpreter sim;

	public TopLevelUser1() {
	}

	public static void main(String[] args) throws Exception {
		TopLevelUser1 example = new TopLevelUser1();
		FlaggedOption semantics = new FlaggedOption("semantics")
				.setStringParser(JSAP.STRING_PARSER).setDefault("SF")
				.setRequired(false).setShortFlag('s').setLongFlag("semantics");
		FlaggedOption dataProviderArg = new FlaggedOption("dataProvider")
				.setStringParser(JSAP.STRING_PARSER).setDefault("CommandLine")
				.setRequired(false).setShortFlag('d').setLongFlag(
						"dataProvider");
		FlaggedOption maxSteps = new FlaggedOption("maxSteps").setStringParser(
				JSAP.INTEGER_PARSER).setDefault("10").setRequired(false)
				.setShortFlag(JSAP.NO_SHORTFLAG).setLongFlag("maxSteps");
		FlaggedOption csvFile = new FlaggedOption("csvFilePath")
				.setStringParser(JSAP.STRING_PARSER).setDefault("")
				.setRequired(false).setShortFlag('c').setLongFlag("csvFile");
		Switch checkOutputSw = new Switch("checkOutput").setShortFlag(
				JSAP.NO_SHORTFLAG).setLongFlag("checkOutput");
		FlaggedOption looperArg = new FlaggedOption("looper").setStringParser(
				JSAP.STRING_PARSER).setDefault("Default").setRequired(false)
				.setShortFlag('l').setLongFlag("looper");
		Switch checkPropertiesSw = new Switch("checkProperties").setShortFlag(
				JSAP.NO_SHORTFLAG).setLongFlag("checkProperties");

		JSAP jsap = new JSAP();
		jsap.registerParameter(semantics);
		jsap.registerParameter(dataProviderArg);
		jsap.registerParameter(csvFile);
		jsap.registerParameter(checkOutputSw);
		jsap.registerParameter(maxSteps);
		jsap.registerParameter(looperArg);
		jsap.registerParameter(checkPropertiesSw);
		JSAPResult config = jsap.parse(args);

		IDataProvider dataProvider = new CommandLineDataProvider();
		IDataPrinter dataPrinter = null;
		ILooper looper = new DefaultLooper();
		List<Pattern> patterns = new ArrayList<Pattern>();

		if (config.getString("dataProvider").toUpperCase().equals("CSV")) {
			String fileName = config.getString("csvFilePath");
			boolean checkOutput = config.getBoolean("checkOutput");
			CSVDataProvider csvDP = new CSVDataProvider(fileName, checkOutput);
			dataProvider = csvDP;
			if (checkOutput) {
				dataPrinter = csvDP;
			}
		}
		if (config.getString("dataProvider").toUpperCase().equals("SYMBOLIC")) {
			int steps = config.getInt("maxSteps");
			SymbolicDataProvider sDP = new SymbolicDataProvider(steps);
			dataProvider = sDP;
		}

		if (config.getString("looper").toUpperCase().equals("NONDETERMINISTIC")) {
			looper = new NondeterministicLooper();
		}

		if (config.getBoolean("checkProperties")) {
			patterns = example.makeProperties(example.r1.User122);
		}

		IDataReader dataReader = new User1Reader(example.r1.User122,
				dataProvider, dataPrinter);
		if (config.getString("semantics").toUpperCase().equals("UML")) {
			example.sim = new UMLInterpreter(example.sm, dataReader, looper);
		} else {
			example.sim = new StateflowInterpreter(example.sm, dataReader,
					looper, patterns);
		}
		example.sim.getLooper().setInterpreter(example.sim);
		example.initializeTransitions();
		example.DoSim();
	}

	public void DoSim() {
		this.sim.initialize();
		this.sim.dataAndEventLoop();
	}

	public List<Pattern> makeProperties(REGION_T.STATE_T s) {
		ArrayList<Pattern> patterns = new ArrayList<Pattern>();
		return patterns;
	}

	public class REGION_T extends Region {
		public REGION_T(IRegionParent parent) {
			super(parent);
		}

		public Pseudostate p250 = new Pseudostate(this, Kind.INITIAL);

		public class STATE_T extends State {
			public int resourceIn;
			public int resourceOut;
			public boolean request;
			public boolean grant;
			public boolean cancel;
			public boolean deny;
			public boolean rescind;
			public boolean reset;

			public void setData(int val_0, boolean val_1, boolean val_2,
					boolean val_3, boolean val_4) {
				this.resourceIn = val_0;
				this.grant = val_1;
				this.deny = val_2;
				this.rescind = val_3;
				this.reset = val_4;
			}

			public STATE_T(String name, Region parent, boolean isFinal) {
				super(name, parent, isFinal);
			}

			public class RegionR41 extends Region {
				public RegionR41(IRegionParent parent) {
					super(parent);
				}

				public Pseudostate p36 = new Pseudostate(this, Kind.INITIAL);

				public class StateIdle52 extends State {
					public StateIdle52(String name, Region parent,
							boolean isFinal) {
						super(name, parent, isFinal);
					}
				}

				public StateIdle52 Idle23 = new StateIdle52("Idle1", this, false);

				public class StateBusy53 extends State {
					public StateBusy53(String name, Region parent,
							boolean isFinal) {
						super(name, parent, isFinal);
					}

					public class RegionR40 extends Region {
						public RegionR40(IRegionParent parent) {
							super(parent);
						}

						public Pseudostate p37 = new Pseudostate(this,
								Kind.INITIAL);

						public class StateInit54 extends State {
							public StateInit54(String name, Region parent,
									boolean isFinal) {
								super(name, parent, isFinal);
							}
						}

						public StateInit54 Init25 = new StateInit54("Init1",
								this, false);

						public class StatePending55 extends State {
							public StatePending55(String name, Region parent,
									boolean isFinal) {
								super(name, parent, isFinal);
							}
						}

						public StatePending55 Pending26 = new StatePending55(
								"Pending1", this, false);

						public class StateGranted56 extends State {
							public StateGranted56(String name, Region parent,
									boolean isFinal) {
								super(name, parent, isFinal);
							}
						}

						public StateGranted56 Granted27 = new StateGranted56(
								"Granted1", this, false);

						public class Transition253 extends Transition {
							public Transition253(IVertex source,
									IVertex target, Region parent,
									List<Event> triggers, int priority) {
								super(source, target, parent, triggers,
										priority);
							}

							public boolean guard() {
								return rescind == ((1) != 0);
							}

							public void action(IEventHolder e) {
								;
							}

							public void conditionAction(IEventHolder e) {
								cancel = 1 != 0;
								;
							}
						}

						ArrayList<Event> triggers254 = new ArrayList<Event>() {
							{
								add(new Event(""));
							}
						};
						Transition253 p252 = new Transition253(Granted27,
								Init25, this, triggers254, 1);

						public class Transition256 extends Transition {
							public Transition256(IVertex source,
									IVertex target, Region parent,
									List<Event> triggers, int priority) {
								super(source, target, parent, triggers,
										priority);
							}

							public boolean guard() {
								return grant == ((1) != 0);
							}

							public void action(IEventHolder e) {
								assert(r1.User122.reset==false);

							}

							public void conditionAction(IEventHolder e) {
								;
							}
						}

						ArrayList<Event> triggers257 = new ArrayList<Event>() {
							{
								add(new Event(""));
							}
						};
						Transition256 p255 = new Transition256(Pending26,
								Granted27, this, triggers257, 2);

						public class Transition259 extends Transition {
							public Transition259(IVertex source,
									IVertex target, Region parent,
									List<Event> triggers, int priority) {
								super(source, target, parent, triggers,
										priority);
							}

							public void action(IEventHolder e) {
								;
							}

							public void conditionAction(IEventHolder e) {
								;
							}
						}

						ArrayList<Event> triggers260 = new ArrayList<Event>() {
							{
								add(new Event(""));
							}
						};
						Transition259 p258 = new Transition259(p37, Init25,
								this, triggers260, 1);

						public class Transition262 extends Transition {
							public Transition262(IVertex source,
									IVertex target, Region parent,
									List<Event> triggers, int priority) {
								super(source, target, parent, triggers,
										priority);
							}

							public void action(IEventHolder e) {
								;
							}

							public void conditionAction(IEventHolder e) {
								request = 1 != 0;
								;
							}
						}

						ArrayList<Event> triggers263 = new ArrayList<Event>() {
							{
								add(new Event(""));
							}
						};
						Transition262 p261 = new Transition262(Init25,
								Pending26, this, triggers263, 1);

						public class Transition265 extends Transition {
							public Transition265(IVertex source,
									IVertex target, Region parent,
									List<Event> triggers, int priority) {
								super(source, target, parent, triggers,
										priority);
							}

							public boolean guard() {
								return deny == ((1) != 0);
							}

							public void action(IEventHolder e) {
								;
							}

							public void conditionAction(IEventHolder e) {
								request = 0 != 0;
								;
							}
						}

						ArrayList<Event> triggers266 = new ArrayList<Event>() {
							{
								add(new Event(""));
							}
						};
						Transition265 p264 = new Transition265(Pending26,
								Init25, this, triggers266, 1);
					}

					public RegionR40 r25 = new RegionR40(this);
				}

				public StateBusy53 Busy24 = new StateBusy53("Busy1", this, false);

				public class Transition268 extends Transition {
					public Transition268(IVertex source, IVertex target,
							Region parent, List<Event> triggers, int priority) {
						super(source, target, parent, triggers, priority);
					}

					public boolean guard() {
						return reset == ((1) != 0);
					}

					public void action(IEventHolder e) {
						;
					}

					public void conditionAction(IEventHolder e) {
						cancel = 1 != 0;
						;
						request = 0 != 0;
						;
					}
				}

				ArrayList<Event> triggers269 = new ArrayList<Event>() {
					{
						add(new Event(""));
					}
				};
				Transition268 p267 = new Transition268(Busy24, Idle23, this,
						triggers269, 1);

				public class Transition271 extends Transition {
					public Transition271(IVertex source, IVertex target,
							Region parent, List<Event> triggers, int priority) {
						super(source, target, parent, triggers, priority);
					}

					public boolean guard() {
						return resourceIn >= 0 && resourceIn < 5;
					}

					public void action(IEventHolder e) {
						;
					}

					public void conditionAction(IEventHolder e) {
						resourceOut = resourceIn;
						;
						cancel = 0 != 0;
						;
					}
				}

				ArrayList<Event> triggers272 = new ArrayList<Event>() {
					{
						add(new Event(""));
					}
				};
				Transition271 p270 = new Transition271(Idle23, Busy24, this,
						triggers272, 1);

				public class Transition274 extends Transition {
					public Transition274(IVertex source, IVertex target,
							Region parent, List<Event> triggers, int priority) {
						super(source, target, parent, triggers, priority);
					}

					public void action(IEventHolder e) {
						;
					}

					public void conditionAction(IEventHolder e) {
						;
					}
				}

				ArrayList<Event> triggers275 = new ArrayList<Event>() {
					{
						add(new Event(""));
					}
				};
				Transition274 p273 = new Transition274(p36, Idle23, this,
						triggers275, 1);

				public class Transition277 extends Transition {
					public Transition277(Region parent, List<Event> triggers,
							int priority) {
						super(parent, triggers, priority);
					}

					public void action(IEventHolder e) {
						;
					}

					public void conditionAction(IEventHolder e) {
						cancel = 1 != 0;
						;
						request = 0 != 0;
						;
					}
				}

				ArrayList<Event> triggers278 = new ArrayList<Event>() {
					{
						add(new Event(""));
					}
				};
				Transition277 p276 = new Transition277(this, triggers278, 2);
			}

			public RegionR41 r23 = new RegionR41(this);
		}

		public STATE_T User122 = new STATE_T("User1", this, false);
		Transition p251 = new Transition(p250, User122, this, new Event(""));
	}

	public REGION_T r1 = new REGION_T(sm);

	public void initializeTransitions() {
		r1.User122.r23.p276.initialize(r1.User122.r23.Busy24.r25.Granted27,
				r1.User122.r23.Idle23);
	}
}
