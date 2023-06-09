package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Enemy extends Object{
    ScreenGame sg;
    public Enemy() {
        super(0, 0, 130, 130);
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height/2, SCR_HEIGHT*2);
        vy = MathUtils.random(-2f, -1f);
        int i = 0;
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


