package com.ktar5.gameengine.util.textoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.util.Renderable;
import com.ktar5.gameengine.util.Updatable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class TextDisplay implements Disposable, Updatable, Renderable {
    private BitmapFont font;
    protected Vector3 location;
    ShaderProgram fontShader;

    public TextDisplay() {
        font = new BitmapFont();
        font.getData().setScale(1);
        font.setColor(Color.BLACK);
        location = getLocation();

        fontShader = new ShaderProgram(Gdx.files.internal("shaders/font.vert"), Gdx.files.internal("shaders/font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }


    }

    public abstract Vector3 getLocation();

    public void dispose() {
        font.dispose();
    }

    public abstract String getText();

    @Override
    public void render(SpriteBatch batch, float dTime) {
        Matrix4 projectionMatrix = batch.getProjectionMatrix();
//        batch.setProjectionMatrix(camera.combined);
        batch.setShader(fontShader);
//        Vector3 v3 = camera.unproject(location.cpy());
        Vector3 v3 = EngineManager.get().getCameraBase().getCamera().unproject(location.cpy());

        font.draw(batch, getText(), v3.x, v3.y);
        batch.setProjectionMatrix(projectionMatrix);
        batch.setShader(null);
    }
}