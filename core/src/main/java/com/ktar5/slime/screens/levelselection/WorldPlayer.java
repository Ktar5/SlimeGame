package com.ktar5.slime.screens.levelselection;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.input.ControllerInput;
import com.ktar5.gameengine.input.devices.JamPad;
import com.ktar5.gameengine.util.Renderable;
import com.ktar5.slime.KInput;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.GameScreen;
import com.ktar5.slime.screens.levelselection.pathing.ControlPoint;
import com.ktar5.slime.screens.levelselection.pathing.Path;
import com.ktar5.slime.screens.levelselection.pathing.Path.PathDirection;
import com.ktar5.slime.screens.levelselection.pathing.PathPoint;

import java.util.UUID;

public class WorldPlayer implements Renderable {
    private World world;
    private int x, y;
    private LevelLocation location;

    private float unitsX, unitsY;
    private TextureRegion currentFrame;

    public WorldPlayer(World world) {
        this.world = world;
        unitsX = 16;
        unitsY = 16;

        currentFrame = null;

        location = new LevelLocation();

        ControlPoint controlPoint = world.getControlPoints().get(world.getStartingControlPoint());
        location.controlPointLastAt = controlPoint;
        x = controlPoint.getX();
        y = controlPoint.getY();
    }

    @Override
    public void render(SpriteBatch batch, float dTime) {
//        batch.draw(currentFrame,
//                (int) (x - (currentFrame.getRegionWidth() / (2))),
//                (int) (y - (currentFrame.getRegionHeight() / (2))),
//                currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2,
//                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
//                unitsX / (currentFrame.getRegionWidth()),
//                unitsY / currentFrame.getRegionHeight(),
//                0);
        batch.end();
        SlimeGame.getGame().getShapeRenderer().setAutoShapeType(true);
        SlimeGame.getGame().getShapeRenderer().begin();
        SlimeGame.getGame().getShapeRenderer().circle(x, y, 12);
        SlimeGame.getGame().getShapeRenderer().end();
        batch.begin();
    }

    //TODO finish
    public void update(float dTime) {
        if (location.isIdle()) {
            ControllerInput cInp = EngineManager.get().getControllerInput();
            if (KInput.isKeyJustPressed(Keys.ENTER) || cInp.isButtonJustPressed(JamPad.A, JamPad.START, JamPad.SELECT)) {
                String data = location.controlPointLastAt.getData();
                int levelID = Integer.parseInt(data);
                SlimeGame.getGame().getLevelHandler().setLevel(levelID);
                SlimeGame.getGame().setScreen(new GameScreen());
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
                }else{
                    location.pathDirection = null;
                    return;
                }

                if (pathID == null || world.getPaths().get(pathID) == null) {
                    return;
                }

                Path path = world.getPaths().get(pathID);
                location.pathDirection = path.getPathDirection(location.controlPointLastAt.getControlID());
                System.out.println(location.pathDirection.firstPoint);
                location.nextPPoint = location.pathDirection.firstPoint;
            }
        } else if(location.nextPPoint != null){
            //TODO probably hundreds of problems here
            System.out.println("dsadsadsdsadassadsdaasdsda");
            System.out.println(location == null);
            System.out.println(location.pathDirection == null);
            System.out.println(location.nextPPoint == null);
            if ((location.pathDirection.forward && location.nextPPoint.getNext() == null)
                    || (!location.pathDirection.forward && location.nextPPoint.getPrev() == null)) {
                boolean finished = moveTowards(new Vector2(location.pathDirection.end.getX(), location.pathDirection.end.getY()), SPEED, true);
                if(finished){
                    System.out.println("finished");
                    location.controlPointLastAt = location.pathDirection.end;
                    location.nextPPoint = null;
                    location.pathDirection = null;
                }
            } else {
                moveTowards(new Vector2(location.nextPPoint.getX(), location.nextPPoint.getY()), SPEED, false);
            }
            //Queue up movement
        }
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
                moveTowards(new Vector2(location.pathDirection.end.getX(), location.pathDirection.end.getY()), extra, true);
            } else {
                moveTowards(new Vector2(location.nextPPoint.getX(), location.nextPPoint.getY()), extra, false);
            }
        } else {
            Vector2 movement = unit.scl(amount);
            x += movement.x;
            y += movement.y;
        }
        return false;
    }

    private static final float SPEED = 3.0f;

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
