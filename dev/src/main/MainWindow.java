package main;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import func.BasicSheetWorker;
import func.BigBoardWorker;

import scouting.InterviewWorker;
import scouting.ScoutingWorker;
import scouting.WorkoutWorker;
import tracker.PointsWorker;
import tracker.TrackerWorker;

import java.io.File;
import java.lang.Short;

// singleton class, final, no sub-classing of main window allowed
public final class MainWindow implements Runnable {

    // reference to the singleton MainWindow
    private static MainWindow mwRef = null;

    // method called to get the instance of the mainWindow
    // call this instead of constructor
    public static MainWindow GetInstance()
    {
        if(mwRef == null){
            mwRef = new MainWindow();
        }
        return mwRef;
    }
    
    // class attributes
    private String currentDir = "---";
    private File directory;
    
	// window components
    JFrame f = new JFrame("Mike's Scouting v1.2");
 
    // menu components
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu fileMenu = new JMenu("File");
    private final JMenu helpMenu = new JMenu("Help");
    
    // main components
    private final JLabel selectedFileLabel = new JLabel("Select Folder: " + currentDir);
    private final JButton scoutButton = new JButton("Scout");
    private final JButton interviewButton = new JButton("Interview");
    private final JButton workoutButton = new JButton("Workout");
    private final JButton basicSheetButton = new JButton("SpreadSheet");
    private final JButton bigBoardButton = new JButton("Big Board");
    private final JButton trackerButton = new JButton("Tracker");
    private final JButton pointsButton = new JButton("Points");
	private static JTextArea output = new JTextArea(20,20); // Scanner output
    private final JScrollPane scrollPane = new JScrollPane(output);

    // status components
	private final JProgressBar progressBar = new JProgressBar(0,100);
	private final JLabel statusLabel = new JLabel("Status: Idle");

	// layout boxes
    private final Box fileBox = new Box(BoxLayout.X_AXIS);
    private final Box scoutBox = new Box(BoxLayout.X_AXIS);
    private final Box miscBox = new Box(BoxLayout.X_AXIS);
    private final Box outputLabelBox = new Box(BoxLayout.X_AXIS);
    private final Box statusBox = new Box(BoxLayout.X_AXIS);
    private final Box mainBox = new Box(BoxLayout.Y_AXIS);
    
