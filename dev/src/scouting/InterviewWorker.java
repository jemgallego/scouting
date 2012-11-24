package scouting;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingWorker;

import draftClass.DraftClass;



import main.MainWindow;


public class InterviewWorker extends SwingWorker<Object, Object> {
	
	private static DraftClass rookies;
	private enum InterviewResult {Dunk, Post, Drive, Jumper, Three, CON, GRE, LOY, PFW, PT, PER, DUR, WE, POP, IQ}; 
	private File directory;
	private BufferedWriter interviews;
	
	public InterviewWorker(File f)
	{
		directory = f;
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		conductInterviews();
        
        //set the status text in the main window back to idle
		MainWindow.GetInstance().SetStatusIdle();
        
		return null;
	}

	private void conductInterviews() throws IOException
	{
		rookies = new DraftClass(); // Generate ratings table for rookie class
		File files[] = directory.listFiles(); // Get all the files in the directory.
		
		for(File f: files)
		{
			String filename = f.getName();
			
			if (filename.endsWith(".txt"))
			{	
				BufferedReader br = new BufferedReader(new FileReader(f));
				
				// Start reading the interview file
				String str; 
				String teamName;
				int count = 0;
				
				teamName = br.readLine(); // read Team Name

				// Create a text file with the results for the respective team.
				interviews = new BufferedWriter(new FileWriter("results/" + teamName + ".txt"));
				interviews.append("\n" + teamName.toUpperCase() + "\n\n");
				
				while ((str = br.readLine()) != null)
				{						
					getInterviewResults(str);
					interviews.append("\n");
					
					count++; // keep track of # of players interviewed
				}
				br.close();
				interviews.close();
				MainWindow.GetInstance().updateOutput(filename + " -- " + count + "\n");
			}
		}
		MainWindow.GetInstance().updateOutput("\nINTERVIEWS -- DONE\n");
	}
	
	public void getInterviewResults(String name) throws IOException
	{	
		// Error check: Name
		if (!rookies.checkName(name))
		{
			interviews.append("ERROR: Name Not Found! \n --/--\n");
			return;
		}
		
		int[] interview = rookies.getInterview(name);
		int i=0;
		
		interviews.append(name + "\n");
		
		for (InterviewResult category : InterviewResult.values())
		{
			interviews.append(category + ": " + interview[i] + "\n");
			i++;
		}
	}
}

