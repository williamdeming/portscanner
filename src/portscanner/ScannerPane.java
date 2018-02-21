/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 *
 * @author William Deming
 */
public class ScannerPane extends GridPane{
    protected CheckBox scanOptionsSYN, scanOptionsACK;
    protected TextArea networkText, scanOutputText;
    
    public ScannerPane(){
        this.setPadding(new Insets(30, 10, 30, 20));
        
        Label networkLabel = new Label("Network Info:");
        networkLabel.setFont(Font.font("Consolas", 22));
        this.add(networkLabel, 1, 1);
        
        networkText = new TextArea("ifconfig output here\n\n");
        networkText.setFont(Font.font("Consolas", 12));
        networkText.setPrefSize(350, 200);
        this.add(networkText, 1, 2);
        
        Label scanOptionsLabel = new Label("Scan Options:");
        scanOptionsLabel.setFont(Font.font("Consolas", 18));
        scanOptionsLabel.setPadding(new Insets(40, 0, 0, 0));
        this.add(scanOptionsLabel, 1, 3);
        
        scanOptionsSYN = new CheckBox("SYN Scan");
        scanOptionsSYN.setFont(Font.font("Consolas", 14));
        scanOptionsSYN.setPadding(new Insets(0, 0, 5, 0));
        this.add(scanOptionsSYN, 1, 4);
        
        scanOptionsACK = new CheckBox("ACK Scan");
        scanOptionsACK.setFont(Font.font("Consolas", 14));
        scanOptionsACK.setPadding(new Insets(0, 0, 5, 0));
        this.add(scanOptionsACK, 1, 5);
        
        Button portsButton = new Button();
        portsButton.setText("Edit Ports");
        portsButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Open ports dialog");
            }
        });
        portsButton.setFont(Font.font("Consolas", 14));
        portsButton.setPrefSize(120, 28);
        this.add(portsButton, 1, 6);
        
        Button startScanButton = new Button();
        startScanButton.setText("Start Scan");
        startScanButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Beginning scan...");
                String[] command = {"/home/admin/Downloads/PortScanner/src/portscanner/scans/synscan", "-i", "10.0.2.15"};
                ProcessBuilder pb = new ProcessBuilder(command);
                try{
                    Process process = pb.start();
                    InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null){
                        System.out.println(line);
                        addScanOutput(line);
                    }
                } catch(Exception ex){
                    System.out.println("Exception " + ex + " was caught.");
                }
            }
        });
        startScanButton.setAlignment(Pos.CENTER);
        startScanButton.setFont(Font.font("Consolas", 18));
        startScanButton.setPrefSize(200, 50);
        startScanButton.setTranslateX(75);
        startScanButton.setTranslateY(30);
        this.add(startScanButton, 1, 7);
        
        Label scanOutputLabel = new Label("Scan Output:");
        scanOutputLabel.setFont(Font.font("Consolas", 22));
        scanOutputLabel.setTranslateX(175);
        this.add(scanOutputLabel, 2, 1);
        
        scanOutputText = new TextArea("scan output here\n\n");
        scanOutputText.setFont(Font.font("Consolas", 12));
        scanOutputText.setPrefSize(425, 400);
        scanOutputText.setTranslateX(175);
        this.add(scanOutputText, 2, 2);
        
        Button sendReportButton = new Button();
        sendReportButton.setText("Send Report");
        sendReportButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Sending report...");
            }
        });
        sendReportButton.setAlignment(Pos.CENTER);
        sendReportButton.setFont(Font.font("Consolas", 18));
        sendReportButton.setPrefSize(200, 50);
        sendReportButton.setTranslateX(300);
        sendReportButton.setTranslateY(30);
        this.add(sendReportButton, 2, 3);
        
        Button saveReportButton = new Button();
        saveReportButton.setText("Save Report");
        saveReportButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Saving report...");
            }
        });
        saveReportButton.setAlignment(Pos.CENTER);
        saveReportButton.setFont(Font.font("Consolas", 18));
        saveReportButton.setPrefSize(200, 50);
        saveReportButton.setTranslateX(300);
        saveReportButton.setTranslateY(30);
        this.add(saveReportButton, 2, 4);
    }
    
    public void addScanOutput(String text){
        scanOutputText.appendText(text + "\n");
    }
}
