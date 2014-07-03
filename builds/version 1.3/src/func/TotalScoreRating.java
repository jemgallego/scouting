package func;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import resources.DraftClass;

import main.MainWindow;

public class TotalScoreRating {
	private static Hashtable<String, Integer> scoreRatings = new Hashtable<String,Integer>(); // player, scoreRating
	
	public TotalScoreRating() throws IOException
	{
		File f = new File("files/scoreRatings.txt");
		if (!f.exists()) createFile(f);
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;
		
		// import the contents of points.txt into our teamPoints table.
		while((line = reader.readLine()) != null)
		{
			StringTokenizer st = new StringTokenizer(line," ");
			int tokens = st.countTokens();
			String name = "";
			
			for(int i = 1; i < tokens; i++)
			{
				name += st.nextToken() + " ";
			}
			name = name.trim();
			
			scoreRatings.put(name, Integer.parseInt(st.nextToken()));
		}	
		reader.close();
	}
	
	public void createFile(File f)
	{		
		// if scoreRatings.txt does not exist, create a blank one.
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));	
			
			// Add list of players with tsr equal to 0.
			DraftClass draftClass = new DraftClass();
			ArrayList<String> players = draftClass.getProspectNames();
			
			for(int i=0; i< players.size(); i++ )
				writer.append(players.get(i) + " 0\n");
			
			writer.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			MainWindow.GetInstance().updateOutput("\n===== START ERROR MESSAGE =====\n\n" +
					"ERROR: Creating scoreRatings.txt file FAILED!\n\n" +
					"\n=====  END ERROR MESSAGE  =====\n\n" );
		}

	}
			
	public int calculateTSR(String name, int[] rating, int pos)
	{
		int tsr = 0;
		int skills = 0;
		
		// sum of the preferences & current ratings
		for(int j = 0; j < 20; j++)
		{
			tsr += rating[j] * 1.2;
		}
		// sum of the potential ratings
		for(int j = 20; j < rating.length; j++)
		{
			tsr += rating[j] * 1.8;
		}
		
		// factor in general skills
		tsr += (rating[15] + rating[16] + rating[32] + rating[36]) * 1.5; // SCR, DEF, IQ
		
		// factor in positional skills. Broke it down to several lines for easy editing.
		switch(pos)
		{
			case 1: 
				skills += rating[18]; // PAS
				skills += rating[20]; // HDL
				skills += rating[28]; // STL
				skills += rating[36]; // IQ
				break;
			case 2:
				skills += rating[16]; // SCR
				skills += rating[20]; // HDL
				skills += rating[28]; // STL
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
		
		tsr += skills;
		
		return tsr;
	}
	
	public void updateTSR(String playerName, int score) {
		// check if player has been scouted before
		if(scoreRatings.get(playerName) == 0) // not scouted yet.
		{
			scoreRatings.put(playerName,score);
		}
		else 
		{
			// Old score has 80% weight while new score has 20% weight
			int newScore = (int) ((scoreRatings.get(playerName) * 0.8) + (score *0.2));
//			int newScore = (scoreRatings.get(playerName) + score) / 2;
			scoreRatings.put(playerName, newScore);
			System.out.println("hello");
		}  
	}
	
	public void saveRatings() throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("files/scoreRatings.txt"));
		// Update ScoreRatings File
		List<Map.Entry<String, Integer>> list2 = new ArrayList<Map.Entry<String, Integer>>(scoreRatings.entrySet());
		Collections.sort(list2, new Comparator<Map.Entry<String, Integer>>() {
			public int compare (Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				Integer i1 = (Integer) e1.getValue();
				Integer i2 = (Integer) e2.getValue();
				return i2.compareTo(i1);
			}
		});
		
		for(Map.Entry<String, Integer> e : list2)
		{
			writer.append(e.getKey() + " " + e.getValue() + "\n");
		}
		writer.close();
	}

}
