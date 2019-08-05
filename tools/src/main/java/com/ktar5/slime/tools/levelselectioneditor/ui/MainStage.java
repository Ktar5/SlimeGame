package com.ktar5.slime.tools.levelselectioneditor.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.slime.tools.levelselectioneditor.points.ControlPoint;
import com.ktar5.slime.tools.levelselectioneditor.scene.Scene;
import com.ktar5.slime.tools.levelselectioneditor.scene.SceneRenderer;
import com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.Sidebar;
import com.ktar5.slime.tools.levelselectioneditor.ui.topmenu.TopMenu;
import lombok.Getter;

import java.io.File;

@Getter
public class MainStage extends Stage {
    public InputMode inputMode = InputMode.NONE;

    private FileChooser fileChooser;

    private Sidebar sidebar;
    private SceneRenderer sceneRenderer;

    public MainStage() {
        super(new ScreenViewport(), new SpriteBatch());

        VisUI.load();

        Gdx.input.setInputProcessor(this);

        FileChooser.setDefaultPrefsName("com.ktar5.tools.levelselectioneditor.filepicker");
        fileChooser = new FileChooser(FileChooser.Mode.OPEN);

        Table mainTable = new Table();
        mainTable.debugAll();

        mainTable.setFillParent(true);
        TopMenu topMenu = new TopMenu();
        mainTable.add(topMenu.getTable()).fillX().expandX();
        mainTable.top();
        mainTable.row();

        Table belowMenuTable = new Table();

        //Create sidebar
        sidebar = new Sidebar();
        //Add sidebar to main table
        belowMenuTable.add(sidebar).width(200).growY();

        Table editorTable = new Table();
        sceneRenderer = new SceneRenderer();
        editorTable.add(sceneRenderer).grow();

        //todo TESTING
        this.setScene(createScene());
        //todo TESTING

        belowMenuTable.add(editorTable).grow();

        mainTable.add(belowMenuTable).grow();

        this.addActor(mainTable);
    }

    public Scene createScene() {
        File saveFile = new File("C:\\Users\\Carter\\Desktop\\SCENETEST.json");
        File textureFile = new File("C:\\Users\\Carter\\Desktop\\andreas1.png");
        Scene scene = new Scene("test1", saveFile, textureFile);

        Path path = new Path(scene, "path1");

        ControlPoint start1 = new ControlPoint("start_one", 0, 0);
        start1.setPathUp(path.getPathID());
        ControlPoint end1 = new ControlPoint("end_one", 150, 150);
        end1.setPathDown(path.getPathID());

        scene.getControlPoints().put(start1.getControlID(), start1);
        scene.getControlPoints().put(end1.getControlID(), end1);

        path.setControlStart(start1.getControlID());
        path.setControlEnd(end1.getControlID());

        scene.getPaths().put(path.getPathID(), path);
        return scene;
    }

    public void setScene(Scene scene) {
        sceneRenderer.setScene(scene);
    }

}
