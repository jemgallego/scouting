import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.io.File;
import java.lang.Short;

//singleton class, final, no sub-classing of main window allowed
public final class MainWindow implements Runnable{

    //reference to the singleton MainWindow
    private static MainWindow mwRef = null;

    //method called to get the instance of the mainWindow
    //call this instead of constructor
    public static MainWindow GetInstance(){
        if(mwRef == null){
            mwRef = new MainWindow();
        }
        return mwRef;
    }
 
	///Event Handlers///
	
	//callback for click event on the exit menu item
    class ExitButtonListener implements ActionListener{
	    //callback handler
	    public void actionPerformed(ActionEvent e){
            //Exit program
            System.exit(0);
	    }
    }
    
    //callback for events on the filechooser window
    class FileChooserListener implements ActionListener{
        //the action listener function
        public void actionPerformed(ActionEvent actionEvent){
            //alias to the file chooser to enable access to its data
            JFileChooser fileChooser = (JFileChooser) actionEvent.getSource();

            String command = actionEvent.getActionCommand();
            if(command.equals(JFileChooser.APPROVE_SELECTION)){
                //get the selected filename
                directory = fileChooser.getSelectedFile();
                String folderName = directory.getName();
                //update main window texts
                MainWindow.GetInstance().setDirectory(folderName);
            }
        }
    }
    
    //choose file click event
    class ChooseFileButtonListener implements ActionListener{
        JFileChooser fileChooser;
	    //constructor
	    public ChooseFileButtonListener(){
            fileChooser = new JFileChooser(".");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.addActionListener(new FileChooserListener());
	    }
	    //callback handler
	    public void actionPerformed(ActionEvent e){
		    //now that the button has been clicked, show the jFileChooser
            fileChooser.showOpenDialog(MainWindow.GetInstance().f);
	    }
    }
    
    class ScoutButtonListener implements ActionListener
    {	
	    //callback handler
	    public void actionPerformed(ActionEvent e){
	    	 // Clear the textarea.
            clearOutput();
               
            if(lastDir.equals(currentDir))
            	updateOutput("You already finished scouting this period");
            else
            {
            //create, register callbacks and run the asynchronous worker
            ScoutingWorker worker = new ScoutingWorker(getDirectory());                
            //callback for asynchronous update of the progress bar
		    worker.addPropertyChangeListener(new PropertyChangeListener(){
			    public void propertyChange(PropertyChangeEvent evt){
				    if("progress".equals(evt.getPropertyName())){
			    		MainWindow.GetInstance().SetProgressBar(
                                            (Integer)evt.getNewValue());
				    }
			    }
		    });
            worker.execute(); //schedule asynchronous run
            lastDir = currentDir;
            }
	    }
    }
    
    class InterviewButtonListener implements ActionListener
    {	
    	
	    //callback handler
	    public void actionPerformed(ActionEvent e){
	    	 // Clear the textarea.
            clearOutput();
            
            //create, register callbacks and run the asynchronous worker
            InterviewWorker worker = new InterviewWorker(getDirectory());                
            //callback for asynchronous update of the progress bar
		    worker.addPropertyChangeListener(new PropertyChangeListener(){
			    public void propertyChange(PropertyChangeEvent evt){
				    if("progress".equals(evt.getPropertyName())){
			    		MainWindow.GetInstance().SetProgressBar(
                                            (Integer)evt.getNewValue());
				    }
			    }
		    });
            worker.execute(); //schedule asynchronous run
	    }
    }
    
    class WorkoutButtonListener implements ActionListener
    {	
    
	    //callback handler
	    public void actionPerformed(ActionEvent e){
	    	 // Clear the textarea.
            clearOutput();
            
            //create, register callbacks and run the asynchronous worker
            WorkoutWorker worker = new WorkoutWorker(getDirectory());                
            //callback for asynchronous update of the progress bar
		    worker.addPropertyChangeListener(new PropertyChangeListener(){
			    public void propertyChange(PropertyChangeEvent evt){
				    if("progress".equals(evt.getPropertyName())){
			    		MainWindow.GetInstance().SetProgressBar(
                                            (Integer)evt.getNewValue());
				    }
			    }
		    });
            worker.execute(); //schedule asynchronous run
	    }
    }
    
    class BasicSheetButtonListener implements ActionListener
    {	
	    //callback handler
	    public void actionPerformed(ActionEvent e){
	    	 // Clear the textarea.
            clearOutput();
            
            //create, register callbacks and run the asynchronous worker
            BasicSheetWorker worker = new BasicSheetWorker();                
            //callback for asynchronous update of the progress bar
		    worker.addPropertyChangeListener(new PropertyChangeListener(){
			    public void propertyChange(PropertyChangeEvent evt){
				    if("progress".equals(evt.getPropertyName())){
			    		MainWindow.GetInstance().SetProgressBar(
                                            (Integer)evt.getNewValue());
				    }
			    }
		    });
            worker.execute(); //schedule asynchronous run
	    }
    }
    
    ///class attributes///
    String currentDir = "---";
    String lastDir = "none";
    private File directory;
    
	//window components
    JFrame f = new JFrame("Mike's Scouting v1.2");
 
