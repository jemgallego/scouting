package func;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.SwingWorker;

import jxl.Workbook;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import main.MainWindow;

public class MediaScoutingWorker extends SwingWorker<Object, Object> {
	
	public MediaScoutingWorker()
	{
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		generateMediaScouting();
                
		return null;
	}
	
	private void generateMediaScouting() throws IOException
	{		
		try 
		{
			Workbook template = Workbook.getWorkbook(new File("files/prospects.xls"));
			WritableWorkbook w = Workbook.createWorkbook(new File("mediaScouting.xls"), template);
			
			WritableSheet sheet = w.getSheet(0);
			
			for(int i = 1; i < sheet.getRows(); i++)
			{
				for(int j = 8; j < 17; j++) 
				{
					WritableCell cell = sheet.getWritableCell(j,i);
					int num = Integer.parseInt(cell.getContents());
					
					num = randomizeIntangibles(num);
					
					Number n = (Number)cell;
					n.setValue(num);
				}
				
				for(int j = 22; j < 38; j++)
				{
					WritableCell cell = sheet.getWritableCell(j,i);
					int num = Integer.parseInt(cell.getContents());
					
					if (j == 22 || j == 23 || j == 24 || j == 25 || j == 26 || j == 34) // FGD, FGI, FGJ, FT, FG3, DRFL
						num = randomizeFG(num);
					else
						num = randomizeCurrent(num);
						
					Number n = (Number)cell;					
					n.setValue(num);
				}
				
				for(int j = 38; j < sheet.getColumns(); j++)
				{
					WritableCell cell = sheet.getWritableCell(j,i);
					int num = Integer.parseInt(cell.getContents());
					
					if (j == 38 || j == 39 || j == 40 || j == 41 || j == 42 || j == 50) // FGD, FGI, FGJ, FT, FG3, DRFL
						num = randomizeFG(num);
					else
						num = randomizePotential(num);
					
					Number n = (Number)cell;
					n.setValue(num);
				}
			}
			
			MainWindow.GetInstance().updateOutput("Generate Media Scouting -- DONE");
			w.write();
			w.close();
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			MainWindow.GetInstance().updateOutput("\n===== START ERROR MESSAGE =====\n\n" +
					"UNKNOWN ERROR: Generating excel file failed!\n\n" +
					"\n=====  END ERROR MESSAGE  =====\n\n" );
		} 
		
	}
	
	// random generator for basic sheet.	
	public int randomizeFG(int rtg) // +/- 4 deviation
	{
		Random rand = new Random();
		
		int min = rtg - 4;
		int num = rand.nextInt(9) + min;
	
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
	
	public int randomizeCurrent(int rtg) // +/- 10 deviation
	{
		Random rand = new Random();
		
		int min = rtg - 10;
		int num = rand.nextInt(21) + min;
		
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
	
	public int randomizePotential(int rtg) // +/- 20 deviation
	{
		Random rand = new Random();
		
		int min = rtg - 20;
		int num = rand.nextInt(41) + min;
		
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
	
	public int randomizeIntangibles(int rtg) // +/- 25 deviation
	{
		Random rand = new Random();
		
		int min = rtg - 25;
		int num = rand.nextInt(51) + min;
		
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
}

