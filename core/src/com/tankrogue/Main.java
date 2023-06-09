package com.tankrogue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Main extends ApplicationAdapter {
	private static final int TILE_SIZE = 32;
	private static final int WORLD_WIDTH = 1920;
	private static final int WORLD_HEIGHT = 1080;
	private static final float MOVE_SPEED = 5.0f;
	private static final float BULLET_SPEED = 10.0f;
	private static final int MAX_ENEMIES = 10;

	private static final float ENEMY_SPEED = 5.0f;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture tankTexture;
	private Texture bulletTexture;
	private Texture enemyTexture;
	private Vector2 tankPosition;
	private Vector2 tankVelocity;
	private Array<Rectangle> walls;
	private Array<Vector2> bullets;
	private Array<Rectangle> enemies;
	private BitmapFont font;
	private int score;
	private int health;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

		batch = new SpriteBatch();

		tankTexture = new Texture("tank.png");
		bulletTexture = new Texture("bullet.png");
		enemyTexture = new Texture("enemy.png");
		tankPosition = new Vector2(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
		tankVelocity = new Vector2();
		walls = new Array<>();
		bullets = new Array<>();
		enemies = new Array<>();
		font = new BitmapFont();
		score = 0;
		health = 100;

		generateWorld();
		spawnEnemies();
	}

	private void generateWorld() {
		// Процедурная генерация лабиринта или мира
		// Реализуйте свою логику генерации мира здесь
	}

	private void spawnEnemies() {
		for (int i = 0; i < MAX_ENEMIES; i++) {
			float x = MathUtils.random(0, WORLD_WIDTH - TILE_SIZE);
			float y = MathUtils.random(0, WORLD_HEIGHT - TILE_SIZE);
			Rectangle enemy = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
			enemies.add(enemy);
		}
	}

	@Override
	public void render() {
		handleInput();
		update();

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(tankTexture, tankPosition.x, tankPosition.y);

		for (Rectangle wall : walls) {
			batch.draw(tankTexture, wall.x, wall.y);
		}

		for (Vector2 bullet : bullets) {
			batch.draw(bulletTexture, bullet.x, bullet.y);
		}

		for (Rectangle enemy : enemies) {
			batch.draw(enemyTexture, enemy.x, enemy.y);
		}

		font.draw(batch, "Score: " + score, 10, WORLD_HEIGHT - 10);
		font.draw(batch, "Health: " + health, 10, WORLD_HEIGHT - 30);

		batch.end();
	}

	private void handleInput() {
		tankVelocity.set(0, 0);

		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			tankVelocity.y = MOVE_SPEED;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			tankVelocity.y = -MOVE_SPEED;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			tankVelocity.x = -MOVE_SPEED;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			tankVelocity.x = MOVE_SPEED;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			Vector2 bulletPosition = new Vector2(tankPosition.x + tankTexture.getWidth() / 2 - bulletTexture.getWidth() / 2,
					tankPosition.y + tankTexture.getHeight() / 2 - bulletTexture.getHeight() / 2);
			bullets.add(bulletPosition);
		}
	}

	private void update() {
		tankPosition.add(tankVelocity);

		// Проверка коллизии с границами экрана
		if (tankPosition.x < 0) {
			tankPosition.x = 0;
		} else if (tankPosition.x > WORLD_WIDTH - TILE_SIZE) {
			tankPosition.x = WORLD_WIDTH - TILE_SIZE;
		}
		if (tankPosition.y < 0) {
			tankPosition.y = 0;
		} else if (tankPosition.y > WORLD_HEIGHT - TILE_SIZE) {
			tankPosition.y = WORLD_HEIGHT - TILE_SIZE;
		}

		// Обновление положения снарядов
		for (int i = bullets.size - 1; i >= 0; i--) {
			Vector2 bullet = bullets.get(i);
			bullet.y += BULLET_SPEED;

			// Удаление снарядов, вышедших за пределы экрана
			if (bullet.y > WORLD_HEIGHT) {
				bullets.removeIndex(i);
			}
		}

		// Обновление положения врагов
		for (Rectangle enemy : enemies) {
			enemy.x += ENEMY_SPEED * Gdx.graphics.getDeltaTime();

			// Логика атаки врагов
			if (enemy.x < tankPosition.x + tankTexture.getWidth() &&
					enemy.x + enemy.width > tankPosition.x &&
					enemy.y < tankPosition.y + tankTexture.getHeight() &&
					enemy.y + enemy.height > tankPosition.y) {
				health -= 10;
				if (health <= 0) {
					// Game over
				}
			}

			// Проверка коллизии снарядов и врагов
			for (int i = bullets.size - 1; i >= 0; i--) {
				Vector2 bullet = bullets.get(i);
				if (bullet.x < enemy.x + enemy.width && bullet.x + bulletTexture.getWidth() > enemy.x &&
						bullet.y < enemy.y + enemy.height && bullet.y + bulletTexture.getHeight() > enemy.y) {
					bullets.removeIndex(i);
					enemies.removeValue(enemy, true);
					score += 10;
					break;
				}
			}
		}

		// Проверка коллизии со стенами
		for (Rectangle wall : walls) {
			if (tankPosition.x < wall.x + wall.width && tankPosition.x + TILE_SIZE > wall.x &&
					tankPosition.y < wall.y + wall.height && tankPosition.y + TILE_SIZE > wall.y) {
				tankVelocity.set(0, 0); // Остановка танка
				break;
			}
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		tankTexture.dispose();
		bulletTexture.dispose();
		enemyTexture.dispose();
		font.dispose();
	}
}