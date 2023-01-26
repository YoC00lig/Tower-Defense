package agh.ics.oop;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GameMap implements IPositionChangeObserver {
    public final Vector2d lowerRight;
    public final Vector2d upperLeft;
    private Castle castle;
    public ConcurrentHashMap<Vector2d, LinkedList<Enemy>> enemies = new ConcurrentHashMap<>();  //(position, enemy) zmienić na ConcurrentHashMap
    public ArrayList<Enemy> listOfEnemies = new ArrayList<>();
    public Map<Vector2d, Tower> towers = new HashMap<>();   //(upperLeft, tower)
    public ArrayList<Tower> listOfTowers = new ArrayList<>();
    public int[] deadEnemies = {0, 0, 0};
    public static int size;
    private int spawnCountdown;
    private int initialSpawnCountdown;
    public int money;
    private int waveIndex = 1;
    public int[][] waveSizes = {};
    private final int[][] waveVariant1 = {{10, 0, 0}, {8, 5, 0}, {7, 5, 3}, {7, 5, 5}, {3, 7, 5}, {0, 10, 5}};
    private final int[][] waveVariant2 = {{10, 0, 0}, {8, 5, 0}, {5, 5, 5}, {3, 5, 5}, {10,0,6}, {4, 4, 7}, {0, 5, 8}};
    private final int[][] waveVariant3 = {{10, 5, 0}, {10, 10, 0}, {5, 5, 5}, {5, 10, 5}, {2, 8, 8}, {2, 10, 5}, {0, 10, 10}};
    boolean floodMode;
    public ArrayList<Vector2d> cells = new ArrayList<>();
    private ArrayList<Tower> newTowers = new ArrayList<>();
    private ArrayList<Tower> soldTowers = new ArrayList<>();
    public ArrayList<Wall> newWalls = new ArrayList<>();
    public ArrayList<Wall> walls = new ArrayList<>();
    public boolean extendedMode;
    public Vector2d startWall=null;
    private int score = 0;


    public GameMap(Vector2d lowerRight, Vector2d upperLeft, int InitMoney, int mapVariant, boolean flood, boolean extended) {
        if ((Math.abs(lowerRight.x - upperLeft.x) <= 10) || (Math.abs(lowerRight.y - upperLeft.y) <= 10)) {
            throw new IllegalArgumentException("Incorrect map coordinates, map must be bigger.");
        }
        this.lowerRight = lowerRight;
        this.upperLeft = upperLeft;
        size = lowerRight.x - upperLeft.x;
        this.initialSpawnCountdown = 50;
        this.spawnCountdown = 50;
        this.money = InitMoney;
        this.floodMode = flood;
        this.extendedMode = extended;

        if (mapVariant == 1) this.waveSizes = this.waveVariant1;
        else if (mapVariant == 2) this.waveSizes = this.waveVariant2;
        else if (mapVariant == 3) this.waveSizes = this.waveVariant3;

        placeCastle();
        if (flood) this.cells = generateFloodVectors();
    }

    private void placeCastle() {
        Vector2d upperLeft_ = new Vector2d((lowerRight.x - 10) / 2, (upperLeft.y - 10) / 2 + 9);
        Vector2d lowerLeft_ = new Vector2d(upperLeft_.x, upperLeft_.y - 9);
        Vector2d lowerRight_ = new Vector2d(lowerLeft_.x + 9, lowerLeft_.y);
        this.castle = new Castle(500, lowerRight_, upperLeft_);
    }

    private int getRandomFromRange(int min, int max) {
        Random random = new Random(); // [min,max]
        return random.nextInt((max - min) + 1) + min;
    }

    public void placeEnemy(int enemyVariant) {
        Vector2d position;
        Random random = new Random();
        int side = random.nextInt(4);
        position = switch (side) {
            case 0 -> new Vector2d(getRandomFromRange(upperLeft.x, lowerRight.x), lowerRight.y); // bottom
            case 1 -> new Vector2d(upperLeft.x, getRandomFromRange(lowerRight.y, upperLeft.y)); // left
            case 2 -> new Vector2d(getRandomFromRange(upperLeft.x, lowerRight.x), upperLeft.y); //upper
            default -> new Vector2d(lowerRight.x, getRandomFromRange(lowerRight.y, upperLeft.y)); // right
        };
        if (enemyVariant == 0) {
            Enemy enemy = new Enemy(10, 1, position, this, 0);
            this.listOfEnemies.add(enemy);
            this.enemies.computeIfAbsent(position, k -> new LinkedList<>());
            this.enemies.get(position).add(enemy);
            enemy.addObserver(this);
        } else if (enemyVariant == 1) {
            Enemy enemy = new Enemy(20, 2, position, this, 1);
            this.listOfEnemies.add(enemy);
            this.enemies.computeIfAbsent(position, k -> new LinkedList<>());
            this.enemies.get(position).add(enemy);
            enemy.addObserver(this);
        } else if (enemyVariant == 2) {
            Enemy enemy = new Enemy(30, 2, position, this, 2);
            this.listOfEnemies.add(enemy);
            this.enemies.computeIfAbsent(position, k -> new LinkedList<>());
            this.enemies.get(position).add(enemy);
            enemy.addObserver(this);
        }

    }

    // szukanie najbliższego wroga, do którego strzela wieża w jednum ruchu
    public Enemy findNearestEnemy(Tower tower) {
        Vector2d position = new Vector2d(tower.getUpperLeft().x + 1, tower.getUpperLeft().y - 1);
        int range = tower.getRange();
        Vector2d nearestPosition = new Vector2d(0, 0);
        Enemy nearestEnemy = null;
        for (Enemy enemy : listOfEnemies) {
            Vector2d currPosition = enemy.getPosition();
            double currDist = sqrt(pow(currPosition.x - position.x, 2) + pow(currPosition.y - position.y, 2));
            if (currDist < sqrt(pow(nearestPosition.x - position.x, 2) + pow(nearestPosition.y - position.y, 2)) && currDist < range) {
                nearestPosition = currPosition;
                nearestEnemy = enemy;
            }
        }
        return nearestEnemy;
    }

    public boolean checkIfIsNearbyCastle(Vector2d position) {
        int startX, endX, startY, endY;
        startX = castle.getUpperLeft().x;
        endX = castle.getUpperRight().x;
        startY = castle.getLowerLeft().y;
        endY = castle.getUpperLeft().y;
        if (position.x == startX - 1 && position.y <= endY && position.y >= startY) return true;
        else if (position.x == endX + 1 && position.y <= endY && position.y >= startY) return true;
        else if (position.y == startY - 1 && position.x >= startX && position.x <= endX) return true;
        else return position.y == endY + 1 && position.x >= startX && position.x <= endX;
    }

    public boolean checkIfIsNearbyTower(Vector2d position, Tower tower) {
        int startX, endX, startY, endY;
        startX = tower.getUpperLeft().x;
        endX = tower.getUpperRight().x;
        startY = tower.getLowerLeft().y;
        endY = tower.getUpperLeft().y;
        if (position.x == startX - 1 && position.y <= endY && position.y >= startY) return true;
        else if (position.x == endX + 1 && position.y <= endY && position.y >= startY) return true;
        else if (position.y == startY - 1 && position.x >= startX && position.x <= endX) return true;
        else return position.y == endY + 1 && position.x >= startX && position.x <= endX;
    }

    // szukanie wrogów, którzy stoją przy zamku
    public ArrayList<Enemy> findAttackingEnemies() {
        ArrayList<Enemy> attackingEnemies = new ArrayList<>();
        for (Map.Entry<Vector2d, LinkedList<Enemy>> entry : this.enemies.entrySet()) {
            Vector2d position = entry.getKey();
            boolean flag = checkIfIsNearbyCastle(position);
            if (flag) {
                attackingEnemies.addAll(entry.getValue());
            }
        }
        return attackingEnemies;
    }

    // szukanie wrogów, którzy stoją przy wieży
    public ArrayList<Enemy> findAttackingEnemiesTower(Tower tower) {
        ArrayList<Enemy> attackingEnemiesTower = new ArrayList<>();
        for (Map.Entry<Vector2d, LinkedList<Enemy>> entry : this.enemies.entrySet()) {
            Vector2d position = entry.getKey();
            boolean flag = checkIfIsNearbyTower(position, tower);
            if (flag) {
                attackingEnemiesTower.addAll(entry.getValue());
            }
        }
        return attackingEnemiesTower;
    }

    public ArrayList<Enemy> findAttackingWall(Wall wall){
        ArrayList<Enemy> attackingEnemies = new ArrayList<>();
        for (Map.Entry<Vector2d, LinkedList<Enemy>> entry : this.enemies.entrySet()) {
            Vector2d position = entry.getKey();
            boolean flag = isNextToWall(position, wall);
            if (flag) {
                attackingEnemies.addAll(entry.getValue());
            }
        }
        return attackingEnemies;
    }

    // sprawdzanie czy enemy stoi przy zamku
    public boolean isNextToCastle(Vector2d position) {
        return checkIfIsNearbyCastle(position);
    }

    // sprawdzanie czy enemy stoi przy jakiejś wierzy
    public boolean isNextToTower(Vector2d position) {
        for (Tower tower : this.listOfTowers) {
            if (tower.isNextTo(position)) return true;
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
            if (nearestEnemy != null) {
                strength = tower.getStrength();
                value = random.nextInt(5 * strength);
                nearestEnemy.subtractHealth(value);
                score += 10;
            }
        }
    }

    // usuwanie zabitych wrogów
    public void deleteDeadEnemies() {
        ArrayList<Enemy> tmp = new ArrayList<>();
        for (Enemy enemy : listOfEnemies) {
            if (enemy.getHealth() <= 0) {
                tmp.add(enemy);
                this.deadEnemies[enemy.getType()] += 1;
                if (enemy.getType() == 0) {
                    money += 50;
                    score += 50;
                } else if (enemy.getType() == 1) {
                    money += 70;
                    score += 70;
                } else if (enemy.getType() == 2) {
                    money += 100;
                    score += 100;
                }
            }
        }
        for (Enemy enemy : tmp) {
            Vector2d pos = enemy.getPosition();
            this.enemies.get(pos).remove(enemy);
            this.listOfEnemies.remove(enemy);
            enemy.removeObserver(this);
        }
    }

    // usuwanie zniszczonych wież
    public void deleteDeadTowers() {
        ArrayList<Tower> tmp = new ArrayList<>();
        for (Tower tower : listOfTowers) {
            if (tower.getHealth() <= 0) {
                tmp.add(tower);
                score -= 50;
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
        if (listOfTowers.size() > 0) {
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
        if (this.waveIndex < this.waveSizes.length) {  //
            if (this.spawnCountdown <= 0) {
                for (int i = 0; i < this.waveSizes[waveIndex].length; i++) {
                    for (int j = 0; j < this.waveSizes[waveIndex][i]; j++)
                        this.placeEnemy(i);
                }
                this.initialSpawnCountdown -= 2;
                this.spawnCountdown = this.initialSpawnCountdown;
                this.waveIndex += 1;
            } else {
                this.spawnCountdown -= 2;
            }
        }
    }

    public Castle getCastle() {
        return this.castle;
    }

    // dodawanie wieży
    public Tower getNewTower(Vector2d upperLeft, int type) {
        Vector2d low = new Vector2d(upperLeft.x, upperLeft.y - 2);
        Vector2d high = new Vector2d(upperLeft.x + 2, upperLeft.y);
        Tower tower;
        if (type == 1) tower = new Tower(250, 5, 2, low, high, 1);
        else tower = new Tower(300, 10, 3, low, high, 2);
        return tower;
    }

    public void addNewTower(Tower tower) {
        this.newTowers.add(tower);
    }

    // poruszanie przeciwników - jeden ruch
    public boolean buildingAt(Vector2d position) {
        if (floodMode && this.cells != null && this.cells.contains(position)) return true;

        for (Wall wall: this.walls){
            if (wall.getPosition() == position) return true;
        }

        for (Tower tower : this.listOfTowers) { // sprawdzanie czy jakaś wieża tam nie stoi
            Vector2d low = tower.getLowerLeft();
            Vector2d high = tower.getUpperRight();
            if (position.x >= low.x && position.x <= high.x && position.y >= low.y && position.y <= high.y) return true;
        }
        Vector2d low = castle.getLowerRight(); // sprawdzanie czy tam nie stoi zamek
        Vector2d high = castle.getUpperLeft();
        return position.x >= high.x && position.x <= low.x && position.y >= low.y && position.y <= high.y;
    }

    @Override
    public void positionChanged(Vector2d oldPos, Vector2d newPos, Enemy enemy) {
        LinkedList<Enemy> OldPlace = this.enemies.get(oldPos);
        this.enemies.computeIfAbsent(newPos, k -> new LinkedList<>());
        LinkedList<Enemy> NewPlace = this.enemies.get(newPos);
        OldPlace.remove(enemy);
        NewPlace.add(enemy);
    }

    // wyszukiwanie najbliższego obiektu dla wroga
    public static boolean isValid(Vector2d v, GameMap map) {
        return !map.buildingAt(v) && v.x >= map.upperLeft.x && v.x <= map.lowerRight.x && v.y >= map.lowerRight.y && v.y <= map.upperLeft.y;
    }

    public int BFS(Vector2d s, Vector2d destination) {
        boolean[][] visited = new boolean[this.lowerRight.x + 1][this.upperLeft.y + 1];
        int[][] dist = new int[this.lowerRight.x + 1][this.upperLeft.y + 1];
        LinkedList<Vector2d> queue = new LinkedList<>();
        visited[s.x][s.y] = true;
        queue.add(s);
        dist[s.x][s.y] = 0;
        Vector2d p;

        while (queue.size() != 0) {
            p = queue.poll();
            if (p.x == destination.x && p.y == destination.y) return dist[p.x][p.y];
            ArrayList<Vector2d> next_vectors = new ArrayList<>();
            int x = p.x;
            int y = p.y;
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

                if (isValid(next_step, this) && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    queue.add(next_step);
                    dist[nx][ny] = dist[p.x][p.y] + 1;
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    public int shortestDist(Vector2d start, int startX, int endX, int startY, int endY) {
        int dist = Integer.MAX_VALUE;
        if (startX - 1 >= 0){
            for (int col = startY; col <= endY; col ++){
                int newDist = BFS(start, new Vector2d(startX-1,col));
                if (newDist < dist) dist = newDist;
            }
        }
        if (endX + 1 <= this.lowerRight.x){
            for (int col = startY; col <= endY; col ++){
                int newDist = BFS(start, new Vector2d(startX+1,col));
                if (newDist < dist) dist = newDist;
            }
        }
        if (startY - 1 >= 0){
            for (int row = startX; row <= endX; row++){
                int newDist = BFS(start, new Vector2d(row, startY - 1));
                if (newDist < dist) dist = newDist;
            }
        }
        if (startY + 1 <= this.upperLeft.y){
            for (int row = startX; row <= endX; row++){
                int newDist = BFS(start, new Vector2d(row, startY + 1));
                if (newDist < dist) dist = newDist;
            }
        }
        return dist;
    }


    public IMapElement findNearestObject(Enemy enemy) {
        IMapElement object = this.castle;
        Vector2d lowC = this.castle.getLowerLeft();
        Vector2d highC = this.castle.getUpperRight();
        Vector2d position = enemy.getPosition();
        int dist = shortestDist(position, lowC.x, highC.x, lowC.y, highC.y);
        int tmp;
        for (Tower tower : this.listOfTowers) {
            Vector2d low = tower.getLowerLeft();
            Vector2d high = tower.getUpperRight();
            tmp = shortestDist(position, low.x, high.x, low.y, high.y);
            if (tmp < dist) {
                dist = tmp;
                object = tower;
            }
        }
        return object;
    }

    public void moveAll() {
        for (Enemy enemy : this.listOfEnemies) {
            boolean flag = false;
            for (Wall wall: this.walls){
                if (isNextToWall(enemy.getPosition(), wall)) flag = true;
            }
            if (!isNextToCastle(enemy.getPosition()) && !isNextToTower(enemy.getPosition()) && !enemy.getMadeMove() && !flag) {
                enemy.move();
                enemy.changeMadeMove(true);
            }
        }
    }

    public void removeMoves() {
        for (Enemy enemy : listOfEnemies) {
            enemy.changeMadeMove(false);
        }
    }

    // flood mode
    public ArrayList<Vector2d> generateFloodVectors() {
        int number = 100;
        ArrayList<Vector2d> cells = new ArrayList<>();
        while (number > 0) {
            int x = (int) Math.floor(Math.random() * (this.lowerRight.x - this.upperLeft.x + 1) + this.upperLeft.x);
            int y = (int) Math.floor(Math.random() * (this.upperLeft.y - this.lowerRight.y + 1) + this.lowerRight.y);
            Vector2d pos = new Vector2d(x, y);
            if (!buildingAt(pos) && !cells.contains(pos)) {
                number -= 1;
                cells.add(pos);

            }
        }
        return cells;
    }

    public boolean checkIfCanPlaceTower(Tower tower) {
        boolean flag = true;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                flag = flag && !buildingAt(tower.getUpperLeft().add(new Vector2d(i, j)));
            }
        }
        Vector2d low = tower.getLowerLeft();
        Vector2d high = tower.getUpperRight();
        flag = low.y >= this.lowerRight.y && high.y <= this.upperLeft.y && low.x >= this.upperLeft.x && high.x <= this.lowerRight.x;
        return flag;
    }

    public void addTowers(){
        if (this.newTowers.size()!=0) {
            for (Tower tower : this.newTowers) {
                towers.put(tower.getUpperLeft(), tower);
                listOfTowers.add(tower);
                score += 70;
            }
            this.newTowers.clear();
        }
    }
    // potrzebne do gui/Shop
    public boolean castleAt(Vector2d v){
        for (int row = castle.getLowerLeft().x; row <= castle.getLowerRight().x; row++){
            for (int col = castle.getLowerLeft().y; col <= castle.getUpperLeft().y; col++){
                if (v.equals(new Vector2d(row,col))) return true;
            }
        }
        return false;
    }

    public Tower towerAt(Vector2d v){
        for (Tower tower: this.listOfTowers){
            if (v.equals(tower.getUpperLeft())) return tower;
        }
        return null;
    }
    
    // sprzedawanie wież
    public void sellTowers(){
        if (this.soldTowers.size() > 0){
            for (Tower tower: this.soldTowers){
                listOfTowers.remove(tower);
                towers.remove(tower.getUpperLeft());
                this.increaseMoney(tower.getCurrentPriceToSell());
                score += tower.getCurrentPriceToSell();
            }
        }
        this.soldTowers.clear();
    }

    public void increaseMoney(int value){
        this.money += value;
    }
    public void sellTower(Tower tower){
        this.soldTowers.add(tower);
    }

    public int getWaveIndex(){
        return this.waveIndex;
    }
    public int[] getDeadEnemies(){
        return this.deadEnemies;
    }

    // mur
    public void setWallStart(Vector2d start){
        this.startWall = start;
    }

    public void addWall(Vector2d v){
        this.newWalls.add(new Wall(25, v));
    }

    public void addWalls() {
        if (this.newWalls.size() > 0){
            this.walls.addAll(this.newWalls);
            newWalls.clear();
            score += 3;
        }
    }

    public void removeWalls() {
        ArrayList<Wall> tmp = new ArrayList<>();
        for (Wall wall: this.walls){
            if (wall.getHealth() <= 0) tmp.add(wall);
        }
        for (Wall wall: tmp){
            this.walls.remove(wall);
        }
    }

    public boolean isNextToWall(Vector2d v, Wall wall){
        int x = v.x;
        int y = v.y;
        int wx = wall.getPosition().x;
        int wy = wall.getPosition().y;
        return (x + 1 == wx && y == wy) || (x - 1 == wx && y == wy) || (x == wx && y + 1 == wy) || (x == wx && y - 1 == wy);
    }

    public void attackWalls() {
        Random random = new Random();
        int value;
        int strength;
        Enemy enemy;
        if (walls.size() > 0) {
            for (Wall wall:walls) {
                ArrayList<Enemy> attackingEnemies = findAttackingWall(wall);
                for (Enemy item : attackingEnemies) {
                    enemy = item;
                    if (!enemy.getMadeHit()) {
                        strength = enemy.getStrength();
                        value = random.nextInt(5 * strength);
                        wall.subtractHealth(value);
                        enemy.changeMadeHit(true);
                    }
                }
            }
        }
    }

    public int getScore() {
        this.score += money;
        return this.score;
    }
}