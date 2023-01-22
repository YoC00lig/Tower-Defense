package agh.ics.oop;
import java.util.*;

public class Enemy implements IMapElement{
    private int health;
    GameMap map;
    private int strength;
    private Vector2d position;
    protected List<IPositionChangeObserver> observers = new ArrayList<>();

    private boolean madeHit; // czy w ruchu juz uderzył
    ArrayList<Vector2d> steps = new ArrayList<>();
    private boolean pathExists;
    private int nextMove;
    private static final int[] row = { -1, 0, 0, 1 };
    private static final int[] col = { 0, -1, 1, 0 };


    public Enemy(int health, int strength, Vector2d position, GameMap map){
        this.health = health;
        this.strength = strength;
        this.position = position;
        this.map = map;
        this.madeHit = false;
        this.steps = BFS(this.position);
        this.nextMove = 1;
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

    @Override
    public String getPath(IMapElement object) {
        return "src/main/resources/archer1.png";
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
            if (map.isNextToCastle(p)) return backtrace(s, p, parents);

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

                if (isValid(next_step,map) && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    parents.put(next_step, p);
                    queue.add(next_step);
                }
            }
        }
        return new ArrayList<>();
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    public void move() {
        if (this.nextMove < steps.size()) {
            Vector2d newPosition = steps.get(this.nextMove);
            positionChanged(this.position, newPosition);
            this.position = steps.get(this.nextMove);
            this.nextMove += 1;
        }

    }
}
