/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.ui;

import au.edu.uq.rcc.RegionOfInterest;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 *
 * @author oliver
 */
public class ROITrackSelectorUI extends UIGroup
{
    
    ComboBox<RegionOfInterest> combo1;
    ComboBox<RegionOfInterest> combo2;
    
    public ROITrackSelectorUI(Pane pane)
    {
        super(pane);
        titleLabel.setText("ROI Track Selector");
        
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHalignment(HPos.RIGHT);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);
        cc2.setMaxWidth(Double.MAX_VALUE);
        
    }
    
}
