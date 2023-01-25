package agh.ics.oop;

import java.util.*;

public class Enemy implements IMapElement {
    private int health;
    GameMap map;
    private int strength;
    private Vector2d position;
    protected List<IPositionChangeObserver> observers = new ArrayList<>();
    private boolean madeHit; // czy w ruchu juz uderzył
    ArrayList<Vector2d> steps;
    private int nextMove;
    private IMapElement destination;
    private boolean madeMove;
    private int type;
    public double maxHealth;

    public Enemy(int health, int strength, Vector2d position, GameMap map, int type) {
        this.health = health;
        this.strength = strength;
        this.position = position;
        this.map = map;
        this.madeHit = false;
        this.steps = BFS(this.position);
        this.nextMove = 1;
        this.destination = map.getCastle();
        this.madeMove = false;
        this.type = type;
        this.maxHealth = health;
    }

    public void subtractHealth(int value) {
        this.health -= value;
    }

    public int getHealth() {
        return this.health;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public int getStrength() {
        return this.strength;
    }

    public boolean getMadeHit() {
        return this.madeHit;
    }

    public void changeMadeHit(boolean x) {
        this.madeHit = x;
    }
    public boolean getMadeMove() {
        return this.madeMove;
    }
    public void changeMadeMove(boolean x) {
        this.madeMove = x;
    }
    public int getType(){
        return this.type;
    }

    public String toString() {
        return "E";
    }

    @Override
    public String getPath(IMapElement object) {
        if (this.getType() == 0)  return "src/main/resources/enemy1.png";
        else if (this.getType() == 1)  return "src/main/resources/enemy2.png";
        else return "src/main/resources/enemy3.png";
    }

    public static boolean isValid(Vector2d v, GameMap map) {
        return !map.buildingAt(v) && v.x >= map.upperLeft.x && v.x <= map.lowerRight.x && v.y >= map.lowerRight.y && v.y <= map.upperLeft.y;
    }

    // odtwarzanie ścieżki
    private ArrayList<Vector2d> backtrace(Vector2d start, Vector2d end, Map<Vector2d, Vector2d> parents) {
        ArrayList<Vector2d> result = new ArrayList<>();
        result.add(end);
        Vector2d prev = parents.get(end);
        Vector2d new_prev;
        while (!(prev == start)) {
            result.add(prev);
            new_prev = parents.get(prev);
            prev = new_prev;
        }
        result.add(start);
        Collections.reverse(result);
        return result;
    }

    // wyszukuje najkrótszą ścieżkę z s do destination i zwraca kolejne punkty w formie tablicy ArrayList,
    // jesli nie istnieje taka ścieżka to zwracana tablica jest pusta.
    public ArrayList<Vector2d> BFS(Vector2d s) {
        boolean[][] visited = new boolean[70][40];
        Map<Vector2d, Vector2d> parents = new HashMap<>();
        LinkedList<Vector2d> queue = new LinkedList<>();
        visited[s.x][s.y] = true;
        queue.add(s);
        Vector2d p;

        while (queue.size() != 0) {
            p = queue.poll();
            if (map.isNextToCastle(p) || map.isNextToTower(p)) {
                return backtrace(s, p, parents);
            }

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

            Collections.shuffle(next_vectors);

            for (Vector2d next_step : next_vectors) {
                int nx = next_step.x;
                int ny = next_step.y;

                if (isValid(next_step, map) && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    parents.put(next_step, p);
                    queue.add(next_step);
                }
            }
        }
        return new ArrayList<>();
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public void move() {
        IMapElement building = this.map.findNearestObject(this);
        if (building != this.destination) {
            this.steps = BFS(this.position);
            this.nextMove = 1;
            this.destination = building;
            Vector2d newPosition = steps.get(this.nextMove);
            positionChanged(this.position, newPosition);
            this.position = newPosition;
            this.nextMove += 1;
        } else if (this.nextMove < this.steps.size()) {
            Vector2d newPosition = steps.get(this.nextMove);
            positionChanged(this.position, newPosition);
            this.position = newPosition;
            this.nextMove += 1;
        }
    }

    public double maxHealth() {
        return this.maxHealth;
    }
}
