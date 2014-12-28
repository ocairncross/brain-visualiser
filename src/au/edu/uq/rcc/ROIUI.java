/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author oliver
 */
public class ROIUI
{

    Pane pane;
    VBox stack;
    BorderWidths bw = new BorderWidths(1.0);
    CornerRadii ci = new CornerRadii(5);
    BorderStroke bs = new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, ci, bw);
    Border border = new Border(bs);
    
    public ROIUI(Pane pane)
    {
        this.pane = pane;
        stack = new VBox();
        stack.setPadding(new Insets(5.0));
        stack.setSpacing(3.0);
        stack.setBorder(border);
        Label titleLabel = new Label("ROI");
        titleLabel.setAlignment(Pos.CENTER);        
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        stack.getChildren().add(titleLabel);
        pane.getChildren().add(stack);
    }

    public BooleanProperty addROI(RenderableROI roi)
    {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        Label labelSource = new Label();
        Label labelInfo = new Label();
        CheckBox checkBox = new CheckBox();
        ColorPicker colorPicker = new ColorPicker(roi.getColor());
        // colorPicker.setPromptText(null);
        colorPicker.getStyleClass().add("button");
        colorPicker.setStyle("-fx-color-label-visible: false");
        
        colorPicker.setMaxSize(18, 18);
        colorPicker.setMinSize(18, 18);        
        colorPicker.setPadding(new Insets(-12.0));
        
        colorPicker.setOnAction((ActionEvent event) ->
        {
            System.out.printf("colour changed %s\n", event.toString());
            roi.setColor(colorPicker.getValue());
        });
        
        vBox.setPadding(new Insets(3.0));
        vBox.setBorder(border);

        labelSource.setText(roi.getName());
        labelSource.setMaxWidth(Double.MAX_VALUE);

        labelInfo.setText("tracks: " + roi.getNumberOfTracks());

        HBox.setHgrow(labelSource, Priority.ALWAYS);        
        hBox.setSpacing(5.0);
        hBox.getChildren().addAll(labelSource, colorPicker, checkBox);

        vBox.getChildren().add(hBox);
        vBox.getChildren().add(labelInfo);

        stack.getChildren().add(vBox);

        return checkBox.selectedProperty();
    }

}
