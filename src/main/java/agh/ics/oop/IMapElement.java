package agh.ics.oop;

public interface IMapElement {
    Vector2d getPosition();
    String toString();
    String getPath(IMapElement object);
    int getHealth();
    double maxHealth();
}
