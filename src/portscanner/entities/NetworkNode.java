/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner.entities;

import java.util.ArrayList;

/**
 *
 * @author William Deming
 */
public class NetworkNode {
    private ArrayList<Port> ports = new ArrayList<Port>();
    private String address = null; 
    private String network = null;
    
    public NetworkNode(ArrayList<Port> ports, String address){
        this.ports = ports;
        this.address = address;
    }
    
    public NetworkNode(String address, String network){
        this.address = address;
        this.network = network;
    }
    
    public ArrayList<Port> getPorts(){
        return ports;
    }
    
    public void setPorts(ArrayList<Port> ports){
        this.ports = ports;
    }
    
    public String getAddress(){
        return address;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
   
    public void printContents(){
        System.out.println("  Node " + address);
        for(int i = 0; i < ports.size(); i++){
            System.out.println("    Port: " + ports.get(i).getNumber()  +
                    "\t\tExpected Status: " + ports.get(i).getStatus());
        }
    }
}
