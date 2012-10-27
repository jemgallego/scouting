import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingWorker;

public class WorkoutWorker extends SwingWorker<Object, Object> {
	
	private static RookieClass rookies;
	private enum Scoring {FGJ, FT, FGI, FGD, FG3, SCR};
	private enum Offense {PAS, HDL, ORB, DRFL, IQ}; 
	private enum Defense {DRB, BLK, STL, DEF, DIS};
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
				workouts = new BufferedWriter(new FileWriter("results/workouts/" + teamName + ".txt"));
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
	}
	
	public void getWorkoutResults(String name) throws IOException
	{
		int[][] rating = rookies.get(name); // get the player's ratings
		int i;
		
		// Error check: Name
		if (rating == null) { 
			workouts.append("ERROR: Name Not Found! \n --/--\n");
			return;
		}
		workouts.append(name + "\n");
		
		i = 0;
		for (Scoring category : Scoring.values())
		{
			workouts.append(category + ": " + rating[1][i] + "\n");
			i++;
		}
		
		i = 6;
		for (Offense category : Offense.values())
		{
			workouts.append(category + ": " + rating[1][i] + "\n");
			i++;
		}

		i = 11;
		for (Defense category : Defense.values())
		{
			workouts.append(category + ": " + rating[1][i] + "\n");
			i++;
		}
	}
}

