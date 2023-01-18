package agh.ics.oop;

import java.util.ArrayList;
import java.util.Random;

public class GameEngine {
    private ArrayList<Enemy> enemies;
    private ArrayList<Tower> towers;
    private GameMap map;
    private boolean isRunning;
    private int spawnCountdown;
    private int initialSpawnCountdown;


    public void shotFromTowers(){
        Random random = new Random();
        int value;
        for(Tower tower: towers){
            Enemy nearestEnemy = map.findNearestEnemy(tower);
            value = random.nextInt(5);
            nearestEnemy.subtractHealth(value);
        }
    }

    public void deleteDeadEnemies(){
        ArrayList<Enemy> tmp = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.getHealth() <= 0) {
                tmp.add(enemy);
            }
        }
        for (Enemy enemy : tmp) {
            Vector2d pos = enemy.getPosition();
            this.enemies.remove(enemy);
            // usunięcie enemies z GameMap
        }
    }

    public void deleteDeadTowers(){
        ArrayList<Tower> tmp = new ArrayList<>();
        for (Tower tower : towers) {
            if (tower.getHealth() <= 0) {
                tmp.add(tower);
            }
        }
        for (Tower tower : tmp) {
            Vector2d pos = tower.getUpperLeft();
            this.towers.remove(tower);
            // usunięcie enemies z GameMap
        }
    }

    // atak na zamek
    public void attackCastle(){
        ArrayList<Enemy> attackingEnemiesCastle = map.findAttackingEnemies();
        int size = attackingEnemiesCastle.size();
        Random random = new Random();
        int value;
        Castle castle = this.map.getCastle();
        for(int i = 0; i<size; i++){
            value = random.nextInt(5);
            castle.subtractHealth(value);
        }
    }

    // atak na wierzę - trzeba zrobić tak żeby jeden enemy atakował w jednym ruchu tylko zamek lub tylko wieżę jeśli stoi przy obu obiektach
    public void attackTowers(){
        Random random = new Random();
        int value;
        for (Tower tower : towers) {
            ArrayList<Enemy> attackingEnemiesTower = map.findAttackingEnemiesTower(tower);
            int size = attackingEnemiesTower.size();
            for(int i = 0; i<size; i++){
                value = random.nextInt(5);
                tower.subtractHealth(value);
            }
        }
    }

    public void enemiesMove(){
        for (Enemy enemy: enemies){
            if(map.isNextToCastle(enemy) || map.isNextToTower(enemy)){
                continue;
            }

        }

    }

    public void updateMap(){
        if(this.isRunning){
            if (this.spawnCountdown<=0){

            }
            else{
                this.spawnCountdown -= 1;
            }
            enemiesMove();
            attackCastle();
            attackTowers();
            shotFromTowers();
            deleteDeadEnemies();
            deleteDeadTowers();
        }
    }

    public void run(){

    }

}


