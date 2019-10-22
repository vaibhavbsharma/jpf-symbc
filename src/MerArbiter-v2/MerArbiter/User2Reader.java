package MerArbiter;

import java.util.*;
import java.io.*;
import edu.vanderbilt.isis.sm.*;

public class User2Reader implements IDataReader {
	private TopLevelUser2.REGION_T.STATE_T state;
	private IDataProvider dataProvider;
	private IDataPrinter dataPrinter;

	public User2Reader(TopLevelUser2.REGION_T.STATE_T state,
			IDataProvider dataProvider, IDataPrinter dataPrinter) {
		this.state = state;
		this.dataProvider = dataProvider;
		this.dataPrinter = dataPrinter;
	}

	// Implemented from IDataReader for custom inputting
	public void setInput() {
//		ArrayList<String> inputs = new ArrayList<String>();
//		for (int i = 0; i < 5; i++) {
//			inputs.add(dataProvider.readData());
//		}
//		dataProvider.advance();
//		if (inputs.size() == 5) { // A little error-checking
//			int val_0 = Integer.parseInt(inputs.get(0));
//			;
//			boolean val_1 = Boolean.parseBoolean(inputs.get(1));
//			;
//			boolean val_2 = Boolean.parseBoolean(inputs.get(2));
//			;
//			boolean val_3 = Boolean.parseBoolean(inputs.get(3));
//			;
//			boolean val_4 = Boolean.parseBoolean(inputs.get(4));
//			;
//			state.setData(val_0, val_1, val_2, val_3, val_4);
//		}
	}

	public void writeOutput() {
		ArrayList<String> outputs = new ArrayList<String>();
		System.out.println("Value of resourceOut is: " + state.resourceOut);
		outputs.add("" + state.resourceOut);
		System.out.println("Value of request is: " + state.request);
		outputs.add("" + state.request);
		System.out.println("Value of cancel is: " + state.cancel);
		outputs.add("" + state.cancel);
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