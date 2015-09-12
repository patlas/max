/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/*

long timestamp = Long.parseLong("1442095500") * 1000;

    SimpleDateFormat sdf = new SimpleDateFormat("MMM d H:mm");
    String date = sdf.format(timestamp);
    
        System.out.println(date);
*/
package agh;

import agh.exception.MyException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintWriter;
import org.apache.log4j.Logger;

/**
 *
 * @author patlas
 */
public class FileAppender {
    
    final static Logger logger = Logger.getLogger(FileAppender.class);
    
    private static String[] transponderTags = {
        "POSITION", "SATELLITE", "FREQ", "POLARISATION", "TRANSPONDER",
        "STREAM", "SYMBOL RATE", "FEC"
    };
    
    private static String[] channelTags = {
        "NAME", "COUNTRY", "CATEGORY", "PACKET", "CODING",
        "SID", "VPID", "AUDIO", "PMT", "PCR", "TXT", "LAST UPDATE"
    };
    
    
    String dataFile = null;
    String channelFile = null;
    public PrintWriter writer = null;
    File file = null;
    
    
    public FileAppender(String dFile){
        Boolean newFile = false;
        dataFile = dFile;
        file = new File(dFile);
        
        try{
            writer = new PrintWriter( new FileOutputStream(file, false) );
            if(!file.exists())
            {
                try{
                        file.createNewFile();
                        newFile = true;
                }
                catch(IOException ioe){
                 logger.error("Natrafiono na problem podczas tworzenia pliku.");
                 ioe.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
                logger.fatal("Brak pliku lub plik otwarty.");
                e.printStackTrace();
        }

    }
    
    public void appendTransponders(ArrayList<ArrayList<String>> transponders) throws MyException
    {			
        int index = 0;

        for(ArrayList<String>  tr : transponders)
        {
            index++;
                writer.println("<TRANSPONDER: "+index+">");

                int i = 0;
                for( String t : tr)
                {
                    writer.println("\t<"+transponderTags[i]+">"+t+"</"+transponderTags[i]+">");
                    i++;
                }
                writer.println("</TRANSPONDER>");
                writer.println("");
        }	
        
        logger.info("Zapis do pliku zakończony sukcesem.");
    }
	
	
    public void appendChannels(ArrayList<ArrayList<ArrayList<String>>> channelGroups) throws MyException
    {
        int index = 0;
        for(ArrayList<ArrayList<String>> channels : channelGroups)
        {
            index++;
            for(ArrayList<String>  ch : channels)
            {
                Boolean nullAdded = true;

                if(ch.size() == 10)//mamy radio
                {
                        nullAdded = false;
                }

                writer.println("<CHANEL>");
                int i = 0;
                for( String c : ch)
                {
                    writer.println("\t<"+channelTags[i]+">"+c+"</"+channelTags[i]+">");
                    if(nullAdded == false)
                    {
                            i+=2;
                            nullAdded = true;
                            continue;
                    }
                    i++;
                }
                writer.println("</CHANEL>");
                writer.println("");
            }	
        }

    logger.info("Zapis do pliku zakończony sukcesem.");
    }	
    
}
