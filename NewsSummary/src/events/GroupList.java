package events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class GroupList {
	private ArrayList<Group> groupList = null;
	private ArrayList<Event> bestList = null;
	private HashMap<String, Integer> eventIndex = null;
	public GroupList(TableEvent tableEvent) {
		groupList = new ArrayList<>();
		bestList = new ArrayList<>();
		eventIndex = new HashMap<>();
		for (Event event : tableEvent.getEvent()) {
			String eventVerb = event.getSKID().toString();
			if (eventIndex.containsKey(eventVerb)) {
				int index = eventIndex.get(eventVerb);
				Group group = groupList.get(index);
				group.add(event);		
			}
			else {
				//add group list				
				Group group = new Group();
				group.add(event);
				groupList.add(group);
				//add eventIndex
				eventIndex.put(eventVerb, groupList.size() - 1);
			}
		}
	}
	
	public ArrayList<Event> getBestList() {
		return bestList;
	}
	public ArrayList<Group> getGroupList() {
		return groupList;
	}
	public void setBestList(ArrayList<Event> bestList) {
		this.bestList = bestList;
	}
	public void setGroupList(ArrayList<Group> groupList) {
		this.groupList = groupList;
	}
	
	public ArrayList<Event> getBest(){
		for(Group group : groupList) {
			bestList.add(group.getBest());
		}
		timeFlow();
		return bestList;
	}
	private void timeFlow() {
		try {
				Collections.sort(bestList, new Comparator<Event>() {
					@Override
					public int compare(Event a, Event b) {
						if (a.getNormalizeDate().isEmpty())
							return 1;
						if (b.getNormalizeDate().isEmpty())
							return -1;
						return a.getNormalizeDate().get(0).getInterval().first().compareTo(
								b.getNormalizeDate().get(0).getInterval().first());
					}
				});
			}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Null o day" + bestList.toString());
		}
		
	}
}
