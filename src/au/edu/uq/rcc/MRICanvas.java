/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.vecmath.Tuple3i;

/**
 *
 * @author oliver
 */
public class MRICanvas implements ChangeListener<Number>
{

    private final List<Image> images;    
    private final ImageView imageView;
    private int currentSlice;
    
    public MRICanvas(MRISource mriSource, ImageView imageView)
    {
        Tuple3i dim = mriSource.getDimensions();
        this.imageView = imageView;        
        currentSlice = (int) dim.z / 2;
        images = new ArrayList<>(dim.z);        
        for (int k = 0; k < dim.z; k++)
        {
            WritableImage image = new WritableImage(dim.x, dim.y);
            PixelWriter pixelWriter = image.getPixelWriter();
            for (int i = 0; i < dim.x; i++)
            {
                for (int j = 0; j < dim.y; j++)
                {                    
                    float voxel = (float) (mriSource.getVoxel(i, dim.y - j, k) / mriSource.getMaxVoxel());
                    if (voxel > 1)
                    {
                        voxel = 1;
                    }
                    Color c = new Color(voxel, voxel, voxel, 1.0f);
                    pixelWriter.setColor(i, j, c);
                }
            }
            images.add(image);
        }
        drawCurrentSlice();
    }

    public int getCurrentSlice()
    {
        return currentSlice;
    }
    
    public void bindIntegerProperty(DoubleProperty ip)
    {
        ip.addListener(this);
    }
    
    public final void drawCurrentSlice()
    {
        imageView.setImage(images.get(currentSlice));
    }
    
    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
    {        
        if (newValue.intValue() != currentSlice)
        {            
            currentSlice = newValue.intValue();
            drawCurrentSlice();
        }
    }

}
