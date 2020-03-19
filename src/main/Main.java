package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    //to run: mvn compile exec:java
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("1D automaton");
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.setMinWidth(550);
        primaryStage.setMinHeight(300);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
