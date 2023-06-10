package com.tankrogue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreCounter {
    private BitmapFont font;

    public ScoreCounter() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    public void draw(SpriteBatch batch, int score) {
        font.draw(batch, "Score: " + score, 20, 580);
    }
}