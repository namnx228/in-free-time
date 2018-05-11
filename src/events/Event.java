package events;


import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

import general.General;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NormalizedNamedEntityTagAnnotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.time.SUTime.Time;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.*;

public class Event {

	private ArrayList<CoreMap> SKID, CTID;
	private ArrayList<CoreMap> TGID, NCID, ObjID, otherID;
	private ArrayList<Time> normalizeDate;
	private int weight;
	private String content;
	private CoreMap sentence;
	
	public Event()
	{
		SKID = new ArrayList<>();
		CTID = new ArrayList<>();
		TGID = new ArrayList<>();
		NCID = new ArrayList<>();
		ObjID = new ArrayList<>();
		otherID = new ArrayList<>();
		normalizeDate = new ArrayList<>();
		weight = 0;
		content = "";
	}
	
	
	
	public String printCTID() {
		String res = "";
		
		for (CoreMap tokenCT: CTID) {
			res = res + General.coreMapToString(tokenCT) + " ";
		}
		return res;
	}
	
	public String printSKID() {
		String res = "";		
		
		for (CoreMap tokenCT: SKID) {
			res = res + General.coreMapToString(tokenCT) + " ";
			//theo yeu cau cu chuoi cua co: 1 dong tu chinh, 1 phu:Khong dap ung duoc --> chi in 1 verb --> break
			break;
		}
		return res;
	}
	
	public String printNormalizeTGID() {
		if (normalizeDate.isEmpty()) return "";
		String res = General.timeToString(normalizeDate.get(0));		
		return res;
	}
	private void filterDuplicate() {
		ArrayList<CoreMap> res = new ArrayList<>();
		//String tmp = "The answer was far, very far. Chancellor Angela Merkel of Germany and President Emmanuel Macron of France beat back populist and far-right insurgencies in the past year";
		
		
		for (CoreMap cm : TGID) {
			boolean ok = true;
			for (CoreMap cm2 : res) {
				if (General.coreMapToString(cm).compareTo(General.coreMapToString(cm2)) == 0) {
					ok = false;
					break;
				}
			}
			if (ok) res.add(cm);
		}
		TGID = res;
	}
	
	public String printTGID() {
		filterDuplicate();
		String res = "";
		for (CoreMap tokenCT: TGID) {
			res = res + General.coreMapToString(tokenCT) + " ";
		}
		if (!res.isEmpty())
			res = "on " + res;
		
		return res;
	}
	
	public ArrayList<CoreMap> getOtherID() {
		return otherID;
	}
	public void setOtherID(ArrayList<CoreMap> otherID) {
		this.otherID = otherID;
	}
	
	public String printNCID() {
		String res = "";
		
		for (CoreMap tokenCT: NCID) {
			res = res + General.coreMapToString(tokenCT) + " ";
		}
		if (!res.isEmpty())
			res = "in " + res;
		return res;
	}
	/*
	private String trim(String s, String rm) {
		String res = s.trim();
		if (res.length() > 6) {
			String tmp = res.substring(res.length() - 3);
			if (tmp.compareTo(rm) == 0)					
				res = res.substring(0, res.length() - 3);
		}
		return res;
	}*/
	
	public String printOtherId() {
		String res = "";		
		for (CoreMap tokenCT: otherID) {
			res = res + General.coreMapToString(tokenCT) + " ";
		}		
		return res;
	}
	
	public String printObjID() {
		String res = "";
		
		for (CoreMap tokenCT: ObjID) {
			res = res + General.coreMapToString(tokenCT) + " ";
		}
		//res = trim(res, " on");
		//res = trim(res, " in");
		//res = trim(res, " on");
		return res;
	}
	
	public ArrayList<CoreMap> getCTID() {
		return CTID;
	}
	
