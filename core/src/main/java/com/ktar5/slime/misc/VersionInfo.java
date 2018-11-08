package com.ktar5.slime.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.ktar5.slime.engine.util.textoverlay.TextDisplay;

public class VersionInfo extends TextDisplay {

    public VersionInfo() {
        Texture texture = new Texture(Gdx.files.internal("fonts/text.png"), false);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);


        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/text.fnt"), new TextureRegion(texture), false);
        font.setUseIntegerPositions(false);
        setFont(font);

        this.getFont().setColor(Color.WHITE);
        this.getFont().getData().setScale(.5f);
//        camera.zoom = .5f;
    }

    @Override
    public Vector3 getLocation() {
        return new Vector3(Gdx.graphics.getWidth() - 250, 75, 0);
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public void update(float dTime) {
        //location = new Vector3(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 200, 0);
    }
}
