package ru.myitschool.satspaceshooter;


import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Horse extends Object{
    public Horse() {
        super(0, 0, 150, 150);
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height/2, SCR_HEIGHT*2);
        vy = MathUtils.random(-6f, -4f);

    }

    @Override
    void move() {
        outOfBounds();
        y = y + vy;
    }
    @Override
    boolean outOfBounds() {
        return y<-height/2;
    }
}