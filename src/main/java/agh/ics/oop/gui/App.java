package agh.ics.oop.gui;

import agh.ics.oop.Castle;
import agh.ics.oop.GameMap;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import javafx.geometry.Insets;

public class App extends Application {
    private GridPane gridPane = new GridPane();
    private final Stage stage = new Stage();
    HBox mainbox;
    GameMap map1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)  {
        gridPane.getChildren().clear();
        gridPane = new GridPane();

        BorderPane border = new BorderPane();

        Label welcomeLabel = new Label("WELCOME IN TOWER DEFENCE ⚔️");
        welcomeLabel.setFont(new Font("Arial", 50));
        Button start = new Button("START GAME");
        start.setFont(new Font("Arial", 40));

        border.setTop(welcomeLabel);
        border.setBottom(start);

        Image image1, image2;
        try {
            image1 = new Image(new FileInputStream("src/main/resources/shield.png"));
            image2 = new Image(new FileInputStream("src/main/resources/defence.png"));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        ImageView view1 = new ImageView(image1);
        ImageView view2 = new ImageView(image2);
        view1.setFitHeight(300);
        view1.setFitWidth(300);
        view2.setFitHeight(300);
        view2.setFitWidth(300);

        HBox pictures = new HBox(view1, view2);
        pictures.setSpacing(30);
        pictures.setAlignment(Pos.CENTER);

        border.setCenter(pictures);
        border.setMinWidth(1500);
        border.setMinHeight(1000);
        border.setStyle("-fx-background-image: url('bckg.jpg');");

        start.setOnMouseClicked(event -> {
            try {
                mapChoice();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        styleButtonHover(start);
        start.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 2em; ");

        BorderPane.setAlignment(welcomeLabel,Pos.CENTER);
        BorderPane.setAlignment(start,Pos.CENTER);
        BorderPane.setAlignment(pictures,Pos.CENTER);
        BorderPane.setMargin(welcomeLabel, new Insets(200,0,0,0));
        BorderPane.setMargin(start, new Insets(0,0,100,0));

        gridPane.add(border,0,0);
        gridPane.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(gridPane,1500,1000));
        stage.setResizable(false);
        stage.setTitle("Tower defence");
        stage.show();

    }

    public void mapChoice() throws FileNotFoundException {
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        BorderPane border = new BorderPane();

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

        border.setCenter(vBox);
        border.setStyle("-fx-background-image: url('bckg.jpg');");

        BorderPane.setAlignment(chooseLabel,Pos.CENTER);
        BorderPane.setAlignment(hBox,Pos.CENTER);


        stage.setScene(new Scene(border,1500,1000));
        stage.show();
    }

    public void drawMap(int mapVariant) throws FileNotFoundException {
        map1 = new GameMap(new Vector2d(0,0), new Vector2d(69,39), 6, 300);
        int size = 20;
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        Label label = new Label("y/x");

        gridPane.add(label, 0, 0);
        gridPane.getRowConstraints().add(new RowConstraints(size));
        gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.setGridLinesVisible(false);

        for (int i = map1.lowerRight.x; i <= map1.upperLeft.x; i++){
            Label numberX = new Label("" + i );
            VBox box = new VBox(numberX);
            gridPane.add(box,  i - map1.lowerRight.x + 1, 0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(size));
            int finalI = i;
            box.setOnMouseClicked(event -> handle(gridPane, finalI - map1.lowerRight.x + 1, 0 , finalI, 0));
            GridPane.setHalignment(box, HPos.CENTER);
        }

        for (int i = map1.lowerRight.y; i <= map1.upperLeft.y; i++){
            Label numberY = new Label("" + i);
            VBox box = new VBox(numberY);
            gridPane.add(box, 0,map1.upperLeft.y - i + 1);
            gridPane.getRowConstraints().add(new RowConstraints(size));
            int finalI = i;
            box.setOnMouseClicked(event -> handle(gridPane, 0,map1.upperLeft.y - finalI + 1, 0, finalI));
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

        gridPane.setMaxHeight(800);
        gridPane.setMaxWidth(1400);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-image: url('grassbg.jpg');");

        VBox box = new VBox(view);
        box.setAlignment(Pos.CENTER);
        gridPane.add(box,  castle.getLowerRight().x - map1.lowerRight.x + 1, map1.upperLeft.y - castle.getUpperLeft().y + 1,10,10);
        mainbox = new HBox(gridPane);
        mainbox.setAlignment(Pos.CENTER);
        mainbox.setStyle("-fx-background-color: #1f2e2e;");

        stage.setResizable(false);
        stage.setScene(new Scene(mainbox,1500,1000));
        stage.show();
    }

    public void handle(GridPane gridPane, int colIndex, int rowIndex, int col, int row) { // otwiera się okno ze sklepem
        Stage stageShop = new Stage();
        stageShop.setTitle("Shop");
        Shop shop = new Shop(stageShop, gridPane, colIndex, rowIndex, col, row, map1);
        Scene shopping = new Scene(shop.getVB(), 400, 400);
        stageShop.setResizable(false);
        stageShop.setScene(shopping);
        stageShop.show();
    }
    private void addPane(int colIndex, int rowIndex, int row, int col) {
        Pane pane = new Pane();
        pane.setOnMouseClicked(e -> handle(gridPane, colIndex, rowIndex, col, row));
        gridPane.add(pane, colIndex, rowIndex);
    }

    public void styleButtonHover(Button B) {
        B.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> B.setEffect(new DropShadow()));
        B.addEventHandler(MouseEvent.MOUSE_EXITED, e -> B.setEffect(null));
    }
}
