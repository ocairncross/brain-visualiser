/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.manager;

import au.edu.uq.rcc.TrackProvider;
import au.edu.uq.rcc.TrackUI;
import au.edu.uq.rcc.canvas.TrackCanvas;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author oliver
 */
public class TrackManager implements ChangeListener<Boolean>
{
 
    Map<BooleanProperty, TrackProvider> trackProviders;
    TrackCanvas trackCanvas;
    TrackUI trackProviderUI;
    
    public TrackManager(TrackCanvas trackCanvas, TrackUI trackProviderUI)
    {
        trackProviders = new HashMap<>();
        this.trackCanvas = trackCanvas;
        this.trackProviderUI = trackProviderUI;
    }
    
    public void addTrackProvider(TrackProvider trackProvider)
    {
        BooleanProperty boolProp = trackProviderUI.addSource(trackProvider);
        boolProp.addListener(this);        
        trackProviders.put(boolProp, trackProvider);
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
    {        
        if (observable.getValue() == true)
        {     
            trackProviders.keySet()
                    .stream()
                    .filter(b -> !b.equals(observable) && b.getValue() == true)
                    .forEach(b ->
                    {
                        b.removeListener(this);
                        b.setValue(false);
                        b.addListener(this);
                    });
            if (trackCanvas != null)
            {
                trackCanvas.setTrack(trackProviders.get(observable));
            }
        }
        else
        {
            if (trackCanvas != null)
            {
                trackCanvas.setTrack(null);
            }
        }
    }
    
    
}
