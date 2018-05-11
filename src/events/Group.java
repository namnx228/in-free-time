package events;

import java.util.ArrayList;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.time.SUTime.Time;
import edu.stanford.nlp.util.CoreMap;
import general.General;

public class Group {
	private ArrayList<CoreMap> eventVerb;
	private ArrayList<Time> normalizedTime;
	private ArrayList<CoreMap> location;
	private ArrayList<Event> itemList;	
	private ArrayList<CoreMap> subject;
	private ArrayList<CoreMap> Object;
	private ArrayList<CoreMap> other;
	private int weight;
	
	
	public Group(Event event) {
		this();
		this.add(event);
		
	}
	
	public Group() {
		this.eventVerb = new ArrayList<>();
		this.normalizedTime= new ArrayList<>();
		this.location = new ArrayList<>() ;
		itemList = new ArrayList<>();
		subject = new ArrayList<>();
		Object = new ArrayList<>();
		other = new ArrayList<>();
		weight = 0;
	}
	
	public int getWeight() {
		return weight;
	}
	
	
	public Event getBest() {
		int maxWeight = -1;
		Event bestEvent = null;
		for(Event event : itemList) {
			if (event.getWeight() > maxWeight) {
				maxWeight = event.getWeight();
				bestEvent = event;
			}
		}
		
		return bestEvent;
	}
	
	
	public void setItemList(ArrayList<Event> itemList) {
		this.itemList = itemList;
	}
	
	public ArrayList<CoreMap> getOther() {
		return other;
	}
	
	public void setOther(ArrayList<CoreMap> other) {
		this.other = other;
	}
	
	
	public ArrayList<Time> getNormalizedTime() {
		return normalizedTime;
	}
	public ArrayList<Event> getItemList() {
		return itemList;
	}
	
	
	public void addEvent(ArrayList<CoreMap> eventVerb) {
		for (CoreMap e: eventVerb) {
			if (!inside(this.eventVerb, e)) {
				this.eventVerb.add(e);
			}
		}
	}
	
	public void addLocation(ArrayList<CoreMap> location) {
		for (CoreMap e: location) {
			if (!inside(this.location, e)) {
				this.location.add(e);
			}
		}
	}
	public boolean timeComparation(ArrayList<Time> time) {
		if (this.normalizedTime.isEmpty())
			return true;
		if (this.normalizedTime.get(0).getInterval().first().compareTo(time.get(0).getInterval().second()) == 0) return true;
		return false;
		
	}
	public void addTime(ArrayList<Time>time) {
		if (!time.isEmpty())
			if (this.normalizedTime.isEmpty())
				this.normalizedTime.add(time.get(0));
	}
	
	public void add(Event event) {
		itemList.add(event);
		addEvent(event.getSKID());
		addLocation(event.getNCID());
		addTime(event.getNormalizeDate());
		addSubject(event.getCTID());
		addObject(event.getObjID());
		addOther(event.getOtherID());
		weight++;		
	}
	private void addOther(ArrayList<CoreMap> other ) {
		if (!this.other.isEmpty()) 
			for (CoreMap cm: other) {
				this.other.add(cm);
			}
	}
	private void addSubject(ArrayList<CoreMap> sub) {
		for (CoreMap cm : sub) {
			subject.add(cm);
		}
	}
	private void addObject(ArrayList<CoreMap> Object) {
		for (CoreMap cm: Object)
			if (!inside(this.Object, cm))
				this.Object.add(cm);
	}
	
