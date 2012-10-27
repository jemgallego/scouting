import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingWorker;

public class WorkoutWorker extends SwingWorker<Object, Object> {
	
	private static RookieClass rookies;
	private enum Rating {FGD, FGI, FGJ, FG3, FT, SCR,
		PAS, HDL, ORB, DRB, DEF, BLK, STL, DRFL, DIS, IQ};
		
	private File directory;
	private BufferedWriter workouts;
	
	public WorkoutWorker(File f)
	{
		directory = f;
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		conductWorkouts();
        
        //set the status text in the main window back to idle
		MainWindow.GetInstance().SetStatusIdle();
        
		return null;
	}
	
	private void conductWorkouts() throws IOException
	{
		rookies = new RookieClass(); // Generate ratings table for rookie class
		File files[] = directory.listFiles(); // Get all the files in the directory.
		
		for(File f: files)
		{
			String filename = f.getName();
			
			if (filename.endsWith(".txt"))
			{	
				BufferedReader br = new BufferedReader(new FileReader(f));
				
				// Start reading the work out file
				String str; 
				String teamName;
				int count = 0;
		
				teamName = br.readLine(); // read Team Name

				// Create a text file with the results for the respective team.
				workouts = new BufferedWriter(new FileWriter("results/" + teamName + ".txt"));
				workouts.append("\n" + teamName.toUpperCase() + "\n\n");		
				
				
				while ((str = br.readLine()) != null)
				{
					getWorkoutResults(str);	
					workouts.append("\n");
					
					count++; // keep track of # of players worked out.
				}	
				br.close();
				workouts.close();
				MainWindow.GetInstance().updateOutput(filename + " -- " + count + "\n");
			}
		}
		MainWindow.GetInstance().updateOutput("\nWORKOUT -- DONE\n");
	}
	
	public void getWorkoutResults(String name) throws IOException
	{
		int i=0;
		
		// Error check: Name
		if (!rookies.checkName(name))
		{
			workouts.append("ERROR: Name Not Found! \n --/--\n");
			return;
		}
	
		int[] workout = rookies.getWorkout(name);
		workouts.append(name + "\n");
	
		for (Rating category : Rating.values())
		{
			workouts.append(category + ": " + workout[i] + "\n");
			i++;
		}
	}
}

