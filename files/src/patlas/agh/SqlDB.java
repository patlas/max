package patlas.agh;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import patlas.agh.exception.MyException;

public class SqlDB {
	
	Connection con = null;
	private static int FREE_POSITION=0;
	public static Boolean IN_USE = false;
	public static String DB_NAME = "../SatelliteDB.db";//"test.db";
	
	private String loc = null;
	private int myID = 0;
	
	final static Logger logger = Logger.getLogger(SqlDB.class);
	
	
	public SqlDB(String localization)
	{
		loc = localization;
		myID = 1000*FREE_POSITION;
		FREE_POSITION++;
	}
	
	public static void incPosition()
	{
		FREE_POSITION++;
	}
	
	public void connectDB() throws MyException
	{
		Boolean exist = false;
		
		if((new File(loc)).exists() == true) exist = true;
		
		try {
			con = DriverManager.getConnection("jdbc:sqlite:"+loc);
			
			if(exist == false)
			{
				Statement stmt = con.createStatement();
				String sql = "CREATE TABLE TRANSPONDERS "
		                   + "(ID INT PRIMARY 	KEY     NOT NULL,"
		                   + " POSITION       	TEXT    NOT NULL, "
		                   + " SATELLITE      	TEXT    NOT NULL, "
		                   + " FREQ           	CHAR(50)," 
		                   + " POLAR          	CHAR(2),"
		                   + " TRANSPONDER	  	TEXT	NOT NULL,"
		                   + " STREAM			TEXT	NOT NULL,"
		                   + " SYMBOL_RATE		TEXT	NOT NULL,"
		                   + " FEC				TEXT	NOT NULL)"; 
				
		      stmt.executeUpdate(sql);
		      
		      sql =   "CREATE TABLE CHANNELS "
		      		+ "(ID INT 			NOT NULL,"
		      		+ " NAME			TEXT, "
		      		+ " COUNTRY			TEXT, "
		      		+ " CATEGORY		TEXT, "
		      		+ " PACKET			TEXT, "
		      		+ " CODING			TEXT, "
		      		+ " SID				TEXT, "
		      		+ " VPID			TEXT, "
		      		+ " AUDIO			TEXT, "
		      		+ " PMT				TEXT, "
		      		+ " PCR				TEXT, "
		      		+ " TXT             TEXT, "			
		      		+ " LAST_UPDATE		TEXT) ";
		      
		      stmt.executeUpdate(sql);
		      
		      logger.info("Nowe tabele zosta³y utworzone");
		      
		      stmt.close();
		     // con.close();			
			}
		
		} catch (SQLException e) {
			SqlDB.IN_USE = false;
			logger.fatal("B³¹d podczas próby nawiazania po³¹czenia z baz¹ danych.");
			throw new MyException(e.toString());
		}
		
	}

	public void insertTransponderRows(ArrayList<ArrayList<String>> transponders) throws MyException
	{
		
		if(con == null) throw new MyException("Database does not exist or is not open");
		
		int index = 0;
		for(ArrayList<String>  tr : transponders)
        {

			index++;
			StringBuilder values = new StringBuilder();
        	for( String t : tr)
        	{
        		values.append( " \""+t+"\",");
        		
        	}
        	values.deleteCharAt(values.length()-1);
        	
        	try {
				Statement stmt = con.createStatement();
				
				String sql =  "INSERT INTO TRANSPONDERS (ID,POSITION,SATELLITE,FREQ,POLAR,"
							+ "TRANSPONDER,STREAM,SYMBOL_RATE,FEC) "
							+ "VALUES("+(myID+index)+"," + values.toString()+");";
				
				stmt.executeUpdate(sql);
				logger.info("Aktualizacja bazy transponderów zakoñczona pomyœlnie");
			} catch (SQLException e) {
				SqlDB.IN_USE = false;
				logger.error("Wyst¹pi³ b³¹d podczas próby aktualizacji bazy transponderów");
				throw new MyException(e.toString());				
			}
        }	
	}
	
	
	public void insertChannelRows(ArrayList<ArrayList<ArrayList<String>>> channelGroups) throws MyException
	{
		
		if(con == null) throw new MyException("Database does not exist or is not open");
		
		int index = 0;
		
		for(ArrayList<ArrayList<String>> channels : channelGroups)
		{
			index++;

			StringBuilder insertQuery = new StringBuilder();
			for(ArrayList<String>  ch : channels)
	        {
				
				StringBuilder values = new StringBuilder(); 
				Boolean nullAdded = true;
				
				if(ch.size() == 10)//mamy radio
				{
					nullAdded = false;
				}
				
				values.append("("+(myID+index)+",");
	        	for( String c : ch)
	        	{
	        		values.append( " \""+c+"\",");
	        		if(nullAdded == false)
	        		{
	        			values.append("NULL, NULL,");
	        			nullAdded = true;
	        		}
	        		
	        	}
	        	values.deleteCharAt(values.length()-1);
	        	values.append(')');
	        	insertQuery.append(values.toString()+", \n");
	        	
	        }	
			insertQuery.deleteCharAt(insertQuery.length()-3);
					
			
			try {
				Statement stmt = con.createStatement();
				
				String sql =  "INSERT INTO CHANNELS (ID,NAME,COUNTRY,CATEGORY,PACKET,"
							+ "CODING,SID,VPID,AUDIO,PMT,PCR,TXT,LAST_UPDATE) "
							+ "VALUES "+ insertQuery.toString() +";";
				
				stmt.executeUpdate(sql);
				logger.info("Aktualizacja bazy kana³ów zakoñczona pomyœlnie");
			} catch (SQLException e) {
				SqlDB.IN_USE = false;
				logger.error("Wyst¹pi³ b³¹d podczas próby aktualizacji bazy kana³ów");
				throw new MyException(e.toString());
			}
		
		}
	}	
	
	
	
	public  String[][] readData(String table) throws MyException
	{
		int size = 0;
		if(table.equalsIgnoreCase("transponders") == true)
			size = 9;
		else if(table.equalsIgnoreCase("channels") == true)
			size = 13;
		
		
		try {
			Statement stmt = con.createStatement();	
            ResultSet result = stmt.executeQuery("SELECT * FROM "+table+";");
            

            ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
      
            while(result.next()) 
            {
            	ArrayList<String> retBld = new ArrayList<String>();
            	for(int index=1; index<size+1; index++)
            	{
            		retBld.add(result.getString(index));
            	}
            	array.add(retBld);
            }
            
            if(array.isEmpty()) throw new MyException("Data base empty or doesn't exist");
            
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
            
        } catch (SQLException e) {
        	SqlDB.IN_USE = false;
        	logger.error("Podczas odczytu zawartoœci bazy danych natrafiono na b³êdy.");
            e.printStackTrace();
            return null;
        }
		
	}
	
	
	
	public void clearDB() throws MyException
	{
		if(con == null) throw new MyException("Database does not exist or is not open");
		
		try {
			Statement stmt = con.createStatement();
			String sql =  "DELETE  FROM TRANSPONDERS; "
						+ "DELETE  FROM CHANNELS;";
			
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			SqlDB.IN_USE = false;
			throw new MyException(e.toString()); 
		}
		
	}
	

}