	private boolean inside(ArrayList<CoreMap> list, CoreMap obj) {
		String tmp = obj.get(CoreAnnotations.TextAnnotation.class);
		if (tmp == null)
			tmp = obj.toString();
		
		try {
		for (CoreMap cm : list) 	{
			String tmp2 = cm.get(CoreAnnotations.TextAnnotation.class);
			if (tmp2 == null)
				tmp2 = cm.toString();
			
		
			if (tmp.compareToIgnoreCase(tmp2) == 0) 
						return true;
			}
		}
			catch (NullPointerException e) {
					e.printStackTrace();
				}
			
		
		return false;
	}
	private boolean inside(ArrayList<Time> list, Time time) {
		try {
			Time time1 = null, time2 = null;
			if (list.get(0).getInterval() == null)
				time1 = list.get(0).getTime();
			else time1 = list.get(0).getInterval().first();
			
			if (time.getInterval() == null)
				time2 = time.getTime();
			else  time2 = time.getInterval().first();
			if (time1.compareTo(time2)== 0)
				return true;
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean same(Event e) {
		boolean ok1 = false, ok2 = false, ok3 = false;
		//for eventVerb
		for (CoreMap cm : e.getSKID()) 
			if (inside(eventVerb, cm)) {
				ok1 = true;
				break;
			}
		
		if (ok1) {
			if (e.getNCID().isEmpty() || location.isEmpty()) {
				ok2 = true;
				
			}
			else
				for (CoreMap cm : e.getNCID()) 
					if (inside(location, cm)) {
						ok2 = true;
						break;
					}
			
		}
		if (ok2) {
			if (e.getNormalizeDate().isEmpty() || normalizedTime.isEmpty()) {
				ok3 = true;
			}
			else
				for (Time time : e.getNormalizeDate()) 
					if (inside(normalizedTime, time)) {
						ok3 = true;
						break;
					}
			
		}
		if (ok3) 
			return true;		
		return false;
	}
	public void pickOneSubject(ArrayList<String> listIgnore) {
		ArrayList<CoreMap> tmp = new ArrayList<>(subject);
		for (CoreMap cm : subject) {
			try {
				if (containIgnoreCase(listIgnore, cm.toString()) && tmp.size() > 1)
					tmp.remove(cm);
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		subject = tmp;
	}
	private boolean containIgnoreCase(ArrayList<String> list, String s) throws NullPointerException {
		for (String text : list)
			if (text.compareToIgnoreCase(s) == 0)
				return true;
		return false;
	}
	private void Print(Object object)
	{
		System.out.println(object);
	}
	public void printGroup() {
		Print("");
		Print("Chu the:" + subject.toString());
		Print("Su Kien:" + eventVerb.toString());
		Print("Thoi gian:" + normalizedTime.toString());
		Print("Dia diem:" + location.toString());
		Print("Khac:" + other.toString());
		Print("Trong so:" + weight);
	}
	
	public String printCTID() {
		/*String res = "";
		
		for (CoreMap tokenCT: subject) {
			res = res + General.coreMapToString(tokenCT) + " ";
		}
		return res;
		*/
		return General.coreMapToString(subject.get(0));
	}
	
	public String printSKID() {
		String res = "";		
		
		for (CoreMap tokenCT: eventVerb) {
			res = res + General.coreMapToString(tokenCT) + " ";
			//theo yeu cau cu chuoi cua co: 1 dong tu chinh, 1 phu:Khong dap ung duoc --> chi in 1 verb --> break
			break;
		}
		return res;
	}
	
	public String printOtherID() {
		if (other.isEmpty()) return "";
		String res = "";
		
		for (CoreMap tokenOther : other) {
			res = General.coreMapToString(tokenOther);
		}
		return res;
	}
	
	public String printNormalizeTGID() {
		if (normalizedTime.isEmpty()) return "";
		String res = General.timeToString(normalizedTime.get(0));		
		return res;
	}
	
	
	
	
	public String printNCID() {
		String res = "";
		
		for (CoreMap tokenCT: location) {
			res = res + General.coreMapToString(tokenCT) + " ";
		}
		return res;
	}
	
	public String printObjID() {
		String res = "";
		
		for (CoreMap tokenCT: other) {
			res = res + General.coreMapToString(tokenCT) + " ";
		}
		return res;
	}

}
