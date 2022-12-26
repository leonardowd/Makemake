package com.zerbait.snakegame;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class SnakeGame extends ApplicationAdapter {
	private SpriteBatch batch;
	Texture snakeImg;
	Texture foodImg;
	private Sound biteSound;
	private Rectangle snake;
	private Rectangle food;
	private Array<Rectangle> snakeBody;
	private OrthographicCamera camera;
	private char direction = 'R';
	
	//Adding the foods
	private Array<Rectangle> foods;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		snakeImg = new Texture(Gdx.files.internal("snake.png"));
		foodImg = new Texture(Gdx.files.internal("food.png"));
		
		//create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		//load the sounds
		//biteSound = Gdx.audio.newSound(Gdx.files.internal("biteSound.wav"));
		//TODO fix the load's song bug
		
		snakeBody = new Array<Rectangle>();
		spawnSnake();
		
		//instantiate foods
		foods = new Array<Rectangle>();
		spawnFood();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		for(Rectangle snake : snakeBody) {
			batch.draw(snakeImg, snake.x, snake.y);			
		}
		
		for(Rectangle food : foods) {
			batch.draw(foodImg, food.x, food.y);
		}

		
		batch.end();
		
		//TODO make the snake collision
		
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && direction != 'L') {
			direction = 'R';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && direction != 'R') {
			direction = 'L';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && direction != 'D') {
			direction = 'U';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && direction != 'U') {
			direction = 'D';
		}

		switch (direction) {
		case 'R':
			snake.x += 3;
			break;
		case 'L':
			snake.x -= 3;
			break;
		case 'U':
			snake.y += 3;
			break;
		case 'D':
			snake.y -= 3;
			break;
		default:
			break;
		}
		
		for(Iterator<Rectangle> i = foods.iterator(); i.hasNext();) {
			Rectangle food = i.next();
			
			if(food.overlaps(snake)) {
				i.remove();
				spawnFood();
			}
		}
		
	}
	
	private void spawnSnake() {
		//create the snake
		snake = new Rectangle();
		snake.x = 800/2 - 16/2;
		snake.y = 480 / 2 - 16/2;
		snake.width = 16;
		snake.height = 16;
		snakeBody.add(snake);
	}

	private void spawnFood() {
		food = new Rectangle();
		food.x = MathUtils.random(0, 800-10);
		food.y = MathUtils.random(0, 480-10);
		food.width = 16;
		food.height = 16;
		foods.add(food);
	}
	
	@Override
	public void dispose () {
		snakeImg.dispose();
		foodImg.dispose();
		batch.disableBlending();
	}
}
