package GUI;

import java.util.ArrayList;

import javax.swing.JTable;


import events.Event;
import events.TableEvent;

public class Table {
	private Object[][] data;
	private String[] columnNames;
	private JTable table;
	
	public Table(Object[][] data, String[] columnNames) {
		this.data = data;
		this.columnNames = columnNames;
		table = new JTable(data, columnNames);
	}
	
	public Table(TableEvent tableEvent) {
		String[] colName = {"CTID", "SKID", "TGID", "NCID", "ObjID"};
		
		ArrayList<Object[]> data = new ArrayList<>();
		
		
		for (Event e : tableEvent.getEvent()) {
			ArrayList<String> arrayEvent = new ArrayList<>();
			arrayEvent.add(e.printCTID());
			arrayEvent.add(e.printSKID());
			arrayEvent.add(e.printTGID());
			arrayEvent.add(e.printNCID());
			arrayEvent.add(e.printObjID());
			data.add(arrayEvent.toArray());
		}
		this.columnNames = colName;
		this.data = (Object[][])data.toArray();
		table = new JTable(this.data, this.columnNames);
	}
	

}
