package func;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.SwingWorker;

import jxl.Cell;
import jxl.Workbook;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import main.MainWindow;

public class DraftCampWorker extends SwingWorker<Object, Object> {

	private Random rand = new Random();
	
	public DraftCampWorker()
	{
		setProgress(0);
	}
		
	@Override
	public Object doInBackground() throws IOException
	{        
		runDraftCamp();
                
		return null;
	}
	
	private void runDraftCamp() throws IOException
	{		
		try 
		{
			Workbook template = Workbook.getWorkbook(new File("files/prospects.xls"));
			WritableWorkbook w = Workbook.createWorkbook(new File("preDraftCamp.xls"), template);
			
			WritableSheet sheet = w.getSheet(0);
			
			for(int i = 1; i < sheet.getRows(); i++)
			{				
				for(int j = 8; j < 22; j++) // clear intangibles and preferences
				{
					WritableCell cell = sheet.getWritableCell(j,i);
					Number n = (Number)cell;
					n.setValue(0);
				}
				
				for(int j = 22; j < 38; j++) // current ratings
				{
					WritableCell cell = sheet.getWritableCell(j,i);
					int num = Integer.parseInt(cell.getContents());
					
					if (j == 22 || j == 23 || j == 24 || j == 25 || j == 26 || j == 34) // FGD, FGI, FGJ, FT, FG3, DRFL
						num = randomizeFG(num);
					else
						num = randomize(num);
						
					Number n = (Number)cell;					
					n.setValue(num);
				}
				
				for(int j = 38; j < sheet.getColumns(); j++) // clear potential values
				{
					WritableCell cell = sheet.getWritableCell(j,i);
					Number n = (Number)cell;
					n.setValue(0);
				}
			}
			
			MainWindow.GetInstance().updateOutput("Draft Camp Simulation -- DONE");
			w.write();
			w.close();
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			MainWindow.GetInstance().updateOutput("\n===== START ERROR MESSAGE =====\n\n" +
					"UNKNOWN ERROR: Generating preDraftCamp.xls file failed!\n\n" +
					"\n=====  END ERROR MESSAGE  =====\n\n" );
		} 
	}
	
	// Simulate camp by randomizing ratings
	public int randomizeFG(int rtg) // +/- 4 deviation
	{
		int min = rtg - 4;
		int num = rand.nextInt(9) + min;
	
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
	
	public int randomize(int rtg) // +/- 10 deviation
	{	
		int min = rtg - 10;
		int num = rand.nextInt(21) + min;
	
		if(num < 0) num = 0;
		else if (num > 100) num = 100;
		
		return num;
	}
}
