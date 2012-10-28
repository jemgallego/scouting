package draftClass;

import java.io.*;
import java.util.*;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import main.MainWindow;

public class DraftClass {
	
	// key = name, value = ratings (int[][])
	private static Hashtable<String, int[][]> prospects = new Hashtable<String, int[][]>();
	// the excel file being loaded should have this column order
	private static final String ORDER = "NAME SURNAME POS AGE HEIGHT DH WEIGHT COLLEGE " 
		+ "CON GRE LOY PFW PT PER DUR WE POP Dunk Post Drive Jumper Three "
		+ "FGD FGI FGJ FT FG3 SCR PAS HDL ORB DRB BLK STL DRFL DEF DIS IQ "
		+ "FGD FGI FGJ FT FG3 SCR PAS HDL ORB DRB BLK STL DRFL DEF DIS IQ "; 
	
	private Random rand = new Random();
		
	public DraftClass() throws IOException
	{
		generateDraftClass("files/prospects.xls");
	}
	
	public void generateDraftClass(String filename) throws IOException
	{
		String firstLine = "";
		File inputWorkbook = new File(filename);
		Workbook w;
		
		try 
		{
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			
			for (int j = 0; j < sheet.getColumns(); j++) 
			{
				Cell cell = sheet.getCell(j, 0);
				firstLine += cell.getContents() + " ";
			}
		
			// check to make sure the file is in the correct order.
			if(!firstLine.equals(ORDER))
			{
				MainWindow.GetInstance().updateOutput("ERROR: Order of columns is wrong!\n\n");
				return;
			}
			
			for(int i = 1; i < sheet.getRows(); i++) 
			{
				int[][] rating = new int[4][16];
				
				// Get the player name
				Cell firstName = sheet.getCell(0,i);
				Cell lastName = sheet.getCell(1,i);
				String name = firstName.getContents() + " " + lastName.getContents();
	
				// Row 0 = (CON, GRE, LOY, PFW, PT, PER, DUR, WE, POP)
				for (int j = 0; j < 9; j++) 
				{
					Cell cell = sheet.getCell(j+8, i);
					rating[0][j] = Integer.parseInt(cell.getContents());
				}
				
				// Row 1 = (Dunk, Post, Drive, Jumper, Three)
				for (int j=0; j < 5; j++)
				{
					Cell cell = sheet.getCell(j+17, i);
					rating[1][j] = Integer.parseInt(cell.getContents());
				}
				
				// Row 2 = (All current ratings - 16 total)
				// Row 3 = (All potential ratings - 16 total)
				for(int row=2; row<=3; row++)
				{
					for(int col=0; col < 16; col++)
					{
						Cell cell = sheet.getCell(col+22,i);
						rating[row][col] = Integer.parseInt(cell.getContents());
					}
				}
				
				prospects.put(name, rating); // put the name & rating pairing into the Hashtable
			}	
		} 
		catch (BiffException e) 
		{
			e.printStackTrace();
			MainWindow.GetInstance().updateOutput("Error reading excel file!\n\n");
		} 
	}
	
	public boolean checkName(String name)
	{
		return prospects.containsKey(name);
	}
	
	public int[] getScoutingReport(String name)
	{
		int[][] ratings = prospects.get(name);
		int[] scoutingReport = new int[37]; 
		
		// (Dunk, Post, Drive, Jumper, Three) -- Separate because it's the true rating
		for (int i=0; i < 5 ; i++)
		{
			scoutingReport[i] = ratings[1][i];
		}
		
		int j = 5;
		for (int i=0; i < 16; i++) // Rest of the ratings -- current and potential
		{				
			if(i == 0 || i == 1 || i == 2 || i == 3 || i == 13) // FGD, FGI, FGJ, FG3, DRFL
			{
				scoutingReport[j] = randomCurrent2(ratings[2][i]);
				scoutingReport[j+1] = randomPotential2(scoutingReport[j], ratings[3][i]);
			}
			else
			{
				scoutingReport[j] = randomCurrent(ratings[2][i]);
				scoutingReport[j+1] = randomPotential(scoutingReport[j], ratings[3][i]);
			}
			j+=2;
		}
		
		return scoutingReport;
	}
	
	public int[] getInterview(String name)
	{
		int[][] ratings = prospects.get(name);
		int[] interview = new int[10];
		
		interview[0] = randomInterview(ratings[3][15]);
		
		for (int i=0; i < 9; i++)
		{
			interview[i+1] = randomInterview(ratings[0][i]);
		}
		
		return interview;
	}
	
	public int[] getWorkout(String name)
	{
		int[][] ratings = prospects.get(name);
		int[] workout = new int[16];
		
		for (int i=0; i < 16; i++)
		{
			workout[i] = ratings[2][i];
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
