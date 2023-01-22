package agh.ics.oop;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldpos, Vector2d newPos, Enemy enemy);

}