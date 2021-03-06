package com.ktar5.slime.world.tiles;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.tweenengine.Tween;
import com.ktar5.gameengine.tweenengine.TweenCallback;
import com.ktar5.gameengine.tweenengine.equations.Quint;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.Teleportable;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;
import org.tinylog.Logger;

public class Teleporter extends WholeGameTile {
    public static final float TELEPORT_SPEED = .1f;

    private int x;
    private int y;

    private Tween tween;

    public Teleporter(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void processProperty(MapProperties properties) {
        String location = properties.get("location", String.class);
        String[] split = location.split(",");
        x = Integer.valueOf(split[0]);
        y = Integer.valueOf(split[1]);
    }

    @Override
    public void reset() {
        if (tween != null && tween.isStarted() && !tween.isFinished()){
            Logger.debug("Killing tween");
            tween.kill();
        }
    }

    @Override
    public boolean preMove(Entity entity) {
        if (!(entity instanceof Teleportable)) {
            return true;
        }

        entity.setHaltMovement(true);
        ((Teleportable) entity).setTeleporting(true);

        Vector2 target = new Vector2((x * 16) + 8, 8 + (16 * (SlimeGame.getGame().getLevelHandler().getCurrentLevel().getHeight() - y - 1)));
//        target.x += entity.getLastMovedDirection().x;
//        target.y += entity.getLastMovedDirection().y;
        float dst = target.dst(entity.getPosition()) / 16;

        tween = Tween.to(entity, 1, dst * TELEPORT_SPEED)
                .target(target.x, target.y)
                .setCallback((type, source) -> {
                    if (type == TweenCallback.COMPLETE) {
                        entity.position.set(target);
                        entity.setHaltMovement(false);
                    }
                })
                .ease(Quint.OUT)
                .start(EngineManager.get().getTweenManager());
        return false;
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }
}
