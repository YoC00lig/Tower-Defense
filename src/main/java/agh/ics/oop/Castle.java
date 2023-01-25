package agh.ics.oop;

public class Castle implements IMapElement{
    private int health;
    public double maxHealth;
    private Vector2d lowerRight, lowerLeft;
    private Vector2d upperLeft, upperRight;

    public Castle(int health, Vector2d lowerRight, Vector2d upperLeft){
        this.health = health;
        this.maxHealth = health;
        this.lowerRight = lowerRight;
        this.upperLeft = upperLeft;
        this.lowerLeft = new Vector2d(upperLeft.x, lowerRight.y);
        this.upperRight = new Vector2d(lowerRight.x, upperLeft.y);
    }
    public boolean isDestroyed(){
        return health <= 0;
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

    public Vector2d getLowerLeft(){return this.lowerLeft;}
    public Vector2d getUpperRight(){return this.upperRight;}

    public String toString(){
        return "c";
    }

    public void subtractHealth(int value){
        this.health -= value;
    }

    public String getPath(IMapElement object){
        return "src/main/resources/castle.png";
    }
    public Vector2d getPosition(){
        return this.upperLeft;
    }

    public void heal(){
        this.health = (int) maxHealth;
    }

    public double maxHealth() {
        return this.maxHealth;
    }
}