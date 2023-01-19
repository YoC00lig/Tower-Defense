package agh.ics.oop;

public class Tower {
    private Vector2d lowerRight;
    private Vector2d upperLeft;
    private int health;
    private int range;

    public Tower(int health, int range, Vector2d lowerRight, Vector2d upperLeft){
        this.health = health;
        this.range = range;
        this.lowerRight = lowerRight;
        this.upperLeft = upperLeft;
    }

    public int getHealth(){
        return this.health;
    }

    public int getRange() { return this.range; }
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
        return "t";
    }

}
