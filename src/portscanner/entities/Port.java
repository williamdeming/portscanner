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
public class Port {
    private int number = 0;
    private String status = null; 
    private String expected_status = null;  
    
    //Used for XML inserts
    public Port(int number, String expected_status){
        this.number = number;
        this.expected_status = expected_status;
    }
    
    //Used for database retrieval
    public Port(int number, String status, String expected_status){
        this.number = number;
        this.status = status;
        this.expected_status = expected_status;
    }
    
    //Empty constructor used for everything else
    public Port(){
        
    }      

    public String getExpectedStatus() {
        return expected_status;
    }

    public void setExpectedStatus(String expected_status) {
        this.expected_status = expected_status;
    }
    
    public int getNumber(){
        return number;
    }
    
    public void setNumber(int number){
        this.number = number;
    }
    
    public String getStatus(){
        return status;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
}
