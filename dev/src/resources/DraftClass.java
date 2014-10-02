package resources;

import java.io.*;
import java.util.*;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import main.MainWindow;

// DraftClass handles the conversion of the excel file as well as preparing 
// the Scouting, Interview, and Workout reports.

public class DraftClass {
	
	// the excel file being loaded should have this column order
	private static final String ORDER = "NAME SURNAME POS AGE HEIGHT DH WEIGHT COLLEGE " 
		+ "CON GRE LOY PFW PT PER DUR WE POP Dunk Post Drive Jumper Three "
		+ "FGD FGI FGJ FT FG3 SCR PAS HDL ORB DRB BLK STL DRFL DEF DIS IQ "
		+ "FGD FGI FGJ FT FG3 SCR PAS HDL ORB DRB BLK STL DRFL DEF DIS IQ "; 
	// use to randomize scouting reports
	private Random rand = new Random();
	
	// key = name, value = ratings (int[][])
	private static Hashtable<String, int[][]> prospects = new Hashtable<String, int[][]>();
	private static Hashtable<String, String> schoolAttended = new Hashtable<String, String>();
	private static ArrayList<String> prospectNames = new ArrayList<String>();
	
	public DraftClass()	{}
	
	public void generateDraftClass(String filename) throws IOException
	{
		String firstLine = ""; // used to make sure prospect.xls is in correct order.
	
		try 
		{
			File inputWorkbook = new File(filename);
			Workbook w  = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			
			for (int j = 0; j < sheet.getColumns(); j++) 
			{
				Cell cell = sheet.getCell(j, 0);
				firstLine += cell.getContents() + " ";
			}
		
			// check to make sure the file is in the correct order.
			if(!firstLine.equals(ORDER))
			{
				MainWindow.GetInstance().updateOutput("\n===== START ERROR MESSAGE =====\n\n" +
						"ERROR: There is an error in prospects.xls!\n\n" +
						"Check the column names and order.\n\n" + 
						"\n=====  END ERROR MESSAGE  =====\n\n" );
				return;
			}
			
			for(int i = 1; i < sheet.getRows(); i++) 
			{
				int[][] rating = new int[5][16];
				
				// Get the player name
				Cell firstName = sheet.getCell(0,i);
				Cell lastName = sheet.getCell(1,i);
				String name = firstName.getContents().trim() + " " + lastName.getContents().trim();
				
				// Row 0 = (CON, GRE, LOY, PFW, PT, PER, DUR, WE, POP)
				for (int j=0; j < 9; j++) 
				{
					Cell cell = sheet.getCell(j+8,i);
					rating[0][j] = Integer.parseInt(cell.getContents());
				}
				
				// Row 1 = (Dunk, Post, Drive, Jumper, Three)
				for (int j=0; j < 5; j++)
				{
					Cell cell = sheet.getCell(j+17,i);
					rating[1][j] = Integer.parseInt(cell.getContents());
				}
				
				// Row 2 = (All current ratings - 16 total)
				for(int j=0; j < 16; j++)
				{
					Cell cell = sheet.getCell(j+22,i);
					rating[2][j] = Integer.parseInt(cell.getContents());
				}
				
				// Row 3 = (All potential ratings - 16 total)
				for(int j=0; j < 16; j++)
				{
					Cell cell = sheet.getCell(j+38,i);
					rating[3][j] = Integer.parseInt(cell.getContents());
				}
				
				// Row 4 = (POS, AGE, DH, WEIGHT)
				// NOTE: We skip DH so rating[4][3] has a value of 0.
				for(int j=0; j < 5; j++)
				{
					if(j == 3) continue;
					Cell cell = sheet.getCell(j+2, i);
					rating[4][j] = Integer.parseInt(cell.getContents());
				}
				
				// Get the college name
				Cell c = sheet.getCell(7,i);
				String college = c.getContents().trim();
				
				prospectNames.add(name);
				schoolAttended.put(name, college);
				prospects.put(name, rating); 
			}
			w.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			MainWindow.GetInstance().updateOutput("\n===== START ERROR MESSAGE =====\n\n" +
					"ERROR: Can't find prospects.xls file!\n\n" +
					"Check to make sure prospects.xls is in the files folder.\n\n" + 
					"\n=====  END ERROR MESSAGE  =====\n\n" );
		} 
	}
	
