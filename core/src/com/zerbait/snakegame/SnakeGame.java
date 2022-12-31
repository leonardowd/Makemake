package com.zerbait.snakegame;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class SnakeGame extends ApplicationAdapter {
	private SpriteBatch batch;
	Texture snakeImg;
	Texture foodImg;
	private Sound takeSound;
	private Music spaceShip;
	private Rectangle snakeHead;
	private Rectangle food;
	private Array<Rectangle> snakeBody = new Array<Rectangle>();
	private OrthographicCamera camera;
	private char direction = 'R';
	private static final int FOOD_WIDTH_HEIGHT = 32;
	private static final int SNAKE_WIDTH_HEIGHT = 44;
	
	private static final int VIEWPORT_WIDTH = 800;
	private static final int VIEWPORT_HEIGHT = 480;
	private double snakeStep = 1;
	private long speed = 100;
	private int scoreboard = 0;
	
	//bitmap
	private BitmapFont scoreFont;
	private BitmapFont gameOverFont;
	
	//Adding the foods
	private Array<Rectangle> foods = new Array<Rectangle>();
	
	//Collision
	private int leftWall = 0;
	private int righttWall = VIEWPORT_WIDTH;
	private int topWall = 0;
	private int downWall = VIEWPORT_HEIGHT;
	private boolean wallColision = false;
		
	@Override
	public void create () {
		batch = new SpriteBatch();
		snakeImg = new Texture(Gdx.files.internal("shipBeige_manned.png"));
		foodImg = new Texture(Gdx.files.internal("deathtex3.png"));
		
		//create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		
		//bitmap font
		scoreFont = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
		gameOverFont = new BitmapFont(Gdx.files.internal("gameOverFont.fnt"), Gdx.files.internal("gameOverFont.png"), false);
		
		//load the sounds
		takeSound = Gdx.audio.newSound(Gdx.files.internal("takeSound.wav"));
		spaceShip = Gdx.audio.newMusic(Gdx.files.internal("space_ship_floating_sound_1.mp3"));
		
		// start the background music immediately
		spaceShip.setLooping(true);
		spaceShip.play();
		
		//instantiate snake and foods
		spawnSnake();	
		spawnFood();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		
		batch.begin();
		batch.draw(snakeImg, snakeHead.x, snakeHead.y);
		for(Rectangle snake : snakeBody) {
			batch.draw(snakeImg, snake.x, snake.y);			
		}

		for(Rectangle food : foods) {
			batch.draw(foodImg, food.x, food.y);
		}
		
		scoreFont.draw(batch, "Scoreboard: " + scoreboard, 10, 460);
		
		batch.end();
		
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			direction = 'R';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			direction = 'L';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			direction = 'U';
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			direction = 'D';
		}
		
		switch (direction) {
		case 'R':
			moveSnakeToRight();
			break;
		case 'L':
			moveSnakeToLeft();
			break;
		case 'U':
			moveSnakeUp();
			break;
		case 'D':
			moveSnakeDown();
			break;
		default:
			break;
		}
		
		detectWallColision();
		
		for(Iterator<Rectangle> i = foods.iterator(); i.hasNext();) {
			Rectangle food = i.next();
			
			if(food.overlaps(snakeHead)) {
				takeSound.play();
				i.remove();
				spawnFood();
				eat();
			}
		}
		
		gameOver();
		
	}
	
	private void spawnSnake() {
		//create the snake
		snakeHead = new Rectangle();
		snakeHead.x = VIEWPORT_WIDTH /2 - SNAKE_WIDTH_HEIGHT /2;
		snakeHead.y = VIEWPORT_HEIGHT / 2 - SNAKE_WIDTH_HEIGHT /2;
		snakeHead.width = SNAKE_WIDTH_HEIGHT;
		snakeHead.height = SNAKE_WIDTH_HEIGHT;
	}

	private void spawnFood() {
		food = new Rectangle();
		food.x = MathUtils.random(0, VIEWPORT_WIDTH - FOOD_WIDTH_HEIGHT);
		food.y = MathUtils.random(0, VIEWPORT_HEIGHT - FOOD_WIDTH_HEIGHT);
		food.width = FOOD_WIDTH_HEIGHT;
		food.height = FOOD_WIDTH_HEIGHT;
		foods.add(food);
	}
	
	private void eat() {
		switch (direction) {
		case 'R':
			moveSnakeToRight();
			break;
		case 'L':
			moveSnakeToLeft();
			break;
		case 'U':
			moveSnakeDown();
			break;
		case 'D':
			moveSnakeUp();
			break;
		default:
			break;
		}
		
		this.speed = increaseSpeed(speed);
		this.scoreboard = increaseScoreboard(scoreboard);
	}
	
	private void moveSnakeToRight() {
		snakeHead.x += speed * Gdx.graphics.getDeltaTime();
	}

	private void moveSnakeToLeft() {
		snakeHead.x -= speed * Gdx.graphics.getDeltaTime();
	}
	
	private void moveSnakeUp() {
		snakeHead.y += speed * Gdx.graphics.getDeltaTime();
	}

	private void moveSnakeDown() {
		snakeHead.y -= speed * Gdx.graphics.getDeltaTime();
	}
	
	private long increaseSpeed(long speed) {
		speed = this.speed;		
			if (speed < 300) {
				speed += 20;			
			} else if (speed < 500) {
				speed += 10;
			} else if (speed < 800) {
				speed += 5;
			} else {
				speed += 1;
			}
		return speed;
	}
	
	private int increaseScoreboard(int score) {
		if (this.snakeStep < 3) {
			score += 5;
		} else if (this.snakeStep < 8) {
			score += 10;
		} else {
			score += 15;
		}
		
		return score;
	}
	
	private void detectWallColision() {
		if (snakeHead.x <= leftWall || snakeHead.x >= righttWall 
				|| snakeHead.y <= topWall || snakeHead.y >= downWall) {
			this.wallColision = true;
		}
	}
	
	private void gameOver() {
		if (this.wallColision == true) {
			batch.begin();
			spaceShip.stop();
			ScreenUtils.clear(0, 0, 0, 1);
			scoreFont.draw(batch, "Scoreboard: " + scoreboard, VIEWPORT_WIDTH / 2 - 50, VIEWPORT_HEIGHT / 2 + 50);
			scoreFont.draw(batch, "Close the game and reopen if you wanna play again", VIEWPORT_WIDTH / 2 - 180, VIEWPORT_HEIGHT / 2);
			gameOverFont.draw(batch, "GAME OVER", VIEWPORT_WIDTH / 2 - 100, VIEWPORT_HEIGHT / 2 + 100);
			batch.end();
		}
	}
	
	@Override
	public void dispose () {
		snakeImg.dispose();
		foodImg.dispose();
		takeSound.dispose();
		spaceShip.dispose();
		batch.disableBlending();
	}
}
