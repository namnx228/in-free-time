package events;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.time.TimeExpression.Annotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.*;

public class SentenceAnalysis {

	private Tree tree;
	private Tree npNode, vpNode;
	private Event event;
	private ArrayList<String> listVerb;
	private CoreMap inputSentence;
	private ArrayList<CoreMap> objectList;
	private ArrayList<String> objectTag;
	private ArrayList<CoreMap> otherList;
	
	
	public SentenceAnalysis(Tree tree, ArrayList<String> listVerb, CoreMap inputSentence)
	{
		this.tree = tree;
		event = new Event();
		this.listVerb = listVerb;
		this.inputSentence = inputSentence;
		objectList = new ArrayList<>();
		objectTag = makeObjectTag();
		otherList = new ArrayList<>();
	}
	
	private ArrayList<String> makeObjectTag(){
		objectTag = new ArrayList<>();
		objectTag.add("NP");
		objectTag.add("NNP");
		objectTag.add("NN");
		objectTag.add("NNPS");
		objectTag.add("PRP");
		/*
		objectTag.add("JJ");
		objectTag.add("NNS");
		objectTag.add("IN");
		objectTag.add("PRP$");
		objectTag.add("DT");
		objectTag.add("RB");
		objectTag.add("CC");
		objectTag.add("POS");
		objectTag.add("JJS");
		objectTag.add("RBS");
		objectTag.add("CD");
		objectTag.add("WRB");
		objectTag.add("JJR");
		*/
		
		return objectTag;
		
	}
	
	private ArrayList<CoreMap> trim(ArrayList<CoreMap> otherId){
		if (otherId.isEmpty())
			return otherId;
		String tmp = otherId.get(otherId.size()-1).get(CoreAnnotations.TextAnnotation.class);
		if (tmp == null) tmp = otherId.get(otherId.size()-1).toString();
		if (tmp.compareTo("on") == 0 || tmp.compareTo("in") == 0 || tmp.compareTo(".") == 0)
			otherId.remove(otherId.get(otherId.size() - 1));
		return otherId;
		
	}
	
	private void makeOtherList() {
		for (CoreMap token : inputSentence.get(CoreAnnotations.TokensAnnotation.class)) {
			
			if (!contain(event.getCTID(), token)
					&& !contain(event.getNCID(), token)
					&& !contain(event.getTGID(), token)
					&& !contain(event.getObjID(), token)
					&& !contain(event.getSKID(), token))
				
				otherList.add(token);
		}
		otherList = trim(otherList);
		otherList = trim(otherList);
		otherList = trim(otherList);
		event.setOtherID(otherList);
	}
	public void analysisSentence()
	{
		try {
			event = new Event(); // (1)
			npNode = getFirstInterestingSubTree(tree, tree, "NP");
			//tree.getLeaves().get(0).
		    //System.out.println(npNode.label().toString() + " " +  npNode.nodeNumber(tree));
		    putSbtoEvent();
		    
		    vpNode = getFirstInterestingSubTree(tree, tree, "VP");
		    
		    // find word np to add to sub
		    
		    //find vb in vp
		    findVbInVp();
		    
		    //name entity
		    NEREngine nerEngine = new NEREngine(inputSentence);
		    
		    putStuffOfEvent(nerEngine);
		    
		    putObject();
		    makeOtherList();
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
	    
	}
	
	private boolean contain(ArrayList<CoreMap> list, CoreMap cm) {
		if (list.isEmpty())
			return false;
		for (CoreMap word : list)
			try {
				String tmp2 = word.get(CoreAnnotations.TextAnnotation.class);
				if (tmp2 == null)
					tmp2 = word.toString();
				
				String tmp1 =  cm.get(CoreAnnotations.TextAnnotation.class);
				if (tmp1 == null)
					tmp1 = cm.toString();
				else ;
				if (tmp1.compareToIgnoreCase(tmp2) == 0)
					return true;
			}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private void putObject() {
		//Tree, event, NP
		for (CoreMap token : inputSentence.get(CoreAnnotations.TokensAnnotation.class)) {
			String label = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
			if (objectTag.contains(label) && !contain(event.getCTID(), token)
					&& !contain(event.getNCID(), token)
					&& !contain(event.getTGID(), token))
				objectList.add(token);
		}
		
		
		event.setObjID(objectList);
	}
	
	private void putStuffOfEvent(NEREngine nerEngine)
	{
		event.setNCID(nerEngine.getListLocation());
		event.setTGID(nerEngine.getListDate());
		//event.setObjID(nerEngine.getListObject());
		event.setContent(inputSentence.get(CoreAnnotations.TextAnnotation.class));
		event.setSentence(inputSentence);
		event.calWeight();
	}
	private void putSbtoEvent()
	{
		event.setCTID((ArrayList<CoreMap>)getTokenFromTree(npNode));
	}
	
	private void findVbInVp()
	{
		//tim vb dau tien
		
			ArrayList<Tree> Vb = getInterestingSubTree(vpNode, vpNode, VB);	
			//tach lay tu
			List<CoreMap> listWord = new ArrayList<>();
			if (Vb != null)
				for (Tree tree : Vb) {
					try { 
				
						listWord.addAll(getTokenFromTree(tree));
					}
					catch (NullPointerException e) {
						e.printStackTrace();
					}
				}		
		//dem ss
			//neu dung la event thi cho vao record
		for(CoreMap token : listWord)
		{
			//ss token voi list event word
			// if in list thi cho vao event
			if (inListVerb(token))
			{
				//cho verb vao event
				//break
				event.getSKID().add(token);
				//token.get(CoreAnnotations.W)
			}
		}
	}
	
	
	private List<CoreMap> getTokenFromTree(Tree tree)
	{
		List<Tree> children = tree.getLeaves();
		List<CoreMap> result = new ArrayList<>();
		for (Tree child : children)
		{
			CoreLabel tmp = new CoreLabel(child);
			result.add((CoreMap)tmp);
		}
		return result;
	}
	
	private ArrayList<Tree> getInterestingSubTree(Tree original, Tree tree, String targetLabel){
		if (tree == null) {
		    return null;
		  }
		ArrayList<Tree> res = new ArrayList<>();
		  String label = tree.label().toString();
		  if (label.matches(targetLabel)) {
			  res.add(tree);
			  return res;
		  } 
		  else if (!tree.isLeaf()) {
		    Tree originalTree = tree;
		   
		    for (Tree child : originalTree.children()) {
		    	res.addAll(getInterestingSubTree(originalTree, child, targetLabel));
		    	  
		    }
		  }
		  return res;
	}
	
	private Tree getFirstInterestingSubTree(Tree original, Tree tree, String targetLabel) {
		  if (tree == null) {
		    return null;
		  }
		  String label = tree.label().toString();
		  if (label.matches(targetLabel)) {
		    return tree;
		  } else if (!tree.isLeaf()) {
		    Tree originalTree = tree;
		    
		    for (Tree child : originalTree.children()) {
		    	Tree result = getFirstInterestingSubTree(originalTree, child, targetLabel);
		    	  if (result != null) return result;
		    }
		  }
		  return null;
		}
	
	
	private boolean inListVerb(CoreMap token)
	{
		String  targetText = ((CoreLabel)token).value();
		for (String text : listVerb)
		{
			
			if (targetText.compareToIgnoreCase(text) == 0)
				return true;
		}
		return false;
	}
	
	public Event getEvent() {
		return event;
	}
	
	private final String NP = "NP";
	private final String VP = "VP";
	private final String VB = "VB\\w*";
	
}
