package agh.ics.oop;

public class Tower {
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private int health;
    private int range;
    public int type;

    public Tower(int health, int range, Vector2d lowerLeft, Vector2d upperRight, int type){
        this.health = health;
        this.range = range;
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        this.type = type;
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

    public String getPath() {
        if (this.type ==1 ) return "src/main/resources/tower.png";
        else return "src/main/resources/tower1.png";
    }

}
