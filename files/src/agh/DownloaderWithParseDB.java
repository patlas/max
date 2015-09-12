/**
 * 
 */
package agh;

import agh.exception.MyException;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;


/**
 * @author PatLas
 *
 */
public class DownloaderWithParseDB extends Downloader {

    final static Logger logger = Logger.getLogger(DownloaderWithParseDB.class);
    static String outputFile = "output.txt";

    public DownloaderWithParseDB(Preference pref) {
            super(pref);
    }


    @Override
    public void run() {

        super.run();
        logger.info("Wątek o nazwie: "+Thread.currentThread().getName()+" rozpoczął zapis do pliku.");


        Parser parser = new Parser(this);
        FileAppender fa = new FileAppender(outputFile);
        try{
            fa.appendTransponders(parser.getTransponder());
            fa.appendChannels(parser.getChannels());
            fa.writer.close();
        } catch(MyException me){
            me.printStackTrace();
        }
        if(this.getFile().delete() == true); // DODAC LOGGER (nie)uda�o si� usun�� pliku
        logger.info("Wątek : "+Thread.currentThread().getName()+" zakończył parsowanie i zapis do pliku");


        JOptionPane.showMessageDialog(null,"Strona o adresie: "+ this.getStringUrl() + " została"
                        + " przetworzona pomyślnie!","Pobieranie zakończone",
                    JOptionPane.INFORMATION_MESSAGE
                  );
		
    }
}