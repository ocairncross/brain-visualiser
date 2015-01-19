/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.ui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author oliver
 */
public class TwoColumnGrid
{
    
    int currnentRow = 0;
    GridPane gridPane = new GridPane();

    public TwoColumnGrid()
    {
        gridPane = new GridPane();
        
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHalignment(HPos.RIGHT);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);
        cc2.setMaxWidth(Double.MAX_VALUE);
        
        HBox hBox = new HBox();
        
        gridPane.getColumnConstraints().addAll(cc1, cc2);
        HBox.setHgrow(gridPane, Priority.ALWAYS);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        hBox.getChildren().add(gridPane);
        
    }
    
    public void addRow(String label, Node control)
    {
        gridPane.add(createLabel(label) , 0, currnentRow);
        gridPane.add(control, 1, currnentRow);
        currnentRow++;
    }
    
    private Label createLabel(String labelText)
    {
        final Insets insets = new Insets(0, 10, 0, 0);
        Label label = new Label(labelText);
        GridPane.setMargin(label, insets);
        return label;
    }
    
    public Node getGrid()
    {
        return gridPane;
    }
    
    
    
}
