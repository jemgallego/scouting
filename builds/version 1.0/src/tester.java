import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;


public class tester {
	public static void main(String args[]) throws IOException
	{
		Scanner sc = new Scanner(new File("rookies/Rookies.txt"));
		PrintWriter pw = new PrintWriter(new File("results/basicSheet.txt"));
		
		while (sc.hasNext())
		{
			String str = sc.nextLine();	
			str = str.replaceAll("\\s++", " ");
			str = str.trim();
			
			StringTokenizer st = new StringTokenizer(str," ");
			String[] curr = new String[16];
			String[] pot = new String [16];
			
			pw.write(st.nextToken() +"\t" + st.nextToken() + "\t");
			
			// skip the next 5 tokens. 
			// this is the Shot Selection area and we don't want it in the basic sheet.
			for(int i=0; i < 5; i++)
			{
				pw.write(st.nextToken() +"\t");
			}
			
			// get the next 16 tokens
			// these are all the current ratings
			for(int i=0; i < 16; i++)
			{
				int n = Integer.parseInt(st.nextToken());
		
				if (i == 0) // FGJ 
					n = (int)(100 + ((0-100)/(25-55))*(n-55));	
				else if (i == 2) // FGI
					n = (int)(100 + ((0-100)/(30-60))*(n-60));
				else if (i == 4) // FG3
					n = (int)(100 + ((0-100)/(0-45))*(n-45));
				else if (i == 9) // DRFL
					n = (int)(100 + ((0-100)/(0-25))*(n-25));
					
				curr[i] = getGrade(n);
			}
			
			for(int i=0; i < 16; i++)
			{
				int n = Integer.parseInt(st.nextToken());
		
				if (i == 0) // FGJ 
					n = (int)(100 + ((0-100)/(25-55))*(n-55));	
				else if (i == 2) // FGI
					n = (int)(100 + ((0-100)/(30-60))*(n-60));
				else if (i == 4) // FG3
					n = (int)(100 + ((0-100)/(0-45))*(n-45));
				else if (i == 9) // DRFL
					n = (int)(100 + ((0-100)/(0-25))*(n-25));
					
				pot[i] = getGrade(n);
			}
			
			for(int i=0; i < 16; i++)
			{
				pw.write(curr[i] + "\t" + pot[i]  + "\t");
			}
		
			for(int i=0; i < 9; i++)
			{
				pw.write(st.nextToken() +"\t");
			}
			pw.write("\n");
		}		
		
		// update the main window as soon as we're done.
		System.out.println("Generate Basic Spreadsheet -- DONE");
		pw.close();
	}
	
	public static String getGrade(int rating)
	{
		String grade;
		
		if(rating <= 100 && rating >= 90)
			grade = "A+";
		else if(rating <= 89 && rating >= 80)
			grade = "A-";
		else if(rating <= 79 && rating >= 70)
			grade = "B+";
		else if(rating <= 69 && rating >= 60)
			grade = "B-";
		else if(rating <= 59 && rating >= 50)
			grade = "C+";
		else if(rating <= 49 && rating >= 40)
			grade = "C-";
		else if(rating <= 39 && rating >= 30)
			grade = "D+";
		else if(rating <= 29 && rating >= 20)
			grade = "D-";
		else if(rating <= 19 && rating >= 0)
			grade = "F";
		else 
			grade = "--";
			
		return grade;
	}
}
