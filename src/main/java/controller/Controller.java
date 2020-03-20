package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import main.java.model.Drawer;
import main.java.model.Tuple;

import java.util.ArrayList;

public class Controller {
    private double max = 100;
    private double min = -100;
    @FXML
    private Canvas canvas;
    @FXML
    private Spinner<Integer> rowWidthSpinner;
    @FXML
    private Spinner<Integer> iterationsSpinner;
    @FXML
    private Spinner<Integer> ruleSpinner;
    @FXML
    private GridPane gridPane;
    @FXML
    private CheckBox maxResolutionOnResizeCheckBox;
    @FXML
    private CheckBox autoRedrawCheckBox;
    private void resetSpinners(boolean setToMax){
        int oldValue;
        if (rowWidthSpinner.getValue() != null) {
            oldValue = rowWidthSpinner.getValue();
        }
        else oldValue = (int) canvas.getWidth();

        rowWidthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, (int) canvas.getWidth()));
        rowWidthSpinner.getValueFactory().setValue((int)Math.min(oldValue, canvas.getWidth()));
        rowWidthSpinner.getValueFactory().valueProperty().addListener((observableValue, integer, t1) -> {
            if (autoRedrawCheckBox.isSelected()){
                draw();
            }
        });

        if (iterationsSpinner.getValue() != null)
            oldValue = iterationsSpinner.getValue();
        else oldValue = (int) canvas.getHeight();

        iterationsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, (int) canvas.getHeight()));
        iterationsSpinner.getValueFactory().setValue((int)Math.min(oldValue, canvas.getHeight()));
        iterationsSpinner.getValueFactory().valueProperty().addListener((observableValue, integer, t1) -> {
            if (autoRedrawCheckBox.isSelected()){
                draw();
            }
        });
        if (setToMax){
            rowWidthSpinner.getValueFactory().setValue((int) canvas.getWidth());
            iterationsSpinner.getValueFactory().setValue((int) canvas.getHeight());
        }
    }
    @FXML
    public void initialize() {
        resetSpinners(true);

        gridPane.widthProperty().addListener((observableValue, oldValue, newValue) -> {
            canvas.setWidth(newValue.doubleValue() - gridPane.getColumnConstraints().get(0).getMaxWidth());
            resetSpinners(maxResolutionOnResizeCheckBox.isSelected());
            if (autoRedrawCheckBox.isSelected()) {
                draw();
            }
        });

        gridPane.heightProperty().addListener((observableValue, oldValue, newValue) -> {
            canvas.setHeight(newValue.doubleValue()- gridPane.getRowConstraints().get(1).getMaxHeight());
            resetSpinners(maxResolutionOnResizeCheckBox.isSelected());
            if (autoRedrawCheckBox.isSelected()) {
                draw();
            }
        });
        ruleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, 90));
        ruleSpinner.getValueFactory().valueProperty().addListener((observableValue, integer, t1) -> {
            if (autoRedrawCheckBox.isSelected()){
                draw();
            }
        });
    }
    @FXML
    public void draw(){
        if (maxResolutionOnResizeCheckBox.isSelected()){
            rowWidthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    1, (int) canvas.getWidth(), (int) canvas.getWidth()));
            iterationsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    1, (int) canvas.getHeight(), (int) canvas.getHeight()));
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,
                0,
                gc.getCanvas().getWidth(),
                gc.getCanvas().getHeight());

        int width = rowWidthSpinner.getValue();
        int iterations = iterationsSpinner.getValue();
        boolean[] row = new boolean[width];
        Drawer drawer = new Drawer(canvas.getGraphicsContext2D(), width, iterations);
        row[width/2] = true;
        for (int i = 0; i < iterations; i++){
            ArrayList<Tuple<Double,Double>> pointsToDraw = new ArrayList<>();
            for (int j = 0; j < width; j++){
                if (row[j]) {
                    pointsToDraw.add(new Tuple<>((double) j, (double) i));
                }
            }
            if (pointsToDraw.size() > 0) {
                drawer.draw(pointsToDraw);
            }
            row = generateNewRow(row);
        }

    }
    private boolean[] generateNewRow(boolean[] oldRow){

        boolean[] newRow = new boolean[oldRow.length];
        for (int i = 0; i < oldRow.length; i++) {
            int rule = ruleSpinner.getValue();
            int currentExpectedSum = 7;
            int ruleToCheck = 128;
            boolean prev = i - 1 > 0 ? oldRow[i-1] : oldRow[oldRow.length-1];
            boolean current = oldRow[i];
            boolean next = i + 1 < oldRow.length ? oldRow[i+1] : oldRow[0];
            while (rule > 0) {
                if (rule >= ruleToCheck) {
                    int sum = 0;
                    if (prev) sum += 4;
                    if (current) sum += 2;
                    if (next) sum += 1;
                    if (sum == currentExpectedSum) {
                        newRow[i] = true;
                        break;
                    }
                    rule -= ruleToCheck;
                }
                ruleToCheck /= 2;
                currentExpectedSum--;
            }
        }
        return newRow;
    }
}
