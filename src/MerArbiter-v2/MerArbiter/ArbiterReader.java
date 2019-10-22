package MerArbiter;

import java.util.*;
import java.io.*;
import edu.vanderbilt.isis.sm.*;

public class ArbiterReader implements IDataReader {
	private TopLevelArbiter.REGION_T.STATE_T state;
	private IDataProvider dataProvider;
	private IDataPrinter dataPrinter;

	public ArbiterReader(TopLevelArbiter.REGION_T.STATE_T state,
			IDataProvider dataProvider, IDataPrinter dataPrinter) {
		this.state = state;
		this.dataProvider = dataProvider;
		this.dataPrinter = dataPrinter;
	}

	// Implemented from IDataReader for custom inputting
	public void setInput() {
//		ArrayList<String> inputs = new ArrayList<String>();
//		for (int i = 0; i < 6; i++) {
//			inputs.add(dataProvider.readData());
//		}
//		dataProvider.advance();
//		if (inputs.size() == 6) { // A little error-checking
//			int val_0 = Integer.parseInt(inputs.get(0));
//			boolean val_1 = Boolean.parseBoolean(inputs.get(1));
//			boolean val_2 = Boolean.parseBoolean(inputs.get(2));
//			boolean val_3 = Boolean.parseBoolean(inputs.get(3));
//			boolean val_4 = Boolean.parseBoolean(inputs.get(4));
//			int val_5 = Integer.parseInt(inputs.get(5));
//			state.setData(val_0, val_1, val_2, val_3, val_4, val_5);
//		}
	}

	public void writeOutput() {
		ArrayList<String> outputs = new ArrayList<String>();
		System.out.println("Value of u1grant is: " + state.u1grant);
		outputs.add("" + state.u1grant);
		System.out.println("Value of u1deny is: " + state.u1deny);
		outputs.add("" + state.u1deny);
		System.out.println("Value of u1rescind is: " + state.u1rescind);
		outputs.add("" + state.u1rescind);
		System.out.println("Value of u2grant is: " + state.u2grant);
		outputs.add("" + state.u2grant);
		System.out.println("Value of u2deny is: " + state.u2deny);
		outputs.add("" + state.u2deny);
		System.out.println("Value of u2rescind is: " + state.u2rescind);
		outputs.add("" + state.u2rescind);
		if (dataPrinter != null) {
			dataPrinter.writeData(outputs);
		}
		dataProvider.advance();
	}

	public boolean hasData() {
		return this.dataProvider.hasData();
	}

	public String readEvent() {
		return this.dataProvider.readEvent();
	}
};