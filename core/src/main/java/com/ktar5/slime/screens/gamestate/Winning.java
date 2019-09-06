package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.ktar5.gameengine.EngConst;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.input.ControllerInput;
import com.ktar5.gameengine.input.devices.XboxOneGamepad;
import com.ktar5.slime.KInput;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.analytics.LevelCompleteEvent;
import com.ktar5.slime.screens.GameScreen;

public class Winning extends GameState {
    private Stage stage;

    private VisLabel collectiblesScore = new VisLabel(), slimeCoveredScore = new VisLabel(), winText = new VisLabel();

    public Winning(GameScreen gameScreen) {
        super(gameScreen);

//        camera.getCamera().position.set(camera.getCamera().viewportWidth / 2, camera.getCamera().viewportHeight / 2, 0);
//        camera.getCamera().update();
//
        stage = new Stage(SlimeGame.getGame().getUiCamera().getViewport(), SlimeGame.getGame().getSpriteBatch());
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
    }


    @Override
    public void start() {
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
        // VfxManager manages internal off-screen buffers,
        // which should always match the required viewport (whole screen in our case).
        SlimeGame.getGame().getPostProcess().getVfxManager().resize(width, height);

        getGameScreen().getCamera().getViewport().update(width, height);
        getGameScreen().getCamera().getCamera().position.set(getGameScreen().getCamera().getCamera().viewportWidth / 2, getGameScreen().getCamera().getCamera().viewportHeight / 2, 0);
        getGameScreen().getCamera().getCamera().update();
//        SlimeGame.getGame().getUiCamera().getCamera().viewportHeight = height;
//        SlimeGame.getGame().getUiCamera().getCamera().viewportWidth = width;
//        SlimeGame.getGame().getUiCamera().getCamera().update();
//
////        camera.getViewport().setScreenWidth(Gdx.graphics.getWidth());
////        camera.getViewport().setScreenWidth(Gdx.graphics.getHeight());
////        camera.getViewport().apply();
//
//        stage.getViewport().update(width, height, true);
//        getGameScreen().getCamera().getViewport().update(width, height);
    }


    @Override
    public void onUpdate(float dTime) {
        stage.act();

        if (KInput.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            SlimeGame.getGame().getLevelHandler().advanceLevel();
            changeState(Running.class);
        }
        ControllerInput controller = EngineManager.get().getControllerInput();
        if (controller.isButtonJustPressed(XboxOneGamepad.X) || controller.isButtonJustPressed(XboxOneGamepad.B) ||
                controller.isButtonJustPressed(XboxOneGamepad.Y) || controller.isButtonJustPressed(XboxOneGamepad.A)) {
            SlimeGame.getGame().getLevelHandler().advanceLevel();
            changeState(Running.class);
        }
    }

    @Override
    protected void end() {

    }

    @Override
    public void render(SpriteBatch batch, float dTime) {
        SlimeGame.getGame().getPostProcess().getVfxManager().beginCapture();

        Gdx.gl.glClearColor(168 / 255f, 118 / 255f, 86 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        SlimeGame.getGame().getLevelHandler().render(batch, EngConst.STEP_TIME);
        getGameScreen().getFrameRate().render(batch, EngConst.STEP_TIME);
        getGameScreen().getVersionInfo().render(batch, EngConst.STEP_TIME);

        // End render to an off-screen buffer.
        SlimeGame.getGame().getPostProcess().getVfxManager().endCapture();

        // Perform effect chain processing and render result to the screen.
        SlimeGame.getGame().getPostProcess().getVfxManager().render();


        SlimeGame.getGame().getSpriteBatch().end();
        stage.draw();
        SlimeGame.getGame().getSpriteBatch().begin();
    }
}
