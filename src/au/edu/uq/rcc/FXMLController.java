/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import au.edu.uq.rcc.ui.TrackUI;
import au.edu.uq.rcc.ui.ROIUI;
import au.edu.uq.rcc.manager.TrackManager;
import au.edu.uq.rcc.canvas.TrackCanvas;
import au.edu.uq.rcc.canvas.ROICanvas;
import au.edu.uq.rcc.canvas.MRICanvas;
import au.edu.uq.rcc.index.BrainIndex;
import au.edu.uq.rcc.manager.ROIManager;
import au.edu.uq.rcc.ui.ComparatorUI;
import au.edu.uq.rcc.ui.ROITrackSelectorUI;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javax.vecmath.Tuple3i;
import org.apache.commons.math3.util.FastMath;

/**
 * FXML Controller class
 *
 * @author oliver
 */
public class FXMLController implements Initializable
{

    public static TrackCanvas trackHiTrackCanvas;
    public static int DISPLAY_MAX_TRACKS = 1000;

    File root = new File("/media/oliver/A066DA6266DA392C/projects/brain/human/NC001");
    File mriSouceFile = new File(root, "bet/NC001_convert_eddy_Ave_nodif.nii");
    File trackSourceFile = new File(root, "mrtrix/NC001_whole_brain_DT_STREAM.tck");
    File mrtrixTrackFile = new File(root, "mrtrix/Amyg_L-Amyg_R.tck");
    File roiDirectory = new File(root, "roi");

    File roiSourceFile = new File(root, "flirt/NC001_Amyg_L.nii");
    File roiTargetFile = new File(root, "flirt/NC001_Amyg_R.nii");

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
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Init fastmath - this will load tables.    
        FastMath.sqrt(Math.PI);

        MRISource brainMRI = new MRISource(mriSouceFile);
        Tuple3i dim = brainMRI.getDimensions();
        TrackCollection tracksAll = new TrackCollection(trackSourceFile, brainMRI.getTransform());
        BrainIndex brainIndex = new BrainIndex(tracksAll, brainMRI);

        initializeSlider(dim);

        stackPane.setOnScroll((ScrollEvent s) ->
        {            
            if (s.getDeltaY() < 0)
            {
                slider.adjustValue(slider.getValue() - 1);
            } else
            {
                slider.adjustValue(slider.getValue() + 1);
            }
        });

        MRICanvas mriCanvas = new MRICanvas(this, brainMRI);
        ROIManager roiManager = initialiseROI(dim, brainIndex);

        TrackCanvas trackCanvas = new TrackCanvas(this, dim);
        TrackUI trackUI = new TrackUI(toolPanel);
        
        trackHiTrackCanvas = new TrackCanvas(this, dim);
        
        TrackManager trackManager = new TrackManager(trackCanvas, trackUI);
        roiManager.setTrackManager(trackManager);
        
        TrackCollection tracksMRTRX = new TrackCollection(mrtrixTrackFile, brainMRI.getTransform());
        trackManager.addTrackProvider(tracksAll);
        trackManager.addTrackProvider(tracksMRTRX);
        
        ROITrackSelectorUI selctorUI = new ROITrackSelectorUI(toolPanel, roiManager, trackManager);
        trackManager.addTrackProvider(selctorUI);
        
        ComparatorUI comparatorUI = new ComparatorUI(toolPanel, trackManager);
        comparatorUI.addTrackProviders(trackManager.getObservableTrackProviders());        
        trackManager.addTrackProvider(comparatorUI);
        
        
    }

    // Load ROI's from source directory and generate tracks using index.
    private ROIManager initialiseROI(Tuple3i dim, BrainIndex brainIndex)
    {
        ROISourceCollection mriSource = new ROISourceCollection(roiDirectory, brainIndex);
        ROICanvas roiCanvas = new ROICanvas(this, dim);
        ROIUI roiUI = new ROIUI(toolPanel);
        ROIManager roiManager = new ROIManager(roiCanvas, roiUI);
        mriSource.getROIList().forEach(roiManager::addROI);
        return roiManager;
    }

    private void initializeSlider(Tuple3i dim)
    {
        slider.setMin(0);
        slider.setMax(dim.z - 1);
        slider.setValue((int) dim.z / 2);
        currentSlice.textProperty().bind(slider.valueProperty().asString("%2.2f"));
    }

    public Slider getSlider()
    {
        return slider;
    }

    public CheckBox getClipPlane()
    {
        return clipPlane;
    }

    public StackPane getStackPane()
    {
        return stackPane;
    }

}
