package agh.ics.oop;

public class Enemy {
    private int health;
    private int strength;
    private Vector2d position;

    private boolean madeHit;


    public Enemy(int health, int strength, Vector2d position){
        this.health = health;
        this.strength = strength;
        this.position = position;
        this.madeHit = false;
    }

    public void subtractHealth(int value){
        this.health -= value;
    }

    public int getHealth(){
        return this.health;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public void changeMadeHit(boolean x){
        this.madeHit = x;
    }
}
