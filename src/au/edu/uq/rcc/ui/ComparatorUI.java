/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.ui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 *
 * @author oliver
 */
public class ComparatorUI extends UIGroup
{
    
    Label label1;
    Label label2;
    ComboBox combo1;
    ComboBox combo2;
    GridPane gridPane;
    
    ObservableList obList;
    

    public ComparatorUI(Pane pane)
    {
        super(pane);
        
        ColumnConstraints cc1 = new ColumnConstraints();        
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);
        cc2.setMaxWidth(Double.MAX_VALUE);
        
        HBox hBox = new HBox();
        stack.getChildren().add(hBox);
        
        gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        
        titleLabel.setText("Comparator");
        label1 = new Label("src 1:");
        label2 = new Label("src 2:");
        combo1 = new ComboBox();
        combo2 = new ComboBox();
        
        // cc1.setMinWidth(label1.getPrefWidth());
        
        combo1.setMaxWidth(Double.MAX_VALUE);
        combo2.setMaxWidth(Double.MAX_VALUE);
        
        
        GridPane.setConstraints(label1, 0, 0);
        GridPane.setConstraints(label2, 0, 1);
        GridPane.setConstraints(combo1, 1, 0);
        GridPane.setConstraints(combo2, 1, 1);
        GridPane.setMargin(label1, new Insets(0, 10, 0, 0));
        GridPane.setMargin(label2, new Insets(0, 10, 0, 0));
        
        gridPane.getColumnConstraints().addAll(cc1, cc2);
        
        gridPane.getChildren().add(label1);
        gridPane.getChildren().add(label2);
        gridPane.getChildren().add(combo1);
        gridPane.getChildren().add(combo2);
        
        HBox.setHgrow(gridPane, Priority.ALWAYS);        
        gridPane.setMaxWidth(Double.MAX_VALUE);
        hBox.getChildren().add(gridPane);
        
    }
    
    
    
}
