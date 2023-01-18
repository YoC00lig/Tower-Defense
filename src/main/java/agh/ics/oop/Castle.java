package agh.ics.oop;

public class Castle {
    private int health;
    private Vector2d lowerRight;
    private Vector2d upperLeft;

    public Castle(int health, Vector2d lowerRight, Vector2d upperLeft){
        this.health = health;
        this.lowerRight = lowerRight;
        this.upperLeft = upperLeft;
    }

    public boolean isDestroyed(){
        if (health <= 0){
            return true;
        }
        return false;
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

    public String toString(){
        return "*";
    }

    public void subtractHealth(int value){
        this.health -= value;
    }
}
