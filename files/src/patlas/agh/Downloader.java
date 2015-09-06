package patlas.agh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import org.apache.log4j.Logger;



public class Downloader implements Runnable {

	public int numtry = 1;
	public Boolean isDownloaded = false;
	public static long TIMEOUT=0;
	public static String usrAgent = "brak";
	
	private URL url;
	private File file;	
	private String fName;
	private String dir= "web_pages/";
	private int timeout = 1000;
	
	
	
	final static Logger logger = Logger.getLogger(Downloader.class);
	
	public static void setTimeout(long time)
	{
		TIMEOUT = time;
		logger.info("Zmienna timeout zosta³a ustawiona na:"+TIMEOUT );
	}
	
	
	private static String MD5string(String str) //tworzenie nazw plików je¿eli takowych nie podano
	{
		MessageDigest md5;
		String ret = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(StandardCharsets.UTF_8.encode(str));
			ret = String.format("%032x", new BigInteger(1, md5.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();//DODAÆ LOGGER!!!
		}

		return String.valueOf(Math.abs(str.hashCode()));//ret;
	}
	
	

	public void run()
	{
	try {
	      doRun();
	    } finally 
	    {
	     
	    }
	}
	
	public void doRun(/*Charset charSet*/)
	{
	    InputStream is = null;
	    BufferedReader br;
	    BufferedWriter bw = null;
	    String line;
	 	logger.info("Ruszy³ w¹tek: " + url.toString()); 

	    Thread.currentThread().setName(fName);
	    System.setProperty("http.agent", ""); 
	    
	    if(!file.exists())
		{
			try{
				file.createNewFile();
			}
			catch(IOException ioe){
		         logger.warn("Napotkano problem podczas trzowania pliku: "+file.toString());
		    	 ioe.printStackTrace();
			}
		}

	    try {  	
	    	HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	    	httpConn.setRequestProperty("User-agent", usrAgent);
	    	httpConn.setConnectTimeout(timeout);
	    	is = httpConn.getInputStream();
	    	
	    	
	        br = new BufferedReader(new InputStreamReader(is/*,charSet*/));
	        bw = new BufferedWriter(new FileWriter(file,false));
	        //PrintWriter writer = new PrintWriter( new FileOutputStream(file, false) );
	        while ((line = br.readLine()) != null) {
	            //System.out.println(line);
	        	bw.write(line);
	        	bw.newLine();
	        }
	    } catch (MalformedURLException mue) {
	    	logger.error("Podano b³êdny adres strony www");
	         mue.printStackTrace();
	    } catch (IOException ioe) {
	    	logger.error("Wykryto b³¹d podczas próby zapisu/odczytu do pliku: " +fName);
	         ioe.printStackTrace();
	    } finally {
	        try {
	            if (is != null) is.close();
	            if (bw != null) bw.close();
	            logger.info("Strona zosta³a pobrana i zapisana do pliku: " +fName);
	        } catch (IOException ioe) {
	            // nothing to see here
	        }
	    }
	    isDownloaded = true;

	}
	
	public String getStringUrl()
	{
		return url.toString();
	}
	
	
	public File getFile()
	{
		return file;
	}
	
	public Downloader(String urlString)
	{
		fName = "f_"+Downloader.MD5string(urlString)+".html";
		file = new File(dir+fName);
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			logger.fatal("Podany adres nie spe³nia wymogów standardu.");
			e.printStackTrace();
		}				
	}
	
	public Downloader(URL Url)
	{
		fName = "f_"+Downloader.MD5string(Url.toString())+".html";
		file = new File(dir+fName);
		url = Url;
		isDownloaded = true;
	}
	
	public Downloader(String urlString, String fileName)
	{
		if(fileName.contains("/") == true)
		{
			dir = "";	
			fName = fileName;
		}
		else
		{
			if(fileName.contains(".html")==true)
				fName = fileName;
			else
				fName = fileName+".html";
		}
		
		file = new File(dir+fName);
		
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			logger.fatal("Podany adres nie spe³nia wymogów standardu.");
			e.printStackTrace();
		}	
		isDownloaded = true;
			
	}

	
	public Downloader(Preference pref)
	{
		String urlString = pref.getAddr();
		TIMEOUT = Preference.TIMEOUT;
		numtry = pref.getNumTry();
		timeout = pref.getTimeout();
		
		fName = "f_"+Downloader.MD5string(urlString)+".html";
		file = new File(dir+fName);
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			logger.fatal("Podany adres nie spe³nia wymogów standardu.");
			e.printStackTrace();
		}	
		
		usrAgent = Preference.getAgent();
		isDownloaded = true;
	}
	
	
}
