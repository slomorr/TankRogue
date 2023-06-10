package com.tankrogue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tankrogue.Bullet;

import java.util.ArrayList;

public class Tank {
    private Texture texture;
    private Vector2 position;
    private Vector2 movement;
    private Rectangle bounds;
    private float speed;
    private float rotation;
    private Texture barrelTexture;
    private float barrelRotation;
    private ArrayList<Bullet> bullets;
    private float fireRate;
    private float fireCooldown;
    private int screenWidth;
    private int screenHeight;
    private int currentHealth;

    public Tank(Texture texture, Texture barrelTexture, float x, float y, int screenWidth, int screenHeight) {
        this.texture = texture;
        this.barrelTexture = barrelTexture;
        this.position = new Vector2(x, y);
        this.movement = new Vector2();
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        this.speed = 200f;
        this.rotation = 0f;
        this.barrelRotation = 0f;
        this.bullets = new ArrayList<>();
        this.fireRate = 0.5f;
        this.fireCooldown = 0f;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.currentHealth = 100;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getBarrelRotation() {
        return barrelRotation;
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            // Танк уничтожен
        }
    }
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }
    protected void move(Vector2 movement) {
        this.movement.set(movement);
        this.movement.nor();
        position.add(this.movement.scl(speed * Gdx.graphics.getDeltaTime()));
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update(float deltaTime) {
        handleInput();
        updateMovement(deltaTime);
        updateRotation();
        updateBounds();
        updateBarrelRotation();
        fireCooldown -= deltaTime;
    }

    private void handleInput() {
        movement.set(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movement.y = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movement.y = -1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.x = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.x = 1;
        }
    }

    private void updateMovement(float deltaTime) {
        movement.nor();
        position.add(movement.scl(speed * deltaTime));

        // Проверка столкновения с краями экрана или стенами
        if (position.x < 0) {
            position.x = 0;
        } else if (position.x + texture.getWidth() > screenWidth) {
            position.x = screenWidth - texture.getWidth();
        }

        if (position.y < 0) {
            position.y = 0;
        } else if (position.y + texture.getHeight() > screenHeight) {
            position.y = screenHeight - texture.getHeight();
        }
    }

    private void updateRotation() {
        if (movement.x != 0 || movement.y != 0) {
            rotation = MathUtils.atan2(movement.y, movement.x) * MathUtils.radiansToDegrees;
        }
    }

    private void updateBounds() {
        bounds.set(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    private void updateBarrelRotation() {
        Vector2 mousePosition = new Vector2(Gdx.input.getX(), screenHeight - Gdx.input.getY());
        Vector2 direction = mousePosition.sub(position).nor();
        barrelRotation = MathUtils.atan2(direction.y, direction.x) * MathUtils.radiansToDegrees;
    }

    public void fire() {
        if (fireCooldown <= 0) {
            // Создание и выстрел снарядом
            Vector2 bulletPosition = new Vector2(position.x + texture.getWidth() / 2, position.y + texture.getHeight() / 2);
            Bullet bullet = new Bullet(barrelTexture, bulletPosition, barrelRotation, this);
            bullets.add(bullet);
            fireCooldown = fireRate;
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getSpeed() {
        return speed;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

}