package com.tankrogue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
    private Texture wallTexture;
    private float screenWidth;
    private float screenHeight;
    private float wallSize;

    private List<Wall> walls = new ArrayList<Wall>();

    public MazeGenerator(Texture wallTexture, float screenWidth, float screenHeight, float wallSize) {
        this.wallTexture = wallTexture;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.wallSize = wallSize;
    }
    public void dispose() {
        // Пустой метод dispose()
    }

    public List<Wall> generateMaze() {
        List<Wall> walls = new ArrayList<>();
        Random random = new Random();

        int numHorizontalWalls = (int) (screenWidth / wallSize);
        int numVerticalWalls = (int) (screenHeight / wallSize);

        for (int i = 0; i < numHorizontalWalls; i++) {
            float x = i * wallSize;
            walls.add(new Wall(wallTexture, x, 0));
            walls.add(new Wall(wallTexture, x, screenHeight - wallSize));
        }

        for (int i = 1; i < numVerticalWalls - 1; i++) {
            float y = i * wallSize;
            walls.add(new Wall(wallTexture, 0, y));
            walls.add(new Wall(wallTexture, screenWidth - wallSize, y));
        }

        for (int i = 1; i < numHorizontalWalls - 1; i++) {
            for (int j = 1; j < numVerticalWalls - 1; j++) {
                if (random.nextFloat() < 0.3f) {
                    float x = i * wallSize;
                    float y = j * wallSize;
                    walls.add(new Wall(wallTexture, x, y));
                }
            }
        }
        walls = walls;
        return walls;

    }
    public boolean isWall(int x, int y) {
        for (Wall wall : walls) {
            if (wall.getPosition().x == x && wall.getPosition().y == y) {
                return true;
            }
        }
        return false;
    }

}
