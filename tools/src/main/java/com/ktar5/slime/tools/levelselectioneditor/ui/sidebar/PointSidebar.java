package com.ktar5.slime.tools.levelselectioneditor.ui.sidebar;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.slime.tools.levelselectioneditor.points.ControlPoint;
import com.ktar5.slime.tools.levelselectioneditor.points.DataPoint;
import com.ktar5.slime.tools.levelselectioneditor.points.PathPoint;
import com.ktar5.slime.tools.levelselectioneditor.points.Point;
import com.ktar5.slime.tools.levelselectioneditor.scene.Scene;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.KChangeListener;

import java.util.UUID;

public class PointSidebar extends Table {
    private Point point;

    private VisLabel x, y, data;

    private VisTextButton addPointBeforeButton;
    private VisTextButton addPointAfterButton;
    private VisTextButton deleteCurrentButton;
    private VisTextButton selectPathButton;
    private VisTextButton moveCurrentPointButton;

    public PointSidebar() {
        x = new VisLabel("xNA");
        y = new VisLabel("yNA");
        data = new VisLabel("N/A");

        selectPathButton = new VisTextButton("Select Point's Path");
        moveCurrentPointButton = new VisTextButton("Move Point");

        addPointBeforeButton = new VisTextButton("Add Point Before");
        addPointAfterButton = new VisTextButton("Add Point After");
        deleteCurrentButton = new VisTextButton("Delete Selected");

        moveCurrentPointButton.addListener(new KChangeListener((changeEvent, actor) -> {
            //TODO enable move mode (left click confirm) (something to cancel? escape)
        }));

        selectPathButton.addListener(new KChangeListener((changeEvent, actor) -> {
            Main.getInstance().mainStage.getSidebar().setEditMode(EditMode.PATH);
            Main.getInstance().mainStage.getSidebar().setPathSidebar(((PathPoint) point).getPath());
        }));

        addPointBeforeButton.addListener(new KChangeListener((changeEvent, actor) -> {
            PathPoint current = (PathPoint) point;
            Point before;
            PathPoint between = new PathPoint(current.getPath(), 0, 0);
            if (((PathPoint) point).getPrev() == null) {
                if (current.getPath().getStart() != null) {
                    before = current.getPath().getStart();
                    current.getPath().setFirstPoint(between);
                    between.setNext(current);
                    current.setPrev(between);
                } else {
                    return;
                }
            } else {
                PathPoint pathBefore = ((PathPoint) point).getPrev();
                before = pathBefore;
                between.setNext(current);
                between.setPrev(pathBefore);
                pathBefore.setNext(between);
                current.setPrev(between);
            }

            between.setX((current.getX() + before.getX()) / 2);
            between.setY((current.getY() + before.getY() / 2));

            Main.getInstance().mainStage.getSceneRenderer().getScene().setDirty(true);
        }));

        addPointAfterButton.addListener(new KChangeListener((changeEvent, actor) -> {
            PathPoint current = (PathPoint) point;
            Point after;
            PathPoint between = new PathPoint(current.getPath(), 0, 0);
            if (((PathPoint) point).getNext() == null) {
                if (current.getPath().getEnd() != null) {
                    after = current.getPath().getEnd();
                    current.getPath().setLastPoint(between);
                    between.setPrev(current);
                    current.setNext(between);
                } else {
                    return;
                }
            } else {
                PathPoint pathAfter = ((PathPoint) point).getPrev();
                after = pathAfter;
                between.setNext(pathAfter);
                between.setPrev(current);
                pathAfter.setPrev(between);
                current.setNext(between);
            }

            between.setX((current.getX() + after.getX()) / 2);
            between.setY((current.getY() + after.getY() / 2));

            Main.getInstance().mainStage.getSceneRenderer().getScene().setDirty(true);
        }));

        deleteCurrentButton.addListener(new KChangeListener((changeEvent, actor) -> {
            Scene scene = Main.getInstance().mainStage.getSceneRenderer().getScene();
            if (point instanceof PathPoint) {
                PathPoint pathPoint = (PathPoint) point;
                if (pathPoint.getPrev() == null) {
                    pathPoint.getPath().setFirstPoint(pathPoint.getNext());
                    if (pathPoint.getNext() != null) {
                        pathPoint.getNext().setPrev(null);
                    }
                }
                if (pathPoint.getNext() == null) {
                    pathPoint.getPath().setLastPoint(pathPoint.getPrev());
                    if (pathPoint.getPrev() != null) {
                        pathPoint.getPrev().setNext(null);
                    }
                }
                if (pathPoint.getNext() != null && pathPoint.getPrev() != null) {
                    pathPoint.getPrev().setNext(pathPoint.getNext());
                    pathPoint.getNext().setPrev(pathPoint.getPrev());
                }
            } else if (point instanceof ControlPoint) {
                ControlPoint cpoint = (ControlPoint) this.point;
                deleteControlPoint(scene, cpoint.getControlID(), cpoint.getPathDown());
                deleteControlPoint(scene, cpoint.getControlID(), cpoint.getPathLeft());
                deleteControlPoint(scene, cpoint.getControlID(), cpoint.getPathRight());
                deleteControlPoint(scene, cpoint.getControlID(), cpoint.getPathUp());
            } else if (point instanceof DataPoint) {
                scene.getDataPoints().remove(((DataPoint) point));
            }
        }));


        this.add(new VisLabel("X Pos:"));
        this.add(x);
        this.row();

        this.add(new VisLabel("Y Pos:"));
        this.add(y);
        this.row();

        this.add(new VisLabel("Data:"));
        this.add(data);
        this.row();

        this.row();

        this.add(addPointBeforeButton);
        this.row();
        this.add(addPointAfterButton);
        this.row();

        this.add(deleteCurrentButton);

        //TODO
        //on select point, create the 4 arrows to move it slowly in a specific direction
    }

    private void deleteControlPoint(Scene scene, UUID cpoint, UUID pathID) {
        if (pathID == null) {
            return;
        }

        Path path = scene.getPaths().get(pathID);

        if (path.getControlStart().equals(cpoint)) {
            path.setControlStart(null);
        }
        if (path.getControlEnd().equals(cpoint)) {
            path.setControlStart(null);
        }
    }

    @Override
    public void act(float delta) {
        update();
    }

    private void update() {
        if (point != null && point.isUpdated()) {
            point.updated = false;
            x.setText(point.getX());
            y.setText(point.getY());
            if (point instanceof DataPoint) {
                data.setText(((DataPoint) point).getData());
            } else {
                data.setText("N/A");
            }
        }
    }

    public void setPoint(Point point) {
        this.point = point;
        update();
        if (!(point instanceof PathPoint)) {
            selectPathButton.setDisabled(true);
            addPointAfterButton.setDisabled(true);
            addPointBeforeButton.setDisabled(true);
        } else {
            selectPathButton.setDisabled(false);
            addPointAfterButton.setDisabled(false);
            addPointBeforeButton.setDisabled(false);
        }
    }

}
