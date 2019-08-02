package com.ktar5.slime.tools.levelselectioneditor.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.slime.tools.levelselectioneditor.points.PathPoint;
import com.ktar5.slime.tools.levelselectioneditor.points.Point;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.ZoomablePannableWidget;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

public class SceneRenderer extends ZoomablePannableWidget {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;

    private final Rectangle clip = new Rectangle();

    @Getter
    private Scene scene;
    public TextureRegion textureRegion;

    public SceneRenderer() {
        super();
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setAutoShapeType(true);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ArrayList<Path> arrayList = new ArrayList<>(scene.getPaths().values());
                Path path = arrayList.get(0);
                if (path != null) {
                    System.out.println();
                    System.out.println("Unfixed: " + (x - getRenderX()) + " , " + (y - getRenderY()));
                    System.out.println(scale);

                    int xFixed = (int) ((x - getRenderX()) / scale);
                    int yFixed = (int) ((y - getRenderY()) / scale);

                    System.out.println("Fixed: " + xFixed + " , " + yFixed);

                    PathPoint point = new PathPoint(5, xFixed, yFixed);
                    path.addPoint(point);
                }
            }
        });
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        this.textureRegion = new TextureRegion(scene.getTexture());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (scene == null) {
            return;
        }

        batch.flush();
        if (!ScissorStack.pushScissors(clip.set(getX(), getY(), getWidth(), getHeight()))) {
            return;
        }

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        if (!batch.isDrawing()) {
            batch.begin();
        }
        //Draw the background texture for reference when making paths
        batch.draw(textureRegion, getRenderX(), getRenderY(), 0, 0, scene.getTexture().getWidth(),
                scene.getTexture().getHeight(), scale, scale, 0);
        batch.end();

        //Draw the paths
        shapeRenderer.begin();
        Collection<Path> values = scene.getPaths().values();
        for (Path value : values) {
            if (value.getFirstPoint() != null) {
                shapeRenderer.setColor(Color.BLUE);
                PathPoint current = value.getFirstPoint();

                //Draw lines between each of the points and draw the points themselves
                while (current != null) {
                    int x = (int) (getRenderX() + (current.getX() * scale));
                    int y = (int) (getRenderY() + (current.getY() * scale));
                    shapeRenderer.circle(x, y, 3 * scale);

                    if(current.equals(value.getFirstPoint()) && value.getStart() != null){
                        Point start = value.getStart();
                        shapeRenderer.line(x, y, (int) (getRenderX() + (start.getX() * scale)), (int) (getRenderY() + (start.getY() * scale)));
                    }
                    if(current.equals(value.getLastPoint()) && value.getEnd() != null){
                        Point end = value.getEnd();
                        shapeRenderer.line(x, y, (int) (getRenderX() + (end.getX() * scale)), (int) (getRenderY() + (end.getY() * scale)));
                    }

                    //Move on to next point and draw the line to the next point at the same time
                    current = current.getNext();
                    if(current != null){
                        shapeRenderer.line(x, y, (int) (getRenderX() + (current.getX() * scale)), (int) (getRenderY() + (current.getY() * scale)));
                    }

                }
            }

            //Draw the start and the end points of the path
            shapeRenderer.setColor(Color.GREEN);
            if (value.getStart() != null) {
                shapeRenderer.circle((value.getStart().getX() * scale) + getRenderX(), (value.getStart().getY() * scale) + getRenderY(), 5 * scale);
            }
            shapeRenderer.setColor(Color.RED);
            if (value.getEnd() != null) {
                shapeRenderer.circle((value.getEnd().getX() * scale) + getRenderX(), (value.getEnd().getY() * scale) + getRenderY(), 5 * scale);
            }

        }
        shapeRenderer.end();

        ScissorStack.popScissors();

        batch.begin();
    }


    @Override
    public float getContentCenterX() {
        if (scene == null) {
            return 0;
        }
        return getRenderX() + (scene.getTexture().getWidth() * scale / 2f);
    }

    @Override
    public float getContentCenterY() {
        if (scene == null) {
            return 0;
        }
        return getRenderY() + (scene.getTexture().getHeight() * scale / 2f);
    }
}
