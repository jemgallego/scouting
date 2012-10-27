import java.io.*;
import java.util.*;


public class RookieClass {
	// key = name, value = ratings (int[][])
	private static Hashtable<String, int[][]> rookies = new Hashtable<String, int[][]>();
		
	public RookieClass() throws IOException
	{
		generateRookieClass("rookies/Rookies.txt");
	}
	
	public void generateRookieClass(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String str;

		while ((str = br.readLine()) != null)
		{
			str = str.replaceAll("\\s++", " ");
			str = str.trim();
			
			StringTokenizer st = new StringTokenizer(str," ");
			String name = st.nextToken() + " " + st.nextToken(); // get the name
			
			int[][] rating = new int[4][16];

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
	}
	
	// get the ratings of the specified player.
	public int[][] get(String name)
	{
		int[][] rating;
		
		rating = rookies.get(name);
		
		return rating;
	}
	
}
