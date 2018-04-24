/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import portscanner.entities.NetworkNode;
import portscanner.utils.DatabaseUtils;

/**
 *
 * @author William Deming
 */
public class PSMenu extends MenuBar{
    DatabaseUtils dbUtils = new DatabaseUtils("root", "Default1!");
    
    ArrayList<NetworkNode> nodes = new ArrayList<NetworkNode>();
    
    public PSMenu(ArrayList<NetworkNode> nodes){
        this.nodes = nodes;
        
        Menu menuDatabase = new Menu("Database");
        MenuItem restoreDatabase = new MenuItem("Restore Database",
            new ImageView(new Image("resources/images/exclamation.png")));
        restoreDatabase.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                dbUtils.dropDatabase();
                dbUtils.checkDatabase();
            }
        });
        MenuItem updateDatabase = new MenuItem("Update Database (XML)",
            new ImageView(new Image("resources/images/database.png")));
        updateDatabase.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                dbUtils.updateDatabaseFromXML(nodes);
            }
        });
        menuDatabase.getItems().addAll(updateDatabase, restoreDatabase);             
        
        Menu menuEmail = new Menu("Email");
        MenuItem emailGroup = new MenuItem("Edit Email Group",
            new ImageView(new Image("resources/images/emailgroup.png")));
        emailGroup.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/portscanner/views/editEmailGroup.fxml"));
                    Scene scene;
                    scene = new Scene(fxmlLoader.load(), 600, 400);
                    
                    Stage stage;
                    stage = new Stage();
                    stage.setTitle("Edit Email Group");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    System.out.println("Failed to create new Window: " + e);
                }
            }
        });
        menuEmail.getItems().addAll(emailGroup);   
        
        Menu menuSettings = new Menu("Settings");
        MenuItem editSettings = new MenuItem("Edit Settings",
            new ImageView(new Image("resources/images/editsettings.png")));
        editSettings.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        menuSettings.getItems().addAll(editSettings);
        
        Menu menuHelp = new Menu("Help");
        MenuItem getHelp = new MenuItem("Get Help",
            new ImageView(new Image("resources/images/gethelp.png")));
        getHelp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        menuHelp.getItems().addAll(getHelp);
        
        this.getMenus().addAll(menuDatabase, menuEmail, menuSettings, menuHelp);
    }
}