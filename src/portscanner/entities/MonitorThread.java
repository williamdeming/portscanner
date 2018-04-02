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
    private DatabaseUtils dbUtils = new DatabaseUtils();
    
    private ArrayList<NetworkNode> networkNodes = new ArrayList<NetworkNode>();
    
    public MonitorThread(ArrayList<NetworkNode> nodes){
        networkNodes = nodes;
    }
    
    public void run(){
        dbUtils.checkDatabase();
    }    
}
