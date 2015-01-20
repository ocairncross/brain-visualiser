/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.ui;

import au.edu.uq.rcc.Track;
import au.edu.uq.rcc.TrackComparator;
import au.edu.uq.rcc.TrackProvider;
import au.edu.uq.rcc.manager.TrackManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author oliver
 */
public class ComparatorUI extends UIGroup implements TrackProvider
{

    ComboBox<TrackProvider> combo1;
    ComboBox<TrackProvider> combo2;
    ComboBox<String> comboOperation;
    GridPane gridPane;

    ObservableList<TrackProvider> trackProviders;
    TrackManager trackManager;
    
    public ComparatorUI(Pane pane, TrackManager trackManager)
    {
        super(pane);
        titleLabel.setText("Track Comparator");
        this.trackManager = trackManager;

        ObservableList<String> observableOperations = FXCollections.observableArrayList("src1", "src2", "union");
        trackProviders = FXCollections.observableArrayList();

        combo1 = createCombo(trackProviders);
        combo2 = createCombo(trackProviders);
        comboOperation = createCombo(observableOperations);
        comboOperation.setValue("src1");

        TwoColumnGrid tcg = new TwoColumnGrid();
        tcg.addRow("source 1:", combo1);
        tcg.addRow("source 2:", combo2);
        tcg.addRow("operation:", comboOperation);
        
        Comparator comparator = new Comparator(comboOperation.valueProperty(), 
                combo1.valueProperty(), 
                combo2.valueProperty()
        );

        stack.getChildren().add(tcg.getGrid());
    }

    private ComboBox createCombo(ObservableList obervableList)
    {
        ComboBox comboBox = new ComboBox(obervableList);        
        return comboBox;
    }

    public void addTrackProviders(ObservableList<TrackProvider> trackProviders)
    {
        this.trackProviders = trackProviders;
        trackProviders.addListener((ListChangeListener.Change<? extends TrackProvider> c) ->
        {
            fireChange();
        });
        fireChange();
    }

    private void fireChange()
    {
        Stream.of(combo1, combo2).forEach(c ->
        {
            c.setItems(trackProviders);
            if (!trackProviders.contains(c.getValue()))
            {
                c.setValue(null);
            }
        });
    }

    private Collection<Track> trackList = Collections.EMPTY_LIST;

    @Override
    public Collection<Track> getTrackList()
    {
        return trackList;
    }

    @Override
    public String getName()
    {
        return "Track Comparator";
    }

    private class Comparator
    {

        ObjectProperty<String> operation;
        ObjectProperty<TrackProvider> source1;
        ObjectProperty<TrackProvider> source2;

        public Comparator(ObjectProperty<String> operation, ObjectProperty<TrackProvider> source1, ObjectProperty<TrackProvider> source2)
        {
            this.operation = operation; 
            
            operation.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
            {
                compare();
            });
            
            source1.addListener(e -> compare());
            source2.addListener(e -> compare());
            
        }
        
        private void compare()
        {
            if (operation.get().equals("src1"))
                {                 
                    if (combo1.getValue() != null)
                    {
                        trackList = combo1.getValue().getTrackList();                                            
                    }
                    else
                    {
                        trackList = Collections.EMPTY_LIST;
                    }
                    System.out.printf("set: %d\n", trackList.size());
                    trackManager.fireChange();
                }
                else if (operation.get().equals("src2"))
                {
                    if (combo2.getValue() != null)
                    {
                        trackList = combo2.getValue().getTrackList();                                            
                    }
                    else
                    {
                        trackList = Collections.EMPTY_LIST;
                    }
                    System.out.printf("set: %d\n", trackList.size());
                    trackManager.fireChange();
                }
                else if (operation.get().equals("union"))
                {   
                    if (combo1.getValue() != null && combo2.getValue() != null)
                    {                        
                        List<Track> trackList1 = new ArrayList<>(combo1.getValue().getTrackList());
                        List<Track> trackList2 = new ArrayList<>(combo2.getValue().getTrackList());                                                
                        TrackComparator tc = new TrackComparator(trackList1, trackList2);                        
                        trackList = tc.getSameTracks();
                    }
                    else
                    {
                        trackList = Collections.EMPTY_LIST;
                    }
                    System.out.printf("set: %d\n", trackList.size());
                    trackManager.fireChange();
                }
        }
        
    }

}
