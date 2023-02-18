package ru.myitschool.satspaceshooter;

import static ru.myitschool.satspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.satspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;

public class ScreenSettings implements Screen {
    MyGG gg;
    Texture imgBackGround;

    TextButton btnName, btnClearRec, btnSound, btnMusic, btnBack;
    String playerName = "Noname";

    public ScreenSettings(MyGG myGG){
        gg = myGG;
        imgBackGround = new Texture("bg/cosmos02.jpg");

        btnName = new TextButton(gg.fontLarge, "Имя: "+playerName, 20, 1100);
        btnClearRec = new TextButton(gg.fontLarge, "Очистка рекордов", 20, 1000);
        btnSound = new TextButton(gg.fontLarge, "Звук вкл", 20, 900);
        btnMusic = new TextButton(gg.fontLarge, "Музыка вкл", 20, 800);
        btnBack = new TextButton(gg.fontLarge, "Назад", 20, 700);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // касания экрана и клики мыши
        if(Gdx.input.justTouched()){
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);
            if(btnName.hit(gg.touch.x, gg.touch.y)) {

            }
            if(btnClearRec.hit(gg.touch.x, gg.touch.y)) {

            }
            if(btnSound.hit(gg.touch.x, gg.touch.y)) {

            }
            if(btnMusic.hit(gg.touch.x, gg.touch.y)) {

            }
            if(btnBack.hit(gg.touch.x, gg.touch.y)) {
                gg.setScreen(gg.screenIntro);
            }
        }

        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        gg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnName.font.draw(gg.batch, btnName.text, 10, btnName.y, SCR_WIDTH-20, Align.center, true);
        btnClearRec.font.draw(gg.batch, btnClearRec.text, 10, btnClearRec.y, SCR_WIDTH-20, Align.center, true);
        btnSound.font.draw(gg.batch, btnSound.text, 10, btnSound.y, SCR_WIDTH-20, Align.center, true);
        btnMusic.font.draw(gg.batch, btnMusic.text, 10, btnMusic.y, SCR_WIDTH-20, Align.center, true);
        btnBack.font.draw(gg.batch, btnBack.text, 10, btnBack.y, SCR_WIDTH-20, Align.center, true);
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

    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
