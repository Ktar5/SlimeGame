package com.ktar5.slime.analytics;

import com.ktar5.slime.entities.player.JumpPlayer;

public class LevelResetEvent extends LevelFailEvent {

    public LevelResetEvent(JumpPlayer player, String cause) {
        super(player, cause);
    }

    @Override
    public String getSubEventName() {
        return "reset";
    }

}
