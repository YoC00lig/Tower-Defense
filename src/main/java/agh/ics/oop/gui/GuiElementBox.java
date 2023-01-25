package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox{
    private final VBox vBox;
    private final  ImageView imageView;

    public GuiElementBox(IMapElement object){
        Image image = null;
        try {
            image = new Image(new FileInputStream(object.getPath(object)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);

        vBox = new VBox();
        vBox.getChildren().addAll(imageView);
        vBox.setAlignment(Pos.CENTER);
    }
    public VBox getvBox()  {
        return vBox;
    }
}