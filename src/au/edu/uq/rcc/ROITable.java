/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import au.edu.uq.rcc.index.BrainIndex;
import java.io.File;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;

/**
 *
 * @author oliver
 */
public class ROITable
{
    
    TableView tableView;
    private ObservableList<RegionOfInterest> roiList = FXCollections.observableArrayList();

    public ROITable(TableView<RegionOfInterest> tableView)
    {
        this.tableView = tableView;
        tableView.setEditable(true);
        
        TableColumn<RegionOfInterest, String> roiNameCol = new TableColumn<>();        
        roiNameCol.setText("ROI");
        roiNameCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getName()));
        tableView.getColumns().add(roiNameCol);
        
        TableColumn<RegionOfInterest, String> roiTrackCol = new TableColumn<>();        
        roiTrackCol.setText("Tracks");
        roiTrackCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().numberOfTracks()));
        tableView.getColumns().add(roiTrackCol);
        
        TableColumn<RegionOfInterest, RegionOfInterest.Selection> roiSelectCol = new TableColumn<>();        
        roiSelectCol.setText("Select");
        roiSelectCol.setCellValueFactory(c -> c.getValue().getSelectionProperty());
        roiSelectCol.setCellFactory(ComboBoxTableCell.<RegionOfInterest, RegionOfInterest.Selection>forTableColumn(RegionOfInterest.Selection.AND, RegionOfInterest.Selection.NOT, RegionOfInterest.Selection.NONE));
        roiSelectCol.setEditable(true);
        tableView.getColumns().add(roiSelectCol);
        
        // dayCol.setCellFactory(ComboBoxTableCell.<WorkDay, Day>forTableColumn(Day.values()));  
        
    }
    
    public void populate(File roiDirectory, BrainIndex index)
    {
        ROISourceCollection sourceCollection = new ROISourceCollection(roiDirectory, index);
        roiList.addAll(sourceCollection.getROIList());
        tableView.setItems(roiList);
    }
    
}
