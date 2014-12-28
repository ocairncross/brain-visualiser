/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import au.edu.uq.rcc.manager.TrackManager;
import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author oliver
 */
public class Test extends Application
{
    
    File root = new File("/media/oliver/A066DA6266DA392C/projects/brain/human/NC001");
    File mriSouceFile = new File(root, "bet/NC001_convert_eddy_Ave_nodif.nii");
    File trackSourceFile = new File(root, "mrtrix/NC001_whole_brain_DT_STREAM.tck");
    File mrtrixTrackFile = new File(root, "mrtrix/Amyg_L-Amyg_R.tck");
    
    @Override
    public void start(Stage primaryStage)
    {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Test");
        primaryStage.setScene(scene);
        primaryStage.show();
     
        MRISource mriSource = new MRISource(mriSouceFile);
        Transform transform = mriSource.getTransform();
        
        TrackCollection trackCollection1 = new TrackCollection(trackSourceFile, transform);
        TrackCollection trackCollection2 = new TrackCollection(mrtrixTrackFile, transform);
        
        TrackUI testFomat = new TrackUI(root);        
        TrackManager trackSourceManager = new TrackManager(null, testFomat);
        trackSourceManager.addTrackProvider(trackCollection1);
        trackSourceManager.addTrackProvider(trackCollection2);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
