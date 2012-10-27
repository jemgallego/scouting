package main;
// Mike's Super Awesome Scouting System
// Copyright 2012 Jem Gallego
// Version: 1.2


import java.io.*;

import javax.swing.*;

public class Main {
	
	public static void main(String[] args) throws IOException
	{	
        try{
            //set the look and feel to the system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //load the images
            //windowIcon = ImageIO.read(new File("src/images/mochaLogoOnlySmall.png"));
            //open the main window
		    SwingUtilities.invokeLater(MainWindow.GetInstance());
        }
        catch(Exception e){
            System.out.println("Unhandled Exception in main: " + e);
        }
	}   
}
