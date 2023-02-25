package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
    MyGG gg;
    Texture imgStars;
    Texture imgShip;
    Texture imgEnemy;

    Sky[] skies = new Sky[2];
    Ship ship;
    ArrayList<Enemy> enemies = new ArrayList<>();

    boolean isGyroscopeAvailable;
    boolean isAccelerometerAvailable;

    long timeEnemyLastSpawn, timeEnemySpawnInterval = 1500;

    public ScreenGame(MyGG myGG){
        gg = myGG;
        imgStars = new Texture("stars.png");
        imgShip = new Texture("ship.png");
        imgEnemy = new Texture("enemy.png");
        skies[0] = new Sky(SCR_WIDTH/2, SCR_HEIGHT/2);
        skies[1] = new Sky(SCR_WIDTH/2, SCR_HEIGHT+SCR_HEIGHT/2);

        isAccelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        isGyroscopeAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        ship = new Ship(SCR_WIDTH/2, 150, 100, 100);
    }

    @Override
    public void render(float delta) {
        // касания экрана и клики мыши
        if(Gdx.input.isTouched()){
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);
            ship.hit(gg.touch.x, gg.touch.y);
        } else if(isAccelerometerAvailable) {
            ship.vx = -Gdx.input.getAccelerometerX()*10;
        } else if(isGyroscopeAvailable) {
            ship.vx = Gdx.input.getGyroscopeY()*10;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            gg.setScreen(gg.screenIntro);
        }

        // события
        for (Sky sky: skies) sky.move();
        ship.move();
        spawnEnemy();
        for(Enemy e: enemies) e.move();

        // отрисовка всего
        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        for (Sky sky: skies) gg.batch.draw(imgStars, sky.getX(), sky.getY(), sky.width, sky.height);
        for(Enemy enemy: enemies) gg.batch.draw(imgEnemy, enemy.getX(), enemy.getY(), enemy.width, enemy.height);
        gg.batch.draw(imgShip, ship.getX(), ship.getY(), ship.width, ship.height);
        gg.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
    }

    @Override
    public void dispose() {
        imgStars.dispose();
        imgShip.dispose();
    }

    void spawnEnemy(){
        if(TimeUtils.millis() > timeEnemyLastSpawn+timeEnemySpawnInterval) {
            enemies.add(new Enemy());
            timeEnemyLastSpawn = TimeUtils.millis();
        }
    }
}
