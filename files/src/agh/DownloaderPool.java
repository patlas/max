package agh;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class DownloaderPool {

	final static Logger logger = Logger.getLogger(DownloaderPool.class);
	
	private ThreadPoolExecutor exec;
	
	public DownloaderPool()
	{
		//list = null;
	}
	
	
	public ArrayList<Future<?>> addAll(ArrayList<Downloader> list) throws MalformedURLException
	{
					
		ArrayList<Future<?>> ret = new ArrayList<Future<?>>();
		
		BlockingQueue<Runnable> runnables = new ArrayBlockingQueue<Runnable>(1024);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16, Downloader.TIMEOUT, TimeUnit.SECONDS, runnables);
		
		exec = executor;
		
		for (Downloader dwn : list)
			ret.add(executor.submit(dwn));
		executor.shutdown();
		logger.info("Rozpocz�to procedur� wielow�tkowego pobierania.");
		return ret;
	}
	
	public void stopNow()
	{
		exec.shutdownNow();
		logger.warn("Parsowanie zosta�o przerwane przez u�ytkownika.");
	}

}
