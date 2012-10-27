import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.SwingWorker;

public class BasicSheetWorker extends SwingWorker<Object, Object> {
	
	public BasicSheetWorker()
	{
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		generateSpreadsheet();
        
        //set the status text in the main window back to idle
		MainWindow.GetInstance().SetStatusIdle();
        
		return null;
	}
	
	// create a text file of the basic spreadsheet. 
	// you can then just copy and paste it to an Excel file.
	private void generateSpreadsheet() throws IOException
	{
		Scanner sc = new Scanner(new File("rookies/Rookies.txt"));
		PrintWriter pw = new PrintWriter(new File("results/basicSheet.txt"));
		
		while (sc.hasNext())
		{
			String str = sc.nextLine();	
			str = str.replaceAll("\\s++", " ");
			str = str.trim();
			
			StringTokenizer st = new StringTokenizer(str," ");
			String rtg;
			
			pw.write(st.nextToken() +"\t" + st.nextToken() + "\t");
			
			// skip the next 5 tokens. 
			// this is the Shot Selection area and we don't want it in the basic sheet.
			for(int i=0; i < 5; i++)
			{
				st.nextToken();
				pw.write("\t");
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
					
				rtg = getGrade(n);
				pw.write(rtg + "\t\t");
			}
					
			pw.write("\n");
		}		
		
		// update the main window as soon as we're done.
		MainWindow.GetInstance().updateOutput("Generate Basic Spreadsheet -- DONE");
		pw.close();
	}
	
	public static String getGrade(int rating)
	{
		String grade;
		
		if(rating <= 100 && rating >= 80)
			grade = "A";
		else if(rating <= 79 && rating >= 60)
			grade = "B";
		else if(rating <= 59 && rating >= 40)
			grade = "C";
		else if(rating <= 39 && rating >= 20)
			grade = "D";
		else if (rating <= 19 && rating >= 0)
			grade = "F";
		else
			grade = "--";
			
		return grade;
	}
}

