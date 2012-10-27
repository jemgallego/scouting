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

public class PointsWorker extends SwingWorker<Object, Object>{
	private Hashtable<String, Integer> teamPoints = new Hashtable<String,Integer>();
	
	public PointsWorker()
	{
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		displayPoints();
        
        //set the status text in the main window back to idle
		MainWindow.GetInstance().SetStatusIdle();
        
		return null;
	}
		
	public void displayPoints() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader("files/points.txt"));
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
}
