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
public class TrackSourceManager implements ChangeListener<Boolean>
{
 
    Map<BooleanProperty, TrackProvider> trackProviders;
    TrackCanvas trackCanvas;
    TrackProviderUI trackProviderUI;
    
    public TrackSourceManager(TrackCanvas trackCanvas, TrackProviderUI trackProviderUI)
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
        System.out.printf("%s\n", observable.toString());
        if (observable.getValue() == true)
        {
            System.out.printf("Setting others to false\n");
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
