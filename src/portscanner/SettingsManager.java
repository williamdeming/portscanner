/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author William Deming
 */
public class SettingsManager {
    public SettingsManager(){
        File conf = new File("watchtower.conf");
        if(conf.exists() == false){
            System.out.println("Creating configuration file...");
            String[] command = {"touch", "/home/admin/Downloads/PortScanner/watchtower.conf"};
            ProcessBuilder pb = new ProcessBuilder(command);
            try{
                pb.start();
            } catch(Exception ex){
                System.out.println("Exception " + ex + " was caught.");
            }
        }
    }
}
