package main;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import func.BasicSheetWorker;
import func.BigBoardWorker;
import func.DraftCampWorker;
import func.FinderWorker;
import func.PointsWorker;
import func.ProfileWorker;
import func.TrackerWorker;

import scouting.InterviewWorker;
import scouting.ScoutingWorker;
import scouting.WorkoutWorker;

import java.io.File;
import java.io.IOException;
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
    private final JMenu extrasMenu = new JMenu("Extras");
    private final JMenu helpMenu = new JMenu("Help");
    
    // main components
    private final JLabel selectedFolderLabel = new JLabel("Select Folder: " + currentDir);
    private final JButton scoutButton = new JButton("Scout");
    private final JButton interviewButton = new JButton("Interview");
    private final JButton workoutButton = new JButton("Workout");
    private final JButton bigBoardButton = new JButton("Big Board");
    private final JButton trackerButton = new JButton("Tracker");
    private final JButton pointsButton = new JButton("Points");
	private static JTextArea output = new JTextArea(20,20); // Scanner output
    private final JScrollPane scrollPane = new JScrollPane(output);

    // status components
	private final JProgressBar progressBar = new JProgressBar(0,100);
	private final JLabel statusLabel = new JLabel("Status: Idle");

	// layout boxes
    private final Box selectedFolderBox = new Box(BoxLayout.X_AXIS);
    private final Box buttonBox = new Box(BoxLayout.X_AXIS);
    private final Box miscBox = new Box(BoxLayout.X_AXIS);
    private final Box outputLabelBox = new Box(BoxLayout.X_AXIS);
    private final Box statusBox = new Box(BoxLayout.X_AXIS);
    private final Box mainBox = new Box(BoxLayout.Y_AXIS);
    
    // private to preserve singleton class property
    private MainWindow()
    {	
    	Color menuBarColor = new Color(214,217,223);
    	
    	// build the file menu
    	JMenuItem selectFolder = new JMenuItem("Select Folder");
    	JMenuItem exit = new JMenuItem("Exit");
    	
    	selectFolder.addActionListener(new SelectFolderButtonListener());
    	selectFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.META_DOWN_MASK));
    	
    	exit.addActionListener(new ExitButtonListener());
    	exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.META_DOWN_MASK));
    	
    	fileMenu.setBackground(menuBarColor);
    	fileMenu.add(selectFolder);
    	fileMenu.add(exit);
    	
    	// build the extras menu
    	JMenuItem profiles = new JMenuItem ("Draft Profiles");
    	JMenuItem draftCamp = new JMenuItem ("Pre-Draft Camp");
    	JMenuItem finder = new JMenuItem("Prospect Finder");
    	JMenuItem basicSheet = new JMenuItem ("Basic Sheet");
    	JMenuItem points = new JMenuItem ("Points");
    	JMenuItem tracker = new JMenuItem ("Tracker");
    	
    	finder.addActionListener(new FinderButtonListener());
    	finder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.META_DOWN_MASK));
    	
    	basicSheet.addActionListener(new BasicSheetButtonListener());
    	basicSheet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.META_DOWN_MASK));
    	
    	points.addActionListener(new PointsButtonListener());
    	points.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.META_DOWN_MASK));
    	
    	tracker.addActionListener(new TrackerButtonListener());
    	tracker.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.META_DOWN_MASK));
    	
    	draftCamp.addActionListener(new DraftCampListener());
    	draftCamp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK));
    	
    	profiles.addActionListener(new DraftProfilesListener());
    	
    	extrasMenu.setBackground(menuBarColor);
    	extrasMenu.add(profiles);
    	extrasMenu.add(draftCamp);
    	extrasMenu.add(finder);
    	extrasMenu.add(basicSheet);
    	extrasMenu.add(points);
    	extrasMenu.add(tracker);
    	
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
    	menuBar.add(extrasMenu);
    	menuBar.add(helpMenu);
    	
    	output.setEditable(false);
    }
	
	public void run()
	{
        // set the icon image and layout manager of the rootPane
        // f.setIconImage(Program.windowIcon);
        f.setLayout(new BorderLayout());

        // dimensions used for empty fillers
        Dimension minDim = new Dimension(25, 10);
        Dimension prefDim = new Dimension(150, 10);
        Dimension maxDim = new Dimension(Short.MAX_VALUE, 10);

        // configure component sizes
     
        // border adjustments
      
        mainBox.setBorder(new EmptyBorder(new Insets(0,10,10,10)));
        selectedFolderLabel.setBorder(new EmptyBorder(new Insets(0,6,10,0)));
        buttonBox.setBorder(new EmptyBorder(new Insets(0,0,10,0)));
        miscBox.setBorder(new EmptyBorder(new Insets(0,0,10,0)));
        statusBox.setBorder(new EmptyBorder(new Insets(10,0,0,0)));
        selectedFolderBox.setBorder(new EmptyBorder(new Insets(10,0,0,10)));
        outputLabelBox.setBorder(new EmptyBorder(new Insets(0,0,0,10)));
        
        // alignment adjustments (all components must have the same alignment
        // when using box layout to prevent left aligning to the center of
        // center aligned components)
        selectedFolderBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        miscBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        outputLabelBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
        JLabel dividerLabel = new JLabel(" | ");
        
        selectedFolderBox.add(selectedFolderLabel);
        selectedFolderBox.add(new Box.Filler(minDim, prefDim, maxDim));
        
        buttonBox.add(scoutButton);
        buttonBox.add(interviewButton);
        buttonBox.add(workoutButton);
        buttonBox.add(dividerLabel);
        buttonBox.add(bigBoardButton);
        buttonBox.add(trackerButton);
        
        // statusBox.add(statusLabel);
        // statusBox.add(new Box.Filler(minDim, prefDim, maxDim));

        // add everything to the main box
        mainBox.add(selectedFolderBox);
        mainBox.add(buttonBox);
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
    
    //**************EVENT HANDLERS**************//
	
    class ExitButtonListener implements ActionListener 
    {
    	@Override
	    public void actionPerformed(ActionEvent e)
	    {
            System.exit(0); // Exit program.
	    }
    }
    
    class FileChooserListener implements ActionListener 
    {
    	@Override
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
    
    class SelectFolderButtonListener implements ActionListener
    {
        JFileChooser fileChooser;
	    
	    public SelectFolderButtonListener() 
	    {
	    	// limit selection to folders
	    	fileChooser = new JFileChooser(".");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.addActionListener(new FileChooserListener());
	    }
	    
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		    // show the fileChooser window
            fileChooser.showOpenDialog(MainWindow.GetInstance().f);
	    }
    }
    
    class FinderButtonListener implements ActionListener
    {	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			clearOutput();
			
			String findPlayer = JOptionPane.showInputDialog("Enter prospect name:");
			FinderWorker worker = new FinderWorker(findPlayer);
			
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
    
    
    class ScoutButtonListener implements ActionListener 
    {	
    	@Override
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
    	@Override
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
    	@Override
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
    
    class DraftProfilesListener implements ActionListener
    {	
    	@Override
	    public void actionPerformed(ActionEvent e)
	    {
	    	// Clear the textarea.
            clearOutput();
            
            ProfileWorker worker = new ProfileWorker();   
            
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
    
    class DraftCampListener implements ActionListener
    {	
    	@Override
	    public void actionPerformed(ActionEvent e)
	    {
	    	// Clear the textarea.
            clearOutput();
            
            DraftCampWorker worker = new DraftCampWorker();   
            
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
    	@Override
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
    	@Override
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
    	@Override
	    public void actionPerformed(ActionEvent e)
	    {
	    	// Clear the textarea.
            clearOutput();

            TrackerWorker worker = null;
			try {
				worker = new TrackerWorker();
			} catch (IOException e1) {
				e1.printStackTrace();
				MainWindow.GetInstance().updateOutput("\n===== START ERROR MESSAGE =====\n\n" +
						"ERROR: Something went wring :( \n\n" + 
						"\n=====  END ERROR MESSAGE  =====\n\n" );
			}                

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
    	@Override
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
    
    //**************CLASS FUNCTIONS**************//
    
    public void setDirectory(String fileName) 
    {
        currentDir = fileName;
        
        // update the selected file label
        selectedFolderLabel.setText("Selected Folder: " + currentDir); 
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

