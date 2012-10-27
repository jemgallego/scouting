package tracker;
import java.io.BufferedReader;
import java.io.FileReader;
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
	private Hashtable<String, Integer> scoutedPlayers = new Hashtable<String,Integer>();
	
	public TrackerWorker()
	{
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		displayTracker();
        
        //set the status text in the main window back to idle
		MainWindow.GetInstance().SetStatusIdle();
        
		return null;
	}
		
	public void displayTracker() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader("files/tracker.txt"));
		String player;
		
		// register all the scouted player into the tracker ArrayList.
		while((player = reader.readLine()) != null)
		{
			StringTokenizer st = new StringTokenizer(player," ");
			String name = st.nextToken() + " " + st.nextToken();
			scoutedPlayers.put(name, Integer.parseInt(st.nextToken()));
		}	
		reader.close();
		
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(scoutedPlayers.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				Integer i1 = (Integer) e1.getValue();
				Integer i2 = (Integer) e2.getValue();
				return i2.compareTo(i1);
			}
		});
		
		MainWindow.GetInstance().updateOutput("Tracker Report\n");
		
		int i=1;
		for(Map.Entry<String, Integer> e : list)
		{
			MainWindow.GetInstance().updateOutput(i + ". " + e.getKey() + " " + e.getValue() + "\n");
			i++;
		}
	}
}
