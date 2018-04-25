/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import portscanner.SettingsManager;

/**
 *
 * @author William Deming
 */
public class EditScanController {
    @FXML private TextField ipText;
    @FXML private TextField portText;
    
    SettingsManager.EditSettings es = new SettingsManager.EditSettings();
    
    @FXML protected void handleSaveButtonAction(ActionEvent event) {
        es.ipString = ipText.getText();
        es.portString = portText.getText();
    }    
}
