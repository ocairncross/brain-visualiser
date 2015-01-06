/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.ui;

import au.edu.uq.rcc.RenderableROI;
import au.edu.uq.rcc.TrackProvider;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author oliver
 */
public class TrackUI extends UIGroup
{

    Map<TrackProvider, Node> providerMap = new HashMap<>();
    
    public TrackUI(Pane pane)
    {
        super(pane);
        titleLabel.setText("Track");
    }
    
    public void removeTrackProvider(TrackProvider trackProvider)
    {
        stack.getChildren().remove(providerMap.get(trackProvider));
    }

    public BooleanProperty addTrackProvider(TrackProvider trackProvider)
    {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        
        if (trackProvider instanceof RenderableROI)
        {
            BackgroundFill bf = new BackgroundFill(Color.CORAL, corderRadii, Insets.EMPTY);
            vBox.setBackground(new Background(bf));
        }
        
        Label labelSource = new Label();
        Label labelInfo = new Label();
        CheckBox checkBox = new CheckBox();

        vBox.setPadding(new Insets(5.0));
        vBox.setBorder(border);

        labelSource.setText(trackProvider.getName());
        labelSource.setMaxWidth(Double.MAX_VALUE);
        labelInfo.setText("tracks: " + trackProvider.getTrackList().size());

        HBox.setHgrow(labelSource, Priority.ALWAYS);
        hBox.getChildren().addAll(labelSource, checkBox);
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(labelInfo);

        stack.getChildren().add(vBox);

        providerMap.put(trackProvider, vBox);
        
        return checkBox.selectedProperty();
    }

}
