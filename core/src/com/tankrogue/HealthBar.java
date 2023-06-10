package com.tankrogue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HealthBar {
    private Texture barBackground;
    private Texture barFill;

    public HealthBar() {
        barBackground = new Texture("bar_background.png");
        barFill = new Texture("bar_fill.png");
    }

    public void draw(SpriteBatch batch, int health) {
        batch.draw(barBackground, 20, 540);
        batch.draw(barFill, 20, 540, health * 2, 20);
    }
}