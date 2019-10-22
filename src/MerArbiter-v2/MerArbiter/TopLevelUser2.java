package MerArbiter;

import java.util.*;
import java.io.*;
import edu.vanderbilt.isis.sm.*;
import edu.vanderbilt.isis.sm.Pseudostate.Kind;
import edu.vanderbilt.isis.sm.properties.*;
import com.martiansoftware.jsap.*;

public class TopLevelUser2 implements Serializable {
	public StateMachine sm = new StateMachine();
	public Interpreter sim;

	public TopLevelUser2() {
	}

	public static void main(String[] args) throws Exception {
		TopLevelUser2 example = new TopLevelUser2();
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
			patterns = example.makeProperties(example.r1.User228);
		}

		IDataReader dataReader = new User2Reader(example.r1.User228,
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

		public Pseudostate p279 = new Pseudostate(this, Kind.INITIAL);

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

			public class RegionR43 extends Region {
				public RegionR43(IRegionParent parent) {
					super(parent);
				}

				public Pseudostate p38 = new Pseudostate(this, Kind.INITIAL);

				public class StateIdle57 extends State {
					public StateIdle57(String name, Region parent,
							boolean isFinal) {
						super(name, parent, isFinal);
					}
				}

				public StateIdle57 Idle29 = new StateIdle57("Idle2", this, false);

				public class StateBusy58 extends State {
					public StateBusy58(String name, Region parent,
							boolean isFinal) {
						super(name, parent, isFinal);
					}

					public class RegionR42 extends Region {
						public RegionR42(IRegionParent parent) {
							super(parent);
						}

						public Pseudostate p39 = new Pseudostate(this,
								Kind.INITIAL);

						public class StateInit59 extends State {
							public StateInit59(String name, Region parent,
									boolean isFinal) {
								super(name, parent, isFinal);
							}
						}

						public StateInit59 Init31 = new StateInit59("Init2",
								this, false);

						public class StatePending60 extends State {
							public StatePending60(String name, Region parent,
									boolean isFinal) {
								super(name, parent, isFinal);
							}
						}

						public StatePending60 Pending32 = new StatePending60(
								"Pending2", this, false);

						public class StateGranted61 extends State {
							public StateGranted61(String name, Region parent,
									boolean isFinal) {
								super(name, parent, isFinal);
							}
						}

						public StateGranted61 Granted33 = new StateGranted61(
								"Granted2", this, false);

						public class Transition282 extends Transition {
							public Transition282(IVertex source,
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

						ArrayList<Event> triggers283 = new ArrayList<Event>() {
							{
								add(new Event(""));
							}
						};
						Transition282 p281 = new Transition282(Granted33,
								Init31, this, triggers283, 1);

						public class Transition285 extends Transition {
							public Transition285(IVertex source,
									IVertex target, Region parent,
									List<Event> triggers, int priority) {
								super(source, target, parent, triggers,
										priority);
							}

							public boolean guard() {
								return grant == ((1) != 0);
							}

							public void action(IEventHolder e) {
								;
							}

							public void conditionAction(IEventHolder e) {
								;
							}
						}

						ArrayList<Event> triggers286 = new ArrayList<Event>() {
							{
								add(new Event(""));
							}
						};
						Transition285 p284 = new Transition285(Pending32,
								Granted33, this, triggers286, 2);

						public class Transition288 extends Transition {
							public Transition288(IVertex source,
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

						ArrayList<Event> triggers289 = new ArrayList<Event>() {
							{
								add(new Event(""));
							}
						};
						Transition288 p287 = new Transition288(p39, Init31,
								this, triggers289, 1);

						public class Transition291 extends Transition {
							public Transition291(IVertex source,
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

						ArrayList<Event> triggers292 = new ArrayList<Event>() {
							{
								add(new Event(""));
							}
						};
						Transition291 p290 = new Transition291(Init31,
								Pending32, this, triggers292, 1);

						public class Transition294 extends Transition {
							public Transition294(IVertex source,
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

						ArrayList<Event> triggers295 = new ArrayList<Event>() {
							{
								add(new Event(""));
							}
						};
						Transition294 p293 = new Transition294(Pending32,
								Init31, this, triggers295, 1);
					}

					public RegionR42 r31 = new RegionR42(this);
				}

				public StateBusy58 Busy30 = new StateBusy58("Busy2", this, false);

				public class Transition297 extends Transition {
					public Transition297(IVertex source, IVertex target,
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

				ArrayList<Event> triggers298 = new ArrayList<Event>() {
					{
						add(new Event(""));
					}
				};
				Transition297 p296 = new Transition297(Busy30, Idle29, this,
						triggers298, 1);

				public class Transition300 extends Transition {
					public Transition300(IVertex source, IVertex target,
							Region parent, List<Event> triggers, int priority) {
						super(source, target, parent, triggers, priority);
					}

					public boolean guard() {
						return resourceIn >= 0 && resourceIn < 5;
//						return resourceIn >= 0 ;
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

				ArrayList<Event> triggers301 = new ArrayList<Event>() {
					{
						add(new Event(""));
					}
				};
				Transition300 p299 = new Transition300(Idle29, Busy30, this,
						triggers301, 1);

				public class Transition303 extends Transition {
					public Transition303(IVertex source, IVertex target,
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

				ArrayList<Event> triggers304 = new ArrayList<Event>() {
					{
						add(new Event(""));
					}
				};
				Transition303 p302 = new Transition303(p38, Idle29, this,
						triggers304, 1);

				public class Transition306 extends Transition {
					public Transition306(Region parent, List<Event> triggers,
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

				ArrayList<Event> triggers307 = new ArrayList<Event>() {
					{
						add(new Event(""));
					}
				};
				Transition306 p305 = new Transition306(this, triggers307, 2);
			}

			public RegionR43 r29 = new RegionR43(this);
		}

		public STATE_T User228 = new STATE_T("User2", this, false);
		Transition p280 = new Transition(p279, User228, this, new Event(""));
	}

	public REGION_T r1 = new REGION_T(sm);

	public void initializeTransitions() {
		r1.User228.r29.p305.initialize(r1.User228.r29.Busy30.r31.Granted33,
				r1.User228.r29.Idle29);
	}
}
