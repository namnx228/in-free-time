package events;


import java.util.ArrayList;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.*;

public class Event {

	private ArrayList<CoreMap> SKID, CTID;
	private ArrayList<CoreLabel> TGID, NCID, ObjID;
	
	
	public ArrayList<CoreMap> getCTID() {
		return CTID;
	}
	
	public ArrayList<CoreLabel> getNCID() {
		return NCID;
	}
	public ArrayList<CoreMap> getSKID() {
		return SKID;
	}
	public ArrayList<CoreLabel> getTGID() {
		return TGID;
	}
	public void setCTID(ArrayList<CoreMap> cTID) {
		CTID = cTID;
	}
	public void setNCID(ArrayList<CoreLabel> nCID) {
		NCID = nCID;
	}
	
	public void setSKID(ArrayList<CoreMap> sKID) {
		SKID = sKID;
	}
	public void setTGID(ArrayList<CoreLabel> tGID) {
		TGID = tGID;
	}
	
	public ArrayList<CoreLabel> getObjID() {
		return ObjID;
	}
	public void setObjID(ArrayList<CoreLabel> objID) {
		ObjID = objID;
	}
	
	private void Print(Object object)
	{
		System.out.println(object);
	}
	
	public void printEvent()
	{
		Print("Su kien: " + SKID.toString());
		Print("Chu the: " + CTID.toString());
		Print("Thoi gian: " + TGID.toString());
		Print("Dia diem: " + NCID.toString() );
		Print("Cac thu khac " + ObjID.toString());
	}
	
	
}
