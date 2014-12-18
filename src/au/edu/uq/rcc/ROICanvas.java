/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

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
    private RegionOfInterest roi;
    private Color color = Color.YELLOW;
    private final CheckBox clipPlane;
    private boolean[][] roiMask;

    public ROICanvas(Pane pane, CheckBox clipPlane, Slider slider)
    {
        this.currentSlice = (int) slider.getValue();
        this.pane = pane;
        this.clipPlane = clipPlane;
        canvas = new Canvas(pane.getWidth(), pane.getHeight());
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

    public void setROI(RegionOfInterest roi)
    {
        this.roi = roi;
        Tuple3i dim = roi.getDimensions();
        roiMask = new boolean[dim.x][dim.y];
        for (int i = 0; i < dim.x; i++)
        {
            for (int j = 0; j < dim.y; j++)
            {
                for (int k = 0; k < dim.z; k++)
                {
                    if (roi.getRoiMask()[i][j][k])
                    {
                        roiMask[i][j] = true;
                    }
                }
            }
        }
        draw();
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
        Tuple3i dim = roi.getDimensions();
        for (int i = 0; i < dim.x; i++)
        {
            for (int j = 0; j < dim.y; j++)
            {
                if (roi.getRoiMask()[i][j][currentSlice])
                {
                    gc.setFill(color);
                    gc.fillRect((i - 0.5) * scale, (dim.y - (j + 0.5)) * scale, scale, scale);
                }
                else if (!clipPlane.isSelected() && roiMask[i][j])
                {
                    gc.setFill(color.darker());
                    gc.fillRect((i - 0.5) * scale, (dim.y - (j + 0.5)) * scale, scale, scale);
                }                
            }
        }
    }
}
