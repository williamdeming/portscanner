/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner.controllers;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import portscanner.utils.DatabaseUtils;

/**
 *
 * @author William Deming
 */
public class EditEmailController {
    
    @FXML private TextField emailAddText;
    @FXML private TextField emailDeleteText;
    @FXML private TextArea emailDisplayText;
    
    DatabaseUtils dbUtils = new DatabaseUtils("root", "Default1!");
    
    @FXML protected void handleAddEmailButtonAction(ActionEvent event) {
        String email = emailAddText.getText();
        dbUtils.insertEmail(email);
    }
    
    @FXML protected void handleDeleteEmailButtonAction(ActionEvent event) {
        String email = emailDeleteText.getText();
        dbUtils.deleteEmail(email);
    }
    
    @FXML protected void handleRefreshEmailButtonAction(ActionEvent event) {
        ArrayList<String> emails = new ArrayList<String>();
        emails = dbUtils.getAllEmails();
        
        emailDisplayText.clear();
        for(int i = 0; i < emails.size(); i++){
            emailDisplayText.appendText(emails.get(i) + "\n");
        }
    }
}
