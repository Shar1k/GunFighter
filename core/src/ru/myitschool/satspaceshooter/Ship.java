package ru.myitschool.satspaceshooter;

public class Ship extends SpaceObject{
    public Ship(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    void hit(float tx, float ty) {
        vx = (tx-x)/50;
    }
}
