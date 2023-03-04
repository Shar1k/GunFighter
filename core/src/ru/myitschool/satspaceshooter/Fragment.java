package ru.myitschool.satspaceshooter;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    int type;
    float a, v;

    public Fragment(float x, float y) {
        super(x, y, MathUtils.random(10, 25), MathUtils.random(10, 25));
        a = MathUtils.random(0f, 360);
        v = MathUtils.random(1f, 8);
        vx = v*MathUtils.sin(a);
        vy = v*MathUtils.cos(a);
        type = MathUtils.random(0, 3);
    }
}
