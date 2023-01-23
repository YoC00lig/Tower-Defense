package agh.ics.oop;

public class Wall {
    Vector2d position;
    private int health;

    public Wall(int health, Vector2d position){
        this.health = health;
        this.position = position;
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
}
