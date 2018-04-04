/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner.entities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import javafx.scene.control.TextArea;
import portscanner.utils.DatabaseUtils;

/**
 *
 * @author William Deming
 */
public class MonitorThread extends Thread{
    private DatabaseUtils dbUtils = new DatabaseUtils("root", "Default1!");
    
    private ArrayList<NetworkNode> networkNodes = null;
    private TextArea monitorOutputText;
    
    final String[] output = new String[1];
    
    public MonitorThread(TextArea monitorOutputText){
        this.monitorOutputText = monitorOutputText;
    }
    
    public void run(){
        ArrayList<Port> ports = null;
        
        String[] command = new String[5];
        command[0] = "\"/home/admin/Downloads/portscanner/src/portscanner/scans/synscan\"";
        command[1] = "-i";
        command[3] = "-p";
        
        int runs = 0;
        String ip = null;
        String portString = null;
        
        try{
        
            //Ensure database and tables have been created
            dbUtils.checkDatabase();
        
            //Get all computers and their ports from database
            networkNodes = dbUtils.getAll();
        
            //Loop through until interrupted
            while(!Thread.currentThread().isInterrupted()){
                for(int i = 0; i < networkNodes.size(); i++){
                    ip = networkNodes.get(i).getAddress();
                    ports = networkNodes.get(i).getPorts();
                    
                    //Assemble ports for current computer into string
                    for(int j = 0; j < ports.size(); j++){
                        if(j == 0){
                            portString = Integer.toString(ports.get(j).getNumber());
                        }else{
                            portString += ";" + Integer.toString(ports.get(j).getNumber());
                        }
                    }
                    
                    //Finish command array
                    command[2] = ip;
                    command[4] = portString;
                    
                    //Run the scan in a scanthread, use latch to synchronize
                    final CountDownLatch latch = new CountDownLatch(1);
                    ScanThread scanThread = new ScanThread(command){
                        @Override
                        public void run(){
        
                            ProcessBuilder pb = new ProcessBuilder(command);
                            try{
                                //System.out.println("Monitoring " + command[2]);
                                Process process = pb.start();
                                InputStream is = process.getInputStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader br = new BufferedReader(isr);
                                String line;
                                while ((line = br.readLine()) != null){
                                    output[0] += line + "\n";
                                }
                            } catch(Exception ex){
                                System.out.println("Exception " + ex + " was caught.");
                            }
                            
                            latch.countDown();
                        }
                    };
                    
                    scanThread.start();
                    //wait for latch before collecting output
                    latch.await();
                    
                    //1. parse output array string
                    
                    //2. compare to ports arraylist & make changes to status of ports if needed
                    
                    //3. if status changes & != expected_status, send alert
                    runs++;
                    System.out.println("Test alert for computer " + command[2] + ", " + runs + " runs completed." + "\n");
                    //monitorOutputText.appendText("Test alert for computer " + command[2] + ", " + runs + " runs completed." + "\n");
                    
                    //4. update database with status changes if there are any
                    
                    
                    output[0] = null;
                }
            }
            
        }catch (Exception e){
            System.out.println(e);          
        }
    }
    
    public void cancel(){
        interrupt();
    }
}