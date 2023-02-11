package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

public class Sky extends SpaceObject{
    public Sky(float x, float y) {
        super(x, y);
        vy = -1;
        width = SCR_WIDTH;
        height = SCR_HEIGHT;
    }

    @Override
    void move() {
        super.move();
        if(outOfBounds()){
            y = SCR_HEIGHT+height/2;
        }
    }
}
