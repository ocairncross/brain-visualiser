/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package au.edu.uq.rcc;

import com.sun.glass.ui.Screen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author oliver
 */
public class BrainVisualiser extends Application
{
    
    public static Stage stage;
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BrainVisualiser.stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));        
        primaryStage.setTitle("Brain Trace");
        Scene scene = new Scene(root, 1500, 1000);
        
        primaryStage.setScene(scene);
        primaryStage.setX(3300);
        primaryStage.setY(100);        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
    
    
    
}
