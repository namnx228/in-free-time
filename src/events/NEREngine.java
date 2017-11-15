package events;

import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;



public class NEREngine {
	
	private String inputSentence;
	
	private ArrayList<CoreLabel> listLocation, listDate, listObject;

    public NEREngine(String inputSentence) {

		try 
		{
		    listLocation = new ArrayList<>();
		    listDate = new ArrayList<>();
		    listObject = new ArrayList<>();
		      
		    AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		
		   
		    if (true) {
		      List<List<CoreLabel> > sentences = classifier.classify(inputSentence);
		      for (List<CoreLabel> sentence : sentences)
			      for (CoreLabel word : sentence) {
			     
			    	  if (word.get(AnswerAnnotation.class).compareTo(LOCATION) == 0){
			        		listLocation.add(word);
			    	  }
			    	  else if (word.get(AnswerAnnotation.class).compareTo(DATE) == 0)
			    		  listDate.add(word);
			    	  else if (word.get(AnswerAnnotation.class).compareTo("0") == 0)
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
/*out = classifier.classifyFile("inp.txt");
for (List<CoreLabel> sentence : out) {
  for (CoreLabel word : sentence) {
    System.out.print(word.word() + '/' + word.get(AnswerAnnotation.class) + ' ');
  }
  System.out.println();
}*/

    
 
    public ArrayList<CoreLabel> getListDate() {
		return listDate;
	}
    public ArrayList<CoreLabel> getListLocation() {
		return listLocation;
	}
    public ArrayList<CoreLabel> getListObject() {
		return listObject;
	}
    
    private final String serializedClassifier = "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";
    private final String LOCATION = "LOCATION";
    private final String DATE = "DATE";
    
}