package events;



import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.StanfordCoreNLPServer;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.*;
import java.util.ArrayList;
import java.util.Properties;

import GUI.Table;

public class TableEvent {
	
	private String title;
	private ArrayList<Event> tableEvent;
	
	public void setTableEvent(ArrayList<Event> tableEvent) {
		this.tableEvent = tableEvent;
	}
	public ArrayList<Event> getTableEvent() {
		return tableEvent;
	}
	
	
	public TableEvent()
	{
		title = "";
		tableEvent = new ArrayList<>();
		//nhap verb tu file
		
		
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	
	private void Print(String input) {
		System.out.println(input);
	}
	
	public void printTableEvent() {
		Print("\n");
		Print("Doc:" + title);
		
		for(int i = 1; i <= tableEvent.size(); i++) {
			Print("");
			Print("Su Kien " + i + ":");
			Event e = tableEvent.get(i-1);
			//e.getSentence().get(TreeCoreAnnotations.TreeAnnotation.class).pennPrint();
			Print("CTID: " + e.printCTID());
			Print("SKID: " + e.printSKID());
			Print("TGID: " + e.printTGID());
			Print("NCID: " + e.printNCID());
			Print("ObjID: " + e.printObjID());
			Print("OtherID: " + e.printOtherId());
		}
		//Table table = new Table(this);
	}
	
	public void printEvent()
	{
		for (Event event : tableEvent) {
			
			event.printEvent();
			
		}
	}
	public ArrayList<Event> getEvent()
	{
		return tableEvent;
	}
	

}
