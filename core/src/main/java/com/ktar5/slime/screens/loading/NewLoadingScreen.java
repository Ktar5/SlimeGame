package com.ktar5.slime.screens.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.analytics.MongoDBInstance;
import com.ktar5.gameengine.camera.CameraBase;
import com.ktar5.gameengine.core.AbstractScreen;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.entities.EntityTweenAccessor;
import com.ktar5.gameengine.tweenengine.Tween;
import com.ktar5.gameengine.tweenengine.TweenAccessor;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.mainmenu.MainMenuScreen;
import com.ktar5.slime.world.level.LevelHandler;
import com.ktar5.slime.world.tiles.RetractingSpikes;

import static com.ktar5.slime.SlimeGame.DEVELOPER_MODE;


public class NewLoadingScreen extends AbstractScreen {
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    int i = 0;
    int frames = 1;

    public NewLoadingScreen(CameraBase camera) {
        super(camera);

        atlas = new TextureAtlas("textures/skins/pixel/skin.atlas");
        skin = new Skin(Gdx.files.internal("textures/skins/pixel/skin.json"), atlas);

        stage = new Stage(getCamera().getViewport(), SlimeGame.getGame().getSpriteBatch());
    }

    FloatClass alpha = new FloatClass();
    Tween start;

    @Override
    public void show() {
        stage.addActor(new Actor() {
            Texture texture = EngineManager.get().getAnimationLoader().getTexture("textures/MenuBG_Loading.png");

            @Override
            public void draw(Batch batch, float parentAlpha) {
                stage.getBatch().draw(texture, 0, 0, 480, 270);
            }
        });
//        SlimeGame.getGame().getSpriteBatch().setColor(1, 1, 1, 0);
//        Tween.registerAccessor(FloatClass.class, new FloatAccessor());
//        start = Tween.to(alpha, 1, 1f)
//                .target(1)
//                .ease(Quint.OUT)
//                .start(EngineManager.get().getTweenManager());
    }

    @Override
    public void render(float delta) {
        if (i >= frames) {
            load();
            EngineManager.get().getGame().setScreen(new MainMenuScreen());
        }
        i++;

        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        SlimeGame.getGame().getSpriteBatch().end();

//        SlimeGame.getGame().getSpriteBatch().setColor(1, 1, 1, alpha.value);
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
//        EngineManager.get().getTweenManager().update(dTime);
//        if(alpha.value == 1f){
//            load();
////            EngineManager.get().getGame().setScreen(new MainMenuScreen());
//
//            Tween.to(alpha, 1, 2f)
//                    .target(0)
//                    .setCallback((type2, source2) -> {
//                        if (type2 == TweenCallback.COMPLETE) {
//                            EngineManager.get().getGame().setScreen(new MainMenuScreen());
//                        }
//                    })
//                    .ease(Quint.IN)
//                    .start(EngineManager.get().getTweenManager());
//        }
    }


    public void load() {
        VisUI.load();

        EngineManager.get().getConsole().setDisplayKeyID(Input.Keys.GRAVE);
        Tween.registerAccessor(Entity.class, new EntityTweenAccessor());
        Tween.registerAccessor(RetractingSpikes.class, new RetractingSpikes.SpikesTweenAccessor());
        MongoDBInstance mongoDBInstance = new MongoDBInstance("mongodb+srv://analytics:test@cluster0-k5pjp.mongodb.net/test?retryWrites=true", "test");
        Preferences slimegame = Gdx.app.getPreferences("com.ktar5.slimegame");
        String build_id = "0.1.0";
        if (DEVELOPER_MODE) {
            build_id = "developer";
        }
        Analytics.create(slimegame, mongoDBInstance, build_id, 2, 3);
        EngineManager.get().getAnimationLoader().loadAtlas("textures/player/Slime.atlas");
        SlimeGame.getGame().setLevelHandler(new LevelHandler());
    }

    private class FloatAccessor implements TweenAccessor<FloatClass> {

        @Override
        public int getValues(FloatClass target, int tweenType, float[] returnValues) {
            returnValues[0] = target.value;
            return 1;
        }

        @Override
        public void setValues(FloatClass target, int tweenType, float[] newValues) {
            target.value = newValues[0];
        }
    }

    public class FloatClass {
        float value = 0;
    }

}

