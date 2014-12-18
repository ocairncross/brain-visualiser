/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import au.edu.uq.rcc.index.BrainIndex;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javax.vecmath.Tuple3i;

/**
 * FXML Controller class
 *
 * @author oliver
 */
public class FXMLController implements Initializable
{

    File root = new File("/media/oliver/A066DA6266DA392C/projects/brain/human/NC001");
    File mriSouceFile = new File(root, "bet/NC001_convert_eddy_Ave_nodif.nii");
    File trackSourceFile = new File(root, "mrtrix/NC001_whole_brain_DT_STREAM.tck");
    File mrtrixTrackFile = new File(root, "mrtrix/Amyg_L-Amyg_R.tck");
    File roiDirectory = new File(root,"roi");
    
    File roiSourceFile = new File(root, "flirt/NC001_Amyg_L.nii");
    File roiTargetFile = new File(root, "flirt/NC001_Amyg_R.nii");
    
    MRISource brainMRI;
    
    TrackCanvas trackCanvas;
    ROICanvas sourceMaskCanvas;
    ROICanvas targetMaskCanvas;
    
    List<PartitionedTrack> segments;
    TrackCollection tracks;
    RegionOfInterest roiSource;
    RegionOfInterest roiTarget;
    
    @FXML
    private StackPane stackPane;
    @FXML
    private Slider slider;
    @FXML
    private Label currentSlice;    
    @FXML
    private CheckBox clipPlane;
    @FXML
    private VBox toolPanel;
    
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
        Tuple3i dim;
        
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);        
        imageView.fitHeightProperty().bind(stackPane.heightProperty());
        imageView.fitWidthProperty().bind(stackPane.widthProperty());
        stackPane.getChildren().add(imageView);
        
        brainMRI = new MRISource(mriSouceFile);
        MRICanvas mriCanvas = new MRICanvas(brainMRI, imageView);
        dim = brainMRI.getDimensions();

        slider.setMin(0);
        slider.setMax(dim.z - 1);        
        mriCanvas.bindIntegerProperty(slider.valueProperty());
        slider.setValue(mriCanvas.getCurrentSlice());
        
        BrainIndex brainIndex = new BrainIndex(brainMRI);
        
        tracks = new TrackCollection(trackSourceFile, brainMRI.getTransform());        
        trackCanvas = new TrackCanvas(stackPane, brainMRI, clipPlane, slider);
        
        MRISourceCollection mriSource = new MRISourceCollection(roiDirectory, brainIndex);
        List<RegionOfInterest> roiList = mriSource.getROIList();        
        ObservableList<RegionOfInterest> observableArrayList = FXCollections.observableArrayList(roiList);
                
        sourceMaskCanvas = new ROICanvas(stackPane, clipPlane, slider);
        targetMaskCanvas = new ROICanvas(stackPane, clipPlane, slider);
        targetMaskCanvas.setColor(Color.RED);
                
        currentSlice.textProperty().bind(slider.valueProperty().asString("%2.2f"));
        
        TrackProviderUI trackProviderUI = new TrackProviderUI(toolPanel);
        TrackSourceManager trackSourceManager = new TrackSourceManager(trackCanvas, trackProviderUI);
        
        TrackCollection trackCollection1 = new TrackCollection(trackSourceFile, brainMRI.getTransform());
        TrackCollection trackCollection2 = new TrackCollection(mrtrixTrackFile, brainMRI.getTransform());
        trackSourceManager.addTrackProvider(trackCollection1);
        trackSourceManager.addTrackProvider(trackCollection2);
        
        
    }


    private void loadMRT(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Track File");
        fileChooser.setInitialDirectory(root);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("tracks", "*.tck"));        
        File file = fileChooser.showOpenDialog(BrainVisualiser.stage);        
        TrackCollection mrTracks = new TrackCollection(file, brainMRI.getTransform());  
        // trackCanvas.setTrack(mrTracks.getTrackList());        
    }
    
    TrackComparator comparator;
    private List<Track> trackList1;
    private List<Track> trackList2;

    private void loadTrack2(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Track File");
        fileChooser.setInitialDirectory(root);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("tracks", "*.tck"));        
        File file = fileChooser.showOpenDialog(BrainVisualiser.stage);        
        trackList2 = new TrackCollection(file, brainMRI.getTransform()).getTrackList();        
        comparator = new TrackComparator(trackList1, trackList2);
        System.out.printf("common: %d, track1: %d, track2 %d", 
                comparator.getSameTracks().size(),
                comparator.getFirstTracks().size(),
                comparator.getSecondTracks().size());
    }

    
}
