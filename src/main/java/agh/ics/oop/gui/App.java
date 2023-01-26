package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
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
    BorderPane mainbox = new BorderPane();
    GameMap map1;
    GameEngine engine;
    Scene scene;
    boolean floodMode = false;
    VBox box;
    Thread thread;
    Button play = drawButton();
    public boolean lose = false;
    public boolean win = false;
    String nick;
    TextField text;
    Label label;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)  {
        gridPane.getChildren().clear();
        gridPane = new GridPane();

        BorderPane border = new BorderPane();

        Label welcomeLabel = new Label("WELCOME IN TOWER DEFENCE");
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

        play = drawButton();
        lose = false;
        win = false;

        BorderPane border = new BorderPane();
        Label label = new Label();
        label.setMinHeight(100);
        label.setFont(new Font("Arial", 20));
        BorderPane.setMargin(label, new Insets(0,0,50,0));
        border.setBottom(label);

        Label chooseLabel = new Label("Choose a map");
        chooseLabel.setFont(new Font("Arial", 50));
        Label nickLabel = new Label("Enter your nickname");
        nickLabel.setFont(new Font("Arial", 50));

        Button m1 = new Button("BASIC");
        Button m2 = new Button("FLOOD");
        Button m3 = new Button("EXTENDED");
        m1.setFont(new Font("Arial", 20));
        m2.setFont(new Font("Arial", 20));
        m3.setFont(new Font("Arial", 20));

        styleButtonHover(m1);
        m1.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 1.5em; ");
        styleButtonHover(m2);
        m2.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 1.5em; ");
        styleButtonHover(m3);
        m3.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 1.5em; ");

        m1.setOnMouseClicked(event -> {
            nick = text.getText();
            if (!(nick.length() == 0)) {
                this.map1 = new GameMap(new Vector2d(69, 0), new Vector2d(0, 39), 1000, 1, false, false);
                engine = new GameEngine(this.map1, this);
                thread = new Thread(engine);
                try {
                    drawMap();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                VBox info = Information(4);
                border.setBottom(info);
                BorderPane.setAlignment(info, Pos.CENTER);
            }
        });

        m2.setOnMouseClicked(event -> {
            nick = text.getText();
            if (!(nick.length() == 0)) {
                this.map1 = new GameMap(new Vector2d(69, 0), new Vector2d(0, 39), 1500, 2, true, false);
                floodMode = true;
                engine = new GameEngine(this.map1, this);
                thread = new Thread(engine);
                try {
                    drawMap();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                VBox info = Information(4);
                border.setBottom(info);
                BorderPane.setAlignment(info, Pos.CENTER);
            }
        });

        m3.setOnMouseClicked(event -> {
            nick = text.getText();
            if (!(nick.length() == 0)) {
                this.map1 = new GameMap(new Vector2d(69, 0), new Vector2d(0, 39), 1500, 2, false, true);
                engine = new GameEngine(this.map1, this);
                thread = new Thread(engine);
                try {
                    drawMap();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                VBox info = Information(4);
                border.setBottom(info);
                BorderPane.setAlignment(info, Pos.CENTER);
            }
        });

        text = new TextField();
        text.setPromptText("Your nickname");
        text.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 2em; ");
        text.setFont(new Font("Arial", 20));
        text.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(40, m1, m2, m3);
        hBox.setAlignment(Pos.CENTER);
        text.setMaxWidth(300);
        text.setMinHeight(50);


        m1.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            VBox info = Information(1);
            border.setBottom(info);
            BorderPane.setAlignment(info, Pos.CENTER);
        });
        m2.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            VBox info = Information(2);
            border.setBottom(info);
            BorderPane.setAlignment(info, Pos.CENTER);
        });
        m3.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            VBox info = Information(3);
            border.setBottom(info);
            BorderPane.setAlignment(info, Pos.CENTER);
        });

        m1.addEventHandler(MouseEvent.MOUSE_EXITED, event -> border.setBottom(label));
        m2.addEventHandler(MouseEvent.MOUSE_EXITED, event -> border.setBottom(label));
        m3.addEventHandler(MouseEvent.MOUSE_EXITED, event -> border.setBottom(label));

        VBox vB = new VBox(100, nickLabel, text, chooseLabel, hBox);
        vB.setAlignment(Pos.CENTER);

        border.setCenter(vB);
        border.setStyle("-fx-background-image: url('bckg.jpg');");

        BorderPane.setAlignment(chooseLabel,Pos.CENTER);
        BorderPane.setAlignment(hBox,Pos.CENTER);

        scene.setRoot(border);
        stage.setScene(scene);
        stage.show();
    }

    public VBox Information(int option){
        if (option == 1) label = new Label("Basic version, two types of towers to buy");
        else if (option == 2) label = new Label("Inaccessible to enemies places on the map that have been flooded");
        else if (option == 3) label = new Label("Premium version, you can buy health for castle, improve towers range and sell towers");
        else label = new Label("You must enter your nickname first");
        label.setFont(new Font("Arial", 20));
        VBox box = new VBox(label);
        if ( option != 4 ) box.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 3em; ");
        else box.setStyle("-fx-background-color: #f28061;" + "-fx-background-radius: 3em; " + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 30, 0, 0, 0)");
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(800);
        box.setMinHeight(100);
        BorderPane.setMargin(box, new Insets(0,0,50,0));
        return box;
    }

    public Button drawButton() {
        Button startGame = new Button("PLAY");
        styleButtonHover(startGame);
        startGame.setStyle("-fx-background-color: #ffdd99;" + "-fx-background-radius: 2em; ");
        startGame.setFont(new Font("Arial", 20));
        startGame.setAlignment(Pos.CENTER);
        styleButtonHover(startGame);
        BorderPane.setMargin(startGame, new Insets(20,0,10,0));

        startGame.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            thread.start();
            startGame.setDisable(true);
            startGame.setText("Running");
        });
        return startGame;
    }

    public void drawMap() throws FileNotFoundException {
        int size = 20;
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        Label label = new Label();

        Vector2d low = new Vector2d(map1.upperLeft.x, map1.lowerRight.y);
        Vector2d high = new Vector2d(map1.lowerRight.x, map1.upperLeft.y);

        gridPane.add(label, 0, 0);
        gridPane.getRowConstraints().add(new RowConstraints(size));
        gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.setGridLinesVisible(true);

        // dodawanie wrogów, wież etc do gridpane
        for (int i = low.x; i <= high.x; i++){
            Label numberX = new Label();
            VBox box = new VBox(numberX);
            gridPane.add(box,  i - low.x + 1, 0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(size));
            GridPane.setHalignment(box, HPos.CENTER);
            int finalI = i;
            box.setOnMouseClicked(event -> handle(gridPane, finalI - low.x + 1, 0 , finalI, 0));
        }

        for (int i = low.y; i <= high.y; i++){
            Label numberY = new Label();
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
                GuiElementBox guiElement = new GuiElementBox(element);
                VBox elem = guiElement.getvBox();
                Vector2d pos = element.getPosition();
                gridPane.add(elem,  pos.x - low.x + 1, high.y - pos.y + 1);
                GridPane.setHalignment(elem, HPos.CENTER);
            }
        }

        for (Wall element : map1.walls){
            GuiElementBox guiElement = new GuiElementBox(element);
            VBox elem = guiElement.getvBox();
            Vector2d pos = element.getPosition();
            gridPane.add(elem,  pos.x - low.x + 1, high.y - pos.y + 1);
            GridPane.setHalignment(elem, HPos.CENTER);
        }

        for (Tower tower: map1.listOfTowers){
            String path = tower.getPath(tower);
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
            view.setFitHeight(50);
            view.setFitWidth(60);

            double progress = tower.getHealth() / tower.maxHealth();
            ProgressBar HB = new ProgressBar(Math.min(1.00, progress));
            if (progress > 0.8) HB.setStyle("-fx-accent: green;");
            else if (progress >= 0.6 && progress < 0.8) HB.setStyle("-fx-accent: #ccff33;");
            else if (progress >= 0.4 && progress < 0.6) HB.setStyle("-fx-accent: #ffff1a;");
            else if (progress >= 0.2 && progress < 0.4) HB.setStyle("-fx-accent: #ff9900;");
            else HB.setStyle("-fx-accent: red;");
            HB.setPrefHeight(10);
            HB.setMinHeight(10);
            HB.setMinWidth(30);

            VBox box = new VBox(HB, view);
            gridPane.add(box,colidx,rowidx,3,3);
            box.setOnMouseClicked(event -> handle(gridPane, tower.getUpperLeft().x -low.x + 1, high.y - tower.getUpperLeft().y + 1,tower.getUpperLeft().y,tower.getUpperLeft().x));
            GridPane.setHalignment(view, HPos.CENTER);
        }
        // dodawanie wody do gridpane w wylosowanych miejscach jeśli jest floodMode
        if (floodMode){
            for (Vector2d pos : map1.cells){
                VBox elem = new VBox();
                elem.setMinWidth(20);
                elem.setMinHeight(20);
                elem.setStyle("-fx-background-color: #b3e6ff;");
                gridPane.add(elem,  pos.x - low.x + 1, high.y - pos.y + 1);
                GridPane.setHalignment(elem, HPos.CENTER);
            }
        }
        // dodawanie zamku do gridpane
        Castle castle = map1.getCastle();
        Image image = new Image(new FileInputStream("src/main/resources/castle.png"));
        ImageView view = new ImageView(image);
        view.setFitWidth(200);
        view.setFitHeight(180);

        double progress = castle.getHealth() / castle.maxHealth();
        ProgressBar castleHB = new ProgressBar(Math.min(1.00, progress));
        if (progress > 0.8) castleHB.setStyle("-fx-accent: green;");
        else if (progress >= 0.6 && progress < 0.8) castleHB.setStyle("-fx-accent: #ccff33;");
        else if (progress >= 0.4 && progress < 0.6) castleHB.setStyle("-fx-accent: #ffff1a;");
        else if (progress >= 0.2 && progress < 0.4) castleHB.setStyle("-fx-accent: #ff9900;");
        else castleHB.setStyle("-fx-accent: red;");
        castleHB.setPrefHeight(20);
        castleHB.setMinHeight(20);
        castleHB.setMinWidth(180);

        gridPane.setMaxHeight(800);
        gridPane.setMaxWidth(1400);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #26734d;");

        // dodawanie paska z PLAY i ilością $
        Label currMoney;
        if (this.thread.isAlive()) currMoney = new Label("Your money: " + map1.money);
        else currMoney = new Label("Place your first towers and click PLAY, at start you have " + map1.money + "$");
        currMoney.setFont(new Font("Arial", 15));
        currMoney.setStyle("-fx-text-fill: #ffffff;");
        HBox topBox = new HBox(currMoney,play);
        BorderPane.setAlignment(topBox, Pos.CENTER);
        topBox.setAlignment(Pos.CENTER);
        topBox.setSpacing(30);
        mainbox.setTop(topBox);
        BorderPane.setMargin(topBox, new Insets(20,0,0,0));
        BorderPane.setAlignment(topBox, Pos.CENTER);

        // dodawanie całego gridpane
        box = new VBox(castleHB, view);
        box.setAlignment(Pos.CENTER);
        box.setOnMouseClicked(event -> handle(gridPane, castle.getUpperLeft().x -low.x + 1, high.y - castle.getUpperLeft().y + 1,castle.getUpperLeft().y,castle.getUpperLeft().x));
        gridPane.add(box,  castle.getUpperLeft().x -low.x + 1, high.y - castle.getUpperLeft().y + 1,10,10);
        mainbox.setCenter(gridPane);
        BorderPane.setAlignment(gridPane, Pos.CENTER);
        mainbox.setStyle("-fx-background-color: #1f2e2e;");

        scene.setRoot(mainbox);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    public void handle(GridPane gridPane, int colIndex, int rowIndex, int col, int row) { // otwiera się okno ze sklepem
        Stage stageShop = new Stage();
        if (map1.startWall == null) {
            Shop shop = new Shop(stageShop, gridPane, colIndex, rowIndex, col, row, map1, stage);
            stageShop.setTitle("Shop");
            Scene shopping = new Scene(shop.getHB(), 600, 400);
            stageShop.setResizable(false);
            stageShop.setScene(shopping);
            stageShop.show();
        }
        else {
            placeWall(row, col);
            map1.setWallStart(null);
        }
    }

    public void drawGameOver(String string){
        Stage gameOverStage = new Stage();
        gameOverStage.setTitle("Game Over");
        GameOver gameOver = new GameOver(gameOverStage, map1, stage,this, string);
        Scene stats = new Scene(gameOver.getGP(), 700, 500);
        gameOver.setScene(stats);
        gameOverStage.setResizable(false);
        gameOverStage.setScene(stats);
        gameOverStage.show();
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

    public void placeWall(int row, int col) {
        Vector2d low = new Vector2d(map1.upperLeft.x, map1.lowerRight.y);
        Vector2d high = new Vector2d(map1.lowerRight.x, map1.upperLeft.y);
        Vector2d start = map1.startWall;
        Vector2d end = new Vector2d(row, col);
        ArrayList<Vector2d> walls2 = BFS(start, end);
        Wall wall = new Wall(100, new Vector2d(0,0));
        for (Vector2d v: walls2){
            if (map1.money - wall.getPrice() >= 0){
                map1.addWall(v);
                map1.money -= wall.getPrice();
            }
        }
        for (Wall element: map1.newWalls){
            GuiElementBox guiElement = new GuiElementBox(element);
            VBox elem = guiElement.getvBox();
            Vector2d pos = element.getPosition();
            this.gridPane.add(elem,  pos.x - low.x + 1, high.y - pos.y + 1);
            GridPane.setHalignment(elem, HPos.CENTER);
        }
    }

    public static boolean isValid(Vector2d v, GameMap map) {
        return !map.buildingAt(v) && v.x >= map.upperLeft.x && v.x <= map.lowerRight.x && v.y >= map.lowerRight.y && v.y <= map.upperLeft.y;
    }

    private ArrayList<Vector2d> backtrace(Vector2d start, Vector2d end, Map<Vector2d, Vector2d> parents) {
        ArrayList<Vector2d> result = new ArrayList<>();
        result.add(end);
        Vector2d prev = parents.get(end);
        Vector2d new_prev;
        while (!(prev == start)) {
            result.add(prev);
            new_prev = parents.get(prev);
            prev = new_prev;
        }
        result.add(start);
        Collections.reverse(result);
        return result;
    }

    public ArrayList<Vector2d> BFS(Vector2d s, Vector2d e) {
        if (s.equals(e)) {
            ArrayList<Vector2d> result = new ArrayList<>();
            result.add(s);
            return result;
        }
        boolean[][] visited = new boolean[70][40];
        Map<Vector2d, Vector2d> parents = new HashMap<>();
        LinkedList<Vector2d> queue = new LinkedList<>();
        visited[s.x][s.y] = true;
        queue.add(s);
        Vector2d p;

        while (queue.size() != 0) {
            p = queue.poll();
            if (p.equals(e)) return backtrace(s,p,parents);

            ArrayList<Vector2d> next_vectors = new ArrayList<>();
            int x = p.x;
            int y = p.y;
            next_vectors.add(new Vector2d(x, y - 1));
            next_vectors.add(new Vector2d(x, y + 1));
            next_vectors.add(new Vector2d(x - 1, y));
            next_vectors.add(new Vector2d(x + 1, y));

            Collections.shuffle(next_vectors);

            for (Vector2d next_step : next_vectors) {
                int nx = next_step.x;
                int ny = next_step.y;

                if (isValid(next_step, map1) && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    parents.put(next_step, p);
                    queue.add(next_step);
                }
            }
        }
        return new ArrayList<>();
    }

    public void draw() throws FileNotFoundException {
        Platform.runLater(() -> {
            try {
                if(!lose) {
                    drawMap();
                }
                if(lose){
                    drawGameOver("YOU LOSE");
                }
                if(win){
                    drawGameOver("VICTORY!");
                }
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