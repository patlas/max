package agh;


public class Preference {
	

	
	public static  int TIMEOUT = 80;
	private String addr;
	private static String USR_AGENT;
	private int numTry;
	private int timeout = 1000;
	
	public Preference(String addr, int tm)
	{
		this.timeout = tm;
		this.addr = addr;
		Preferences.prefList.add(this);
	}
	
	public Preference()
	{
		Preferences.prefList.add(this);
	}
	
	//do usuniecia
	public Preference( int num, String addr)
	{
		this.numTry = num;
		this.addr = addr;
	}
	
	public static final void setTIMEOUT(int timeout)
	{
		Preference.TIMEOUT = timeout;
	}
	
	public void setAddr(String link)
	{
		this.addr = link;
	}

	public String getAddr()
	{
		return this.addr; 
	}
	
	public void setNumTry(int num)
	{
		this.numTry = num;
	}
	
	public int getNumTry()
	{
		return this.numTry;
	}
	
	
	public void setTimeout(int num)
	{
		this.timeout = num;
	}
	
	public int getTimeout()
	{
		return this.timeout;
	}
	
	public static String getAgent()
	{
		return USR_AGENT;
	}
	
	public static void setAgent(String usrAgent)
	{
		 USR_AGENT = usrAgent;
	}
	
}

