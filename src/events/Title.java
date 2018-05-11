package events;

import java.util.ArrayList;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.pipeline.Annotation;

public class Title {
	private String bestTitle;
	private ArrayList<String> listTitle;
	public Title() {
		bestTitle = "";
		listTitle = new ArrayList<>();
		Annotation doc = new Annotation(IOUtils.slurpFileNoExceptions(TITLE));
		for (String title : doc.toString().split("\n")) {
			if (title.length() > bestTitle.length()) {
				bestTitle = title;
			}
		}
	}
	public String getBestTitle() {
		return bestTitle;
	}

	
	private final String TITLE = "title.txt";
}
