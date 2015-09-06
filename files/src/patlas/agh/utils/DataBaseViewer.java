package patlas.agh.utils;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import patlas.agh.SqlDB;
import patlas.agh.exception.MyException;

public class DataBaseViewer extends JFrame{
	private JTable tableTr;
	private JTable tableCh;
	public DataBaseViewer() {
		setTitle("Preview");
		
		String[] columnNamesTransponder ={
				"ID",
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
	      		"ID",
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
			
	
		
	
		String[][] dataTransponders, dataChannells;
		SqlDB db = new SqlDB(SqlDB.DB_NAME);
		try {
			db.connectDB();	
		} catch (MyException e) { //DODAC LOGGER!!
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(SqlDB.IN_USE == true) return;
		SqlDB.IN_USE = true;
		
		
		try {
			dataTransponders = db.readData("transponders");
			dataChannells = db.readData("channels");
		} catch (MyException e) {
			// TODO Auto-generated catch block
			System.out.println("Brak danych");
			e.printStackTrace(); //dodaæ logger
			SqlDB.IN_USE = false;
			return;
		}
		if(dataChannells.length == 0 || dataTransponders.length == 0)
		{
			System.out.println("NIE ma co wyœwietli");
			return; //DODAÆ LOG, nie ma co wyœwietlic
		}
		
		
		
		getContentPane().setLayout(new BorderLayout(0, 0));
				
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane);
		
		JPanel panelTr = new JPanel();
		tabbedPane.addTab("Transponders", null, panelTr, null);
		panelTr.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollTr = new JScrollPane();
		panelTr.add(scrollTr);
		
		tableTr = new JTable(dataTransponders,columnNamesTransponder);
		scrollTr.setViewportView(tableTr);
		
		JPanel panelCh = new JPanel();
		tabbedPane.addTab("Channells", null, panelCh, null);
		panelCh.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollCh = new JScrollPane();
		panelCh.add(scrollCh);
		
		tableCh = new JTable(dataChannells,columnNamesChannels);
		scrollCh.setViewportView(tableCh);
		
		
		
	}
}
