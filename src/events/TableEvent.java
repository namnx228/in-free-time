package events;



import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.StanfordCoreNLPServer;
import edu.stanford.nlp.util.*;
import java.util.ArrayList;
import java.util.Properties;

public class TableEvent {
	
	private ArrayList<Event> tableEvent;
	private ArrayList<String> listVerb;
	public void setTableEvent(ArrayList<Event> tableEvent) {
		this.tableEvent = tableEvent;
	}
	public ArrayList<Event> getTableEvent() {
		return tableEvent;
	}
	public ArrayList<String> getListVerb() {
		return listVerb;
	}
	public void setListVerb(ArrayList<String> listVerb) {
		this.listVerb = listVerb;
	}
	
	public TableEvent()
	{
		tableEvent = new ArrayList<>();
		//nhap verb tu file
		listVerb = new ArrayList<>();
		Annotation sent = new Annotation(IOUtils.slurpFileNoExceptions(verbfile));
		for(String word :  sent.toString().split(" "))
		{
			listVerb.add(word);
		}
	}
	public void printEvent()
	{
		for (Event event : tableEvent)
			event.printEvent();
	}
	private final String verbfile = "verbList.txt";

}
