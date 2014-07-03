// Mike's Super Awesome Scouting System
// Copyright 2012 Jem Gallego
// Version: 1.3.2

package main;

import java.io.*;
import javax.swing.*;

public class Main {
	
	public static void main(String[] args) throws IOException 
	{
        try 
        {
        	/* BLACK THEME
        	Color myBlack = new Color(24,24,24);
        	Color myOrange = new Color(243,170,0);
        	
            //set the look and feel to the system default
        	UIManager.put("control", myBlack);
        	UIManager.put("nimbusBase", myBlack);
        	UIManager.put("nimbusFocus", myOrange);
        	UIManager.put("nimbusBlueGrey", myBlack);
        	UIManager.put("nimbusSelectionBackground", myBlack);
        
        	UIManager.put("text", Color.WHITE);
        	
        	UIManager.put("nimbusSelection", myOrange);
        	
        	UIManager.put("TextArea.background", new Color(64,64,64));
        	
        	for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
        	    if ("Nimbus".equals(info.getName())) {
        	        UIManager.setLookAndFeel(info.getClassName());
        	        break;
        	    }
        	}*/
        	
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    SwingUtilities.invokeLater(MainWindow.GetInstance());
        }
        catch(Exception e) {}
	}   
}
