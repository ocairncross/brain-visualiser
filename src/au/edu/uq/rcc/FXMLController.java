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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
    File mrtrixTrackFile = new File(root, "mrtrix/out.tck");
    File roiDirectory = new File(root,"roi");
    
    File roiSourceFile = new File(root, "flirt/NC001_Amyg_L.nii");
    File roiTargetFile = new File(root, "flirt/NC001_Amyg_R.nii");
    
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
    private ComboBox<RegionOfInterest> sourceCombo;
    @FXML
    private ComboBox<RegionOfInterest> targetCombo;
    @FXML
    private Label sourceLabel;
    @FXML
    private Label targetLabel;
    @FXML
    private CheckBox clipPlane;
    @FXML
    private Label trackInfoLabel;
    

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
        
        MRISource brainMRI = new MRISource(mriSouceFile);
        MRICanvas mriCanvas = new MRICanvas(brainMRI, imageView); 
        dim = brainMRI.getDimensions();

        slider.setMin(0);
        slider.setMax(dim.z - 1);        
        mriCanvas.bindIntegerProperty(slider.valueProperty());
        slider.setValue(mriCanvas.getCurrentSlice());
        
        BrainIndex brainIndex = new BrainIndex(brainMRI);
        tracks = new TrackCollection(trackSourceFile, brainIndex);        
        trackCanvas = new TrackCanvas(stackPane, brainMRI, clipPlane, slider);
        
        MRISourceCollection mriSource = new MRISourceCollection(roiDirectory, brainIndex);
        List<RegionOfInterest> roiList = mriSource.getROIList();        
        ObservableList<RegionOfInterest> observableArrayList = FXCollections.observableArrayList(roiList);
        sourceCombo.setItems(observableArrayList);
        targetCombo.setItems(observableArrayList);
        
        sourceMaskCanvas = new ROICanvas(mriCanvas.getCurrentSlice(), stackPane);
        slider.valueProperty().addListener(sourceMaskCanvas);
        
        targetMaskCanvas = new ROICanvas(mriCanvas.getCurrentSlice(), stackPane);
        targetMaskCanvas.setColor(Color.RED);
        slider.valueProperty().addListener(targetMaskCanvas);
        
        currentSlice.textProperty().bind(slider.valueProperty().asString());   
        
        
        
    }

    @FXML
    private void trackAction(ActionEvent event)
    {
        if (roiTarget != null  && roiSource != null)
        {
            List<PartitionedTrack> partitionedTracks = roiSource.getPartitionedTracks(roiTarget);
            trackCanvas.setPartitionedTrack(partitionedTracks);
            setTrackInfo(partitionedTracks);
        }
    }
    
    @FXML
    private void trackIntersection(ActionEvent event)
    {
        if (roiTarget != null  && roiSource != null)
        {
            List<Track> intersectionTracks = roiSource.getCommonTracks(roiTarget);
            trackCanvas.setTrack(intersectionTracks);
            setTrackInfo(intersectionTracks);
        }
    }

    @FXML
    private void sourceAction(ActionEvent event)
    {
        ComboBox source = (ComboBox) event.getSource();        
        roiSource = (RegionOfInterest) source.getValue();
        sourceMaskCanvas.setROI(roiSource);
        String label = String.format("Faces: %d, tracks %d\n", roiSource.getFaces().size(), roiSource.numberOfTracks());
        sourceLabel.textProperty().set(label);        
    }

    @FXML
    private void targetAction(ActionEvent event)
    {
        ComboBox source = (ComboBox) event.getSource();
        roiTarget = (RegionOfInterest) source.getValue();
        targetMaskCanvas.setROI(roiTarget);
        String label = String.format("Faces: %d, tracks %d\n", roiTarget.getFaces().size(), roiTarget.numberOfTracks());
        targetLabel.textProperty().set(label);
    }

    private void setTrackInfo(Collection c)
    {
        trackInfoLabel.setText("total tracks " + c.size());
    }
    
    @FXML
    private void sourceTracks(ActionEvent event)
    {
        ArrayList a = new ArrayList<>(roiSource.getTracks());
        trackCanvas.setTrack(a);
        setTrackInfo(a);
    }

    @FXML
    private void targetTracks(ActionEvent event)
    {
        ArrayList a = new ArrayList<>(roiTarget.getTracks());
        trackCanvas.setTrack(a);
        setTrackInfo(a);
    }

    @FXML
    private void sourceCloseTracks(ActionEvent event)
    {
        ArrayList a = new ArrayList<>(roiSource.getCloseTracks(30));
        trackCanvas.setTrack(a);
        setTrackInfo(a);
    }

    @FXML
    private void targetCloseTracks(ActionEvent event)
    {
        ArrayList a = new ArrayList<>(roiTarget.getCloseTracks(30));
        trackCanvas.setTrack(a);
        setTrackInfo(a);
    }

    @FXML
    private void showAllTracks(ActionEvent event)
    {
        trackCanvas.setTrack(tracks.getTrackList());
        setTrackInfo(tracks.getTrackList());
    }

    @FXML
    private void showMrtrixTracks(ActionEvent event)
    {        
        TrackCollection mrTracks = new TrackCollection(mrtrixTrackFile);
        trackCanvas.setTrack(mrTracks.getTrackList());
        setTrackInfo(mrTracks.getTrackList()); 
    }
    
}
