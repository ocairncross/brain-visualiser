/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
public abstract class UIGroup
{

    protected Pane pane;
    protected VBox stack;    
    protected BorderStroke borderStroke;
    protected Border border;
    protected BorderWidths borderWidth;
    protected CornerRadii corderRadii;
    protected Label titleLabel;

    public UIGroup(Pane pane)
    {
        this.pane = pane;
        borderStroke = new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, corderRadii, borderWidth);
        border = new Border(borderStroke);
        borderWidth = new BorderWidths(1.0);
        corderRadii = new CornerRadii(5);
        stack = new VBox();
        stack.setPadding(new Insets(5.0));
        stack.setSpacing(3.0);
        stack.setBorder(border);
        titleLabel = new Label();
        titleLabel.setAlignment(Pos.CENTER);        
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        stack.getChildren().add(titleLabel);
        pane.getChildren().add(stack);
    }

}
