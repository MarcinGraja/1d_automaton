package main.java.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class Drawer {
    private GraphicsContext graphicsContext;
    private int pointSize;
    private int rowSize;
    private int iterations;
    private int canvasWidth;
    private int canvasHeight;
    public Drawer(GraphicsContext graphicsContext, int rowSize, int iterations) {
        this.graphicsContext = graphicsContext;

        canvasWidth = (int) graphicsContext.getCanvas().getWidth();
        canvasHeight = (int) graphicsContext.getCanvas().getHeight();
        this.rowSize = rowSize;
        this.iterations = iterations;
        this.pointSize = Math.min(canvasWidth / rowSize, canvasHeight /iterations);
    }
    public void draw(List<Tuple<Double,Double>> points){

        for (Tuple<Double, Double> point : points) {
            transformPoint(point, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(point.x, point.y, pointSize, pointSize);
        }
    }
    private void transformPoint(Tuple <Double, Double> point, int windowWidth, int windowHeight){
        point.x = point.x * windowWidth / rowSize;
        point.y = point.y * windowHeight / iterations;
    }
}
