/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.vecmath.Tuple3i;

/**
 *
 * @author oliver
 */
public class ROICanvas
{

    private int currentSlice;
    private final Canvas canvas;
    private final Pane pane;    
    private Color color = Color.YELLOW;
    private final CheckBox clipPlane;    
    private final Tuple3i dim;
    private Set<RenderableROI> roiList = new HashSet<>();
    private GraphicsContext gc;

    public ROICanvas(Pane pane, CheckBox clipPlane, Slider slider, Tuple3i dim)
    {
        this.currentSlice = (int) slider.getValue();
        this.pane = pane;
        this.clipPlane = clipPlane;
        this.dim = dim;
        canvas = new Canvas(pane.getWidth(), pane.getHeight());
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        clipPlane.selectedProperty().addListener((ob, o, n) -> draw());

        slider.valueProperty().addListener((ob, o, n) ->
        {
            if (n.intValue() != currentSlice)
            {
                currentSlice = n.intValue();
                draw();                
            }
        });

        pane.widthProperty().addListener((ob, o, n) -> draw());
        pane.heightProperty().addListener((ob, o, n) -> draw());

    }
    
    public void addROI(RenderableROI roi)
    {        
        roiList.add(roi);
    }
    
    public void removeROI(RenderableROI roi)
    {
        roiList.remove(roi);
    }
    
    public void setColor(Color c)
    {
        this.color = c;
    }

    private void resize(Pane pane)
    {
        draw();
    }

    public final void draw()
    {        
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (roiList.isEmpty())
        {
            return;
        }

        double side = pane.getWidth() < pane.getHeight() ? pane.getWidth() : pane.getHeight();
        canvas.setWidth(side);
        canvas.setHeight(side);
        double scale = side / dim.x;        
        for (int i = 0; i < dim.x; i++)
        {
            for (int j = 0; j < dim.y; j++)
            {
                for (RenderableROI r : roiList)
                {
                    if (r.isVisible().getValue())
                    {
                        paintROI(r, i, j, scale);
                    }
                }                
            }
        }
    }

    private void paintROI(RenderableROI renderableROI, int i, int j, double scale)
    {
        if (renderableROI.isVoxel(i, j, currentSlice))
        {
            gc.setFill(color);
            gc.fillRect((i - 0.5) * scale, (dim.y - (j + 0.5)) * scale, scale, scale);
        }
        else if (!clipPlane.isSelected() && renderableROI.isOutline(i, j))
        {
            gc.setFill(color.darker());
            gc.fillRect((i - 0.5) * scale, (dim.y - (j + 0.5)) * scale, scale, scale);
        }
    }
}
