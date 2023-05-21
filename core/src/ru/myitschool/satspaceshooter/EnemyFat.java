package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class EnemyFat extends Object{
    ScreenGame sg;
    public EnemyFat() {
        super(0, 0, 130, 130);
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height/2, SCR_HEIGHT*2);
        vy = MathUtils.random(-2f, -1f);
        lives = 5;
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


