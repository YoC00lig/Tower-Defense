package agh.ics.oop;

import agh.ics.oop.gui.App;
import java.io.FileNotFoundException;

public class GameEngine implements Runnable, IEngine{
    private GameMap map;
    private boolean isRunning;
    public final int moveDelay = 50;
    private final App app;

    public GameEngine(GameMap map, App app){
        this.map = map;
        this.app =  app;

        this.isRunning = true;

        for (int i = 0; i < map.waveSizes[0].length; i++){
            for(int j = 0; j < map.waveSizes[0][i]; j++)
                map.placeEnemy(i);
        }
    }

    public void updateMap(){
        map.sellTowers();
        map.addTowers();
        map.addWalls();
        map.moveAll();   // ruch wrogów
        map.attackCastle();  // atak wrogów na zamek
        map.attackTowers();  // atak wrogów na wieże
        map.attackWalls();
        map.deleteDeadTowers();   // usunięcie zniszczonych wież
        map.shotFromTowers();     // atak wież na wrogów
        map.deleteDeadEnemies();  // usunięcie martwych wrogów
        map.removeWalls();
        map.removeHits();    // reset informacji o tym czy wróg zaatakował
        map.removeMoves();
        map.enemiesWave();   // wygenerowanie fali wrogów
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.moveDelay);
        } catch (InterruptedException e) {
            System.exit(0);
        }
        while (true) {
            updateMap();
            if(this.map.getCastle().getHealth()<=0){
                app.lose = true;
                try {
                    app.draw();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.exit(0);
                    throw new RuntimeException(e);
                }
                break;
            }
            if(this.map.listOfEnemies.size()==0){
                app.win = true;
                try {
                    app.draw();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.exit(0);
                    throw new RuntimeException(e);
                }
                break;
            }
            try {
                app.draw();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException e) {
                System.exit(0);
                break;
            }
        }
        Thread.interrupted();
    }

}