    //Scouting components
    private final JLabel selectedFileLabel = new JLabel("Select Folder: " + currentDir);
    private final JButton scoutButton = new JButton("Scout");
    private final JButton interviewButton = new JButton("Interview");
    private final JButton workoutButton = new JButton("Workout");
    private final JButton basicSheetButton = new JButton("Generate Basic Sheet");
	private static JTextArea output = new JTextArea(20,20); // Scanner output
    private final JScrollPane scrollPane = new JScrollPane(output);

    //status components
	private final JProgressBar progressBar = new JProgressBar(0,100);
	private final JLabel statusLabel = new JLabel("Status: Idle");

	//Layout boxes
    private final Box fileBox = new Box(BoxLayout.X_AXIS);
    private final Box scoutBox = new Box(BoxLayout.X_AXIS);
    private final Box miscBox = new Box(BoxLayout.X_AXIS);
    private final Box outputLabelBox = new Box(BoxLayout.X_AXIS);
    private final Box statusBox = new Box(BoxLayout.X_AXIS);
    private final Box mainBox = new Box(BoxLayout.Y_AXIS);
    //Menu Components
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu fileMenu = new JMenu("File");
    private final JMenu helpMenu = new JMenu("Help");

    //private to preserve singleton class property
    private MainWindow(){
    	//build the file menu
    	JMenuItem scoutingFile = new JMenuItem("Open File");
    	JMenuItem exit = new JMenuItem("Exit");
    	scoutingFile.addActionListener(new ChooseFileButtonListener());
    	exit.addActionListener(new ExitButtonListener());
    	fileMenu.add(scoutingFile);
    	fileMenu.add(exit);
    	
    	//build the help menu
    	JMenuItem FAQ = new JMenuItem("FAQ");
    	JMenuItem manual = new JMenuItem("Scouting Manual");
    	//FAQ.addActionListener(new FAQListener());
    	//manual.addActionListner(new manualListener());   	
    	helpMenu.add(FAQ);
    	helpMenu.add(manual);

    	menuBar.add(fileMenu);
    	menuBar.add(helpMenu);
    	
    	output.setEditable(false);
    }
	
	public void run(){
        //set the icon image and layout manager of the rootPane
        //f.setIconImage(Program.windowIcon);
        f.setLayout(new BorderLayout());

        //dimensions used for empty fillers
        Dimension minDim = new Dimension(25, 10);
        Dimension prefDim = new Dimension(150, 10);
        Dimension maxDim = new Dimension(Short.MAX_VALUE, 10);

        //configure component sizes
     
        //border adjustments
        mainBox.setBorder(new EmptyBorder(new Insets(0,10,10,10)));
        selectedFileLabel.setBorder(new EmptyBorder(new Insets(0,0,5,0)));
        scoutBox.setBorder(new EmptyBorder(new Insets(0,0,10,0)));
        miscBox.setBorder(new EmptyBorder(new Insets(0,0,10,0)));
        statusBox.setBorder(new EmptyBorder(new Insets(10,0,0,0)));
        fileBox.setBorder(new EmptyBorder(new Insets(10,0,0,10)));
        outputLabelBox.setBorder(new EmptyBorder(new Insets(0,0,0,10)));
        
        //alignment adjustments (all components must have the same alignment
        //when using box layout to prevent left aligning to the center of
        //center aligned components)
        fileBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        scoutBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        miscBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        outputLabelBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//connect all of the window components (and add padding)
        fileBox.add(selectedFileLabel);
        fileBox.add(new Box.Filler(minDim, prefDim, maxDim));
        
        scoutBox.add(scoutButton);
        scoutBox.add(interviewButton);
        scoutBox.add(workoutButton);
        
        miscBox.add(basicSheetButton);
        
        statusBox.add(statusLabel);
        statusBox.add(new Box.Filler(minDim, prefDim, maxDim));

        //add everything to the main box
        mainBox.add(fileBox);
        mainBox.add(scoutBox);
        mainBox.add(miscBox);
        mainBox.add(outputLabelBox);
		mainBox.add(scrollPane);
		mainBox.add(statusBox);

		f.add(menuBar, BorderLayout.NORTH);
        f.add(mainBox, BorderLayout.CENTER);
		f.add(progressBar, BorderLayout.PAGE_END);
		
		// register listeners
		scoutButton.addActionListener(new ScoutButtonListener());
		interviewButton.addActionListener(new InterviewButtonListener());
		workoutButton.addActionListener(new WorkoutButtonListener());
		basicSheetButton.addActionListener(new BasicSheetButtonListener());
		
		//finish setting up the window
		f.pack();
        f.setSize(500, 550);
        f.setLocation(600, 200);
        f.setLocationByPlatform(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

    //update the selected file JLabel text
    public void setDirectory(String fileName){
        currentDir = fileName;
        selectedFileLabel.setText("Selected Folder: " + currentDir);
    }
    
    public File getDirectory()
    {
    	return directory;
    }
    
    public void updateOutput(String str)
    {
    	output.append(str);
    }
    
    public void clearOutput()
    {
    	output.setText(null);
    }
    
    //update the progress bar
    public void SetProgressBar(int val){
        if(val > 99)
            progressBar.setValue(0);
        else
            progressBar.setValue(val);
    }
    //functions for updating the status texts
    public void SetStatusLabel(String in){
    	statusLabel.setText("Status: " + in);
    }
    public void SetStatusIdle(){
    	SetStatusLabel("Idle");
    }
  
}

