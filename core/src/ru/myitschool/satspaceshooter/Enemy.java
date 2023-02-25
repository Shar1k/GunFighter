package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.*;

import com.badlogic.gdx.math.MathUtils;

public class Enemy extends SpaceObject{
    public Enemy() {
        super(0, 0, 100, 100);
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height/2, SCR_HEIGHT*2);
        vy = MathUtils.random(-6f, -3);
    }

    @Override
    void move() {
        super.move();
    }
}
