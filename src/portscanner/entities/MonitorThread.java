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
    
    //Used for getting output from scanthread
    final String output[] = new String[1];
    
    public MonitorThread(TextArea monitorOutputText){
        this.monitorOutputText = monitorOutputText;
    }
    
    public void run(){
        
        //Used for comparison of database ports and scan output
        ArrayList<Port> ports = new ArrayList<Port>();
        ArrayList<Port> outputPorts = new ArrayList<Port>();
        
        //Command string that starts our scan thread
        String command[] = new String[5];
        command[0] = "/home/admin/Downloads/portscanner/src/portscanner/scans/synscan";
        command[1] = "-i";
        command[3] = "-p";
        
        //Used for parsing scan output into array of lines
        String outputLines[] = null;
        
        //Used for iterations
        int runs = 0;
        Port currentPort = new Port();
        int currentPortNumber = 0;
        String currentPortStatus = null;
        String ip = null;
        String portString = null;
        String spacing = null;
        
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
                    System.out.println("\nMonitor Output" + "\t\tComputer " + ip);
                    
                    //Assemble ports for current computer into string
                    for(int j = 0; j < ports.size(); j++){
                        if(j == 0){
                            portString = Integer.toString(ports.get(j).getNumber());
                        }else{
                            portString += ":" + Integer.toString(ports.get(j).getNumber());
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
                                    //System.out.println(line);
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
                    
                    sleep(1000);
                    
                    //Parse output array string
                    outputLines = output[0].split("\\r?\\n");
                    
                    for(int k = 0; k < ports.size(); k++){
                        currentPort = new Port();
                        
                        //get port number
                        currentPortNumber = Integer.parseInt(outputLines[k+1].replaceAll("[^0-9]", ""));
                        
                        //get port status
                        currentPortStatus = outputLines[k+1].substring(outputLines[k+1].lastIndexOf(" ") + 1);
                        
                        if(Integer.toString(currentPortNumber).length() >= 3){
                            spacing = "\t\t\t\t";
                        }else{
                            spacing = "\t\t\t\t\t";
                        }
                        
                        System.out.println("Scan Output\t\tPort " + currentPortNumber + spacing + currentPortStatus);
                        
                        currentPort.setNumber(currentPortNumber);
                        currentPort.setStatus(currentPortStatus);
                        outputPorts.add(currentPort);
                    }
                    
                    //Compare to ports arraylist & update status of ports if needed
                    for(int m = 0; m < ports.size(); m++){
                        
                        //If the output status != expected status for this port,
                        //then we need to send an alert to the user
                        if(!outputPorts.get(m).getStatus().equals(ports.get(m).getExpectedStatus())){
                            monitorOutputText.appendText("Alert!\t\t\tPort " + outputPorts.get(m).getNumber() +
                                               " is " + outputPorts.get(m).getStatus() +
                                               " (Expected status: " + ports.get(m).getExpectedStatus() + ")\n");
                            
                            System.out.println("Alert!\t\t\tPort " + outputPorts.get(m).getNumber() +
                                               " is " + outputPorts.get(m).getStatus() +
                                               " (Expected status: " + ports.get(m).getExpectedStatus() + ")");
                        }
                        
                        //If the output status != status in database, 
                        //then we need to update the database entry
                        if(!outputPorts.get(m).getStatus().equals(ports.get(m).getStatus())){
                            dbUtils.updatePortStatus(ip, outputPorts.get(m).getNumber(), outputPorts.get(m).getStatus());
                        }

                    }
                    
                    output[0] = null;
                    runs++;
                    System.out.println(runs + " runs completed.");
                    //monitorOutputText.appendText("Test alert for computer " + command[2] + ", " + runs + " runs completed." + "\n");
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