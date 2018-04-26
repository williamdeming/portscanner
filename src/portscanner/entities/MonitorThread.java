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
    
    private ArrayList<NetworkNode> networkNodes = null;
    private DatabaseUtils dbUtils = new DatabaseUtils("root", "Default1!");
    private TextArea monitorOutputText;
    //Used for getting output from port scan thread
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
        //Used for parsing scan output into array, separated by newline
        String outputLines[] = null;
        //Used for iterations
        boolean alert = false;
        int currentPortNumber = 0;
        int runs = 0;
        Port currentPort = new Port();
        String currentPortStatus = null;
        String ip = null;
        String portString = null;
        String spacing = null;
        
        try{
        
            //Ensure database and tables have been created
            dbUtils.checkDatabase();
        
            //Retrieve all computers and ports from database
            networkNodes = dbUtils.getAll();
        
            //Run monitor until interrupted
            while(!Thread.currentThread().isInterrupted()){
                for(int i = 0; i < networkNodes.size(); i++){
                    ip = networkNodes.get(i).getAddress();
                    ports = networkNodes.get(i).getPorts();
                    //monitorOutputText.appendText("Checking " + ip + "\n");
                    System.out.println("\nMonitor\t\t\t" + "Computer " + ip);
                    
                    //Concatenate ports for current computer into string
                    for(int j = 0; j < ports.size(); j++){
                        if(j == 0){
                            portString = Integer.toString(ports.get(j).getNumber());
                        }else{
                            portString += ":" + Integer.toString(ports.get(j).getNumber());
                        }
                    }
                    
                    //Add ip address and portString to command
                    command[2] = ip;
                    command[4] = portString;
                    
                    //Run the port scan in a thread, use latch to synchronize
                    final CountDownLatch latch = new CountDownLatch(1);
                    ScanThread scanThread = new ScanThread(command){
                        @Override
                        public void run(){
        
                            ProcessBuilder pb = new ProcessBuilder(command);
                            try{
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
                    //Wait for latch
                    latch.await();
                    
                    //Parse output string
                    outputLines = output[0].split("\\r?\\n");
                    
                    for(int k = 0; k < ports.size(); k++){
                        currentPort = new Port();
                        
                        //Parse port number
                        currentPortNumber = Integer.parseInt(outputLines[k+1].replaceAll("[^0-9]", ""));
                        //Parse port status
                        currentPortStatus = outputLines[k+1].substring(outputLines[k+1].lastIndexOf(" ") + 1);
                        
                        //Set spacing for some aesthetically pleasing console output
                        if(Integer.toString(currentPortNumber).length() >= 3){
                            spacing = "\t\t\t\t";
                        }else{
                            spacing = "\t\t\t\t\t";
                        }
                        System.out.println("Scan\t\t\tPort " + currentPortNumber + spacing + currentPortStatus);
                        
                        //Add the scan output info for this port into our output port arraylist
                        currentPort.setNumber(currentPortNumber);
                        currentPort.setStatus(currentPortStatus);
                        outputPorts.add(currentPort);
                    }
                    
                    //Compare the output from the scan to database
                    for(int m = 0; m < ports.size(); m++){
                        
                        //If the output status != expected status for this port,
                        //then send an alert to the user
                        if(!outputPorts.get(m).getStatus().equals(ports.get(m).getExpectedStatus())){
                            if(alert == false){
                                monitorOutputText.appendText("Alert!\t\t" + "Computer " + ip + ":\n");
                                alert = true;
                            }
                            
                            //Set spacing for some aesthetically pleasing console output
                            if(("Port " + outputPorts.get(m).getNumber() +
                                " is " + outputPorts.get(m).getStatus()).length() >= 20){
                                spacing = "\t\t";
                            }else{
                                spacing = "\t\t\t";
                            }
                            
                            monitorOutputText.appendText("\t\t\tPort " + outputPorts.get(m).getNumber() +
                                               " is " + outputPorts.get(m).getStatus() + spacing +
                                               "(expected: " + ports.get(m).getExpectedStatus() + ")\n");
                            
                            System.out.println("Monitor\t\t\tPort " + outputPorts.get(m).getNumber() +
                                               " is " + outputPorts.get(m).getStatus() + spacing +
                                               "(expected: " + ports.get(m).getExpectedStatus() + ")");
                        }
                        
                        //If the output status != status in database, 
                        //then update the database entry for this port
                        if(!outputPorts.get(m).getStatus().equals(ports.get(m).getStatus())){
                            dbUtils.updatePortStatus(ip, outputPorts.get(m).getNumber(), outputPorts.get(m).getStatus());
                        }

                    }
                    
                    //Flush scan output arrays
                    outputPorts = new ArrayList<Port>();
                    output[0] = null;
                    alert = false;
                    //Sleep for some interval
                    sleep(1000);
                    //Write current run to console
                    runs++;
                    System.out.println(runs + " runs completed.");
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