package events;


import java.util.ArrayList;


import edu.stanford.nlp.util.*;

public class Event {

	private ArrayList<CoreMap> SKID, CTID, TGID, NCID;
	
	public ArrayList<CoreMap> getCTID() {
		return CTID;
	}
	
	public ArrayList<CoreMap> getNCID() {
		return NCID;
	}
	public ArrayList<CoreMap> getSKID() {
		return SKID;
	}
	public ArrayList<CoreMap> getTGID() {
		return TGID;
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
	}
	
	
	
}
