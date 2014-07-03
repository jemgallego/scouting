package scouting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingWorker;

import func.PointsWorker;
import func.TotalScoreRating;
import func.TrackerWorker;

import resources.DraftClass;
import resources.TeamList;

import main.MainWindow;

public class ScoutingWorker extends SwingWorker<Object, Object> {
	private enum ShotSelection {Dunk, Post, Drive, Jumper, Threes};
	private enum Rating {FGD, FGI, FGJ, FT, FG3, SCR, PAS, HDL, ORB, DRB, BLK, STL, DRFL, DEF, DIS, IQ};
	
	private DraftClass prospects;
	private File directory;
	private BufferedWriter reports;

	public ScoutingWorker(File f)
	{
		prospects = new DraftClass(); // Generate ratings table for draft class	
		directory = f;
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		conductScouting();
		return null;
	}
	
	private void conductScouting() throws IOException
	{	
		File files[] = directory.listFiles(); // Get all the files in the directory.
		TeamList teamList = new TeamList(); 
		PointsWorker points = new PointsWorker();
		TrackerWorker tracker = new TrackerWorker();
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
				
				// update the team's point counter
				points.addPoint(teamName);

				// Create a scouting report for the respective team.
				reports = new BufferedWriter(new FileWriter("results/" + teamName + ".txt"));
				reports.append("\n" + teamName.toUpperCase() + "\n\n");		
					
				while ((str = br.readLine()) != null)
				{
					if(str.isEmpty())
						continue;	
									
					playerName = str.trim();
					playerName = playerName.replaceAll("\\s++", " ");
					
					if (prospects.checkName(playerName))
						tracker.addPoint(playerName); // update the player's point counter
					else
					{
						reports.append("ERROR: Name Not Found! \n --/--\n\n");
						continue;
					}
					
					int[] rating = prospects.getScouting(playerName); // get scouting deviation for this player.
					
					appendScoutingReport(playerName, rating); // add scouting report for this player to the file.
					
					int pos = prospects.getPosition(playerName);
					int tsr = scoreRatings.calculateTSR(playerName, rating, pos);
					
					scoreRatings.updateTSR(playerName, tsr);
					
					count++; // keep track of total scouting points used.
				}	
				br.close();
				reports.close();
					
				// update the main window as soon as we finish processing the file.
				MainWindow.GetInstance().updateOutput(filename + " -- " + count + "\n");
			}
		}
		
		tracker.saveTracker();
		points.savePoints();
		scoreRatings.saveRatings();
		
		MainWindow.GetInstance().updateOutput("\nSCOUTING -- DONE\n");
	}
	
	private void appendScoutingReport(String name, int[] rating) throws IOException
	{
		
		reports.append(name + "\n");
		
		int i = 0;
		
		for (ShotSelection category : ShotSelection.values())
		{
			reports.append(category + ": " + rating[i] + "\n");
			i++;
		}
		
		for (Rating category : Rating.values())
		{
			reports.append(category + ": " + rating[i] + "/" + rating[i+1] + "\n");
			i+=2;
		}
		reports.append("\n");
	}
}

