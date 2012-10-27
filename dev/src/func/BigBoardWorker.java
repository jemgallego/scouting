package func;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.SwingWorker;

import prospects.Prospects;


import main.MainWindow;


public class BigBoardWorker extends SwingWorker<Object, Object> {
	
	public BigBoardWorker()
	{
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		generateBigBoard();
        
        //set the status text in the main window back to idle
		MainWindow.GetInstance().SetStatusIdle();
        
		return null;
	}
	
	public void generateBigBoard() throws IOException
	{	
		Prospects rookies = new Prospects();
		Map<String, Integer> m = new HashMap<String,Integer>();
		
		Scanner sc = new Scanner(new File("files/Rookies.txt"));
		sc.nextLine(); // skip the Headers.
		
		while (sc.hasNext())
		{
			String str = sc.nextLine();	
			str = str.replaceAll("\\s++", " ");
			str = str.trim();
						
			StringTokenizer st = new StringTokenizer(str," ");
			
			String name = st.nextToken() + " " + st.nextToken(); // name
			int pos = Integer.parseInt(st.nextToken()); // position
			int age = Integer.parseInt(st.nextToken()); // age
		
			
			int rating[] = rookies.getScoutingReport(name);
				
			int total = 0;
			
			// sum of the ratings
			for(int i = 0; i < rating.length; i++)
			{
				total += rating[i];
			}
			
			total -= age * 50;
			total += (rating[10] * 1.3) + (rating[26] * 1.7); // SCR
			
			switch(pos)
			{
				case 1: 
					total += rating[10] + rating[26]; // SCR
					total += rating[11] + rating[27]; // PAS
					total += rating[12] + rating[28]; // HDL
					break;
				case 2:
					total += rating[10] + rating[26]; // SCR
					total += rating[10] + rating[26]; // SCR
					total += rating[17]  + rating[33]; // STL
					break;
				case 3:
					total += rating[10] + rating[26]; // SCR
					total += rating[15] + rating[31]; // DEF
					total += rating[17] + rating[33]; // STL
					break;
				case 4:
					total += rating[10] + rating[26]; // SCR
					total += rating[13] + rating[29]; // ORB
					total += rating[14] + rating[30]; // DRB
					break;
				case 5:
					total += rating[13] + rating[29]; // ORB
					total += rating[14] + rating[30]; // DRB
					total += rating[16] + rating[32]; // BLK
					break;
				default:
					break;
			}
			m.put(name, total);
		}	
		
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(m.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				Integer i1 = (Integer) e1.getValue();
				Integer i2 = (Integer) e2.getValue();
				return i2.compareTo(i1);
			}
		});
		
		MainWindow.GetInstance().updateOutput("Big Board\n");
		
		int i=1;
		for(Map.Entry<String, Integer> e : list)
		{
			MainWindow.GetInstance().updateOutput(i + ". " + e.getKey() + "\n");
			i++;
		}
		
		
//		Collections.sort(rank);
//		Collections.reverse(rank);
//		
//		MainWindow.GetInstance().updateOutput("Big Board\n");
//		for(int i=0;i<rank.size(); i++)
//		{
//			String s = rank.get(i);
//			//s = s.replaceAll("[\\d*.]", "");
//			MainWindow.GetInstance().updateOutput((i+1) + ". " + s + "\n");
//		}
	}
}