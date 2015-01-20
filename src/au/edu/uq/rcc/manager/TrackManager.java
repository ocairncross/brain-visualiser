/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.manager;

import au.edu.uq.rcc.TrackProvider;
import au.edu.uq.rcc.ui.TrackUI;
import au.edu.uq.rcc.canvas.TrackCanvas;
import au.edu.uq.rcc.ui.TrackChart;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author oliver
 */
public class TrackManager implements ChangeListener<Boolean>
{
    BiMap<BooleanProperty, TrackProvider> biTrackProviders;
    ObservableList<TrackProvider> observableTrackProviders;
    TrackCanvas trackCanvas;
    TrackUI trackProviderUI;
    
    public TrackManager(TrackCanvas trackCanvas, TrackUI trackProviderUI)
    {
        observableTrackProviders = FXCollections.observableArrayList();
        biTrackProviders = HashBiMap.create();
        this.trackCanvas = trackCanvas;
        this.trackProviderUI = trackProviderUI;
    }
    
    public void addTrackProvider(TrackProvider trackProvider)
    {
        BooleanProperty boolProp = trackProviderUI.addTrackProvider(trackProvider);
        boolProp.addListener(this);
        biTrackProviders.put(boolProp, trackProvider);
        observableTrackProviders.add(trackProvider);
    }
    
    public void removeTrackProvider(TrackProvider trackProvider)
    {
        trackProviderUI.removeTrackProvider(trackProvider);       
        if (trackCanvas != null && biTrackProviders.inverse().get(trackProvider).getValue())
        {
            trackCanvas.setTrack(null);
        }
        biTrackProviders.inverse().remove(trackProvider);
        observableTrackProviders.remove(trackProvider);
    }
    
    public ObservableList<TrackProvider> getObservableTrackProviders()
    {
        return observableTrackProviders;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
    {        
        if (observable.getValue() == true)
        {     
            biTrackProviders.keySet()
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
                trackCanvas.setTrack(biTrackProviders.get(observable));
                TrackChart tc = new TrackChart(biTrackProviders.get(observable));
                tc.plotChart();
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
    
    public void fireChange()
    {        
        trackCanvas.draw();
    }
    
    
}
