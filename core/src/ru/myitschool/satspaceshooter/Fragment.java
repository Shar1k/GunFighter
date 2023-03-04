package ru.myitschool.satspaceshooter;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    int type;

    public Fragment(float x, float y) {
        super(x, y, MathUtils.random(10, 25), MathUtils.random(10, 25));
        vx = MathUtils.random(-8f, 8);
        vy = MathUtils.random(-8f, 8);
        type = MathUtils.random(0, 3);
    }
}
