package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Map {
    public enum WallType {
        HARD(0, 5, true, false, false),
        SOFT(1, 3, true, false, false),
        INDESTRUCTIBLE(2, 1, false, false, false),
        WATER(3, 1, false, false, true),
        NONE(0, 0, false, true, true);
        int index;
        int maxHP;
        boolean unitPossible;
        boolean projectilePossible;
        boolean destructible;

        WallType(int index, int maxHP, boolean destructible, boolean unitPossible, boolean projectilePossible) {
            this.index = index;
            this.maxHP = maxHP;
            this.destructible = destructible;
            this.unitPossible = unitPossible;
            this.projectilePossible = projectilePossible;
        }
    }

    private class Cell {
        WallType type;
        int hp;

        public Cell(WallType type) {
            this.type = type;
            this.hp = type.maxHP;
        }

        public void damage() {
            if (type.destructible) {
                hp--;
                if (hp <= 0) {
                    type = WallType.NONE;
                }
            }
        }

        public void changeType(WallType type) {
            this.type = type;
            this.hp = type.maxHP;
        }
    }

    private TextureRegion grassTexture;
    private TextureRegion[][] wallsTexture;
    private static final int SIZE_X = 64;
    private static final int SIZE_Y = 36;
    private static final int CELL_SIZE = 20;
    private Cell cells[][];


    public Map(TextureAtlas atlas) {
        this.wallsTexture = new TextureRegion(atlas.findRegion("obstacles")).split(CELL_SIZE, CELL_SIZE);
        this.grassTexture = atlas.findRegion("grass40");
        this.cells = new Cell[SIZE_X][SIZE_Y];
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                cells[i][j] = new Cell(WallType.NONE);
                int cx = i / 4;
                int cy = j / 4;
                if (cx % 2 == 0 && cy % 2 == 0) {
                    if (MathUtils.random() < 0.7f) {
                        cells[i][j].changeType(WallType.WATER);
                    } else {
                        cells[i][j].changeType(WallType.SOFT);
                    }
                }
            }
        }
        for (int i = 0; i < SIZE_X; i++) {
            cells[i][0].changeType(WallType.INDESTRUCTIBLE);
            cells[i][SIZE_Y - 1].changeType(WallType.INDESTRUCTIBLE);
        }

        for (int i = 0; i < SIZE_Y; i++) {
            cells[0][i].changeType(WallType.INDESTRUCTIBLE);
            cells[SIZE_X - 1][i].changeType(WallType.INDESTRUCTIBLE);
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < 1280 / 40; i++) {
            for (int j = 0; j < 720 / 40; j++) {
                batch.draw(grassTexture, i * 40, j * 40);
            }
        }
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                if (cells[i][j].type != WallType.NONE) {
                    batch.draw(wallsTexture[cells[i][j].type.index][cells[i][j].hp - 1], i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }

    public void checkWallAndBulletsCollision(Bullet bullet) {
        int cx = (int) (bullet.getPosition().x / CELL_SIZE);
        int cy = (int) (bullet.getPosition().y / CELL_SIZE);
        if (cx >= 0 && cy >= 0 && cx < SIZE_X && cy < SIZE_Y) {
            if (!cells[cx][cy].type.projectilePossible) {
                cells[cx][cy].damage();
                bullet.deactivate();
            }
        }
    }

    public boolean checkIsAreaClear(float x, float y, float halfSize) {
        int leftX = (int) ((x - halfSize) / CELL_SIZE);
        int rightX = (int) ((x + halfSize) / CELL_SIZE);
        int bottomY = (int) ((y - halfSize) / CELL_SIZE);
        int topY = (int) ((y + halfSize) / CELL_SIZE);

        if (leftX < 0) {
            leftX = 0;
        }
        if (rightX >= SIZE_X) {
            rightX = SIZE_X - 1;
        }

        if (bottomY < 0) {
            bottomY = 0;
        }
        if (topY >= SIZE_Y) {
            topY = SIZE_Y - 1;
        }

        for (int i = leftX; i <= rightX; i++) {
            for (int j = bottomY; j <= topY; j++) {
                if (!cells[i][j].type.unitPossible) {
                    return false;
                }
            }
        }
        return true;
    }
}
