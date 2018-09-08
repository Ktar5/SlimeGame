package com.ktar5.slime.world.tiles;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Teleporter extends WholeTile {
    private int x;
    private int y;

    public Teleporter(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void giveProperties(MapProperties properties) {
        String location = properties.get("location", String.class);
        String[] split = location.split(",");
        x = Integer.valueOf(split[0]);
        y = Integer.valueOf(split[1]);
    }

    @Override
    public void reset() {

    }

    @Override
    public void onPlayerHitTile(JumpPlayer player, Side hit) {
        player.position.set(x, player.getLevel().getGrid().height - y - 1);
    }

    @Override
    public void onPlayerCross(JumpPlayer player) {

    }

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return false;
    }
}
