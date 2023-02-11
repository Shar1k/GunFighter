package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

public class SpaceObject {
    float x, y;
    float width, height;
    float vx, vy;

    public SpaceObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    void move() {
        x += vx;
        y += vy;
    }

    boolean outOfBounds(){
        return x<-width/2 || x>SCR_WIDTH+width/2 || y<-height/2 || y>SCR_HEIGHT+height/2;
    }
}
