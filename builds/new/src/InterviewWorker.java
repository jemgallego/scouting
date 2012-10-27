import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingWorker;


public class InterviewWorker extends SwingWorker<Object, Object> {
	
	private static RookieClass rookies;
	private enum Intangibles {CON, GRE, LOY, PFW, PT, PER, DUR, WE, POP}; 
	private File directory;
	private BufferedWriter interviews;
	
	public InterviewWorker(File f)
	{
		directory = f;
		setProgress(0);
	}
	
	@Override
	public Object doInBackground() throws IOException
	{        
		conductInterviews();
        
        //set the status text in the main window back to idle
		MainWindow.GetInstance().SetStatusIdle();
        
		return null;
	}

	private void conductInterviews() throws IOException
	{
		rookies = new RookieClass(); // Generate ratings table for rookie class
		File files[] = directory.listFiles(); // Get all the files in the directory.
		
		for(File f: files)
		{
			String filename = f.getName();
			
			if (filename.endsWith(".txt"))
			{	
				BufferedReader br = new BufferedReader(new FileReader(f));
				
				// Start reading the interview file
				String str; 
				String teamName;
				int count = 0;
				
				teamName = br.readLine(); // read Team Name

				// Create a text file with the results for the respective team.
				interviews = new BufferedWriter(new FileWriter("results/interviews/" + teamName + ".txt"));
				interviews.append("\n" + teamName.toUpperCase() + "\n\n");
				
				while ((str = br.readLine()) != null)
				{						
					getInterviewResults(str);
					MainWindow.GetInstance().updateOutput("\n");
					
					count++; // keep track of # of players interviewed
				}
				br.close();
				interviews.close();
				MainWindow.GetInstance().updateOutput(filename + " -- " + count + "\n");
			}
		}	
	}
	
	public void getInterviewResults(String name) throws IOException
	{
		int[][] rating = rookies.get(name); // get the player's ratings
		int i=0;
		
		// Error check: Name
		if (rating == null) { 
			interviews.append("ERROR: Name Not Found! \n --/--\n");
			return;
		}
		interviews.append(name + "\n");
		
		interviews.append("IQ: " + rating[1][10] + "\n");
		
		for (Intangibles category : Intangibles.values())
		{
			interviews.append(category + ": " + rating[3][i] + "\n");
			i++;
		}

	}
}

