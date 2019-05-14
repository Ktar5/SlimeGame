package com.ktar5.slime.analytics;

import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import org.bson.Document;

public class LevelFailEvent extends LevelEvent {
    private int positionX;
    private int positionY;
    private String cause;
    private Side directionMoving;

    public LevelFailEvent(JumpPlayer player, String cause) {
        super(player.getLevel());

        this.cause = cause;

        this.positionX = (int) player.getPosition().x / 16;
        this.positionY = (int) player.getPosition().y / 16;
        this.directionMoving = player.getLastMovedDirection();
    }

    @Override
    public String getSubEventName() {
        return "fail";
    }

    @Override
    public Document getData() {
        Document data = super.getData();
        return data
                .append("move_dir", directionMoving.name())
                .append("pos_x", positionX)
                .append("pos_y", positionY)
                .append("cause", cause);
    }
}
