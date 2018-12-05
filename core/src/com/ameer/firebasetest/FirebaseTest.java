package com.ameer.firebasetest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import mk.gdx.firebase.GdxFIRApp;
import mk.gdx.firebase.GdxFIRDatabase;
import mk.gdx.firebase.annotations.MapConversion;
import mk.gdx.firebase.callbacks.DataCallback;
public class FirebaseTest extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	int nNumber;
	int nTouches;
	Datacache numTouches;
	Datacache dataMsg;
	FreeTypeFontGenerator generator;
	FreeTypeFontGenerator.FreeTypeFontParameter textParams;
	BitmapFont font;
	@Override
	public void create () {
		GdxFIRApp.instance().configure();
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		nNumber = 10;
		numTouches = new Datacache();
		dataMsg = new Datacache();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("pixeldown.ttf"));
		textParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
		textParams.size = 150;
		font = generator.generateFont(textParams);
		Gdx.input.setInputProcessor(this);
		updateNumtouches();
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		font.draw(batch, Integer.toString(nTouches), Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		batch.end();
		updateNumtouches();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
	public void updateNumtouches(){
		GdxFIRDatabase.instance().inReference("/Tapapp").readValue(Datacache.class, new DataCallback<Datacache>() {
			@MapConversion(Datacache.class)
			@Override
			public void onData(Datacache data) {
				Datacache dataRec = data;
				nTouches = Integer.parseInt(data.getWord());
			}
			@Override
			public void onError(Exception e) {
				System.err.println(e);
			}
		});
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(screenX < Gdx.graphics.getWidth() / 2){
			nTouches --;
		} else if( screenX > Gdx.graphics.getWidth() / 2){
			nTouches ++;
		}

		numTouches.setWord(Integer.toString(nTouches));
		GdxFIRDatabase.instance().inReference("/Tapapp").setValue(numTouches);

		return true;
	}
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
