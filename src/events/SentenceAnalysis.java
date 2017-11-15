package events;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.*;

public class SentenceAnalysis {

	private Tree tree;
	private Tree npNode, vpNode;
	private Event event;
	private ArrayList<String> listVerb;
	private String inputSentence;
	
	public SentenceAnalysis(Tree tree, ArrayList<String> listVerb, String inputString)
	{
		this.tree = tree;
		event = new Event();
		this.listVerb = listVerb;
		this.inputSentence = inputString;
	}
	
	public void analysisSentence()
	{
		event = new Event(); // (1)
		npNode = getFirstInterestingSubTree(tree, tree, "NP");
	    System.out.println(npNode.label().toString() + " " +  npNode.nodeNumber(tree));
	    putSbtoEvent();
	    
	    vpNode = getFirstInterestingSubTree(tree, tree, "VP");
	    
	    // find word np to add to sub
	    
	    //find vb in vp
	    findVbInVp();
	    
	    //name entity
	    NEREngine nerEngine = new NEREngine(inputSentence);
	    putObjectEvent(nerEngine);
	    
	}
	
	private void putObjectEvent(NEREngine nerEngine)
	{
		event.setNCID(nerEngine.getListLocation());
		event.setTGID(nerEngine.getListDate());
		event.setObjID(nerEngine.getListObject());
	}
	private void putSbtoEvent()
	{
		event.setCTID((ArrayList<CoreMap>)getTokenFromTree(npNode));
	}
	
	private void findVbInVp()
	{
		//tim vb dau tien
		Tree firstVb = getFirstInterestingSubTree(vpNode, vpNode, VB);
		//tach lay tu
		List<CoreMap> listWord = getTokenFromTree(firstVb);
		
		
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
			result.add(tmp);
		}
		return result;
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
		    List<Tree> siblings = tree.siblings(original);
		    if (siblings != null) {
		      for (Tree sibling : siblings) {
		    	  Tree result = getFirstInterestingSubTree(originalTree, sibling, targetLabel);
		    	  if (result != null) return result;
		        
		      }
		    }
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
			
			if (targetText.compareTo(text) == 0)
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
