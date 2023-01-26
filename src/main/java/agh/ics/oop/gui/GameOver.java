package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GameOver {
    Scene scene;
    private GridPane gridPane = new GridPane();
    private final GameMap map;
    private final App application;
    private final String string;
    private final Stage stage;
    Button quit;
    Button playAgain;
    String[] scores = new String[3];
    String[] names = new String[3];
    File file = new File("src/main/resources/highscores.txt");
    VBox places;
    int score;


    public GameOver(Stage stage, GameMap map, Stage primaryStage, App application, String string) {
        this.map = map;
        this.score = map.getScore();
        this.stage = stage;
        this.stage.setAlwaysOnTop(true);
        this.stage.initModality(Modality.APPLICATION_MODAL);
        this.stage.initOwner(primaryStage);
        this.application = application;
        this.string = string;
        stage.setOnCloseRequest(event -> Platform.exit());
        getBoard();
        create();
    }

    private void create() {
        gridPane.getChildren().clear();
        gridPane = new GridPane();

        Label gameOver = new Label(this.string);
        HBox gameOverBox = new HBox(gameOver);
        gameOverBox.setAlignment(Pos.CENTER);
        gameOver.setFont(new Font("Arial", 40));

        playAgain = new Button("PLAY AGAIN");
        playAgain.setOnMouseClicked(event -> {
            try {
                stage.close();
                application.mapChoice();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        styleButtonHover(playAgain);
        playAgain.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 2em; ");
        playAgain.setFont(new Font("Arial", 20));

        quit = new Button("QUIT GAME");
        quit.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });
        styleButtonHover(quit);
        quit.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 2em; ");
        quit.setFont(new Font("Arial", 20));

        Button scores = new Button("HIGHSCORES");
        scores.setOnMouseClicked(event -> this.createScoresBoard());
        styleButtonHover(scores);
        scores.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 2em; ");
        scores.setFont(new Font("Arial", 20));

        HBox buttonHBox = new HBox(40, playAgain, quit, scores);
        buttonHBox.setAlignment(Pos.CENTER);

        Label castleHealth = new Label("CASTLE HEALTH:");
        castleHealth.setFont(new Font("Arial", 20));
        Label waveNumber = new Label("ENEMY WAVE:");
        waveNumber.setFont(new Font("Arial", 20));
        Label enemies1 = new Label("NUMBER OF TYPE 1 ENEMIES DEFEATED:");
        enemies1.setFont(new Font("Arial", 20));
        Label enemies2 = new Label("NUMBER OF TYPE 2 ENEMIES DEFEATED:");
        enemies2.setFont(new Font("Arial", 20));
        Label enemies3 = new Label("NUMBER OF TYPE 3 ENEMIES DEFEATED:");
        enemies3.setFont(new Font("Arial", 20));
        Label money = new Label("MONEY LEFT:");
        money.setFont(new Font("Arial", 20));
        int health = Math.max(0, this.map.getCastle().getHealth());
        Label castleHealthValue = new Label("" + health);
        castleHealthValue.setFont(new Font("Arial", 20));
        Label waveNumberValue = new Label("" + this.map.getWaveIndex());
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

    public void setScene(Scene scene){
        this.scene = scene;
    }

    private void createScoresBoard(){
        gridPane.getChildren().clear();
        gridPane = new GridPane();

        Label label = new Label("HIGHSCORES");
        HBox scoresBox = new HBox(label);
        scoresBox.setAlignment(Pos.CENTER);
        label.setFont(new Font("Arial", 40));

        Button back = new Button("RETURN");
        back.setOnMouseClicked(event -> {
            this.create();
            scene.setRoot(gridPane);
        });

        styleButtonHover(back);
        back.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 2em; ");
        back.setFont(new Font("Arial", 20));

        Label yourScore = new Label("Your score: " + this.score);
        yourScore.setFont(new Font("Arial", 30));

        VBox board = new VBox(yourScore);
        board.setAlignment(Pos.CENTER);


        HBox buttonHBox = new HBox(40, playAgain, quit, back);
        buttonHBox.setAlignment(Pos.CENTER);

        gridPane.add(scoresBox, 0, 0);
        gridPane.add(board,0,1);
        gridPane.add(places,0,2);
        gridPane.add(buttonHBox,0,3);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(50);
        gridPane.setStyle("-fx-background-image: url('bckg.jpg');");
        scene.setRoot(gridPane);
    }

    public void styleButtonHover(Button B) {
        B.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> B.setEffect(new DropShadow()));
        B.addEventHandler(MouseEvent.MOUSE_EXITED, e -> B.setEffect(null));
    }

    // get highscore
    public void writeLineToFile(){
        try {
            FileWriter fileReader = new FileWriter(file); // Strumień, który łączy się z plikiem tekstowym
            BufferedWriter bufferedWriter = new BufferedWriter(fileReader); // Połącz FileWriter z BufferedWriter
            for (int i = 0; i < 3; i++) {
                bufferedWriter.write(names[i] + "\n");
                bufferedWriter.write(scores[i] + "\n");
            }
            bufferedWriter.close (); // Zamknij strumień
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readLineFromFile() {
        int idx1 = 0;
        int idx2 = 0;

        try {
            FileReader fileReader = new FileReader(file); // Java FileReader to strumień, który łączy się z plikiem tekstowym, umożliwiając czytanie z pliku w języku Java
            BufferedReader bufferedReader = new BufferedReader(fileReader); // Połącz FileReader z BufferedReader
            for (int i = 0; i < 6; i++){
                String line = bufferedReader.readLine();
                if ((i%2) == 0){ // mamy name
                    names[idx1] = line;
                    idx1++;
                }
                else{
                    scores[idx2] = line;
                    idx2++;
                }
             }
            bufferedReader.close(); // Zamknij strumień
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFile() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter("src/main/resources/highscores.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pw.close();

        writeLineToFile();
    }

    public void getCurrentAndCompare(){
        readLineFromFile();
        HashMap<String, String> maps = new HashMap<>();
        ArrayList<Integer> scoresTab = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            maps.put(scores[i], names[i]);
            scoresTab.add(Integer.parseInt(scores[i]));
        }

        maps.put("" + this.score, application.nick);
        scoresTab.add(this.score);

        Collections.sort(scoresTab);
        Collections.reverse(scoresTab);

        for (int i = 0; i <3; i++){
            scores[i] = "" + scoresTab.get(i);
            names[i] = maps.get(scores[i]);
        }
        updateFile();
    }

    public void getBoard(){
        getCurrentAndCompare();

        Label first = new Label("1. " + names[0] + " : " + scores[0] + " points");
        Label second = new Label("2. " + names[1] + " : " + scores[1] + " points");
        Label third = new Label("3. " + names[2] + " : " + scores[2] + " points");

        first.setFont(new Font("Arial", 20));
        second.setFont(new Font("Arial", 20));
        third.setFont(new Font("Arial", 20));

        places = new VBox(20,first,second,third);
        places.setAlignment(Pos.CENTER);
    }

}
