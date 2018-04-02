/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner.entities;

import java.sql.*;
import java.util.ArrayList;
import portscanner.utils.DatabaseUtils;

/**
 *
 * @author William Deming
 */
public class MonitorThread extends Thread{
    private DatabaseUtils dbUtils = new DatabaseUtils("root", "Default1!");
    
    private ArrayList<NetworkNode> networkNodes = null;
    
    public MonitorThread(){
        
    }
    
    public void run(){
        String ip = null;
        
        try{
        
            //Ensure database and tables have been created
            dbUtils.checkDatabase();
        
            //Get all computers and their ports from database
            networkNodes = dbUtils.getAll();
        
            //Loop through until interrupted
            while(!Thread.currentThread().isInterrupted()){
                for(int i = 0; i < networkNodes.size(); i++){
                    ip = networkNodes.get(i).getAddress();
                    System.out.println("IM WORKIN OVER HERE");
                    sleep(1000);
                }
            }
            
        }catch (Exception e){
            System.out.println(e);          
        }
    }
    
    public void cancel() { interrupt(); }
}
