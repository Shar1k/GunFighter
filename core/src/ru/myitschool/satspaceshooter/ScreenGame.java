package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
    MyGG gg;
    Texture imgStars;
    Texture imgShip;
    Texture imgEnemy;
    Texture imgShot;
    Texture imgAtlasFragment;
    TextureRegion[] imgFragment = new TextureRegion[4];

    Sound sndShot, sndExplosion;

    Sky[] skies = new Sky[2];
    Ship ship;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Shot> shots = new ArrayList<>();
    ArrayList<Fragment> fragments = new ArrayList<>();

    TextButton btnPlay, btnExit;

    boolean isGyroscopeAvailable;
    boolean isAccelerometerAvailable;

    long timeEnemyLastSpawn, timeEnemySpawnInterval = 1000;
    long timeShotLastSpawn, timeShotSpawnInterval = 500;

    Player[] players = new Player[10];

    boolean pause;
    boolean gameOver;
    int frags;

    public ScreenGame(MyGG myGG){
        gg = myGG;
        imgStars = new Texture("stars.png");
        imgShip = new Texture("ship.png");
        imgEnemy = new Texture("enemy.png");
        imgShot = new Texture("shot.png");
        imgAtlasFragment = new Texture("fragment.png");
        imgFragment[0] = new TextureRegion(imgAtlasFragment, 0, 0, 200, 200);
        imgFragment[1] = new TextureRegion(imgAtlasFragment, 200, 0, 200, 200);
        imgFragment[2] = new TextureRegion(imgAtlasFragment, 0, 200, 200, 200);
        imgFragment[3] = new TextureRegion(imgAtlasFragment, 200, 200, 200, 200);

        sndShot = Gdx.audio.newSound(Gdx.files.internal("blaster.wav"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

        btnPlay = new TextButton(gg.fontLarge, "ИГРАТЬ", 100, 300);
        btnExit = new TextButton(gg.fontLarge, "ВЫХОД", 400, 300);

        skies[0] = new Sky(SCR_WIDTH/2, SCR_HEIGHT/2);
        skies[1] = new Sky(SCR_WIDTH/2, SCR_HEIGHT+SCR_HEIGHT/2);

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
            for (Sky sky : skies) sky.move();
            if(!gameOver) {
                ship.move();
            }
            if(ship.isVisible) {
                spawnShot();
            }
            spawnEnemy();

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
                        spawnFragments(enemies.get(j).x, enemies.get(j).y);
                        shots.remove(i);
                        enemies.remove(j);
                        if(gg.soundOn) sndExplosion.play();
                        frags++;
                        break;
                    }
                }
            }
        }
        for (int i = fragments.size()-1; i >= 0 ; i--) {
            fragments.get(i).move();
            if (fragments.get(i).outOfBounds()) {
                fragments.remove(i);
            }
        }

        // отрисовка всего
        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        for(Sky sky: skies) gg.batch.draw(imgStars, sky.getX(), sky.getY(), sky.width, sky.height);
        for(Fragment frag: fragments)
            gg.batch.draw(imgFragment[frag.type], frag.getX(), frag.getY(), frag.width/2, frag.height/2,
                    frag.width, frag.height, 1, 1, frag.angle);
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
    }

    @Override
    public void dispose() {
        imgStars.dispose();
        imgShip.dispose();
        imgAtlasFragment.dispose();
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

    void spawnFragments(float x, float y){
        for (int i = 0; i < 30; i++) {
            fragments.add(new Fragment(x, y));
        }
    }

    void killShip(){
        spawnFragments(ship.x, ship.y);
        ship.kill();
        if(gg.soundOn) sndExplosion.play();
        if(ship.lives == 0) gameOver();
    }

    void newGame(){
        ship = new Ship(SCR_WIDTH/2, 100, 100, 100);
        enemies.clear();
        fragments.clear();
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
