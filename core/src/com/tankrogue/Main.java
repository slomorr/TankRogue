package com.tankrogue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Main extends ApplicationAdapter {
	private static final int TILE_SIZE = 32;
	private static final int WORLD_WIDTH = 800;
	private static final int WORLD_HEIGHT = 600;
	private static final float MOVE_SPEED = 5.0f;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture tankTexture;
	private Vector2 tankPosition;

	private Array<Vector2> walls;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

		batch = new SpriteBatch();

		tankTexture = new Texture("tank.png");
		tankPosition = new Vector2(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);

		walls = new Array<>();
		walls.add(new Vector2(100, 100));
		walls.add(new Vector2(200, 200));
		walls.add(new Vector2(300, 300));
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

		for (Vector2 wall : walls) {
			batch.draw(tankTexture, wall.x, wall.y);
		}

		batch.end();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			tankPosition.y += MOVE_SPEED;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			tankPosition.y -= MOVE_SPEED;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			tankPosition.x -= MOVE_SPEED;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			tankPosition.x += MOVE_SPEED;
		}
	}

	private void update() {
		// Perform game logic here
	}

	@Override
	public void dispose() {
		batch.dispose();
		tankTexture.dispose();
	}
}