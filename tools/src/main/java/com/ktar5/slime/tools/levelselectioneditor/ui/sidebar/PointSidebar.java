package com.ktar5.slime.tools.levelselectioneditor.ui.sidebar;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.slime.tools.levelselectioneditor.input.Input;
import com.ktar5.slime.tools.levelselectioneditor.input.InputMode;
import com.ktar5.slime.tools.levelselectioneditor.points.*;
import com.ktar5.slime.tools.levelselectioneditor.scene.Scene;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.InputDialog;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.KChangeListener;
import lombok.Getter;

import java.util.UUID;

public class PointSidebar extends Table {
    @Getter private Point point;

    private VisLabel x, y, data;

    private VisTextButton addPointBeforeButton;
    private VisTextButton addPointAfterButton;
    private VisTextButton deleteCurrentButton;
    private VisTextButton selectPathButton;
    private VisTextButton moveCurrentPointButton;
    private VisTextButton createControlPoint;
    private VisTextButton editData;

    public PointSidebar() {
        x = new VisLabel("xNA");
        y = new VisLabel("yNA");
        data = new VisLabel("N/A");

        selectPathButton = new VisTextButton("Select Point's Path");
        moveCurrentPointButton = new VisTextButton("Move Point");
        moveCurrentPointButton.setProgrammaticChangeEvents(false);
        addPointBeforeButton = new VisTextButton("Add Point Before");
        addPointAfterButton = new VisTextButton("Add Point After");
        createControlPoint = new VisTextButton("Create Control Point");
        editData = new VisTextButton("Edit Data");
        deleteCurrentButton = new VisTextButton("Delete Selected");

        createControlPoint.addListener(new KChangeListener((changeEvent, actor) -> {
            if(createControlPoint.isChecked()){
                Input.inputMode = InputMode.CREATE_CONTROL_POINT;
            }else{
                Input.inputMode = InputMode.NONE;
            }
        }));

        editData.addListener(new KChangeListener((changeEvent, actor) -> {
            if(point == null){
                return;
            }
            InputDialog enter_control_point_data = new InputDialog("Enter Control Point Data", "Data: ", new InputDialogAdapter() {
                @Override
                public void finished(String input) {
                    ((DataHaver) point).setData(input);
                    point.updated = true;
                }
            });
            enter_control_point_data.setText(((DataHaver) point).getData());
            getStage().addActor(enter_control_point_data.fadeIn());
        }));

        moveCurrentPointButton.addListener(new KChangeListener((changeEvent, actor) -> {
            if(moveCurrentPointButton.isChecked()){
                Input.inputMode = InputMode.MOVE_POINT;
            }else{
                Input.inputMode = InputMode.NONE;
            }
        }));

        selectPathButton.addListener(new KChangeListener((changeEvent, actor) -> {
            Main.getInstance().mainStage.getSidebar().setEditMode(EditMode.PATH);
            Main.getInstance().mainStage.getSidebar().setPathSidebar();
            Main.getInstance().mainStage.getSidebar().getPathSidebar().setToPathData(((PathPoint) point).getPath());
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
                Main.getInstance().mainStage.getSceneRenderer().getScene().getControlPoints().remove(((ControlPoint) point).getControlID());
            } else if (point instanceof DataPoint) {
                scene.getDataPoints().remove(((DataPoint) point));
            }
        }));

        Table dataTable = new Table();
        dataTable.left();

        dataTable.add(new VisLabel("X Pos:")).padRight(15);
        dataTable.add(x);
        dataTable.row();

        dataTable.add(new VisLabel("Y Pos:")).padRight(15);
        dataTable.add(y);
        dataTable.row();

        dataTable.add(new VisLabel("Data:")).padRight(15);
        dataTable.add(data);
        dataTable.row();

        this.add(dataTable);
        this.row().row();

        this.add(selectPathButton);
        this.row();
        this.add(moveCurrentPointButton);
        this.row();
        this.add(addPointBeforeButton);
        this.row();
        this.add(addPointAfterButton);
        this.row();
        this.add(editData);
        this.row();
        this.add(createControlPoint);
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

        if (path.getControlStart() != null && path.getControlStart().equals(cpoint)) {
            path.setControlStart(null, null, false);
        }
        if (path.getControlEnd() != null && path.getControlEnd().equals(cpoint)) {
            path.setControlStart(null, null, false);
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
            if (point instanceof DataHaver) {
                String data = ((DataHaver) point).getData();
                if(data.length() > 15){
                    data = data.substring(0, 13).concat(" ...");
                }
                this.data.setText(data);
            } else {
                data.setText("N/A");
            }
        }
    }

    public void setPoint(Point point) {
        if(this.point != null && this.point.equals(point)){
            return;
        }
        this.point = point;
        if(this.point != null){
            this.point.setUpdated(true);
        }
        update();
        if (!(point instanceof PathPoint)) {
            selectPathButton.setDisabled(true);
            addPointAfterButton.setDisabled(true);
            addPointBeforeButton.setDisabled(true);
            editData.setDisabled(false);
        } else {
            editData.setDisabled(true);
            selectPathButton.setDisabled(false);
            addPointAfterButton.setDisabled(false);
            addPointBeforeButton.setDisabled(false);
        }
    }

}
