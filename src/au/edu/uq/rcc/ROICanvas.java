/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.vecmath.Tuple3i;

/**
 *
 * @author oliver
 */
public class ROICanvas implements ChangeListener<Number>
{

    private int currentSlice;
    private final Canvas canvas;
    private final Pane pane;
    private RegionOfInterest roi;    
    private Color color = Color.YELLOW;

    public ROICanvas(int currentSlice, Pane pane)
    {    
        this.currentSlice = currentSlice;
        this.pane = pane;
        canvas = new Canvas(pane.getWidth(), pane.getHeight());
        this.bind(pane.heightProperty());
        this.bind(pane.widthProperty());
        pane.getChildren().add(canvas);
    }

    public void setROI(RegionOfInterest roi)
    {
        this.roi = roi;
        drawROI();
    }

    public void setColor(Color c)
    {
        this.color = c;
    }

    private void bind(ObservableValue observable)
    {
        observable.addListener((ob, o, n) -> resize(pane));
    }

    private void resize(Pane pane)
    {
        drawROI();
    }

    public void drawROI()
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (roi == null)
        {
            return;
        }
        
        double side = pane.getWidth() < pane.getHeight() ? pane.getWidth() : pane.getHeight();
        canvas.setWidth(side);
        canvas.setHeight(side);
        double scale = side / roi.getDimensions().x;
        
        gc.setFill(color);
        gc.setStroke(color);
        Tuple3i dim = roi.getDimensions();
        for (int i = 0; i < dim.x; i++)
        {
            for (int j = 0; j < dim.y; j++)
            {
                for (int k = 0; k < dim.z; k++)
                {
                    if (roi.getRoiMask()[i][j][k] && k == currentSlice)
                    {
                        // gc.fillOval(i * scale, (dim.y - j) * scale, scale, scale);
                        gc.strokeOval(i * scale, (dim.y - j) * scale, scale, scale);
                    }
                }
            }
        }        
    }
    
    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
    {        
        if (newValue.intValue() != currentSlice)
        {            
            currentSlice = newValue.intValue();
            drawROI();
        }
    }

}
