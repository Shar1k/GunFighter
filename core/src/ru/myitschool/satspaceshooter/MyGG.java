package ru.myitschool.satspaceshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;

public class MyGG extends Game {
	private static final String TAG = "BluetoothPong";
	private IBluetooth bluetoothCom;
	public static final float SCR_WIDTH = 720, SCR_HEIGHT = 1280;
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font, fontLarge;

	ScreenIntro screenIntro;
	ScreenGame screenGame;
	ScreenSettings screenSettings;
	ScreenAbout screenAbout;

	boolean soundOn = true;
	boolean musicOn = true;

	String playerName = "Noname";
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();
		generateFont();

		screenIntro = new ScreenIntro(this);
		screenGame = new ScreenGame(this);
		screenSettings = new ScreenSettings(this);
		screenAbout = new ScreenAbout(this);
		setScreen(screenIntro);
	}

	public void onDisconnect()
	{
	}

	public void onConnected(boolean isHost)
	{
		if( isHost )
		{

		}
		else
		{

		}
	}

	public void setBluetoothInterface(IBluetooth interfaceBluetooth)
	{
		bluetoothCom = interfaceBluetooth;
	}

	private float getFloatFromStr(String str)
	{
		try
		{
			return Float.parseFloat(str);
		}
		catch (NumberFormatException ex)
		{
			return 0.0f;
		}
	}

	public void incomingMessage(String str)
	{
		Gdx.app.log(TAG, str);

		int index,end;
		// Get Opponent Paddle Position from String
		index = str.indexOf("[");
		if(index != -1)
		{
			end = str.indexOf("]");
			if( end > index)
			{

			}
		}

		//Handle Ball Position Update
		index = str.indexOf("{");
		if(index != -1)
		{
			end = str.indexOf("}");
			if( end > index)
			{
				String content = str.substring(index + 1,end);
				Gdx.app.log(TAG, content);

				String[] splited = content.split(":");

			}
		}

	}


	@Override
	public void dispose () {
		batch.dispose();
	}

	void generateFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Chibola.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 60;
		parameter.color = new Color().set(1, 0, 0, 1);
		parameter.borderColor = new Color().set(0, 0, 0, 1);
		parameter.borderWidth = 2;
		parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		font = generator.generateFont(parameter);
		parameter.color = new Color().set(1, 0, 0, 1);
		parameter.size = 80;
		fontLarge = generator.generateFont(parameter);
		generator.dispose();
	}
}
