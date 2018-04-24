/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author William Deming
 */
public class ScannerPane extends GridPane implements Initializable{
    
    public void initialize(URL arg0, ResourceBundle arg1) {
        saveButton.setOnAction(this::handleSaveButtonAction);
    }
    
    @FXML private TextField ipText;
    @FXML private TextField portText;
    @FXML private Button saveButton;
    
    @FXML protected void handleSaveButtonAction(ActionEvent event) {
        scanIP = ipText.getText();
        scanPorts = portText.getText();
        System.out.println("Scan\t\t\t" + "Current IP: " + scanIP + "\t\tCurrent portString: " + scanPorts + "\n");
    }
    
    public String scanIP = "127.0.0.1"; 
    public String scanPorts = "21:22:23";
    
    private CheckBox scanOptionsSYN, scanOptionsACK;
    private TextArea networkText, scanOutputText;
    
    public ScannerPane(){
        
        this.setPadding(new Insets(30, 10, 30, 20));
        
        Label networkLabel = new Label("Network Info:");
        this.add(networkLabel, 1, 1);
        
        networkText = new TextArea();
        networkText.setFont(Font.font("Carlito", 12));
        networkText.setPrefSize(500, 500);
        this.add(networkText, 1, 2);

        String[] command = {"ip", "route"};
        ProcessBuilder pb = new ProcessBuilder(command);
        try{
            Process process = pb.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null){
                networkText.appendText(line + "\n");
            }
        } catch(Exception ex){
            System.out.println("Exception " + ex + " was caught.");
        }
        
        Label scanOptionsLabel = new Label("Scan Options:");
        scanOptionsLabel.setPadding(new Insets(40, 0, 0, 0));
        this.add(scanOptionsLabel, 1, 3);
        
        scanOptionsSYN = new CheckBox("SYN Scan");
        scanOptionsSYN.setPadding(new Insets(0, 0, 5, 0));
        this.add(scanOptionsSYN, 1, 4);
        
        scanOptionsACK = new CheckBox("");
        scanOptionsACK.setPadding(new Insets(0, 0, 5, 0));
        this.add(scanOptionsACK, 1, 5);
        
        Button portsButton = new Button();
        portsButton.setText("Edit Scan");
        portsButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("App\t\t\tOpen edit scan window");
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/portscanner/views/editScan.fxml"));
                    Scene scene;
                    scene = new Scene(fxmlLoader.load(), 600, 400);
                    
                    Stage stage;
                    stage = new Stage();
                    stage.setTitle("Edit Scan");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    System.out.println("Failed to create new Window: " + e);
                }
            }    
        });
        portsButton.setPrefSize(200, 50);
        this.add(portsButton, 1, 6);
        
        Button startScanButton = new Button();
        startScanButton.setText("Start Scan");
        startScanButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                scanThread scanner = new scanThread();
                scanner.setIP(scanIP);
                scanner.setPortString(scanPorts);
                scanner.start();
            }
        });
        startScanButton.setAlignment(Pos.CENTER);
        startScanButton.setPrefSize(200, 50);
        startScanButton.setTranslateX(0);
        startScanButton.setTranslateY(0);
        this.add(startScanButton, 1, 7);
        
        Label scanOutputLabel = new Label("Scan Output:");
        scanOutputLabel.setTranslateX(0);
        this.add(scanOutputLabel, 2, 1);
        
        scanOutputText = new TextArea();
        scanOutputText.setFont(Font.font("Carlito", 12));
        scanOutputText.setPrefSize(500, 500);
        scanOutputText.setTranslateX(0);
        this.add(scanOutputText, 2, 2);
        
        Button sendReportButton = new Button();
        sendReportButton.setText("Send Report");
        sendReportButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("App\t\t\tSending report");
            }
        });
        sendReportButton.setAlignment(Pos.CENTER);
        sendReportButton.setPrefSize(200, 50);
        sendReportButton.setTranslateX(150);
        sendReportButton.setTranslateY(30);
        this.add(sendReportButton, 2, 3);
        
        Button saveReportButton = new Button();
        saveReportButton.setText("Save Report");
        saveReportButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("App\t\t\tSaving report");
            }
        });
        saveReportButton.setAlignment(Pos.CENTER);
        saveReportButton.setPrefSize(200, 50);
        saveReportButton.setTranslateX(150);
        saveReportButton.setTranslateY(30);
        this.add(saveReportButton, 2, 4);
    }
    
    public void addScanOutput(String text){
        scanOutputText.appendText(text + "\n");
    }
    
    public class scanThread extends Thread {
        private String ip, portString;
        
        public void run(){
            String[] command;
            ProcessBuilder pb = new ProcessBuilder();
            if(scanOptionsSYN.isSelected() == true){
                SettingsManager sm = new SettingsManager();
                command = new String[]{ sm.getSrcDir() + "scans/synscan", "-i", ip, "-p", portString};
                pb = new ProcessBuilder(command);
            }
            try{
                System.out.println("App\t\t\tBeginning scan");
                Process process = pb.start();
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null){
                    scanOutputText.appendText(line + "\n");
                }
                scanOutputText.appendText("\n");
            } catch(Exception ex){
                System.out.println("Exception " + ex + " was caught.");
            }
            System.out.println("App\t\t\tFinished scan");
        }
        
        public String getIP(){
            return ip;
        }
        
        public void setIP(String address){
            ip = address;
        }
        
        public String getPortString() {
            return portString;
        }

        public void setPortString(String portString) {
            this.portString = portString;
        }
    }
}
