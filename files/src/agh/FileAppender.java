/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author patlas
 */
public class FileAppender {
    
    String dataFile = null;
    String channelFile = null;
    PrintWriter writer = null;
    
    public FileAppender(String dFile){
        Boolean newFile = false;
        dataFile = dFile;
		File file = new File(dFile);
		
		if(!file.exists())
		{
			try{
				file.createNewFile();
				newFile = true;
			}
			catch(IOException ioe){
		         logger.error("Natrafiono na blad podczas pr�by zapisu preferencji.");
		    	 ioe.printStackTrace();
			}
		}
		
		try {
				writer = new PrintWriter( new FileOutputStream(file, false) );	
				if(newFile != true)
				{

				}
				
				writer.close();
				logger.info("Aktualizacja preferencji zako�czona sukcesem.");
		
		} catch (FileNotFoundException e) {
			logger.fatal("Brak pliku preferencji lub plik otwarty.");
			e.printStackTrace();
		}
        
    }
    
    public addTransponder
    public addChannels
    
}
