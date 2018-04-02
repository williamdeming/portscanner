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
    private int number;
    private String status, expected_status;
    
    public Port(int number, String expected_status){
        this.number = number;
        this.status = expected_status;
    }
    
    public Port(int number, String status, String expected_status){
        this.number = number;
        this.status = status;
        this.expected_status = expected_status;
    }

    public String getExpected_status() {
        return expected_status;
    }

    public void setExpected_status(String expected_status) {
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
