/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner.entities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author William Deming
 */
public class ScanThread extends Thread {
    
    private String[] command;
    
    public ScanThread(String[] command){
        this.command = command;
    }
        
    public void run(){
        
    }
}