/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.canvas;

import au.edu.uq.rcc.FXMLController;
import au.edu.uq.rcc.RenderableROI;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
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
    private final CheckBox clipPlane;    
    private final Tuple3i dim;
    private final Set<RenderableROI> roiList = new HashSet<>();
    private GraphicsContext gc;

    public ROICanvas(FXMLController controler, Tuple3i dim)
    {
        this.dim = dim;
        this.currentSlice = (int) controler.getSlider().getValue();
        this.pane = controler.getStackPane();
        this.clipPlane = controler.getClipPlane();
        canvas = new Canvas(pane.getWidth(), pane.getHeight());
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        clipPlane.selectedProperty().addListener((ob, o, n) -> draw());

        controler.getSlider().valueProperty().addListener((ob, o, n) ->
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
        draw();
    }
    
    public void removeROI(RenderableROI roi)
    {
        roiList.remove(roi);
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
            gc.setFill(renderableROI.getColor());
            gc.fillRect((i - 0.5) * scale, (dim.y - (j + 0.5)) * scale, scale, scale);
        }
        else if (!clipPlane.isSelected() && renderableROI.isOutline(i, j))
        {
            gc.setFill(renderableROI.getColor().darker());
            gc.fillRect((i - 0.5) * scale, (dim.y - (j + 0.5)) * scale, scale, scale);
        }
    }
}
