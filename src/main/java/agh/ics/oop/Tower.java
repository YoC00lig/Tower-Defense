package agh.ics.oop;

public class Tower {
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private int health;
    private int range;

    public Tower(int health, int range, Vector2d lowerLeft, Vector2d upperRight){
        this.health = health;
        this.range = range;
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    public int getHealth(){
        return this.health;
    }

    public int getRange() { return this.range; }
    public Vector2d getLowerLeft(){
        return this.lowerLeft;
    }

    public Vector2d getUpperRight(){
        return this.upperRight;
    }

    public  Vector2d getUpperLeft() {
        return new Vector2d(this.lowerLeft.x, this.upperRight.y);
    }

    public  Vector2d getLowerRight() {
        return new Vector2d(this.upperRight.x, this.lowerLeft.y);
    }
    public void subtractHealth(int value){
        this.health -= value;
    }

    public String toString(){
        return "t";
    }

}
