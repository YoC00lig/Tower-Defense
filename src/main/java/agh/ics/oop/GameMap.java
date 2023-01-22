package agh.ics.oop;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GameMap {
    public final Vector2d lowerRight;
    public final Vector2d upperLeft;
    private Castle castle;
    public ConcurrentHashMap<Vector2d, LinkedList<Enemy>> enemies = new ConcurrentHashMap<>();  //(position, enemy) zmienić na ConcurrentHashMap
    public ArrayList<Enemy> listOfEnemies = new ArrayList<>();
    public Map<Vector2d, Tower> towers = new HashMap<>();   //(upperLeft, tower)
    public ArrayList<Tower> listOfTowers = new ArrayList<>();
    public static int size;
    private int spawnCountdown;
    private int initialSpawnCountdown;
    public int startEnemies;
    private ArrayList<Integer> waveSizes = new ArrayList<>();

    private int waveIndex, money;


    public GameMap(Vector2d lowerRight, Vector2d upperLeft,  int initialNumberOfEnemies, int InitMoney) {
        if ((Math.abs(lowerRight.x - upperLeft.x) <= 10) || (Math.abs(lowerRight.y - upperLeft.y) <= 10)) {
            throw new IllegalArgumentException("Incorrect map coordinates, map must be bigger.");
        }

        this.lowerRight = lowerRight;
        this.upperLeft = upperLeft;
        this.startEnemies = initialNumberOfEnemies;
        size = lowerRight.x - upperLeft.x;
        this.initialSpawnCountdown = 0;
        this.money = InitMoney;
        placeCastle();
    }

    private void placeCastle() {
        Vector2d upperLeft_ = new Vector2d((lowerRight.x-10)/2, (upperLeft.y-10)/2);
        Vector2d upperRight_ = new Vector2d(upperLeft_.x + 9, upperLeft_.y);
        Vector2d lowerLeft_ = new Vector2d(upperLeft_.x, upperLeft_.y+9);
        Vector2d lowerRight_ = new Vector2d(lowerLeft_.x+9, lowerLeft_.y);
        this.castle = new Castle(100,lowerRight_,upperLeft_);
    }

    private int getRandomFromRange(int min, int max){
        Random random = new Random(); // [min,max]
        return random.nextInt((max - min) + 1) + min;
    }
    public void placeEnemy() {
        Vector2d position;
        Random random = new Random();
        int side = random.nextInt(4);
        position = switch (side){
            case 0 -> new Vector2d(getRandomFromRange(upperLeft.x,lowerRight.x), lowerRight.y); // bottom
            case 1 -> new Vector2d(upperLeft.x, getRandomFromRange(lowerRight.y,upperLeft.y)); // left
            case 2 -> new Vector2d(getRandomFromRange(upperLeft.x,lowerRight.x), upperLeft.y); //upper
            default -> new Vector2d(lowerRight.x, getRandomFromRange(lowerRight.y,upperLeft.y)); // right
        };

        Enemy enemy = new Enemy(10, 1, position, this);
        this.listOfEnemies.add(enemy);
        this.enemies.computeIfAbsent(position, k -> new LinkedList<>());
        this.enemies.get(position).add(enemy);
    }

    // szukanie najbliższego wroga, do którego strzela wieża w jednum ruchu
    public Enemy findNearestEnemy(Tower tower) {
        Vector2d position = new Vector2d(tower.getUpperLeft().x + 1, tower.getUpperLeft().y - 1);
        int range = tower.getRange();
        Vector2d nearestPosition = new Vector2d(0, 0);
        Enemy nearestEnemy = null;
        for (Map.Entry<Vector2d, LinkedList<Enemy>> entry : this.enemies.entrySet()) {
            Vector2d currPosition = entry.getKey();
            double currDist = sqrt(pow(currPosition.x - position.x, 2) + pow(currPosition.y - position.y, 2));
            if (currDist < sqrt(pow(nearestPosition.x - position.x, 2) + pow(nearestPosition.y - position.y, 2)) && currDist < range) {
                nearestPosition = currPosition;
                nearestEnemy = entry.getValue().get(0);
            }
        }
        return nearestEnemy;
    }

    public boolean checkIfIsNearby(Vector2d enemyPosition, Vector2d upperLeft, Vector2d lowerRight) {
        boolean flag = false;
        if (enemyPosition.x == upperLeft.x - 1 && enemyPosition.y <= upperLeft.y && enemyPosition.y >= lowerRight.y) {
            flag = true;
        } else if (enemyPosition.y == upperLeft.y + 1 && enemyPosition.x >= upperLeft.x && enemyPosition.x <= lowerRight.x) {
            flag = true;
        } else if (enemyPosition.x == lowerRight.x + 1 && enemyPosition.y <= upperLeft.y && enemyPosition.y >= lowerRight.y) {
            flag = true;
        } else if (enemyPosition.y == lowerRight.y - 1 && enemyPosition.y >= upperLeft.y && enemyPosition.y <= lowerRight.y) {
            flag = true;
        }

        return flag;
    }

    // szukanie wrogów, którzy stoją przy zamku
    public ArrayList<Enemy> findAttackingEnemies() {
        Vector2d castleUpperLeft = this.castle.getUpperLeft();
        Vector2d castleLowerRight = this.castle.getLowerRight();
        ArrayList<Enemy> attackingEnemies = new ArrayList<>();
        for (Map.Entry<Vector2d, LinkedList<Enemy>> entry : this.enemies.entrySet()) {
            Vector2d position = entry.getKey();
            boolean flag = checkIfIsNearby(position, castleUpperLeft, castleLowerRight);
            if (flag) {
                attackingEnemies.addAll(entry.getValue());
            }
        }
        return attackingEnemies;
    }

    // szukanie wrogów, którzy stoją przy wierzy
    public ArrayList<Enemy> findAttackingEnemiesTower(Tower tower) {
        Vector2d towerUpperLeft = tower.getUpperLeft();
        Vector2d towerLowerRight = tower.getLowerRight();
        ArrayList<Enemy> attackingEnemiesTower = new ArrayList<>();
        for (Map.Entry<Vector2d, LinkedList<Enemy>> entry : this.enemies.entrySet()) {
            Vector2d position = entry.getKey();
            boolean flag = checkIfIsNearby(position, towerUpperLeft, towerLowerRight);
            if (flag) {
                attackingEnemiesTower.addAll(entry.getValue());
            }
        }
        return attackingEnemiesTower;
    }

    // sprawdzanie czy enemy stoi przy zamku
    public boolean isNextToCastle(Enemy enemy) {
        Vector2d castleUpperLeft = this.castle.getUpperLeft();
        Vector2d castleLowerRight = this.castle.getLowerRight();
        Vector2d position = enemy.getPosition();
        return checkIfIsNearby(position, castleUpperLeft, castleLowerRight);
    }

    // sprawdzanie czy enemy stoi przy jakiejś wierzy
    public boolean isNextToTower(Enemy enemy) {
        for (Map.Entry<Vector2d, Tower> tower : this.towers.entrySet()) {
            Vector2d towerUpperLeft = tower.getKey();
            Vector2d towerLowerRight = towerUpperLeft.add(new Vector2d(2, -2));
            Vector2d position = enemy.getPosition();
            if (checkIfIsNearby(position, towerUpperLeft, towerLowerRight)) {
                return true;
            }
        }
        return false;
    }

    // strzelanie z wież
    public void shotFromTowers() {
        Random random = new Random();
        int value;
        int strength;
        for (Tower tower : listOfTowers) {
            Enemy nearestEnemy = findNearestEnemy(tower);
            if (nearestEnemy != null){
                strength = nearestEnemy.getStrength();
                value = random.nextInt(5 * strength);
                nearestEnemy.subtractHealth(value);
            }
        }
    }

    // usuwanie zabitych wrogów
    public void deleteDeadEnemies() {
        ArrayList<Enemy> tmp = new ArrayList<>();
        for (Enemy enemy : listOfEnemies) {
            if (enemy.getHealth() <= 0) {
                tmp.add(enemy);
            }
        }
        for (Enemy enemy : tmp) {
            Vector2d pos = enemy.getPosition();
            this.enemies.get(pos).remove(enemy);
            this.listOfEnemies.remove(enemy);
        }
    }

    // usuwanie zniszczonych wież
    public void deleteDeadTowers() {
        ArrayList<Tower> tmp = new ArrayList<>();
        for (Tower tower : listOfTowers) {
            if (tower.getHealth() <= 0) {
                tmp.add(tower);
            }
        }
        for (Tower tower : tmp) {
            Vector2d pos = tower.getUpperLeft();
            this.towers.remove(pos, tower);
            this.listOfTowers.remove(tower);
        }
    }

    // atak na zamek
    public void attackCastle() {
        ArrayList<Enemy> attackingEnemiesCastle = findAttackingEnemies();
        Random random = new Random();
        int value;
        int strength;
        Enemy enemy;
        Castle castle = this.castle;
        for (Enemy item : attackingEnemiesCastle) {
            enemy = item;
            if (!enemy.getMadeHit()) {
                strength = enemy.getStrength();
                value = random.nextInt(5 * strength);
                castle.subtractHealth(value);
                enemy.changeMadeHit(true);
            }

        }
    }

    // atak na wieżę
    public void attackTowers() {
        Random random = new Random();
        int value;
        int strength;
        Enemy enemy;
        if (listOfTowers.size() > 0){
            for (Tower tower : listOfTowers) {
                ArrayList<Enemy> attackingEnemiesTower = findAttackingEnemiesTower(tower);
                for (Enemy item : attackingEnemiesTower) {
                    enemy = item;
                    if (!enemy.getMadeHit()) {
                        strength = enemy.getStrength();
                        value = random.nextInt(5 * strength);
                        tower.subtractHealth(value);
                        enemy.changeMadeHit(true);
                    }
                }
            }
        }
    }

    // resetuje informacje o tym czy enemy zaatakował wieżę lub zamek
    public void removeHits() {
        for (Enemy enemy : listOfEnemies) {
            enemy.changeMadeHit(false);
        }
    }

    // fale wrogów
    public void enemiesWave() {
        if (this.waveIndex < this.waveSizes.size()) {  //
            if (this.spawnCountdown <= 0) {
                for (int i = 0; i < waveSizes.get(waveIndex); i++) placeEnemy();
                this.initialSpawnCountdown -= 5;
                this.spawnCountdown = this.initialSpawnCountdown;
                this.waveIndex += 1;
            } else {
                this.spawnCountdown -= 1;
            }
        }
    }

    public Castle getCastle() {
        return this.castle;
    }
    // dodawanie wieży
    public Tower getNewTower(Vector2d upperLeft, int type){
        Vector2d low = new Vector2d(upperLeft.x, upperLeft.y - 2);
        Vector2d high = new Vector2d(upperLeft.x + 2, upperLeft.y);
        Tower tower;
        if (type == 1) tower = new Tower(100, 1, low, high,1);
        else tower = new Tower(100, 2, low, high,2);
        return tower;
    }

    public void addTower(Tower tower) {
        Vector2d upperLeft_ = tower.getUpperLeft();
        towers.put(upperLeft_, tower);
        listOfTowers.add(tower);
    }
    public boolean checkIfCanPlaceTower(Tower tower) {
        boolean left = !buildingAt(tower.getUpperLeft()) && !buildingAt(tower.getLowerLeft());
        boolean right = !buildingAt(tower.getUpperRight()) && !buildingAt(tower.getLowerRight());
        return left && right;
    }

    // poruszanie przeciwników - jeden ruch
    public boolean buildingAt(Vector2d position){
        for (Tower tower: towers.values()){ // sprawdzanie czy jakaś wieża tam nie stoi
            Vector2d low = tower.getLowerLeft();
            Vector2d high = tower.getUpperRight();
            if (position.x >= low.x && position.x <= high.x && position.y >= low.y && position.y <= high.y) return true;
        }
        Vector2d low = castle.getLowerRight(); // sprawdzanie czy tam nie stoi zamek
        Vector2d high = castle.getUpperLeft();
        return position.x >= high.x && position.x <= low.x && position.y >= low.y && position.y <= high.y;
    }

    public void moveAll(){
        for (LinkedList<Enemy> list: enemies.values()){
            for (Enemy enemy: list) {
                if (!isNextToCastle(enemy) && !isNextToTower(enemy)) enemy.move();
            }
        }
    }
}