/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner.entities;

/**
 *
 * @author William Deming
 */
public class CommandThread extends Thread{
    private String[] command;
    
    public CommandThread(String[] command){
        this.command = command;
    }
    
    public void run(){
        ProcessBuilder pb = new ProcessBuilder(command);
        try{
            pb.start();
        } catch(Exception ex){
            System.out.println("Exception " + ex + " was caught.");
        }
    }    
}
