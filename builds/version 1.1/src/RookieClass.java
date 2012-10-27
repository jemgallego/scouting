import java.io.*;
import java.util.*;

public class RookieClass {
	// key = name, value = ratings (int[][])
	private static Hashtable<String, int[][]> rookies = new Hashtable<String, int[][]>();
	private Random rand = new Random();
		
	public RookieClass() throws IOException
	{
		generateRookieClass("files/Rookies.txt");
	}
	
	public void generateRookieClass(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));

		String order = "FirstName LastName Position Age Height Weight Dunk Post Drive Jumper Three "
			+ "FGD FGI FGJ FG3 FT SCR PAS HDL ORB DRB DEF BLK STL DRFL DIS IQ "
			+ "FGD FGI FGJ FG3 FT SCR PAS HDL ORB DRB DEF BLK STL DRFL DIS IQ CON GRE LOY PFW PT PER DUR WE POP";
		String firstLine = br.readLine();
		firstLine = firstLine.replaceAll("\\s++", " ");
		firstLine = firstLine.trim();
		
		// check to make sure the file is in the correct order.
		if(!order.equals(firstLine))
		{
			MainWindow.GetInstance().updateOutput("Error in Rookie file\n\n");
			return;
		}
		
		String str;
		
		while ((str = br.readLine()) != null)
		{
			int[][] rating = new int[4][16];
			
			str = str.replaceAll("\\s++", " ");
			str = str.trim();
			
			StringTokenizer st = new StringTokenizer(str," ");
			String name = st.nextToken() + " " + st.nextToken(); // get the name
			
			// Skip over the next four tokens (Pos ,Age, Ht, Wt).
			for(int i=0; i < 4; i++)
			{
				st.nextToken(); 
			}

			// Row 0 = (Dunk, Post, Drive, Jumper, Three)
			for (int i=0; i < 5; i++)
			{
				rating[0][i] = Integer.parseInt(st.nextToken());
			}
			
			// Row 1 = (All current ratings - 16 total)
			// Row 2 = (All potential ratings - 16 total)
			for(int row=1; row<=2; row++) {
				for(int col=0; col < 16; col++)	{
					rating[row][col] = Integer.parseInt(st.nextToken());
				}
			}
			
			// Row 3 = (CON, GRE, LOY, PFW, PT, PER, DUR, WE, POP)
			for (int i=0; i < 9; i++)
			{
				rating[3][i] = Integer.parseInt(st.nextToken());
			}
			
			rookies.put(name, rating); // put the name & rating pairing into the Hashtable
		}
		br.close();
	}
	
	public boolean checkName(String name)
	{
		return rookies.containsKey(name);
	}
	
	public int[] getScoutingReport(String name)
	{
		int[][] ratings = rookies.get(name);
		int[] scoutingReport = new int[37]; 
		
		// (Dunk, Post, Drive, Jumper, Three) -- Separate because it's the true rating
		for (int i=0; i < 5 ; i++)
		{
			scoutingReport[i] = ratings[0][i];
		}
		
		int j = 5;
		for (int i=0; i < 16; i++) // Rest of the ratings -- current and potential
		{				
			if(i == 0 || i == 1 || i == 2 || i == 3 || i == 13) // FGD, FGI, FGJ, FG3, DRFL
			{
				scoutingReport[j] = randomCurrent2(ratings[1][i]);
				scoutingReport[j+1] = randomPotential2(scoutingReport[j], ratings[2][i]);
			}
			else
			{
				scoutingReport[j] = randomCurrent(ratings[1][i]);
				scoutingReport[j+1] = randomPotential(scoutingReport[j], ratings[2][i]);
			}
			j+=2;
		}
		
		return scoutingReport;
	}
	
	public int[] getInterview(String name)
	{
		int[][] ratings = rookies.get(name);
		int[] interview = new int[10];
		
		interview[0] = randomInterview(ratings[2][15]);
		
		for (int i=0; i < 9; i++)
		{
			interview[i+1] = randomInterview(ratings[3][i]);
		}
		
		return interview;
	}
	
	public int[] getWorkout(String name)
	{
		int[][] ratings = rookies.get(name);
		int[] workout = new int[16];
		
		for (int i=0; i < 16; i++)
		{
			workout[i] = ratings[1][i];
		}
		
		return workout;
	}
	
	// random number generator
	public int randomCurrent(int rtg) // +/- 7 deviation
	{
		int min = rtg - 7;
		int num = rand.nextInt(15) + min;
	
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
	 
	public int randomPotential(int min, int max) // +/- 15 deviation
	{	
		max = max - 15;
		int num = rand.nextInt(31) + max;
	
		if(num < min) num = min;
		else if (num > 100) num = 100;
		
		return num;

	}
	
	// random number generator for FGI, FGJ, FG3, DRFL
	public int randomCurrent2(int rtg) // +/- 2 deviation
	{
		int min = rtg - 2;
		int num = rand.nextInt(5) + min;
	
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
	
	public int randomPotential2(int min, int max) // +/- 2 deviation
	{
		max = max - 2;
		int num = rand.nextInt(5) + max;
	
		if(num < min) num = min;
		else if (num > 100) num = 100;
		
		return num;
	}	
	
	public int randomInterview(int rtg) // +/- 10 deviation
	{
		int min = rtg - 10;
		int num = rand.nextInt(21) + min;
	
		if(num < 0) num = 0;
		else if (num > 99) num = 99;
		
		return num;
	}
}
