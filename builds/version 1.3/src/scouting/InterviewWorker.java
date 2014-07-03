package scouting;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingWorker;

import resources.DraftClass;
import resources.TeamList;




import main.MainWindow;


public class InterviewWorker extends SwingWorker<Object, Object> {
	
	private DraftClass prospects;
	private enum InterviewResult {IQ, CON, GRE, LOY, PFW, PT, PER, DUR, WE, POP}; 
	private File directory;
	private BufferedWriter interviews;
	
	public InterviewWorker(File f)
	{
		prospects = new DraftClass(); // Generate ratings table for rookie class
		directory = f;
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		conductInterviews();
                
		return null;
	}

	private void conductInterviews() throws IOException
	{
		File files[] = directory.listFiles(); // Get all the files in the directory.
		
		for(File f: files)
		{
			String filename = f.getName();
			
			if (filename.endsWith(".txt"))
			{					
				BufferedReader br = new BufferedReader(new FileReader(f));
				TeamList teamList = new TeamList(); 
				
				String teamName;
				String playerName;
				String str; 
				int count = 0;
			
				teamName = br.readLine(); // read Team Name
				teamName = teamName.trim();	
				
				// skip if file doesn't start with correct team name
				if (!teamList.match(teamName))
					continue; 
				
				// Create a text file with the results for the respective team.
				interviews = new BufferedWriter(new FileWriter("results/" + teamName + ".txt"));
				interviews.append("\n" + teamName.toUpperCase() + "\n\n");	
					
				while ((str = br.readLine()) != null)
				{
					if(str.isEmpty())
						continue;	
									
					playerName = str.trim();
										
					getInterviewResults(playerName); // generate Interview report for this player						
					count++; // keep track of # of players interviewed.
				}	
				br.close();
				interviews.close();
				
				// update the main window as soon as we finish processing the file.
				MainWindow.GetInstance().updateOutput(filename + " -- " + count + "\n");
			}
		}
		MainWindow.GetInstance().updateOutput("\nINTERVIEWS -- DONE\n");
	}
	
	public void getInterviewResults(String name) throws IOException
	{	
		// Error check: Name
		if (!prospects.checkName(name))
		{
			interviews.append("ERROR: Name Not Found! \n --/--\n");
			return;
		}
		
		interviews.append(name + "\n");
		
		int[] interview = prospects.getInterview(name);
		int i=0;
		
		for (InterviewResult category : InterviewResult.values())
		{
			interviews.append(category + ": " + interview[i] + "\n");
			i++;
		}
		interviews.append("\n");
	}
}

