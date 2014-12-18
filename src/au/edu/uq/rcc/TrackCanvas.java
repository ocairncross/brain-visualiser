/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3i;
import javax.vecmath.Vector3d;

/**
 *
 * @author oliver
 */
public class TrackCanvas
{

    private final Vector3d unitX = new Vector3d(1.0, 0.0, 0.0);
    private final Vector3d unitY = new Vector3d(0.0, 1.0, 0.0);
    private final Vector3d unitZ = new Vector3d(0.0, 0.0, 1.0);

    // TrackCollection trackList;
    private int currentSlice;
    private final MRISource mri;
    private final Canvas canvas;
    private final Pane pane;
    private final CheckBox clipPlane;
    private double scale = 1.0;
    private final Tuple3i dim;

    private TrackProvider trackProvider = null;
    
    // private List<Track> trackList = null;    
    // private List<PartitionedTrack> partitionedTrackList = null;

    public TrackCanvas(Pane pane, MRISource mri, CheckBox clipPane, Slider slider)
    {
        this.mri = mri;
        this.dim = mri.getDimensions();
        this.currentSlice = (int) slider.getValue();
        this.pane = pane;
        this.clipPlane = clipPane;
        canvas = new Canvas(pane.getWidth(), pane.getHeight());
        this.bind(pane.heightProperty());
        this.bind(pane.widthProperty());
        pane.getChildren().add(canvas);
        
        clipPlane.selectedProperty().addListener((ob, o, n) -> draw());
        
        slider.valueProperty().addListener((ob, o, n) ->
        {
            if (n.intValue() != currentSlice)
            {
                currentSlice = n.intValue();
                if (clipPane.isSelected())
                {
                    draw();                    
                }
            }
        });
        
        pane.widthProperty().addListener((ob, o, n) -> draw());
        pane.heightProperty().addListener((ob, o, n) -> draw());
        
    }

    public void setTrack(TrackProvider trackProvider)
    {
        this.trackProvider = trackProvider;
        draw();
    }

    public void setPartitionedTrack(List<PartitionedTrack> partitionedTrackList)
    {        
    }

    private void draw()
    {
        if (trackProvider != null)
        {
            drawTracks(trackProvider);
        }
        else
        {
            clearTracks();
        }
    }

    public void drawTestPattern()
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.RED);
        gc.setStroke(Color.RED);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.strokeOval(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void bind(ObservableValue observable)
    {
        observable.addListener((ob, o, n) -> resize(pane));
    }

    private void resize(Pane pane)
    {
        double s = pane.getWidth() < pane.getHeight() ? pane.getWidth() : pane.getHeight();
        canvas.setWidth(s);
        canvas.setHeight(s);
        scale = s / dim.x;
    }
    
    private void clearTracks()
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawTracks(TrackProvider trackProvider)
    {
        List<Track> trackList = trackProvider.getTrackList();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double s = canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight();
        scale = s / dim.x;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLUE);
        double height = canvas.getHeight();
        for (int i = 0; i < trackList.size(); i++)
        {
            Track track = trackList.get(i);
            for (int j = 0; j < track.numberOfVertices() - 1; j++)
            {
                Tuple3d p0 = track.getVertices().get(j);
                Tuple3d p1 = track.getVertices().get(j + 1);                
                // Change voxel origin.
                if (clipPlane.isSelected() && (p0.z < currentSlice - 0.5 || p0.z > currentSlice + 0.5))
                {
                    continue;
                }                
                gc.setStroke(getColor(p0, p1));
                gc.strokeLine(p0.x * scale, height - p0.y * scale, p1.x * scale, height - p1.y * scale);
            }
        }
    }

    private void drawPartitionedTracks(List<PartitionedTrack> partitionedTracks)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double height = canvas.getHeight();
        partitionedTracks.stream().forEach(
                t ->
                {
                    t.getTrackIntervals()
                    .stream()
                    .forEach(ti ->
                            {

                                for (int j = ti.getStart(); j < ti.getEnd() - 1; j++)
                                {
                                    Tuple3d p0 = ti.getTrack().getVertices().get(j);
                                    Tuple3d p1 = ti.getTrack().getVertices().get(j + 1);                                    
                                    if (clipPlane.isSelected() && (p0.z < currentSlice - 1 || p0.z > currentSlice + 2))
                                    {
                                        continue;
                                    }                                    
                                    gc.setStroke(getColor(p0, p1));
                                    gc.strokeLine(p0.x * scale, height - p0.y * scale, p1.x * scale, height - p1.y * scale);
                                }
                    }
                    );
                }
        );

    }

    private Color getColor(Tuple3d p0, Tuple3d p1)
    {
        Vector3d v = new Vector3d(p1);
        v.sub(p0);
        v.normalize();
        double r = Math.abs(v.dot(unitX));
        double g = Math.abs(v.dot(unitY));
        double b = Math.abs(v.dot(unitZ));
        return new Color(r, g, b, 1.0);
    }

}
