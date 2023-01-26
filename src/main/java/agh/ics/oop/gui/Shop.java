package agh.ics.oop.gui;

import agh.ics.oop.GameMap;
import agh.ics.oop.Tower;
import agh.ics.oop.Vector2d;
import agh.ics.oop.Wall;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.effect.DropShadow;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.*;

public class Shop {
    HBox box = new HBox();
    Button btn1, btn2, btn3;
    Stage stage;
    GridPane grid;
    int rowidx, colidx; // indeksy na gridPane
    int col, row; // prawdziwe indeksy
    GameMap map;
    BorderPane pane1, pane2, pane3;

    public Shop(Stage stage, GridPane gird, int colidx, int rowidx, int col, int row, GameMap map, Stage primaryStage) {
        this.stage = stage;
        this.grid = gird;
        this.colidx = colidx;
        this.rowidx = rowidx;
        this.col = col;
        this.row = row;
        this.map = map;
        this.stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {if (!isNowFocused) stage.hide();});
        try {
            if (!map.castleAt(new Vector2d(row,col)) && map.towerAt(new Vector2d(row, col)) == null) towerShop();
            else if (map.extendedMode && map.castleAt(new Vector2d(row,col))) cureCastle();
            else if (map.extendedMode && map.towerAt(new Vector2d(row, col)) != null) rangeShop(map.towerAt(new Vector2d(row, col)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void towerShop() throws FileNotFoundException{
        pane1 = new BorderPane();
        Label label1 = new Label("$300");
        label1.setStyle("-fx-background-color: #ffd11a;");
        pane1.setTop(label1);
        Image image1 = new Image(new FileInputStream("src/main/resources/tower.png"));
        ImageView view1 = new ImageView(image1);
        view1.setFitWidth(200);
        view1.setFitHeight(150);
        btn1 = new Button("BUY");
        pane1.setCenter(view1);
        pane1.setBottom(btn1);
        BorderPane.setAlignment(label1, Pos.CENTER);
        BorderPane.setAlignment(view1, Pos.CENTER);
        BorderPane.setAlignment(btn1, Pos.CENTER);
        BorderPane.setMargin(btn1, new Insets(0,0,40,0));
        BorderPane.setMargin(label1, new Insets(40,0,0,0));

        pane2 = new BorderPane();
        Label label2 = new Label("$700");
        label2.setStyle("-fx-background-color: #ffd11a;");
        pane2.setTop(label2);
        Image image2 = new Image(new FileInputStream("src/main/resources/tower1.png"));
        ImageView view2 = new ImageView(image2);
        view2.setFitWidth(200);
        view2.setFitHeight(150);
        btn2 = new Button("BUY");
        pane2.setCenter(view2);
        pane2.setBottom(btn2);
        BorderPane.setAlignment(label2, Pos.CENTER);
        BorderPane.setAlignment(view2, Pos.CENTER);
        BorderPane.setAlignment(btn2, Pos.CENTER);
        BorderPane.setMargin(btn2, new Insets(0,0,40,0));
        BorderPane.setMargin(label2, new Insets(40,0,0,0));

        pane3 = new BorderPane();
        Label label3 = new Label("$50");
        label3.setStyle("-fx-background-color: #ffd11a;");
        pane3.setTop(label3);
        Image image3 = new Image(new FileInputStream("src/main/resources/wall.png"));
        ImageView view3 = new ImageView(image3);
        view3.setFitWidth(150);
        view3.setFitHeight(150);
        btn3 = new Button("BUY");
        pane3.setCenter(view3);
        pane3.setBottom(btn3);
        BorderPane.setAlignment(label3, Pos.CENTER);
        BorderPane.setAlignment(view3, Pos.CENTER);
        BorderPane.setAlignment(btn3, Pos.CENTER);
        BorderPane.setMargin(btn3, new Insets(0,0,40,0));
        BorderPane.setMargin(label3, new Insets(40,0,0,0));

        styleButtons();

        box = new HBox(pane1, pane2, pane3);
        box.setMinHeight(300);
        box.setMinWidth(600);
        box.setStyle("-fx-background-color: #1f2e2e;");
    }

    public HBox getHB() {
        return this.box;
    }

    public void placeTowerOnMap(String path) { // dodaje obrazek wieży na mapę
        Image image;
        try {
            image = new Image(new FileInputStream(path));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        ImageView view = new ImageView(image);
        view.setFitHeight(60);
        view.setFitWidth(60);
        this.grid.add(view,this.colidx,this.rowidx,3,3);
        stage.close();
    }

    public void cureCastle() throws FileNotFoundException{
        pane1 = new BorderPane();
        Label label1 = new Label("$1000");
        label1.setStyle("-fx-background-color: #ffd11a;");
        pane1.setTop(label1);
        Image image1 = new Image(new FileInputStream("src/main/resources/heart.png"));
        ImageView view1 = new ImageView(image1);
        view1.setFitWidth(200);
        view1.setFitHeight(150);
        btn3 = new Button("BUY");
        pane1.setCenter(view1);
        pane1.setBottom(btn3);
        BorderPane.setAlignment(label1, Pos.CENTER);
        BorderPane.setAlignment(view1, Pos.CENTER);
        BorderPane.setAlignment(btn3, Pos.CENTER);
        BorderPane.setMargin(btn3, new Insets(0,0,40,0));
        BorderPane.setMargin(label1, new Insets(40,0,0,0));
        box = new HBox(pane1);
        box.setMinHeight(300);
        box.setMinWidth(200);
        box.setStyle("-fx-background-color: #1f2e2e;");
        box.setAlignment(Pos.CENTER);
        styleButtonHover(btn3);
        btn3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (this.map.money - 1000 >= 0) {
                this.map.getCastle().heal();
                map.money -= 1000;
                stage.close();
            }
            else {
                stage.close();
            }
        });
    }

    public void rangeShop(Tower tower) throws FileNotFoundException{
        pane1 = new BorderPane();
        Label label1 = new Label("$900");
        label1.setStyle("-fx-background-color: #ffd11a;");
        pane1.setTop(label1);
        Image image1 = new Image(new FileInputStream("src/main/resources/boom.png"));
        ImageView view1 = new ImageView(image1);
        view1.setFitWidth(200);
        view1.setFitHeight(150);
        btn1 = new Button("BUY");
        pane1.setCenter(view1);
        pane1.setBottom(btn1);
        BorderPane.setAlignment(label1, Pos.CENTER);
        BorderPane.setAlignment(view1, Pos.CENTER);
        BorderPane.setAlignment(btn1, Pos.CENTER);
        BorderPane.setMargin(btn1, new Insets(0,0,40,0));
        BorderPane.setMargin(label1, new Insets(40,0,0,0));
        styleButtonHover(btn1);
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (this.map.money - 900 >= 0) {
                map.money -= 900;
                tower.addRange();
                stage.close();
            }
            else {
                stage.close();
            }
        });

        pane2 = new BorderPane();
        Label label2 = new Label("$" + tower.getCurrentPriceToSell());
        label2.setStyle("-fx-background-color: #ffd11a;");
        pane2.setTop(label2);
        Image image2 = new Image(new FileInputStream("src/main/resources/money.png"));
        ImageView view2 = new ImageView(image2);
        view2.setFitWidth(200);
        view2.setFitHeight(150);
        btn2 = new Button("SELL");
        pane2.setCenter(view2);
        pane2.setBottom(btn2);
        BorderPane.setAlignment(label2, Pos.CENTER);
        BorderPane.setAlignment(view2, Pos.CENTER);
        BorderPane.setAlignment(btn2, Pos.CENTER);
        BorderPane.setMargin(btn2, new Insets(0,0,40,0));
        BorderPane.setMargin(label2, new Insets(40,0,0,0));

        styleButtonHover(btn2);
        btn2.setStyle("-fx-background-color: #de8c81;" + "-fx-background-radius: 1em; ");
        btn2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            map.sellTower(tower);
            stage.close();
        });

        box = new HBox(pane1, pane2);
        box.setMinHeight(300);
        box.setMinWidth(400);
        box.setStyle("-fx-background-color: #1f2e2e;");
        box.setAlignment(Pos.CENTER);
    }

    public void styleButtons() { // Event listenery dla guzików BUY, żeby dodawać wieżę na mapę i do tablicy towers w GameMap
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Tower tower = map.getNewTower(new Vector2d(row, col), 1);
            if (map.checkIfCanPlaceTower(tower) && map.money - tower.getPrice() >= 0){
                placeTowerOnMap("src/main/resources/tower.png");
                map.money -= tower.getPrice();
                map.addNewTower(tower);
                stage.close();
            } else if (map.money - tower.getPrice() < 0) {
                Label label = new Label("NOT ENOUGH MONEY");
                label.setStyle("-fx-background-color: #ff4d4d;");
                BorderPane.setAlignment(label, Pos.CENTER);
                BorderPane.setMargin(label, new Insets(40,0,0,0));
                pane1.setTop(label);
            }else if (!map.checkIfCanPlaceTower(tower)) {
                stage.close();
            }
        });
        btn2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Tower tower = map.getNewTower(new Vector2d(row, col), 2);
            if (map.checkIfCanPlaceTower(tower) && map.money - tower.getPrice() >= 0){
                placeTowerOnMap("src/main/resources/tower1.png");
                map.money -= tower.getPrice();
                map.addNewTower(tower);
                stage.close();
            } else if (map.money - tower.getPrice() < 0) {
                Label label = new Label("NOT ENOUGH MONEY");
                label.setStyle("-fx-background-color: #ff4d4d;");
                BorderPane.setAlignment(label, Pos.CENTER);
                BorderPane.setMargin(label, new Insets(40,0,0,0));
                pane2.setTop(label);
            } else if (!map.checkIfCanPlaceTower(tower)) {
                stage.close();
            }
        });

        btn3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (map.startWall == null){
                map.setWallStart(new Vector2d(this.row, this.col));
                stage.close();
            }
        });

        styleButtonHover(btn1);
        styleButtonHover(btn2);
        styleButtonHover(btn3);
    }

    public void styleButtonHover(Button B) {
        B.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> B.setEffect(new DropShadow()));
        B.addEventHandler(MouseEvent.MOUSE_EXITED, e -> B.setEffect(null));
        B.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 1em; ");
        B.setFont(new Font("Arial", 14));
    }

}