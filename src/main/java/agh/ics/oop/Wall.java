package agh.ics.oop;

import java.util.*;

public class Wall implements IMapElement{
    Vector2d position;
    private int health;
    public double maxHealth;

    public Wall(int health, Vector2d position){
        this.health = health;
        this.position = position;
        this.maxHealth = health;
    }

    public int getHealth(){
        return this.health;
    }
    public Vector2d getPosition() {
        return this.position;
    }

    public void subtractHealth(int value){
        this.health -= value;
    }

    public String toString(){
        return "w";
    }
    public String getPath(IMapElement object) {
        return "src/main/resources/wall.png";
    }
    public int getPrice() {
        return 100;
    }
    public double maxHealth() {
        return this.maxHealth;
    }
}