	public ArrayList<CoreMap> getNCID() {
		nullFilter(NCID);
		return NCID;
	}
	public ArrayList<CoreMap> getSKID() {
		nullFilter(SKID);
		return SKID;
	}
	public ArrayList<CoreMap> getTGID() {
		nullFilter(TGID);
		return TGID;
	}
	public ArrayList<Time> getNormalizeDate() {
		nullFilter(normalizeDate);
		return normalizeDate;
	}
	public int getWeight() {
		return weight;
	}
	public String getContent() {
		return content;
	}
	public void setNormalizeDate(ArrayList<Time> normalizeDate) {
		this.normalizeDate = normalizeDate;
	}
	public CoreMap getSentence() {
		return sentence;
	}
	public void setCTID(ArrayList<CoreMap> cTID) {
		CTID = cTID;
	}
	public void setNCID(ArrayList<CoreMap> nCID) {
		NCID = nCID;
	}
	
	public void setSKID(ArrayList<CoreMap> sKID) {
		SKID = sKID;
	}
	public void setTGID(ArrayList<CoreMap> tGID) {
		TGID = tGID;
		updateNormalizedTime();
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ArrayList<CoreMap> getObjID() {
		return ObjID;
	}
	public void setObjID(ArrayList<CoreMap> objID) {
		ObjID = objID;
	}
	public void setSentence(CoreMap sentence) {
		this.sentence = sentence;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public void calWeight() {
		int count = 0;
		if (!TGID.isEmpty())
			count++;
		if (!NCID.isEmpty())
			count++;
		if (!ObjID.isEmpty())
			count++;
		weight = count;
	}
	
	private void updateNormalizedTime()
	{
		for (CoreMap token : TGID) {
			try {
				Time t = token.get(TimeExpression.Annotation.class).getTemporal().getTime();
				if (t != null)
					normalizeDate.add(t);
				
				
			}
			catch (NullPointerException e) {
				continue;
			}
	
		}
		//normalizeDate = new ArrayList<>(new HashSet<>(normalizeDate));
		//Collections.sort(normalizeDate, new Comparator<String>());
		earliestFirst();
	}
	private void earliestFirst() {
		/*
		Time earliest = normalizeDate.get(0);
		for (int i = 1; i < normalizeDate.size(); i++) {
			Time t = normalizeDate.get(i);
			if (t.getInterval().first().compareTo(earliest.getInterval().first()) < 0) {
				earliest = t;
			}
		}
		normalizeDate.clear();
		normalizeDate.add(earliest);
		*/
		Collections.sort(normalizeDate, new Comparator<Time>() {
			@Override
			public int compare(Time a, Time b) {
				try {	
					if (a == null)
						return -1;
					if (b == null)
						return 1;
					Time time1 = null, time2 = null;
					if (a.getInterval() == null)
						time1 = a.getTime();
					else time1 = a.getInterval().first();
					
					if (b.getInterval() == null)
						time2 = b.getTime();
					else time2 = b.getInterval().first();
					if (time1 == null)
						return -1;
					if (time2 == null)
						return 1;
					return time1.compareTo(
							time2);
				}
				catch (NullPointerException e) {
					//e.printStackTrace();
					return 0;
				}
				
			}
		});
		
		
	}
	
	private void Print(Object object)
	{
		System.out.println(object);
	}
	
	public void printEvent()
	{
		try{
			Print("Su kien: " + SKID.toString());
			Print("Chu the: " + CTID.toString());
			Print("Thoi gian: " + TGID.toString());
			Print("Dia diem: " + NCID.toString() );
			Print("Cac thu khac " + ObjID.toString());
			Print("Thoi gian chuan: " + normalizeDate);
			Print("Trong so: " + weight);
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
	}
	public void printDate()
	{
		for (CoreMap cm : TGID)
		{
			String result = cm.get(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class);
			System.out.println(result);
		}
	}
	
	private void nullFilter(ArrayList list) {
		ArrayList tmp = new ArrayList<>(list);
		for (Object o : list) {
			if (o == null)
				tmp.remove(o);
		}
		list = tmp;
	}
	
	
}
