package com.ktar5.slime.tools.levelselectioneditor.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.slime.tools.levelselectioneditor.points.ControlPoint;
import com.ktar5.slime.tools.levelselectioneditor.scene.Scene;
import com.ktar5.slime.tools.levelselectioneditor.scene.SceneRenderer;
import com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.ContextMenu;
import com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.DataSidebar;
import com.ktar5.slime.tools.levelselectioneditor.ui.topmenu.TopMenu;
import lombok.Getter;

import java.io.File;

@Getter
public class MainScene extends Stage {
    private Table mainTable;
    private Table editorTable;

    private SceneRenderer sceneRenderer;

    private FileChooser fileChooser;

    //hotkeys

    public MainScene() {
        super(new ScreenViewport(), new SpriteBatch());

        VisUI.load();

        Gdx.input.setInputProcessor(this);

        FileChooser.setDefaultPrefsName("com.ktar5.tools.scene.filepicker");
        fileChooser = new FileChooser(FileChooser.Mode.OPEN);

        mainTable = new Table();
        mainTable.debugAll();

        mainTable.setFillParent(true);
        TopMenu topMenu = new TopMenu();
        mainTable.add(topMenu.getTable()).fillX().expandX();
        mainTable.top();
        mainTable.row();

        Table belowMenuTable = new Table();

        Table sidebarTable = new Table();
        sidebarTable.add(new ContextMenu());
        sidebarTable.row();
        sidebarTable.add(new DataSidebar());

        belowMenuTable.add(sidebarTable).width(200);

        editorTable = new Table();
        sceneRenderer = new SceneRenderer();
        editorTable.add(sceneRenderer).grow();

        //TESTING
        this.setScene(createScene());
        //TESTING

        belowMenuTable.add(editorTable).grow();

        mainTable.add(belowMenuTable).grow();

        this.addActor(mainTable);
    }

    public Scene createScene() {
        File saveFile = new File("C:\\Users\\Carter\\Desktop\\SCENETEST.json");
        File textureFile = new File("C:\\Users\\Carter\\Desktop\\andreas1.png");
        Scene scene = new Scene("test1", saveFile, textureFile);

        ControlPoint start1 = new ControlPoint("start_one", 1, 0, 0);
        ControlPoint end1 = new ControlPoint("end_one", 2, 150, 150);

        Vector2 vector2 = new Vector2(start1.getX(), start1.getY());

        scene.getControlPoints().put(start1.getControlID(), start1);
        scene.getControlPoints().put(end1.getControlID(), end1);

        Path path = new Path(scene, "path1");
        path.setControlStart(start1.getControlID());
        path.setControlEnd(end1.getControlID());

        scene.getPaths().put(path.getPathID(), path);
        return scene;
    }

    public void setScene(Scene scene) {
        sceneRenderer.setScene(scene);
    }

}
