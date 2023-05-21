package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class ScreenAbout implements Screen {
    MyGG gg;
    Texture imgBackGround;

    TextButton btnBack;
    String textAbout =  "";

    Music menuMus = Gdx.audio.newMusic(Gdx.files.internal("menu.mp3"));

    public ScreenAbout(MyGG myGG){
        gg = myGG;
        imgBackGround = new Texture("bgMenu.png");

        btnBack = new TextButton(gg.fontLarge, "ВЫХОД", 100, 200, true);
    }

    @Override
    public void show() {

        if(gg.musicOn) menuMus.play();
    }

    @Override
    public void render(float delta) {
        // касания экрана и клики мыши
        if(Gdx.input.justTouched()){
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);

            if(btnBack.hit(gg.touch.x, gg.touch.y)) {
                gg.setScreen(gg.screenIntro);
            }

        }

        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        gg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        gg.font.draw(gg.batch, textAbout, 30, 1100);
        btnBack.font.draw(gg.batch, btnBack.text, btnBack.x, btnBack.y);
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
    menuMus.stop();
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
