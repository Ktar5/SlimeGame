package com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.path;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kotcrab.vis.ui.util.adapter.ListAdapter;
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

    private Label startX, startY, startDirection, startUUID, startData;
    private Label endX, endY, endDirection, endUUID, endData;

    private Label name;

    private TextButton deletePathButton;
    private TextButton insertPointToggleButton;
    private TextButton changeNameButton;
    private TextButton clearPointsButton;
    private TextButton setStartButton;
    private TextButton setEndButton;
    private TextButton backButton;

    @Getter
    private Path path;

    public PathData() {
        startX = new Label("Null", Main.skin);
        startY = new Label("Null", Main.skin);
        startDirection = new Label("Null", Main.skin);
        startUUID = new Label("Null", Main.skin);
        startData = new Label("Null", Main.skin);
        endX = new Label("Null", Main.skin);
        endY = new Label("Null", Main.skin);
        endData = new Label("Null", Main.skin);
        endDirection = new Label("Null", Main.skin);
        endUUID = new Label("Null", Main.skin);
        name = new Label("Null", Main.skin);

        deletePathButton = new TextButton("Delete Path", Main.skin);
        insertPointToggleButton = new TextButton("Insert Point Mode", Main.skin, "toggle");
        changeNameButton = new TextButton("Change Name", Main.skin);
        clearPointsButton = new TextButton("Clear All Points", Main.skin);
        setStartButton = new TextButton("Set Start Control", Main.skin, "toggle");

        setEndButton = new TextButton("Set End Control", Main.skin, "toggle");
        backButton = new TextButton("<-- Back", Main.skin);

        deletePathButton.addListener(new KChangeListener((changeEvent, actor) -> {
            Main.getInstance().mainStage.getSidebar().getPathSidebar().setToSelection();
            Main.getInstance().mainStage.getSidebar().getPathSidebar().getPathSelection().itemsChanged();
            Collection<ControlPoint> values = Main.getInstance().mainStage.getSceneRenderer().getScene().getControlPoints().values();
            for (ControlPoint value : values) {
                if (path.getPathID().equals(value.getPathUp())) value.setPath(null, Direction.N);
                if (path.getPathID().equals(value.getPathRight())) value.setPath(null, Direction.E);
                if (path.getPathID().equals(value.getPathLeft())) value.setPath(null, Direction.W);
                if (path.getPathID().equals(value.getPathDown())) value.setPath(null, Direction.S);
            }
            Main.getInstance().mainStage.getSceneRenderer().getScene().getPaths().remove(path.getPathID());
            ListAdapter<Path> adapter = Main.getInstance().mainStage.getSidebar().getPathSidebar().getPathSelection().getAdapter();
            ((Adapter) adapter).remove(path);
            ((Adapter) adapter).itemsChanged();
        }));

        insertPointToggleButton.addListener(new KChangeListener((changeEvent, actor) -> {
            if (insertPointToggleButton.isChecked()) {
                Input.inputMode = InputMode.CREATE_POINT;
            } else {
                Input.inputMode = InputMode.NONE;
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
            if (setStartButton.isChecked()) {
                Input.inputMode = InputMode.SELECT_START;
            } else {
                Input.inputMode = InputMode.NONE;
            }
        }));


        setEndButton.addListener(new KChangeListener((changeEvent, actor) -> {
            if (setEndButton.isChecked()) {
                Input.inputMode = InputMode.SELECT_END;
            } else {
                Input.inputMode = InputMode.NONE;
            }
        }));

        backButton.addListener(new KChangeListener((changeEvent, actor) -> {
            Main.getInstance().mainStage.getSidebar().getPathSidebar().setToSelection();
        }));

        Table dataTable = new Table();
        this.debugAll();

        this.add(backButton).fillX().row();

        dataTable.row();
        dataTable.add(new Label("Path Name:", Main.skin));
        dataTable.add(name);
        dataTable.row();

        dataTable.add(new Label("Start X:", Main.skin));
        dataTable.add(startX);
        dataTable.row();

        dataTable.add(new Label("Start Y:", Main.skin));
        dataTable.add(startY);
        dataTable.row();

        dataTable.add(new Label("Start Dir:", Main.skin));
        dataTable.add(startDirection);
        dataTable.row();

        dataTable.add(new Label("Start ID:", Main.skin));
        dataTable.add(startUUID);
        dataTable.row();

        dataTable.add(new Label("Start Data", Main.skin));
        dataTable.add(startData);
        dataTable.row();


        dataTable.add(new Label("End X:", Main.skin));
        dataTable.add(endX);
        dataTable.row();

        dataTable.add(new Label("End Y:", Main.skin));
        dataTable.add(endY);
        dataTable.row();

        dataTable.add(new Label("End Dir:", Main.skin));
        dataTable.add(endDirection);
        dataTable.row();

        dataTable.add(new Label("End ID:", Main.skin));
        dataTable.add(endUUID);
        dataTable.row();

        dataTable.add(new Label("End Data", Main.skin));
        dataTable.add(endData);
        dataTable.row();
        this.add(dataTable).grow().row();

        this.add(insertPointToggleButton).fillX();
        this.row();
        this.add(changeNameButton).fillX();
        this.row();
        this.add(setStartButton).fillX();
        this.row();
        this.add(setEndButton).fillX();

        this.row();
        this.row();
        this.add(deletePathButton).fillX();
        this.row();
        this.add(clearPointsButton).fillX();
    }


    @Override
    public void act(float delta) {
        super.act(delta);
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

//                startUUID.setText(start.getControlID().toString());
                startData.setText(start.getData());
            }
            if (path.getControlEnd() == null || path.getEnd() == null) {
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

//                endUUID.setText(end.getControlID().toString());
                endData.setText(end.getData());
            }

            name.setText(path.getName());
        }
    }

    public void setPath(Path path) {
        this.path = path;
        path.updated = true;
        update();
        insertPointToggleButton.setChecked(false);
    }

}
