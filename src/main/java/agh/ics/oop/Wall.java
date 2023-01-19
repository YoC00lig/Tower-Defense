package agh.ics.oop;

public class Wall {
    private Vector2d lowerRight;
    private Vector2d upperLeft;
    private int health;

    /*
    wymiary:
    10x1 i 1x10 dla zamku
    3x1 i 1x3 dla wieży
    1x1? jako np dodatkowe przeszkody?
    chyba, że da się zrobić tak aby gracz kładł kilka 1x1 obok siebie,
    ale tylko pionowo albo poziomo (w sensie, żeby nie stworzyć kwadratu)
    i potem te które się stykają łączyć w mur, o health tyle ile tych 1x1
    i on niszczy się cały razem, a nie pojedynczo 1x1
     */

    public Wall(int health, Vector2d lowerRight, Vector2d upperLeft){
        this.health = health;
        this.lowerRight = lowerRight;
        this.upperLeft = upperLeft;
    }

    public int getHealth(){
        return this.health;
    }

    public Vector2d getLowerRight(){
        return this.lowerRight;
    }

    public Vector2d getUpperLeft(){
        return this.upperLeft;
    }

    public void subtractHealth(int value){
        this.health -= value;
    }

    public String toString(){
        return "w";
    }
}
