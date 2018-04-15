/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import portscanner.entities.MonitorThread;

import portscanner.utils.NetworkUtils;

/**
 *
 * @author William Deming
 */
public class PortScanner extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        //Settings
        SettingsManager settings = new SettingsManager();
        
        //NetworkUtil
        NetworkUtils netUtils = new NetworkUtils();
        
        //Vbox
        final VBox vbox = new VBox();
        
        //Menu
        PSMenu psMenu = new PSMenu(settings.getNetworkNodes());
        
        //GridPanes
        ScannerPane scannerPane = new ScannerPane();
        
        MonitorPane monitorPane = new MonitorPane();
        
        //Tabs
        TabPane tabPane = new TabPane();
        
        Tab scannerTab = new Tab();
        scannerTab.setClosable(false);
        scannerTab.setText("    Scanner    ");
        scannerTab.setContent(scannerPane);
        tabPane.getTabs().add(scannerTab);
        
        Tab monitorTab = new Tab();
        monitorTab.setClosable(false);
        monitorTab.setText("    Monitor    ");
        monitorTab.setContent(monitorPane);
        tabPane.getTabs().add(monitorTab);
        
        //Scene
        Scene scene = new Scene(new VBox(), 1000, 800);
        scene.getStylesheets().add("resources/styles/psentry.css");
        ((VBox) scene.getRoot()).getChildren().addAll(vbox, psMenu, tabPane, monitorPane, scannerPane);
        
        primaryStage.setTitle("PSentry");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}