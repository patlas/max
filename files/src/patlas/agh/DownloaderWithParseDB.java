/**
 * 
 */
package patlas.agh;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import patlas.agh.exception.MyException;

/**
 * @author PatLas
 *
 */
public class DownloaderWithParseDB extends Downloader {

	final static Logger logger = Logger.getLogger(DownloaderWithParseDB.class);
	
	public DownloaderWithParseDB(Preference pref) {
		super(pref);
	}


	@Override
	public void run() {

		super.run();
		
		while(SqlDB.IN_USE == true){ 
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.info("W¹tek o nazwie: "+Thread.currentThread().getName()+" zosta³ wstrzymany.");
				return;

				
			}
		}
		SqlDB.IN_USE = true;
		
		logger.info("W¹tek o nazwie: "+Thread.currentThread().getName()+" rozpocz¹³ zapis do bazy danych.");
		
		SqlDB db = new SqlDB(SqlDB.DB_NAME);
		Parser parser = new Parser(this);
		try {
			db.connectDB();
			db.insertTransponderRows(parser.getTransponder());
			db.insertChannelRows(parser.getChannels());
			SqlDB.IN_USE = false;
			if(this.getFile().delete() == true); // DODAC LOGGER (nie)uda³o siê usun¹æ pliku
			logger.info("W¹tek : "+Thread.currentThread().getName()+" zakoñczy³ parsowanie i zapis bazy danych");

			
			JOptionPane.showMessageDialog(null,"Strona o adresie: "+ this.getStringUrl() + " zosta³a"
					+ " przetworzona pomyœlnie!","Pobieranie zakoñczone",
				    JOptionPane.INFORMATION_MESSAGE
				  );
			
		} catch (MyException e) {
			SqlDB.IN_USE = false;
			logger.error("W¹tek : "+Thread.currentThread().getName()+" natrafi³ na problem podczas przetwarzanai strony");
			e.printStackTrace();
		}
		
	}
	

}
