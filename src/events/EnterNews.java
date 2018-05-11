package events;

import java.io.File;
import java.util.ArrayList;

public class EnterNews {
	private ArrayList<File> listInput;
	public EnterNews() {
		listInput = new ArrayList<>();
		File folder = new File(INPUTFOLDER);
		File[] listFile = folder.listFiles();
		for (File f : listFile) {
			listInput.add(f);
		}
		
		
	}
	public ArrayList<File> getListInput() {
		return listInput;
	}
	public void printFilesName() {
		for (File file : listInput) {
			System.out.println("Filename is:" + file.getName());
		}
	}
	public String bestTitle() {
		String bestTitle = "";
		for (File file : listInput) {
			if (file.getName().compareTo(bestTitle) > 0 )
				bestTitle = file.getName();
		}
		return bestTitle;
	}
	
	private String INPUTFOLDER = "text/input";
}
