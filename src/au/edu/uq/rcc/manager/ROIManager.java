/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.manager;

import au.edu.uq.rcc.ui.ROIUI;
import au.edu.uq.rcc.RegionOfInterest;
import au.edu.uq.rcc.RenderableROI;
import au.edu.uq.rcc.canvas.ROICanvas;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
 
    private final Map<BooleanProperty, RenderableROI> renderableROIs;
    private final ROICanvas roiCanvas;
    private final ROIUI roiUI;
    private TrackManager trackManager;
    
    public ROIManager(ROICanvas roiCanvas, ROIUI roiUI)
    {
        renderableROIs = new HashMap<>();
        this.roiCanvas = roiCanvas;
        this.roiUI = roiUI;
    }
    
    public void addROI(RegionOfInterest roi)
    {
        RenderableROI renderableROI = new RenderableROI(roi, roiCanvas);
        BooleanProperty boolProp = roiUI.addROI(renderableROI);
        boolProp.addListener(this);
        renderableROI.setVisible(boolProp);
        renderableROIs.put(boolProp, renderableROI);
    }

    public void setTrackManager(TrackManager trackManager)
    {
        this.trackManager = trackManager;
    }
    
    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
    {        
        if (observable.getValue() == true)
        {   
            roiCanvas.addROI(renderableROIs.get(observable));
            if (trackManager != null)
            {
                trackManager.addTrackProvider(renderableROIs.get(observable));
            }
        }
        else
        {
            roiCanvas.removeROI(renderableROIs.get(observable));      
            if (trackManager != null)
            {
                trackManager.removeTrackProvider(renderableROIs.get(observable));                               
            }
        }
    }
    
    public Collection<RenderableROI> getROIs()
    {
        return renderableROIs.values();
    }
    
    public void setSelectedROIs(List<RenderableROI> roiSet)
    {        
        renderableROIs.keySet().forEach((BooleanProperty b) ->
        {
            RenderableROI r = renderableROIs.get(b);
            if(roiSet.contains(r))
            {
               if (b.get() == false) 
               {
                   b.set(true);                   
               }               
            }
            else
            {
                if(b.get() == true)
                {
                    b.set(false);
                }
            }
        });
        
    }
    
    
}
