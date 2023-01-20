package agh.ics.oop;

import java.util.ArrayList;
import java.util.Collections;

public class Enemy {
    private int health;
    private int strength;
    private Vector2d position;
    GameMap map;

    private boolean madeHit; // czy w ruchu juz udeżył


    public Enemy(int health, int strength, Vector2d position, GameMap map){
        this.health = health;
        this.strength = strength;
        this.position = position;
        this.map = map;
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

    public void move() {
        ArrayList<Vector2d> next_vectors = new ArrayList<>();
        int x = this.position.x;
        int y = this.position.y;
        next_vectors.add(new Vector2d(x, y - 1));
        next_vectors.add(new Vector2d(x, y + 1));
        next_vectors.add(new Vector2d(x - 1, y));
        next_vectors.add(new Vector2d(x + 1, y));
        next_vectors.add(new Vector2d(x - 1, y + 1));
        next_vectors.add(new Vector2d(x - 1, y - 1));
        next_vectors.add(new Vector2d(x + 1, y + 1));
        next_vectors.add(new Vector2d(x + 1, y - 1));
        Collections.shuffle(next_vectors);
        for (Vector2d v: next_vectors){
            if (!map.buildingAt(v)){
                this.position = v;
            }
        }
        
    public int getStrength(){
        return this.strength;
    }
    public boolean getMadeHit(){
        return this.madeHit;
    }
    public void changeMadeHit(boolean x){
        this.madeHit = x;
    }

    public String toString(){
        return "E";

    }
}
