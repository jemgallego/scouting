package scouting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.SwingWorker;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import resources.DraftClass;
import resources.TeamList;

import main.MainWindow;

public class ScoutingWorker extends SwingWorker<Object, Object> {
	private Hashtable<String, Integer> scoutedPlayers = new Hashtable<String,Integer>();
	private Hashtable<String, Integer> teamPoints = new Hashtable<String,Integer>();

	private enum Rating {FGD, FGI, FGJ, FT, FG3, SCR, PAS, HDL, ORB, DRB, BLK, STL, DRFL, DEF, DIS, IQ};
	
	private static DraftClass prospects;
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
		prospects = new DraftClass(); // Generate ratings table for draft class	
		File files[] = directory.listFiles(); // Get all the files in the directory.
		TeamList teamList = new TeamList(); 
		
		BufferedReader pointsReader = new BufferedReader(new FileReader("files/points.txt"));
		String team;
		
		try 
		{
			File inputWorkbook = new File("files/tracker.xls");
			Workbook w  = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			
			// import the contents of tracker.xls into our scoutedPlayers table.
			for(int i = 1; i < sheet.getRows(); i++)
			{
				Cell firstName = sheet.getCell(0,i);
				Cell lastName = sheet.getCell(1,i);
				String name = firstName.getContents() + " " + lastName.getContents();
				
				Integer total = Integer.parseInt(sheet.getCell(2,i).getContents());
				
				scoutedPlayers.put(name, total);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			MainWindow.GetInstance().updateOutput("\n===== START ERROR MESSAGE =====\n\n" +
					"ERROR: Can't find tracker.xls file!\n\n" +
					"Check to make sure tracker.xls is in the files folder.\n\n" + 
					"\n=====  END ERROR MESSAGE  =====\n\n" );
		} 
					
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
				String name;
				String str; 
				int count = 0;
			
				teamName = br.readLine(); // read Team Name
				teamName = teamName.trim();	
				
				// skip if file doesn't start with correct team name
				if (!teamList.match(teamName))
					continue; 
				
				// update the team's point counter
				int ctr = teamPoints.get(teamName) + 1;
				teamPoints.put(teamName, ctr);

				// Create a scouting report for the respective team.
				reports = new BufferedWriter(new FileWriter("results/" + teamName + ".txt"));
				reports.append("\n" + teamName.toUpperCase() + "\n\n");		
					
				while ((str = br.readLine()) != null)
				{
					if(str.isEmpty())
						continue;	
									
					name = str.trim();
					
					// check if player has been scouted before
					if(scoutedPlayers.containsKey(name))
					{
						int num = scoutedPlayers.get(name) + 1;
						scoutedPlayers.put(name,num);
					} // else check if player name exists. 
					else if (prospects.checkName(name))
					{
						scoutedPlayers.put(name,1);
					}
					
					generateReport(name); // generate scouting report for this player						
					count++; // keep track of total scouting points used.
				}	
				br.close();
				reports.close();
					
				// update the main window as soon as we finish processing the file.
				MainWindow.GetInstance().updateOutput(filename + " -- " + count + "\n");
			}
		}
		
		// Update Tracker File 
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(scoutedPlayers.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				Integer i1 = (Integer) e1.getValue();
				Integer i2 = (Integer) e2.getValue();
				return i2.compareTo(i1);
			}
		});
		
		for(Map.Entry<String, Integer> e : list)
		{
			tracker.append(e.getKey() + " " + e.getValue() + "\n");
		}
		tracker.close();
		
		// Update Points File
		Set<String> s2 = teamPoints.keySet();
		Iterator<String> it2 = s2.iterator();
		
		while(it2.hasNext())
		{
			String tp = it2.next();
			points.append(tp + " " + teamPoints.get(tp) + "\n");
		}
		points.close();
		
		MainWindow.GetInstance().updateOutput("\nSCOUTING -- DONE\n");
	}
	
	public void generateReport(String name) throws IOException
	{
		// Error check: Name
		if (!prospects.checkName(name))
		{
			reports.append("ERROR: Name Not Found! \n --/--\n");
			return;
		}
		
		reports.append(name + "\n");
		
		int[] rating = prospects.getScoutingReport(name);
		int i = 0;
		
		for (Rating category : Rating.values())
		{
			if (category == Rating.FGD || category == Rating.FGI || category == Rating.FGJ || 
				category == Rating.FT || category == Rating.FG3)
			{
				reports.append(category + ": " + rating[i] + "\n");
			}
			else
			{
				reports.append(category + ": " + rating[i] + "/" + rating[i+1] + "\n");
				i++;
			}
			i++;
		}
		reports.append("\n");
	}
}

