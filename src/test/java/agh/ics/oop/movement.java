package agh.ics.oop;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class movement {

    @Test
    public void Test1(){
        GameMap map1 = new GameMap(new Vector2d(69,0),new Vector2d(0,39), 1000, 1, false, false);
        for (int i = 0; i < map1.waveSizes[0].length; i++){
            for(int j = 0; j < map1.waveSizes[0][i]; j++)
                map1.placeEnemy(i);
        }
        ArrayList<Vector2d> positions1 = new ArrayList<>();
        for (Enemy enemy: map1.listOfEnemies){
            positions1.add(enemy.getPosition());
        }
        map1.moveAll();
        int i = 0;
        for (Enemy enemy: map1.listOfEnemies){
            Vector2d newPos = enemy.getPosition();
            Vector2d oldPos = positions1.get(i);
            ArrayList<Vector2d> next_vectors = new ArrayList<>();
            int x = oldPos.x;
            int y = oldPos.y;
            next_vectors.add(new Vector2d(x, y - 1));
            next_vectors.add(new Vector2d(x, y + 1));
            next_vectors.add(new Vector2d(x - 1, y));
            next_vectors.add(new Vector2d(x + 1, y));
            next_vectors.add(new Vector2d(x - 1, y + 1));
            next_vectors.add(new Vector2d(x - 1, y - 1));
            next_vectors.add(new Vector2d(x + 1, y + 1));
            next_vectors.add(new Vector2d(x + 1, y - 1));
            if(next_vectors.contains(newPos)) {
                int idx = next_vectors.indexOf(newPos);
                assertEquals(newPos, next_vectors.get(idx));
            }
            i += 1;
        }

    }

    @Test public void Test2() {
        GameMap map1 = new GameMap(new Vector2d(69,0),new Vector2d(0,39), 1000, 1, false, false);
        for (int i = 0; i < map1.waveSizes[0].length; i++){
            for(int j = 0; j < map1.waveSizes[0][i]; j++)
                map1.placeEnemy(i);
        }
        System.out.println(map1.listOfEnemies.size());
        map1.enemiesWave();
        System.out.println(map1.listOfEnemies.size());

        ArrayList<Vector2d> positions2 = new ArrayList<>();
        for (Enemy enemy: map1.listOfEnemies){
            positions2.add(enemy.getPosition());
        }

        map1.moveAll();

        int i2 = 0;
        for (Enemy enemy: map1.listOfEnemies){
            Vector2d newPos = enemy.getPosition();
            Vector2d oldPos = positions2.get(i2);
            ArrayList<Vector2d> next_vectors = new ArrayList<>();
            int x = oldPos.x;
            int y = oldPos.y;
            next_vectors.add(new Vector2d(x, y - 1));
            next_vectors.add(new Vector2d(x, y + 1));
            next_vectors.add(new Vector2d(x - 1, y));
            next_vectors.add(new Vector2d(x + 1, y));
            next_vectors.add(new Vector2d(x - 1, y + 1));
            next_vectors.add(new Vector2d(x - 1, y - 1));
            next_vectors.add(new Vector2d(x + 1, y + 1));
            next_vectors.add(new Vector2d(x + 1, y - 1));
            if(next_vectors.contains(newPos)) {
                int idx = next_vectors.indexOf(newPos);
                assertEquals(newPos, next_vectors.get(idx));
            }
            i2 += 1;
        }
    }


}
