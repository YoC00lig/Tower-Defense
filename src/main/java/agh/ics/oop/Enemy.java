package agh.ics.oop;

public class Enemy {
    private int health;
    private int strength;
    private Vector2d position;


    public Enemy(int health, int strength, Vector2d position){
        this.health = health;
        this.strength = strength;
        this.position = position;
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
}
