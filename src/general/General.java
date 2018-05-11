package general;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.time.SUTime.Time;
import edu.stanford.nlp.util.CoreMap;

public class General {
	public  static String coreMapToString(CoreMap cm) {
		  String res = cm.get(CoreAnnotations.TextAnnotation.class);
		  if (res == null)
			  res = cm.toString();
		  return res;
	  }
	public static String timeToString(Time time) {
		try {
			if(time.getInterval() == null)
				return time.getTime().toString();
			return time.getInterval().first().toString();
		}
		catch (NullPointerException e) {
			return "";
		}
		
	}
}
