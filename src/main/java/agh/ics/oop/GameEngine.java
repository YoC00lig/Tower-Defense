package agh.ics.oop;

import agh.ics.oop.gui.App;
import java.io.FileNotFoundException;


public class GameEngine implements Runnable, IEngine{
    private GameMap map;
    private boolean isRunning;
    public final int moveDelay = 300;
    private final App app;

    public GameEngine(GameMap map, App app){
        this.map = map;
        this.app =  app;
        this.isRunning = true;

        for (int i = 0; i < map.startEnemies; i++){
            map.placeEnemy();
        }
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

    @Override
    public void run() {
        while (map.getCastle().getHealth() >= 0) {
            updateMap();
            try {
                app.draw();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException e) {
                break;
            }
        }
        Thread.interrupted();
    }

}


