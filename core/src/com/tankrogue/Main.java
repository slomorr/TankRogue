package com.tankrogue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

public class Main extends ApplicationAdapter {
	private static final int SCREEN_WIDTH = 1920;
	private static final int SCREEN_HEIGHT = 1080;
	private static final int MAP_WIDTH = 30;
	private static final int MAP_HEIGHT = 30;
	private static final int TILE_SIZE = 32;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Tank playerTank;
	private Texture wallText;
	private ArrayList<EnemyTank> enemyTanks;
	private Texture background;
	private MazeGenerator mazeGenerator;
	private ScoreCounter scoreCounter;
	private int lastShotTime = 0;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		background = new Texture("bar_background.png");
		background = new Texture("wall.png");
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		batch = new SpriteBatch();
		playerTank = new Tank(new Texture("player.png"), new Texture("player.png"), TILE_SIZE, TILE_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT);
		enemyTanks = new ArrayList<>();
		mazeGenerator = new MazeGenerator(new Texture("bar_background.png"), MAP_WIDTH, MAP_HEIGHT, TILE_SIZE);
		scoreCounter = new ScoreCounter();

		generateEnemies();

		mazeGenerator.generateMaze();

		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
	}

	private void generateEnemies() {
		Random random = new Random();
		int numEnemies = random.nextInt(5) + 5; // Генерация от 5 до 10 противников

		for (int i = 0; i < numEnemies; i++) {
			int x = random.nextInt(MAP_WIDTH);
			int y = random.nextInt(MAP_HEIGHT);

			// Проверка, чтобы противник не спавнился на игроке
			if (x != playerTank.getPosition().x / TILE_SIZE || y != playerTank.getPosition().y / TILE_SIZE) {
				EnemyTank enemyTank = new EnemyTank(new Texture("enemy.png"), new Texture("enemy.png"), x * TILE_SIZE, y * TILE_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT, playerTank);
				enemyTanks.add(enemyTank);
			}
		}
	}

	@Override
	public void render() {
		handleInput();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		batch.draw(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		drawMaze();

		playerTank.update(Gdx.graphics.getDeltaTime());
		playerTank.render(batch);

		for (EnemyTank enemyTank : enemyTanks) {
			enemyTank.update(Gdx.graphics.getDeltaTime());
			enemyTank.render(batch);
		}

		batch.end();
	}

	private void drawMaze() {
		for (int x = 0; x < MAP_WIDTH; x++) {
			for (int y = 0; y < MAP_HEIGHT; y++) {
				if (mazeGenerator.isWall(x, y)) {
					Wall wall = new Wall(wallText, x * TILE_SIZE, y * TILE_SIZE);
					wall.render(batch);
				}
			}
		}
	}

	private void handleInput() {
		Vector2 movement = new Vector2();

		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			movement.y += 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			movement.y -= 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			movement.x -= 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			movement.x += 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			float currentTime = TimeUtils.nanoTime() / 1000000000.0f;
			if (currentTime - lastShotTime > 0.5f) {
				playerTank.fire();
				lastShotTime = (int) currentTime;
			}
		}

		playerTank.move(movement);
	}

	@Override
	public void dispose() {
		batch.dispose();
		playerTank.dispose();
		for (EnemyTank enemyTank : enemyTanks) {
			enemyTank.dispose();
		}
	}
}