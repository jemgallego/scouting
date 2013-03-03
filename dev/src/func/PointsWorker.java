package func;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.SwingWorker;

import resources.TeamList;

import main.MainWindow;

public class PointsWorker extends SwingWorker<Object, Object>{
	private static Hashtable<String, Integer> teamPoints = new Hashtable<String,Integer>(); // team, scouting points
	
	public PointsWorker() throws IOException
	{
		File f = new File("files/points.txt");
		if (!f.exists()) createFile(f);
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String team;
		
		// import the contents of points.txt into our teamPoints table.
		while((team = reader.readLine()) != null)
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
		reader.close();
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		displayPoints();
             
		return null;
	}
	
	public void createFile(File f)
	{		
		// if points.txt does not exist, create a blank one.
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			
			// Add list of teams with points equal to 0.
			TeamList tl = new TeamList();
			ArrayList<String> teams = tl.getTeams();
			
			for(int i=0; i< teams.size(); i++ )
				writer.append(teams.get(i) + " 0\n");
				
			writer.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			MainWindow.GetInstance().updateOutput("\n===== START ERROR MESSAGE =====\n\n" +
					"ERROR: Creating points.txt file FAILED!\n\n" +
					"\n=====  END ERROR MESSAGE  =====\n\n" );
		}

	}
	
	public void addPoint(String team)
	{
		int ctr = teamPoints.get(team) + 1;
		teamPoints.put(team, ctr);
	}
		
	public void displayPoints()
	{
		// convert table to a list and sort
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(teamPoints.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				Integer i1 = (Integer) e1.getValue();
				Integer i2 = (Integer) e2.getValue();
				return i2.compareTo(i1);
			}
		});
		
		MainWindow.GetInstance().updateOutput("Scouting Points\n");
		
		int i=1;
		for(Map.Entry<String, Integer> e : list)
		{
			MainWindow.GetInstance().updateOutput(i + ". " + e.getKey() + " " + e.getValue() + "\n");
			i++;
		}
	}
	
	public void savePoints() throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("files/points.txt"));
		// Update Points File
		List<Map.Entry<String, Integer>> list2 = new ArrayList<Map.Entry<String, Integer>>(teamPoints.entrySet());
		Collections.sort(list2, new Comparator<Map.Entry<String, Integer>>() {
			public int compare (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				String i1 = e1.getKey();
				String i2 = e2.getKey();
				return i1.compareTo(i2);
			}
		});
		
		for(Map.Entry<String, Integer> e : list2)
		{
			writer.append(e.getKey() + " " + e.getValue() + "\n");
		}
		writer.close();
	}
}
