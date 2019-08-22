package com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.path;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.slime.tools.levelselectioneditor.input.Input;
import com.ktar5.slime.tools.levelselectioneditor.input.InputMode;
import com.ktar5.slime.tools.levelselectioneditor.points.ControlPoint;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.KChangeListener;
import com.ktar5.utilities.common.constants.Direction;
import lombok.Getter;

import java.util.Collection;

public class PathData extends Table {

    private VisLabel startX, startY, startDirection, startUUID, startData;
    private VisLabel endX, endY, endDirection, endUUID, endData;

    private VisLabel name;

    private VisTextButton deletePathButton;
    private VisTextButton insertPointToggleButton;
    private VisTextButton changeNameButton;
    private VisTextButton clearPointsButton;
    private VisTextButton setStartButton;
    private VisTextButton setEndButton;
    private VisTextButton backButton;

    @Getter
    private Path path;

    public PathData() {
        startX = new VisLabel("Null");
        startY = new VisLabel("Null");
        startDirection = new VisLabel("Null");
        startUUID = new VisLabel("Null");
        startData = new VisLabel("Null");
        endX = new VisLabel("Null");
        endY = new VisLabel("Null");
        endData = new VisLabel("Null");
        endDirection = new VisLabel("Null");
        endUUID = new VisLabel("Null");
        name = new VisLabel("Null");

        deletePathButton = new VisTextButton("Delete Path");
        insertPointToggleButton = new VisTextButton("Insert Point Mode");
        changeNameButton = new VisTextButton("Change Name");
        clearPointsButton = new VisTextButton("Clear All Points");
        setStartButton = new VisTextButton("Set Start Control");
        setEndButton = new VisTextButton("Set End Control");
        backButton = new VisTextButton("<-- Back");

        deletePathButton.addListener(new KChangeListener((changeEvent, actor) -> {
            Main.getInstance().mainStage.getSidebar().getPathSidebar().setToSelection();
            Main.getInstance().mainStage.getSidebar().getPathSidebar().getPathSelection().itemsChanged();
            Main.getInstance().mainStage.getSceneRenderer().getScene().getPaths().remove(path.getPathID());
            Collection<ControlPoint> values = Main.getInstance().mainStage.getSceneRenderer().getScene().getControlPoints().values();
            for (ControlPoint value : values) {
                if (path.getPathID().equals(value.getPathUp())) value.setPath(null, Direction.N);
                if (path.getPathID().equals(value.getPathRight())) value.setPath(null, Direction.E);
                if (path.getPathID().equals(value.getPathLeft())) value.setPath(null, Direction.W);
                if (path.getPathID().equals(value.getPathDown())) value.setPath(null, Direction.S);
            }
        }));

        insertPointToggleButton.addListener(new KChangeListener((changeEvent, actor) -> {
            if(setStartButton.isChecked()){
                insertPointToggleButton.setChecked(false);
                setStartButton.setChecked(false);
                setEndButton.setChecked(false);
                Input.inputMode = InputMode.NONE;
            }else{
                insertPointToggleButton.setChecked(true);
                setEndButton.setChecked(false);
                setStartButton.setChecked(false);
                Input.inputMode = InputMode.CREATE_POINT;
            }
        }));

        changeNameButton.addListener(new KChangeListener((changeEvent, actor) -> {
            //TODO create rename dialog
        }));

        clearPointsButton.addListener(new KChangeListener((changeEvent, actor) -> {
            path.setFirstPoint(null);
            path.setLastPoint(null);
        }));

        setStartButton.addListener(new KChangeListener((changeEvent, actor) -> {
            if(setStartButton.isChecked()){
                setStartButton.setChecked(false);
                setEndButton.setChecked(false);
                insertPointToggleButton.setChecked(false);
                Input.inputMode = InputMode.NONE;
            }else{
                setStartButton.setChecked(true);
                setEndButton.setChecked(false);
                insertPointToggleButton.setChecked(false);
                Input.inputMode = InputMode.SELECT_START;
            }
        }));

        setEndButton.addListener(new KChangeListener((changeEvent, actor) -> {
            if(setEndButton.isChecked()){
                setEndButton.setChecked(false);
                setStartButton.setChecked(false);
                insertPointToggleButton.setChecked(false);
                Input.inputMode = InputMode.NONE;
            }else{
                setEndButton.setChecked(true);
                setStartButton.setChecked(false);
                insertPointToggleButton.setChecked(false);
                Input.inputMode = InputMode.SELECT_END;
            }
        }));

        backButton.addListener(new KChangeListener((changeEvent, actor) -> {
            Main.getInstance().mainStage.getSidebar().getPathSidebar().setToSelection();
        }));

        this.add(backButton);
        this.row();
        this.add(new VisLabel("Path Name:"));
        this.add(name);
        this.row();

        this.add(new VisLabel("Start X:"));
        this.add(startX);
        this.row();

        this.add(new VisLabel("Start Y:"));
        this.add(startY);
        this.row();

        this.add(new VisLabel("Start Dir:"));
        this.add(startDirection);
        this.row();

        this.add(new VisLabel("Start ID:"));
        this.add(startUUID);
        this.row();

        this.add(new VisLabel("Start Data"));
        this.add(startData);
        this.row();


        this.add(new VisLabel("End X:"));
        this.add(endX);
        this.row();

        this.add(new VisLabel("End Y:"));
        this.add(endY);
        this.row();

        this.add(new VisLabel("End Dir:"));
        this.add(endDirection);
        this.row();

        this.add(new VisLabel("End ID:"));
        this.add(endUUID);
        this.row();

        this.add(new VisLabel("End Data"));
        this.add(endData);
        this.row();

        this.add(insertPointToggleButton);
        this.row();
        this.add(changeNameButton);
        this.row();
        this.add(setStartButton);
        this.row();
        this.add(setEndButton);

        this.row();
        this.row();
        this.add(deletePathButton);
        this.row();
        this.add(clearPointsButton);

    }


