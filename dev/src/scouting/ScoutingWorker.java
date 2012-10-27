package scouting;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.SwingWorker;

import prospects.Prospects;


import main.MainWindow;

public class ScoutingWorker extends SwingWorker<Object, Object> {
	private Hashtable<String, Integer> scoutedPlayers = new Hashtable<String,Integer>();
	private Hashtable<String, Integer> teamPoints = new Hashtable<String,Integer>();
	private enum ShotSelection {Dunk, Post, Drive, Jumper, Threes};
	private enum Rating {FGD, FGI, FGJ, FG3, FT, SCR,
		PAS, HDL, ORB, DRB, DEF, BLK, STL, DRFL, DIS, IQ};
		
	private static Prospects rookies;
	private File directory;
	private BufferedWriter reports;

	public ScoutingWorker(File f)
	{
		directory = f;
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		conductScouting();
        
        //set the status text in the main window back to idle
		MainWindow.GetInstance().SetStatusIdle();
        
		return null;
	}
	
	private void conductScouting() throws IOException
	{
		rookies = new Prospects(); // Generate ratings table for rookie class	
		File files[] = directory.listFiles(); // Get all the files in the directory.
		
		BufferedReader trackerReader = new BufferedReader(new FileReader("files/tracker.txt"));
		BufferedReader pointsReader = new BufferedReader(new FileReader("files/points.txt"));
		String player;
		String team;
		
		// import the contents of tracker.txt into our scoutedPlayers table.
		while((player = trackerReader.readLine()) != null)
		{
			StringTokenizer st = new StringTokenizer(player," ");
			String name = st.nextToken() + " " + st.nextToken();
			scoutedPlayers.put(name, Integer.parseInt(st.nextToken()));
		}	
		trackerReader.close();
		
		// import the contents of points.txt into our teamPoints table.
		while((team = pointsReader.readLine()) != null)
		{
			StringTokenizer st = new StringTokenizer(team," ");
			int tokens = st.countTokens();
			String name = "";
			
			for(int i = 1; i < tokens; i++)
			{
				name += st.nextToken() + " ";
			}
			name = name.trim();

			teamPoints.put(name, Integer.parseInt(st.nextToken()));
		}	
		pointsReader.close();
		
		BufferedWriter tracker = new BufferedWriter(new FileWriter("files/tracker.txt"));
		BufferedWriter points = new BufferedWriter(new FileWriter("files/points.txt"));
		
		for(File f: files)
		{
			String filename = f.getName();
			
			if (filename.endsWith(".txt"))
			{	
				BufferedReader br = new BufferedReader(new FileReader(f));
				
				String teamName;
				String str; 
				int count = 0;
			
				teamName = br.readLine(); // read Team Name
				teamName = teamName.trim();	
				
				if (teamPoints.containsKey(teamName))
				{		
					int num = teamPoints.get(teamName) + 1;
					teamPoints.put(teamName,num);
				}
							
				// Create a scouting report for the respective team.
				reports = new BufferedWriter(new FileWriter("results/" + teamName + ".txt"));
				reports.append("\n" + teamName.toUpperCase() + "\n\n");		
					
				while ((str = br.readLine()) != null)
				{
					if(str.isEmpty())
						break;	
									
					str = str.trim();
					
					if(scoutedPlayers.containsKey(str))
					{
						int num = scoutedPlayers.get(str) + 1;
						scoutedPlayers.put(str,num);
					}
					else
						scoutedPlayers.put(str,1);
					
					getScoutingReport(str);
						
					reports.append("\n");
					count++; // keep track of total scouting points used.
				}	
				br.close();
				reports.close();
					
				// update the main window as soon as we finish processing the file.
				MainWindow.GetInstance().updateOutput(filename + " -- " + count + "\n");
			}
		}
		
		Set<String> s = scoutedPlayers.keySet();
		Iterator<String> it = s.iterator();
		
		while(it.hasNext())
		{
			String p = it.next();
			tracker.append(p + " " + scoutedPlayers.get(p) + "\n");
		}
		tracker.close();
		
		Set<String> s2 = teamPoints.keySet();
		Iterator<String> it2 = s2.iterator();
		
		while(it2.hasNext())
		{
			String p = it2.next();
			points.append(p + " " + teamPoints.get(p) + "\n");
		}
		points.close();
		
		MainWindow.GetInstance().updateOutput("\nSCOUTING -- DONE\n");
	}
	
	public void getScoutingReport(String name) throws IOException
	{
		// Error check: Name
		if (!rookies.checkName(name))
		{
			reports.append("ERROR: Name Not Found! \n --/--\n");
			return;
		}
		
		reports.append(name + "\n");
		
		int[] rating = rookies.getScoutingReport(name);
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
	}
}

