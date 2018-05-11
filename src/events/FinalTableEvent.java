package events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.time.SUTime.Time;

public class FinalTableEvent {
	private ArrayList<Event> listEvent;
	private ArrayList<Group> listGroup;
	public FinalTableEvent(ArrayList<TableEvent> listTableEvent) {
		listEvent = new ArrayList<>();
		listGroup = new ArrayList<>();
		putEventToList(listTableEvent);
		syntheticEvent();
		
	}
	
	private void putEventToList(ArrayList<TableEvent> listTableEvent) {
		for (TableEvent table: listTableEvent) {
			for (Event e: table.getEvent()) {
				listEvent.add(e);
			}
		}
		
	}
	
	private void syntheticEvent() {
		//cho vao group
		for (Event e: listEvent) {
			//for listGroup
			if (!belongToAnyGroup(e)) {
				listGroup.add(new Group(e));
			}
		}
		timeFlow();
		subjectFilter();
		//printGroup();
		printTableEvent();
	}
	private void printGroup() {
		for(Group g : listGroup)
			g.printGroup();
	}
	private void subjectFilter() {
		Annotation annotation = new Annotation(IOUtils.slurpFileNoExceptions(PRONOUNS_LIST));
		ArrayList<String> listSub = new ArrayList<>();
		for (String s : annotation.toString().split("\n")) {
			listSub.add(s);
		}
		
		for (Group group : listGroup) {
			try {
				group.pickOneSubject(listSub);
			}
			catch(NullPointerException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	private boolean belongToAnyGroup(Event e) {
		for (Group group: listGroup) {
			if (group.same(e)) {
				group.add(e);
				return true;
			}
		}
		
		return false;
		
	}
	
	private void timeFlow() {
		
				Collections.sort(listGroup, new Comparator<Group>() {
					@Override
					public int compare(Group a, Group b) {
						try {
							if (a.getNormalizedTime().isEmpty())
								return 1;
							if (b.getNormalizedTime().isEmpty())
								return -1;
							Time time1 = null, time2 = null;
							if (a.getNormalizedTime().get(0).getInterval() == null)
								time1 = a.getNormalizedTime().get(0).getTime();
							else time1 = a.getNormalizedTime().get(0).getInterval().first();
							
							if (b.getNormalizedTime().get(0).getInterval() == null)
								time2 = b.getNormalizedTime().get(0).getTime();
							else time2 = b.getNormalizedTime().get(0).getInterval().first();
							
							return time1.compareTo(
									time2);
						}
						catch (NullPointerException e) {
							return 0;
						}
					}
				});
			}
	private void Print(String input) {
		System.out.println(input);
	}
	
	public void printTableEvent() {
		Print("\n");
		Print("TONG HOP SU KIEN");
		
		for(int i = 1; i <= listGroup.size(); i++) {
			Print("");
			Print("Su Kien " + i + ":");
			Group g = listGroup.get(i-1);
			Print("CTID: " + g.printCTID());
			Print("SKID: " + g.printSKID());
			Print("TGID: " + g.printNormalizeTGID());
			Print("NCID: " + g.printNCID());
			Print("ObjID: " + g.printObjID());
			Print("OtherID: " + g.printOtherID());
			Print("Trong so: " + g.getWeight());
		}
		//Table table = new Table(this);
	}
	private final String PRONOUNS_LIST = "text/subject.txt";

}
