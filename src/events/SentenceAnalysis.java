package events;

import java.util.List;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.*;

public class SentenceAnalysis {

	private Tree tree;
	private Tree npNode, vpNode;
	private Event event;
	
	public SentenceAnalysis(Tree tree)
	{
		this.tree = tree;
		event = new Event();
	}
	
	public void analysisSentence()
	{
		npNode = getFirstInterestingSubTree(tree, tree, "NP");
	    System.out.println(npNode.label().toString() + " " +  npNode.nodeNumber(tree));
	    
	    vpNode = getFirstInterestingSubTree(tree, tree, "VP");
	    
	    // find word np to add to sub
	    
	    //find vb in vp
	    findVbInVp();
	    
	    //name entity
	}
	private void findVbInVp()
	{
		//tim vb dau tien
		Tree firstVb = getFirstInterestingSubTree(vpNode, vpNode, VB);
		//tach lay tu
		List<CoreMap> listWord = getTokenFromTag(firstVb);
		
		
		//dem ss
			//neu dung la event thi cho vao record
		for(CoreMap token : listWord)
		{
			//ss token voi list event word
		}
	}
	
	private List<CoreMap> getTokenFromTag(Tree tree)
	{
		
		return null;
	}
	
	private Tree getFirstInterestingSubTree(Tree original, Tree tree, String targetLabel) {
		  if (tree == null) {
		    return null;
		  }
		  String label = tree.label().toString();
		  if (label.compareTo(targetLabel) == 0) {
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
	
	private final String NP = "NP";
	private final String VP = "VP";
	private final String VB = "VB";
	
}
