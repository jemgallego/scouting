package scouting;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingWorker;

import func.TotalScoreRating;

import resources.DraftClass;
import resources.TeamList;




import main.MainWindow;


public class InterviewWorker extends SwingWorker<Object, Object> {
	
	private DraftClass draftclass;
	private enum InterviewResult {IQ, CON, GRE, LOY, PFW, PT, PER, DUR, WE, POP}; 
	private File directory;
	private BufferedWriter interviews;
	
	public InterviewWorker(File f)
	{
		draftclass = new DraftClass(); // Generate ratings table for rookie class
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
		TeamList teamList = new TeamList();
		TotalScoreRating scoreRatings = new TotalScoreRating();
		
		for(File f: files)
		{
			String filename = f.getName();
			
			if (filename.endsWith(".txt"))
			{					
				BufferedReader br = new BufferedReader(new FileReader(f));
				
				String teamName;
				String playerName;
				String str; 
				int count = 0;
			
				teamName = br.readLine(); // read Team Name
				teamName = teamName.trim();	
				teamName = teamName.replaceAll("\\s++", " ");
				
				teamName = teamList.getTeamName(teamName);
				
				// skip if file doesn't start with correct team name
				if (teamName == null)
				{
					MainWindow.GetInstance().updateOutput(filename + " -- ERROR: Team Name\n");
					continue; 
				}
				
				// Create an interview report for the respective team.
				interviews = new BufferedWriter(new FileWriter("results/" + teamName + ".txt"));
				interviews.append("\n" + teamName.toUpperCase() + "\n\n");	
					
				while ((str = br.readLine()) != null)
				{
					if(str.isEmpty())
						continue;	
									
					playerName = str.trim();
					playerName = playerName.replaceAll("\\s++", " ");
					
					// Error check: Name
					if (!draftclass.checkName(playerName))
					{
						interviews.append("ERROR: Name Not Found! \n --/--\n\n");
						continue;
					}
					
					int[] interview = draftclass.getInterview(playerName); 
					appendInterviewReport(playerName, interview); // add interview report for this player
					
					int tsi = scoreRatings.calculateTSI(playerName, interview);
					scoreRatings.addTSI(playerName, tsi);
					
					count++; // keep track of # of players interviewed.
				}	
				br.close();
				interviews.close();
				
				// update the main window as soon as we finish processing the file.
				MainWindow.GetInstance().updateOutput(filename + " -- " + count + "\n");
			}
		}
		
		scoreRatings.saveRatings();
		
		MainWindow.GetInstance().updateOutput("\nINTERVIEWS -- DONE\n");
	}
	
	public void appendInterviewReport(String name, int[] interview) throws IOException
	{	
		interviews.append(name + "\n");
		
		int i=0;
		
		for (InterviewResult category : InterviewResult.values())
		{
			interviews.append(category + ": " + interview[i] + "\n");
			i++;
		}
		interviews.append("\n");
	}
}

