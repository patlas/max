package patlas.agh.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import patlas.agh.Downloader;
import patlas.agh.DownloaderPool;
import patlas.agh.Preference;
import patlas.agh.Preferences;

public class TestDownloaderPool {

	public static void main(String[] args) {

		/*Wczytanie zawartoœci pliku preferences*/
		Preferences.loadSettings();
		
		/* List obiektów Downloader - runnable */
		ArrayList<Downloader> list = new ArrayList<Downloader>();
		
		/* Wykorzystanie wczytanych preferencji do stworzenia obiektów runnable
		 * i umieszczenia ich na liœcie obiektów do uruchomienia + prezentacja 
		 * konstruktora Downloader(Preference)
		 */
		for(Preference x : Preferences.prefList)
		{
			list.add(new Downloader(x));
			System.out.println("Dodalem (konstruktor1): "+ x.getAddr());
		}
		
		
		
		URL url = null;
		try {
			url = new URL("http://onet.pl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		if(url != null){
			/* jw dodanie obiektu do listy + prezentacja konstruktora Downloader(URL)*/
			list.add(new Downloader(url));
			System.out.println("Dodalem (konstruktor2): "+ url.toString());
		}
		
		/* jw dodanie obiektu do listy + prezentacja konstruktora Downloader(String) */
		list.add(new Downloader("http://seguro.pl"));
		
		/* jw dodanie obiektu do listy + prezentacja konstruktora Downloader(String,String) */
		list.add(new Downloader("http://pl.kingofsat.net/pos-13E_pol.php","hotbird.html"));
		
		
		
		DownloaderPool dp = new DownloaderPool();		
		try {
			dp.addAll(list);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Pobra³em wszystko");

	}

}
