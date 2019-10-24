package MerArbiter;

import java.util.*;
import java.io.*;
import edu.vanderbilt.isis.sm.*;
import edu.vanderbilt.isis.sm.Pseudostate.Kind;
import edu.vanderbilt.isis.sm.properties.*;
import com.martiansoftware.jsap.*;

public class TopLevelArbiter implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public StateMachine sm = new StateMachine();
	public Interpreter sim;

	public TopLevelArbiter() {
	}

	public static void main(String[] args) throws Exception {
		TopLevelArbiter example = new TopLevelArbiter();
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
			patterns = example.makeProperties(example.r1.Arbiter3);
		}

		IDataReader dataReader = new ArbiterReader(example.r1.Arbiter3,
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

		public Pseudostate p40 = new Pseudostate(this, Kind.INITIAL);

		public class STATE_T extends State {
			public boolean u1cancel ;
			public int commUser ;
			public int panCamUser ;
			public int driveUser ;
			public int armUser ;
			public int ratUser ;
			public boolean u1request ;
			public int u1resource ;
			public boolean u1grant ;
			public boolean u1deny ;
			public boolean u1rescind ;
			public int[] resources = new int[5];
			public boolean u2cancel ;
			public boolean u2request ;
			public int u2resource ;
			public boolean u2grant ;
			public boolean u2deny ;
			public boolean u2rescind ;

			@Override
			public boolean equals(Object obj) {
				if (obj instanceof STATE_T) {
					STATE_T o = (STATE_T) obj;
					boolean ret = o.u1cancel == u1cancel && o.commUser == commUser && o.panCamUser == panCamUser
							&& o.driveUser == driveUser
							&& o.armUser == armUser
							&& o.ratUser == ratUser
							&& o.u1request == u1request
//							&& o.u1resource == u1resource // causes a mismatch for some weird reason
							&& o.u1grant == u1grant
							&& o.u1deny == u1deny
							&& o.u1rescind == u1rescind && o.u2cancel == u2cancel
							&& o.u2request == u2request && o.u2grant == u2grant && o.u2deny == u2deny
							&& o.u2rescind == u2rescind && o.resources.length == resources.length;
					if (!ret) {
						System.out.println("STATE_T an attribute mismatched");
						return false;
					}
					else {
						for (int i = 0; i < resources.length; i++) {
							if (resources[i] != o.resources[i]) return false;
						}
						return true;
					}
				}
				return false;
			}

			public void setData(int val_0, boolean val_1, boolean val_2,
					boolean val_3, boolean val_4, int val_5) {
				this.u1resource = val_0;
				this.u1request = val_1;
				this.u1cancel = val_2;
				this.u2cancel = val_3;
				this.u2request = val_4;
				this.u2resource = val_5;
			}

			public STATE_T(String name, Region parent, boolean isFinal) {
				super(name, parent, isFinal);
			}

			public class RegionR39 extends Region {
				public RegionR39(IRegionParent parent) {
					super(parent);
				}

				public Pseudostate p1 = new Pseudostate(this, Kind.INITIAL);

				public class StateInit34 extends State {
					public StateInit34(String name, Region parent,
							boolean isFinal) {
						super(name, parent, isFinal);
					}
				}

				public StateInit34 Init4 = new StateInit34("Init", this, false);

				public class StateRunning35 extends State {
					public StateRunning35(String name, Region parent,
							boolean isFinal) {
						super(name, parent, isFinal);
					}

					public class RegionR36 extends Region {
						public RegionR36(IRegionParent parent) {
							super(parent);
						}

						public Pseudostate p141 = new Pseudostate(this,
								Kind.INITIAL);

						public class StateUser136 extends State {
							public StateUser136(String name, Region parent,
									boolean isFinal) {
								super(name, parent, isFinal);
							}

							public class RegionR35 extends Region {
								public RegionR35(IRegionParent parent) {
									super(parent);
								}

								public Pseudostate p18 = new Pseudostate(this,
										Kind.INITIAL);
								public Pseudostate p2 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p3 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p4 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p5 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p6 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p7 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p8 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p9 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p10 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p11 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p12 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p13 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p14 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p15 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p16 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p17 = new Pseudostate(this,
										Kind.JUNCTION);

								public class StateIdle37 extends State {
									public StateIdle37(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}
								}

								public StateIdle37 Idle7 = new StateIdle37(
										"Idle1", this, false);

								public class StateComm38 extends State {
									public StateComm38(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}

									public void entryAction() {
										u1grant = 1 != 0;
										;
										u1deny = 0 != 0;
										;
										resources[(int) (commUser)] = (int) (1);
										;
									}
								}

								public StateComm38 Comm8 = new StateComm38(
										"Comm1", this, false);

								public class StateWaitForCancel39 extends State {
									public StateWaitForCancel39(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}
								}

								public StateWaitForCancel39 WaitForCancel9 = new StateWaitForCancel39(
										"WaitForCancel1", this, false);

								public class StateDrive40 extends State {
									public StateDrive40(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}

									public void entryAction() {
										u1grant = 1 != 0;
										;
										u1deny = 0 != 0;
										;
										resources[(int) (driveUser)] = (int) (1);
										;
									}
								}

								public StateDrive40 Drive10 = new StateDrive40(
										"Drive1", this, false);

								public class StateArm41 extends State {
									public StateArm41(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}

									public void entryAction() {
										u1grant = 1 != 0;
										;
										u1deny = 0 != 0;
										;
										resources[(int) (armUser)] = (int) (1);
										;
									}
								}

								public StateArm41 Arm11 = new StateArm41("Arm1",
										this, false);

								public class StateRat42 extends State {
									public StateRat42(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}

									public void entryAction() {
										u1grant = 1 != 0;
										;
										u1deny = 0 != 0;
										;
										resources[(int) (ratUser)] = (int) (1);
										;
									}
								}

								public StateRat42 Rat12 = new StateRat42("Rat1",
										this, false);

								public class StatePanCam43 extends State {
									public StatePanCam43(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}

									public void entryAction() {
										u1grant = 1 != 0;
										;
										u1deny = 0 != 0;
										;
										resources[(int) (panCamUser)] = (int) (1);
										;
									}
								}

								public StatePanCam43 PanCam13 = new StatePanCam43(
										"PanCam1", this, false);

								public class Transition43 extends Transition {
									public Transition43(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1resource == armUser;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers44 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition43 p42 = new Transition43(p7, p8,
										this, triggers44, 1);

								public class Transition46 extends Transition {
									public Transition46(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers47 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition46 p45 = new Transition46(
										WaitForCancel9, Comm8, this,
										triggers47, 1);

								public class Transition49 extends Transition {
									public Transition49(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1resource == commUser
												&& resources[(int) (commUser)] == 0
												&& resources[(int) (driveUser)] == 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers50 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition49 p48 = new Transition49(p2, Comm8,
										this, triggers50, 1);

								public class Transition52 extends Transition {
									public Transition52(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (commUser)] != 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										u1deny = 1 != 0;
										;
										u1grant = 0 != 0;
										;
									}
								}

								ArrayList<Event> triggers53 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition52 p51 = new Transition52(p2, Idle7,
										this, triggers53, 2);

								public class Transition55 extends Transition {
									public Transition55(IVertex source,
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

								ArrayList<Event> triggers56 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition55 p54 = new Transition55(p2, p4,
										this, triggers56, 3);

								public class Transition58 extends Transition {
									public Transition58(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1request == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers59 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition58 p57 = new Transition58(Idle7, p3,
										this, triggers59, 1);

								public class Transition61 extends Transition {
									public Transition61(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1resource == commUser;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers62 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition61 p60 = new Transition61(p3, p2,
										this, triggers62, 1);

								public class Transition64 extends Transition {
									public Transition64(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (driveUser)] == 2;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										u2rescind = 1 != 0;
										;
									}
								}

								ArrayList<Event> triggers65 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition64 p63 = new Transition64(p4,
										WaitForCancel9, this, triggers65, 1);

								public class Transition67 extends Transition {
									public Transition67(IVertex source,
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

								ArrayList<Event> triggers68 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition67 p66 = new Transition67(p12, p13,
										this, triggers68, 2);

								public class Transition70 extends Transition {
									public Transition70(IVertex source,
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

								ArrayList<Event> triggers71 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition70 p69 = new Transition70(p3, p5,
										this, triggers71, 2);

								public class Transition73 extends Transition {
									public Transition73(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1resource == driveUser;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers74 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition73 p72 = new Transition73(p5, p6,
										this, triggers74, 1);

								public class Transition76 extends Transition {
									public Transition76(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (commUser)] == 0
												&& resources[(int) (driveUser)] == 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers77 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition76 p75 = new Transition76(p6,
										Drive10, this, triggers77, 1);

								public class Transition79 extends Transition {
									public Transition79(IVertex source,
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

								ArrayList<Event> triggers80 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition79 p78 = new Transition79(p6, p13,
										this, triggers80, 2);

								public class Transition82 extends Transition {
									public Transition82(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										resources[(int) (commUser)] = (int) (0);
										;
									}
								}

								ArrayList<Event> triggers83 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition82 p81 = new Transition82(Comm8,
										Idle7, this, triggers83, 1);

								public class Transition85 extends Transition {
									public Transition85(IVertex source,
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

								ArrayList<Event> triggers86 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition85 p84 = new Transition85(p5, p7,
										this, triggers86, 2);

								public class Transition88 extends Transition {
									public Transition88(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (armUser)] == 0
												&& resources[(int) (ratUser)] == 0
												&& resources[(int) (panCamUser)] == 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers89 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition88 p87 = new Transition88(p8, Arm11,
										this, triggers89, 1);

								public class Transition91 extends Transition {
									public Transition91(IVertex source,
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

								ArrayList<Event> triggers92 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition91 p90 = new Transition91(p7, p9,
										this, triggers92, 2);

								public class Transition94 extends Transition {
									public Transition94(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1resource == ratUser;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers95 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition94 p93 = new Transition94(p9, p10,
										this, triggers95, 1);

								public class Transition97 extends Transition {
									public Transition97(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (armUser)] == 0
												&& resources[(int) (ratUser)] == 0
												&& resources[(int) (commUser)] == 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers98 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition97 p96 = new Transition97(p10, Rat12,
										this, triggers98, 1);

								public class Transition100 extends Transition {
									public Transition100(IVertex source,
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

								ArrayList<Event> triggers101 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition100 p99 = new Transition100(p9, p11,
										this, triggers101, 2);

								public class Transition103 extends Transition {
									public Transition103(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1resource == panCamUser;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers104 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition103 p102 = new Transition103(p11,
										p12, this, triggers104, 1);

								public class Transition106 extends Transition {
									public Transition106(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (armUser)] == 0
												&& resources[(int) (panCamUser)] == 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers107 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition106 p105 = new Transition106(p12,
										PanCam13, this, triggers107, 1);

								public class Transition109 extends Transition {
									public Transition109(IVertex source,
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

								ArrayList<Event> triggers110 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition109 p108 = new Transition109(p8, p13,
										this, triggers110, 2);

								public class Transition112 extends Transition {
									public Transition112(IVertex source,
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

								ArrayList<Event> triggers113 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition112 p111 = new Transition112(p10,
										p13, this, triggers113, 2);

								public class Transition115 extends Transition {
									public Transition115(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										resources[(int) (driveUser)] = (int) (0);
										;
									}
								}

								ArrayList<Event> triggers116 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition115 p114 = new Transition115(Drive10,
										p14, this, triggers116, 1);

								public class Transition118 extends Transition {
									public Transition118(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										resources[(int) (armUser)] = (int) (0);
										;
									}
								}

								ArrayList<Event> triggers119 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition118 p117 = new Transition118(Arm11,
										p15, this, triggers119, 1);

								public class Transition121 extends Transition {
									public Transition121(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										resources[(int) (ratUser)] = (int) (0);
										;
									}
								}

								ArrayList<Event> triggers122 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition121 p120 = new Transition121(Rat12,
										p16, this, triggers122, 1);

								public class Transition124 extends Transition {
									public Transition124(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u1cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										resources[(int) (panCamUser)] = (int) (0);
										;
									}
								}

								ArrayList<Event> triggers125 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition124 p123 = new Transition124(
										PanCam13, p17, this, triggers125, 1);

								public class Transition127 extends Transition {
									public Transition127(IVertex source,
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

								ArrayList<Event> triggers128 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition127 p126 = new Transition127(p17,
										p16, this, triggers128, 1);

								public class Transition130 extends Transition {
									public Transition130(IVertex source,
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

								ArrayList<Event> triggers131 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition130 p129 = new Transition130(p16,
										p15, this, triggers131, 1);

								public class Transition133 extends Transition {
									public Transition133(IVertex source,
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

								ArrayList<Event> triggers134 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition133 p132 = new Transition133(p15,
										p14, this, triggers134, 1);

								public class Transition136 extends Transition {
									public Transition136(IVertex source,
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

								ArrayList<Event> triggers137 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition136 p135 = new Transition136(p14,
										Idle7, this, triggers137, 1);

								public class Transition139 extends Transition {
									public Transition139(IVertex source,
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

								ArrayList<Event> triggers140 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition139 p138 = new Transition139(p18,
										Idle7, this, triggers140, 1);
							}

							public RegionR35 r6 = new RegionR35(this);
						}

						public StateUser136 User16 = new StateUser136("User1",
								this, false);
						Transition p142 = new Transition(p141, User16, this,
								new Event(""));
					}

					public RegionR36 r5 = new RegionR36(this);

					public class RegionR38 extends Region {
						public RegionR38(IRegionParent parent) {
							super(parent);
						}

						public Pseudostate p242 = new Pseudostate(this,
								Kind.INITIAL);

						public class StateUser244 extends State {
							public StateUser244(String name, Region parent,
									boolean isFinal) {
								super(name, parent, isFinal);
							}

							public class RegionR37 extends Region {
								public RegionR37(IRegionParent parent) {
									super(parent);
								}

								public Pseudostate p35 = new Pseudostate(this,
										Kind.INITIAL);
								public Pseudostate p19 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p20 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p21 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p22 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p23 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p24 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p25 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p26 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p27 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p28 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p29 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p30 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p31 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p32 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p33 = new Pseudostate(this,
										Kind.JUNCTION);
								public Pseudostate p34 = new Pseudostate(this,
										Kind.JUNCTION);

								public class StateRat45 extends State {
									public StateRat45(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}

									public void entryAction() {
										u2grant = 1 != 0;
										;
										u2deny = 0 != 0;
										;
										resources[(int) (ratUser)] = (int) (2);
										;
									}
								}

								public StateRat45 Rat15 = new StateRat45("Rat2",
										this, false);

								public class StatePanCam46 extends State {
									public StatePanCam46(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}

									public void entryAction() {
										u2grant = 1 != 0;
										;
										u2deny = 0 != 0;
										;
										resources[(int) (panCamUser)] = (int) (2);
										;
									}
								}

								public StatePanCam46 PanCam16 = new StatePanCam46(
										"PanCam2", this, false);

								public class StateIdle47 extends State {
									public StateIdle47(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}
								}

								public StateIdle47 Idle17 = new StateIdle47(
										"Idle2", this, false);

								public class StateComm48 extends State {
									public StateComm48(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}

									public void entryAction() {
										u2grant = 1 != 0;
										;
										u2deny = 0 != 0;
										;
										resources[(int) (commUser)] = (int) (2);
										;
									}
								}

								public StateComm48 Comm18 = new StateComm48(
										"Comm2", this, false);

								public class StateWaitForCancel49 extends State {
									public StateWaitForCancel49(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}
								}

								public StateWaitForCancel49 WaitForCancel19 = new StateWaitForCancel49(
										"WaitForCancel2", this, false);

								public class StateDrive50 extends State {
									public StateDrive50(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}

									public void entryAction() {
										u2grant = 1 != 0;
										;
										u2deny = 0 != 0;
										;
										resources[(int) (driveUser)] = (int) (2);
										;
									}
								}

								public StateDrive50 Drive20 = new StateDrive50(
										"Drive2", this, false);

								public class StateArm51 extends State {
									public StateArm51(String name,
											Region parent, boolean isFinal) {
										super(name, parent, isFinal);
									}

									public void entryAction() {
										u2grant = 1 != 0;
										;
										u2deny = 0 != 0;
										;
										resources[(int) (armUser)] = (int) (2);
										;
									}
								}

								public StateArm51 Arm21 = new StateArm51("Arm2",
										this, false);

								public class Transition144 extends Transition {
									public Transition144(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers145 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition144 p143 = new Transition144(
										WaitForCancel19, Comm18, this,
										triggers145, 1);

								public class Transition147 extends Transition {
									public Transition147(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2resource == commUser
												&& resources[(int) (commUser)] == 0
												&& resources[(int) (driveUser)] == 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers148 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition147 p146 = new Transition147(p20,
										Comm18, this, triggers148, 1);

								public class Transition150 extends Transition {
									public Transition150(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (commUser)] != 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										u1deny = 1 != 0;
										;
										u1grant = 0 != 0;
										;
									}
								}

								ArrayList<Event> triggers151 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition150 p149 = new Transition150(p20,
										Idle17, this, triggers151, 2);

								public class Transition153 extends Transition {
									public Transition153(IVertex source,
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

								ArrayList<Event> triggers154 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition153 p152 = new Transition153(p20,
										p21, this, triggers154, 3);

								public class Transition156 extends Transition {
									public Transition156(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2request == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers157 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition156 p155 = new Transition156(Idle17,
										p19, this, triggers157, 1);

								public class Transition159 extends Transition {
									public Transition159(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2resource == commUser;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers160 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition159 p158 = new Transition159(p19,
										p20, this, triggers160, 1);

								public class Transition162 extends Transition {
									public Transition162(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (driveUser)] == 1;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										u1rescind = 1 != 0;
										;
									}
								}

								ArrayList<Event> triggers163 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition162 p161 = new Transition162(p21,
										WaitForCancel19, this, triggers163, 1);

								public class Transition165 extends Transition {
									public Transition165(IVertex source,
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

								ArrayList<Event> triggers166 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition165 p164 = new Transition165(p19,
										p22, this, triggers166, 2);

								public class Transition168 extends Transition {
									public Transition168(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2resource == driveUser;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers169 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition168 p167 = new Transition168(p22,
										p23, this, triggers169, 1);

								public class Transition171 extends Transition {
									public Transition171(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (commUser)] == 0
												&& resources[(int) (driveUser)] == 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers172 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition171 p170 = new Transition171(p23,
										Drive20, this, triggers172, 1);

								public class Transition174 extends Transition {
									public Transition174(IVertex source,
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

								ArrayList<Event> triggers175 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition174 p173 = new Transition174(p22,
										p24, this, triggers175, 2);

								public class Transition177 extends Transition {
									public Transition177(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2resource == armUser;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers178 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition177 p176 = new Transition177(p24,
										p25, this, triggers178, 1);

								public class Transition180 extends Transition {
									public Transition180(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (armUser)] == 0
												&& resources[(int) (ratUser)] == 0
												&& resources[(int) (panCamUser)] == 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers181 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition180 p179 = new Transition180(p25,
										Arm21, this, triggers181, 1);

								public class Transition183 extends Transition {
									public Transition183(IVertex source,
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

								ArrayList<Event> triggers184 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition183 p182 = new Transition183(p24,
										p26, this, triggers184, 2);

								public class Transition186 extends Transition {
									public Transition186(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2resource != ratUser;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers187 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition186 p185 = new Transition186(p26,
										p27, this, triggers187, 1);

								public class Transition189 extends Transition {
									public Transition189(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (armUser)] == 0
												&& resources[(int) (ratUser)] == 0
												&& resources[(int) (commUser)] == 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers190 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition189 p188 = new Transition189(p27,
										Rat15, this, triggers190, 1);

								public class Transition192 extends Transition {
									public Transition192(IVertex source,
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

								ArrayList<Event> triggers193 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition192 p191 = new Transition192(p26,
										p31, this, triggers193, 2);

								public class Transition195 extends Transition {
									public Transition195(IVertex source,
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

								ArrayList<Event> triggers196 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition195 p194 = new Transition195(p25,
										p28, this, triggers196, 2);

								public class Transition198 extends Transition {
									public Transition198(IVertex source,
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

								ArrayList<Event> triggers199 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition198 p197 = new Transition198(p27,
										p28, this, triggers199, 2);

								public class Transition201 extends Transition {
									public Transition201(IVertex source,
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

								ArrayList<Event> triggers202 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition201 p200 = new Transition201(p32,
										p28, this, triggers202, 2);

								public class Transition204 extends Transition {
									public Transition204(IVertex source,
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

								ArrayList<Event> triggers205 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition204 p203 = new Transition204(p23,
										p28, this, triggers205, 2);

								public class Transition207 extends Transition {
									public Transition207(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										resources[(int) (commUser)] = (int) (0);
										;
									}
								}

								ArrayList<Event> triggers208 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition207 p206 = new Transition207(Comm18,
										Idle17, this, triggers208, 1);

								public class Transition210 extends Transition {
									public Transition210(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										resources[(int) (driveUser)] = (int) (0);
										;
									}
								}

								ArrayList<Event> triggers211 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition210 p209 = new Transition210(Drive20,
										p29, this, triggers211, 1);

								public class Transition213 extends Transition {
									public Transition213(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										resources[(int) (armUser)] = (int) (0);
										;
									}
								}

								ArrayList<Event> triggers214 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition213 p212 = new Transition213(Arm21,
										p30, this, triggers214, 1);

								public class Transition216 extends Transition {
									public Transition216(IVertex source,
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

								ArrayList<Event> triggers217 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition216 p215 = new Transition216(p33,
										p30, this, triggers217, 1);

								public class Transition219 extends Transition {
									public Transition219(IVertex source,
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

								ArrayList<Event> triggers220 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition219 p218 = new Transition219(p30,
										p29, this, triggers220, 1);

								public class Transition222 extends Transition {
									public Transition222(IVertex source,
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

								ArrayList<Event> triggers223 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition222 p221 = new Transition222(p29,
										Idle17, this, triggers223, 1);

								public class Transition225 extends Transition {
									public Transition225(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2resource == panCamUser;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers226 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition225 p224 = new Transition225(p31,
										p32, this, triggers226, 1);

								public class Transition228 extends Transition {
									public Transition228(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return resources[(int) (armUser)] == 0
												&& resources[(int) (panCamUser)] == 0;
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										;
									}
								}

								ArrayList<Event> triggers229 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition228 p227 = new Transition228(p32,
										PanCam16, this, triggers229, 1);

								public class Transition231 extends Transition {
									public Transition231(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										resources[(int) (ratUser)] = (int) (0);
										;
									}
								}

								ArrayList<Event> triggers232 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition231 p230 = new Transition231(Rat15,
										p33, this, triggers232, 1);

								public class Transition234 extends Transition {
									public Transition234(IVertex source,
											IVertex target, Region parent,
											List<Event> triggers, int priority) {
										super(source, target, parent, triggers,
												priority);
									}

									public boolean guard() {
										return u2cancel == ((1) != 0);
									}

									public void action(IEventHolder e) {
										;
									}

									public void conditionAction(IEventHolder e) {
										resources[(int) (panCamUser)] = (int) (0);
										;
									}
								}

								ArrayList<Event> triggers235 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition234 p233 = new Transition234(
										PanCam16, p34, this, triggers235, 1);

								public class Transition237 extends Transition {
									public Transition237(IVertex source,
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

								ArrayList<Event> triggers238 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition237 p236 = new Transition237(p34,
										p33, this, triggers238, 1);

								public class Transition240 extends Transition {
									public Transition240(IVertex source,
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

								ArrayList<Event> triggers241 = new ArrayList<Event>() {
									{
										add(new Event(""));
									}
								};
								Transition240 p239 = new Transition240(p35,
										Idle17, this, triggers241, 1);
							}

							public RegionR37 r15 = new RegionR37(this);
						}

						public StateUser244 User214 = new StateUser244("User2",
								this, false);
						Transition p243 = new Transition(p242, User214, this,
								new Event(""));
					}

					public RegionR38 r14 = new RegionR38(this);
				}

				public StateRunning35 Running5 = new StateRunning35("Running",
						this, false);

				public class Transition245 extends Transition {
					public Transition245(IVertex source, IVertex target,
							Region parent, List<Event> triggers, int priority) {
						super(source, target, parent, triggers, priority);
					}

					public void action(IEventHolder e) {
						;
					}

					public void conditionAction(IEventHolder e) {
						commUser = (int) (0);
						;
						driveUser = (int) (1);
						;
						panCamUser = (int) (2);
						;
						armUser = (int) (3);
						;
						ratUser = (int) (4);
						;
						resources[(int) (commUser)] = (int) (0);
						;
						resources[(int) (driveUser)] = (int) (0);
						;
						resources[(int) (panCamUser)] = (int) (0);
						;
						resources[(int) (armUser)] = (int) (0);
						;
						resources[(int) (ratUser)] = (int) (0);
						;
					}
				}

				ArrayList<Event> triggers246 = new ArrayList<Event>() {
					{
						add(new Event(""));
					}
				};
				Transition245 p244 = new Transition245(p1, Init4, this,
						triggers246, 1);

				public class Transition248 extends Transition {
					public Transition248(IVertex source, IVertex target,
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

				ArrayList<Event> triggers249 = new ArrayList<Event>() {
					{
						add(new Event(""));
					}
				};
				Transition248 p247 = new Transition248(Init4, Running5, this,
						triggers249, 1);
			}

			public RegionR39 r3 = new RegionR39(this);
		}

		public STATE_T Arbiter3 = new STATE_T("Arbiter", this, false);
		Transition p41 = new Transition(p40, Arbiter3, this, new Event(""));
	}

	public REGION_T r1 = new REGION_T(sm);

	public void initializeTransitions() {
	}
}
