import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingWorker;

public class ScoutingWorker extends SwingWorker<Object, Object> {
	
	private static RookieClass rookies;
	private enum ShotSelection {Dunk, Post, Drive, Jumper, Threes};
	private enum Scoring {FGJ, FT, FGI, FGD, FG3, SCR};
	private enum Offense {PAS, HDL, ORB, DRFL, IQ}; 
	private enum Defense {DRB, BLK, STL, DEF, DIS};
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
		rookies = new RookieClass(); // Generate ratings table for rookie class	
		File files[] = directory.listFiles(); // Get all the files in the directory.
		
		for(File f: files)
		{
			String filename = f.getName();
			
			if (filename.endsWith(".txt"))
			{	
				BufferedReader br = new BufferedReader(new FileReader(f));
				BufferedReader br2 = new BufferedReader(new FileReader("tracker/Tracker.txt")); 
				BufferedWriter bw = new BufferedWriter(new FileWriter("tracker/Tracker.txt",true));  
			
				ArrayList<String> tracker = new ArrayList<String>();
				String scoutedPlayer;
				Boolean scouted;
				
				// register all the scouted player into the tracker ArrayList.
				while((scoutedPlayer = br2.readLine()) != null)
					tracker.add(scoutedPlayer);
				
				// Start reading the scouting file
				String str; 
				String teamName;
				int count = 0;
			
				teamName = br.readLine(); // read Team Name
				
				// Create a scouting report for the respective team.
				reports = new BufferedWriter(new FileWriter("scouting reports/" + teamName + ".txt"));
				reports.append("\n" + teamName.toUpperCase() + "\n\n");		
				
				
				while ((str = br.readLine()) != null)
				{
					String validator = teamName + ": " + str;
						
					if(tracker.contains(validator)) // check if the team already scouted the player.
							scouted = true;
					else {
						tracker.add(validator);
						bw.append(validator + "\n");
						scouted = false;
					}
						
					String[] splits = str.split(" - ");
					
					String name = splits[0].replaceAll("\\s+", " "); // Player Name
					String area = splits[1].replaceAll("\\s+", " "); // Scouting Area
					
					name = name.trim();
					area = area.trim();
					
					getScoutingReport(name, area, scouted);
				
					reports.append("\n");
					count++; // keep track of total scouting points used.
				}	
				br.close();
				br2.close();
				bw.close();
				reports.close();
				
				// update the main window as soon as we finish processing the file.
				MainWindow.GetInstance().updateOutput(filename + " -- " + count + "\n");
			}
		}
		
		// put a marker at the end of the tracker file to mark the scouting period. 
		BufferedWriter bw2 = new BufferedWriter(new FileWriter("tracker/Tracker.txt",true)); 
		bw2.append("---END " + directory.getName().toUpperCase() + "---\n\n" );
		bw2.close();
	}
	
	public void getScoutingReport(String name, String area, Boolean scouted) throws IOException
	{
		int[][] rating = rookies.get(name); // get the player's ratings
		int i;
		
		// Error check: Name
		if (rating == null) { 
			reports.append("ERROR: Name Not Found! \n --/--\n");
			return;
		}
		reports.append(name + "\n");
		
		// Error check: Scouting Area
		if (area.equals("Shot Selection")) {
			i = 0;  
			for (ShotSelection category : ShotSelection.values())
			{
				reports.append(category + ": " + rating[0][i] +"\n");
				i++;
			}
		}
		else if (area.equals("Scoring")) {
			i = 0;
			for (Scoring category : Scoring.values())
			{
				int n = rating[1][i];
				
				// special cases, the grades are curved
				if (category.equals(Scoring.FGJ)) 
					n = (int)((100 + ((0-100)/(25-55))*(n-55)));	
				else if (category.equals(Scoring.FGI)) 
					n = (int)((100 + ((0-100)/(30-60))*(n-60)));
				else if (category.equals(Scoring.FG3)) 
					n = (int)((100 + ((0-100)/(0-45))*(n-45)));
				
				String current = getGrade(n);
				
				if (!scouted)
					reports.append(category + ": " + current + "\n");
				else {
					n = rating[2][i];
					
					// special cases, the grades are curved
					if (category.equals(Scoring.FGJ)) 
						n = (int)(100 + ((0-100)/(25-55))*(n-55));	
					else if (category.equals(Scoring.FGI)) 
						n = (int)(100 + ((0-100)/(30-60))*(n-60));
					else if (category.equals(Scoring.FG3)) 
						n = (int)(100 + ((0-100)/(0-45))*(n-45));
					
					String potential = getGrade(n);      
					reports.append(category + ": " + current + "/" + potential + "\n");			
				}
				i++;
			}
		}
		else if (area.equals("Offense")) {
			i = 6;
			for (Offense category : Offense.values())
			{
				int rtg = rating[1][i];
				
				// special case, the grades are curved
				if(category.equals(Offense.DRFL))
					rtg = rtg * 4;
				
				String current = getGrade(rtg);
				
				if (!scouted)
					reports.append(category + ": " + current + "\n");
				else {	
					rtg = rating [2][i];
					
					// special case, the grades are curved
					if(category.equals(Offense.DRFL))
						rtg = rating[2][i] * 4;			
						
					String potential = getGrade(rtg);      
					reports.append(category + ": " + current + "/" + potential + "\n");			
				}
				i++;
			}
		}
		else if(area.equals("Defense")) {
			i = 11;
			for (Defense category : Defense.values())
			{
				String current = getGrade(rating[1][i]);
				
				if (!scouted)
					reports.append(category + ": " + current + "\n");
				else {	
					String potential = getGrade(rating[2][i]);      
					reports.append(category + ": " + current + "/" + potential + "\n");			
				}
				i++;
			}
		}
		else
			reports.append("--Scouting Area Invalid!-- \n");
	}
	
	public static String getGrade(int rating)
	{
		String grade;
		
		if(rating <= 100 && rating >= 90)
			grade = "A+";
		else if(rating <= 89 && rating >= 80)
			grade = "A-";
		else if(rating <= 79 && rating >= 70)
			grade = "B+";
		else if(rating <= 69 && rating >= 60)
			grade = "B-";
		else if(rating <= 59 && rating >= 50)
			grade = "C+";
		else if(rating <= 49 && rating >= 40)
			grade = "C-";
		else if(rating <= 39 && rating >= 30)
			grade = "D+";
		else if(rating <= 29 && rating >= 20)
			grade = "D-";
		else if(rating <= 19 && rating >= 0)
			grade = "F";
		else 
			grade = "--";
			
		return grade;
	}
}

