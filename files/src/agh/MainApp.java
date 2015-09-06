package agh;

import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class MainApp {

	
	final static Logger logger = Logger.getLogger(MainApp.class);
	
	public static void createAndShowGUI() { 
		MyGUI gui = new MyGUI(); 
		gui.pack(); 
		gui.setVisible(true); 
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(400, 320);
		logger.info("Interfejs GUI zosta� uruchomiony.");
		} 
	
	
		public static void main(String[] args) { 
		
			PropertyConfigurator.configure("log4j.properties");
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() { 
		public void run() { createAndShowGUI(); } 
		}); 
		} 
		
		
		
		
	
}

