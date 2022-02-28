package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    private float fireTimer;
    private float firePeriod;
    private int damage;
    private TextureRegion texture;

    public Weapon(TextureAtlas atlas) {
        this.texture = atlas.findRegion("simpleWeapon");
        this.fireTimer = 0.0f;
        this.firePeriod = 0.4f;
        this.damage = 1;
    }

    public float getFireTimer() {
        return fireTimer;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }

    public TextureRegion getTexture() {
        return texture;
    }
}
