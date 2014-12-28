/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.canvas;

import au.edu.uq.rcc.FXMLController;
import au.edu.uq.rcc.PartitionedTrack;
import au.edu.uq.rcc.Track;
import au.edu.uq.rcc.TrackProvider;
import java.util.Collection;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3i;
import javax.vecmath.Vector3d;
import org.apache.commons.math3.util.FastMath;

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
    private final Canvas canvas;
    private final Pane pane;
    private final CheckBox clipPlane;
    private double scale = 1.0;
    private final Tuple3i dim;

    private TrackProvider trackProvider = null;

    // private List<Track> trackList = null;    
    // private List<PartitionedTrack> partitionedTrackList = null;
    public TrackCanvas(FXMLController controler, Tuple3i dim)
    {
        this.dim = dim;
        this.currentSlice = (int) controler.getSlider().getValue();
        this.pane = controler.getStackPane();
        this.clipPlane = controler.getClipPlane();
        canvas = new Canvas(pane.getWidth(), pane.getHeight());
        this.bind(pane.heightProperty());
        this.bind(pane.widthProperty());
        pane.getChildren().add(canvas);

        clipPlane.selectedProperty().addListener((ob, o, n) -> draw());

        controler.getSlider().valueProperty().addListener((ob, o, n) ->
        {
            if (n.intValue() != currentSlice)
            {
                currentSlice = n.intValue();
                if (clipPlane.isSelected())
                {
                    draw();
                }
            }
        });

        pane.widthProperty().addListener((ob, o, n) -> draw());
        pane.heightProperty().addListener((ob, o, n) -> draw());

    }

    private void bind(ObservableValue observable)
    {
        observable.addListener((ob, o, n) -> resize(pane));
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
        } else
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
        Collection<Track> trackList = trackProvider.getTrackList();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double s = canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight();
        scale = s / dim.x;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLUE);
        double height = canvas.getHeight();

        trackList.stream()
                .limit(clipPlane.isSelected() ? Long.MAX_VALUE : FXMLController.DISPLAY_MAX_TRACKS)
                .forEach((Track track) ->
                {
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
        );
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
        
//        Vector3d v = new Vector3d(p1);
//        v.sub(p0);
//        v.normalize();
//        double r = Math.abs(v.dot(unitX));
//        double g = Math.abs(v.dot(unitY));
//        double b = Math.abs(v.dot(unitZ));
        
        double x = p1.x - p0.x;
        double y = p1.y - p0.y;
        double z = p1.z - p0.z;        
        double norm = FastMath.sqrt((x * x) + (y * y) + (z * z));       
        x /= norm;
        y /= norm;
        z /= norm;               
        return new Color(FastMath.abs(x), FastMath.abs(y), FastMath.abs(z), 1.0);
    }

}
