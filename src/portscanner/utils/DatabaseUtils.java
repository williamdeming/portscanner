/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner.utils;

import java.sql.*;


/**
 *
 * @author William Deming
 */
public class DatabaseUtils {
    public DatabaseUtils(){
        
    }
    
    public void checkDatabase(){
        try{
            System.out.println("Checking if database exists...");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=Default1!");
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS portscan;");
        }catch(Exception e){ 
            System.out.println(e);
        }
    }
}