    // private to preserve singleton class property
    private MainWindow()
    {	
    	// build the file menu
    	Color menuBarColor = new Color(214,217,223);
    	JMenuItem scoutingFile = new JMenuItem("Open File");
    	JMenuItem exit = new JMenuItem("Exit");
    	
    	scoutingFile.addActionListener(new ChooseFileButtonListener());
    	scoutingFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.META_DOWN_MASK));
    	
    	exit.addActionListener(new ExitButtonListener());
    	exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.META_DOWN_MASK));
    	
    	fileMenu.setBackground(menuBarColor);
    	fileMenu.add(scoutingFile);
    	fileMenu.add(exit);
    	
    	// build the help menu
    	JMenuItem FAQ = new JMenuItem("FAQ");
    	JMenuItem manual = new JMenuItem("Scouting Manual");
    	
    	// FAQ.addActionListener(new FAQListener());
    	// manual.addActionListner(new manualListener());   	
    	
    	helpMenu.setBackground(menuBarColor);
    	helpMenu.add(FAQ);
    	helpMenu.add(manual);

    	menuBar.setBackground(menuBarColor);
    	menuBar.add(fileMenu);
    	menuBar.add(helpMenu);
    	
    	output.setEditable(false);
    }
	
	public void run()
	{
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
        miscBox.add(bigBoardButton);
        miscBox.add(trackerButton);
        miscBox.add(pointsButton);
        
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
		bigBoardButton.addActionListener(new BigBoardButtonListener());
		trackerButton.addActionListener(new TrackerButtonListener());
		pointsButton.addActionListener(new PointsButtonListener());
		
		//finish setting up the window
		f.pack();
        f.setSize(500, 600);
        f.setLocation(600, 200);
        f.setLocationByPlatform(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
    
    //**************************//
	// 		Event Handlers		//
	//**************************//
	
    class ExitButtonListener implements ActionListener 
    {
	    public void actionPerformed(ActionEvent e)
	    {
            System.exit(0); // Exit program.
	    }
    }
    
    class FileChooserListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent actionEvent) 
        {  
            JFileChooser fileChooser = (JFileChooser) actionEvent.getSource();
            String command = actionEvent.getActionCommand();
            
            // If pressed OK...
            if(command.equals(JFileChooser.APPROVE_SELECTION))
            {
                directory = fileChooser.getSelectedFile();
                String folderName = directory.getName();
                //show selected Folder in MainWindow.
                MainWindow.GetInstance().setDirectory(folderName);
            }
        }
    }
    
    class ChooseFileButtonListener implements ActionListener
    {
        JFileChooser fileChooser;
	    
	    public ChooseFileButtonListener() 
	    {
	    	// limit selection to folders
	    	fileChooser = new JFileChooser(".");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.addActionListener(new FileChooserListener());
	    }
	    
	    public void actionPerformed(ActionEvent e)
	    {
		    // show the fileChooser window
            fileChooser.showOpenDialog(MainWindow.GetInstance().f);
	    }
    }
    
    class ScoutButtonListener implements ActionListener 
    {	
	    public void actionPerformed(ActionEvent e)
	    {
	    	// Clear the text area.
            clearOutput();
         
            ScoutingWorker worker = new ScoutingWorker(getDirectory());
            
            // progress bar
		    worker.addPropertyChangeListener(new PropertyChangeListener() {
			    public void propertyChange(PropertyChangeEvent evt) {
				    if("progress".equals(evt.getPropertyName())) {
			    		MainWindow.GetInstance().setProgressBar((Integer)evt.getNewValue());
				    }
			    }
		    });
		    
            worker.execute(); //schedule asynchronous run
	    }
    }
    
    class InterviewButtonListener implements ActionListener
    {	
	    public void actionPerformed(ActionEvent e)
	    {
	    	// Clear the textarea.
            clearOutput();
   
            InterviewWorker worker = new InterviewWorker(getDirectory());                
   
		    worker.addPropertyChangeListener(new PropertyChangeListener() {
			    public void propertyChange(PropertyChangeEvent evt) {
				    if("progress".equals(evt.getPropertyName())) {
			    		MainWindow.GetInstance().setProgressBar((Integer)evt.getNewValue());
				    }
			    }
		    });
		    
            worker.execute(); //schedule asynchronous run
	    }
    }
    
    class WorkoutButtonListener implements ActionListener
    {	
	    public void actionPerformed(ActionEvent e)
	    {
	    	// Clear the textarea.
            clearOutput();
            
            WorkoutWorker worker = new WorkoutWorker(getDirectory());   
            
		    worker.addPropertyChangeListener(new PropertyChangeListener() {
			    public void propertyChange(PropertyChangeEvent evt) {
				    if("progress".equals(evt.getPropertyName())) {
			    		MainWindow.GetInstance().setProgressBar((Integer)evt.getNewValue());
				    }
			    }
		    });
		    
            worker.execute(); //schedule asynchronous run
	    }
    }
    
    class BasicSheetButtonListener implements ActionListener
    {	
	    public void actionPerformed(ActionEvent e)
	    {
	    	// Clear the textarea.
            clearOutput();

            BasicSheetWorker worker = new BasicSheetWorker();                

		    worker.addPropertyChangeListener(new PropertyChangeListener() {
			    public void propertyChange(PropertyChangeEvent evt) {
				    if("progress".equals(evt.getPropertyName())) {
			    		MainWindow.GetInstance().setProgressBar((Integer)evt.getNewValue());
				    }
			    }
		    });
		    
            worker.execute(); //schedule asynchronous run
	    }
    }
    
    class BigBoardButtonListener implements ActionListener
    {	
	    public void actionPerformed(ActionEvent e)
	    {
	    	// Clear the textarea.
            clearOutput();

            BigBoardWorker worker = new BigBoardWorker();                

		    worker.addPropertyChangeListener(new PropertyChangeListener() {
			    public void propertyChange(PropertyChangeEvent evt) {
				    if("progress".equals(evt.getPropertyName())) {
			    		MainWindow.GetInstance().setProgressBar((Integer)evt.getNewValue());
				    }
			    }
		    });
		    
            worker.execute(); //schedule asynchronous run
	    }
    }
    
    class TrackerButtonListener implements ActionListener
    {	
	    public void actionPerformed(ActionEvent e)
	    {
	    	// Clear the textarea.
            clearOutput();

            TrackerWorker worker = new TrackerWorker();                

		    worker.addPropertyChangeListener(new PropertyChangeListener() {
			    public void propertyChange(PropertyChangeEvent evt) {
				    if("progress".equals(evt.getPropertyName())) {
			    		MainWindow.GetInstance().setProgressBar((Integer)evt.getNewValue());
				    }
			    }
		    });
		    
            worker.execute(); //schedule asynchronous run
	    }
    }
    
    class PointsButtonListener implements ActionListener
    {	
	    public void actionPerformed(ActionEvent e)
	    {
	    	// Clear the textarea.
            clearOutput();

            PointsWorker worker = new PointsWorker();                

		    worker.addPropertyChangeListener(new PropertyChangeListener() {
			    public void propertyChange(PropertyChangeEvent evt) {
				    if("progress".equals(evt.getPropertyName())) {
			    		MainWindow.GetInstance().setProgressBar((Integer)evt.getNewValue());
				    }
			    }
		    });
		    
            worker.execute(); //schedule asynchronous run
	    }
    }
    
    //**************************//
	// 		Class Functions		//
	//**************************//
    
    //update the selected file label 
    public void setDirectory(String fileName) 
    {
        currentDir = fileName;
        selectedFileLabel.setText("Selected Folder: " + currentDir);
    }
    
    // get selected folder
    public File getDirectory()
    {
    	return directory;
    }
    
    // update text area
    public void updateOutput(String str)
    {
    	output.append(str);
    }
    
    // clear the text area
    public void clearOutput()
    {
    	output.setText(null);
    }
    
    //update the progress bar
    public void setProgressBar(int val){
        if(val > 99)
            progressBar.setValue(0);
        else
            progressBar.setValue(val);
    }

    // update status label
    public void setStatusLabel(String in)
    {
    	statusLabel.setText("Status: " + in);
    }
    
    // set status to Idle
    public void SetStatusIdle()
    {
    	setStatusLabel("Idle");
    }
}