    @Override
    public void act(float delta) {
        update();
    }

    private void update() {
        if (path != null && path.isUpdated()) {
            path.updated = false;
            if (path.getControlStart() == null) {
                startX.setText("Null");
                startY.setText("Null");
                startDirection.setText("Null");
                startUUID.setText("Null");
                startData.setText("Null");
            } else {
                ControlPoint start = path.getStart();
                startX.setText(start.getX());
                startY.setText(start.getY());
                if (path.getPathID().equals(start.getPathDown())) startDirection.setText("Down");
                else if (path.getPathID().equals(start.getPathLeft())) startDirection.setText("Left");
                else if (path.getPathID().equals(start.getPathRight())) startDirection.setText("Right");
                else if (path.getPathID().equals(start.getPathUp())) startDirection.setText("Up");

                startUUID.setText(start.getControlID().toString());
                startData.setText(start.getData());
            }
            if (path.getControlEnd() == null) {
                endX.setText("Null");
                endY.setText("Null");
                endDirection.setText("Null");
                endUUID.setText("Null");
                endData.setText("Null");
            } else {
                ControlPoint end = path.getEnd();
                endX.setText(end.getX());
                endY.setText(end.getY());
                if (path.getPathID().equals(end.getPathDown())) endDirection.setText("Down");
                else if (path.getPathID().equals(end.getPathLeft())) endDirection.setText("Left");
                else if (path.getPathID().equals(end.getPathRight())) endDirection.setText("Right");
                else if (path.getPathID().equals(end.getPathUp())) endDirection.setText("Up");

                endUUID.setText(end.getControlID().toString());
                endData.setText(end.getData());
            }

            name.setText(path.getName());
        }
    }

    public void setPath(Path path) {
        this.path = path;
        update();
        insertPointToggleButton.setChecked(false);
    }

}