	public boolean checkName(String name)
	{
		return prospects.containsKey(name);
	}
	
	public ArrayList<String> getProspectNames()
	{
		return prospectNames;
	}
	
	public int getPosition(String name)
	{
		int[][] ratings = prospects.get(name);
		
		return ratings[4][0];
	}
	
	public int getAge(String name)
	{
		int[][] ratings = prospects.get(name);
		
		return ratings[4][1];
	}
	
	public int getHeight(String name)
	{
		int[][] ratings = prospects.get(name);
		
		return ratings[4][2];
	}
	
	public int getWeight(String name)
	{
		int[][] ratings = prospects.get(name);
		
		return ratings[4][4]; // NOTE: We skip DH so rating[4][3] has a value of 0.
	}

	public String getCollege(String name)
	{
		return schoolAttended.get(name);
	}
	
	// Used by the Player Finder functionality
	public int[] getPlayerRatings(String name)
	{
		int[][] ratings = prospects.get(name);
		int[] player = new int[32]; 
				
		int j = 0;
		for (int i=0; i < 16; i++)
		{				
			player[j] = ratings[2][i];
			player[j+1] = ratings[3][i];
			j+=2;
		}
				
		return player;
	}
	
	// Scouting Report
	public int[] getScouting(String name)
	{
		int[][] ratings = prospects.get(name);
		int[] scoutingReport = new int[37]; 
				
		// (Dunk, Post, Drive, Jumper, Three) -- Separate because it's the true rating
		for (int i=0; i<5; i++)
		{
			scoutingReport[i] = ratings[1][i];
		}
		
		int j = 5;
		for (int i=0; i < 16; i++)
		{				
			if(i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 12) // FGD, FGI, FGJ, FT, FG3, DRFL
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
	
	// Interview Report
	public int[] getInterview(String name)
	{
		int[][] ratings = prospects.get(name);
		int[] interview = new int[10];
		
		interview[0] = ratings[3][15]; // Potential IQ - True Rating
		
		for (int i=0; i < 9; i++)
		{
			interview[i+1] = randomInterview(ratings[0][i]);
		}
				
		return interview;
	}
	
	// Workout Report
	public int[] getWorkout(String name)
	{
		int[][] ratings = prospects.get(name);
		int[] workout = new int[32]; 
				
		int j = 0;
		for (int i=0; i < 16; i++)
		{				
			if(i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 12) // FGD, FGI, FGJ, FT, FG3, DRFL
			{
				workout[j] = randomCurrent2(ratings[2][i]);
				workout[j+1] = randomPotential2(workout[j], ratings[3][i]);
			}
			else
			{
				workout[j] = randomCurrent(ratings[2][i]);
				workout[j+1] = randomPotential(workout[j], ratings[3][i]);
			}
			j+=2;
		}
		
		return workout;
	}
	
	// random number generator
	private int randomCurrent(int rtg) // +/- 5 deviation
	{
		int min = rtg - 5;
		int num = rand.nextInt(11) + min;
	
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
	 
	private int randomPotential(int min, int max) // +/- 15 deviation
	{	
		max = max - 15;
		int num = rand.nextInt(31) + max;
	
		if(num < min) num = min;
		else if (num > 100) num = 100;
		
		return num;

	}
	
	// random number generator for FGD, FGI, FGJ, FT, FG3, DRFL
	private int randomCurrent2(int rtg) // +/- 2 deviation
	{
		int min = rtg - 2;
		int num = rand.nextInt(5) + min;
	
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
	
	// random number generator for FGD, FGI, FGJ, FT, FG3, DRFL
	private int randomPotential2(int min, int max) // +/- 2 deviation
	{
		max = max - 2;
		int num = rand.nextInt(5) + max;
	
		if(num < min) num = min;
		else if (num > 100) num = 100;
		
		return num;
	}	
	
	private int randomInterview(int rtg) // +/- 15 deviation
	{
		int min = rtg - 15;
		int num = rand.nextInt(31) + min;
	
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
}
