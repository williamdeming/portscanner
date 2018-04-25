/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.PrintWriter;
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
    
    private CheckBox monitorOptionsPChange, monitorOptionsTimestamp;
    private DatabaseUtils dbUtils = new DatabaseUtils("root", "Default1!");
    private MonitorThread monitor;
    private TextArea monitorOutputText, databaseText;
    public boolean monitorRunning = false;
    public String spacing;
    
    SettingsManager.EditSettings es = new SettingsManager.EditSettings();
    
    public MonitorPane(){
        this.setPadding(new Insets(30, 10, 30, 20));
        
        Label databaseLabel = new Label("Monitored Computers:");
        this.add(databaseLabel, 1, 1);
        
        databaseText = new TextArea();
        databaseText.setEditable(false);
        databaseText.setFont(Font.font("Carlito", 12));
        databaseText.setPrefSize(500, 500);
        this.add(databaseText, 1, 2);
        
        ArrayList<NetworkNode> computers = dbUtils.getAllComputers();
        if(computers.size() > 0){
            for(int i = 0; i < computers.size(); i++){
                
                //Set spacing for some aesthetically pleasing console output
                if(computers.get(i).getAddress().length() >= 10){
                    spacing = "\t\t";
                }else{
                    spacing = "\t\t\t";
                }
                databaseText.appendText(computers.get(i).getAddress() + spacing + 
                                        computers.get(i).getNetwork() + "\n");
            }
        }
        
        Label monitorOptionsLabel = new Label("Monitor Options:");
        monitorOptionsLabel.setPadding(new Insets(40, 0, 0, 0));
        this.add(monitorOptionsLabel, 1, 3);
        
        monitorOptionsPChange = new CheckBox("");
        monitorOptionsPChange.setPadding(new Insets(0, 0, 5, 0));
        this.add(monitorOptionsPChange, 1, 4);
        
        monitorOptionsTimestamp = new CheckBox("");
        monitorOptionsTimestamp.setPadding(new Insets(0, 0, 5, 0));
        this.add(monitorOptionsTimestamp, 1, 5);
        
        Button startButton = new Button();
        startButton.setText("Start Monitoring");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("App\t\t\tStarting monitor");
                monitor = new MonitorThread(monitorOutputText);
                if(monitorRunning == false){
                    monitorOutputText.clear();
                    monitor.start();
                    monitorRunning = true;
                }
            }
        });
        startButton.setPrefSize(200, 50);
        this.add(startButton, 1, 6);
        
        Button stopButton = new Button();
        stopButton.setText("Stop Monitoring");
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("App\t\t\tStopping monitor");
                if(monitorRunning == true){
                    monitor.cancel();
                    monitorRunning = false;
                }
            }
        });
        stopButton.setAlignment(Pos.CENTER);
        stopButton.setPrefSize(200, 50);
        //stopButton.setTranslateY(10);
        this.add(stopButton, 1, 7);
        
        Label monitorOutputLabel = new Label("Monitor Output:");
        monitorOutputLabel.setTranslateX(0);
        this.add(monitorOutputLabel, 2, 1);
        
        monitorOutputText = new TextArea();
        monitorOutputText.setEditable(false);
        monitorOutputText.setFont(Font.font("Carlito", 12));
        monitorOutputText.setPrefSize(500, 500);
        monitorOutputText.setTranslateX(0);
        this.add(monitorOutputText, 2, 2);
        
        Button sendReportButton = new Button();
        sendReportButton.setText("Send Report");
        sendReportButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("App\t\t\tSending monitor output as report");
                try{
                    //Create filename
                    es.reportNumber++;
                    String filename = es.projDir + "report" + Integer.toString(es.reportNumber);
                    System.out.println("App\t\t\t" + filename + "created");
                    
                    //Write monitor output to the file
                    PrintWriter writer = new PrintWriter(filename, "UTF-8");
                    writer.println("PSentry Monitor Output");
                    for (String line : monitorOutputText.getText().split("\n")){
                        writer.println(line);
                    }
                    writer.close();
                    
                    //Get all emails from email group
                    ArrayList<String> emails = new ArrayList<String>();
                    emails = dbUtils.getAllEmails();
                    
                    //for each email address, email it a report
                    for(int i = 0; i < emails.size(); i++){
                        
                    }
                } catch(Exception e){
                    System.out.println(e);
                }
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
                System.out.println("App\t\t\tSaving monitor output as report");
                try{
                    //Create filename
                    es.reportNumber++;
                    String filename = es.projDir + "report" + Integer.toString(es.reportNumber);
                    System.out.println("App\t\t\t" + filename + "created");

                    //Write monitor output to the file
                    PrintWriter writer = new PrintWriter(filename, "UTF-8");
                    writer.println("PSentry Monitor Output");
                    for (String line : monitorOutputText.getText().split("\n")){
                        writer.println(line);
                    }
                    writer.close();
                } catch(Exception e){
                    System.out.println(e);
                }
            }
        });
        saveReportButton.setAlignment(Pos.CENTER);
        saveReportButton.setPrefSize(200, 50);
        saveReportButton.setTranslateX(150);
        saveReportButton.setTranslateY(30);
        this.add(saveReportButton, 2, 4);
    }
}