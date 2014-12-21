/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
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

    public BooleanProperty addSource(RegionOfInterest roi)
    {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        Label labelSource = new Label();
        Label labelInfo = new Label();
        CheckBox checkBox = new CheckBox();

        vBox.setPadding(new Insets(5.0));
        vBox.setBorder(border);

        labelSource.setText(roi.getName());
        labelSource.setMaxWidth(Double.MAX_VALUE);

        labelInfo.setText("tracks: " + roi.getTracks().size());

        HBox.setHgrow(labelSource, Priority.ALWAYS);
        hBox.getChildren().addAll(labelSource, checkBox);

        vBox.getChildren().add(hBox);
        vBox.getChildren().add(labelInfo);

        stack.getChildren().add(vBox);

        return checkBox.selectedProperty();
    }

}