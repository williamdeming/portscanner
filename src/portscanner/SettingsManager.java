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
        File addresses = new File("addresses");
        File settings = new File("settings");
        if(addresses.exists() == false || settings.exists() == false){
            System.out.println("Creating config files");
            String[] command = {"/home/admin/Downloads/PortScanner/create-settings.sh"};
            ProcessBuilder pb = new ProcessBuilder(command);
            try{
                pb.start();
            } catch(Exception ex){
                System.out.println("Exception " + ex + " was caught.");
            }
        }
    }
}
