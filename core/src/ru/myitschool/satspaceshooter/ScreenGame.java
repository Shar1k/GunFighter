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
    Music fightMus = Gdx.audio.newMusic(Gdx.files.internal("fight1.mp3"));

    TextButton btnPlay, btnExit;

    boolean isGyroscopeAvailable;
    boolean isAccelerometerAvailable;

    Player[] players = new Player[10];

    boolean pause;
    boolean gameOver;

    public ScreenGame(MyGG myGG){
        gg = myGG;
        imgBackGround = new Texture("rev.png");
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
            if(btnPlay.hit(gg.touch.x, gg.touch.y)) {
                newGame();
            }
            if(btnExit.hit(gg.touch.x, gg.touch.y)) {
                newGame();
                gg.setScreen(gg.screenIntro);
            }
        } else if(isAccelerometerAvailable) {
        } else if(isGyroscopeAvailable) {
        }
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            gg.setScreen(gg.screenIntro);
        }

        // события
        if(!pause) {


        }

        // отрисовка всего
        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        gg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);

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
        imgBackGround.dispose();
        fightMus.dispose();

    }

    void newGame(){

        gameOver = false;
    }

    void gameOver(){
        gameOver = true;
        players[players.length-1].name = gg.playerName;
        Player.sortTableOfRecords(players);
        Player.saveTableOfRecords(players);
    }
}
