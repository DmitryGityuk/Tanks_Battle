package com.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Weapon;
import com.mygdx.game.utils.Direction;
import com.mygdx.game.utils.TankOwner;

public class PlayerTank extends Tank {
    int lives;
    int score;

    public PlayerTank(GameScreen game, TextureAtlas atlas) {
        super(game);
        this.weapon = new Weapon(atlas);
        this.ownerType = TankOwner.PLAYER;
        this.texture = atlas.findRegion("playerTankBase");
        this.textureHP = atlas.findRegion("bar");
        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100.0f;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.hpMax = 10;
        this.hp = this.hpMax;
        this.circle = new Circle(position.x, position.y, (width + height) / 2);
        this.lives = 5;
    }

    /**
     * Метод реализует движение танка
     */
    public void checkMovement(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            move(Direction.LEFT, dt);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            move(Direction.RIGHT, dt);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            move(Direction.UP, dt);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            move(Direction.DOWN, dt);
        }
    }

    /**
     * Метод реализует обновление положения танка
     */
    public void update(float dt) {
        checkMovement(dt);
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        rotateTurretToPoint(mx, my, dt);
        if (Gdx.input.isTouched()) {
            fire();
        }
        super.update(dt);
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font) {
        font.draw(batch, "Score: " + score + "\nLives: " + lives, 10, 700);
    }

    public void addScore(int amount) {
        score += amount;
    }

    @Override
    public void destroy() {
        lives--;
        hp = hpMax;
    }
}
