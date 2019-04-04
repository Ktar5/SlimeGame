package com.ktar5.slime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.camera.CameraBase;
import com.ktar5.slime.engine.core.AbstractScreen;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.world.level.LevelHandler;
import org.pmw.tinylog.Logger;

import java.util.Collections;
import java.util.List;

public class LoadingScreen extends AbstractScreen {

    private Stage stage;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg;

    private float startX, endX;
    private float percent;

    private Actor loadingBar;


    public LoadingScreen(CameraBase camera) {
        super(camera);
    }

    @Override
    public List<Renderable> initializeRenderables() {
        return Collections.emptyList();
    }

    @Override
    public void initializeUpdatables() {

    }

    @Override
    public void show() {
        Logger.debug("Starting loading screen");
        // Tell the manager to load assets for the loading screen
        Logger.debug("Starting load of loading screen resources");
        EngineManager.get().getAssetManager().load("data/loading.pack", TextureAtlas.class);
        // Wait until they are finished loading
        EngineManager.get().getAssetManager().finishLoading();
        Logger.debug("Finished loading of loading screen resources");

        // Initialize the stage where we will place everything
        stage = new Stage();

        // Get our textureatlas from the manager
        TextureAtlas atlas = EngineManager.get().getAssetManager().get("data/loading.pack", TextureAtlas.class);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion("libgdx-logo"));
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        screenBg = new Image(atlas.findRegion("screen-bg"));
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

        // Add the loading bar animation
        Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim"));
        anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        loadingBar = new LoadingBar(anim);

        // Or if you only need a static bar, you can do
        // loadingBar = new Image(atlas.findRegion("loading-bar1"));

        // Add all the actors to the stage
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);

        EngineManager.get().getAnimationLoader().loadAtlas("textures/player/Slime.atlas");
        SlimeGame.getGame().setLevelHandler(new LevelHandler());
//        EngineManager.get().getAssetManager();

        // Add everything to be loaded, for instance:
        // game.manager.load("data/assets1.pack", TextureAtlas.class);
        // game.manager.load("data/assets2.pack", TextureAtlas.class);
        // game.manager.load("data/assets3.pack", TextureAtlas.class);
        //DreamGame.getGame().getManager().load("texture/hud/crosshair.png", Texture.class);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Logger.debug("Updating load.. " + EngineManager.get().getAssetManager().getProgress() + "%");
        if (EngineManager.get().getAssetManager().update()) { // Load some, will return true if done loading
            Logger.debug("Finished loading all assets!");
            EngineManager.get().getGame().setScreen(new MenuScreen());
        }

        // Interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, EngineManager.get().getAssetManager().getProgress(), 0.1f);

        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(450 - 450 * percent);
        loadingBg.invalidate();

        // Show the loading screen
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        // Dispose the loading assets as we no longer need them
        EngineManager.get().getAssetManager().unload("data/loading.pack");
        Logger.debug("Disposed of the loading resources");
    }

    @Override
    public void resize(int width, int height) {
//        super.resize(width, height);
//        // Set our screen to always be XXX x 480 in size
//        width = 480 * width / height;
//        height = 480;
//        stage.getViewport().update(width, height, false);
//
//        // Make the background fill the screen
//        screenBg.setSize(width, height);
//
//        // Place the logo in the middle of the screen and 100 px up
//        logo.setX((width - logo.getWidth()) / 2);
//        logo.setY((height - logo.getHeight()) / 2 + 100);
//
//        // Place the loading frame in the middle of the screen
//        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
//        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);
//
//        // Place the loading bar at the same spot as the frame, adjusted a few px
//        loadingBar.setX(loadingFrame.getX() + 15);
//        loadingBar.setY(loadingFrame.getY() + 5);
//
//        // Place the image that will hide the bar on top of the bar, adjusted a few px
//        loadingBarHidden.setX(loadingBar.getX() + 35);
//        loadingBarHidden.setY(loadingBar.getY() - 3);
//        // The start position and how far to move the hidden loading bar
//        startX = loadingBarHidden.getX();
//        endX = 440;
//
//        // The rest of the hidden bar
//        loadingBg.setSize(450, 50);
//        loadingBg.setX(loadingBarHidden.getX() + 30);
//        loadingBg.setY(loadingBarHidden.getY() + 3);
    }

    @Override
    public void dispose() {

    }


    /**
     * @author Mats Svensson
     */
    class LoadingBar extends Actor {

        Animation<TextureRegion> animation;
        TextureRegion reg;
        float stateTime;

        public LoadingBar(Animation<TextureRegion> animation) {
            this.animation = animation;
            reg = animation.getKeyFrame(0);
        }

        @Override
        public void act(float delta) {
            stateTime += delta;
            reg = animation.getKeyFrame(stateTime);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(reg, getX(), getY());
        }
    }

}
