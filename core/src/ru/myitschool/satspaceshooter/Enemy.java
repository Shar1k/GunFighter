package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.*;

import com.badlogic.gdx.math.MathUtils;

public class Enemy extends SpaceObject{
    public Enemy() {
        super(0, 0, 100, 100);
        x = SCR_WIDTH/2;
        y = SCR_HEIGHT - 100;
        vy = 0;
    }

    @Override
    boolean outOfBounds() {
        return y<-height/2;
    }
}
