package com.ktar5.slime.tools.levelselectioneditor.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.slime.tools.levelselectioneditor.points.ControlPoint;
import com.ktar5.slime.tools.levelselectioneditor.points.PathPoint;
import com.ktar5.slime.tools.levelselectioneditor.points.Point;
import com.ktar5.slime.tools.levelselectioneditor.scene.Scene;
import com.ktar5.slime.tools.levelselectioneditor.scene.SceneRenderer;
import com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.EditMode;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.ZoomablePannableWidget;
import com.ktar5.utilities.common.constants.Direction;

import java.util.Map;
import java.util.UUID;

public class Input implements InputProcessor {

    public static InputMode inputMode = InputMode.NONE;

    public Input() {

    }

    //TODO TEST CONTROL START AND CONTROL END REMOVAL AND ADDING AND SELECTING
    public static void handleClick(int x, int y) {
        System.out.println(inputMode);
        switch (inputMode) {
            case SELECT_POINT:
                Input.selectClosestPoint(x, y);
                break;
            case CREATE_POINT:
                createPoint(x, y, Main.getInstance().mainStage.getSidebar().getSelectedPath());
                break;
            case CREATE_CONTROL_POINT:
                createControlPoint(x, y);
                break;
            case SELECT_START:
                ControlPoint controlPoint = selectClosestControlPoint(x, y);
                float angle = new Vector2(x - controlPoint.getX(), y - controlPoint.getY()).angle();
                Direction direction = Direction.fromAngleCardinal(angle);
                Main.getInstance().mainStage.getSidebar().getSelectedPath().setControlStart(controlPoint.getControlID(), direction, false);
                break;
            case SELECT_END:
                System.out.println("ABC");
                controlPoint = selectClosestControlPoint(x, y);
                angle = new Vector2(x - controlPoint.getX(), y - controlPoint.getY()).angle();
                direction = Direction.fromAngleCardinal(angle);
                System.out.println(angle);
                System.out.println(direction);
                Main.getInstance().mainStage.getSidebar().getSelectedPath().setControlEnd(controlPoint.getControlID(), direction, false);
                break;
        }
    }

    private static void createControlPoint(int x, int y) {
        ControlPoint controlPoint = new ControlPoint("No Data", x, y);
        Main.getInstance().mainStage.getSceneRenderer().getScene().getControlPoints().put(controlPoint.getControlID(), controlPoint);
    }

    public static ControlPoint selectClosestControlPoint(int x, int y) {
        SceneRenderer sceneRenderer = Main.getInstance().mainStage.getSceneRenderer();
        Scene scene = sceneRenderer.getScene();
        Map<UUID, Path> paths = scene.getPaths();

        float distance2 = Float.MAX_VALUE;
        ControlPoint closestPoint = null;
        for (ControlPoint point : scene.getControlPoints().values()) {
            float tempDist = dist2(point.getX(), point.getY(), x, y);
            if (tempDist < distance2) {
                closestPoint = point;
                distance2 = tempDist;
            }
        }
        return closestPoint;
    }

    public static void handleMovePoint() {
        //TODO
    }

    public boolean isKeyPressed(int key) {
        return Gdx.input.isKeyPressed(key);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public static void createPoint(int x, int y, Path path) {
        PathPoint point = new PathPoint(path, x, y);
        path.addPoint(point);
    }

    public static void selectClosestPoint(int x, int y) {
        if (Main.getInstance().mainStage.getSidebar().getEditMode().equals(EditMode.POINT)) {
            SceneRenderer sceneRenderer = Main.getInstance().mainStage.getSceneRenderer();
            Scene scene = sceneRenderer.getScene();
            Map<UUID, Path> paths = scene.getPaths();

            float distance2 = Float.MAX_VALUE;
            Point closestPoint = null;
            for (ControlPoint point : scene.getControlPoints().values()) {
                float tempDist = dist2(point.getX(), point.getY(), x, y);
                if (tempDist < distance2) {
                    closestPoint = point;
                    distance2 = tempDist;
                }
            }
            for (Path path : paths.values()) {
                PathPoint point = path.getFirstPoint();
                while (point != null) {
                    float tempDist = dist2(point.getX(), point.getY(), x, y);
                    if (tempDist < distance2) {
                        closestPoint = point;
                        distance2 = tempDist;
                    }
                    point = point.getNext();
                }
            }

            Main.getInstance().mainStage.getSidebar().setPointSidebar(closestPoint);
        }
    }

    public static float dist2(float x1, float y1, float x2, float y2) {
        return ((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2));
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println();

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        Actor hit = Main.getInstance().mainStage.hit(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), false);
        if (hit instanceof ZoomablePannableWidget) {
            ((ZoomablePannableWidget) hit).scale(amount);
        }
        return true;
    }
}

