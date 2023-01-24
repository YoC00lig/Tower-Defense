package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class GameOver {
    private GridPane gridPane = new GridPane();
    private GameMap map;
    private App application;

    private Stage stage;


    public GameOver(Stage stage, GameMap map, Stage primaryStage, App application) {
        this.map = map;
        this.stage = stage;
        this.stage.setAlwaysOnTop(true);
        this.stage.initModality(Modality.APPLICATION_MODAL);
        this.stage.initOwner(primaryStage);
        this.application = application;
        create();

    }

    private void create() {
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        Label gameOver = new Label("GAME OVER");
        HBox gameOverBox = new HBox(gameOver);
        gameOverBox.setAlignment(Pos.CENTER);
        gameOver.setFont(new Font("Arial", 40));
        Button playAgain = new Button("PLAY AGAIN");
        playAgain.setOnMouseClicked(event -> {
            try {
                stage.close();
                application.mapChoice();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        Button quit = new Button("QUIT GAME");
        quit.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });
        HBox buttonHBox = new HBox(40, playAgain, quit);
        buttonHBox.setAlignment(Pos.CENTER);

        Label castleHealth = new Label("CASTLE HEALTH:");
        castleHealth.setFont(new Font("Arial", 20));
        Label waveNumber = new Label("ENEMY WAVE NUMBER:");
        waveNumber.setFont(new Font("Arial", 20));
        Label enemies1 = new Label("NUMBER OF TYPE 1 ENEMIES DEFEATED:");
        enemies1.setFont(new Font("Arial", 20));
        Label enemies2 = new Label("NUMBER OF TYPE 2 ENEMIES DEFEATED:");
        enemies2.setFont(new Font("Arial", 20));
        Label enemies3 = new Label("NUMBER OF TYPE 3 ENEMIES DEFEATED:");
        enemies3.setFont(new Font("Arial", 20));
        Label money = new Label("MONEY:");
        money.setFont(new Font("Arial", 20));
        int health = Math.max(0, this.map.getCastle().getHealth());
        Label castleHealthValue = new Label("" + health);
        castleHealthValue.setFont(new Font("Arial", 20));
        Label waveNumberValue = new Label("" + (this.map.getWaveIndex() + 1));
        waveNumberValue.setFont(new Font("Arial", 20));
        Label enemies1Value = new Label("" + this.map.getDeadEnemies()[0]);
        enemies1Value.setFont(new Font("Arial", 20));
        Label enemies2Value = new Label("" + this.map.getDeadEnemies()[1]);
        enemies2Value.setFont(new Font("Arial", 20));
        Label enemies3Value = new Label("" + this.map.getDeadEnemies()[2]);
        enemies3Value.setFont(new Font("Arial", 20));
        Label moneyValue = new Label("" + this.map.money);
        moneyValue.setFont(new Font("Arial", 20));

        GridPane statsGrid = new GridPane();
        statsGrid.add(castleHealth, 0, 0);
        statsGrid.add(castleHealthValue, 1, 0);
        statsGrid.add(waveNumber, 0, 1);
        statsGrid.add(waveNumberValue, 1, 1);
        statsGrid.add(enemies1, 0, 2);
        statsGrid.add(enemies1Value, 1, 2);
        statsGrid.add(enemies2, 0, 3);
        statsGrid.add(enemies2Value, 1, 3);
        statsGrid.add(enemies3, 0, 4);
        statsGrid.add(enemies3Value, 1, 4);
        statsGrid.add(money, 0, 5);
        statsGrid.add(moneyValue, 1, 5);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setVgap(20);
        statsGrid.setHgap(50);


        gridPane.add(gameOverBox, 0, 0);
        gridPane.add(statsGrid, 0, 1);
        gridPane.add(buttonHBox, 0, 2);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(50);
        gridPane.setStyle("-fx-background-image: url('bckg.jpg');");
    }

    public GridPane getGP() {
        return this.gridPane;
    }
}
