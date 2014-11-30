/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc;

import java.util.List;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3i;
import javax.vecmath.Vector3d;

/**
 *
 * @author oliver
 */
public class TrackCanvas implements ChangeListener<Number>
{

    private final Vector3d unitX = new Vector3d(1.0, 0.0, 0.0);
    private final Vector3d unitY = new Vector3d(0.0, 1.0, 0.0);
    private final Vector3d unitZ = new Vector3d(0.0, 0.0, 1.0);

    // TrackCollection trackList;
    private int currentSlice;
    private final MRISource mri;
    private final Canvas canvas;
    private final Pane pane;
    private double scale = 1.0;
    private Tuple3i dim;

    public TrackCanvas(int currentSlice, Pane pane, MRISource mri)
    {
        this.mri = mri;
        this.dim = mri.getDimensions();
        this.currentSlice = currentSlice;
        this.pane = pane;
        canvas = new Canvas(pane.getWidth(), pane.getHeight());
        this.bind(pane.heightProperty());
        this.bind(pane.widthProperty());
        pane.getChildren().add(canvas);
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
        drawTestPattern();
    }

    public void drawTracks(List<Track> trackList, boolean clip)
    {
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
                p0 = mri.undoTransform(p0);
                if (clip && (p0.z < currentSlice || p0.z > currentSlice + 1))
                {
                    continue;
                }
                p1 = mri.undoTransform(p1);
                gc.setStroke(getColor(p0, p1));
                gc.strokeLine(p0.x * scale, height - p0.y * scale, p1.x * scale, height - p1.y * scale);
            }
            if (i % 100 == 0)
            {
                System.out.printf("processed track %d\n", i);
            }
        }
    }

    public void drawPartitionedTracks(List<PartitionedTrack> partitionedTracks, boolean clip)
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
                                    p0 = mri.undoTransform(p0);
                                    if (clip && (p0.z < currentSlice - 1 || p0.z > currentSlice + 2))
                                    {
                                        continue;
                                    }
                                    p1 = mri.undoTransform(p1);
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

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
    {
        if (newValue.intValue() != currentSlice)
        {
            currentSlice = newValue.intValue();            
        }
    }

}
