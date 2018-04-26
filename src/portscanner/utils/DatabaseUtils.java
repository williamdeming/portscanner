/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner.utils;

import java.sql.*;
import java.util.ArrayList;
import portscanner.entities.NetworkNode;
import portscanner.entities.Port;


/**
 *
 * @author William Deming
 */
public class DatabaseUtils {
    String user, password;
    
    public DatabaseUtils(String user, String password){
        this.user = user;
        this.password = password;
    }
    
    //Update the database from values entered into xml settings file
    public void updateDatabaseFromXML(ArrayList<NetworkNode> nodes){
        ArrayList<Port> ports;
        Port currentPort;
        
        for(int i = 0; i < nodes.size(); i++){
            ports = nodes.get(i).getPorts();
            for(int j = 0; j < ports.size(); j++){
                currentPort = ports.get(j);
                
                //Check if current port is already in database before inserting
                if(checkIfPortExists(nodes.get(i).getAddress(), currentPort.getNumber()) == false){
                    insertPort(nodes.get(i).getAddress(), currentPort.getNumber(), null, currentPort.getExpectedStatus());
                }
            }
            
            //Check if the current computer is already in the database before inserting
            if(checkIfComputerExists(nodes.get(i).getAddress()) == false){
                insertComputer(nodes.get(i).getAddress(), null);
            }
        }
    }    
    
    //Check if portscan database, computers and ports tables have been created yet
    public void checkDatabase(){
        try{
            System.out.println("Database\t\t\t---Checking if database exists---");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/?user=" + user + "&password=" + password + "&useSSL=false");
            Statement st = connection.createStatement();
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS portscan;");
            connection.close();
            
            connection = DriverManager.getConnection("jdbc:mysql://localhost/portscan?user=" + user + "&password=" + password + "&useSSL=false");
            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS computers(id INT unsigned AUTO_INCREMENT, ip VARCHAR(15), network VARCHAR(20), PRIMARY KEY (id))");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS ports(ip VARCHAR(15), port INT(7), status VARCHAR(8), expected_status VARCHAR(8))");
            connection.close();
        }catch(Exception e){ 
            System.out.println(e);
        }
    }
    
    //Check if a given computer exists already
    public boolean checkIfComputerExists(String ip){
        boolean exists = false;
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/portscan?user=" + user + "&password=" + password + "&useSSL=false");
            String query = "SELECT * from computers WHERE ip = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, ip);
            ResultSet rs = ps.executeQuery();
            if(rs.absolute(1)) {
                exists = true;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        
        return exists;
    }
    
    //Check if a given port exists already
    public boolean checkIfPortExists(String ip, int number){
        boolean exists = false;
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/portscan?user=" + user + "&password=" + password + "&useSSL=false");
            String query = "SELECT * from ports WHERE ip = ? AND port = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, ip);
            ps.setInt(2, number);
            ResultSet rs = ps.executeQuery();
            if(rs.absolute(1)) {
                exists = true;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        
        return exists;        
    }
    
    //Drop database table for application
    public void dropDatabase(){
        try{
            System.out.println("Database\t\t\t---Dropping database portscan---");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/portscan?user=" + user + "&password=" + password + "&useSSL=false");
            Statement st = connection.createStatement();
            st.executeUpdate("DROP DATABASE portscan;");
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }        
    }    

    //Returns all computers, with all their ports, from database
    public ArrayList<NetworkNode> getAll(){
        ArrayList<NetworkNode> computers = new ArrayList<NetworkNode>();
        NetworkNode currentComputer = null;
        String ip = null;
        String network = null;
        
        //Step 1 - Get all computers
        computers = getAllComputers();
        
        //Step 2 - Get all ports for each computer
        for(int i = 0; i < computers.size(); i++){
            currentComputer = computers.get(i);
            currentComputer.setPorts(getAllPorts(currentComputer.getAddress()));
        }
        
        return computers;
    }       
    
    //Returns all computers from the computers table
    public ArrayList<NetworkNode> getAllComputers(){
        ArrayList<NetworkNode> computers = new ArrayList<NetworkNode>();
        String ip = null;
        String network = null;
        
        try{
            
            System.out.println("Database\t\t\t---Getting computers---");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/portscan?user=" + user + "&password=" + password + "&useSSL=false");
            String query = "SELECT * FROM computers";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
      
            // iterate through the java resultset
            while (rs.next()){
                ip = rs.getString("ip");
                network = rs.getString("network");
                computers.add(new NetworkNode(ip, network));
            }
            connection.close();
            
        }catch(Exception e){ 
            System.out.println(e);
        }
        
        return computers;
    }
    
    //Returns all ports for a computer from the ports table
    public ArrayList<Port> getAllPorts(String ip){
        ArrayList<Port> ports = new ArrayList<Port>();
        int number = 0;
        String status = null;
        String expected_status = null;
        
        try{
            
            System.out.println("Database\t\t\t---Getting ports for ip " + ip + "---");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/portscan?user=" + user + "&password=" + password + "&useSSL=false");
            String query = "SELECT * FROM ports WHERE ip = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString (1, ip);
            ResultSet rs = ps.executeQuery();
      
            // iterate through the java resultset
            while (rs.next()){
                number = rs.getInt("port");
                status = rs.getString("status");
                expected_status = rs.getString("expected_status");
                ports.add(new Port(number, status, expected_status));
            }
            connection.close();
            
        }catch(Exception e){ 
            System.out.println(e);
        }
        
        return ports;
    }
    
    //Inserts a computer into the computer table
    public void insertComputer(String ip, String network){
        try{
            
            System.out.println("Database\t\t\t---Inserting computer " + ip + "---");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/portscan?user=" + user + "&password=" + password + "&useSSL=false");
            String query = " insert into computers (ip, network)"
                    + " values (?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString (1, ip);
            ps.setString (2, network);
            ps.execute();
            connection.close();
            
        }catch(Exception e){ 
            System.out.println(e);
        }
    }
    
    //Inserts a port into the ports table
    public void insertPort(String ip, int port, String status, String expected_status){
        try{
            
            System.out.println("Database\t\t\t---Inserting port " + port + "---");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/portscan?user=" + user + "&password=" + password + "&useSSL=false");
            String query = " insert into ports (ip, port, status, expected_status)"
                    + " values (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString (1, ip);
            ps.setInt (2, port);
            ps.setString (3, status);
            ps.setString (4, expected_status);
            ps.execute();
            connection.close();
            
        }catch(Exception e){ 
            System.out.println(e);
        }
    }  
    
    //Updates the status of a port in the ports table
    public void updatePortStatus(String ip, int port, String status){
        try{
            
            System.out.println("Database\t\t\t---Port " + port + " status updated to " + status + "---");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/portscan?user=" + user + "&password=" + password + "&useSSL=false");
            String query = "update ports set status = ? where ip = ? and port = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString (1, status);
            ps.setString (2, ip);
            ps.setInt(3, port);
            ps.executeUpdate();
            connection.close();
            
        }catch(Exception e){ 
            System.out.println(e);
        }
    }
}
