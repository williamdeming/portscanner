/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import portscanner.entities.NetworkNode;
import portscanner.utils.DatabaseUtils;

/**
 *
 * @author William Deming
 */
public class MonitorPane extends GridPane{
    private MonitorThread monitor; 

    public void setMonitor(MonitorThread monitor) {
        this.monitor = monitor;
    }
    
    private DatabaseUtils dbUtils = new DatabaseUtils("root", "Default1!");
    
    private CheckBox monitorOptionsPChange, monitorOptionsTimestamp;
    private TextArea monitorOutputText, databaseText;
    
    public MonitorPane(){
        this.setPadding(new Insets(30, 10, 30, 20));
        
        Label databaseLabel = new Label("Monitored Computers:");
        databaseLabel.setFont(Font.font("Consolas", 22));
        this.add(databaseLabel, 1, 1);
        
        databaseText = new TextArea();
        databaseText.setFont(Font.font("Consolas", 12));
        databaseText.setPrefSize(500, 500);
        this.add(databaseText, 1, 2);
        
        ArrayList<NetworkNode> computers = dbUtils.getAllComputers();
        if(computers.size() > 0){
            for(int i = 0; i < computers.size(); i++){
                databaseText.appendText(computers.get(i).getAddress() + "\t\t" + 
                                        computers.get(i).getNetwork() + "\n");
            }
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
