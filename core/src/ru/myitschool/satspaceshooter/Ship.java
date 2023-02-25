package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

public class Ship extends SpaceObject{
    public Ship(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    void move() {
        super.move();
        outOfBounds();
    }

    void hit(float tx, float ty) {
        vx = (tx-x)/50;
    }

    @Override
    boolean outOfBounds() {
        if(x < width/2) {
            x = width/2;
            vx = 0;
        }
        if(x > SCR_WIDTH-width/2){
            x = SCR_WIDTH-width/2;
            vx = 0;
        }
        return true;
    }
}
