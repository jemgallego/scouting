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

import main.MainWindow;

public class TrackerWorker extends SwingWorker<Object, Object>{
	private static Hashtable<String, Integer> scoutedPlayers = new Hashtable<String,Integer>(); // name, # of times scouted
	
	public TrackerWorker() throws IOException
	{
		File f = new File("files/tracker.txt");
		if (!f.exists()) createFile(f);
		
		BufferedReader reader = new BufferedReader(new FileReader("files/tracker.txt"));
		String player;
		
		// register all scouted players to a Hashtable
		while((player = reader.readLine()) != null)
		{
			StringTokenizer st = new StringTokenizer(player," ");
			String name = st.nextToken() + " " + st.nextToken();
			scoutedPlayers.put(name, Integer.parseInt(st.nextToken()));
		}	
		reader.close();
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		displayTracker();
        
        //set the status text in the main window back to idle
		MainWindow.GetInstance().SetStatusIdle();
        
		return null;
	}
	
	public void createFile(File f)
	{
		// if tracker.txt does not exist, create a blank one.
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			MainWindow.GetInstance().updateOutput("\n===== START ERROR MESSAGE =====\n\n" +
					"ERROR: Creating tracker.txt file FAILED!\n\n" +
					"\n=====  END ERROR MESSAGE  =====\n\n" );
		}
	}
	
	public void addPoint(String name)
	{
		// check if player has been scouted before
		if(scoutedPlayers.containsKey(name))
		{
			int num = scoutedPlayers.get(name) + 1;
			scoutedPlayers.put(name,num);
		}  
		else
		{
			scoutedPlayers.put(name,1);
		}
	}
	
	public int getTimesScouted(String name)
	{
		int num = 0; 
			
		if(scoutedPlayers.get(name) == null)
			num = 0;
		else
			num = scoutedPlayers.get(name);
		
		return num;
	}
		
	public void displayTracker()
	{
		// convert table to a list and sort
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(scoutedPlayers.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				Integer i1 = (Integer) e1.getValue();
				Integer i2 = (Integer) e2.getValue();
				return i2.compareTo(i1);
			}
		});
		
		// display result
		MainWindow.GetInstance().updateOutput("Tracker Report\n");
		
		int i=1;
		for(Map.Entry<String, Integer> e : list)
		{
			MainWindow.GetInstance().updateOutput(i + ". " + e.getKey() + " " + e.getValue() + "\n");
			i++;
		}
	}
	
	public void saveTracker() throws IOException 
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("files/tracker.txt"));
		
		// Update Tracker File 
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(scoutedPlayers.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				String i1 = e1.getKey();
				String i2 = e2.getKey();
				return i1.compareTo(i2);
			}
		});
		
		for(Map.Entry<String, Integer> e : list)
		{
			writer.append(e.getKey() + " " + e.getValue() + "\n");
		}
		writer.close();
	}

}
