package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

public class ScreenGame implements Screen {
    MyGG gg;

    Texture imgBackGround;
    Texture imgShip;
    Texture imgEnemy;
    Texture imgShot;
    Music fightMus = Gdx.audio.newMusic(Gdx.files.internal("fight1.mp3"));

    TextButton btnPlay, btnExit;

    boolean isGyroscopeAvailable;
    boolean isAccelerometerAvailable;

    Ship ship;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Shot> shots = new ArrayList<>();
    long timeShotLastSpawn, timeShotSpawnInterval = 500;

    Player[] players = new Player[10];

    boolean pause;
    boolean gameOver;

    int frags;

    public ScreenGame(MyGG myGG){
        gg = myGG;
        imgBackGround = new Texture("fightbg.jpg");
        imgShip = new Texture("fighter2.png");
        imgEnemy = new Texture("enemy2.png");
        imgShot = new Texture("shot.png");

        btnPlay = new TextButton(gg.fontLarge, "ИГРАТЬ", 100, 300);
        btnExit = new TextButton(gg.fontLarge, "ВЫХОД", 400, 300);

        // создаём игроков
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Noname", 0);
        }
        Player.loadTableOfRecords(players);

        isAccelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        isGyroscopeAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
        newGame();
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        pause = false;

        fightMus.play();
    }

    @Override
    public void render(float delta) {
        // касания экрана и клики мыши
        if(Gdx.input.isTouched()){
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);
            ship.hit(gg.touch.x, gg.touch.y);
            if(btnPlay.hit(gg.touch.x, gg.touch.y)) {
                newGame();
            }
            if(btnExit.hit(gg.touch.x, gg.touch.y)) {
                newGame();
                gg.setScreen(gg.screenIntro);
            }
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
            if(!gameOver) {
                ship.move();
            }
            if(ship.isVisible) {
                spawnShot();
            }

            for (int i = enemies.size()-1; i >= 0 ; i--) {
                enemies.get(i).move();
                if (enemies.get(i).outOfBounds()) {
                    if(ship.isVisible) killShip();
                    enemies.remove(i);
                }
            }
            for (int i = shots.size()-1; i >= 0; i--) {
                shots.get(i).move();
                if (shots.get(i).outOfBounds()) {
                    shots.remove(i);
                    break;
                }
                for (int j = enemies.size()-1; j >= 0; j--) {
                    if(shots.get(i).overlap(enemies.get(j))){
                        shots.remove(i);
                        enemies.remove(j);
                        frags++;
                        break;
                    }
                }
            }
        }


        // отрисовка всего
        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        gg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        for(Enemy enemy: enemies) gg.batch.draw(imgEnemy, enemy.getX(), enemy.getY(), enemy.width, enemy.height);
        for(Shot shot: shots) gg.batch.draw(imgShot, shot.getX(), shot.getY(), shot.width, shot.height);
        if(ship.isVisible) gg.batch.draw(imgShip, ship.getX(), ship.getY(), ship.width, ship.height);
        gg.font.draw(gg.batch, "FRAGS: "+frags, 10, SCR_HEIGHT-10);
        for (int i = 1; i < ship.lives+1; i++) {
            gg.batch.draw(imgShip, SCR_WIDTH-60*i, SCR_HEIGHT-60, 50, 50);
        }
        if(gameOver) {
            gg.font.draw(gg.batch, Player.tableOfRecordsToString(players), 200, SCR_HEIGHT-200);
            btnPlay.font.draw(gg.batch, btnPlay.text, btnPlay.x, btnPlay.y);
            btnExit.font.draw(gg.batch, btnExit.text, btnExit.x, btnExit.y);
        }
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
        fightMus.stop();
    }

    @Override
    public void dispose() {
        imgShip.dispose();
    }


    void spawnShot(){
        if(TimeUtils.millis() > timeShotLastSpawn+timeShotSpawnInterval) {
            shots.add(new Shot(ship.x, ship.y));
            timeShotLastSpawn = TimeUtils.millis();
        }
    }


    void killShip(){
        ship.kill();
        if(ship.lives == 0) gameOver();
    }

    void newGame(){
        ship = new Ship(SCR_WIDTH/2, 100, 100, 100);
        enemies.add(new Enemy());
        shots.clear();
        gameOver = false;
        frags = 0;
    }

    void gameOver(){
        gameOver = true;
        players[players.length-1].name = gg.playerName;
        players[players.length-1].frags = frags;
        Player.sortTableOfRecords(players);
        Player.saveTableOfRecords(players);
    }
}