package agh.ics.oop.gui;

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
import javafx.stage.Stage;

public class Shop {
    HBox box;
    Button btn1, btn2;
    Stage stage;
    GridPane grid;
    int row,col;

    public Shop(Stage stage, GridPane gird, int col, int row) {
        this.stage = stage;
        this.grid = gird;
        this.col = col;
        this.row = row;
        try {
            create();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void create() throws FileNotFoundException{
        BorderPane pane1 = new BorderPane();
        Label label1 = new Label("TOWER 1");
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

        BorderPane pane2 = new BorderPane();
        Label label2 = new Label("TOWER 2");
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

        styleButtons(grid, col, row);

        box = new HBox(pane1, pane2);
        box.setStyle("-fx-background-color: #1f2e2e;");
    }

    public HBox getVB() {
        return this.box;
    }

    public void placeTowerOnMap(String path, GridPane gridPane, int colIndex, int rowIndex) {
        Image image;

        try {
            image = new Image(new FileInputStream(path));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        ImageView view = new ImageView(image);
        view.setFitHeight(60);
        view.setFitWidth(60);
        gridPane.add(view,colIndex,rowIndex,3,3);
        stage.close();
    }
    public void styleButtons(GridPane gridPane, int colIndex, int rowIndex) { // todo
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> placeTowerOnMap("src/main/resources/tower.png", gridPane, colIndex, rowIndex));
        btn2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> placeTowerOnMap("src/main/resources/tower1.png", gridPane, colIndex, rowIndex));
        styleButtonHover(btn1);
        styleButtonHover(btn2);
    }

    public void styleButtonHover(Button B) {
        B.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> B.setEffect(new DropShadow()));
        B.addEventHandler(MouseEvent.MOUSE_EXITED, e -> B.setEffect(null));
    }
}
