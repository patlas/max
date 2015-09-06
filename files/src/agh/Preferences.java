package agh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import agh.utils.TwoTypeList;

public class Preferences {

	
	static String prefFile = ".preferences";
	static String dir = "";
	public static List<Preference> prefList = new ArrayList<Preference>();
	
	final static Logger logger = Logger.getLogger(Preferences.class);
	
	private static String defaultBrowser = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	
	public static void saveSettings()
	{
		Boolean newFile = false;
		File file = new File(dir+prefFile);
		
		if(!file.exists())
		{
			try{
				file.createNewFile();
				newFile = true;
			}
			catch(IOException ioe){
		         logger.error("Natrafiono na b��d podczas pr�by zapisu preferencji.");
		    	 ioe.printStackTrace();
			}
		}
		
		try {
				PrintWriter writer = new PrintWriter( new FileOutputStream(file, false) );	
				if(newFile == true)
				{
					writer.println("<AGENT>");
					writer.println(defaultBrowser);
					writer.println("</AGENT>");
					writer.println("");
				}
				else
				{
					writer.println("<AGENT>");
					writer.println(Preference.getAgent());
					writer.println("</AGENT>");
					writer.println("");
					
					for(Preference pref : prefList )
					{
						writer.println("<PAGE>");
						writer.println("\t<ADDR>"+pref.getAddr()+"</ADDR>");
						writer.println("\t<TIMEOUT>"+pref.getTimeout()+"</TIMEOUT>");
						writer.println("</PAGE>");
						writer.println("");
					}
				}
				
				writer.close();
				logger.info("Aktualizacja preferencji zako�czona sukcesem.");
		
		} catch (FileNotFoundException e) {
			logger.fatal("Brak pliku preferencji lub plik otwarty.");
			e.printStackTrace();
		}
	}
	
	private static TwoTypeList<String,Integer> readSettings() // PRZEROBI�
	{
		
		File file = new File(dir+prefFile);
			
		if(!file.exists())
		{
			logger.warn("Plik preferencji nie istnieje lub plik otwarty");
			
			
			saveSettings();
			
			logger.info("Stworzono domy�lny plik preferencji.");
			//return null;
		}
		
	
		Document doc;
		TwoTypeList<String,Integer> lst =  new TwoTypeList<String, Integer>();
		try {
			
			doc = Jsoup.parse(file, "UTF-8");
			ArrayList<Element> pageList = new ArrayList<Element>();
			
			Preference.setAgent(doc.getElementsByTag("AGENT").get(0).text().toString());
			pageList = doc.getElementsByTag("PAGE");
			
			
			for( Element page : pageList )
			{
				lst.addItem(page.select("ADDR").text(), Integer.parseInt(page.select("TIMEOUT").text(),10));
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lst;
	}
	
	public static void loadSettings()
	{
		prefList.clear();
		TwoTypeList<String,Integer> tmp = new TwoTypeList<String, Integer>();
		tmp = Preferences.readSettings();
		
		for(int index=0; index<tmp.size; index++){
			
			new Preference(tmp.getAitem(index), tmp.getBitem(index));
						
		}
		
		logger.info("Wczytywanie preferencji zako�czono sukcesem.");
	}
	
}
