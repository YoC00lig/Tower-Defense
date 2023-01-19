package agh.ics.oop.gui;

import agh.ics.oop.Castle;
import agh.ics.oop.GameMap;
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
import agh.ics.oop.Vector2d;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.layout.VBox;

public class App extends Application {
    private GridPane gridPane = new GridPane();
    private final Stage stage = new Stage();
    HBox mainbox;

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

        stage.setScene(new Scene(gridPane,1500,1000));
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
        stage.setScene(new Scene(gridPane,1500,1000));
        stage.show();
    }

    public void drawMap(int mapVariant) throws FileNotFoundException {
        GameMap map1 = new GameMap(new Vector2d(0,0), new Vector2d(69,39));
        int size = 20;
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        Label label = new Label("y/x");

        gridPane.add(label, 0, 0);
        gridPane.getRowConstraints().add(new RowConstraints(size));
        gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.setGridLinesVisible(true);

        for (int i = map1.lowerRight.x; i <= map1.upperLeft.x; i++){
            Label numberX = new Label("" + i );
            VBox box = new VBox(numberX);
            gridPane.add(box,  i - map1.lowerRight.x + 1, 0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(size));
            int finalI = i;
            box.setOnMouseClicked(event -> handle(gridPane, finalI - map1.lowerRight.x + 1, 0 ));
            GridPane.setHalignment(box, HPos.CENTER);
        }

        for (int i = map1.lowerRight.y; i <= map1.upperLeft.y; i++){
            Label numberY = new Label("" + i);
            VBox box = new VBox(numberY);
            gridPane.add(box, 0,map1.upperLeft.y - i + 1);
            gridPane.getRowConstraints().add(new RowConstraints(size));
            int finalI = i;
            box.setOnMouseClicked(event -> handle(gridPane, 0,map1.upperLeft.y - finalI + 1));
            GridPane.setHalignment(box, HPos.CENTER);
        }

        for (int row = map1.lowerRight.x; row <= map1.upperLeft.x; row++){
            for (int col = map1.lowerRight.y; col <= map1.upperLeft.y; col++){
                addPane(row - map1.lowerRight.x + 1, map1.upperLeft.y - col + 1,row,col);
            }
        }

        Image image = new Image(new FileInputStream("src/main/resources/castle.png"));
        ImageView view = new ImageView(image);
        Castle castle = map1.getCastle();
        view.setFitWidth(220);
        view.setFitHeight(220);

        gridPane.setMaxHeight(Region.USE_PREF_SIZE);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #26734d;");

        VBox box = new VBox(view);
        box.setAlignment(Pos.CENTER);
        gridPane.add(box,  castle.getLowerRight().x - map1.lowerRight.x + 1, map1.upperLeft.y - castle.getUpperLeft().y + 1,10,10);
        mainbox = new HBox(gridPane);
        mainbox.setAlignment(Pos.CENTER);
        mainbox.setStyle("-fx-background-color: #1f2e2e;");

        stage.setScene(new Scene(mainbox,1500,1000));
        stage.show();
    }

    public void handle(GridPane gridPane, int colIndex, int rowIndex) {
        Stage stageShop = new Stage();
        stageShop.setTitle("Shop");
        Shop shop = new Shop(stageShop, gridPane, colIndex, rowIndex);
        Scene shopping = new Scene(shop.getVB(), 400, 400);
        stageShop.setResizable(false);
        stageShop.setScene(shopping);
        stageShop.show();
    }
    private void addPane(int colIndex, int rowIndex, int row, int col) {
        Pane pane = new Pane();
        pane.setOnMouseClicked(e -> {
            System.out.printf("Mouse clicked cell [%d, %d]%n", colIndex, rowIndex);
            System.out.printf("Real vector: " + row + " " + col + "%n");
            handle(gridPane, colIndex, rowIndex);
        });
        gridPane.add(pane, colIndex, rowIndex);
    }
}
