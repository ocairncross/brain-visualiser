/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author oliver
 */
public class ROIManager implements ChangeListener<Boolean>
{
 
    Map<BooleanProperty, RegionOfInterest> trackProviders;
    ROICanvas roiCanvas;
    ROIUI roiUI;
    
    public ROIManager(ROICanvas roiCanvas, ROIUI roiUI)
    {
        trackProviders = new HashMap<>();
        this.roiCanvas = roiCanvas;
        this.roiUI = roiUI;
    }
    
    public void addROI(RegionOfInterest roi)
    {
        BooleanProperty boolProp = roiUI.addSource(roi);
        boolProp.addListener(this);
        trackProviders.put(boolProp, roi);
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
    {        
        if (observable.getValue() == true)
        {            
            // Do something true
        }
        else
        {
            // Do something false
        }
    }
    
    
}
