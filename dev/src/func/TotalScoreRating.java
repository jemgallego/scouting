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
		
		// sum of the preferences & ratings
		for(int j = 0; j < rating.length; j++)
		{   
			tsr += rating[j];
		}
		
		// FGI, FGJ and FG3 multiplier
		tsr += multiplier(rating[7] + rating[8]); // FGI
		tsr += multiplier(rating[8] + rating[9]); // FGJ
		tsr += multiplier(rating[13] + rating[14]); // FG3
				
		// sum of the current ratings
		for(int j = 15; j < rating.length; j++)
		{   
			tsr += multiplier(rating[j]);
			j++;
		}
				
		// sum of the potential ratings
		for(int j = 16; j < rating.length; j++)
		{
			tsr += multiplier(rating[j]);
			j++;
		}
		
		// factor in general skills.
		tsr += (multiplier(rating[15]) + multiplier(rating[16])); // SCR
		tsr += (multiplier(rating[31]) + multiplier(rating[32])); // DEF
		
		// factor in positional skills. Broke it down to several lines for easy editing.
		switch(pos)
		{
			case 1: 
				skills += multiplier(rating[17]) + multiplier(rating[18]); // PAS
				skills += multiplier(rating[19]) + multiplier(rating[20]); // HDL
				skills += multiplier(rating[27]) + multiplier(rating[28]); // STL
				skills += multiplier(rating[31]) + multiplier(rating[32]); // DEF
				skills += multiplier(rating[35]) + multiplier(rating[36]); // IQ
				break;
			case 2:
				skills += multiplier(rating[15]) + multiplier(rating[16]); // SCR
				skills += multiplier(rating[19]) + multiplier(rating[20]); // HDL
				skills += multiplier(rating[27]) + multiplier(rating[28]); // STL
				skills += multiplier(rating[31]) + multiplier(rating[32]); // DEF
				skills += multiplier(rating[35]) + multiplier(rating[36]); // IQ
				break;
			case 3:
				skills += multiplier(rating[15]) + multiplier(rating[16]); // SCR
				skills += multiplier(rating[23]) + multiplier(rating[24]); // DRB
				skills += multiplier(rating[31]) + multiplier(rating[32]); // DEF
				skills += multiplier(rating[33]) + multiplier(rating[34]); // DIS
				skills += multiplier(rating[35]) + multiplier(rating[36]); // IQ
				break;
			case 4:
				skills += multiplier(rating[15]) + multiplier(rating[16]); // SCR
				skills += multiplier(rating[21]) + multiplier(rating[22]); // ORB
				skills += multiplier(rating[23]) + multiplier(rating[24]); // DRB
				skills += multiplier(rating[25]) + multiplier(rating[26]); // BLK
				skills += multiplier(rating[31]) + multiplier(rating[32]); // DEF
				break;
			case 5:
				skills += multiplier(rating[15]) + multiplier(rating[16]); // SCR
				skills += multiplier(rating[21]) + multiplier(rating[22]); // ORB
				skills += multiplier(rating[23]) + multiplier(rating[24]); // DRB
				skills += multiplier(rating[25]) + multiplier(rating[26]); // BLK
				skills += multiplier(rating[31]) + multiplier(rating[32]); // DEF
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
			int newScore = (int) ((scoreRatings.get(playerName) * 0.8) + (score * 0.2));
//			int newScore = (scoreRatings.get(playerName) + score) / 2;
			scoreRatings.put(playerName, newScore);
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
	
	private int multiplier(int rating)
	{
		int adjRating;
		
		if (rating < 49)
			adjRating = rating;
		else if (rating < 69)
			adjRating = (int) (rating * 1.1);
		else if (rating < 79)
			adjRating = (int) (rating * 1.2);
		else if (rating < 89)
			adjRating = (int) (rating * 1.5);
		else if (rating < 95)
			adjRating = (int) (rating * 1.7);
		else 
			adjRating = (int) (rating * 2.5);
		
		return adjRating;
	}

}
