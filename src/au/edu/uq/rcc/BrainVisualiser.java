/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package au.edu.uq.rcc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author oliver
 */
public class BrainVisualiser extends Application
{
    
    public static Stage stage;    
    public static Pane chartPane = new AnchorPane();
        
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BrainVisualiser.stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));        
        primaryStage.setTitle("Brain Trace");
        Scene scene = new Scene(root, 1500, 1000);
        
        primaryStage.setScene(scene);
        //primaryStage.setX(3300);
        //primaryStage.setY(100);        
        primaryStage.show();
        
        Stage chartStage = new Stage();        
        Scene chartScene = new Scene(chartPane, 400, 400);
        chartStage.setScene(chartScene);
        //chartStage.setX(4000);
        //chartStage.setY(200);
        chartStage.initOwner(stage);
        chartStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
    
    
    
}
