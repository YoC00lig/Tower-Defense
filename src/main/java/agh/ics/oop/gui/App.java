package agh.ics.oop.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.geometry.HPos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class App extends Application {
    private GridPane gridPane = new GridPane();
    private final Stage stage = new Stage();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        gridPane.getChildren().clear();
        gridPane = new GridPane();

        Label welcomeLabel = new Label("Welcome in Tower Defence");
        welcomeLabel.setFont(new Font("Arial", 50));
        Button start = new Button("START GAME");
        start.setFont(new Font("Arial", 40));

        start.setOnMouseClicked(event -> {
            try {
                mapChoice();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        VBox vBox = new VBox(100, welcomeLabel, start);
        vBox.setAlignment(Pos.CENTER);
        gridPane.add(vBox,0,0);
        gridPane.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(gridPane,1500,800));
        stage.setTitle("Tower defence");
        stage.show();

    }

    public void mapChoice() throws FileNotFoundException {
        gridPane.getChildren().clear();
        gridPane = new GridPane();

        Label chooseLabel = new Label("Choose a map");
        chooseLabel.setFont(new Font("Arial", 50));

        Button map1 = new Button();
        Button map2 = new Button();
        Button map3 = new Button();

        map1.setOnMouseClicked(event -> {
            try {
                drawMap(1);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        map2.setOnMouseClicked(event -> {
            try {
                drawMap(2);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        map3.setOnMouseClicked(event -> {
            try {
                drawMap(3);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        HBox hBox = new HBox(40, map1, map2, map3);
        hBox.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(100, chooseLabel, hBox);
        vBox.setAlignment(Pos.CENTER);
        gridPane.add(vBox, 0,0);
        gridPane.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(gridPane,1500,800));
        stage.show();
    }

    public void drawMap(int mapVariant) throws FileNotFoundException {

    }
}
