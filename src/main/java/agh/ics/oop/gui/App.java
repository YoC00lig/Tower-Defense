package agh.ics.oop.gui;

import agh.ics.oop.*;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.util.*;
import javafx.application.Platform;

public class App extends Application {
    private GridPane gridPane = new GridPane();
    public final Stage stage = new Stage();
    HBox mainbox;
    GameMap map1;
    GameEngine engine;
    Scene scene;

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

        scene = new Scene(gridPane,1500,1000);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Tower defence");
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();

    }

    public void mapChoice() throws FileNotFoundException {
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        BorderPane border = new BorderPane();

        Label chooseLabel = new Label("Choose a map \uD83D\uDDFA");
        chooseLabel.setFont(new Font("Arial", 50));

        Button m1 = new Button();
        Button m2 = new Button();
        Button m3 = new Button();

        m1.setOnMouseClicked(event -> {
            this.map1 = new GameMap(new Vector2d(69,0),new Vector2d(0,39), 6, 300);
            engine = new GameEngine(this.map1, this);
            Thread thread = new Thread(engine);
            thread.start();
        });

        m2.setOnMouseClicked(event -> {
        });

        m3.setOnMouseClicked(event -> {
        });

        HBox hBox = new HBox(40, m1, m2, m3);
        hBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(100, chooseLabel, hBox);
        vBox.setAlignment(Pos.CENTER);

        border.setCenter(vBox);
        border.setStyle("-fx-background-image: url('bckg.jpg');");

        BorderPane.setAlignment(chooseLabel,Pos.CENTER);
        BorderPane.setAlignment(hBox,Pos.CENTER);

        scene.setRoot(border);
        stage.setScene(scene);
        stage.show();
    }

    public void drawMap() throws FileNotFoundException {
        int size = 20;
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        Label label = new Label("y/x");

        Vector2d low = new Vector2d(map1.upperLeft.x, map1.lowerRight.y);
        Vector2d high = new Vector2d(map1.lowerRight.x, map1.upperLeft.y);

        gridPane.add(label, 0, 0);
        gridPane.getRowConstraints().add(new RowConstraints(size));
        gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.setGridLinesVisible(false);

        for (int i = low.x; i <= high.x; i++){
            Label numberX = new Label("" + i );
            VBox box = new VBox(numberX);
            gridPane.add(box,  i - low.x + 1, 0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(size));
            GridPane.setHalignment(box, HPos.CENTER);
            int finalI = i;
            box.setOnMouseClicked(event -> handle(gridPane, finalI - low.x + 1, 0 , finalI, 0));
        }

        for (int i = low.y; i <= high.y; i++){
            Label numberY = new Label("" + i);
            VBox box = new VBox(numberY);
            gridPane.add(box, 0,high.y - i + 1);
            gridPane.getRowConstraints().add(new RowConstraints(size));
            GridPane.setHalignment(box, HPos.CENTER);
            int finalI = i;
            box.setOnMouseClicked(event -> handle(gridPane, 0,high.y - finalI + 1, 0, finalI));
        }

        for (int row = low.x; row <= high.x; row++){
            for (int col = low.y; col <= high.y; col++){
                addPane(row - low.x + 1, high.y - col + 1, row, col);
            }
        }
        for (LinkedList<Enemy> list: map1.enemies.values()){
            for (Enemy element: list){
                GuiElementBox guiElement = new GuiElementBox(element, 20);
                VBox elem = guiElement.getvBox();
                Vector2d pos = element.getPosition();
                gridPane.add(elem,  pos.x - low.x + 1, high.y - pos.y + 1);
                GridPane.setHalignment(elem, HPos.CENTER);
            }
        }

        for (Tower tower: map1.listOfTowers){
            String path = tower.getPath();
            Vector2d pos = tower.getUpperLeft();
            int colidx = pos.x - low.x + 1;
            int rowidx = high.y - pos.y + 1;
            Image image;
            try {
                image = new Image(new FileInputStream(path));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }

            ImageView view = new ImageView(image);
            view.setFitHeight(60);
            view.setFitWidth(60);
            gridPane.add(view,colidx,rowidx,3,3);
            GridPane.setHalignment(view, HPos.CENTER);
        }

        Image image = new Image(new FileInputStream("src/main/resources/castle.png"));
        ImageView view = new ImageView(image);
        Castle castle = map1.getCastle();
        view.setFitWidth(220);
        view.setFitHeight(220);

        gridPane.setMaxHeight(800);
        gridPane.setMaxWidth(1400);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #26734d;");

        VBox box = new VBox(view);
        box.setAlignment(Pos.CENTER);
        gridPane.add(box,  castle.getUpperLeft().x, castle.getUpperLeft().y,10,10);
        mainbox = new HBox(gridPane);
        mainbox.setAlignment(Pos.CENTER);
        mainbox.setStyle("-fx-background-color: #1f2e2e;");

        scene.setRoot(mainbox);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void handle(GridPane gridPane, int colIndex, int rowIndex, int col, int row) { // otwiera się okno ze sklepem
        Stage stageShop = new Stage();
        stageShop.setTitle("Shop");
        Shop shop = new Shop(stageShop, gridPane, colIndex, rowIndex, col, row, map1,stage);
        Scene shopping = new Scene(shop.getVB(), 400, 400);
        stageShop.setResizable(false);
        stageShop.setScene(shopping);
        stageShop.show();
    }
    private void addPane(int colIndex, int rowIndex, int row, int col) {
        Pane pane = new Pane();
        pane.setOnMouseClicked(e -> {
            handle(gridPane, colIndex, rowIndex, col, row);
        });
        gridPane.add(pane, colIndex, rowIndex);
    }

    public void styleButtonHover(Button B) {
        B.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> B.setEffect(new DropShadow()));
        B.addEventHandler(MouseEvent.MOUSE_EXITED, e -> B.setEffect(null));
    }

    public void draw() throws FileNotFoundException {
        Platform.runLater(() -> {
            try {
                drawMap();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
                throw new RuntimeException(e);
            }
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.exit(0);
        }
    }
}