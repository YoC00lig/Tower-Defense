package agh.ics.oop;

public class Wall {
    private Vector2d lowerRight;
    private Vector2d upperLeft;
    private int health;

    public Wall(int health, Vector2d lowerRight, Vector2d upperLeft){
        this.health = health;
        this.lowerRight = lowerRight;
        this.upperLeft = upperLeft;
    }

    public int getHealth(){
        return this.health;
    }

    public Vector2d getLowerRight(){
        return this.lowerRight;
    }

    public Vector2d getUpperLeft(){
        return this.upperLeft;
    }

    public void subtractHealth(int value){
        this.health -= value;
    }

    public String toString(){
        return "w";
    }
}
