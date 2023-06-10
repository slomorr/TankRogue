package com.tankrogue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle bounds;
    private float speed;
    private float rotation;
    private Tank owner;

    public Bullet(Texture texture, Vector2 position, float rotation, Tank owner) {
        this.texture = texture;
        this.position = position;
        this.rotation = rotation;
        this.owner = owner;
        this.speed = 400f;
        this.velocity = new Vector2(speed, 0).setAngle(rotation);
        this.bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void update(float deltaTime) {
        position.add(velocity.scl(deltaTime));
        updateBounds();
    }

    private void updateBounds() {
        bounds.setPosition(position);
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Tank getOwner() {
        return owner;
    }
}