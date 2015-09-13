/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.utils;

/**
 *
 * @author Max
 */


import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;


public class DownloadPreview{

    final static Logger logger = Logger.getLogger(DownloadPreview.class);
    
    
    public JTable trTab = null;
    public JTable chTab = null;
    public DefaultTableModel trModel, chModel = null;
    
    String[] columnNamesTransponder ={
            "POSITION",
            "SATELLITE",
            "FREQ",
            "POLAR",
            "TRANSPONDER",
            "STREAM",
            "SYMBOL_RATE",
            "FEC"
        };


    String[] columnNamesChannels = {
        "NAME",
        "COUNTRY",
        "CATEGORY",
        "PACKET",
        "CODING",
        "SID",
        "VPID",
        "AUDIO",
        "PMT",
        "PCR, ",
        "TXT",		
        "LAST_UPDATE"
    };

    public DownloadPreview(JTable t, JTable c) {
        trTab = t;
        chTab = c;
        logger.info("Rozpoczęto wypełniani tabeli");
    }
    
    
    public void viewData(ArrayList<ArrayList<String>> tr, ArrayList<ArrayList<ArrayList<String>>> ch){
        String[][] dataTransponders, dataChannells;
        
        dataTransponders = readTransponder(tr);
        dataChannells = readChannel(ch);

        if(dataChannells.length == 0 || dataTransponders.length == 0)
        {
                System.out.println("NIE ma co wyœwietli");
                return; //DODAÆ LOG, nie ma co wyœwietlic
        }

        chModel = new DefaultTableModel(dataChannells, columnNamesChannels);
        trModel = new DefaultTableModel(dataTransponders, columnNamesTransponder);

        trTab.setModel(trModel);
        chTab.setModel(chModel);
        
        logger.info("Wypełniono tablice wpisami transponderów");
        logger.info("Wypełniono tablice wpisami kanałow");
        
    }

    private String[][] readTransponder(ArrayList<ArrayList<String>> array)
    {
        if(array.isEmpty()) ;//

        String [][] ret = new String[array.size()+1][array.get(0).size()+1];
        int index = 0;
        for(ArrayList<String> arr : array)
        {
            int subindex=0;
            for(String str : arr)
            {
                    ret[index][subindex++] = str;
            }
            index++;
        }

        logger.info("Odczyt bazy danych zakoñczy³ siê sukcesem.");

        return ret;
    }
    
    private String[][] readChannel(ArrayList<ArrayList<ArrayList<String>>> array)
    {
        if(array.isEmpty()) logger.fatal("No data to print!");//

        String [][] ret = new String[1][1];

        String [][] temp = null;
        int index = 0;
        
        for(ArrayList<ArrayList<String>> channels : array)
        {
            temp = new String[channels.size()+1][channels.get(0).size()+1];
            index=0;
            for(ArrayList<String>  ch : channels)
            {
                Boolean nullAdded = true;

                if(ch.size() == 10)//mamy radio
                {
                        nullAdded = false;
                }
                
                int i = 0;
                int subindex=0;
                for( String c : ch)
                {
                    temp[index][subindex++] = c;
                    if(nullAdded == false)
                    {
                            i+=2;
                            nullAdded = true;
                            continue;
                    }
                    i++;
                }
                index++;
            }	
            ret = mergeTable(ret, temp);
            
        }
        logger.info("Odczyt bazy danych zakoñczy³ siê sukcesem.");

        return ret;
    }
    
    
    private String[][] mergeTable(String[][] t1, String[][] t2)
    {   
        String[][] result = new String[t1.length + t2.length][];
        System.arraycopy(t1, 0, result, 0, t1.length);
        System.arraycopy(t2, 0, result, t1.length, t2.length);
        
        
        return result;   
    }
		
        
}