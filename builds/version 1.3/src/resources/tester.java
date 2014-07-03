package resources;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

// Used to practice stuff. Not part of the program.

public class tester {
	public static void main(String args[]) throws IOException
	{	
		Scanner sc = new Scanner(new File("results/basicSheet.txt"));
		LinkedList<String> rank = new LinkedList<String>();
		
		while (sc.hasNext())
		{
			String str = sc.nextLine();
			str = str.replaceAll("\\s++", " ");
			str = str.trim();
			StringTokenizer st = new StringTokenizer(str," ");
			
			String name = st.nextToken() + " " + st.nextToken();
			
			for(int i=0; i < 4; i++)
			{
				st.nextToken();
			}
			
			int total = 0;
			for(int i=0; i < 16; i++)
			{	
				total += (Integer.parseInt(st.nextToken()));
			}
					
			rank.add(total + " " + name);
		}
		Collections.sort(rank);
		Collections.reverse(rank);
		
		System.out.println("ESPN Big Board");
		for(int i=0;i<rank.size(); i++)
		{
			String str = rank.get(i);
			str = str.replaceAll("[\\d*]", "");
			System.out.println((i+1) + ". " + str);
		}
	}
	

}
