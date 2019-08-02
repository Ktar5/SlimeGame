package com.ktar5.slime.tools.levelselectioneditor.points;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.KSerializeable;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Setter
@Getter
public abstract class Point implements KSerializeable {
    private int x, y;
    private int id;

    public Point(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Point(JSONObject json) {
        this.id = json.getInt("id");
        this.x = json.getInt("x");
        this.y = json.getInt("y");
    }

    @Override
    public JSONObject serialize() {
        return new JSONObject().put("x", x).put("y", y).put("id", id);
    }

    public void render(ShapeRenderer renderer) {
        renderer.setColor(Color.BLUE);
        renderer.circle(x, y, 2);
    }
}
