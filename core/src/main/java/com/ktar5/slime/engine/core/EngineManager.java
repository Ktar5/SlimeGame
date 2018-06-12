package com.ktar5.slime.engine.core;

import com.badlogic.gdx.utils.Disposable;
import com.ktar5.slime.engine.animation.AnimationLoader;
import com.ktar5.slime.engine.camera.CameraBase;
import com.ktar5.slime.engine.cooldown.CooldownManager;
import com.ktar5.slime.engine.events.GameEvent;
import com.ktar5.slime.engine.rendering.RenderManager;
import com.ktar5.slime.engine.tweenengine.TweenManager;
import lombok.Getter;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.config.IBusConfiguration;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.FileWriter;

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

    private final RenderManager renderManager;

    private EngineManager(G game) {
        initializeLogger();
        this.game = game;
        this.tweenManager = new TweenManager();
        this.cameraBase = game.initializeCameraBase();
        this.eventBus = this.initializeEventBus();

        this.renderManager = new RenderManager();

        this.animationLoader = new AnimationLoader();
        this.cooldownManager = new CooldownManager();
    }

    private void initializeLogger() {
        Configurator.defaultConfig()
                .writer(new ConsoleWriter())
                .level(Level.DEBUG)
                .addWriter(new FileWriter("log.txt"))
                //{date:yyyy-MM-dd HH:mm:ss} {class}.{method}()\n{level}: {message}
                .formatPattern("{date:mm:ss:SSS} {class_name}.{method}() [{level}]: {message}")
                .activate();
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
