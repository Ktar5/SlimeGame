package com.ktar5.slime.screens.levelselection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.input.ControllerInput;
import com.ktar5.gameengine.input.KInput;
import com.ktar5.gameengine.input.devices.JamPad;
import com.ktar5.gameengine.util.Renderable;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.GameScreen;
import com.ktar5.slime.screens.levelselection.pathing.ControlPoint;
import com.ktar5.slime.screens.levelselection.pathing.Path;
import com.ktar5.slime.screens.levelselection.pathing.Path.PathDirection;
import com.ktar5.slime.screens.levelselection.pathing.PathPoint;
import lombok.Getter;

import java.util.UUID;

public class WorldPlayer implements Renderable {
    private World world;
    private float x, y;
    private LevelLocation location;

    private float unitsX, unitsY;
    private TextureRegion currentFrame;

    @Getter
    private ControlPoint controlPointToOrAt;

    public WorldPlayer(World world) {
        this.world = world;
        unitsX = 16;
        unitsY = 16;

        currentFrame = EngineManager.get().getAnimationLoader().getAnimation("slime_jump_down").getKeyFrame(0);

        location = new LevelLocation();

        ControlPoint controlPoint = world.getControlPoints().get(world.getStartingControlPoint());
        location.controlPointLastAt = controlPoint;
        controlPointToOrAt = controlPoint;
        x = controlPoint.getX();
        y = controlPoint.getY();
    }

    @Override
    public void render(SpriteBatch batch, float dTime) {
        batch.draw(currentFrame,
                (int) (x - (currentFrame.getRegionWidth() / (2))),
                (int) (y - (currentFrame.getRegionHeight() / (2))),
                currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2,
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
                unitsX / (currentFrame.getRegionWidth()),
                unitsY / currentFrame.getRegionHeight(),
                0);
    }

    //TODO finish
    int frames = 0;

    public void update(float dTime) {
        if (location.isIdle()) {
            ControllerInput cInp = EngineManager.get().getControllerInput();
            if ((KInput.isKeyJustPressed(Keys.ENTER) || cInp.isButtonJustPressed(JamPad.A, JamPad.START, JamPad.SELECT)) &&
                    (Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT))) {
                if (location.controlPointLastAt.hasLevel()) {
                    SlimeGame.getGame().getLevelHandler().setLevel(location.controlPointLastAt.getLevelID());
                    SlimeGame.getGame().setScreen(new GameScreen());
                }
            } else {
                UUID pathID = null;
                if (KInput.isKeyJustPressed(Keys.W, Keys.UP) || cInp.isButtonJustPressed(JamPad.DPAD_UP)) {
                    pathID = location.controlPointLastAt.getPathUp();
                } else if (KInput.isKeyJustPressed(Keys.A, Keys.LEFT) || cInp.isButtonJustPressed(JamPad.DPAD_LEFT)) {
                    pathID = location.controlPointLastAt.getPathLeft();
                } else if (KInput.isKeyJustPressed(Keys.S, Keys.DOWN) || cInp.isButtonJustPressed(JamPad.DPAD_DOWN)) {
                    pathID = location.controlPointLastAt.getPathDown();
                } else if (KInput.isKeyJustPressed(Keys.D, Keys.RIGHT) || cInp.isButtonJustPressed(JamPad.DPAD_RIGHT)) {
                    pathID = location.controlPointLastAt.getPathRight();
                } else {
                    location.pathDirection = null;
                    return;
                }
                frames = 0;
                if (pathID == null || world.getPaths().get(pathID) == null) {
                    return;
                }

                Path path = world.getPaths().get(pathID);
                location.pathDirection = path.getPathDirection(location.controlPointLastAt.getControlID());
                controlPointToOrAt = location.pathDirection.end;
//                System.out.println(location.pathDirection);

//                System.out.println(location.pathDirection.firstPoint);
                location.nextPPoint = location.pathDirection.firstPoint;
            }
        } else {
            frames += 1;
            boolean finished;
            if (location.nextPPoint == null) {
                finished = moveTowards(new Vector2(location.pathDirection.end.getX(), location.pathDirection.end.getY()), SPEED * SlimeGame.DPERCENT, true);
            } else if ((location.pathDirection.forward && location.nextPPoint.getNext() == null) || (!location.pathDirection.forward && location.nextPPoint.getPrev() == null)) {
                finished = moveTowards(new Vector2(location.pathDirection.end.getX(), location.pathDirection.end.getY()), SPEED * SlimeGame.DPERCENT, true);
            } else {
                finished = moveTowards(new Vector2(location.nextPPoint.getX(), location.nextPPoint.getY()), SPEED * SlimeGame.DPERCENT, false);
            }
            if (finished) {
//                System.out.println("finished w/ " + frames + " frames");
                location.controlPointLastAt = location.pathDirection.end;
                location.nextPPoint = null;
                location.pathDirection = null;
            }

            //Queue up movement
        }
    }

    public float calculatePathLength(Path path) {
        float length = 0;
        ControlPoint start = path.getStart();
        Vector2 firstPoint = new Vector2(path.getFirstPoint().getX(), path.getFirstPoint().getY());
        Vector2 startPoint = new Vector2(start.getX(), start.getY());
        length += firstPoint.cpy().sub(startPoint).len();

        PathPoint current = path.getFirstPoint().getNext();
        Vector2 previousVector = new Vector2();
        Vector2 currentVector = new Vector2();
        while (current != null) {
            previousVector = new Vector2(current.getPrev().getX(), current.getPrev().getY());
            currentVector = new Vector2(current.getX(), current.getY());
            length += currentVector.sub(previousVector).len();
            current = current.getNext();
        }

        ControlPoint end = path.getEnd();
        Vector2 lastPoint;
        if (path.getLastPoint() == null) {
            lastPoint = new Vector2(path.getFirstPoint().getX(), path.getFirstPoint().getY());
        } else {
            lastPoint = new Vector2(path.getLastPoint().getX(), path.getLastPoint().getY());
        }
        Vector2 endPoint = new Vector2(end.getX(), end.getY());
        length += endPoint.cpy().sub(lastPoint).len();

        return length;
    }

    /**
     * @return true if we stopped
     */
    private boolean moveTowards(Vector2 future, float amount, boolean stopOnReach) {
        Vector2 current = new Vector2(x, y);
        Vector2 delta = future.cpy().sub(current);
        Vector2 unit = delta.cpy().nor();
        float extra = amount - Math.min(amount, delta.len());

        //If we hit the last one and go over it, we stop upon reaching it
        if (extra > 0) {
            x = (int) future.x;
            y = (int) future.y;
            if (stopOnReach) {
                return true;
            }
            if (location.pathDirection.forward) {
                location.nextPPoint = location.nextPPoint.getNext();
            } else {
                location.nextPPoint = location.nextPPoint.getPrev();
            }

            if (location.nextPPoint == null) {
                return moveTowards(new Vector2(location.pathDirection.end.getX(), location.pathDirection.end.getY()), extra, true);
            } else {
                return moveTowards(new Vector2(location.nextPPoint.getX(), location.nextPPoint.getY()), extra, false);
            }
        } else {
            Vector2 movement = unit.scl(amount);
            x += movement.x;
            y += movement.y;
        }
        return false;
    }

    private static final float SPEED = 2f;

    public void setFrame(TextureRegion texture) {
//        singleFrameMode = true;
        this.currentFrame = texture;
    }

    private class LevelLocation {
        private ControlPoint controlPointLastAt;
        private PathPoint nextPPoint;
        private PathDirection pathDirection;

        public boolean isIdle() {
            return pathDirection == null;
        }
    }
}
