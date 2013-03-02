package func;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.SwingWorker;
import resources.DraftClass;
import resources.Height;
import main.MainWindow;

public class BigBoardWorker extends SwingWorker<Object, Object> {
	private Hashtable<String, Integer> playerTable = new Hashtable<String,Integer>();
	
	public BigBoardWorker()
	{
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		generateBigBoard();
                
		return null;
	}
	
	public void generateBigBoard() throws IOException
	{	
		Map<String, Integer> m = new HashMap<String,Integer>();

		TrackerWorker tracker = new TrackerWorker(); 		
		DraftClass prospects = new DraftClass();
		String[] names = prospects.getProspectNames();
		
		for(int i=0; i< names.length; i++ )
		{
			int pos = prospects.getPosition(names[i]);
			int age = prospects.getAge(names[i]);
			
			int rating[] = prospects.getScoutingReport(names[i]);
			int total = 0;
			int skills = 0;
			
			// sum of the preferences & current ratings
			for(int j = 0; j < 20; j++)
			{
				total += rating[j] * 0.2;
			}
			// sum of the potential ratings
			for(int j = 20; j < rating.length; j++)
			{
				total += rating[j] * 0.8;
			}
			
			total -= age * 10;
			total += prospects.getHeight(names[i]) * 0.2; 
			total += prospects.getWeight(names[i]) * 0.1;
			total += (rating[15] + rating[16] + rating[32] + rating[36]) * 1.5; // SCR, DEF, IQ
			
			
			switch(pos)
			{
				case 1: 
					skills += rating[18]; // PAS
					skills += rating[20]; // HDL
					skills += rating[28]; // STL
 					skills += rating[36]; // IQ
					break;
				case 2:
					skills += rating[20]; // HDL
					skills += rating[28]; // STL
					skills += rating[32]; // DEF
					skills += rating[36]; // IQ
					break;
				case 3:
					skills += rating[24]; // DRB
					skills += rating[26]; // BLK
					skills += rating[28]; // STL
					skills += rating[32]; // DEF
					break;
				case 4:
					skills += rating[22]; // ORB
					skills += rating[24]; // DRB
					skills += rating[26]; // BLK
					skills += rating[32]; // DEF
					break;
				case 5:
					skills += rating[22]; // ORB
					skills += rating[24]; // DRB
					skills += rating[26]; // BLK
					skills += rating[32]; // DEF
					break;
				default:
					break;
			}
			
			total += skills * 3;
			
			// factor in the tracker
			int num = tracker.getTimesScouted(names[i]);
			total += num * 20;
			
			m.put(names[i], total);
		}	
		
		// Put the players in order
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(m.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				Integer i1 = (Integer) e1.getValue();
				Integer i2 = (Integer) e2.getValue();
				return i2.compareTo(i1);
			}
		});
	
		// import the previous rankings into our hashtable.
		File f = new File("files/bigboard.txt");
		if (f.exists())
		{
			BufferedReader bigboardReader = new BufferedReader(new FileReader(f));
			String player; 
		
			while((player = bigboardReader.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(player," ");
				String name = st.nextToken() + " " + st.nextToken();
				playerTable.put(name, Integer.parseInt(st.nextToken()));
			}	
			bigboardReader.close();
		}
						
		// overwrite the bigboard.txt with the new rankings.
		BufferedWriter bigboard = new BufferedWriter(new FileWriter("files/bigboard.txt"));
		
		int rank=1;
		for(Map.Entry<String, Integer> e : list)
		{
			bigboard.append(e.getKey() + " " + rank + "\n");
			rank++;
		}
		bigboard.close();
					
		MainWindow.GetInstance().updateOutput("[size=200][b]Big Board[/b][/size]\n\n");
		MainWindow.GetInstance().updateOutput("[center][table][tr][td][b]Ranking[/b][/td][td][b]Change[/b][/td]" +
			"[td][b]Player[/b][/td][td][b]Age[/b][/td][td][b]Position[/b][/td][td][b]Height[/b][/td][td][b]Weight[/b][/td][/tr]");
		
		int i=1; // rank number
		String output ="";
		
		for(Map.Entry<String, Integer> e : list)
		{
			int previous;
			if (playerTable.get(e.getKey()) == null) 
				previous = i;
			else 
				previous = playerTable.get(e.getKey());
			
			int change = previous - i;
			String str = "";
			String position = "";
			
			Height h = new Height();
			String height = h.getDisplayHeight(prospects.getHeight(e.getKey()));
			
			if(change == 0)
				str = "--";
			else if (change > 0)
				str = "[color=#008000][b]+" + change + "[/b][/color]";
			else
				str = "[color=#FF0000][b]" + change + "[/b][/color]";
				
			switch(prospects.getPosition(e.getKey()))
			{
				case 1: position = "PG";
					break;
				case 2: position = "SG";
					break;
				case 3: position = "SF";
					break;
				case 4: position = "PF";
					break;
				case 5: position = "C";
					break;
				default:
					break;
			}
			
			int age = prospects.getAge(e.getKey()) + 1; // add 1 to show correct age.
			
			String str1 = "[tr][td]" + i + "[/td][td]" + str + "[/td][td]" + e.getKey() + "[/td][td]" + 
				age + "[/td][td]" + position + "[/td][td]" + height + "[/td][td]" + 
				prospects.getWeight(e.getKey())  + "[/td][/tr]";
    		output = output.concat(str1); 
		
			i++;
		}
		
		MainWindow.GetInstance().updateOutput(output + "[/table][/center]");

	}
}