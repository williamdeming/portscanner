/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author William Deming
 */
public class PSMenu extends MenuBar{
    public PSMenu(){
        Menu menuPresets = new Menu("Presets");
        MenuItem viewPresets = new MenuItem("View Presets",
            new ImageView(new Image("images/viewpresets.png")));
        viewPresets.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        menuPresets.getItems().addAll(viewPresets);
        
        Menu menuEmail = new Menu("Email");
        MenuItem emailGroup = new MenuItem("Email Group",
            new ImageView(new Image("images/emailgroup.png")));
        emailGroup.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        menuEmail.getItems().addAll(emailGroup);
        
        Menu menuSettings = new Menu("Settings");
        MenuItem editSettings = new MenuItem("Edit Settings",
            new ImageView(new Image("images/editsettings.png")));
        editSettings.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        menuSettings.getItems().addAll(editSettings);
        
        Menu menuHelp = new Menu("Help");
        MenuItem getHelp = new MenuItem("Get Help",
            new ImageView(new Image("images/gethelp.png")));
        getHelp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        menuHelp.getItems().addAll(getHelp);
        
        this.getMenus().addAll(menuPresets, menuEmail, menuSettings, menuHelp);
    }
}
