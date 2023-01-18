package agh.ics.oop;

import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GameMap {
    protected final Vector2d lowerRight;
    protected final Vector2d upperLeft;
    private Castle castle;
    private Map<Vector2d, Enemy> enemies;  //(position, enemy)
    private Map<Vector2d, Tower> towers;   //(upperLeft, tower)
    public static int size;
    public static final int moveDelay = 300;

    public GameMap(Vector2d lowerRight, Vector2d upperLeft, int initialNumberOfEnemies) {
        if ((lowerRight.x - upperLeft.x <= 10) || (lowerRight.y - upperLeft.y <= 10)) {
            throw new IllegalArgumentException("Incorrect map coordinates, map must be bigger.");
        }

        this.lowerRight = lowerRight;
        this.upperLeft = upperLeft;
        size = lowerRight.x - upperLeft.x;
        placeCastle();
        for (int i = 0; i < initialNumberOfEnemies; i++) {
            placeEnemy();
        }
    }

    private void placeCastle() {
        Vector2d start = new Vector2d(this.lowerRight.x - this.upperLeft.x - 10, this.upperLeft.y - this.lowerRight.y - 10);
        Vector2d end = new Vector2d(start.x + 10, start.y + 10);

        this.castle = new Castle(100, start, end);
    }

    private void placeEnemy() {
        int lower = this.lowerRight.x;
        int upper = this.upperLeft.x;

        Vector2d position = new Vector2d(0, 0);
        Random random = new Random();
        int side = random.nextInt(4);

        if (side == 0) {  //bottom side
            position = new Vector2d(random.nextInt(upper + 1), lower);
        } else if (side == 1) {  //left side
            position = new Vector2d(lower, random.nextInt(upper + 1));
        } else if (side == 2) {  // upper side
            position = new Vector2d(random.nextInt(upper + 1), upper);
        } else if (side == 3) {  // right side
            position = new Vector2d(upper, random.nextInt(upper + 1));
        }

        Enemy enemy = new Enemy(10, 5, position);
        this.enemies.put(position, enemy);
    }

    public void placeTower(Tower tower) {
        Vector2d lower = new Vector2d(this.lowerRight.x - 1, this.lowerRight.y + 1);
        Vector2d upper = new Vector2d(this.upperLeft.x + 1, this.upperLeft.y - 1);
        Vector2d lowerCastle = this.castle.getLowerRight();
        Vector2d upperCastle = this.castle.getUpperLeft();

        Vector2d lowerTower = tower.getLowerRight();
        Vector2d upperTower = tower.getUpperLeft();

        boolean canPlace = true;

        if (!lowerTower.follows(lower) && !upperTower.precedes(upper)) {
            canPlace = false;
            throw new IllegalArgumentException("Incorrect tower coordinates, cannot be off the map or on the edges.");
        }
        if ((lowerTower.follows(lowerCastle) && lowerTower.precedes(upperCastle)) || (upperTower.follows(lowerCastle) && upperTower.precedes(upperCastle))) {
            canPlace = false;
            throw new IllegalArgumentException("Incorrect tower coordinates, cannot overlap a castle square.");
        }
    }

    public Object objectAt(Vector2d position) {
        return towers.get(position);
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    // szukanie najbliższego wroga, do którego strzela wieża w jednum ruchu
    public Enemy findNearestEnemy(Tower tower){
        Vector2d position = new Vector2d(tower.getUpperLeft().x + 1, tower.getUpperLeft().y - 1);
        int range = tower.getRange();
        Vector2d nearestPosition = new Vector2d(0,0);
        Enemy nearestEnemy = null;
        for (Map.Entry<Vector2d, Enemy> entry : this.enemies.entrySet()){
            Vector2d currPosition = entry.getKey();
            double currDist = sqrt(pow(currPosition.x - position.x,2) + pow(currPosition.y - position.y,2));
            if (currDist < sqrt(pow(nearestPosition.x - position.x,2) + pow(nearestPosition.y - position.y,2)) && currDist < range){
                nearestPosition = currPosition;
                nearestEnemy = entry.getValue();
            }
        }
        return nearestEnemy;
    }

    public boolean checkIfIsNearby(Vector2d enemyPosition, Vector2d upperLeft, Vector2d lowerRight){
        boolean flag = false;
        if (enemyPosition.x == upperLeft.x-1 && enemyPosition.y <= upperLeft.y && enemyPosition.y >= lowerRight.y){
            flag = true;
        }
        else if (enemyPosition.y == upperLeft.y+1 && enemyPosition.x >= upperLeft.x && enemyPosition.x <= lowerRight.x){
            flag = true;
        }
        else if (enemyPosition.x == lowerRight.x+1 && enemyPosition.y <= upperLeft.y && enemyPosition.y >= lowerRight.y){
            flag = true;
        }
        else if (enemyPosition.y == lowerRight.y-1 && enemyPosition.y >= upperLeft.y && enemyPosition.y <= lowerRight.y){
            flag = true;
        }

        return flag;
    }

    // szukanie wrogów, którzy stoją przy zamku
    public ArrayList<Enemy> findAttackingEnemies(){
        Vector2d castleUpperLeft = this.castle.getUpperLeft();
        Vector2d castleLowerRight = this.castle.getLowerRight();
        ArrayList<Enemy> attackingEnemies = new ArrayList<Enemy>();
        for (Map.Entry<Vector2d, Enemy> entry : this.enemies.entrySet()) {
            Vector2d position = entry.getKey();
            boolean flag = checkIfIsNearby(position, castleUpperLeft, castleLowerRight);
            if (flag) {
                attackingEnemies.add(entry.getValue());
            }
        }
        return attackingEnemies;
    }

    // szukanie wrogów, którzy stoją przy wierzy
    public ArrayList<Enemy> findAttackingEnemiesTower(Tower tower){
        Vector2d towerUpperLeft = tower.getUpperLeft();
        Vector2d towerLowerRight = tower.getLowerRight();
        ArrayList<Enemy> attackingEnemiesTower = new ArrayList<Enemy>();
        for (Map.Entry<Vector2d, Enemy> entry : this.enemies.entrySet()) {
            Vector2d position = entry.getKey();
            boolean flag = checkIfIsNearby(position, towerUpperLeft, towerLowerRight);
            if (flag) {
                attackingEnemiesTower.add(entry.getValue());
            }
        }
        return attackingEnemiesTower;
    }

    // sprawdzanie czy enemy stoi przy zamku
    public boolean isNextToCastle(Enemy enemy){
        Vector2d castleUpperLeft = this.castle.getUpperLeft();
        Vector2d castleLowerRight = this.castle.getLowerRight();
        Vector2d position = enemy.getPosition();
        if(checkIfIsNearby(position, castleUpperLeft, castleLowerRight)){
            return true;
        }
        return false;
    }

    // sprawdzanie czy enemy stoi przy jakiejś wierzy
    public boolean isNextToTower(Enemy enemy){
        for (Map.Entry<Vector2d, Tower> tower : this.towers.entrySet()) {
            Vector2d towerUpperLeft = tower.getKey();
            Vector2d towerLowerRight = towerUpperLeft.add(new Vector2d(2,-2));
            Vector2d position = enemy.getPosition();
            if (checkIfIsNearby(position, towerUpperLeft, towerLowerRight)) {
                return true;
            }
        }
        return false;
    }

    // odtwarzanie ścieżki
    private ArrayList<Vector2d> backtrace(Vector2d start, Vector2d end, Map<Vector2d, Vector2d> parents) {
        ArrayList<Vector2d> result = new ArrayList<>();
        result.add(end);
        Vector2d prev = parents.get(end);
        Vector2d new_prev;
        while (!prev.equals(start)) {
            result.add(prev);
            new_prev = parents.get(prev);
            prev = new_prev;
        }
        result.add(start);
        return result;
    }

    // wyszukuje najkrótszą ścieżkę z s do destination i zwraca kolejne punkty w formie tablicy ArrayList,
    // jesli nie istnieje taka ścieżka to zwracana tablica jest pusta.
    public ArrayList<Vector2d> BFS(Vector2d s, Vector2d destination) {
        boolean[][] visited = new boolean[size][size];
        Map<Vector2d, Vector2d> parents = new HashMap<>();
        LinkedList<Vector2d> queue = new LinkedList<>();
        visited[s.x][s.y] = true;
        queue.add(s);

        while (queue.size() != 0) {
            s = queue.poll();
            if (s.equals(destination)) return backtrace(s, destination, parents);
            ArrayList<Vector2d> next_vectors = new ArrayList<>();
            int x = s.x;
            int y = s.y;
            next_vectors.add(new Vector2d(x, y - 1));
            next_vectors.add(new Vector2d(x, y + 1));
            next_vectors.add(new Vector2d(x - 1, y));
            next_vectors.add(new Vector2d(x + 1, y));
            next_vectors.add(new Vector2d(x - 1, y + 1));
            next_vectors.add(new Vector2d(x - 1, y - 1));
            next_vectors.add(new Vector2d(x + 1, y + 1));
            next_vectors.add(new Vector2d(x + 1, y - 1));

            for (Vector2d next_step : next_vectors) {
                int nx = next_step.x;
                int ny = next_step.y;

                if (!visited[nx][ny] && next_step.inBounds(size)) {
                    visited[nx][ny] = true;
                    parents.put(next_step, s);
                    queue.add(next_step);
                }
            }
        }
        return new ArrayList<>();
    }


    public Castle getCastle(){
        return this.castle;
    }
}
