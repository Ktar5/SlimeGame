package com.ktar5.slime.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ktar5.gameengine.core.AbstractScreen;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.slime.KInput;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.hotkeys.GeneralHotkeys;
import com.ktar5.slime.screens.GameScreen;
import com.ktar5.slime.world.level.Chapter;
import com.ktar5.slime.world.level.LevelHandler;

public class LevelSelectionScreen extends AbstractScreen {
    protected Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;

    private int currentChapter = 0;

    private Table chapterTable;

    public LevelSelectionScreen() {
        super(SlimeGame.getGame().getUiCamera());

        atlas = new TextureAtlas("textures/skins/pixel/skin.atlas");
        skin = new Skin(Gdx.files.internal("textures/skins/pixel/skin.json"), atlas);

        camera.getCamera().position.set(camera.getCamera().viewportWidth / 2, camera.getCamera().viewportHeight / 2, 0);
        camera.getCamera().update();

        stage = new Stage(getCamera().getViewport(), SlimeGame.getGame().getSpriteBatch());
    }


    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, SlimeGame.getGame().getInput()));

        stage.getActors().clear();
        stage.clear();

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.bottom().center();
        mainTable.pad(0, 25, 25, 0);

        loadChapter(currentChapter);
        mainTable.add(chapterTable).row();


        //Create buttons
        TextButton backButton = new TextButton("Back to Main Menu", skin);

        //Add listeners to buttons
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SlimeGame.getGame().setScreen(new MainMenuScreen());
            }
        });

        //Add buttons to table
        mainTable.add(backButton).pad(0, 0, 0, 0);

        //Add table to stage
        stage.addActor(new Actor() {
            Texture texture = EngineManager.get().getAnimationLoader().getTexture("textures/MenuBG.png");

            @Override
            public void draw(Batch batch, float parentAlpha) {
                stage.getBatch().draw(texture, 0, 0, 480, 270);
            }
        });
        stage.addActor(mainTable);


    }

    public void loadChapter(int chapterID) {
        final LevelHandler levelHandler = SlimeGame.getGame().getLevelHandler();
        Chapter chapter = levelHandler.getChapters().get(chapterID % levelHandler.getChapters().size());

        Table topBarTable = new Table();

        TextButton leftButton = new TextButton("<", skin);
        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentChapter -= 1;
                if (currentChapter == -1) {
                    currentChapter = levelHandler.getChapters().size();
                }
                loadChapter(currentChapter);
            }
        });
        TextButton rightButton = new TextButton(">", skin);
        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentChapter += 1;
                if(currentChapter == levelHandler.getChapters().size()){
                    currentChapter = 0;
                }
                loadChapter(currentChapter + 1);
            }
        });
        Label textLabel = new Label(chapter.name, skin);

        topBarTable.add(leftButton).padRight(25);
        topBarTable.add(textLabel).padRight(25);
        topBarTable.add(rightButton);
        topBarTable.center();

        if (chapterTable == null) {
            chapterTable = new Table().center();
        } else {
            chapterTable.clear();
        }
        chapterTable.debugAll();
        chapterTable.padTop(25);

        chapterTable.add(topBarTable).center();
        chapterTable.row();


        Table levels = new Table();
        levels.center();
        levels.pad(15, 0, 0, 0);

        for (int i = chapter.firstLevelID; i <= chapter.lastLevelID; i++) {
            TextButton button = new TextButton(String.valueOf(i), skin);
            if (levelHandler.isLevelNull(i)) {
                button.setDisabled(true);
            } else {
                int finalI = i;
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        SlimeGame.getGame().setScreen(new GameScreen());
                        levelHandler.setLevel(finalI);
                    }
                });
            }
            if (i % 10 == 0) {
                levels.row();
            }
            levels.add(button).pad(0, 0, 20, 10).width(25);
        }

        chapterTable.add(levels);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        SlimeGame.getGame().getSpriteBatch().end();
        stage.draw();
        SlimeGame.getGame().getSpriteBatch().begin();
    }

    @Override
    public void resize(int width, int height) {
        getCamera().getViewport().update(width, height);
        getCamera().getCamera().position.set(getCamera().getCamera().viewportWidth / 2, getCamera().getCamera().viewportHeight / 2, 0);
        getCamera().getCamera().update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
    }

    @Override
    public void update(float dTime) {
        GeneralHotkeys.update();
        KInput.update();
    }
}
