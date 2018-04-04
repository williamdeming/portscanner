
import portscanner.utils.DatabaseUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author William Deming
 */
public class TestDatabase {
    private static DatabaseUtils dbUtils = new DatabaseUtils("root", "Default1!");
    
    public static void main(String args[]){
        dbUtils.checkDatabase();
        TestDatabase tdb = new TestDatabase();
        tdb.AddComputers();
        tdb.AddPorts();
    }
    
    public void AddComputers(){
        dbUtils.insertComputer("10.0.8.11", "Network A");
        dbUtils.insertComputer("10.0.8.12", "Network A");
        dbUtils.insertComputer("10.0.200.220", "Network B");
        dbUtils.insertComputer("10.0.50.30", "Network C");
        dbUtils.insertComputer("10.0.50.32", "Network C");
        dbUtils.insertComputer("10.0.50.33", "Network C");
        dbUtils.insertComputer("10.0.50.37", "Network C");
        
    }
    
    public void AddPorts(){
        dbUtils.insertPort("10.0.8.11", 21, "closed", "closed");
        dbUtils.insertPort("10.0.8.11", 22, "closed", "closed");
        dbUtils.insertPort("10.0.8.11", 23, "open", "closed");
        dbUtils.insertPort("10.0.8.12", 79, "closed", "closed");
        dbUtils.insertPort("10.0.8.12", 80, "filtered", "closed");
        dbUtils.insertPort("10.0.50.30", 21, "closed", "closed");
        dbUtils.insertPort("10.0.50.30", 22, "closed", "closed");
        dbUtils.insertPort("10.0.50.30", 23, "closed", "open");
        dbUtils.insertPort("10.0.50.30", 100, "filtered", "filtered");
        dbUtils.insertPort("10.0.50.30", 443, "open", "open");
    }
    
    public void testCheckPorts(){
        boolean result;
        if(dbUtils.checkIfPortExists("10.0.8.11", 22) == true){
            System.out.println("Test 1 passed");
        }
        if(dbUtils.checkIfPortExists("10.0.50.30", 100) == true){
            System.out.println("Test 2 passed");
        }
        if(dbUtils.checkIfPortExists("10.0.50.30", 111) == false){
            System.out.println("Test 3 passed");
        }
        if(dbUtils.checkIfPortExists("ip address", 0) == false){
            System.out.println("Test 4 passed");
        }
        if(dbUtils.checkIfPortExists("10.0.50.30", 21) == true){
            System.out.println("Test 5 passed");
        }
        if(dbUtils.checkIfPortExists("10.0.8.12", 79) == true){
            System.out.println("Test 6 passed");
        }
        if(dbUtils.checkIfPortExists("10.0.8.13", 79) == false){
            System.out.println("Test 7 passed");
        }
    }
}
