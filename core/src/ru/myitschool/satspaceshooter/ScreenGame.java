package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
    MyGG gg;

    Texture imgBackGround;
    Texture imgFighter;
    Texture imgFat;
    Texture imgHorse;
    Texture imgEnemy;
    Texture imgShot;
    Music fightMus = Gdx.audio.newMusic(Gdx.files.internal("fight1.mp3"));
    Sound shotsnd = Gdx.audio.newSound(Gdx.files.internal("shotsnd.wav"));

    TextButton btnPlay, btnExit;

    long timeEnemyLastSpawn, timeEnemySpawnInterval = 3000;
    long timeHorseLastSpawn, timeHorseSpawnInterval = 6000;
    long timeFatLastSpawn, timeFatSpawnInterval = 8000;
    Fighter fighter;

    ArrayList<Enemy> enemies = new ArrayList<>();

    ArrayList<Horse> horses = new ArrayList<>();
    ArrayList<Shot> shots = new ArrayList<>();

    ArrayList<EnemyFat> fats = new ArrayList<>();
    long timeShotLastSpawn, timeShotSpawnInterval = 1000;

    Player[] players = new Player[10];

    boolean pause;
    boolean gameOver;

    int frags;


    public ScreenGame(MyGG myGG){
        gg = myGG;
        imgHorse = new Texture("horse.png");
        imgFat = new Texture("enemyFat.png");
        imgBackGround = new Texture("fightbg.jpg");
        imgFighter = new Texture("fighter2.png");
        imgEnemy = new Texture("enemy2.png");
        imgShot = new Texture("shot.png");

        btnPlay = new TextButton(gg.fontLarge, "ИГРАТЬ", 100, 300);
        btnExit = new TextButton(gg.fontLarge, "ВЫХОД", 400, 300);

        // создаём игроков
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Noname", 0);
        }
        Player.loadTableOfRecords(players);
        newGame();
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        pause = false;
        if(gg.musicOn) fightMus.play();
    }

    @Override
    public void render(float delta) {
        // касания экрана и клики мыши
        if(Gdx.input.isTouched()){
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);
            fighter.hit(gg.touch.x, gg.touch.y);
            if(btnPlay.hit(gg.touch.x, gg.touch.y)) {
                newGame();
            }
            if(btnExit.hit(gg.touch.x, gg.touch.y)) {
                newGame();
                gg.setScreen(gg.screenIntro);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            gg.setScreen(gg.screenIntro);
        }

        // события
        if(!pause) {
            if(!gameOver) {
                fighter.move();
                spawnShot();
                spawnEnemy();
                spawnHorse();
                spawnFat();
            }
            for (int i = enemies.size()-1; i >= 0 ; i--) {
                enemies.get(i).move();
                if (enemies.get(i).outOfBounds()) {
                  killShip();
                  enemies.remove(i);
                }
            }
            for (int i = horses.size()-1; i >= 0 ; i--) {
                horses.get(i).move();
                if (horses.get(i).outOfBounds()) {
                    killShip();
                    horses.remove(i);
                }
            }
            for (int i = fats.size()-1; i >= 0 ; i--) {
                fats.get(i).move();
                if (fats.get(i).outOfBounds()) {
                    killShip();
                    fats.remove(i);
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
                        break;
                    }
                }
                for (int h = horses.size()-1; h >= 0; h--) {
                    if(shots.get(i).overlap(horses.get(h))){
                        shots.remove(i);
                        horses.remove(h);
                        break;
                    }
                }
                for (int h = fats.size()-1; h >= 0; h--) {
                    if(shots.get(i).overlap(fats.get(h))){
                        shots.remove(i);
                        fats.remove(h);
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
        for(EnemyFat : enemies) gg.batch.draw(imgEnemy, enemy.getX(), enemy.getY(), enemy.width, enemy.height);
        for(Horse horse: horses) gg.batch.draw(imgHorse, horse.getX(), horse.getY(), horse.width, horse.height);
        for(Shot shot: shots) gg.batch.draw(imgShot, shot.getX(), shot.getY(), shot.width, shot.height);
        gg.batch.draw(imgFighter, fighter.getX(), fighter.getY(), fighter.width, fighter.height);
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
        imgFighter.dispose();
        imgEnemy.dispose();
        imgHorse.dispose();
        imgShot.dispose();
        imgFat.dispose();
        imgBackGround.dispose();
    }

    void spawnEnemy(){
        if(TimeUtils.millis() > timeEnemyLastSpawn+timeEnemySpawnInterval) {
            enemies.add(new Enemy());
            timeEnemyLastSpawn = TimeUtils.millis();
        }
        if(TimeUtils.millis() > timeHorseLastSpawn+timeHorseSpawnInterval) {
            horses.add(new Horse());
            timeHorseLastSpawn = TimeUtils.millis();
        }
        if(TimeUtils.millis() > timeFatLastSpawn+timeFatSpawnInterval) {
            fats.add(new EnemyFat());
            timeFatLastSpawn = TimeUtils.millis();
        }
    }


    void spawnShot(){
        if(TimeUtils.millis() > timeShotLastSpawn+timeShotSpawnInterval) {
            shots.add(new Shot(fighter.x, fighter.y));
            if (gg.soundOn) shotsnd.play();
            timeShotLastSpawn = TimeUtils.millis();
        }
    }


    void killShip(){
        fighter.kill();
        if(fighter.lives == 0) gameOver();
    }

    void newGame(){
        fighter = new Fighter(SCR_WIDTH/2, 100, 100, 100);
        imgFighter = new Texture("fighter1.png");
        enemies.clear();
        horses.clear();
        shots.clear();
        fats.clear();
        gameOver = false;
        frags = 0;
    }

    void gameOver(){
        gameOver = true;
        imgFighter = new Texture("fighter3.png");
        players[players.length-1].name = gg.playerName;
        players[players.length-1].frags = frags;
        Player.sortTableOfRecords(players);
        Player.saveTableOfRecords(players);
    }
}