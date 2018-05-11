package events;

import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.time.SUTime.*;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.time.TimeExpression.Annotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.io.IOException;




public class NEREngine {
	
	
	
	private ArrayList<CoreMap> listLocation, listDate, listObject;
	private static AbstractSequenceClassifier<CoreMap> classifier;
	

	/*
    public NEREngine(CoreMap inputSentence) {

    	if (classifier == null)
    		 classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		try 
		{
		    listLocation = new ArrayList<>();
		    listDate = new ArrayList<>();
		    listObject = new ArrayList<>();
		      
		   
		
		   
		    if (true) {
		      List<List<CoreMap> > sentences = classifier.classify(inputSentence);
		      for (List<CoreMap> sentence : sentences)
			      for (CoreMap word : sentence) {
			    	  String tmp = word.get(AnswerAnnotation.class);
			    	  if (tmp.compareTo(LOCATION) == 0)
			        		listLocation.add(word);
			    	  else if (tmp.compareTo(DATE) == 0 || tmp.compareTo(TIME) == 0 ) 
			    		  listDate.add(word);
			    	  
			    	  else if (tmp.compareTo("0") == 0)
			    		  listObject.add(word);			
			      }
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	    System.out.println();
    }
    
    */
	
	public NEREngine(CoreMap inputSentence) {

    	
		try 
		{
		    listLocation = new ArrayList<>();
		    listDate = new ArrayList<>();
		    listObject = new ArrayList<>();
		      
		   
	    
		      
		      for (CoreMap word : inputSentence.get(CoreAnnotations.TokensAnnotation.class))
			      {
			    	  String tmp = word.get(CoreAnnotations.NamedEntityTagAnnotation.class);
			    	  if (tmp.compareTo(LOCATION) == 0)
			        		listLocation.add(word);
			    	  /*
			    	  else if (tmp.compareTo(DATE) == 0 || tmp.compareTo(TIME) == 0 
			    			  || tmp.compareTo(DURATION) == 0 || tmp.compareTo(SET) == 0) {
			    		  listDate.add(word); }
			    		  */
			    		 
			 
			    	  
			    	 
			      }
		      for (CoreMap wordTime : inputSentence.get(TimeAnnotations.TimexAnnotations.class)) {
		    	  
		    	  listDate.add(wordTime);
		      }
		      
		    
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	    System.out.println();
    }
	
	
/*out = classifier.classifyFile("inp.txt");
for (List<CoreMap> sentence : out) {
  for (CoreMap word : sentence) {
    System.out.print(word.word() + '/' + word.get(AnswerAnnotation.class) + ' ');
  }
  System.out.println();
}*/

    
 
    public ArrayList<CoreMap> getListDate() {
		return listDate;
	}
    public ArrayList<CoreMap> getListLocation() {
		return listLocation;
	}
    public ArrayList<CoreMap> getListObject() {
		return listObject;
	}
 
    
    private final String serializedClassifier = "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";
    private final String LOCATION = "LOCATION";
    private final String DATE = "DATE";
    private final String TIME = "TIME";
    private final String DURATION = "DURATION";
    private final String SET = "SET";
    
}