package agh.ics.oop;

import agh.ics.oop.gui.App;

import java.util.ArrayList;


public class GameEngine {
    private ArrayList<Enemy> enemies;
    private ArrayList<Tower> towers;
    private GameMap map;
    private boolean isRunning;
    public final int moveDelay = 300;
    private final App app;
    public GameEngine(GameMap map, App app){
        this.map = map;
        this.app =  app;
        this.isRunning = true;
    }

    public void updateMap(){
        if(this.isRunning){
            map.moveAll();   // ruch wrogów
            map.attackCastle();  // atak wrogów na zamek
            map.attackTowers();  // atak wrogów na wieże
            map.deleteDeadTowers();   // usunięcie zniszczonych wież
            map.shotFromTowers();     // atak wież na wrogów
            map.deleteDeadEnemies();  // usunięcie martwych wrogów
            map.removeHits();    // reset informacji o tym czy wróg zaatakował
            map.enemiesWave();   // wygenerowanie fali wrogów
        }
    }

    public void run(){
        while(map.towers.size() > 0) {
            updateMap();
            try {
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



}


