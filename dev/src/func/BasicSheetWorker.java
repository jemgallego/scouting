package func;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.SwingWorker;

import main.MainWindow;

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
		Scanner sc = new Scanner(new File("files/Rookies.txt"));
		PrintWriter pw = new PrintWriter(new File("basicSheet.txt"));
		
		String order = "FirstName LastName Position Age Height Weight Dunk Post Drive Jumper Three "
			+ "FGD FGI FGJ FG3 FT SCR PAS HDL ORB DRB DEF BLK STL DRFL DIS IQ "
			+ "FGD FGI FGJ FG3 FT SCR PAS HDL ORB DRB DEF BLK STL DRFL DIS IQ CON GRE LOY PFW PT PER DUR WE POP";
		String firstLine = sc.nextLine();
		firstLine = firstLine.replaceAll("\\s++", " ");
		firstLine = firstLine.trim();
		
		if(!order.equals(firstLine))
		{
			// check to make sure the file is in the correct order.
			MainWindow.GetInstance().updateOutput("Error in Rookie file");
			pw.close();
			return;
		}
		
		while (sc.hasNext())
		{
			String str = sc.nextLine();	
			str = str.replaceAll("\\s++", " ");
			str = str.trim();
			
			StringTokenizer st = new StringTokenizer(str," ");
			
			// write FirstName, LastName, Position, Age.
			for(int i=0; i < 4; i++)
			{
				pw.write(st.nextToken() +"\t");
			}
			
			// skip the next 7 tokens. we don't want these in the basic sheet.
			for(int i=0; i < 7; i++)
			{
				st.nextToken();
				pw.write("\t");
			}
			
			// get the next 16 tokens
			// these are all the current ratings
			for(int i=0; i < 16; i++)
			{		
				int rtg = (Integer.parseInt(st.nextToken()));
					
				if (i == 1 || i == 2 || i == 3 || i == 13) // FGI, FGJ, FG3, DRFL
					rtg = randomSheet2(rtg);
				else
					rtg = randomSheet(rtg);

				pw.write(rtg + "\t\t"); // two tabs because we're leaving potential blank.
			}
					
			pw.write("\n");
		}		
		
		// update the main window as soon as we're done.
		MainWindow.GetInstance().updateOutput("Generate Basic Spreadsheet -- DONE");
		pw.close();
	}
	
	// random generator for basic sheet.
	public int randomSheet(int rtg) // +/- 10 deviation
	{
		Random rand = new Random();
		
		int min = rtg - 10;
		int num = rand.nextInt(21) + min;
		
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
	
	// random number generator for FGI, FGJ, FG3, DRFL
	public int randomSheet2(int rtg) // +/- 4 deviation
	{
		Random rand = new Random();
		
		int min = rtg - 4;
		int num = rand.nextInt(5) + min;
	
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
}

