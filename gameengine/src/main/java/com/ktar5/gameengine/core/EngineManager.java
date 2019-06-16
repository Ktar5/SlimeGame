package com.ktar5.gameengine.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;
import com.ktar5.gameengine.animation.AnimationLoader;
import com.ktar5.gameengine.camera.CameraBase;
import com.ktar5.gameengine.console.Console;
import com.ktar5.gameengine.console.GUIConsole;
import com.ktar5.gameengine.cooldown.CooldownManager;
import com.ktar5.gameengine.events.GameEvent;
import com.ktar5.gameengine.tweenengine.TweenManager;
import lombok.Getter;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.config.IBusConfiguration;
import org.tinylog.Logger;
import org.tinylog.jul.JulTinylogBridge;

@Getter
public class EngineManager<G extends AbstractGame<G>> implements Disposable {
    private static EngineManager<?> instance;

    private G game;
    /**
     * {@link MBassador#publish(Object)} and {@link MBassador#subscribe(Object)}
     */
    private final MBassador<GameEvent> eventBus;
    private CameraBase cameraBase;

    private TweenManager tweenManager;
    private AnimationLoader animationLoader;
    private CooldownManager cooldownManager;
    private AssetManager assetManager;

    private Console console;

    public void setCamera(CameraBase camera){
        this.cameraBase = camera;
    }

    private EngineManager(G game) {
        initializeLogger();
        assetManager = new AssetManager();

        this.console = new GUIConsole();
        this.console.setCommandExecutor(game.getCommandExecutor());
        
        this.game = game;
        this.tweenManager = new TweenManager();
        this.cameraBase = game.initializeCameraBase();
        this.eventBus = this.initializeEventBus();

        this.animationLoader = new AnimationLoader();
        this.cooldownManager = new CooldownManager();
    }

    private void initializeLogger() {
        JulTinylogBridge.activate();
    }

    private MBassador<GameEvent> initializeEventBus() {
        return new MBassador<>(new BusConfiguration()
                .addFeature(Feature.SyncPubSub.Default())
                .addFeature(Feature.AsynchronousMessageDispatch.Default())
                .addFeature(Feature.AsynchronousHandlerInvocation.Default())
                .addPublicationErrorHandler(Logger::error)
                .setProperty(IBusConfiguration.Properties.BusId, "Game Event Bus"));
    }

    public static synchronized EngineManager<?> get() {
        if (instance == null) {
            throw new RuntimeException("Initialize EngineManager first, nerd.");
        }
        return instance;
    }

    @Override
    public void dispose() {
        eventBus.shutdown();
        game.dispose();
    }


    public static <A extends AbstractGame<A>> EngineManager<A> initialize(A game) {
        EngineManager<A> abEngineManager = new EngineManager<>(game);
        instance = abEngineManager;
        return abEngineManager;
    }
}
