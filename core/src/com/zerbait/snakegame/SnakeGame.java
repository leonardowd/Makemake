package com.zerbait.snakegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class SnakeGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture snakeImg;
	private Rectangle snake;
	private Array<Rectangle> snakeFull;
	private OrthographicCamera camera;
	private char direction = 'R';
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		snakeImg = new Texture(Gdx.files.internal("snake.png"));
		
		//create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		snakeFull = new Array<Rectangle>();
		spawnSnake();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		for(Rectangle snake : snakeFull) {
			batch.draw(snakeImg, snake.x, snake.y);			
		}
		batch.end();
		
		//TODO make the snake collision
		
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//			snake.x += 64 * Gdx.graphics.getDeltaTime();
			direction = 'R';
		}
		
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//			snake.x -= 64 * Gdx.graphics.getDeltaTime();
			direction = 'L';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//			snake.y += 64 * Gdx.graphics.getDeltaTime();
			direction = 'U';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//			snake.y -= 64 * Gdx.graphics.getDeltaTime();
			direction = 'D';
		}

		switch (direction) {
		case 'R':
			snake.x++;
			break;
		case 'L':
			snake.x--;
			break;
		case 'U':
			snake.y++;
			break;
		case 'D':
			snake.y--;
			break;
		default:
			break;
		}
	}
	
	private void spawnSnake() {
		//create the snake
		snake = new Rectangle();
		snake.x = 800/2 - 16/2;
		snake.y = 480 / 2 - 16/2;
		snake.width = 16;
		snake.height = 16;
		snakeFull.add(snake);
	}
	
	@Override
	public void dispose () {
		snakeImg.dispose();
		batch.disableBlending();
	}
}
