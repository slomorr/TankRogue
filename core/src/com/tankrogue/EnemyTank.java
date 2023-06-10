package com.tankrogue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class EnemyTank extends Tank {
    private Tank playerTank;

    public EnemyTank(Texture texture, Texture barrelTexture, float x, float y, int screenWidth, int screenHeight, Tank playerTank) {
        super(texture, barrelTexture, x, y, screenWidth, screenHeight);
        this.playerTank = playerTank;
    }
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(getTexture(), getPosition().x, getPosition().y);
        batch.draw(new Texture("enemy.png"), getPosition().x, getPosition().y, getTexture().getWidth() / 2f, getTexture().getHeight() / 2f,
                getTexture().getWidth(), getTexture().getHeight(), 1f, 1f);
    }

    @Override
    public void dispose() {
        super.dispose();
        new Texture("enemy.png").dispose();
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Vector2 playerDirection = playerTank.getPosition().cpy().sub(getPosition()).nor();
        float targetRotation = playerDirection.angle();

        setRotation(targetRotation);
        move(playerDirection.scl(getSpeed())); // Перемещение с учетом скорости

        float distance = getPosition().dst(playerTank.getPosition());
        if (distance < 300) {
            fire();
        }
    }
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        // Дополнительная логика для вражеского танка при получении урона
    }
}