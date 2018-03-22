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
import java.util.ArrayList;
import portscanner.entities.CommandThread;

import portscanner.entities.Port;

/**
 *
 * @author William Deming
 */
public class SettingsManager {
    private ArrayList<String> addresses = new ArrayList<String>();
    private ArrayList<Port> ports = new ArrayList<Port>();
    
    public SettingsManager(){
        initializeSettings();
    }
    
    public void initializeSettings(){
        File addressesFile = new File("/home/admin/Downloads/PortScanner/addresses");
        File settingsFile = new File("/home/admin/Downloads/PortScanner/settings");
        if(addressesFile.exists() == false || settingsFile.exists() == false){
            System.out.println("Creating config files");
            CommandThread init = new CommandThread(new String[]{"sh", "/home/admin/Downloads/PortScanner/src/portscanner/utils/init-settings.sh"});
            init.start();
        }
    }

    public void retrieveAddresses(){
        
    }

    public void retrievePorts(){
        
    } 
}
