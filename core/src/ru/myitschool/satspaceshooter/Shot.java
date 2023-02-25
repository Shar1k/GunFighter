package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Shot extends SpaceObject{
    public Shot(float x, float y) {
        super(x, y, 100, 100);
        vy = 10;
    }

}
