
import java.io.*;
import java.time.LocalDateTime;

import java.util.*;

import javax.swing.JTable;

import edu.stanford.nlp.coref.CorefCoreAnnotations;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.time.Timex;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.time.SUTime.*;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.*;

import events.*;

/** This class demonstrates building and using a Stanford CoreNLP pipeline. */
public class StanfordCoreNlpDemo {

  /** Usage: java -cp "*" StanfordCoreNlpDemo [inputFile [outputTextFile [outputXmlFile]]] */
  
  
  public static void main(String[] args) throws IOException {
    // set up optional output files
    PrintWriter out;
    if (args.length > 1) {
      out = new PrintWriter(args[1]);
    } else {
      out = new PrintWriter(System.out);
    }
    PrintWriter xmlOut = null;
    if (args.length > 2) {
      xmlOut = new PrintWriter(args[2]);
    }

    // Create a CoreNLP pipeline. To build the default pipeline, you can just use:
    //   StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    // Here's a more complex setup example:
    //   Properties props = new Properties();
    //   props.put("annotators", "tokenize, ssplit, pos, lemma, ner, depparse");
    //   props.put("ner.model", "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");
    //   props.put("ner.applyNumericClassifiers", "false");
    //   StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

    // Add in sentiment
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner,parse");//, dcoref, sentiment");
  
   // props.setProperty("ner.useSUTime", "0");
  
    
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    
    
    //----------------------SUTime-section-------------------------------------------
    //String defs_sutime = "/home/namxuan/workspace/eclipse-workspace/mebong/MeBong/sutime/defs.sutime.txt";
    String defs_sutime = "./sutime/defs.sutime.txt";
	String holiday_sutime = "./sutime/english.holidays.sutime.txt";
	String _sutime = "./sutime/english.sutime.txt";
	
	Properties propSUTime = new Properties();
	String sutimeRules = defs_sutime + "," + holiday_sutime
				+ "," + _sutime;
	propSUTime.setProperty("sutime.rules", sutimeRules);
	propSUTime.setProperty("sutime.binders", "0");
	propSUTime.setProperty("sutime.markTimeRanges", "true");
	propSUTime.setProperty("sutime.includeRange", "true");
	pipeline.addAnnotator(new TimeAnnotator("sutime", propSUTime));
	
    
    //----------------------end-SUTime-section---------------------------------------
    
    // 
    
    // Initialize an Annotation with some text to be annotated. The text is the argument to the constructor.
    Annotation annotation;
    
    if (args.length == 0) 
      annotation = new Annotation("Kosgi Santosh sent an email to Stanford University. He didn't get a reply.");
    
    ArrayList<String> listVerb = new ArrayList<>();
	Annotation sent = new Annotation(IOUtils.slurpFileNoExceptions(verbfile));
	for(String word :  sent.toString().split(" "))
	{
		listVerb.add(word);
	}
	
	EnterNews enter = new EnterNews();
	System.out.println();
	System.out.println("The best title is: " + enter.bestTitle());
    System.out.println();
    //for new
    //can lay ket qua cua ham duoi day vao mot bien nao do
    //ket qua gom co: cai bang(tableEvent can co them title)
	ArrayList<TableEvent> listTableEvent = new ArrayList<>();
    for (File news : enter.getListInput()) {
    	TableEvent tableEvent = annotationProcessing(news.getName(), pipeline, listVerb);
    	listTableEvent.add(tableEvent);
    	tableEvent.printTableEvent();
    }
    
    /*
    for (TableEvent tableEvent:listTableEvent) {
    	System.out.println(tableEvent.getTitle());
    	tableEvent.printEvent();
    	System.out.println(" ");
    	
    	
    }*/
    
    generateFinalTable(listTableEvent);
    
    // run all the selected Annotators on this text
    IOUtils.closeIgnoringExceptions(out);
    IOUtils.closeIgnoringExceptions(xmlOut);
  }
  
  
  private static TableEvent annotationProcessing(String filename, StanfordCoreNLP pipeline, ArrayList<String> listVerb) {
	  	Annotation annotation = new Annotation(IOUtils.slurpFileNoExceptions(INPUT_PATH + filename));
	  	annotation.set(CoreAnnotations.DocDateAnnotation.class, LocalDateTime.now().toString());
	    pipeline.annotate(annotation);   
	    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
	    TableEvent tableEvent = new TableEvent(); 
	    tableEvent.setTitle(filename);
	    if (sentences != null && ! sentences.isEmpty())
		    for (CoreMap sentence : sentences)
		    {			      
			      Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
			      SentenceAnalysis senAna = new SentenceAnalysis(tree, listVerb,sentence);
			      senAna.analysisSentence();
			      /*for (CoreMap token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
			    	//  System.out.println(token.toShorterString());
			    	  //System.out.println(token.get(CoreAnnotations.An.class));
			    	  
			      }*/
			      //sentence.get(TreeCoreAnnotations.TreeAnnotation.class).pennPrint();
			      
			      Event event = senAna.getEvent(); //(2)
			      ArrayList<CoreMap> sk = event.getSKID();
			      if (sk != null && !sk.isEmpty())
			    	tableEvent.getTableEvent().add(senAna.getEvent());      
		     }
		    
		  //tableEvent.printEvent();
		  //GroupList groupList = new GroupList(tableEvent);
		  //ArrayList<Event> bestEvents = groupList.getBest();
		  //printBestEvent(bestEvents);
		  
		  //Title title = new Title();
		  //System.out.println("Best title: " + title.getBestTitle());
		 // EnterNews enter = new EnterNews();
		  //enter.printFilesName();
	    return tableEvent;
		  
  }
  private static void generateFinalTable(ArrayList<TableEvent> listTableEvent) {
	  FinalTableEvent finalTable = new FinalTableEvent(listTableEvent);
	  
	  
  }
  private static final String verbfile = "verbList.txt";
  private static final String INPUT_PATH = "text/input/";
  
  public  String coreMapToString(CoreMap cm) {
	  String res = cm.get(CoreAnnotations.TextAnnotation.class);
	  if (res == null)
		  res = cm.toString();
	  return res;
  }
 
}
