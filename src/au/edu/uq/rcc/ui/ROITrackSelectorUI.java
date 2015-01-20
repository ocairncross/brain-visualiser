/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.ui;

import au.edu.uq.rcc.RegionOfInterest;
import au.edu.uq.rcc.RenderableROI;
import au.edu.uq.rcc.Track;
import au.edu.uq.rcc.TrackProvider;
import au.edu.uq.rcc.manager.ROIManager;
import au.edu.uq.rcc.manager.TrackManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author oliver
 */
public class ROITrackSelectorUI extends UIGroup implements TrackProvider
{
    
    private ComboBox<RenderableROI> combo1;
    private ComboBox<RenderableROI> combo2;
    private List<Track> tracklist = Collections.EMPTY_LIST;
    private ROIManager roiManager;
    private TrackManager trackManager;
    
    public ROITrackSelectorUI(Pane pane, ROIManager roiManager, TrackManager trackManager)
    {
        super(pane);
        this.roiManager = roiManager;
        this.trackManager = trackManager;
        titleLabel.setText("ROI Track Selector");        
        
        ObservableList<RenderableROI> observableROIList = FXCollections.observableList(new ArrayList(roiManager.getROIs()));        
        roiManager.getROIs();
        
        combo1 = new ComboBox<>(observableROIList.sorted((e1, e2) -> {return e1.getName().compareTo(e2.getName());}));
        combo2 = new ComboBox<>(observableROIList.sorted((e1, e2) -> {return e1.getName().compareTo(e2.getName());}));
        combo1.setOnAction(e -> {inputChange();});
        combo2.setOnAction(e -> {inputChange();});
        
        TwoColumnGrid tcg = new TwoColumnGrid();
        tcg.addRow("ROI 1:", combo1);
        tcg.addRow("ROI 2:", combo2);
        
        stack.getChildren().add(tcg.getGrid());
        
    }

    @Override
    public Collection<Track> getTrackList()
    {
        return tracklist;
    }

    @Override
    public String getName()
    {
        return "Track Selector";
    }
    
    private void inputChange()
    {        
        List<RenderableROI> collect = Arrays.asList(combo1.getValue(), combo2.getValue())
                .stream().filter(e -> e != null)
                .collect(Collectors.toList());
                        
        roiManager.setSelectedROIs(collect);        
        
        if (combo1.getValue() == null || combo2.getValue() == null)
        {
            return;
        }        
        RegionOfInterest roi1 = combo1.getValue().getRoi();
        RegionOfInterest roi2 = combo2.getValue().getRoi();    
        tracklist = roi1.getCommonTracks(roi2); 
        trackManager.fireChange();
    }
    
    
    
}
