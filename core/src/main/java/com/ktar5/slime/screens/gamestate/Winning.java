package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.ktar5.gameengine.EngConst;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.camera.StaticCamera;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.postprocessing.PostProcessor;
import com.ktar5.gameengine.postprocessing.effects.Vignette;
import com.ktar5.gameengine.postprocessing.filters.Blur;
import com.ktar5.gameengine.postprocessing.utils.ShaderLoader;
import com.ktar5.gameengine.rendering.Renderable;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.analytics.LevelCompleteEvent;
import com.ktar5.slime.screens.GameScreen;

import java.util.Collections;
import java.util.List;

public class Winning extends GameState {
    private Stage stage;
    private StaticCamera camera = new StaticCamera(new OrthographicCamera(480, 270));
    private PostProcessor postProcessor;
    private Blur blur;
    private VisLabel collectiblesScore = new VisLabel(), slimeCoveredScore = new VisLabel(), winText = new VisLabel();

    public Winning(GameScreen gameScreen) {
        super(gameScreen);
        SpriteBatch batch = new SpriteBatch();

        camera.getCamera().position.set(camera.getCamera().viewportWidth / 2, camera.getCamera().viewportHeight / 2, 0);
        camera.getCamera().update();

        stage = new Stage(camera.getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        EngineManager.get().getConsole().resetInputProcessing();

        VisLabel clickToContinue = new VisLabel("Click anywhere to continue...");

        Table scores = new Table();
        scores.setFillParent(true);
        scores.pad(20, 0, 0, 0);
        scores.top();
        scores.add(winText).padBottom(100f);
        scores.row();
        scores.add(collectiblesScore);
        scores.row();
        scores.add(slimeCoveredScore);

        stage.addActor(scores);

        Table clickToContinueTable = new Table();
        clickToContinueTable.bottom();
        clickToContinueTable.setFillParent(true);
        clickToContinueTable.padBottom(20f);
        clickToContinueTable.add(clickToContinue);

        stage.addActor(clickToContinueTable);


        ShaderLoader.BasePath = "shaders/";
        blur = new Blur(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        postProcessor = new PostProcessor(false, false, true);
        Vignette vignette = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        vignette.setCenter(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        vignette.setIntensity(1f);
//        Blur blur = new Blur(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        postProcessor.addEffect(vignette);
        objects = Collections.singletonList((internalBatch, dTime) -> {

            postProcessor.capture();
            Gdx.gl.glClearColor(168 / 255f, 118 / 255f, 86 / 255f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            SlimeGame.getGame().getLevelHandler().render(internalBatch, EngConst.STEP_TIME);
            getGameScreen().getFrameRate().render(internalBatch, EngConst.STEP_TIME);
            getGameScreen().getVersionInfo().render(internalBatch, EngConst.STEP_TIME);

            blur.setAmount(1f);
            blur.setType(Blur.BlurType.Gaussian3x3b);
            blur.setPasses(10);
            blur.render(postProcessor.getCombinedBuffer());
            postProcessor.render();

            stage.act();
            stage.draw();
        });
    }

    List<Renderable> objects;


    @Override
    public void start() {
        getGameScreen().getRenderManager().setRenderables(objects);

        collectiblesScore.setText("You collected "
                + SlimeGame.getGame().getLevelHandler().getCurrentLevel().getCollectibles()
                + " out of 3 collectibles");
        slimeCoveredScore.setText("You covered "
                + SlimeGame.getGame().getLevelHandler().getCurrentLevel().getNumberTilesSlimed()
                + " tiles in slime!");
        winText.setText("You got the treasure on level " +
                SlimeGame.getGame().getLevelHandler().getCurrentLevel().getId() + "!");

        Analytics.addEvent(new LevelCompleteEvent(SlimeGame.getGame().getLevelHandler().getCurrentLevel()));
        Analytics.flush();
    }

    @Override
    public void resize(int width, int height) {
        camera.getCamera().viewportHeight = height;
        camera.getCamera().viewportWidth = width;
        camera.getCamera().update();

//        camera.getViewport().setScreenWidth(Gdx.graphics.getWidth());
//        camera.getViewport().setScreenWidth(Gdx.graphics.getHeight());
//        camera.getViewport().apply();

        stage.getViewport().update(width, height, true);
        getGameScreen().getCamera().getViewport().update(width, height);
    }


    @Override
    public void onUpdate(float dTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            SlimeGame.getGame().getLevelHandler().advanceLevel();
            changeState(Running.class);
        }
    }

    @Override
    protected void end() {

    }

}
