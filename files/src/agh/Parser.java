package agh;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Parser {
	
	private File file = null;
	final static Logger logger = Logger.getLogger(Parser.class);
	
	public Parser(Downloader webpage)
	{
		if(webpage.isDownloaded == true)
		{
			file = webpage.getFile(); //DODA� LOG!!
			logger.info("Pobieranie pliku preferencji zakończone sukcesem.");
		}

	}
	
	public Parser(File f)
	{
		file = f;
	}
	
	public Parser(String fName)
	{
		file = new File(fName);
	}
	
	public ArrayList<ArrayList<String>> getTransponder(){
		
		Document doc;
		
		ArrayList<ArrayList<String>> parsedTransponders = new ArrayList<ArrayList<String>>();
		
		try {
			doc = Jsoup.parse(file, "UTF-8");
			ArrayList<Element> transponderList = new ArrayList<Element>();
			transponderList = doc.getElementsByClass("frq");
			
			ArrayList<String> transponderParams; 
				
			for(Element transponder : transponderList)
			{
				transponderParams = new ArrayList<String>();
				for(Element params : transponder.getElementsByClass("bld"))
				{
					transponderParams.add(params.text());
				}
				parsedTransponders.add(transponderParams);
			}
			
		} catch (IOException e) {
			logger.fatal("Plik preferencji nie został wczytany lub nie istnieje");
			e.printStackTrace();
		}
		
		return parsedTransponders;
	}
	
	
	
public ArrayList<ArrayList<ArrayList<String>>> getChannels(){
		
		Document doc;
		
		ArrayList<ArrayList<ArrayList<String>>> parsedTranspondersChannelList = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<String>> channelGroups;
		ArrayList<String> channelList;
		
		try {
			doc = Jsoup.parse(file, "UTF-8");
			ArrayList<Element> transponderChannelList = new ArrayList<Element>();
			transponderChannelList = doc.getElementsByClass("fl");
			
			for(Element transponderCL : transponderChannelList)
			{
				channelGroups = new ArrayList<ArrayList<String>>();
				for(Element tCL : transponderCL.getElementsByTag("tr"))
				{
					int index = 0;
					channelList = new ArrayList<String>();
					for(Element tCG : tCL.getElementsByTag("td"))
					{
						if(index++ < 2) continue; //pomijanie 2 pierwszych kolumn "�mieci"
						channelList.add(tCG.text());
					}
					channelGroups.add(channelList);

				}	
				
				parsedTranspondersChannelList.add(channelGroups);
                                logger.info("Zakończono parsowanie kanałów");
			}
			
		} catch (IOException e) {
			logger.fatal("Plik preferencji nie został wczytany lub nie istnieje");
			e.printStackTrace();
		}
			
		return parsedTranspondersChannelList;
	}
	
	

}
