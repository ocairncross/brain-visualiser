/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;


import au.edu.uq.rcc.canvas.ROICanvas;
import java.util.Collection;
import javafx.beans.property.BooleanProperty;
import javafx.scene.paint.Color;
import javax.vecmath.Tuple3i;

/**
 *
 * @author oliver
 */
public class RenderableROI implements TrackProvider
{
    RegionOfInterest roi;
    BooleanProperty isVisible;
    boolean[][] roiMask;
    Color color = Color.YELLOW;
    ROICanvas canvas;
    

    public RenderableROI(RegionOfInterest roi, ROICanvas canvas) 
    {
        this.roi = roi;
        this.canvas = canvas;
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
    
    @Override
    public String getName()
    {
        return roi.getName();
    }

    @Override
    public Collection<Track> getTrackList()
    {
        return roi.getTracks();
    }
    
    public int getNumberOfTracks()
    {
        return roi.numberOfTracks();
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {        
        this.color = color;
        canvas.draw();
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
    
    
    @Override
    public String toString()
    {
        return "ROI: " + getName();
    }
    
}
