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
import portscanner.entities.MonitorThread;

/**
 *
 * @author William Deming
 */
public class MonitorPane extends GridPane{
    private MonitorThread monitor; 

    public void setMonitor(MonitorThread monitor) {
        this.monitor = monitor;
    }
    
    private CheckBox monitorOptionsPChange, monitorOptionsTimestamp;
    private TextArea monitorOutputText, networkText;
    
    public MonitorPane(){
        this.setPadding(new Insets(30, 10, 30, 20));
        
        Label networkLabel = new Label("Network Info:");
        networkLabel.setFont(Font.font("Consolas", 22));
        this.add(networkLabel, 1, 1);
        
        networkText = new TextArea();
        networkText.setFont(Font.font("Consolas", 12));
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
        
        Label monitorOptionsLabel = new Label("Monitor Options:");
        monitorOptionsLabel.setFont(Font.font("Consolas", 18));
        monitorOptionsLabel.setPadding(new Insets(40, 0, 0, 0));
        this.add(monitorOptionsLabel, 1, 3);
        
        monitorOptionsPChange = new CheckBox("Log Port Changes");
        monitorOptionsPChange.setFont(Font.font("Consolas", 14));
        monitorOptionsPChange.setPadding(new Insets(0, 0, 5, 0));
        this.add(monitorOptionsPChange, 1, 4);
        
        monitorOptionsTimestamp = new CheckBox("Include Timestamp");
        monitorOptionsTimestamp.setFont(Font.font("Consolas", 14));
        monitorOptionsTimestamp.setPadding(new Insets(0, 0, 5, 0));
        this.add(monitorOptionsTimestamp, 1, 5);
        
        Button startButton = new Button();
        startButton.setText("Start Monitoring");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Starting monitor...");
                monitor.start();
            }
        });
        startButton.setFont(Font.font("Consolas", 18));
        startButton.setPrefSize(200, 50);
        this.add(startButton, 1, 6);
        
        Button stopButton = new Button();
        stopButton.setText("Stop Monitoring");
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Stopping monitor...");
            }
        });
        stopButton.setAlignment(Pos.CENTER);
        stopButton.setFont(Font.font("Consolas", 18));
        stopButton.setPrefSize(200, 50);
        //stopButton.setTranslateY(10);
        this.add(stopButton, 1, 7);
        
        Label monitorOutputLabel = new Label("Monitor Output:");
        monitorOutputLabel.setFont(Font.font("Consolas", 22));
        monitorOutputLabel.setTranslateX(0);
        this.add(monitorOutputLabel, 2, 1);
        
        monitorOutputText = new TextArea();
        monitorOutputText.setFont(Font.font("Consolas", 12));
        monitorOutputText.setPrefSize(500, 500);
        monitorOutputText.setTranslateX(0);
        this.add(monitorOutputText, 2, 2);
        
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
        sendReportButton.setTranslateX(150);
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
        saveReportButton.setTranslateX(150);
        saveReportButton.setTranslateY(30);
        this.add(saveReportButton, 2, 4);
    }
}
