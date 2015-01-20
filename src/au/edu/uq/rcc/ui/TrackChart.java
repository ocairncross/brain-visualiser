/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uq.rcc.ui;

import au.edu.uq.rcc.BrainVisualiser;
import au.edu.uq.rcc.FXMLController;
import au.edu.uq.rcc.Track;
import au.edu.uq.rcc.TrackProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import utils.MinMax;

/**
 *
 * @author oliver
 */
public class TrackChart
{

    TrackProvider trackProvider;

    public TrackChart(TrackProvider trackProvider)
    {
        this.trackProvider = trackProvider;
    }

    public void plotChart()
    {

        MinMax range = new MinMax();

        trackProvider.getTrackList()
                .stream()
                .mapToInt(t -> t.numberOfVertices())
                .forEach(i -> range.setVal(i));

        Map<String, List<Track>> trackBinCollection = trackProvider.getTrackList()
                .stream()
                .collect(new binTrackCollectorMap(range, 10));

        XYChart.Series series = new XYChart.Series();
        trackBinCollection.forEach((k, v) -> series.getData().add(new XYChart.Data<>(k, v.size())));

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.getData().add(series);

        AnchorPane.setTopAnchor(barChart, 10.0);
        AnchorPane.setBottomAnchor(barChart, 10.0);
        AnchorPane.setLeftAnchor(barChart, 10.0);
        AnchorPane.setRightAnchor(barChart, 10.0);

        BrainVisualiser.chartPane.getChildren().add(barChart);

        // barChart.getChildrenUnmodifiable().forEach(c -> System.out.printf("child %s\n", c.toString()));
        setupHover(series, trackBinCollection);

    }

    private final Glow glow = new Glow(.8);

    private void setupHover(XYChart.Series<String, Number> series, Map<String, List<Track>> trackBinCollection)
    {
        series.getData().stream().forEach((XYChart.Data<String, Number> dt) ->
        {

            final Node n = dt.getNode();
            n.setEffect(null);

            n.setOnMouseEntered((MouseEvent e) ->
            {
                n.setEffect(glow);
                FXMLController.trackHiTrackCanvas.setGlow(true);
                final List<Track> trackList = trackBinCollection.get(dt.getXValue());
                FXMLController.trackHiTrackCanvas.setTrack(new TrackProvider()                        
                {
                    @Override
                    public Collection<Track> getTrackList()
                    {
                        return trackList;
                    }

                    @Override
                    public String getName()
                    {
                        return "chart tracks";
                    }
                });
            });

            n.setOnMouseExited((MouseEvent e) ->
            {
                FXMLController.trackHiTrackCanvas.setTrack(null);
                n.setEffect(null);
            });

            n.setOnMouseClicked((MouseEvent e) ->
            {
                System.out.println("openDetailsScreen(<selected Bar>)");
                System.out.println(dt.getXValue() + " : " + dt.getYValue());
            });
        });
    }

    private class binTrackCollectorMap implements Collector<Track, Map<String, List<Track>>, Map<String, List<Track>>>
    {

        private final MinMax minMax;
        private final int bins;
        private final double binWidth;

        public binTrackCollectorMap(MinMax minMax, int bins)
        {
            this.minMax = minMax;
            this.bins = bins;
            binWidth = minMax.getRange() / bins;
        }

        @Override
        public Supplier<Map<String, List<Track>>> supplier()
        {
            return () ->
            {
                HashMap<String, List<Track>> hm = new HashMap<>();
                for (int i = 0; i < bins; i++)
                {
                    hm.put(String.valueOf(i), new ArrayList<>());
                }
                return hm;
            };
        }

        @Override
        public BiConsumer<Map<String, List<Track>>, Track> accumulator()
        {
            return (Map<String, List<Track>> t, Track u) ->
            {
                int v = u.numberOfVertices() - (int) minMax.getMin();
                int bin = (int) (v / binWidth);
                if (bin == bins)
                {
                    bin--;
                }
                t.get(String.valueOf(bin)).add(u);
            };
        }

        @Override
        public BinaryOperator<Map<String, List<Track>>> combiner()
        {
            return (Map<String, List<Track>> t, Map<String, List<Track>> u) ->
            {
                t.keySet().forEach(k ->
                {
                    t.get(k).addAll(u.get(k));
                });
                return t;
            };
        }

        @Override
        public Function<Map<String, List<Track>>, Map<String, List<Track>>> finisher()
        {
            return (Map<String, List<Track>> t) -> t;
        }

        @Override
        public Set<Collector.Characteristics> characteristics()
        {
            return EnumSet.of(Collector.Characteristics.UNORDERED);
        }

    }

}
