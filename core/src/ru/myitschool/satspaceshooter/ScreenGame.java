package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
    MyGG gg;
    Texture imgStars;
    Texture imgShip;
    Texture imgEnemy;
    Texture imgShot;

    Sound sndShot, sndExplosion;

    Sky[] skies = new Sky[2];
    Ship ship;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Shot> shots = new ArrayList<>();

    boolean isGyroscopeAvailable;
    boolean isAccelerometerAvailable;

    long timeEnemyLastSpawn, timeEnemySpawnInterval = 1500;
    long timeShotLastSpawn, timeShotSpawnInterval = 500;

    boolean pause;

    public ScreenGame(MyGG myGG){
        gg = myGG;
        imgStars = new Texture("stars.png");
        imgShip = new Texture("ship.png");
        imgEnemy = new Texture("enemy.png");
        imgShot = new Texture("shot.png");
        sndShot = Gdx.audio.newSound(Gdx.files.internal("blaster.wav"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

        skies[0] = new Sky(SCR_WIDTH/2, SCR_HEIGHT/2);
        skies[1] = new Sky(SCR_WIDTH/2, SCR_HEIGHT+SCR_HEIGHT/2);

        isAccelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        isGyroscopeAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
        ship = new Ship(SCR_WIDTH/2, 150, 100, 100);
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        pause = false;
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
        if(!pause) {
            for (Sky sky : skies) sky.move();
            ship.move();
            spawnEnemy();
            spawnShot();
            for (int i = enemies.size()-1; i >= 0 ; i--) {
                enemies.get(i).move();
                if (enemies.get(i).outOfBounds()) {
                    enemies.remove(i);
                }
            }
            for (int i = shots.size()-1; i >= 0; i--) {
                shots.get(i).move();
                if (shots.get(i).outOfBounds()) {
                    shots.remove(i);
                }
                for (int j = enemies.size()-1; j >= 0; j--) {
                    if(shots.get(i).overlap(enemies.get(j))){
                        shots.remove(i);
                        enemies.remove(j);
                        if(gg.soundOn) sndExplosion.play();
                    }
                }
            }
        }

        // отрисовка всего
        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        for(Sky sky: skies) gg.batch.draw(imgStars, sky.getX(), sky.getY(), sky.width, sky.height);
        for(Enemy enemy: enemies) gg.batch.draw(imgEnemy, enemy.getX(), enemy.getY(), enemy.width, enemy.height);
        for(Shot shot: shots) gg.batch.draw(imgShot, shot.getX(), shot.getY(), shot.width, shot.height);
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
        pause = true;
    }

    @Override
    public void dispose() {
        imgStars.dispose();
        imgShip.dispose();
        sndExplosion.dispose();
        sndShot.dispose();
    }

    void spawnEnemy(){
        if(TimeUtils.millis() > timeEnemyLastSpawn+timeEnemySpawnInterval) {
            enemies.add(new Enemy());
            timeEnemyLastSpawn = TimeUtils.millis();
        }
    }

    void spawnShot(){
        if(TimeUtils.millis() > timeShotLastSpawn+timeShotSpawnInterval) {
            shots.add(new Shot(ship.x, ship.y));
            timeShotLastSpawn = TimeUtils.millis();
            if(gg.soundOn) sndShot.play();
        }
    }
}
