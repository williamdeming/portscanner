/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        
        Menu menuPresets = new Menu("Presets");
        MenuItem viewPresets = new MenuItem("View Presets",
            new ImageView(new Image("resources/images/viewpresets.png")));
        viewPresets.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        menuPresets.getItems().addAll(viewPresets);
        
        Menu menuEmail = new Menu("Email");
        MenuItem emailGroup = new MenuItem("Email Group",
            new ImageView(new Image("resources/images/emailgroup.png")));
        emailGroup.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        menuEmail.getItems().addAll(emailGroup);
        
        Menu menuDatabase = new Menu("Database");
        MenuItem database = new MenuItem("Update Database (XML)",
            new ImageView(new Image("resources/images/database.png")));
        emailGroup.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        menuDatabase.getItems().addAll(database);        
        
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
        
        this.getMenus().addAll(menuPresets, menuEmail, menuDatabase, menuSettings, menuHelp);
    }
}
