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
	private Hashtable<String, Integer> previousRanking = new Hashtable<String,Integer>(); // name, rank
	
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
		ArrayList<String> tsrRank = new ArrayList<String>();
		DraftClass prospects = new DraftClass();
		
		File scoreFile = new File("files/scoreRatings.txt"); 
		if(!scoreFile.exists())
		{
			MainWindow.GetInstance().updateOutput("\n===== START ERROR MESSAGE =====\n\n" +
					"ERROR: scoreRatings.txt not found!\n\n" +
					"\n=====  END ERROR MESSAGE  =====\n\n" );
			return;
		}	
		
		// get TSR Rankings. TSR Rankings is used to rank the players in the big board.
		BufferedReader tsrReader = new BufferedReader(new FileReader(scoreFile));
		String line;
		
		// import the contents of scoreRatings.txt
		while((line = tsrReader.readLine()) != null)
		{
			String name = line.replaceAll("\\d++", "");
			name = name.trim();
			
			tsrRank.add(name); // change to name
		}	
		tsrReader.close();
			
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
				previousRanking.put(name, Integer.parseInt(st.nextToken()));
			}	
			bigboardReader.close();
		}
						
		updateBigBoard(tsrRank); // overwrite bigboard.txt with the new rankings.
		
		// Generate output - ready to copy and paste to forum.
		MainWindow.GetInstance().updateOutput("[size=200][b]Big Board[/b][/size]\n\n");
		MainWindow.GetInstance().updateOutput("[center][table][tr][td][b]Ranking[/b][/td][td][b]Change[/b][/td]" +
			"[td][b]Player[/b][/td][td][b]Age[/b][/td][td][b]Position[/b][/td][td][b]Height[/b][/td][td][b]Weight[/b][/td][/tr]");
		
		int currentRank=1; // rank number
		String output ="";
		
		for(int i = 0; i < tsrRank.size(); i++)
		{
			int previous;
			String name = tsrRank.get(i);
						
			if (previousRanking.get(name) == null) 
				previous = currentRank;
			else 
				previous = previousRanking.get(name);
			
			int change = previous - currentRank;
			String str = "";
			String position = "";
			
			Height h = new Height();
			String height = h.getDisplayHeight(prospects.getHeight(name));
			
			if(change == 0)
				str = "--";
			else if (change > 0)
				str = "[color=#008000][b]+" + change + "[/b][/color]";
			else
				str = "[color=#FF0000][b]" + change + "[/b][/color]";
				
			switch(prospects.getPosition(name))
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
			
			int age = prospects.getAge(name);
			
			String str1 = "[tr][td]" + currentRank + "[/td][td]" + str + "[/td][td]" + name + "[/td][td]" + 
				age + "[/td][td]" + position + "[/td][td]" + height + "[/td][td]" + 
				prospects.getWeight(name)  + "[/td][/tr]";
    		output = output.concat(str1); 
	
			currentRank++;
		}
		
		MainWindow.GetInstance().updateOutput(output + "[/table][/center]");

	}
	
	// overwrite bigboard.txt with the new rankings.
	private void updateBigBoard(ArrayList<String> tsrRank) throws IOException
	{
		BufferedWriter bigboardWriter = new BufferedWriter(new FileWriter("files/bigboard.txt"));
		
		int rank=1;
		for(int i = 0; i < tsrRank.size(); i++)
		{
			bigboardWriter.append(tsrRank.get(i) + " " + rank + "\n");
			rank++;
		}
		bigboardWriter.close();
	}
}