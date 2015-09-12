/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agh;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;

/**
 *
 * @author patlas
 */
public class MyGUI extends javax.swing.JFrame {

    private ArrayList<Downloader> listDownloaderPool = new ArrayList<Downloader>();
    private DownloaderPool downloaderPool = null;

    final static Logger logger = Logger.getLogger(MyGUI.class);
    public MyGUI() {
        initComponents();
        Preferences.loadSettings();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bDownload = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bDownload.setText("jButton1");
        bDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDownloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(bDownload)
                .addContainerGap(303, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(bDownload)
                .addContainerGap(238, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDownloadActionPerformed
        
        listDownloaderPool.add(new DownloaderWithParseDB(Preferences.prefList.get(0)));
  
        downloaderPool = new DownloaderPool();		
        try {
                ArrayList<Future<?>> tasks = downloaderPool.addAll(listDownloaderPool);
        } catch (MalformedURLException e1) {
                logger.error("Natrafiono na konflikt podczas próby pobierania plików.");
                e1.printStackTrace();
        }

        listDownloaderPool.clear();
    }//GEN-LAST:event_bDownloadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bDownload;
    // End of variables declaration//GEN-END:variables
}
