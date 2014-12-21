/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import java.awt.Color;
import javafx.beans.property.BooleanProperty;
import javax.vecmath.Tuple3i;

/**
 *
 * @author oliver
 */
public class RenderableROI
{
    RegionOfInterest roi;
    BooleanProperty isVisible;
    boolean[][] roiMask;
    Color color = Color.red;

    public RenderableROI(RegionOfInterest roi)
    {
        this.roi = roi;
        roiMask = getROIMask(roi);
    }
    
    public boolean isOutline(int x, int y)
    {
        return roiMask[x][y];
    }
    
    public boolean isVoxel(int x, int y, int z)
    {
        return roi.getRoiMask()[x][y][z];
    }
    
    public String getName()
    {
        return roi.getName();
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public BooleanProperty isVisible()
    {
        return isVisible;
    }

    public void setVisible(BooleanProperty isVisible)
    {
        this.isVisible = isVisible;
    }
    
    private boolean[][] getROIMask(RegionOfInterest roi)
    {
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
        return roiMask;
    }
    
}
