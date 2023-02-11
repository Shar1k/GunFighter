package ru.myitschool.satspaceshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MyGG extends Game {
	public static final float SCR_WIDTH = 720, SCR_HEIGHT = 1280;
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;

	ScreenIntro screenIntro;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();

		screenIntro = new ScreenIntro(this);
		setScreen(screenIntro);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
